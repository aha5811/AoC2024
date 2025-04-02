package aha.aoc2024.day22;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import aha.aoc2024.Part;
import aha.aoc2024.Utils;

public class Part1 extends Part {

	// https://adventofcode.com/2024/day/22
	
	final static int IT = 2000;

	@Override
	public Part compute(final String file) {
		for (final String s : Utils.readLines(this.dir + file))
			try {
				final Integer i = Integer.parseInt(s);
				this.res += getNumber(i, IT);
			} catch (final Exception ex) {
			}
		return this;
	}
	
	private long getNumber(final long l, final int it) {
		long secret = l;
		for (int i = 0; i < it; i++)
			secret = getSecret(secret);
		return secret;
	}
	
	protected long getSecret(long secret) {
		final long l2 = secret * 64,
				l3 = secret ^ l2,
				l4 = l3 % 16777216,
				l5 = l4 / 32,
				l6 = l4 ^ l5,
				l7 = l6 % 16777216,
				l8 = l7 * 2048,
				l9 = l7 ^ l8,
				l10 = l9 % 16777216;
		secret = l10;
		return secret;
	}
	
	@Test
	public void test() {
		assertEquals(37, 42 ^ 15);
		assertEquals(16113920, 100000000 % 16777216);

		long n = 123;
		assertEquals(15887950, n = new Part1().getNumber(n, 1));
		assertEquals(16495136, n = new Part1().getNumber(n, 1));
		assertEquals(527345, n = new Part1().getNumber(n, 1));
		assertEquals(704524, n = new Part1().getNumber(n, 1));
		assertEquals(1553684, n = new Part1().getNumber(n, 1));
		assertEquals(12683156, n = new Part1().getNumber(n, 1));
		assertEquals(11100544, n = new Part1().getNumber(n, 1));
		assertEquals(12249484, n = new Part1().getNumber(n, 1));
		assertEquals(7753432, n = new Part1().getNumber(n, 1));
		assertEquals(5908254, n = new Part1().getNumber(n, 1));
	}

	@Override
	public void aTest() {
		assertEquals(37327623, new Part1().compute("test.txt").res);
	}

	@Override
	public void main() {
		assertEquals(18261820068l, new Part1().compute("input.txt").res);
	}
	
}
