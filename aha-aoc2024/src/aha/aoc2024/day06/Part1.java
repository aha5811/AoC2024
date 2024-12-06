package aha.aoc2024.day06;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collections;
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
		cm.setChar(g.x, g.y, 'X');
	}
	
	protected void computeRes(final CharMap cm) {
		this.res = cm.getAll('X').size();
	}
	
	protected static class Dir {
		String name;
		char c;
		int dx;
		int dy;
		
		public Dir(final String name, final char c, final int dx, final int dy) {
			this.name = name;
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
			final Dir o = (Dir) obj;
			return this.name.equals(o.name);
		}

		@Override
		public int hashCode() {
			return this.name.hashCode();
		}
	}

	protected final static Dir
	U = new Dir("U", '^', Utils.U[0], Utils.U[1]),
	R = new Dir("R", '>', Utils.R[0], Utils.R[1]),
	D = new Dir("D", 'v', Utils.D[0], Utils.D[1]),
	L = new Dir("L", '<', Utils.L[0], Utils.L[1]);
	
	protected final static List<Dir> DIRS = Collections.unmodifiableList(Arrays.asList(U, R, D, L));
	
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
	
	protected final static class Guard extends Pos {
		List<Dir> dirs = new LinkedList<>(DIRS);
		
		Guard(final int x, final int y) {
			super(x, y);
		}
		
		Pos nextPos() {
			final Dir dir = this.dirs.get(0);
			return new Pos(this.x + dir.dx, this.y + dir.dy);
		}
		
		void turn() {
			final Dir dir = this.dirs.remove(0);
			this.dirs.add(dir);
		}

		void move() {
			final Dir dir = this.dirs.get(0);
			this.x += dir.dx;
			this.y += dir.dy;
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
