package aha.aoc2024.day06;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collections;
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

		final Symbol s = cm.getAll('^').get(0);

		final Guard g = new Guard(s.x, s.y);

		while (true) {
			final int[] next = g.nextPos();
			final Character nextC = cm.getChar(next[0], next[1]);
			if (nextC == null)
				break;
			else if (nextC == '#')
				g.turn();
			else {
				g.move();
				doMore(g, nextC);
				cm.setChar(g.x, g.y, toChar(g.dirs.get(0)));
			}
		}
		
		computeRes(cm);
		
		return this;
	}

	protected void computeRes(final CharMap cm) {
		for (final int[] dir : DIRS)
			this.res += cm.getAll(toChar(dir)).size();
	}

	protected void doMore(final Guard g, final char currentC) {
		// extension for Part2
	}

	protected final static List<int[]> DIRS =
			Collections.unmodifiableList(
					Arrays.asList(
							Utils.U, Utils.R, Utils.D, Utils.L));

	protected final static class Guard {
		int x;
		int y;
		List<int[]> dirs = new LinkedList<>(DIRS);

		Guard(final int x, final int y) {
			this.x = x;
			this.y = y;
		}

		int[] nextPos() {
			final int[] dir = this.dirs.get(0);
			return new int[] { this.x + dir[0], this.y + dir[1] };
		}

		void turn() {
			final int[] dir = this.dirs.remove(0);
			this.dirs.add(dir);
		}
		
		void move() {
			final int[] dir = this.dirs.get(0);
			this.x += dir[0];
			this.y += dir[1];
		}
	}

	protected final static char toChar(final int[] dir) {
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
	
	@Override
	public void aTest() {
		assertEquals(41, new Part1().compute("test.txt").res);
	}
	
	@Override
	public void main() {
		assertEquals(4656, new Part1().compute("input.txt").res);
	}

}
