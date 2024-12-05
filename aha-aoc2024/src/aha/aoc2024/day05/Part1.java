package aha.aoc2024.day05;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;

import aha.aoc2024.Part;
import aha.aoc2024.Utils;

public class Part1 extends Part {
	
	@Override
	public Part compute(final String file) {
		final List<Rule> rules = new LinkedList<>();
		final List<List<Integer>> pagesList = new LinkedList<>();
		
		for (final String line : Utils.readLines(this.dir + file))
			if (line.contains("|"))
				rules.add(Rule.fromString(line));
			else if (line.contains(","))
				pagesList.add(Utils.toIs(line.replace(',', ' ')));
		
		initFor(rules);

		for (final List<Integer> is : pagesList)
			computeForPages(rules, is);

		return this;
	}

	protected void initFor(final List<Rule> rules2) {
	}
	
	protected void computeForPages(final List<Rule> rules, final List<Integer> is) {
		if (isOk(rules, is))
			this.res += getMiddle(is);
	}

	protected final boolean isOk(final List<Rule> rules, final List<Integer> is) {
		for (final Rule r : rules)
			if (!r.isOk(is))
				return false;
		return true;
	}

	protected final int getMiddle(final List<Integer> is) {
		return is.get((is.size() - 1) / 2);
	}

	protected static class Rule {
		protected final int first;
		protected final int second;

		private Rule(final int first, final int second) {
			this.first = first;
			this.second = second;
		}

		public static Rule fromString(final String s) {
			final List<Integer> is = Utils.toIs(s.replace('|', ' '));
			return new Rule(is.remove(0), is.remove(0));
		}
		
		boolean isApplicable(final int i1, final int i2) {
			return (i1 == this.first || i1 == this.second) && (i2 == this.first || i2 == this.second);
		}

		boolean isOk(final List<Integer> is) {
			final int fi = is.indexOf(this.first), si = is.indexOf(this.second);
			return fi == -1 || si == -1 || fi < si;
		}
	}

	@Override
	public void aTest() {
		assertEquals(143, new Part1().compute("test.txt").res);
	}
	
	@Override
	public void main() {
		assertEquals(4872, new Part1().compute("input.txt").res);
	}

}
