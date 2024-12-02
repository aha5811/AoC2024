package aha.aoc2024.day02;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;

public class Part2 extends Part1 {

	@Override
	protected boolean checkSafe(final List<Integer> is) {
		if (_checkSafe(is))
			return true;
		
		for (int i = 0; i < is.size(); i++) {
			final List<Integer> itmp = new LinkedList<>(is);
			itmp.remove(i);
			if (_checkSafe(itmp))
				return true;
		}
		
		return false;
	}

	@Override
	public void aTest() {
		assertEquals(4, new Part2().compute("test.txt").res);
	}
	
	@Override
	public void main() {
		assertEquals(604, new Part2().compute("input.txt").res);
	}
	
}
