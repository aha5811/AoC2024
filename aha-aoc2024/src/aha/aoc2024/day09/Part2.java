package aha.aoc2024.day09;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Part2 extends Part1 {

	@Override
	public void aTest() {
		assertEquals(2858, new Part2().compute("test.txt").res);
	}
	
	@Override
	public void main() {
		// assertEquals(0, new Part2().compute("input.txt").res);
	}
	
}
