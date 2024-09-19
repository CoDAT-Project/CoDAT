package com.au.csci7130.plugin;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiSearchHelper;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.psi.SmartPsiElementPointer;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CommentLinkAnnotationMarkerProviderV3 implements LineMarkerProvider {

      final Pattern PATTERN = Pattern.compile("@LinkAnnotation\\s+(\\w+)");

    @Nullable
    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {
        return null; // Method unchanged.
    }


    @Override
    public void collectSlowLineMarkers(@NotNull List<? extends PsiElement> elements, @NotNull Collection<? super LineMarkerInfo<?>> result) {
        if (elements.isEmpty()) return;

        Project project = elements.get(0).getProject();
        LinkAnnotationService annotationService = LinkAnnotationService.getInstance(project);

        PsiSearchHelper searchHelper = PsiSearchHelper.getInstance(project);
        GlobalSearchScope scope = GlobalSearchScope.allScope(project);

        searchHelper.processAllFilesWithWordInComments("@LinkAnnotation", scope, psiFile -> {
            PsiFile file = (PsiFile) psiFile;
            Collection<PsiComment> comments = PsiTreeUtil.findChildrenOfType(file, PsiComment.class);
            for (PsiComment comment : comments) {
                Matcher matcher = PATTERN.matcher(comment.getText());
                if (matcher.find()) {
                    String annotationName = matcher.group(1);
                    annotationService.addAnnotation(annotationName, comment);
                }
            }
            return true;
        });

        // Use the LinkAnnotationService to link comments across the project.
        Map<String, List<PsiComment>> annotationsMap = new HashMap<>();
        // Populate annotationsMap from LinkAnnotationService for linking
        annotationService.linkedAnnotations.forEach((annotationName, pointerList) -> {
            List<PsiComment> comments = pointerList.stream()
                    .map(SmartPsiElementPointer::getElement) // Convert each SmartPsiElementPointer to PsiComment
                    .filter(Objects::nonNull) // Filter out any null references (which indicate invalidated elements)
                    .collect(Collectors.toList());
            annotationsMap.put(annotationName, comments); // Add the list of valid PsiComment objects to the annotationsMap
        });
        linkCommentsAcrossProject(annotationsMap, result, project);
    }
    private void linkCommentsAcrossProject(Map<String, List<PsiComment>> annotationsMap, Collection<? super LineMarkerInfo<?>> result, Project project) {
        annotationsMap.forEach((annotationName, comments) -> {
            if (comments.size() > 1) { // Only link comments if there's more than one with the same annotation
                List<PsiElement> targets = new ArrayList<>(comments);
                comments.forEach(comment -> {
                    NavigationGutterIconBuilder<PsiElement> builder =
                            NavigationGutterIconBuilder.create(AllIcons.Gutter.ImplementedMethod)
                                    .setTargets(targets)
                                    .setTooltipText("Navigate to linked comments with @" + annotationName);
                    result.add(builder.createLineMarkerInfo(comment));
                });
            }
        });
    }

}
