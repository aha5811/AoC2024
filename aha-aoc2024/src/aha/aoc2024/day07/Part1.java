package aha.aoc2024.day07;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import aha.aoc2024.Part;
import aha.aoc2024.Utils;

public class Part1 extends Part {

	@Override
	public Part compute(final String file) {
		for (final String line : Utils.readLines(this.dir + file)) {
			final List<Long> ls = Utils.toLs(line.replace(":", ""));
			final Long target = ls.remove(0);
			if (solvable(target, ls))
				this.res += target;
		}
		return this;
	}

	private boolean solvable(final long res, final List<Long> ls) {
		return solvableRec(res, ls.remove(0), ls);
	}
	
	boolean solvableRec(final long target, final long tmp, final List<Long> ls) {
		
		if (ls.isEmpty())
			return target == tmp;

		if (tmp > target)
			return false;

		final long l = ls.get(0);
		final List<Long> lsNext = ls.subList(1, ls.size());
		
		return solvableRec(target, tmp + l, lsNext) || solvableRec(target, tmp * l, lsNext);
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
