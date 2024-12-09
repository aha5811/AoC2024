package aha.aoc2024.day09;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Part2 extends Part1 {

	@Override
	protected int compact(final int[] fs, final int end) {
		final List<Block> blocks = new LinkedList<>();

		int start = -1;
		int id = -2;
		
		for (int p = 0; p < end; p++)
			if (fs[p] != id) {
				if (start != -1)
					blocks.add(new Block(start, p - start, id));
				start = p;
				id = fs[p];
			}
		if (id != -1)
			blocks.add(new Block(start, end - start + 1, id));

		final List<Block> files = new LinkedList<>(), spaces = new LinkedList<>();
		
		for (final Block b : blocks)
			if (b.id == -1)
				spaces.add(b);
			else
				files.add(b);

		Collections.reverse(files);

		for (final Block f : files)
			move:
				for (final Block space : spaces)
					if (space.l >= f.l) {
						for (int i = 0; i < f.l; i++) {
							fs[space.p + i] = f.id;
							fs[f.p + i] = -1;
						}
						space.p += f.l;
						space.l -= f.l;
						break move;
					}
		
		return end;
	}

	static class Block {
		int p;
		int l;
		int id;

		public Block(final int p, final int l, final int id) {
			this.p = p;
			this.l = l;
			this.id = id;
		}

		@Override
		public String toString() {
			return (this.id == -1 ? "X" : this.id) + ":" + this.p + "/" + this.l;
		}
	}
	
	@Override
	public void aTest() {
		assertEquals(2858, new Part2().compute("test.txt").res);
	}
	
	@Override
	public void main() {
		assertEquals(0, new Part2().compute("input.txt").res);
		// 8449767616409l is too high
	}
	
}