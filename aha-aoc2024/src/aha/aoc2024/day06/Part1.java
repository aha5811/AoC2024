package aha.aoc2024.day06;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;

import aha.aoc2024.Part;
import aha.aoc2024.Utils;
import aha.aoc2024.Utils.CharMap;
import aha.aoc2024.Utils.Symbol;

public class Part1 extends Part {

	@Override
	public Part compute(final String file) {
		final CharMap cm = new CharMap(Utils.readLines(this.dir + file));
		
		final Symbol g = cm.getAll('^').get(0);
		
		final List<int[]> dirs = new LinkedList<>();
		dirs.add(Utils.U);
		dirs.add(Utils.R);
		dirs.add(Utils.D);
		dirs.add(Utils.L);

		int x = g.x, y = g.y;
		int[] dir = bump(dirs);
		
		// initial solution was to write X's and then count X's when the guard left
		// but for part2 more info is needed

		while (true) {
			final int xnext = x + dir[0], ynext = y + dir[1];
			final Character next = cm.getChar(xnext, ynext);
			if (next == null)
				break;
			else if (next == '#')
				dir = bump(dirs);
			else {
				x = xnext;
				y = ynext;
				cm.setChar(x, y, toChar(dir));
			}
		}

		for (final int[] aDir : dirs)
			this.res += cm.getAll(toChar(aDir)).size();

		return this;
	}
	
	private char toChar(final int[] dir) {
		if (dir == Utils.U)
			return '^';
		else if (dir == Utils.R)
			return '>';
		else if (dir == Utils.D)
			return 'v';
		else if (dir == Utils.L)
			return '<';
		return '?'; // won't happen
	}

	private <T> T bump(final List<T> dirs) {
		final T ret = dirs.remove(0);
		dirs.add(ret);
		return ret;
	}
	
	@Override
	public void aTest() {
		assertEquals(41, new Part1().compute("test.txt").res);
	}

	@Override
	public void main() {
		assertEquals(4656, new Part1().compute("input.txt").res);
	}
	
}
