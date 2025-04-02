package aha.aoc2024.day05;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Part2 extends Part1 {
	
	// https://adventofcode.com/2024/day/5#part2
	
	@Override
	protected void computeForPages(final List<Rule> rules, final List<Integer> pages) {
		if (!satisifes(pages, rules)) {
			Collections.sort(pages, new Comparator<>() {
				@Override
				public int compare(final Integer i1, final Integer i2) {
					Integer ret = null;
					for (final Rule r : rules)
						if (ret == null)
							if (i1 == r.first && i2 == r.second)
								ret = -1;
							else if (i1 == r.second && i2 == r.first)
								ret = +1;
					return ret == null ? 0 : ret;
				}
			});
			this.res += getMiddle(pages);
		}
	}
	
	@Override
	public void aTest() {
		assertEquals(123, new Part2().compute("test.txt").res);
	}

	@Override
	public void main() {
		assertEquals(5564, new Part2().compute("input.txt").res);
	}

}
