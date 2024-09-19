
Intellij-Plugin
========


Based on Intellij Psi library


PsiComment: from the Intellij Psi library; gives a way of identifying a token type which is a comment



Annotation:

	an annotation name is a string which uniquely identifies the annotation, as in
	@LinkAnnotation

	an annotation is a list of


annotationsMap: Map<String, List<PsiComment>> 

	hash maps an annotation name (a string) to an annotation, which is a list of PsiComments
	each PsiComment being a single line
