package aha.aoc2024.day24;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Part2 extends Part1 {
	
	// https://adventofcode.com/2024/day/24#part2
	
	@Override
	public void aTest() {
		assertEquals(0, new Part2().compute("test.txt").res);
	}

	@Override
	public void main() {
		// assertEquals(0, new Part2().compute("input.txt").res);
	}

}
