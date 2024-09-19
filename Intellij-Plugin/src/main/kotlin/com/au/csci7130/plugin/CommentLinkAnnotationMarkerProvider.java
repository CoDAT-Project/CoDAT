package com.au.csci7130.plugin;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CommentLinkAnnotationMarkerProvider implements LineMarkerProvider {

    private static final Pattern PATTERN = Pattern.compile("@LinkAnnotation\\s+(\\w+)");

    @Nullable
    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {
        // This method needs to return null for LineMarkerProvider to work properly.
        return null;
    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<? extends PsiElement> elements, @NotNull Collection<? super LineMarkerInfo<?>> result) {
        // Collect all comments in the file and group them by annotation name
        Map<String, List<PsiComment>> annotationsMap = new HashMap<>();
        elements.stream()
                .filter(element -> element instanceof PsiComment)
                .map(element -> (PsiComment) element)
                .forEach(comment -> {
                    Matcher matcher = PATTERN.matcher(comment.getText());
                    if (matcher.find()) {
                        String annotationName = matcher.group(1);
                        annotationsMap.computeIfAbsent(annotationName, k -> new ArrayList<>()).add(comment);
                    }
                });

        // For each group of comments with the same annotation, create line markers linking them together
        annotationsMap.values().forEach(comments -> {
            if (comments.size() > 1) { // Only link comments if there's more than one with the same annotation
                List<PsiElement> targets = comments.stream().map(comment -> (PsiElement) comment).collect(Collectors.toList());
                comments.forEach(comment -> {
                    NavigationGutterIconBuilder<PsiElement> builder =
                            NavigationGutterIconBuilder.create(AllIcons.Gutter.ImplementedMethod)
                                    .setTargets(targets)
                                    .setTooltipText("Navigate to related comments");
                    result.add(builder.createLineMarkerInfo(comment));
                });
            }
        });
    }
}
