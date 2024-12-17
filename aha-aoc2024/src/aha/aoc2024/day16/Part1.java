package aha.aoc2024.day16;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import aha.aoc2024.Part;
import aha.aoc2024.Utils;
import aha.aoc2024.Utils.CharMap;
import aha.aoc2024.Utils.Pos;
import aha.aoc2024.Utils.Symbol;

public class Part1 extends Part {
	
	// https://adventofcode.com/2024/day/16

	final static int TURN_COST = 1000, STEP_COST = 1;
	
	static class Result {
		List<State> trace;
		int cost;
	}

	static class State extends Pos {
		int[] dir;
		
		public State(final int x, final int y, final int[] dir) {
			super(x, y);
			this.dir = dir;
		}

		@Override
		public String toString() {
			return super.toString() + Part1.toString(this.dir);
		}

		// without this method part1 takes <1s, but part2 won't work at all
		@Override
		public boolean equals(final Object obj) {
			if (obj == this)
				return true;
			if (obj == null || !(obj instanceof State))
				return false;
			final State other = (State) obj;
			return this.x == other.x && this.y == other.y && this.dir == other.dir;
		}
	}
	
	private static String toString(final int[] dir) {
		if (dir == Utils.U)
			return "^";
		else if (dir == Utils.D)
			return "v";
		else if (dir == Utils.R)
			return ">";
		else if (dir == Utils.L)
			return "<";
		return "?";
	}

	@Override
	public Part compute(final String file) {
		final CharMap cm = new CharMap(this.dir + file);
		
		this.res = doSearch(cm).cost;

		return this;
	}
	
	protected Result doSearch(final CharMap cm) {
		Result aRes = null;
		
		final List<State> open = new LinkedList<>();
		final List<State> closed = new LinkedList<>();
		final Map<State, State> from = new HashMap<>();
		final Map<State, Integer> g = new HashMap<>(), f = new HashMap<>();

		Pos e;
		{
			final Symbol ss = cm.getAll('S').get(0);
			final State s = new State(ss.x, ss.y, Utils.R);

			final Symbol es = cm.getAll('E').get(0);
			e = new Pos(es.x, es.y);
			
			open.add(s);

			g.put(s, 0);
			f.put(s, h(s, e));
		}
		
		while (!open.isEmpty()) {
			
			Collections.sort(open, new Comparator<Pos>() {
				@Override
				public int compare(final Pos p1, final Pos p2) {
					return Integer.compare(f.get(p1), f.get(p2));
				}
			});

			final State c = open.remove(0);
			closed.add(c);

			if (h(c, e) == 0) {
				aRes = new Result();
				aRes.cost = g.get(c);
				aRes.trace = new LinkedList<>();
				{
					State s = c;
					while ((s = from.get(s)) != null)
						aRes.trace.add(0, s);
				}
				break;
			}

			for (final int[] dir : Utils.DIRS90) {
				final State n = new State(c.x + dir[0], c.y + dir[1], dir);
				if (cm.getChar(n.x, n.y) == '#')
					continue;
				if (closed.contains(n))
					continue;

				int gnext = g.get(c) + getTurns(c.dir, n.dir) * TURN_COST + 1 * STEP_COST;
				if (cm.getChar(n.x, n.y) == 'O')
					gnext++;
				
				if (!g.containsKey(n) || gnext < g.get(n)) {
					from.put(n, c);
					g.put(n, gnext);
					f.put(n, gnext + h(n, e));
					if (!open.contains(n))
						open.add(n);
				}
			}

		}

		return aRes;
	}

	/*

// https://en.wikipedia.org/wiki/A*_search_algorithm

function reconstruct_path(cameFrom, current)
    total_path := {current}
    while current in cameFrom.Keys:
        current := cameFrom[current]
        total_path.prepend(current)
    return total_path

// A* finds a path from start to goal.
// h is the heuristic function. h(n) estimates the cost to reach goal from node n.
function A_Star(start, goal, h)
    // The set of discovered nodes that may need to be (re-)expanded.
    // Initially, only the start node is known.
    // This is usually implemented as a min-heap or priority queue rather than a hash-set.
    openSet := {start}

    // For node n, cameFrom[n] is the node immediately preceding it on the cheapest path from the start
    // to n currently known.
    cameFrom := an empty map

    // For node n, gScore[n] is the currently known cost of the cheapest path from start to n.
    gScore := map with default value of Infinity
    gScore[start] := 0

    // For node n, fScore[n] := gScore[n] + h(n). fScore[n] represents our current best guess as to
    // how cheap a path could be from start to finish if it goes through n.
    fScore := map with default value of Infinity
    fScore[start] := h(start)

    while openSet is not empty
        // This operation can occur in O(Log(N)) time if openSet is a min-heap or a priority queue
        current := the node in openSet having the lowest fScore[] value
        if current = goal
            return reconstruct_path(cameFrom, current)

        openSet.Remove(current)
        for each neighbor of current
            // d(current,neighbor) is the weight of the edge from current to neighbor
            // tentative_gScore is the distance from start to the neighbor through current
            tentative_gScore := gScore[current] + d(current, neighbor)
            if tentative_gScore < gScore[neighbor]
                // This path to neighbor is better than any previous one. Record it!
                cameFrom[neighbor] := current
                gScore[neighbor] := tentative_gScore
                fScore[neighbor] := tentative_gScore + h(neighbor)
                if neighbor not in openSet
                    openSet.add(neighbor)

    // Open set is empty but goal was never reached
    return failure

	 */
	
	private int getTurns(final int[] dir1, final int[] dir2) {
		int turns = 2;
		if (dir1 == dir2)
			turns = 0;
		else if ((dir1 == Utils.U || dir1 == Utils.D) && (dir2 == Utils.R || dir2 == Utils.L)
				|| (dir2 == Utils.U || dir2 == Utils.D) && (dir1 == Utils.R || dir1 == Utils.L))
			turns = 1;
		return turns;
	}
	
	/**
	 * fast heuristic w/ distance w/o turns
	 *
	 * @param s
	 * @param e
	 * @return
	 */
	private int h(final State s, final Pos e) {
		final int dx = Math.abs(s.x - e.x), dy = Math.abs(s.y - e.y);
		return dx + dy;
	}
	
	@Override
	public void aTest() {
		assertEquals(7036, new Part1().compute("test1.txt").res);
		assertEquals(11048, new Part1().compute("test.txt").res);
	}
	
	@Override
	public void main() {
		// assertEquals(72400, new Part1().compute("input.txt").res); // 4s
	}

}
