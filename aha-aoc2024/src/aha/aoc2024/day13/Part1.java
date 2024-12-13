package aha.aoc2024.day13;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;

import aha.aoc2024.Part;
import aha.aoc2024.Utils;

public class Part1 extends Part {

	// https://adventofcode.com/2024/day/13
	
	@Override
	public Part compute(final String file) {
		final List<Machine> machines = new LinkedList<>();
		Button a = null;
		Button b = null;
		for (final String line : Utils.readLines(this.dir + file))
			if (line.startsWith("Button A:"))
				a = fromLine(line, 3);
			else if (line.startsWith("Button B:"))
				b = fromLine(line, 1);
			else if (line.startsWith("Prize:")) {
				final List<Integer> is = Utils.toIs(clean(line));
				machines.add(new Machine(a, b, is.remove(0), is.get(0)));
			}

		for (final Machine m : machines)
			this.res += computeFor(m);

		return this;
	}
	
	protected long computeFor(final Machine m) {

		// as b cost < a cost first get max b pushes without overshoot

		int bp = Math.min(m.px / m.b.xi, m.py / m.b.yi);
		int ap = 0;

		while (bp > 0) {
			while (m.getX(ap, bp) < m.px && m.gety(ap, bp) < m.py)
				ap++;
			if (m.getX(ap, bp) == m.px && m.gety(ap, bp) == m.py)
				break;
			else {
				bp--;
				ap = 0;
			}
		}

		return bp * m.b.t + ap * m.a.t;
	}

	static int MAX_PER_BUTTON = 100;

	static class Machine {
		Button a;
		Button b;
		int px;
		int py;

		public Machine(final Button a, final Button b, final int px, final int py) {
			this.a = a;
			this.b = b;
			this.px = px;
			this.py = py;
		}
		
		int getX(final int pushesA, final int pushesB) {
			return pushesA * this.a.xi + pushesB * this.b.xi;
		}

		int gety(final int pushesA, final int pushesB) {
			return pushesA * this.a.yi + pushesB * this.b.yi;
		}
	}

	static class Button {
		int xi;
		int yi;
		int t; // token cost
		
		public Button(final int xi, final int yi, final int t) {
			this.xi = xi;
			this.yi = yi;
			this.t = t;
		}
	}

	private Button fromLine(final String line, final int t) {
		final List<Integer> is = Utils.toIs(clean(line));
		return new Button(is.remove(0), is.get(0), t);
	}
	
	private String clean(final String s) {
		return s.replaceAll("\\D", " ").replaceAll("\\s+", " ").trim();
	}
	
	@Override
	public void aTest() {
		assertEquals(280, new Part1().compute("test1.txt").res);
		assertEquals(0, new Part1().compute("test2.txt").res);
		assertEquals(200, new Part1().compute("test3.txt").res);
		assertEquals(0, new Part1().compute("test4.txt").res);
		assertEquals(480, new Part1().compute("test.txt").res);
	}

	@Override
	public void main() {
		assertEquals(26810, new Part1().compute("input.txt").res);
	}
	
}
