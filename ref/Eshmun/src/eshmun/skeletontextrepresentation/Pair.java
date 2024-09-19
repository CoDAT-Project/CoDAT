package eshmun.skeletontextrepresentation;

import java.util.AbstractMap;
 
import java.util.Map;
 
/*
 * Java does not provide any implementation of a pair class. This implementation represents a key-value pair
 * based on Map.Entry<K,V> to be used for variable assignments in generateStateSpace() method.
 * 
 * chukri soueidi
 */
public class Pair
{
	// Return a map entry (key-value pair) from the specified values
	public static <T, U> Map.Entry<T, U> of(T first, U second)
	{
		return new AbstractMap.SimpleEntry<>(first, second);
	}
}