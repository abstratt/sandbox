package graphs;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import graphs.Graph.CycleDetectedException;

public class GraphTests {
    @Test
    public void immediateSuccessors() {
        Graph<String> graph = graph(Arrays.asList("A:B", "A:C", "C:D", "F:D"));
        assertEqualSets(Arrays.asList("B", "C"), graph.getImmediateSuccessors("A"));
        assertEqualSets(Arrays.asList("D"), graph.getImmediateSuccessors("C"));
        assertEqualSets(Arrays.asList(), graph.getImmediateSuccessors("B"));
    }

    @Test
    public void immediatePredecessors() {
        Graph<String> graph = graph(Arrays.asList("A:B", "A:C", "C:D", "F:D"));
        assertEqualSets(Arrays.asList("C", "F"), graph.getImmediatePredecessors("D"));
        assertEqualSets(Arrays.asList("A"), graph.getImmediatePredecessors("C"));
        assertEqualSets(Arrays.asList(), graph.getImmediatePredecessors("A"));
    }

    @Test
    public void allPredecessors() {
        Graph<String> graph = graph(Arrays.asList("A:B", "A:C", "C:D", "F:D", "E:G"));
        assertEqualSets(Arrays.asList("A", "C", "F"), graph.getAllPredecessors("D"));
        assertEqualSets(Arrays.asList("A"), graph.getAllPredecessors("C"));
        assertEqualSets(Arrays.asList(), graph.getAllPredecessors("A"));
        assertEqualSets(Arrays.asList("E"), graph.getAllPredecessors("G"));
    }

    @Test
    public void allSuccessors() {
        Graph<String> graph = graph(Arrays.asList("A:B", "A:C", "C:D", "F:D", "E:G"));
        assertEqualSets(Arrays.asList("B", "C", "D"), graph.getAllSuccessors("A"));
        assertEqualSets(Arrays.asList("D"), graph.getAllSuccessors("C"));
        assertEqualSets(Arrays.asList(), graph.getAllSuccessors("B"));
        assertEqualSets(Arrays.asList("G"), graph.getAllSuccessors("E"));
    }

    @Test
    public void allSuccessorsWithCycles() {
        Graph<String> graph = graph(Arrays.asList("A:B", "B:C", "C:A"));
        try {
            graph.getAllSuccessors("A");
            fail("should have failed");
        } catch (CycleDetectedException e) {
            // success
        }
    }

    @Test
    public void sort() {
        Graph<String> graph = graph(Arrays.asList("A:B", "A:C", "C:D", "F:D", "E:G"));
        List<String> sortedNodes = graph.sortedNodes();
        assertEquals(7, sortedNodes.size());
        assertBefore(sortedNodes, "A", "B");
        assertBefore(sortedNodes, "A", "C");
        assertBefore(sortedNodes, "C", "D");
        assertBefore(sortedNodes, "A", "D");
        assertBefore(sortedNodes, "F", "D");
        assertBefore(sortedNodes, "E", "G");
    }

    private void assertBefore(List<String> graph, String node1, String node2) {
        assertTrue(graph.indexOf(node1) >= 0);
        assertTrue(graph.indexOf(node2) >= graph.indexOf(node1));
    }

    private <T> void assertEqualSets(Collection<T> expected, Set<T> actual) {
        assertEquals(new LinkedHashSet<>(expected), actual);
    }

    private Graph<String> graph(List<String> edgeValues) {
        List<Map.Entry<String, String>> edgesAsEntries = edgeValues.stream()//
                .map(s -> s.split(":"))//
                .map(s -> new SimpleEntry<>(s[0], s[1]))//
                .collect(toList());

        // add sources and targets as nodes
        Set<String> allNodes = new LinkedHashSet<>();
        edgesAsEntries.forEach(edge -> {
            allNodes.add(edge.getKey());
            allNodes.add(edge.getValue());
        });

        Map<String, Set<String>> allEdges = edgesAsEntries.stream()//
                .collect(groupingBy(//
                        Map.Entry::getKey, //
                        mapping(Map.Entry::getValue, toSet())//
                ));

        return new Graph<String>(allNodes, source -> Optional.ofNullable(allEdges.get(source)).//
                orElse(emptySet()));
    }
}
