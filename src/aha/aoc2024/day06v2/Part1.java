package aha.aoc2024.day06v2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import aha.aoc2024.Part;
import aha.aoc2024.Utils;
import aha.aoc2024.Utils.CharMap;
import aha.aoc2024.Utils.Pos;
import aha.aoc2024.Utils.Symbol;

public class Part1 extends Part {
	
	@Override
	public Part compute(final String file) {
		final CharMap cm = new CharMap(this.dir + file);

		final Symbol s = cm.getAll('^').get(0);

		final Guard g = new Guard(s.x, s.y);

		while (true) {
			final Pos next = g.nextPos();
			if (cm.isOutside(next.x, next.y))
				break;
			else if (cm.getChar(next.x, next.y) == '#')
				g.turn();
			else {
				g.move();
				cm.setChar(g.x(), g.y(), 'X');
			}
		}
		
		computeRes(cm, s);
		
		return this;
	}

	protected void computeRes(final CharMap cm, final Symbol s) {
		this.res = cm.getAll('X').size();
	}

	protected static class Dir {
		char c;
		int dx;
		int dy;

		public Dir(final char c, final int dx, final int dy) {
			this.c = c;
			this.dx = dx;
			this.dy = dy;
		}
		
		@Override
		public boolean equals(final Object obj) {
			if (obj == this)
				return true;
			if (obj == null || !(obj instanceof Dir))
				return false;
			return this.c == ((Dir) obj).c;
		}
	}
	
	protected final static Dir
	U = new Dir('^', Utils.U[0], Utils.U[1]),
	R = new Dir('>', Utils.R[0], Utils.R[1]),
	D = new Dir('v', Utils.D[0], Utils.D[1]),
	L = new Dir('<', Utils.L[0], Utils.L[1]);

	protected final static Dir[] DIRS = new Dir[] { U, R, D, L };

	protected final static class Guard {
		
		private final Pos p;
		private int nDir = 0;
		
		Guard(final int x, final int y) {
			this.p = new Pos(x, y);
		}

		int x() { return this.p.x; }
		int y() { return this.p.y; }
		Dir dir() { return DIRS[this.nDir]; }
		
		Pos nextPos() {
			return new Pos(x() + dir().dx, y() + dir().dy);
		}
		
		void turn() {
			this.nDir = (this.nDir + 1) % 4;
		}
		
		void move() {
			this.p.x += dir().dx;
			this.p.y += dir().dy;
		}
		
		@Override
		public String toString() {
			return this.p.toString() + dir().c;
		}
	}
	
	@Override
	public void aTest() {
		assertEquals(41, new Part1().compute("test.txt").res);
	}
	
	@Override
	public void main() {
		assertEquals(4656, new Part1().compute("input.txt").res);
	}

}
