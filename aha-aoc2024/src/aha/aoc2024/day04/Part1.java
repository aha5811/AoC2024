package aha.aoc2024.day04;

import static org.junit.jupiter.api.Assertions.assertEquals;

import aha.aoc2024.Part;
import aha.aoc2024.Utils;
import aha.aoc2024.Utils.CharMap;
import aha.aoc2024.Utils.Symbol;

public class Part1 extends Part {
	
	@Override
	public Part compute(final String file) {
		computeFor(new CharMap(Utils.readLines(this.dir + file)));
		return this;
	}
	
	protected void computeFor(final CharMap cm) {
		for (final Symbol s : cm.getAll('X'))
			this.res += countFor(cm, s.x, s.y);
	}

	private long countFor(final CharMap cm, final int x, final int y) {
		int ret = 0;
		for (final int[] dir : Utils.ds45)
			if (getWord(cm, x, y, dir, 3, 0).equals("MAS"))
				ret++;
		return ret;
	}
	
	/**
	 * get the word in direction dir with length l, starting with the next char in direction
	 * offset != 0 moves the starting x,y wrt to given direction
	 */
	protected final String getWord(final CharMap cm, int x, int y, final int[] dir, final int l, final int offset) {
		if (offset != 0) {
			x = x + offset * dir[0];
			y = y + offset * dir[1];
		}

		String ret = "";
		for (int i = 0; i < l; i++) {
			final Character c = cm.getChar(x = x + dir[0], y = y + dir[1]);
			if (c != null)
				ret += c;
		}
		return ret;
	}
	
	@Override
	public void aTest() {
		assertEquals(18, new Part1().compute("test.txt").res);
	}
	
	@Override
	public void main() {
		assertEquals(2521, new Part1().compute("input.txt").res);
	}

}
