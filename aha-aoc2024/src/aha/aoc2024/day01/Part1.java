package aha.aoc2024.day01;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import aha.aoc2024.Part;
import aha.aoc2024.Utils;

public class Part1 extends Part {
	
	private static String dir = "day01/";
	
	public Part1() {
	}
	
	@Override
	public final Part1 compute(final String file) {
		final List<Integer> firsts = new LinkedList<>(), seconds = new LinkedList<>();

		for (final String line : Utils.readLines(dir + file)) {
			final String[] tmp = line.split("\s+");
			firsts.add(Integer.parseInt(tmp[0]));
			seconds.add(Integer.parseInt(tmp[1]));
		}
		
		toRes(firsts, seconds);
		
		return this;
	}
	
	protected void toRes(final List<Integer> firsts, final List<Integer> seconds) {
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
