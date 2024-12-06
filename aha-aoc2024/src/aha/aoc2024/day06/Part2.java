package aha.aoc2024.day06;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import aha.aoc2024.Utils.CharMap;
import aha.aoc2024.Utils.Symbol;

public class Part2 extends Part1 {
	
	private List<Pos> os;
	private List<Symbol> blocks;
	private Map<Pos, List<Dir>> crossed;
	
	@Override
	protected void doInit(final CharMap cm) {
		this.os = new LinkedList<>();
		this.blocks = cm.getAll('#');
		this.crossed = new HashMap<>();
	}
	
	@Override
	protected void doAfterMove(final CharMap cm, final Guard g) {
		final Pos now = new Pos(g.x, g.y);
		if (this.crossed.get(now) == null)
			this.crossed.put(now, new LinkedList<>());
		this.crossed.get(now).add(g.dirs.getFirst());

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

		if (findBlock(now, g.dirs.get(1), cm))
			this.os.add(g.nextPos());
		
	}
	
	private boolean findBlock(final Pos p, final Dir dir, final CharMap cm) {
		for (final Symbol b : this.blocks)
			if (b.x == p.x || b.y == p.y) // block in same column or same row
				if (dir == U && b.x == p.x && b.y < p.y
					|| dir == R && b.y == p.y && b.x > p.x
					|| dir == D && b.x == p.x && b.y > p.y
					|| dir == L && b.y == p.y && b.x < p.x) // block in sight
				{
					// will we go on a loop?
					boolean hitLoop = false;
					final Pos test = new Pos(p.x, p.y);
					while (true) {
						final List<Dir> dirs = this.crossed.get(test);
						if (dirs != null && dirs.contains(dir)) {
							hitLoop = true;
							break;
						}
						test.x += dir.dx;
						test.y += dir.dy;
						if (cm.getChar(test.x, test.y) == '#')
							break;
					}
					if (hitLoop)
						return true;
				}
		return false;
	}
	
	@Override
	protected void computeRes(final CharMap cm) {
		/*
		for (final Pos O : this.os) {
			System.out.println(O.x + "x" + O.y + ":");
			final Character c = cm.getChar(O.x, O.y);
			if (c != null) {
				cm.setChar(O.x, O.y, 'O');
				System.out.println(cm);
				cm.setChar(O.x, O.y, c);
			} else
				System.out.println("outside");
		}
		//*/
		this.res = this.os.size();
	}
	
	@Override
	public void aTest() {
		assertEquals(6, new Part2().compute("test.txt").res);
	}

	@Override
	public void main() {
		assertEquals(428 + 1 /* your answer is too low */, new Part2().compute("input.txt").res);
	}

}
