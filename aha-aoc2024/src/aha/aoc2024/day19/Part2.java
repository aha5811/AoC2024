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
		final List<String> patterns = new LinkedList<>();
		read(file, designs, patterns);
		
		final Map<String, Long> s2combos = new HashMap<>();
		s2combos.put("", 1l);
		
		for (final String d : designs)
			this.res += cntPossible(s2combos, d, patterns);
		
		return this;
	}
	
	private long cntPossible(final Map<String, Long> s2combos, final String s, final List<String> patterns) {
		if (s2combos.containsKey(s))
			return s2combos.get(s);

		final List<String> sps = getPs4s(s, patterns);
		
		long cnt = 0;
		for (final String p : patterns)
			if (s.length() >= p.length() && s.startsWith(p))
				cnt += cntPossible(s2combos, s.substring(p.length()), sps);
		s2combos.put(s, cnt);
		return cnt;
	}
	
	@Override
	public void aTest() {
		assertEquals(16, new Part2().compute("test.txt").res);
	}

	@Override
	public void main() {
		assertEquals(730121486795169l, new Part2().compute("input.txt").res);
	}

}
