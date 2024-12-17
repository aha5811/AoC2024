package aha.aoc2024.day17;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;

public class Part2 extends Part1 {

	// https://adventofcode.com/2024/day/17#part2
	
	@Override
	public Part2 compute(final String file) {
		final Machine m = read(file);

		//	res = 0
		// 	for each digit but the leftmost 2
		//		current eightpower = eight ^ order of digit
		//		add eightpowers to res until digit is correct
		//	for the remaining two digits:
		//	while (quine is not readched)
		//		increase res by 1
		
		// ideally we could go do the first part for all digits
		// but then something goes wrong in digit 1
		// so we break early and do the remaining 63 steps one by one
		
		BigInteger res = BigInteger.ZERO;
		
		int digit = m.ops.length - 1;
		
		while (true) {
			final BigInteger ep = EIGHT.pow(digit);
			while (true) {
				res = res.add(ep);
				reset(m, res);
				
				m.run();
				
				if (m.out.get(digit).intValue() == m.ops[digit])
					break;
			}
			digit--;
			if (digit == 1)
				break;
		}
		
		final String ops2s = ops2s(m); // quine
		
		while (true) {
			reset(m, res);

			m.run();
			
			if (o2s(m).equals(ops2s))
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
