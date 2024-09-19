package t7.types;

import java.util.Iterator;

/**
 * Represents a range (the result of a triplet).
 * @author kinan
 */
public class T7Range implements VariableType, Iterable<Integer> {
	/**
	 * The start value.
	 */
	private T7Integer start;
	
	/**
	 * The end value.
	 */
	private T7Integer end;
	
	/**
	 * The step (increment or decrement).
	 */
	private T7Integer step;
	
	/**
	 * Creates a new range.
	 * @param start the start of the range.
	 * @param end the end of the range.
	 */
	public T7Range(T7Integer start, T7Integer end) {
		this(start, end, null);
	}

	/**
	 * Creates a new range.
	 * @param start the start of the range.
	 * @param end the end of the range.
	 * @param step the step increment or decrement.
	 * @throws IllegalArgumentException in case the step doesnt match start and end.
	 */
	public T7Range(T7Integer start, T7Integer end, T7Integer step) throws IllegalArgumentException {
		this.start = start;
		this.end = end;
		
		int s = start.value();
		int e = end.value();
		int t = step == null ? ( s <= e ? 1 : -1 ) : step.value();
		if(s + t - e <= s - e && s != e) {
			throw new IllegalArgumentException("Illegal step for the given start and end of range.");
		}
		
		this.step = new T7Integer(t);
	}
	
	/**
	 * @return The start of the range.	
	 */
	public T7Integer getStart() {
		return start;
	}
	
	/**
	 * @return The end of the range.
	 */
	public T7Integer getEnd() {
		return end;
	}
	
	/**
	 * @return the step increment/decrement.
	 */
	public T7Integer getStep() {
		return step;
	}
	
	/**
	 * @return Range.
	 */
	@Override
	public Types getType() {
		return Types.Range;
	}

	/**
	 * Gets an iterator over the values in the range.
	 * @return an iterator.
	 */
	@Override
	public Iterator<Integer> iterator() {
		Iterator<Integer> iterator = new Iterator<Integer>() {
			/**
			 * The current value.
			 */
			private int current = start.value();
			
			/**
			 * Checks if the iterator has a next element.
			 */
			@Override
			public boolean hasNext() {
				return current <= T7Range.this.end.value();
			}
			
			/**
			 * Gets the value, then increment it by a step.
			 */
			@Override
			public Integer next() {
				int tmp = current;
				current += step.value();
				return tmp;
			}
			
			/**
			 * Unsupported, cannot remove elements from the range.
			 */
			@Override
			public void remove() {
				throw new UnsupportedOperationException("Cannot remove elements from range.");
			}
		};
		
		return iterator;
	}
	
	/**
	 * Clones this variable.
	 * @return a clone of this variable.
	 */
	@Override
	public T7Range clone() {
		return new T7Range(start, end, step);
	}
	

	/**
	 * Useful for displaying comments to user.
	 * @return The String representation of this type.
	 */
	public String toString() {
		String values = "";
		Iterator<Integer> iterator = iterator();
		while(iterator.hasNext()) {
			values += iterator.next()+", ";
		}
		
		return values.substring(0, values.lastIndexOf(", "));
	}
}
