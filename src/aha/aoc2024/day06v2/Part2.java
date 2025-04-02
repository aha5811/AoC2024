package aha.aoc2024.day06v2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.HashSet;

import aha.aoc2024.Utils.CharMap;
import aha.aoc2024.Utils.Pos;
import aha.aoc2024.Utils.Symbol;

public class Part2 extends Part1 {

	@Override
	protected void computeRes(final CharMap cm, final Symbol s) {
		for (final Symbol step : cm.getAll('X')) {
			if (step.x == s.x && step.y == s.y)
				continue;
			if (checkLoop(cm, step, new Guard(s.x, s.y)))
				this.res++;
		}
	}

	private boolean checkLoop(final CharMap cm, final Symbol obs, final Guard g) {

		final Collection<String> states = new HashSet<>();
		final Pos obsPos = new Pos(obs.x, obs.y);
		
		boolean ret = false;
		while (true) {
			if (!states.add(g.toString())) {
				ret = true;
				break;
			}
			
			final Pos next = g.nextPos();
			if (cm.isOutside(next.x, next.y))
				break;
			else if (cm.getChar(next.x, next.y) == '#' || next.equals(obsPos))
				g.turn();
			else
				g.move();
		}
		return ret;
	}

	@Override
	public void aTest() {
		assertEquals(6, new Part2().compute("test.txt").res);
	}
	
	@Override
	public void main() {
		assertEquals(1575, new Part2().compute("input.txt").res);
	}
	
}
