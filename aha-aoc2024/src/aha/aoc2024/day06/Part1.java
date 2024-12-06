package aha.aoc2024.day06;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;

import aha.aoc2024.Part;
import aha.aoc2024.Utils;
import aha.aoc2024.Utils.CharMap;
import aha.aoc2024.Utils.Symbol;

public class Part1 extends Part {

	@Override
	public Part compute(final String file) {
		final CharMap cm = new CharMap(Utils.readLines(this.dir + file));
		
		doInit(cm);
		
		final Symbol s = cm.getAll('^').get(0);
		
		final Guard g = new Guard(s.x, s.y);
		
		doAfterMove(cm, g);
		
		while (true) {
			final Pos next = g.nextPos();
			final Character nextC = cm.getChar(next.x, next.y);
			if (nextC == null)
				break;
			else if (nextC == '#')
				g.turn();
			else {
				g.move();
				doAfterMove(cm, g);
			}
		}

		computeRes(cm);

		return this;
	}
	
	protected void doInit(final CharMap cm) {
		// extension for Part2
	}
	
	protected void doAfterMove(final CharMap cm, final Guard g) {
		cm.setChar(g.x(), g.y(), 'X');
	}
	
	protected void computeRes(final CharMap cm) {
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
	
	protected static class Pos {
		protected int x;
		protected int y;

		public Pos(final int x, final int y) {
			this.x = x;
			this.y = y;
		}
		
		@Override
		public boolean equals(final Object obj) {
			if (obj == this)
				return true;
			if (obj == null || !(obj instanceof Pos))
				return false;
			final Pos o = (Pos) obj;
			return this.x == o.x && this.y == o.y;
		}

		@Override
		public String toString() {
			return "(" + this.x + "," + this.y + ")";
		}

		@Override
		public int hashCode() {
			return toString().hashCode();
		}
	}
	
	protected final static class Guard {
		Pos p;
		int nDir = 0;

		List<Guard> trace = new LinkedList<>();
		
		Guard(final int x, final int y) {
			this.p = new Pos(x, y);
		}
		
		int x() { return this.p.x; }
		int y() { return this.p.y; }
		Dir dir() { return DIRS[this.nDir]; }
		Pos pos() { return new Pos(this.p.x, this.p.y); }
		
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

		Guard copy() {
			final Guard ret = new Guard(x(), y());
			ret.nDir = this.nDir;
			ret.trace.addAll(this.trace);
			return ret;
		}

		@Override
		public String toString() {
			return this.p.toString() + dir().c;
		}
		
		@Override
		public boolean equals(final Object obj) {
			if (obj == this)
				return true;
			if (obj == null || !(obj instanceof Guard))
				return false;
			final Guard o = (Guard) obj;
			return pos().equals(o.pos()) && dir() == o.dir();
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
