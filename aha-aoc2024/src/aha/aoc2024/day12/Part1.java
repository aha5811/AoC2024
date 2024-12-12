package aha.aoc2024.day12;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import aha.aoc2024.Part;
import aha.aoc2024.Utils;
import aha.aoc2024.Utils.CharMap;
import aha.aoc2024.Utils.Pos;
import aha.aoc2024.Utils.Symbol;

public class Part1 extends Part {

	// https://adventofcode.com/2024/day/12
	
	@Override
	public Part compute(final String file) {
		final CharMap cm = new CharMap(this.dir + file);
		
		final List<Region> regions = getRegions(cm);
		
		for (final Region r : regions) {
			final int b = r.getBorder(), s = r.size();
			System.out.println(r.c + " " + s + " * " + b + " = " + s * b);
			this.res += s * b;
		}
		System.out.println("total: " + this.res);

		return this;
	}
	
	private List<Region> getRegions(final CharMap cm) {
		final List<Region> ret = new LinkedList<>();
		
		for (final char c : cm.getChars()) {
			List<Region> tmp = new LinkedList<>();

			for (final Symbol s : cm.getAll(c)) {
				boolean found = false;
				for (final Region r : tmp)
					if (r.isNeighbour(s)) {
						found = true;
						r.add(s);
					}
				if (!found)
					tmp.add(new Region(s));
			}

			// merge
			while (true) {
				final List<Region> merged = new LinkedList<>();
				while (!tmp.isEmpty()) {
					final Region r = tmp.remove(0);
					final Iterator<Region> ri = tmp.iterator();
					while (ri.hasNext()) {
						final Region or = ri.next();
						if (r.merges(or))
							ri.remove();
					}
					merged.add(r);
				}
				final boolean changed = merged.size() < tmp.size();
				tmp = merged;
				if (!changed)
					break;
			}

			ret.addAll(tmp);
		}
		
		return ret;
	}
	
	static class Region {
		char c;
		Collection<Symbol> poss = new HashSet<>();
		
		public Region(final Symbol s) {
			this.c = s.c;
			add(s);
		}

		public boolean merges(final Region other) {
			boolean touches = false;
			for (final Symbol p : other.poss)
				if (isNeighbour(p)) {
					touches = true;
					break;
				}
			if (touches)
				this.poss.addAll(other.poss);
			return touches;
		}
		
		public void add(final Symbol s) {
			this.poss.add(s);
		}

		int getBorder() {
			int ret = 0;
			for (final Symbol p : this.poss)
				ret += 4 - getNeighbours(p);
			return ret;
		}

		int size() {
			return this.poss.size();
		}
		
		private int getNeighbours(final Pos p) {
			int ret = 0;
			for (final Pos ap : this.poss)
				if (isNeighbour(ap, p))
					ret++;
			return ret;
		}

		boolean isNeighbour(final Symbol s) {
			if (s.c != this.c)
				return false;
			for (final Pos ap : this.poss)
				if (isNeighbour(s, ap))
					return true;
			return false;
		}

		private boolean isNeighbour(final Pos p1, final Pos p2) {
			for (final int[] dir : Utils.DIRS90)
				if (p1.x + dir[0] == p2.x && p1.y + dir[1] == p2.y)
					return true;
			return false;
		}
	}
	
	@Override
	public void aTest() {
		assertEquals(140, new Part1().compute("test1.txt").res);
		assertEquals(772, new Part1().compute("test2.txt").res);
		assertEquals(1930, new Part1().compute("test.txt").res);
	}

	@Override
	public void main() {
		assertEquals(0, new Part1().compute("input.txt").res);
		// 1421126 is too low
	}
	
}
