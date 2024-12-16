package aha.aoc2024.day15;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import aha.aoc2024.Utils;
import aha.aoc2024.Utils.CharMap;
import aha.aoc2024.Utils.Symbol;

public class Part2 extends Part1 {
	
	// https://adventofcode.com/2024/day/15#part2
	
	@Override
	protected List<String> preprocess(final List<String> lines) {
		final List<String> ret = new LinkedList<>();
		for (final String line : lines) {
			String s = "";
			for (final char c : line.toCharArray())
				if (c == 'O')
					s += "[]";
				else if (c == '#')
					s += "##";
				else
					s += c + ".";
			ret.add(s);
		}
		return ret;
	}
	
	@Override
	protected boolean move(final CharMap cm, final Symbol s, final int[] dir) {
		final int nx = s.x + dir[0], ny = s.y + dir[1];
		final Symbol next = cm.getSymbol(nx, ny);
		if (next.c == '#')
			return false;
		else if (next.c == '.') {
			doMove(cm, s, nx, ny);
			return true;
		} else if (dir == Utils.L && next.c == ']' && move(cm, cm.getSymbol(nx - 1, ny), dir)) {
			doMove(cm, next, next.x - 1, next.y);
			doMove(cm, s, nx, ny);
			return true;
		} else if (dir == Utils.R && next.c == '[' && move(cm, cm.getSymbol(nx + 1, ny), dir)) {
			doMove(cm, next, next.x + 1, next.y);
			doMove(cm, s, nx, ny);
			return true;
		} else if ((dir == Utils.U || dir == Utils.D) && (next.c == '[' || next.c == ']')) {
			boolean ret = false;
			
			// we do this by hand
			final Map<Integer, Collection<Integer>> xLines = new HashMap<>();
			final List<Integer> ys = new LinkedList<>();
			int nny = ny;
			{
				ys.add(0, nny);
				xLines.put(nny, new HashSet<>());
				xLines.get(nny).add(nx);
				xLines.get(nny).add(next.c == '[' ? nx + 1 : nx - 1);
			}
			while (true) {
				final Collection<Integer> xLine = xLines.get(nny);
				nny += dir[1];
				ys.add(0, nny);
				xLines.put(nny, new HashSet<>());
				boolean blocked = false;
				for (final int x : xLine) {
					final char c = cm.getChar(x, nny);
					if (c == '#') {
						blocked = true;
						break;
					} else if (c == '[' || c == ']') {
						xLines.get(nny).add(x);
						xLines.get(nny).add(c == '[' ? x + 1 : x - 1);
					}
				}
				if (blocked)
					// move nothing
					break;
				else if (xLines.get(nny).isEmpty()) {
					// free -> move everything
					for (final int y : ys)
						for (final int x : xLines.get(y))
							doMove(cm, cm.getSymbol(x, y), x, y + dir[1]);
					ret = true;
					break;
				}
			}
			
			if (ret)
				doMove(cm, s, nx, ny); // move robot
			
			return ret;
		}
		return false; // something blocked
	}

	private void doMove(final CharMap cm, final Symbol s, final int nx, final int ny) {
		if (s.c == '[' || s.c == ']') {
			cm.setChar(s.x, s.y, '.');
			cm.setChar(nx, ny, s.c);
		} else if (s.c == '@') {
			s.x = nx;
			s.y = ny;
		}
	}

	@Override
	protected void score(final CharMap cm) {
		score(cm, '[');
	}
	
	@Override
	public void aTest() {
		assertEquals(618, new Part2().compute("test2.txt").res);
		assertEquals(9021, new Part2().compute("test.txt").res);
	}

	@Override
	public void main() {
		assertEquals(1472235, new Part2().compute("input.txt").res);
	}

}
