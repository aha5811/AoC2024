package aha.aoc2024.day19;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import aha.aoc2024.Part;

public class Part2 extends Part1 {

	// https://adventofcode.com/2024/day/19#part2
	
	@Override
	public Part compute(final String file) {
		
		final List<String> designs = new LinkedList<>();
		List<String> patterns = new LinkedList<>();
		read(file, designs, patterns);

		// these are the ones that were removed by compactation
		final List<String> uncompactable = new LinkedList<>(patterns);

		// idea: get all possible combinations by searching with trace
		// for each combination c and for all uncompactable patterns ucp
		//   if c contains ucp then
		//     multiply res with number of combinations for ucp

		patterns = compact(patterns);
		
		// without compacting (i.e. total search) the test case runs fine
		// but for input it fails after 20min with out of memory with Xmx48g

		uncompactable.removeAll(patterns);

		// find all solutions for uncompactable with (remaining) patterns
		final Map<String, List<List<String>>> u2 = new HashMap<>();
		for (final String d : uncompactable) {
			u2.put(d, new LinkedList<>());
			doSearch(u2.get(d), new LinkedList<>(), d, patterns);
		}

		final List<List<String>> res = new LinkedList<>();
		for (final String d : designs)
			doSearch(res, new LinkedList<>(), d, patterns);

		for (final List<String> combination : res) {
			System.out.println(combination);

			// {rb=[[r, b]], gb=[[g, b]], br=[[b, r]]}
			// [r, r, b, g, b, r]
			// should be 6

			// r b does not overlap so 2 * rest
			// g b and b r do overlap with 3 possibilities (g b r, gb r, g br) so 3 * rest
			// (1 *) 2 * 3 = 6

			this.res += 1;
		}

		return this;
	}

	private void doSearch(final List<List<String>> res, final List<String> dRes, final String d, final List<String> patterns) {
		if (d.isEmpty())
			res.add(dRes);
		if (patterns.isEmpty())
			return;
		
		final List<String> dps = getPs4s(d, patterns);
		
		for (final String p : dps)
			if (d.length() >= p.length() && d.startsWith(p)) {
				final List<String> dResNext = new LinkedList<>(dRes);
				dResNext.add(p);
				doSearch(res, dResNext, d.substring(p.length()), dps);
			}
	}

	@Override
	public void aTest() {
		assertEquals(16, new Part2().compute("test.txt").res);
	}
	
	@Override
	public void main() {
		// assertEquals(0, new Part2().compute("input.txt").res);
	}
	
}
