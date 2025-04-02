package aha.aoc2024.day11;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import aha.aoc2024.Utils;

public class Part2 extends Part1 {
	
	// https://adventofcode.com/2024/day/11#part2

	@Override
	protected long computeExplosion(final List<Long> startLs) {
		
		Map<Long, Long> l2n = new HashMap<>();
		for (final Long l : startLs)
			inc(l2n, l, 1l);
		
		for (int blink = 0; blink < this.blinks; blink++) {
			final Map<Long, Long> l2nNext = new HashMap<>();
			final List<Long> keys = new LinkedList<>(l2n.keySet());
			for (final Long l : keys) {
				final long n = l2n.get(l);
				String ls;
				if (l == 0)
					inc(l2nNext, 1l, n);
				else if ((ls = Long.toString(l)).length() % 2 == 0) {
					final int split = ls.length() / 2;
					inc(l2nNext, Long.parseLong(ls.substring(0, split)), n);
					inc(l2nNext, Long.parseLong(ls.substring(split)), n);
				} else
					inc(l2nNext, l * 2024, n);
			}
			l2n = l2nNext;
		}
		
		long ret = 0;
		for (final long l : l2n.keySet())
			ret += l2n.get(l);
		return ret;
		
	}
	
	private void inc(final Map<Long, Long> l2n, final Long l, final long m) {
		Utils.inc(l2n, l, m);
	}

	@Override
	public void aTest() {
		assertEquals(22, new Part2().setBlinks(6).compute("test.txt").res);
		assertEquals(55312, new Part2().setBlinks(25).compute("test.txt").res);
	}
	
	@Override
	public void main() {
		assertEquals(259112729857522l, new Part2().setBlinks(75).compute("input.txt").res);
	}

}
