package aha.aoc2024.day12;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.HashSet;

import aha.aoc2024.Utils;
import aha.aoc2024.Utils.Pos;
import aha.aoc2024.Utils.Symbol;

public class Part2 extends Part1 {

	// https://adventofcode.com/2024/day/12#part2

	@Override
	protected RegFac getRegFac() { return s -> new Region2(s); }

	@Override
	protected int getFenceCost(final Region r) {
		final int b = ((Region2) r).getSides(), s = r.size(), total = s * b;
		// System.out.println(r.c + " " + s + " * " + b + " = " + total);
		return total;
	}
	
	static class Edge extends Pos {
		int[] dir;

		public Edge(final int x, final int y, final int[] dir) {
			super(x, y);
			this.dir = dir;
		}

		@Override
		public String toString() {
			return super.toString() + toString(this.dir);
		}
		
		private String toString(final int[] dir) {
			if (dir == Utils.U)
				return "^";
			else if (dir == Utils.R)
				return ">";
			else if (dir == Utils.D)
				return "v";
			else if (dir == Utils.L)
				return "<";
			return "?";
		}
		
		@Override
		public boolean equals(final Object obj) {
			if (obj == null || !(obj instanceof Edge))
				return false;
			if (obj == this)
				return true;
			final Edge other = (Edge) obj;
			return other.x == this.x && other.y == this.y && other.dir == this.dir;
		}
	}
	
	static class Region2 extends Region {

		public Region2(final Symbol s) {
			super(s);
		}
		
		int getSides() {

			// get all outer edges (as Pos with direction facing outward)
			final Collection<Edge> edges = new HashSet<>();
			{
				final Collection<Pos> ps = new HashSet<>();
				for (final Pos p : this.ss)
					ps.add(new Pos(p.x, p.y));
				
				for (final Pos p : ps)
					for (final int[] dir : Utils.DIRS90)
						if (!ps.contains(new Pos(p.x + dir[0], p.y + dir[1])))
							edges.add(new Edge(p.x, p.y, dir));
			}
			
			int ret = 0;
			while (!edges.isEmpty())
				ret += removeRing(edges);
			return ret;
		}

		private int removeRing(final Collection<Edge> edges) {

			// the sides are the amount of turns needed to walk the edge
			
			int turns = 0;
			
			// edges may contain many rings, one for the outside and one for any hole

			// we start with any edge
			// for each step
			// - create next edges for current direction, left turn, right turn
			// - if one of next edges is start -> end (if turn, inc turns)
			// - else
			// .. check which of next is in collection -> goto next (if turn, inc turns)
			// while moving the "used" edges are removed,
			// so in next method call another ring gets removed

			final Edge start = edges.iterator().next();

			Edge e = start;
			
			while (true) {
				edges.remove(e);

				int[] onward = turnRight(e.dir);

				final Edge
				nextOnward = new Edge(e.x + onward[0], e.y + onward[1], e.dir),
				nextRight = new Edge(e.x, e.y, onward),
				nextLeft =
				new Edge(
						e.x + onward[0] + e.dir[0],
						e.y + onward[1] + e.dir[1],
						turnLeft(e.dir)
						);
				
				if (nextOnward.equals(start))
					break;
				else if (nextRight.equals(start) || nextLeft.equals(start)) {
					turns++;
					break;
				} else if (edges.contains(nextOnward))
					e = nextOnward;
				else if (edges.contains(nextRight)) {
					e = nextRight;
					onward = turnRight(onward);
					turns++;
				} else if (edges.contains(nextLeft)) {
					e = nextLeft;
					onward = turnLeft(onward);
					turns++;
				}
			}
			
			return turns;
		}

	}

	static int[] turnRight(final int[] dir) {
		if (dir == Utils.U) return Utils.R;
		if (dir == Utils.R) return Utils.D;
		if (dir == Utils.D) return Utils.L;
		if (dir == Utils.L) return Utils.U;
		return null;
	}

	static int[] turnLeft(final int[] dir) {
		if (dir == Utils.U) return Utils.L;
		if (dir == Utils.L) return Utils.D;
		if (dir == Utils.D) return Utils.R;
		if (dir == Utils.R) return Utils.U;
		return null;
	}

	@Override
	public void aTest() {
		assertEquals(80, new Part2().compute("test1.txt").res);
		assertEquals(436, new Part2().compute("test2.txt").res);
		assertEquals(236, new Part2().compute("test3.txt").res);
		assertEquals(368, new Part2().compute("test4.txt").res);
		assertEquals(1206, new Part2().compute("test.txt").res);
	}
	
	@Override
	public void main() {
		assertEquals(885394, new Part2().compute("input.txt").res);
	}
	
}
