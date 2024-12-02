package aha.aoc2024.day02;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
	
	protected boolean checkSafe(final List<Integer> ns) {
		return _checkSafeMod(ns);
	}
	
	protected final boolean _checkSafeMod(final List<Integer> ns) {
		final boolean inc = ns.get(1) - ns.get(0) > 0;
		int n0 = ns.remove(0);
		while (!ns.isEmpty()) {
			final int n1 = ns.remove(0), diff = n1 - n0, dist = Math.abs(diff);
			if (dist < 1 || dist > 3 || diff > 0 ^ inc)
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
