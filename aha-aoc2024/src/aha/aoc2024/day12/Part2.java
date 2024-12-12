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
		System.out.println(r.c + " " + s + " * " + b + " = " + total);
		return total;
	}

	static class Region2 extends Region {

		public Region2(final Symbol s) {
			super(s);
		}
		
		static class OutPos extends Pos {
			int[] dir;

			public OutPos(final int x, final int y, final int[] dir) {
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
				if (obj == null || !(obj instanceof OutPos))
					return false;
				if (obj == this)
					return true;
				final OutPos other = (OutPos) obj;
				return other.x == this.x && other.y == this.y && other.dir == this.dir;
			}
		}
		
		int getSides() {

			final Collection<OutPos> ops = new HashSet<>();
			{
				final Collection<Pos> ps = new HashSet<>();
				for (final Pos p : this.ss)
					ps.add(new Pos(p.x, p.y));
				
				for (final Pos p : ps)
					for (final int[] dir : Utils.DIRS90)
						if (!ps.contains(new Pos(p.x + dir[0], p.y + dir[1])))
							ops.add(new OutPos(p.x, p.y, dir));
			}
			
			int ret = 0;
			while (!ops.isEmpty())
				ret += removeRing(ops);
			return ret;
		}

		private int removeRing(final Collection<OutPos> ops) {
			int ret = 0;
			
			final OutPos start = ops.iterator().next();
			ops.remove(start);

			OutPos current = start;
			int[] dir = turnRight(start.dir);
			
			while (!ops.isEmpty()) {
				final OutPos nextOn = new OutPos(current.x + dir[0], current.y + dir[1], current.dir),
						nextLeft = new OutPos(
								current.x + dir[0] + current.dir[0], current.y + dir[1]
										+ current.dir[1], turnLeft(current.dir)
								), nextRight = new OutPos(current.x, current.y, dir);
				
				if (nextOn.equals(start)) {
					ops.remove(current);
					break;
				} else if (nextLeft.equals(start) || nextRight.equals(start)) {
					ops.remove(current);
					ret++;
					break;
				} else if (ops.contains(nextOn)) {
					ops.remove(current);
					current = nextOn;
				} else if (ops.contains(nextLeft)) {
					ops.remove(current);
					current = nextLeft;
					dir = turnLeft(dir);
					ret++;
				} else if (ops.contains(nextRight)) {
					ops.remove(current);
					current = nextRight;
					dir = turnRight(dir);
					ret++;
				}
			}
			
			return ret;
		}

	}
	
	static int[] turnLeft(final int[] dir) {
		if (dir == Utils.U)
			return Utils.L;
		if (dir == Utils.L)
			return Utils.D;
		if (dir == Utils.D)
			return Utils.R;
		if (dir == Utils.R)
			return Utils.U;
		return null;
	}
	
	static int[] turnRight(final int[] dir) {
		if (dir == Utils.U)
			return Utils.R;
		if (dir == Utils.R)
			return Utils.D;
		if (dir == Utils.D)
			return Utils.L;
		if (dir == Utils.L)
			return Utils.U;
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
		assertEquals(0, new Part2().compute("input.txt").res);
	}
	
}
