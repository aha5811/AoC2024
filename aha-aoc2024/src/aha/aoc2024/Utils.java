package aha.aoc2024;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {

	private final static String DIR = "/" + Utils.class.getPackageName().replace('.', '/') + "/";
	
	public static Stream<String> streamLines(final String file) {
		try {
			return Files.lines(Paths.get(Utils.class.getResource(DIR + file).toURI()));
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<String> readLines(final String file) {
		return streamLines(file).collect(Collectors.toList());
	}
	
	public static String reverse(final String s) {
		return new StringBuilder(s).reverse().toString();
	}
	
	private static Stream<String> toSs(final String s) {
		return Arrays.stream(s.trim().split("\\s+"));
	}
	
	public static List<Integer> toIs(final String s) {
		return toSs(s).map(Integer::parseInt).collect(Collectors.toList());
	}
	
	public static List<Long> toLs(final String s) {
		return toSs(s).map(Long::parseLong).collect(Collectors.toList());
	}

	public static <T> boolean same(final List<T> ts1, final List<T> ts2) {
		if (ts1.size() != ts2.size())
			return false;
		final Iterator<T> ts2iter = ts2.iterator();
		for (final T t1 : ts1)
			if (!t1.equals(ts2iter.next()))
				return false;
		return true;
	}

	/*
	 * least common multiple
	 */
	public static long lcm(final long a, final long b) {
		return a * b / gcd(a, b);
	}

	/*
	 * greatest common divisor
	 */
	public static long gcd(final long a, final long b) {
		return b == 0 ? a : gcd(b, a % b);
	}
	
	public static class CharMap {
		public final int w;
		public final int h;
		public final char[][] chars;
		
		public CharMap(final List<String> lines) {
			this.h = lines.size();
			this.w = lines.get(0).length();
			this.chars = new char[this.w][this.h];
			int y = 0;
			for (final String line : lines) {
				for (int x = 0; x < this.w; x++)
					this.chars[x][y] = line.charAt(x);
				y++;
			}
		}
		
		public CharMap(final String file) {
			this(Utils.readLines(file));
		}

		public CharMap(final int w, final int h, final char init) {
			this.w = w;
			this.h = h;
			this.chars = new char[this.w][this.h];
			for (int x = 0; x < w; x++)
				for (int y = 0; y < h; y++)
					this.chars[x][y] = init;
		}

		public List<Character> getChars() {
			final Collection<Character> chars = new HashSet<>();
			for (int x = 0; x < this.w; x++)
				for (int y = 0; y < this.h; y++)
					chars.add(this.chars[x][y]);
			return new LinkedList<>(chars);
		}
		
		public boolean isOutside(final int x, final int y) {
			return x < 0 || y < 0 || x >= this.w || y >= this.h;
		}

		public Character getChar(final int x, final int y) {
			return isOutside(x, y) ? null : this.chars[x][y];
		}

		public void setChar(final int x, final int y, final char c) {
			if (!isOutside(x, y))
				this.chars[x][y] = c;
		}

		public Symbol getSymbol(final int x, final int y) {
			final Character c = getChar(x, y);
			return c == null ? null : new Symbol(c, x, y);
		}

		@Override
		public String toString() {
			String ret = "";
			for (int y = 0; y < this.h; y++) {
				String line = "";
				for (int x = 0; x < this.w; x++)
					line += getChar(x, y);
				ret += line + "\n";
			}
			return ret;
		}

		public List<Symbol> getAll(final char c) {
			final List<Symbol> ret = new ArrayList<>();
			for (int x = 0; x < this.w; x++)
				for (int y = 0; y < this.h; y++) {
					final Symbol s = getSymbol(x, y);
					if (s.c == c)
						ret.add(s);
				}
			return ret;
		}

		/**
		 * get the word in direction dir with length l, starting with the next char in direction
		 * offset != 0 moves the starting x,y wrt to given direction
		 */
		public String getWord(int x, int y, final int[] dir, final int l, final int offset) {
			if (offset != 0) {
				x = x + offset * dir[0];
				y = y + offset * dir[1];
			}

			String ret = "";
			for (int i = 0; i < l; i++) {
				final Character c = getChar(x = x + dir[0], y = y + dir[1]);
				if (c != null)
					ret += c;
			}
			return ret;
		}
	}

	public static class Symbol extends Pos {
		public final char c;

		public Symbol(final char c, final int x, final int y) {
			super(x, y);
			this.c = c;
		}
		
		@Override
		public String toString() {
			return this.x + "," + this.y + ":" + this.c;
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.c, this.x, this.y);
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final Symbol other = (Symbol) obj;
			return this.c == other.c && this.x == other.x && this.y == other.y;
		}
	}

	public final static int[] U = new int[] { 0, -1 };
	public final static int[] D = new int[] { 0, 1 };
	public final static int[] L = new int[] { -1, 0 };
	public final static int[] R = new int[] { 1, 0 };

	public final static int[][] DIRS90 = new int[][] { U, R, D, L };

	public final static int[] N = U;
	public final static int[] NE = new int[] { 1, -1 };
	public final static int[] E = R;
	public final static int[] SE = new int[] { 1, 1 };
	public final static int[] S = D;
	public final static int[] SW = new int[] { -1, -1 };
	public final static int[] W = L;
	public final static int[] NW = new int[] { -1, 1 };

	public final static int[][] DIRS45 = new int[][] { N, NE, E, SE, S, SW, W, NW };
	
	/**
	 * removes first T to back of list
	 *
	 * @param <T>
	 * @param ts
	 * @return first T of ts before cycling
	 */
	public final static <T> T cycle(final List<T> ts) {
		final T ret = ts.remove(0);
		ts.add(ret);
		return ret;
	}
	
	public final static <K> void inc(final Map<K, Long> map, final K k, final Long m) {
		if (!map.containsKey(k))
			map.put(k, 0l);
		map.put(k, map.get(k) + m);
	}
	
	public static class Pos {
		public int x;
		public int y;
		
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

}
