package aha.aoc2024.day11;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Part2 extends Part1 {

	// https://adventofcode.com/2024/day/11#part2
	
	@Override
	protected long computeExplosion(final List<Long> ls) {

		// check how many different numbers will emerge
		{
			final Collection<Long> all = new HashSet<>();
			int blink = 0;
			List<Long> start = ls;
			while (true) {
				all.addAll(start);
				final List<Long> next = explode(start);
				blink++;
				next.removeAll(all);
				if (next.isEmpty())
					break;
				start = next;
			}
			
			final List<Long> alls = new LinkedList<>(all);
			Collections.sort(alls);
			
			System.out.println("no more new numbers after blink " + blink);
			System.out.println(alls.size() + ": " + alls);
		}

		System.out.println("-------------------------------");

		// check how long it takes for each digit to emerge again
		// 0 -> 1 -> 2024 -> 20 24 -> 2 0 2 4 => 4 blinks
		// 1 -> 2024 -> 20 24 -> 2 0 2 4 -> ... 1 ... ... => 4 blinks
		// 2 -> 4048 -> 40 48 -> 0 -> 1 -> 2024 -> 20 24 -> 2 => 7 blinks
		// ...

		for (long l = 0; l <= 9; l++) {
			List<Long> start = new LinkedList<>();
			start.add(l);
			List<Long> next;
			int blink = 0;
			while (true) {
				next = explode(start);
				blink++;
				if (next.contains(l))
					break;
				start = next;
			}
			System.out.println(
					"cycle test for " + l
					+ " took " + blink + " blinks"
					+ " to get " + count(next, l) + " '" + l + "'s"
					+ " in " + next);
		}

		return 0;
	}

	private int count(final List<Long> ls, final long l) {
		int ret = 0;
		for (final long al : ls)
			if (l == al)
				ret++;
		return ret;
	}

	private List<Long> explode(final List<Long> list) {
		final List<Long> ret = new LinkedList<>();
		for (final long l : list) {
			String ls;
			if (l == 0)
				ret.add(1l);
			else if ((ls = Long.toString(l)).length() % 2 == 0) {
				final int split = ls.length() / 2;
				ret.add(Long.parseLong(ls.substring(0, split)));
				ret.add(Long.parseLong(ls.substring(split)));
			} else
				ret.add(l * 2024);
		}
		return ret;
	}

	@Override
	public void main() {
		assertEquals(1, new Part2().setBlinks(75).compute("input.txt").res);
	}
	
}
