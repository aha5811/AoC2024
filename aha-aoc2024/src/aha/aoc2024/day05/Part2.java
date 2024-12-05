package aha.aoc2024.day05;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

public class Part2 extends Part1 {

	@Override
	protected void computeForPages(final List<Rule> rules, final List<Integer> is) {
		if (!isOk(rules, is))
			this.res += getMiddle(getOrdered(rules, is));
	}

	private List<Integer> getOrdered(final List<Rule> rules, final List<Integer> is) {
		// TODO
		return null;
	}
	
	@Override
	public void aTest() {
		assertEquals(123, new Part2().compute("test.txt").res);
	}
	
	@Override
	public void main() {
		assertEquals(0, new Part2().compute("input.txt").res);
	}
	
}
