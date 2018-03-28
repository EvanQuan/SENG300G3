package main.util;

import java.util.HashMap;

/**
 * Can contain multiple quantities of each element.
 * 
 * @author Evan Quan
 * @version 1.1.0
 * @since 28 March, 2018
 *
 * @param <T>
 *            type of element contained in multiset
 */
public class Multiset<T> {

	private HashMap<T, Integer> elements;

	/**
	 * Constructs an empty multiset
	 */
	public Multiset() {
		elements = new HashMap<T, Integer>();
	}

	/**
	 * Add 1 element to multiset
	 * 
	 * @param element
	 */
	public void add(T element) {
		add(element, 1);
	}

	/**
	 * Add count number of elements to multiset
	 * 
	 * @param element
	 * @param count
	 */
	public void add(T element, int count) {
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
	public int count(T element) {
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
	public void remove(T element) {
		remove(element, 1);
	}

	/**
	 * Remove count number of elements to multiset
	 * 
	 * @param element
	 * @param count
	 * @return true is elements are removed, else false
	 */
	public void remove(T element, int count) {
		add(element, -count);
	}

	/**
	 * Remove all elements from multiset
	 * 
	 * @param element
	 * @return true is elements removed, else false
	 */
	public boolean removeAll(T element) {
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
}
