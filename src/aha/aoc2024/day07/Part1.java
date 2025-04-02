package aha.aoc2024.day07;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import aha.aoc2024.Part;
import aha.aoc2024.Utils;

public class Part1 extends Part {

	// https://adventofcode.com/2024/day/7
	
	@Override
	public Part compute(final String file) {
		for (final String line : Utils.readLines(this.dir + file)) {
			final List<Long> ls = Utils.toLs(line.replace(":", ""));
			final Long target = ls.remove(0);
			if (solvable(target, ls, getFs()))
				this.res += target;
		}
		return this;
	}
	
	static interface F {
		public long f(long l1, long l2);
	}

	static F Plus = new F() {
		@Override
		public long f(final long l1, final long l2) {
			return l1 + l2;
		};
	};
	
	static F Mult = new F() {
		@Override
		public long f(final long l1, final long l2) {
			return l1 * l2;
		};
	};
	
	protected F[] getFs() { return new F[] { Plus, Mult }; }

	private boolean solvable(final long res, final List<Long> ls, final F[] fs) {
		return solvableRec(res, ls.remove(0), ls, fs);
	}

	private final boolean solvableRec(final long target, final long tmp, final List<Long> ls, final F[] fs) {
		
		if (ls.isEmpty())
			return target == tmp;

		if (tmp > target)
			return false;

		final long l = ls.get(0);
		final List<Long> lsNext = ls.subList(1, ls.size());

		boolean ret = false;
		for (final F f : fs)
			ret |= solvableRec(target, f.f(tmp, l), lsNext, fs);
		return ret;
	}
	
	@Override
	public void aTest() {
		assertEquals(3749, new Part1().compute("test.txt").res);
	}

	@Override
	public void main() {
		assertEquals(1399219271639l, new Part1().compute("input.txt").res);
	}
	
}
