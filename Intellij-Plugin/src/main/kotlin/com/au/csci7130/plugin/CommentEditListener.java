package com.au.csci7130.plugin;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommentEditListener implements DocumentListener {
    private final Project project;
    private static final Pattern PATTERN = Pattern.compile("@LinkAnnotation\\s+(\\w+)");


    public CommentEditListener(Project project) {
        this.project = project;
    }

    @Override
    public void documentChanged(DocumentEvent e) {
        Document document = e.getDocument();
        PsiDocumentManager.getInstance(project).commitDocument(document);
        PsiFile file = PsiDocumentManager.getInstance(project).getPsiFile(document);

        if (file == null) return;

        PsiElement myEditedElement = file.findElementAt(e.getOffset());
        if  (!(myEditedElement instanceof PsiComment editedComment)) {
            return;
        }

        String text = editedComment.getText();
        int editedCommentOffset = editedComment.getTextOffset();
        Matcher matcher = PATTERN.matcher(text);
        if (matcher.find()) {
            String annotationName = matcher.group(1);

            String newText = editedComment.getText();
            LinkAnnotationService.getInstance(project).updateLinkedComments(annotationName, newText,editedCommentOffset,project);
        }
    }
}
