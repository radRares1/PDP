package com.company;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;


class Graph {

    private int noEdges;
    private int noNodes;
    private final Set<Integer> independentSet;
    private final Map<Integer, NodeInfo> nodesInfo;
    private Set<Integer> colors;
    private Map<Integer, Set<Integer>> graph;
    private ExecutorService exec;

    public Graph(int nrOfVertices) {
        this.noNodes = nrOfVertices;
        graph = new HashMap<>();
        nodesInfo = new HashMap<>();
        for (int i = 0; i < nrOfVertices; i++) {
            graph.put(i, new HashSet<>());
            nodesInfo.put(i, new NodeInfo(-1));
        }
        colors = new TreeSet<>();
        independentSet = new HashSet<>(graph.keySet());
        for (int i = 0; i < 100; i++) {
            colors.add(i);
        }
        exec = Executors.newFixedThreadPool(4);
    }

    public void addEdge(int src, int dest) {
        noEdges++;
        graph.get(src).add(dest);
        graph.get(dest).add(src);
    }

    public List<Integer> getNeighboursOfNode(int v) {
        return graph.get(v).stream().filter((node) -> {
            return getColor(node) == -1;
        }).collect(Collectors.toList());
    }

    public void colorGraph() {
        while (!independentSet.isEmpty()) {
            Set<Integer> set = getIndependentSet();
            for (Integer node : set) {
                exec.submit(() -> setColor(node));
            }
            independentSet.removeAll(set);
        }
        exec.shutdown();
    }

    public void printColors() {
        for (Integer v : nodesInfo.keySet())
            System.out.println("node:" + v + "has color:" + getColor(v));
    }

    public Integer getColor(int v) {
        return nodesInfo.get(v).color;
    }

    private boolean checkNode(int v) {
        for (Integer neigh : getNeighboursOfNode(v)) {
            if (v > neigh)
                return false;
        }
        return true;
    }

    public Set<Integer> getNeighboursColors(int v) {
        return getAllNeighbours(v).stream().filter((node) -> {
            return getColor(node) != -1;
        }).map(this::getColor).collect(Collectors.toSet());
    }

    public Set<Integer> getAllNeighbours(int node) {
        return graph.get(node);
    }

    public Integer getSmallestColor(int node) {
        Set<Integer> neighColors = getNeighboursColors(node);
        for (Integer c : colors) {
            if (!neighColors.contains(c))
                return c;
        }
        return 0;
    }

    public void setColor(int v) {
        nodesInfo.get(v).color = getSmallestColor(v);
    }

    public int[][] graphAsAdjencyMatrix() {
        int[][] graphMatrix = new int[graph.size()][graph.size()];

        for (Integer key : graph.keySet()) {
            for (int neighbor : graph.get(key)) {
                graphMatrix[key][neighbor] = 1;
            }

        }
        return graphMatrix;
    }

    public Set<Integer> getIndependentSet() {
        ArrayList<Future<Boolean>> list = new ArrayList<>();
        Set<Integer> res = new HashSet<>();
        List<Integer> listOfNodes = new ArrayList<>(independentSet);
        for (Integer node : listOfNodes) {
            Future<Boolean> f = exec.submit(() -> checkNode(node));
            list.add(f);
        }

        for (int i = 0; i < independentSet.size(); i++) {
            try {
                if (list.get(i).get() == true)
                    res.add(listOfNodes.get(i));
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return res;
    }
}