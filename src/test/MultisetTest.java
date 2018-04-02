package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import main.util.Multiset;

/**
 * JUnit 4 tests for {@link Multiset} class
 * 
 * @author Evan Quan
 * @version 1.0.0
 * @since 2 April 2018
 */
public class MultisetTest {

	private static Multiset<String> set;

	@Before
	public void setup() {
		set = new Multiset<String>();
	}

	@Test
	public void test_isEmpty_true() {
		assertTrue(set.isEmpty());
	}

	@Test
	public void test_add_negative_0() {
		set.add("", -1);
		int result = set.count("");
		assertEquals(0, result);
	}

	@Test
	public void test_remove_positive() {
		set.add("", 2);
		set.remove("", 1);
		int result = set.count("");
		assertEquals(1, result);
	}

	@Test
	public void test_remove_negative() {
		set.remove("", -1);
		int result = set.count("");
		assertEquals(1, result);
	}

	@Test
	public void test_remove() {
		set.add("", 2);
		set.remove("");
		assertEquals(1, set.count(""));
	}

	@Test
	public void test_removeAll_contains() {
		set.add("", 2);
		set.add(" ", 1);
		set.removeAll("");
		assertEquals(0, set.count(""));
		assertEquals(1, set.count(" "));
	}

	@Test
	public void test_removeAll_notContains() {
		assertFalse(set.removeAll(""));

	}

	@Test
	public void test_clear() {
		set.add("", 2);
		set.add(" ", 1);
		set.clear();
		assertEquals(0, set.count(""));
		assertEquals(0, set.count(" "));
	}

	@Test
	public void test_remove_set() {
		set.add("", 2);
		set.add(" ", 1);
		Multiset<String> toRemove = new Multiset<String>();
		for (String str : set) {
			toRemove.add(str, set.count(str));
		}
		set.remove(toRemove);
		assertEquals(0, set.count(""));
		assertEquals(0, set.count(" "));
	}

	@Test
	public void test_clone() {
		set.add("", 2);
		set.add(" ", 1);
		Multiset<String> other = set.clone();
		assertEquals(set.count(""), other.count(""));
		assertEquals(set.count(" "), other.count(" "));
	}

	@Test
	public void test_add_set() {
		set.add("", 2);
		set.add(" ", 1);
		Multiset<String> other = new Multiset<String>();
		other.add(set);
		assertEquals(set.count(""), other.count(""));
		assertEquals(set.count(" "), other.count(" "));
	}

	@Test
	public void test_getTypeCount() {
		set.add("", 2);
		set.add(" ", 1);
		assertEquals(2, set.getTypeCount());
	}

	@Test
	public void test_getElementCount() {
		set.add("", 2);
		set.add(" ", 1);
		assertEquals(3, set.getElementCount());
	}

	@Test
	public void test_add_1() {
		set.add("");
		assertEquals(1, set.count(""));
	}

}
