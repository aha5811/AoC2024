package aha.aoc2024.day14;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import aha.aoc2024.Part;
import aha.aoc2024.Utils.Pos;

public class Part2 extends Part1 {
	
	// https://adventofcode.com/2024/day/14#part2

	public static int findSecond() {

		// by looking at outputs
		// horizontal 'compression' @ 9 && 110
		// 9 110 = 9 + n * 101 (= w)
		// vertical 'compression' @ 65 && 168
		// 65 168 -> 65 + n * 103 (= h)

		int i = 0;
		while (true) {
			if ((i - 9) % 101 == 0 && (i - 65) % 103 == 0)
				return i;
			i++;
		}
	}

	@Override
	public Part compute(final String file) {
		
		final int secs = findSecond();
		
		final List<Robot> robots = read(file);

		doRobots(robots, secs);

		final Map<Pos, Long> p2n = getP2N(robots);
		
		out(p2n, 0, this.w, 0, this.h);

		System.out.println("day14 part 2 solution = " + secs);
		
		return this;
	}

	@Override
	public void aTest() {
		// do nothing
	}

	@Override
	public void main() {
		assertEquals(0, new Part2().setW(WIDTH).setH(HEIGHT).compute("input.txt").res);
	}
	
	/*
1111111111111111111111111111111
1.............................1
1.............................1
1.............................1
1.............................1
1..............1..............1
1.............111.............1
1............11111............1
1...........1111111...........1
1..........111111111..........1
1............11111............1
1...........1111111...........1
1..........111111111..........1
1.........11111111111.........1
1........1111111111111........1
1..........111111111..........1
1.........11111111111.........1
1........1111111111111........1
1.......111111111111111.......1
1......11111111111111111......1
1........1111111111111........1
1.......111111111111111.......1
1......11111111111111111......1
1.....1111111111111111111.....1
1....111111111111111111111....1
1.............111.............1
1.............111.............1
1.............111.............1
1.............................1
1.............................1
1.............................1
1.............................1
1111111111111111111111111111111
	 */

}
