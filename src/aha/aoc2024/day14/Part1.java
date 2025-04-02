package aha.aoc2024.day14;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import aha.aoc2024.Part;
import aha.aoc2024.Utils;
import aha.aoc2024.Utils.Pos;

public class Part1 extends Part {

	// https://adventofcode.com/2024/day/14

	final static int TEST_W = 11, TEST_H = 7, WIDTH = 101, HEIGHT = 103;

	int w = -1, h = -1, secs = -1;
	
	private Part1 setSecs(final int s) {
		this.secs = s;
		return this;
	}

	protected Part1 setW(final int w) {
		this.w = w;
		return this;
	}

	protected Part1 setH(final int h) {
		this.h = h;
		return this;
	}

	@Override
	public Part compute(final String file) {
		
		final List<Robot> robots = read(file);

		doRobots(robots, this.secs);
		
		final Map<Pos, Long> p2n = getP2N(robots);

		final int w2 = this.w / 2, h2 = this.h / 2;
		
		this.res = count(p2n, 0, w2, 0, h2)
				* count(p2n, this.w - w2, this.w, 0, h2)
				* count(p2n, 0, w2, this.h - h2, this.h)
				* count(p2n, this.w - w2, this.w, this.h - h2, this.h);

		return this;
	}
	
	protected void doRobots(final List<Robot> robots, final int secs) {
		for (int s = 0; s < secs; s++)
			for (final Robot r : robots) {

				r.x = (r.x + r.dx) % this.w;
				if (r.x < 0)
					r.x += this.w;

				r.y = (r.y + r.dy) % this.h;
				if (r.y < 0)
					r.y += this.h;
				
			}
	}
	
	protected List<Robot> read(final String file) {
		final List<Robot> robots = new LinkedList<>();
		for (String line : Utils.readLines(this.dir + file)) {
			line = line.replaceAll("[^\\d\\-]", " ").replaceAll("\\s+", " ").trim();
			final List<Integer> is = Utils.toIs(line);
			robots.add(new Robot(is.remove(0), is.remove(0), is.remove(0), is.get(0)));
		}
		return robots;
	}
	
	protected Map<Pos, Long> getP2N(final List<Robot> robots) {
		final Map<Pos, Long> p2n = new HashMap<>();
		for (final Robot r : robots)
			Utils.inc(p2n, (Pos) r, 1l);
		return p2n;
	}
	
	private int count(final Map<Pos, Long> p2n, final int x1, final int x2, final int y1, final int y2) {
		int ret = 0;
		for (int x = x1; x < x2; x++)
			for (int y = y1; y < y2; y++) {
				final Long l = p2n.get(new Pos(x, y));
				ret += l == null ? 0 : l;
			}
		return ret;
	}

	protected void out(final Map<Pos, Long> p2n, final int x1, final int x2, final int y1, final int y2) {
		for (int y = y1; y < y2; y++) {
			String s = "";
			for (int x = x1; x < x2; x++) {
				final Long l = p2n.get(new Pos(x, y));
				s += l == null ? "." : l;
			}
			System.out.println(s);
		}
	}
	
	static class Robot extends Pos {
		int dx;
		int dy;
		
		public Robot(final int x, final int y, final int dx, final int dy) {
			super(x, y);
			this.dx = dx;
			this.dy = dy;
		}
	}
	
	@Override
	public void aTest() {
		assertEquals(12, new Part1().setSecs(100).setW(TEST_W).setH(TEST_H).compute("test.txt").res);
	}
	
	@Override
	public void main() {
		assertEquals(236628054, new Part1().setSecs(100).setW(WIDTH).setH(HEIGHT).compute("input.txt").res);
	}
	
}
