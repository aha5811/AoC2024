package aha.aoc2024.day01;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import aha.aoc2024.Part;
import aha.aoc2024.Utils;

public class Part1 extends Part {

	// https://adventofcode.com/2024/day/1

	@Override
	public final Part1 compute(final String file) {
		
		// 1 2
		// 3 4
		// -> (1, 3), (2, 4)
		
		final List<Integer> firsts = new LinkedList<>(), seconds = new LinkedList<>();
		for (final String line : Utils.readLines(this.dir + file)) {
			final List<Integer> ns = Utils.toIs(line);
			firsts.add(ns.get(0));
			seconds.add(ns.get(1));
		}

		compute(firsts, seconds);

		return this;
	}

	protected void compute(final List<Integer> firsts, final List<Integer> seconds) {
		Collections.sort(firsts);
		Collections.sort(seconds);

		final Iterator<Integer> fi = firsts.iterator(), si = seconds.iterator();
		while (fi.hasNext())
			this.res += Math.abs(fi.next() - si.next());
	}
	
	@Override
	public void aTest() {
		assertEquals(11, new Part1().compute("test.txt").res);
	}

	@Override
	public void main() {
		assertEquals(2057374, new Part1().compute("input.txt").res);
	}
	
}
