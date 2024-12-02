package aha.aoc2024.day02;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;

public class Part2 extends Part1 {
	
	@Override
	protected boolean checkSafe(final List<Integer> ns) {
		if (_checkSafeMod(new LinkedList<>(ns)))
			return true;

		for (int i = 0; i < ns.size(); i++) {
			final List<Integer> nsTmp = new LinkedList<>(ns);
			nsTmp.remove(i);
			if (_checkSafeMod(nsTmp))
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
