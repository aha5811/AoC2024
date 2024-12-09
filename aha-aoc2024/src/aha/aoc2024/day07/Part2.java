package aha.aoc2024.day07;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Part2 extends Part1 {
	
	static F Conc = new F() {
		@Override
		public long f(final long l1, final long l2) {
			return Long.parseLong(Long.toString(l1) + Long.toString(l2));
		}
	};
	
	@Override
	protected F[] getFs() { return new F[] { Plus, Mult, Conc }; }
	
	@Override
	public void aTest() {
		assertEquals(11387, new Part2().compute("test.txt").res);
	}

	@Override
	public void main() {
		assertEquals(275791737999003l, new Part2().compute("input.txt").res);
	}

}
