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
        if (toSort.size() < 2)
            return new ArrayList<N>(toSort);
        Map<N, int[]> predecessorCounts = new HashMap<N, int[]>(toSort.size());
        for (N vertex : toSort)
            predecessorCounts.put(vertex, new int[1]);
        for (N vertex : toSort) {
            Collection<N> successors = nodeWalker.next(vertex);
            for (N successor : successors) {
                int[] predecessorCount = predecessorCounts.get(successor);
                if (predecessorCount != null)
                    predecessorCount[0]++;
            }
        }
        List<N> sorted = new ArrayList<N>(toSort.size());
        for (int i = 0; i < toSort.size(); i++)
            for (Map.Entry<N, int[]> it : predecessorCounts.entrySet())
                if (it.getValue()[0] == 0) {
                    it.getValue()[0] = -1;
                    sorted.add(it.getKey());
                    if (sorted.size() == toSort.size())
                        return sorted;
                    for (N successor : nodeWalker.next(it.getKey())) {
                        int[] predecessorCount = predecessorCounts.get(successor);
                        if (predecessorCount != null)
                            predecessorCount[0]--;
                    }
                }
        throw new CycleDetectedException();
    }

}
