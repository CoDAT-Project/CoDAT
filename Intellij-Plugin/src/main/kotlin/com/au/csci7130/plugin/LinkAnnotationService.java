package com.au.csci7130.plugin;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;

import java.util.*;

@Service
final public class LinkAnnotationService {

     final Map<String, List<SmartPsiElementPointer<PsiComment>>> linkedAnnotations = new HashMap<>();

    // Necessary for obtaining the service instance
    public static LinkAnnotationService getInstance(Project project) {
        return project.getService(LinkAnnotationService.class);
    }

    /**
     * Adds a comment to the list of comments linked by a specific annotation.
     * If the annotation doesn't exist yet, it creates a new entry for it.
     *
     * @param annotationName The name of the annotation linking the comments.
     * @param comment        The PsiComment to add under the specified annotation.
     */
    public synchronized void addAnnotation(String annotationName, PsiComment comment) {

        SmartPsiElementPointer<PsiComment> pointer = SmartPointerManager.createPointer(comment);
        linkedAnnotations.computeIfAbsent(annotationName, k -> new ArrayList<>()).add(pointer);
    }


    /**
     * Updates the text of all comments linked by a specific annotation.
     *
     * @param annotationName The name of the annotation whose linked comments are to be updated.
     * @param newText        The new text to set for each linked comment.
     */



    // Simplified method to demonstrate updating comments with the same annotation
    // Method to update all linked comments for a given annotation name
    public void updateLinkedComments(String annotationName, String newText, int excludingCommentOffset, Project project) {
        List<SmartPsiElementPointer<PsiComment>> pointers = linkedAnnotations.getOrDefault(annotationName, Collections.emptyList());

        ApplicationManager.getApplication().invokeLater(() -> {
            WriteCommandAction.runWriteCommandAction(project, () -> {
                pointers.forEach(pointer -> {
                    PsiComment comment = pointer.getElement();
                    if (comment != null && comment.isValid() && comment.getTextRange().getStartOffset() != excludingCommentOffset) {
                        PsiElementFactory elementFactory = JavaPsiFacade.getInstance(project).getElementFactory();
                        PsiComment newComment = elementFactory.createCommentFromText(newText, null);
                        comment.replace(newComment);
                    }
                });
            });
        });
    }

}
