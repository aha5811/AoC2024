package aha.aoc2024.day01;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

public class Part2 extends Part1 {
	
	private long countN(final int n, final List<Integer> ns) {
		final int s = ns.size();
		ns.removeIf(i -> i == n);
		return s - ns.size();
	}
	
	@Override
	protected void toRes(final List<Integer> firsts, final List<Integer> seconds) {
		while (!firsts.isEmpty()) {
			final int n = firsts.get(0);
			this.res += countN(n, firsts) * n * countN(n, seconds);
		}
	}
	
	@Override
	public void aTest() {
		assertEquals(31, new Part2().compute("test.txt").res);
	}

	@Override
	public void main() {
		assertEquals(23177084, new Part2().compute("input.txt").res);
	}

}
