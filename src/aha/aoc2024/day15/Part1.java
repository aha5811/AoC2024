package aha.aoc2024.day15;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import aha.aoc2024.Part;
import aha.aoc2024.Utils;
import aha.aoc2024.Utils.CharMap;
import aha.aoc2024.Utils.Symbol;

public class Part1 extends Part {
	
	// https://adventofcode.com/2024/day/15

	@Override
	public Part compute(final String file) {
		
		final List<int[]> dirs = new LinkedList<>();
		
		List<String> lines = Utils.readLines(this.dir + file);
		{
			final Iterator<String> lit = lines.iterator();
			while (lit.hasNext()) {
				final String l = lit.next();
				if (l.trim().isEmpty())
					lit.remove();
				else if (Arrays.asList('v', '<', '>', '^').contains(l.charAt(0))) {
					for (final char c : l.toCharArray())
						if (c == '>')
							dirs.add(Utils.R);
						else if (c == 'v')
							dirs.add(Utils.D);
						else if (c == '<')
							dirs.add(Utils.L);
						else if (c == '^')
							dirs.add(Utils.U);
					lit.remove();
				}
			}
		}

		lines = preprocess(lines);

		final CharMap cm = new CharMap(lines);

		final Symbol robot = cm.getAll('@').get(0);
		
		cm.setChar(robot.x, robot.y, '.');

		// out(cm, robot);

		for (final int[] dir : dirs) {
			move(cm, robot, dir);
			// out(cm, robot);
		}
		
		score(cm);
		
		return this;
	}
	
	protected void out(final CharMap cm, final Symbol robot) {
		final char prev = cm.getChar(robot.x, robot.y);
		cm.setChar(robot.x, robot.y, robot.c);
		System.out.println(cm);
		cm.setChar(robot.x, robot.y, prev);
	}
	
	protected void score(final CharMap cm) {
		score(cm, 'O');
	}

	protected void score(final CharMap cm, final char c) {
		for (final Symbol s : cm.getAll(c))
			this.res += s.x + 100 * s.y;
	}

	/**
	 * for Part2
	 *
	 * @param lines
	 * @return
	 */
	protected List<String> preprocess(final List<String> lines) {
		return lines;
	}
	
	protected boolean move(final CharMap cm, final Symbol s, final int[] dir) {
		final int nx = s.x + dir[0], ny = s.y + dir[1];
		final Symbol next = cm.getSymbol(nx, ny);
		if (next.c == '#')
			return false;
		else if (next.c == '.' || next.c == 'O' && move(cm, next, dir)) {
			if (s.c == 'O') {
				cm.setChar(s.x, s.y, '.');
				cm.setChar(nx, ny, s.c);
			} else if (s.c == '@') {
				s.x = nx;
				s.y = ny;
			}
			return true;
		}
		return false; // next = O and can't move
	}
	
	@Override
	public void aTest() {
		assertEquals(2028, new Part1().compute("test1.txt").res);
		assertEquals(10092, new Part1().compute("test.txt").res);
	}
	
	@Override
	public void main() {
		assertEquals(1448589, new Part1().compute("input.txt").res);
	}

}
