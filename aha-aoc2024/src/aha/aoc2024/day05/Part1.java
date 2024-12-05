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
		
		for (final String line : Utils.readLines(this.dir + file))
			if (line.contains("|")) // is rule
				rules.add(Rule.fromString(line));
			else if (line.contains(",")) { // we know all pages lines are below all rules lines
				final List<Integer> pages = Utils.toIs(line.replace(',', ' '));
				
				computeForPages(rules, pages);
			}
		
		return this;
	}
	
	protected void computeForPages(final List<Rule> rules, final List<Integer> pages) {
		if (satisifes(pages, rules))
			this.res += getMiddle(pages);
	}
	
	protected final boolean satisifes(final List<Integer> pages, final List<Rule> rules) {
		for (final Rule r : rules)
			if (!r.isSatisfied(pages))
				return false;
		return true;
	}
	
	protected final int getMiddle(final List<Integer> pages) {
		return pages.get((pages.size() - 1) / 2);
	}
	
	protected final static class Rule {
		protected final int first;
		protected final int second;
		
		private Rule(final int first, final int second) {
			this.first = first;
			this.second = second;
		}
		
		public static Rule fromString(final String s) {
			final List<Integer> is = Utils.toIs(s.replace('|', ' '));
			return new Rule(is.remove(0), is.get(0));
		}

		boolean isSatisfied(final List<Integer> is) {
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
