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
		
		// these are the ones that didn't survive compactation
		final List<String> decompact = new LinkedList<>(patterns);
		
		// idea: get all possible combinations by searching with trace
		// for each combination c and for all decompact patterns dp
		//   if c contains dp then
		//     multiply res with number of combinations for dp
		
		// patterns = compact(patterns);
		
		decompact.removeAll(patterns);
		
		// find all solutions for decompacted with (remaining) patterns
		final Map<String, List<List<String>>> dc2s = new HashMap<>();
		for (final String d : decompact) {
			dc2s.put(d, new LinkedList<>());
			doSearch(dc2s.get(d), new LinkedList<>(), d, patterns);
		}
		
		final List<List<String>> res = new LinkedList<>();
		for (final String d : designs)
			doSearch(res, new LinkedList<>(), d, patterns);
		
		this.res = res.size();

		// {rb=[[r, b]], gb=[[g, b]], br=[[b, r]]}
		// [r, r, b, g, b, r]
		// should be 6
		
		// r b does not overlap so 2 * rest
		// g b and b r do overlap with 3 possibilities (g b r, gb r, g br) so 3 * rest
		// (1 *) 2 * 3 = 6
		
		return this;
	}
	
	private void doSearch(final List<List<String>> res, final List<String> dres, final String d, final List<String> patterns) {
		if (d.isEmpty())
			res.add(dres);
		if (patterns.isEmpty())
			return;

		final List<String> sps = getPs4s(d, patterns);

		for (final String p : sps)
			if (d.length() >= p.length() && d.startsWith(p)) {
				final List<String> ndres = new LinkedList<>(dres);
				ndres.add(p);
				doSearch(res, ndres, d.substring(p.length()), sps);
			}
	}
	
	@Override
	public void aTest() {
		assertEquals(16, new Part2().compute("test.txt").res);
	}

	@Override
	public void main() {
		assertEquals(0, new Part2().compute("input.txt").res);
	}

}
