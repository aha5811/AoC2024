package aha.aoc2024.day17;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;

public class Part2 extends Part1 {

	// https://adventofcode.com/2024/day/17#part2
	
	@Override
	public Part2 compute(final String file) {
		final Machine m = read(file);

		BigInteger res = BigInteger.ZERO;
		
		int digit = m.ops.length - 1;
		
		while (true) {
			final BigInteger c = EIGHT.pow(digit);
			BigInteger mult = BigInteger.ONE;
			BigInteger a;
			while (true) {
				a = res.add(c.multiply(mult));
				reset(m, a);
				m.run();
				final int[] o2i = o2i(m);
				if (m.ops[digit] == o2i[digit])
					break;
				mult = mult.add(BigInteger.ONE);
			}
			res = a;
			digit--;
			if (digit == 1)
				break;
		}
		
		final String ops2s = ops2s(m);
		
		while (true) {
			reset(m, res);
			m.run();
			final String o2s = o2s(m);
			if (o2s.equals(ops2s))
				break;
			res = res.add(BigInteger.ONE);
		}
		
		this.res = res.toString();
		
		return this;
	}

	private String ops2s(final Machine m) {
		String ret = "";
		for (final int op : m.ops)
			ret += "," + op;
		ret = ret.substring(1);
		return ret;
	}
	
	private int[] o2i(final Machine m) {
		final int[] ret = new int[m.out.size()];
		int i = 0;
		for (final BigInteger bi : m.out)
			ret[i++] = bi.intValue();
		return ret;
	}
	
	private void reset(final Machine m, final BigInteger a) {
		m.a = a;
		m.b = BigInteger.ZERO;
		m.c = BigInteger.ZERO;
		m.p = 0;
		m.out.clear();
	}

	@Override
	public void aTest() {
		// https://www.rapidtables.com/convert/number/octal-to-decimal.html
		// 0,3,5,4,3,0 -> 034530 -- oct to dec -> 14680 -- * 8 -> 117440
		assertEquals("117440", new Part2().compute("test2.txt").res);
		// devious because the other program does something completely different
	}
	
	@Override
	public void main() {
		assertEquals("164278899142333", new Part2().compute("input.txt").res);
	}
	
}
