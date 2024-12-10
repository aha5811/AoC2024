package aha.aoc2024.day09;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Part2 extends Part1 {

	// https://adventofcode.com/2024/day/9#part2
	
	@Override
	protected void compact(final int[] fs) {
		
		final List<Block>
		files = new LinkedList<>(),
		spaces = new LinkedList<>();
		
		readInOrder(fs, files, spaces);
		
		files.remove(0); // first file can't be moved
		Collections.reverse(files); // order of move tries

		for (final Block f : files) {
			Block emptySpace = null; // little speedup for fewer comparisons
			move: for (final Block s : spaces) {
				if (s.p > f.p) // only move left
					break move;
				if (s.l >= f.l) {
					// move file
					for (int i = 0; i < f.l; i++) {
						fs[s.p + i] = f.id;
						fs[f.p + i] = -1;
					}
					// resize space
					s.p += f.l;
					s.l -= f.l;
					// check for no space
					if (s.l == 0)
						emptySpace = s;
					break move;
				}
			}
			if (emptySpace != null)
				spaces.remove(emptySpace);
		}

	}

	private void readInOrder(final int[] fs, final List<Block> files, final List<Block> spaces) {
		
		final List<Block> blocks = new LinkedList<>();
		{
			int start = -1;
			int id = -2;
			
			for (int p = 0; p < fs.length; p++)
				if (fs[p] != id) {
					if (start != -1)
						blocks.add(new Block(start, p - start, id));
					start = p;
					id = fs[p];
				}
			if (id != -1)
				blocks.add(new Block(start, fs.length - start, id));
		}

		for (final Block b : blocks)
			if (b.id == -1)
				spaces.add(b);
			else
				files.add(b);
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
		assertEquals(6307653242596l, new Part2().compute("input.txt").res);
	}
	
}
