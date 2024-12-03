package aha.aoc2024.day02;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import aha.aoc2024.Part;
import aha.aoc2024.Utils;

public class Part1 extends Part {

	@Override
	public Part compute(final String file) {
		this.res =
				Utils.streamLines(this.dir + file)
				.filter(line -> checkSafe(Utils.toIs(line)))
				.count();
		return this;
	}
	
	protected boolean checkSafe(final List<Integer> ns) {
		return _checkSafeMod(ns);
	}
	
	public final static int MIN_DIFF = 1, MAX_DIFF = 3;
	
	protected final boolean _checkSafeMod(final List<Integer> ns) {
		final boolean inc = ns.get(1) - ns.get(0) > 0; // 1 -> 2 increasing?
		int n0 = ns.remove(0);
		while (!ns.isEmpty()) {
			final int n1 = ns.remove(0), diff = n1 - n0, dist = Math.abs(diff);
			if (dist < MIN_DIFF || dist > MAX_DIFF || diff > 0 ^ inc)
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
