package eshmun.skeletontextrepresentation;



import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * This is a chained comparator that is used to sort a list by multiple
 * attributes by chaining a sequence of comparators of individual fields
 * together.
 * @author www.codejava.net
 *
 */
public class StateChainedComparator implements Comparator<String> {

	private List<Comparator<String>> listComparators;

	@SafeVarargs
	public StateChainedComparator(Comparator<String>... comparators) {
		this.listComparators = Arrays.asList(comparators);
	}

	@Override
	public int compare(String emp1, String emp2) {
		for (Comparator<String> comparator : listComparators) {
			int result = comparator.compare(emp1, emp2);
			if (result != 0) {
				return result;
			}
		}
		return 0;
	}
}