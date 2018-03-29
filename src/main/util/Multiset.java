package main.util;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Can contain multiple quantities of each element.
 * 
 * @author Evan Quan
 * @version 1.3.0
 * @since 29 March, 2018
 *
 * @param <E>
 *            type of element contained in multiset
 */
public class Multiset<E> implements Iterable<E> {

	private HashMap<E, Integer> elements;

	/**
	 * Constructs an empty multiset
	 */
	public Multiset() {
		elements = new HashMap<E, Integer>();
	}

	/**
	 * Add 1 element to multiset
	 * 
	 * @param element
	 */
	public void add(E element) {
		add(element, 1);
	}

	/**
	 * Add count number of elements to multiset
	 * 
	 * @param element
	 * @param count
	 */
	public void add(E element, int count) {
		int newCount = count(element) + count;
		if (newCount > 0) {
			elements.put(element, newCount);
		} else {
			elements.remove(element);
		}
	}

	/**
	 * Get count of element in multiset
	 * 
	 * @param element
	 * @return count of element
	 */
	public int count(E element) {
		if (elements.containsKey(element)) {
			return elements.get(element);
		} else {
			return 0;
		}
	}

	/**
	 * Remove 1 element from multiset
	 * 
	 * @param element
	 */
	public void remove(E element) {
		remove(element, 1);
	}

	/**
	 * Remove count number of elements to multiset
	 * 
	 * @param element
	 * @param count
	 * @return true is elements are removed, else false
	 */
	public void remove(E element, int count) {
		add(element, -count);
	}

	/**
	 * Remove all elements from multiset
	 * 
	 * @param element
	 * @return true is elements removed, else false
	 */
	public boolean removeAll(E element) {
		if ((elements.remove(element)) == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Empties multiset
	 */
	public void clear() {
		elements.clear();
	}

	/**
	 * 
	 * @return true if multiset is empty
	 */
	public boolean isEmpty() {
		return elements.isEmpty();
	}
	
	/**
	 * 
	 * @return the number of element types contained in this multiset
	 */
	public int getTypeCount() {
		return elements.size();
	}
	
	/**
	 * 
	 * @return the total number of elements contained in this multiset
	 */
	public int getElementCount() {
		int total = 0;
		for (E element : elements.keySet()) {
			total += elements.get(element);
		}
		return total;
	}
	

	/**
	 * Allows multiset to be iterated over in a foreach loop
	 */
	@Override
	public Iterator<E> iterator() {
		return elements.keySet().iterator();
	}
}
