package aha.aoc2024.day04;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.LinkedList;

import aha.aoc2024.Part;
import aha.aoc2024.Utils;
import aha.aoc2024.Utils.CharMap;

public class Part1 extends Part {
	
	@Override
	public Part compute(final String file) {
		final CharMap cm = new CharMap(Utils.readLines(this.dir + file));
		for (int x = 0; x < cm.w; x++)
			for (int y = 0; y < cm.h; y++)
				if (cm.getChar(x, y) == 'X')
					this.res += countFor(cm, x, y);
		return this;
	}

	private final static Collection<int[]> dirs;
	static {
		dirs = new LinkedList<>();
		dirs.add(new int[] { 1, 0 }); // e
		dirs.add(new int[] { 1, 1 }); // se
		dirs.add(new int[] { 0, 1 }); // s
		dirs.add(new int[] { -1, -1 }); // sw
		dirs.add(new int[] { -1, 0 }); // w
		dirs.add(new int[] { -1, 1 }); // nw
		dirs.add(new int[] { 0, -1 }); // n
		dirs.add(new int[] { 1, -1 }); // ne
	}

	private long countFor(final CharMap cm, final int x, final int y) {
		int ret = 0;
		for (final int[] dir : dirs)
			if (getWord(cm, x, y, dir, 3).equals("MAS"))
				ret++;
		return ret;
	}
	
	private String getWord(final CharMap cm, int x, int y, final int[] dir, final int l) {
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
