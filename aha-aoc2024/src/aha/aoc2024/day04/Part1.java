package aha.aoc2024.day04;

import static org.junit.jupiter.api.Assertions.assertEquals;

import aha.aoc2024.Part;
import aha.aoc2024.Utils;
import aha.aoc2024.Utils.CharMap;
import aha.aoc2024.Utils.Symbol;

public class Part1 extends Part {

	@Override
	public Part compute(final String file) {
		computeFor(new CharMap(this.dir + file));
		return this;
	}

	protected void computeFor(final CharMap cm) {
		for (final Symbol s : cm.getAll('X'))
			for (final int[] dir : Utils.DIRS45)
				if (cm.getWord(s.x, s.y, dir, 3, 0).equals("MAS"))
					this.res++;
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
