package main.util;

import java.math.BigInteger;

/**
 * Increments decimals numbers from a starting value by 1. Tracks numbers up to
 * 2^31-1 digits in length. Practically bypasses any number limits.
 *
 * CURRENT UNUSED. But may be used for future iterations.
 * 
 * @author Evan Quan
 * @since March 11, 2018
 *
 */
public class BigIncrementer {
	private static final int DEFAULT_STARTING_VALUE = 0;
	private long num;
	private BigInteger numBig;

	/**
	 * Default constructor. Starting value is 0.
	 */
	public BigIncrementer() {
		this("0");
	}

	/**
	 * Complete constructor
	 *
	 * @param startingValue
	 *            String representation of an Integer that the BigIncrementor starts
	 *            from
	 * @throws NumberFormatException
	 *             startingValue is not a valid representation of an Integer
	 */
	public BigIncrementer(String startingValue) throws NumberFormatException {
		num = DEFAULT_STARTING_VALUE;
		try {
			numBig = new BigInteger(startingValue);
		} catch (Exception e) {
			throw new NumberFormatException();
		}
	}

	/**
	 * Update numBig and reset num if num underflows
	 */
	private void checkUnderflow() {
		if (num == Long.MAX_VALUE) {
			updateNumBig();
		}
	}

	/**
	 *
	 * @return the BigInteger representation of the BigIncrementor value
	 */
	public BigInteger getBigInteger() {
		updateNumBig();
		return numBig;
	}

	/**
	 * Increase value by 1
	 */
	public void increment() {
		checkUnderflow();
		num++;
	}

	/**
	 * String representation of decimal value of this incrementer
	 */
	@Override
	public String toString() {
		return getBigInteger().toString();
	}

	/**
	 * Updates numBig to current count and resets num to 0
	 */
	private void updateNumBig() {
		numBig = numBig.add(new BigInteger(Long.toString(num))); // Update numBig from num
		num = DEFAULT_STARTING_VALUE; // Reset num
	}

}
