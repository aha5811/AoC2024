package aha.aoc2024.day06;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import aha.aoc2024.Utils.CharMap;
import aha.aoc2024.Utils.Symbol;

public class Part2 extends Part1 {
	
	private List<Pos> os;
	private List<Symbol> blocks;
	private Map<Pos, Collection<Dir>> crossed;
	
	@Override
	protected void doInit(final CharMap cm) {
		this.os = new LinkedList<>();
		this.blocks = cm.getAll('#');
		this.crossed = new HashMap<>();
	}
	
	@Override
	protected void doAfterMove(final CharMap cm, final Guard g) {
		final Pos p = new Pos(g.x, g.y);
		
		if (this.crossed.get(p) == null)
			this.crossed.put(p, new HashSet<>());
		this.crossed.get(p).add(g.dirs.getFirst());

		final Pos next = g.nextPos();

		// is the next outside, so there can no obstacle placed
		if (cm.isOutside(next.x, next.y))
			return;

		// is the next step already a block?
		{
			final Character nextC = cm.getChar(next.x, next.y);
			if (nextC != null && nextC == '#')
				return;
		}

		// is there a block in g's sight if g turns now

		if (blockPos(g, cm))
			this.os.add(g.nextPos());
		
	}
	
	private boolean blockPos(final Guard g, final CharMap cm) {
		final Dir dir = g.dirs.get(1); // turn left
		
		for (final Symbol b : this.blocks)
			if ((b.x == g.x || b.y == g.y) // block in same column or same row
					&&
					(dir == U && b.x == g.x && b.y < g.y
					|| dir == R && b.y == g.y && b.x > g.x
					|| dir == D && b.x == g.x && b.y > g.y
					|| dir == L && b.y == g.y && b.x < g.x)
					&&
					willLoop(cm, new Pos(g.x, g.y), dir))
				return true;
		return false;
	}

	private boolean willLoop(final CharMap cm, final Pos p, final Dir dir) {
		boolean hitLoop = false;

		while (true) {
			if (this.crossed.containsKey(p) && this.crossed.get(p).contains(dir)) {
				hitLoop = true;
				break;
			}
			p.x += dir.dx;
			p.y += dir.dy;
			if (cm.getChar(p.x, p.y) == '#')
				break;
		}

		// since we check only for the way to the next block we may miss loops where we
		// have to turn on some blocks before we hit a loop
		
		return hitLoop;
	}
	
	@Override
	protected void computeRes(final CharMap cm) {
		this.res = this.os.size();
	}
	
	@Override
	public void aTest() {
		assertEquals(6, new Part2().compute("test.txt").res);
	}

	@Override
	public void main() {
		assertNotEquals(428 /* answer is too low */, new Part2().compute("input.txt").res);
	}

}
