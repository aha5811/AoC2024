package aha.aoc2024.day10;

import static org.junit.jupiter.api.Assertions.assertEquals;

import aha.aoc2024.Utils;

public class Part2 extends Part1 {
	
	// https://adventofcode.com/2024/day/10#part2

	@Override
	protected int getTHscore(final DigiMap dm, final int x, final int y) {
		return getWaysToTops(dm, x, y);
	}
	
	private int getWaysToTops(final DigiMap dm, final int x, final int y) {
		
		final int here = dm.getInt(x, y);
		
		if (here == 9)
			return 1;
		
		int ret = 0;
		for (final int[] d : Utils.DIRS90) {
			final int xd = x + d[0], yd = y + d[1];
			if (!dm.isOutside(xd, yd) && dm.getInt(xd, yd) == here + 1)
				ret += getWaysToTops(dm, xd, yd);
		}
		return ret;
	}

	@Override
	public void aTest() {
		assertEquals(81, new Part2().compute("test.txt").res);
	}

	@Override
	public void main() {
		assertEquals(1925, new Part2().compute("input.txt").res);
	}

}
