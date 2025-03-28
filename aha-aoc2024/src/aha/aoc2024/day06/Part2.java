package aha.aoc2024.day06;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import aha.aoc2024.Utils.CharMap;
import aha.aoc2024.Utils.Pos;
import aha.aoc2024.Utils.Symbol;

public class Part2 extends Part1 {
	
	// https://adventofcode.com/2024/day/6#part2
	
	private Collection<Pos> obs;
	private List<Symbol> blocks;
	
	@Override
	protected void doInit(final CharMap cm) {
		this.obs = new HashSet<>();
		this.blocks = cm.getAll('#');
	}
	
	@Override
	protected void doAfterChange(final CharMap cm, final Guard g) {

		final Pos potObs = g.nextPos();

		if (this.obs.contains(potObs))
			return;

		final Character c = cm.getChar(potObs.x, potObs.y);
		if (c == null || c == '#') // outside or there is already a block
			return;
		
		if (g.visited(potObs))
			return;
		
		if (goodObs(cm, potObs, g))
			this.obs.add(potObs);
		
	}
	
	// example
	// (3,6), (6,7), (7,7), (1,8), (3,8), (7,9)
	
	private boolean goodObs(final CharMap cm, final Pos obs, final Guard g) {
		final Guard gTmp = g.copy();
		gTmp.turn();
		return hasBlockInSight(gTmp) && willLoop(cm, obs, gTmp);
	}

	private boolean hasBlockInSight(final Guard g) {
		final Dir dir = g.dir();
		for (final Symbol b : this.blocks)
			if (dir == U && b.x == g.x() && b.y < g.y()
			|| dir == R && b.y == g.y() && b.x > g.x()
			|| dir == D && b.x == g.x() && b.y > g.y()
			|| dir == L && b.y == g.y() && b.x < g.x())
				return true;
		return false;
	}

	private boolean willLoop(final CharMap cm, final Pos obsPos, final Guard g) {
		boolean ret = false;
		
		while (true) {

			if (g.isLoop()) {
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
	protected void computeRes(final CharMap cm) {
		this.res = this.obs.size();
	}

	@Override
	public void aTest() {
		assertEquals(6, new Part2().compute("test.txt").res);
	}

	@Override
	public void main() {
		assertEquals(1575, new Part2().compute("input.txt").res); // 39s
	}

}
