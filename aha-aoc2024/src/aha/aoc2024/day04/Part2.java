package aha.aoc2024.day04;

import static org.junit.jupiter.api.Assertions.assertEquals;

import aha.aoc2024.Utils;
import aha.aoc2024.Utils.CharMap;

public class Part2 extends Part1 {

	@Override
	protected void computeFor(final CharMap cm, final int x, final int y) {
		if (cm.getChar(x, y) != 'A')
			return;
		if (ok(getWord(cm, x, y, Utils.NE, 3, -2)) && ok(getWord(cm, x, y, Utils.SE, 3, -2)))
			this.res++;
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
