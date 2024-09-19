# Overview

Program design and commenting is an essential part in program development. It is trivial to convert design specifications to code whereas translating uncommented code to a design specification is incredibly difficult and time consuming.

# CommentLinkAnnotationMarkerProviderV3

```java
package com.au.csci7130.plugin;

// Line markers help annotate code with icons on the gutter. These markers can provide navigation targets to related code.
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;

// Implements the line marker
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;

// Provides a series of icons from which the plugin can choose to use
import com.intellij.icons.AllIcons;

/*
An OpenAPI Specification (OAS) is a description format for REST APIs. Swagger is a set of tools 
based on this specification for writing, documenting, and consuming REST APIs. 
For more information, refer to Swagger documentation.

IntelliJ IDEA provides coding assistance for OpenAPI definitions in YAML and JSON files, 
and integration with Swagger Codegen for generating server stubs, 
client libraries (SDKs), and documentation based on your OpenAPI specification.

Source: https://www.jetbrains.com/help/idea/openapi.html
*/
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;

/*
A PSI (Program Structure Interface) file represents a hierarchy of PSI elements (so-called PSI trees). 
A single PSI file (itself being a PSI element) may expose several PSI trees in specific programming 
languages (see File View Providers). A PSI element, in its turn, can have child PSI elements.

PSI elements and operations at the level of individual PSI elements are used to explore the source 
code's internal structure as it is interpreted by the IntelliJ Platform. For example, 
you can use PSI elements to perform code analysis, such as code inspections or intention actions

Source: https://plugins.jetbrains.com/docs/intellij/psi-elements.html

For example, PsiComment represents a comment in code
*/
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiSearchHelper;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.SmartPsiElementPointer;

/*
JetBrains.Annotations help reduce false positive warnings, explicitly declare purity 
and nullability in your code, deal with implicit usages of members, 
support special semantics of APIs in ASP.NET and XAML frameworks and otherwise 
increase accuracy of JetBrains Rider and ReSharper code inspections.

Source: https://www.nuget.org/packages/JetBrains.Annotations/
*/
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
The java.util package contains the collections framework, legacy collection classes, event model, 
date and time facilities, internationalization, 
and miscellaneous utility classes (a string tokenizer, a random-number generator, and a bit array).

Source: https://docs.oracle.com/javase/8/docs/api/java/util/package-summary.html
*/
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/*
==============================================================================================
Program Design
==============================================================================================

The Code Helper and Comment Tracker, or CHCT, is a tool designed to be used as an interface between 
the user and a backend machine learning engine. The CHCT will assist the user in assessing 
highlighted code or comments for improvements or viability. 

The CHCT will accomplish the following objectives:

1. Provide the user with feedback on suggest code implementations based on a highlighted comment.
2. Track comments and modify them across a file using a root file to hold all comments.

This product would be useful in helping software developers develop reliable code through code suggestions and comment association. 

The intended audience for this program would be software developers and engineers.

==============================================================================================
Program Data Model
==============================================================================================

A single AnnotationName is mapped by the AnnotationService to a single list of a PSIComment. 
The list of PSIComments is equivalent to one or more List Entries where a List Entry is a comment of type 
PSIComment and a Position delineated by a natural number. 

* <HashMap> AnnotationService: AnnotationName -> List of PsiComments
* <String> AnnotationName -> Instance of a RegEx
* <Object> PsiComment -> PSI representation of a comment where PSI provides "comment" as a program structure.

==============================================================================================
Program Dependencies
==============================================================================================

The AI implementation will provide a JSON response from a JSON request originating from the plugin. 

*/

public class CommentLinkAnnotationMarkerProviderV3 implements LineMarkerProvider {

    /*
        The pattern "@LinkAnnotation\\s+(\\w+)" can be broken down as follows: 

        \s+ is one or more spaces
        \w+ is one or more words

        Thus, the comment \\@LinkAnnotation is located in a file, then one or more spaces is 
    */
    final Pattern PATTERN = Pattern.compile("@LinkAnnotation\\s+(\\w+)");

    /*
        getLineMarkerInfo is a required function that needs to return null if not used by the plugin.
    */
    @Nullable
    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {
        return null; // Method unchanged.
    }

    /*
        collectSlowLineMarkers gathers the line markers (comments) that contain the @LinkAnnotation signifier.
    */
    @Override
    public void collectSlowLineMarkers(@NotNull List<? extends PsiElement> elements, @NotNull Collection<? super LineMarkerInfo<?>> result) {
        
        // If the provided elements list is empty, the program simply returns.
        if (elements.isEmpty()) return;

        // The first value within the elements list is the project identifier which is then obtained via the getProject() function.
        Project project = elements.get(0).getProject();

        // The annotationService variable then obtains the instance of the project provided in the above variable.
        LinkAnnotationService annotationService = LinkAnnotationService.getInstance(project);

        /*
        The searchHelper variable initializes the PsiSearchHelper object based on the instance of the project provided.

        The PsiSearchHelper provides low-level search and find usages services for a project, 
        like finding references to an element, finding overriding / inheriting elements, 
        finding to do items and so on. Use getInstance(Project) to get a search helper instance.

        Source: https://dploeger.github.io/intellij-api-doc/com/intellij/psi/search/PsiSearchHelper.html
        */
        PsiSearchHelper searchHelper = PsiSearchHelper.getInstance(project);

        // The scope variable parses the project instance to then cache all PSI Objects within the project 
        GlobalSearchScope scope = GlobalSearchScope.allScope(project);

        /*
            searchHelper.processAllFilesWithWOrdInComments searches through Java files, 
            looks for comments containing @LinkAnnotation, extracts annotation names from these comments, 
            and then adds this information to some service (presumably for further processing or storage).

            Note: "->" is a lambda expression, indicating that the code within 
            the curly braces is executed for each file found matching the search criteria.
        */
        searchHelper.processAllFilesWithWordInComments("@LinkAnnotation", scope, psiFile -> {

            /* 
                The file variable casts psiFile to a PsiFile. 
                PsiFile is a representation of a file in the PSI (Program Structure Interface) tree, 
                which is a part of the IntelliJ Platform SDK for working with Java and other languages.
            */
            PsiFile file = (PsiFile) psiFile;

            /*
                The comments variable finds all comments within the current file. It uses PsiTreeUtil.
                findChildrenOfType(...) to traverse the PSI tree and locate all instances of PsiComment, 
                which represent comments in Java code.
            */
            Collection<PsiComment> comments = PsiTreeUtil.findChildrenOfType(file, PsiComment.class);

            // The for loop iterates over each comment found in the file.
            for (PsiComment comment : comments) {

                /*
                    The matcher variable creates a Matcher object using a pre-defined PATTERN. 
                    This pattern is a regular expression used to extract specific information from comments.
                */
                Matcher matcher = PATTERN.matcher(comment.getText());

                // This if condition checks if the pattern matches the text of the comment.
                if (matcher.find()) {

                    /*
                        The annotationName variable extracts the first capturing group from the match. 
                        This is done using matcher.group(1).
                    */
                    String annotationName = matcher.group(1);

                    /*
                        annotationService.addAnnotation adds the annotation name and the 
                        corresponding comment to some annotationService; 
                        storing information about annotations found in comments.
                    */
                    annotationService.addAnnotation(annotationName, comment);
                }
            }
            return true;
        });

        // Use the LinkAnnotationService to link comments across the project.
        Map<String, List<PsiComment>> annotationsMap = new HashMap<>();

        /*
            Populate annotationsMap from LinkAnnotationService for linking. 

            .map(SmartPsiElementPointer::getElement) Converts each SmartPsiElementPointer to PsiComment

            .filter(Objects::nonNull) Filters out any null references (which indicate invalidated elements)
        */
        annotationService.linkedAnnotations.forEach((annotationName, pointerList) -> {
            List<PsiComment> comments = pointerList.stream()
                    .map(SmartPsiElementPointer::getElement)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            
            // Add the list of valid PsiComment objects to the annotationsMap
            annotationsMap.put(annotationName, comments); 
        });
        linkCommentsAcrossProject(annotationsMap, result, project);
    }

    /*
        linkCommentsAcrossProject method iterates over annotations mapped to lists of 
        comments in a project, and for each annotation with more than one associated comment, 
        it creates navigation gutter icons for each comment. 
        
        These icons are then associated with a list of targets (other comments) and added to 
        the result collection for further processing.
    */
    private void linkCommentsAcrossProject(Map<String, List<PsiComment>> annotationsMap, Collection<? super LineMarkerInfo<?>> result, Project project) {

        // Only link comments if there's more than one with the same annotation
        annotationsMap.forEach((annotationName, comments) -> {

            /* 
                This condition checks if there's more than one comment associated with the current annotation. 
                It ensures that comments are only linked if there's more than one comment with the same annotation.
            */
            if (comments.size() > 1) {

                /*
                    The targets variable creates a new list called targets and initializes it with the contents of the 
                    comments list associated with the current annotation. 
                    
                    It seems to be preparing a list of elements that the navigation gutter icon should target.
                */
                List<PsiElement> targets = new ArrayList<>(comments);

                // This iterates over each comment in the list of comments associated with the current annotation.
                comments.forEach(comment -> {

                    /* 
                        The builder variable creates a new NavigationGutterIconBuilder instance for the given comment. 
                        It sets an icon (e.g., AllIcons.Gutter.ImplementedMethod) to represent the navigation gutter icon. 
                    
                        .setTargets(targets) targets for the gutter icon builder. 
                        It associates the gutter icon with the list of targets created earlier. 
                    
                        .setTooltipText("Navigate to linked comments with @" + annotationName) is where the tooltip text 
                        is set for the gutter icon. It indicates that the gutter icon will navigate to linked comments 
                        with the specific annotation name.
                    */
                    NavigationGutterIconBuilder<PsiElement> builder =
                            NavigationGutterIconBuilder.create(AllIcons.Gutter.ImplementedMethod)
                                    .setTargets(targets)
                                    .setTooltipText("Navigate to linked comments with @" + annotationName);
                                    
                    /*
                        Then, the line marker information created by the builder for the current 
                        comment to the result collection. 
                        
                        Here, the result is a collection to store line marker information.
                    */
                    result.add(builder.createLineMarkerInfo(comment));
                });
            }
        });
    }

}

```