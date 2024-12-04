package aha.aoc2024.day04;

import static org.junit.jupiter.api.Assertions.assertEquals;

import aha.aoc2024.Utils;
import aha.aoc2024.Utils.CharMap;
import aha.aoc2024.Utils.Symbol;

public class Part2 extends Part1 {

	@Override
	protected void computeFor(final CharMap cm) {
		for (final Symbol s : cm.getAll('A')) {
			final int x = s.x, y = s.y, l = 3, off = -2;
			if (ok(cm.getWord(x, y, Utils.NE, l, off)) && ok(cm.getWord(x, y, Utils.SE, l, off)))
				this.res++;
		}
	}

	private boolean ok(final String s) {
		return s.equals("MAS") || s.equals("SAM");
	}
	
	@Override
	public void aTest() {
		assertEquals(9, new Part2().compute("test.txt").res);
	}
	
	@Override
	public void main() {
		assertEquals(1912, new Part2().compute("input.txt").res);
	}
	
}
