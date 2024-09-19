package eshmun.structures;

/**
* <h1>AbstractTransition</h1>
*AbstractState is an abstract class parent to KripkeTransition, IOTransition and BipAbstractTransition
*
* @author  Ali
* @version 1.0
* @since   2015-04-23
*/


public abstract class AbstractTransition {
	private AbstractStructure parentStructure;
	private AbstractState from;
	private AbstractState to;
	private boolean retain;
	
	public AbstractTransition(AbstractState from, AbstractState to, AbstractStructure parentStructure) {
		this.from = from;
		this.to = to;
		this.parentStructure = parentStructure;
	}
	
	public AbstractTransition(String from, String to, AbstractStructure parentStructure) {
		this.from = parentStructure.getStatesMap().get(from);
		this.to = parentStructure.getStatesMap().get(to);
		this.parentStructure = parentStructure;
	}
	
	public AbstractStructure getParentStructure() {
		return parentStructure;
	}

	public boolean isRetain() {
		return retain;
	}

	public void setRetain(boolean retain) {
		this.retain = retain;
	}

	public AbstractState getFrom() {
		return from;
	}

	public AbstractState getTo() {
		return to;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == this) {
			return true;
		}
		if(!(o instanceof AbstractTransition)) {
			return false;
		}
		return this.from.equals(((AbstractTransition) o).getFrom()) && this.to.equals(((AbstractTransition) o).getTo());
	}
}
