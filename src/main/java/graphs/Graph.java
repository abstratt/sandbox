package graphs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


public class Graph<N> {

    public interface Walker<N> {
        Collection<N> next(N node);
    }

    public static class CycleDetectedException extends RuntimeException {
        private static final long serialVersionUID = 1L;
    } 

	private Set<N> nodes;
	private Walker<N> walker;
	private Map<N, Set<N>> backLinks;

	public Graph(Collection<N> nodes, Walker<N> walker) {
		this.nodes = new LinkedHashSet<>(nodes);
		this.walker = walker;
		this.backLinks = buildBackLinks();
	}

	private Map<N, Set<N>> buildBackLinks() {
		Map<N, Set<N>> newBackLinks = new LinkedHashMap<>();
		for (N source : nodes) {
			Collection<N> nextNodes = doGetSuccessors(source);
			if (nextNodes != null) {
				for (N target : nextNodes) {
					newBackLinks.computeIfAbsent(target, n -> new LinkedHashSet<>()).add(source);
				}
			}
		}
		return newBackLinks;
	}

	public Set<N> getImmediatePredecessors(N target) {
		return Optional.ofNullable(backLinks.get(target)).orElseGet(Collections::emptySet);
	}

	public Set<N> getAllPredecessors(N node) {
		return navigate(node, new LinkedHashSet<>(), this::getImmediatePredecessors);
	}

	public Set<N> getImmediateSuccessors(N node) {
		return new LinkedHashSet<>(doGetSuccessors(node));
	}

	private Collection<N> doGetSuccessors(N node) {
		return walker.next(node);
	}

	public Set<N> getAllSuccessors(N node) {
		return navigate(node, new LinkedHashSet<>(), this::getImmediateSuccessors);
	}

	private Set<N> navigate(N node, Set<N> collected, Walker<N> walker) {
        Collection<N> immediate = walker.next(node);
		for (N n : immediate) {
            if (collected.contains(n)) {
                throw new CycleDetectedException();
            }            
            collected.add(n);
			navigate(n, collected, walker);
		}
		return collected;
	}

	public List<N> sortedNodes() {
		return sort(nodes, walker);
	}

	public static <N> List<N> sort(Collection<N> toSort, Walker<N> nodeWalker) {
		class Counter {
			int count = 0;

			@Override
			public String toString() {
				return "" + count;
			}
		}

		if (toSort.size() < 2)
			return new ArrayList<N>(toSort);
		Map<N, Counter> predecessorCounts = new HashMap<N, Counter>(toSort.size());
		for (N vertex : toSort)
			predecessorCounts.put(vertex, new Counter());
		for (N vertex : toSort) {
			Collection<N> successors = nodeWalker.next(vertex);
			for (N successor : successors) {
				Counter predecessorCount = predecessorCounts.get(successor);
				if (predecessorCount != null)
					predecessorCount.count++;
			}
		}
		List<N> sorted = new ArrayList<N>(toSort.size());
		for (int i = 0; i < toSort.size(); i++)
			for (Map.Entry<N, Counter> it : predecessorCounts.entrySet())
				if (it.getValue().count == 0) {
					it.getValue().count = -1;
					sorted.add(it.getKey());
					if (sorted.size() == toSort.size())
						return sorted;
					for (N successor : nodeWalker.next(it.getKey())) {
						Counter predecessorCount = predecessorCounts.get(successor);
						if (predecessorCount != null)
							predecessorCount.count--;
					}
				}
		throw new CycleDetectedException();
	}

}
