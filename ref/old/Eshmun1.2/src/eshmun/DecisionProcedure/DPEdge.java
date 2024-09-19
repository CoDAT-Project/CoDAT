package eshmun.DecisionProcedure;

public class DPEdge {
	
	private DPVertex vFrom;
	private DPVertex vTo;
	private String Name ;

	public DPVertex getvFrom() {
		return vFrom;
	}

	public void setvFrom(DPVertex vFrom) {
		this.vFrom = vFrom;
	}

	public DPVertex getvTo() {
		return vTo;
	}

	public void setvTo(DPVertex vTo) {
		this.vTo = vTo;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((vFrom == null) ? 0 : vFrom.hashCode());
		result = prime * result + ((vTo == null) ? 0 : vTo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DPEdge other = (DPEdge) obj;
		if (vFrom == null) {
			if (other.vFrom != null)
				return false;
		} else if (!vFrom.equals(other.vFrom))
			return false;
		if (vTo == null) {
			if (other.vTo != null)
				return false;
		} else if (!vTo.equals(other.vTo))
			return false;
		return true;
	}

	public DPEdge(DPVertex from, DPVertex to) {
		vFrom = from;
		vTo = to;
		Name = from.getName() + to.getName();
	}

	

}
