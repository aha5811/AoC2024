package aha.aoc2024.day19;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import aha.aoc2024.Part;
import aha.aoc2024.Utils;

public class Part1 extends Part {

	// https://adventofcode.com/2024/day/19
	
	@Override
	public Part compute(final String file) {
		
		final List<String> designs = new LinkedList<>();
		List<String> patterns = new LinkedList<>();
		read(file, designs, patterns);

		patterns = compact(patterns);
		
		for (final String d : designs)
			if (possible(d, patterns))
				// System.out.println(d);
				this.res++;
		
		return this;
	}

	protected void read(final String file, final List<String> designs, final List<String> patterns) {
		for (final String line : Utils.readLines(this.dir + file))
			if (line.contains(","))
				patterns.addAll(Arrays.asList(line.replace(" ", "").split(",")));
			else if (line.trim().isEmpty())
				continue;
			else
				designs.add(line);
	}
	
	/**
	 * returns a list where all patterns that can be build by other patterns in the
	 * list are removed
	 *
	 * @param patterns
	 * @return
	 */
	protected List<String> compact(final List<String> patterns) {
		Collections.sort(patterns, new Comparator<String>() {
			@Override
			public int compare(final String s1, final String s2) {
				return -1 * Integer.compare(s1.length(), s2.length());
			}
		});
		
		List<String> ret = patterns;
		
		while (true) {
			final List<String> tmp = new LinkedList<>(ret);
			boolean compacted = false;
			final List<String> notCompactable = new LinkedList<>();
			while (!tmp.isEmpty()) {
				final String p = tmp.removeFirst();
				if (possible(p, tmp))
					compacted = true;
				else
					notCompactable.add(p);
			}
			if (!compacted)
				break;
			else
				ret = notCompactable;
		}

		return ret;
	}
	
	private boolean possible(final String s, final List<String> patterns) {
		if (s.isEmpty())
			return true;
		
		final List<String> sps = getPs4s(s, patterns);

		boolean ret = false;
		for (final String p : patterns)
			if (s.length() >= p.length() && s.startsWith(p))
				if (possible(s.substring(p.length()), sps)) {
					ret = true;
					break;
				}
		return ret;
	}
	
	/**
	 * returns only those patterns that could appear anywhere in the string
	 *
	 * @param t       target
	 * @param strings
	 * @return
	 */
	protected List<String> getPs4s(final String t, final List<String> strings) {
		final List<String> ret = new LinkedList<>();
		for (final String s : strings)
			if (t.length() >= s.length() && t.contains(s))
				ret.add(s);
		return ret;
	}
	
	@Override
	public void aTest() {
		assertEquals(6, new Part1().compute("test.txt").res);
	}

	@Override
	public void main() {
		assertEquals(365, new Part1().compute("input.txt").res);
	}
	
}
