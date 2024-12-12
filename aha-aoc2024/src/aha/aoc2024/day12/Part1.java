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

		final List<Region> regions = getRegions(cm, getRegFac());
		
		for (final Region r : regions)
			this.res += getFenceCost(r);
		
		return this;
	}

	interface RegFac {
		Region from(final Symbol s);
	}

	protected RegFac getRegFac() { return s -> new Region(s); }
	
	protected int getFenceCost(final Region r) {
		final int b = r.getBorder(), s = r.size(), total = s * b;
		System.out.println(r.c + " " + s + " * " + b + " = " + total);
		return total;
	}
	
	private List<Region> getRegions(final CharMap cm, final RegFac rf) {
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
					tmp.add(rf.from(s));
			}
			
			// merge
			while (tmp.size() > 1) {
				final List<Region> merged = new LinkedList<>();
				final int inSize = tmp.size();
				while (!tmp.isEmpty()) {
					final Region r = tmp.remove(0);
					merged.add(r);
					final Iterator<Region> ri = tmp.iterator();
					while (ri.hasNext()) {
						final Region or = ri.next();
						if (r.merges(or))
							ri.remove();
					}
				}
				tmp = merged;
				if (tmp.size() == inSize)
					break;
			}
			
			ret.addAll(tmp);
		}

		return ret;
	}

	static class Region {
		char c;
		Collection<Symbol> ss = new HashSet<>();

		public Region(final Symbol s) {
			this.c = s.c;
			add(s);
		}
		
		boolean merges(final Region other) {
			boolean touches = false;
			for (final Symbol s : other.ss)
				if (isNeighbour(s)) {
					touches = true;
					break;
				}
			if (touches)
				this.ss.addAll(other.ss);
			return touches;
		}

		void add(final Symbol s) {
			this.ss.add(s);
		}
		
		boolean isNeighbour(final Symbol s) {
			if (s.c != this.c)
				return false;
			return isNeighbourP(s);
		}
		
		boolean isNeighbourP(final Pos p) {
			for (final Pos ap : this.ss)
				if (isNeighbour(p, ap))
					return true;
			return false;
		}
		
		int size() {
			return this.ss.size();
		}

		int getBorder() {
			int ret = 0;
			for (final Pos p : this.ss)
				ret += 4 - getNeighbours(p);
			return ret;
		}
		
		protected final int getNeighbours(final Pos p) {
			int ret = 0;
			for (final Pos ap : this.ss)
				if (isNeighbour(ap, p))
					ret++;
			return ret;
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
		assertEquals(1421958, new Part1().compute("input.txt").res);
	}

}
