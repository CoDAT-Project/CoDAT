package eshmun.expression;

import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;
import eshmun.regex.IndexReplacementContext;

public interface IExpressionVariable {
	public Object evaluate(Kripke structure, KripkeState state);
	public IExpressionVariable replaceParameters(IndexReplacementContext indexReplacementContext);
}
