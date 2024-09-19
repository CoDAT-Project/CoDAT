package com.au.csci7130.plugin;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiRecursiveElementVisitor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiSearchHelper;
import com.intellij.util.Processor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CommentLinkAnnotationMarkerProviderV2 implements LineMarkerProvider {

    private static final Pattern PATTERN = Pattern.compile("@LinkAnnotation\\s+(\\w+)"); 
                  // \s+ = one or more spaces, \w+ = one or more words

    @Nullable
    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {
        // No changes here, as per the contract of the LineMarkerProvider interface.
        return null; //not using this, so return null
    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<? extends PsiElement> elements, @NotNull Collection<? super LineMarkerInfo<?>> result) {
        //Effects: generates markup for comment gutter

        if (elements.isEmpty()) return;

        PsiElement anyElement = elements.get(0); // Get any element to extract the project for scope (project in every element)
        PsiSearchHelper searchHelper = PsiSearchHelper.getInstance(anyElement.getProject()); 
        //PsiSearchHelper is a class, PsiSearchHelper.getInstance is a method that creates an instance of PsiSearchHelper, which
        //is a utility class, i.e., PsiSearchHelper is a class name
        Map<String, List<PsiComment>> annotationsMap = new HashMap<>(); //maps comment annotation (a string) to the List<PsiComment> object 
        //which contains the actual comment

        searchHelper.processAllFilesWithWordInComments("@LinkAnnotation", GlobalSearchScope.allScope(anyElement.getProject()), file -> {
            // Now process each file that contains the @LinkAnnotation in comments
            processFileForComments(file, annotationsMap); //execute this code on each file that is found
            return true; // Return true to continue processing other files.
        });

        linkCommentsAcrossProject(annotationsMap, result);
    }

    private void processFileForComments(PsiFile file, Map<String, List<PsiComment>> annotationsMap) {
        //Effects: finds all the comments and inserts into the map
        //Sketch:  Use PsiFile's accept method with a PsiRecursiveElementVisitor to find comments
        file.accept(new PsiRecursiveElementVisitor(true) {

            public void visitComment(PsiComment comment) {
                super.visitComment(comment);
                Matcher matcher = PATTERN.matcher(comment.getText());
                if (matcher.find()) {
                    String annotationName = matcher.group(1);
                    annotationsMap.computeIfAbsent(annotationName, k -> new ArrayList<>()).add(comment);
                }
            }
        });
    }

    private void linkCommentsAcrossProject(Map<String, List<PsiComment>> annotationsMap, Collection<? super LineMarkerInfo<?>> result) {
        //Effects: iterates and links all corresponding comments together
        // Logic to link comments remains the same
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

    // Additional helper classes like PsiRecursiveElementVisitor may be needed here.
}
