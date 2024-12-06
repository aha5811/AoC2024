package aha.aoc2024.day06;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import aha.aoc2024.Utils.CharMap;
import aha.aoc2024.Utils.Symbol;

public class Part2 extends Part1 {

	private Collection<Pos> obs;
	private List<Symbol> blocks;

	@Override
	protected void doInit(final CharMap cm) {
		this.obs = new HashSet<>();
		this.blocks = cm.getAll('#');
	}

	@Override
	protected void doAfterMove(final CharMap cm, final Guard g) {

		final Pos potObsPos = g.nextPos();
		
		if (this.obs.contains(potObsPos))
			return;
		
		final Character c = cm.getChar(potObsPos.x, potObsPos.y);
		if (c == null || c == '#') // outside or there is already a block
			return;

		if (goodObs(cm, potObsPos, g))
			this.obs.add(potObsPos);

	}
	
	private boolean goodObs(final CharMap cm, final Pos obs, final Guard g) {
		final Guard testG = g.copy();
		testG.turn();
		return hasBlockInSight(testG) && isOnLoop(cm, obs, testG);
	}
	
	private boolean hasBlockInSight(final Guard g) {
		final Dir dir = g.dir();
		for (final Symbol b : this.blocks)
			if ((b.x == g.x || b.y == g.y) // block in same column or same row
					&& (dir == U && b.x == g.x && b.y < g.y
					|| dir == R && b.y == g.y && b.x > g.x
					|| dir == D && b.x == g.x && b.y > g.y
					|| dir == L && b.y == g.y && b.x < g.x)) // block in sight
				return true;
		return false;
	}
	
	private boolean isOnLoop(final CharMap cm, final Pos obs, final Guard g) {
		boolean ret = false;

		final List<Guard> trace = new LinkedList<>();
		
		while (true) {

			g.move();
			
			if (trace.contains(g)) {
				ret = true;
				break;
			} else
				trace.add(g.copy());
			
			final Pos next = g.nextPos();
			if (cm.isOutside(next.x, next.y))
				break;
			else if (cm.getChar(next.x, next.y) == '#' || next.equals(obs))
				g.turn();

		}

		return ret;
	}

	@Override
	protected void computeRes(final CharMap cm) {
		this.res = this.obs.size();
	}

	// example
	// (3,6), (6,7), (7,7), (1,8), (3,8), (7,9)

	@Override
	public void aTest() {
		assertEquals(6, new Part2().compute("test.txt").res);
	}
	
	@Override
	public void main() {
		// assertEquals(1512, new Part2().compute("input.txt").res); // 13s
	}
	
}
