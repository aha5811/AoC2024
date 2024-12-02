package aha.aoc2024.day02;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;

import aha.aoc2024.Part;
import aha.aoc2024.Utils;

public class Part1 extends Part {
	
	static String dir = "day02/";

	public Part1() {
	}
	
	@Override
	public Part compute(final String file) {
		for (final String line : Utils.readLines(dir + file))
			if (checkSafe(Utils.toIs(line)))
				this.res++;
		return this;
	}

	protected boolean checkSafe(final List<Integer> is) {
		return _checkSafe(is);
	}

	protected boolean _checkSafe(final List<Integer> is) {
		final List<Integer> itmp = new LinkedList<>(is);
		final boolean up0 = itmp.get(1) - itmp.get(0) > 0;
		int n0 = itmp.remove(0);
		while (!itmp.isEmpty()) {
			final int n1 = itmp.remove(0), diff = n1 - n0, ad = Math.abs(diff);
			if (ad < 1 || ad > 3 || diff > 0 ^ up0)
				return false;
			n0 = n1;
		}
		return true;
	}

	@Override
	public void aTest() {
		assertEquals(2, new Part1().compute("test.txt").res);
	}
	
	@Override
	public void main() {
		assertEquals(564, new Part1().compute("input.txt").res);
	}

}
