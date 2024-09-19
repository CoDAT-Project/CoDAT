package com.au.csci7130.plugin;

import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import org.jetbrains.annotations.NotNull;

public class CommentLinkPluginInitializer implements StartupActivity {
    @Override
    public void runActivity(@NotNull Project project) {
        EditorFactory.getInstance().getEventMulticaster().addDocumentListener(new CommentEditListener(project), project);
    }
}
