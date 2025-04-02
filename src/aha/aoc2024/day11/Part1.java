package aha.aoc2024.day11;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import aha.aoc2024.Part;
import aha.aoc2024.Utils;

public class Part1 extends Part {

	// https://adventofcode.com/2024/day/11

	int blinks = 0;

	public Part1 setBlinks(final int blinks) {
		this.blinks = blinks;
		return this;
	}
	
	@Override
	public Part compute(final String file) {
		final List<Long> ls = Utils.toLs(Utils.readLines(this.dir + file).get(0));
		this.res = computeExplosion(ls);
		return this;
	}
	
	protected long computeExplosion(final List<Long> ls) {
		int ret = 0;
		for (final long l : ls)
			ret += explode(l, this.blinks);
		return ret;
	}
	
	private long explode(final long l, final int blinks) {
		if (blinks == 0)
			return 1;
		final int bnext = blinks - 1;
		String ls;
		if (l == 0)
			return explode(1, bnext);
		else if ((ls = Long.toString(l)).length() % 2 == 0) {
			final int split = ls.length() / 2;
			return explode(Long.parseLong(ls.substring(0, split)), bnext)
					+ explode(Long.parseLong(ls.substring(split)), bnext);
		} else
			return explode(l * 2024, bnext);
	}
	
	@Override
	public void aTest() {
		assertEquals(22, new Part1().setBlinks(6).compute("test.txt").res);
		assertEquals(55312, new Part1().setBlinks(25).compute("test.txt").res);
	}

	@Override
	public void main() {
		assertEquals(217812, new Part1().setBlinks(25).compute("input.txt").res);
	}
	
}
