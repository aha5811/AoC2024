package aha.aoc2024.day10;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import aha.aoc2024.Part;
import aha.aoc2024.Utils;
import aha.aoc2024.Utils.CharMap;
import aha.aoc2024.Utils.Pos;
import aha.aoc2024.Utils.Symbol;

public class Part1 extends Part {

	@Override
	public Part compute(final String file) {
		final DigiMap dm = new DigiMap(this.dir + file, -1);

		for (final Symbol s : dm.getAll('0'))
			this.res += getTHscore(dm, s.x, s.y);
		
		return this;
	}
	
	static class DigiMap extends CharMap {
		
		public final int[][] ints;

		public DigiMap(final String file, final int def) {
			super(file);
			this.ints = new int[this.w][this.h];
			for (int x = 0; x < this.w; x++)
				for (int y = 0; y < this.h; y++) {
					int i;
					try {
						i = Integer.parseInt("" + getChar(x, y));
					} catch (final Exception ex) {
						i = def;
					}
					this.ints[x][y] = i;
				}
		}

		int getInt(final int x, final int y) {
			return this.ints[x][y];
		}

	}

	protected int getTHscore(final DigiMap dm, final int x, final int y) {
		return getReachableTops(dm, x, y).size();
	}

	private Collection<Pos> getReachableTops(final DigiMap dm, final int x, final int y) {

		final int here = dm.getInt(x, y);

		if (here == 9)
			return Arrays.asList(new Pos(x, y));

		final Collection<Pos> ret = new HashSet<>();
		for (final int[] d : Utils.DIRS90) {
			final int xd = x + d[0], yd = y + d[1];
			if (!dm.isOutside(xd, yd) && dm.getInt(xd, yd) == here + 1)
				ret.addAll(getReachableTops(dm, xd, yd));
		}
		return ret;
	}
	
	@Override
	public void aTest() {
		assertEquals(36, new Part1().compute("test.txt").res);
	}

	@Override
	public void main() {
		assertEquals(778, new Part1().compute("input.txt").res);
	}
	
}
