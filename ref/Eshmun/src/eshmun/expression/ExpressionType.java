package eshmun.expression;

/**
 * This enumerator contains the possible types of an expression.
 * 
 * @author Kinan Dak Al Bab
 * @since 1.0
 */
public enum ExpressionType {
	BooleanVariable, BooleanLiteral,
	NotOperator, AndOperator, OrOperator,
	ImplicationOperator, EquivalenceOperator,
	AFModality, AGModality, AXModality, EGModality, EFModality, EXModality,
	AUModality, AWModality, AVModality, EUModality, EWModality, EVModality
}
