package aha.aoc2024.day16;

import static org.junit.jupiter.api.Assertions.assertEquals;

import aha.aoc2024.Part;
import aha.aoc2024.Utils.CharMap;
import aha.aoc2024.Utils.Symbol;

public class Part2 extends Part1 {
	
	// https://adventofcode.com/2024/day/16#part2
	
	// idea: after search add trace positions to positions collection
	// next search: to go to a pos from positions costs 1 more
	// do searches until search does not add more positions
	
	@Override
	public Part compute(final String file) {
		final CharMap cm = new CharMap(this.dir + file);

		final Symbol S = cm.getAll('S').get(0), E = cm.getAll('E').get(0);

		while (true) {
			
			final Result ar = doSearch(cm);

			final int Osize1 = cm.getAll('O').size();

			for (final State s : ar.trace)
				cm.setChar(s.x, s.y, 'O');

			cm.setChar(S.x, S.y, S.c);
			cm.setChar(E.x, E.y, E.c);
			
			final int Osize2 = cm.getAll('O').size();
			
			if (Osize1 == Osize2)
				break;
		}

		// System.out.println(cm);
		
		this.res = cm.getAll('O').size() + 2;
		
		return this;
	}
	
	@Override
	public void aTest() {
		assertEquals(45, new Part2().compute("test1.txt").res);
		assertEquals(64, new Part2().compute("test.txt").res);
	}

	@Override
	public void main() {
		// assertEquals(435, new Part2().compute("input.txt").res); // 17s
	}

}
