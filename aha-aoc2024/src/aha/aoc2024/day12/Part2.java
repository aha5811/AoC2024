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

	static class Region2 extends Region {
		
		public Region2(final Symbol s) {
			super(s);
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
			
			OutPos curr = start;
			int[] onward = turnRight(start.dir);

			while (!ops.isEmpty()) {
				ops.remove(curr);
				
				final OutPos
				nextOn = new OutPos(curr.x + onward[0], curr.y + onward[1], curr.dir),
				nextLeft =
				new OutPos(
						curr.x + onward[0] + curr.dir[0],
						curr.y + onward[1] + curr.dir[1],
						turnLeft(curr.dir)),
				nextRight = new OutPos(curr.x, curr.y, onward);

				if (nextOn.equals(start))
					break;
				else if (nextLeft.equals(start) || nextRight.equals(start)) {
					ret++;
					break;
				} else if (ops.contains(nextOn))
					curr = nextOn;
				else if (ops.contains(nextLeft)) {
					curr = nextLeft;
					onward = turnLeft(onward);
					ret++;
				} else if (ops.contains(nextRight)) {
					curr = nextRight;
					onward = turnRight(onward);
					ret++;
				}
			}

			return ret;
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
