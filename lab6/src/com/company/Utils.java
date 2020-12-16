package com.company;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class Utils {

    public ArrayList<ArrayList<Integer>> graph ;

    public Utils(ArrayList<ArrayList<Integer>> initGraph){
        graph=initGraph;
    }

    public void startSearch() {
        ArrayList<Integer> path = new ArrayList<Integer>();

        IntStream.range(0,graph.size()).forEach(e -> path.add(-1));

        try {
            path.set(0,0);
            search(0, path, 1);
            throw new Exception("no solution");
        } catch (final Exception e) {
                throw new RuntimeException(e.getMessage());
        }
    }

    public void search(int currentVertex, ArrayList<Integer> path, int pathCount) throws Exception {
        System.out.println(path);

        //base case when we found a solution
        //we got to the first vertex and the pathcount is equal to the number of vertexes
        if(graph.get(currentVertex).get(0) == 1 && pathCount == graph.size()){
            System.out.println(path);

            throw new Exception("sol found");
        }
        //if we visited all vertices we just return
        if(pathCount == graph.size()){
            return;
        }

        //we start checking all possible vertices
        for(int i=0;i<graph.size();i++){

            //if there is an edge we add it to the path
            //and mark it as visited(temporarly remove the edge)
            if(graph.get(currentVertex).get(i)==1){

                path.set(pathCount++,i);
                graph.get(currentVertex).set(i,0);
                graph.get(i).set(currentVertex,0);

                //if we didn't check the path of this vertex we
                //call this function recursively on a new thread
                if(!checkVisited(i,path,pathCount)){

                    ExecutorService ex = Executors.newSingleThreadExecutor();
                    final int vertex = i;
                    final int count = pathCount;

                    final Runnable task = () -> {
                        try{
                            //System.out.println("ok");
                            search(vertex,path,count);

                        } catch (Exception e) {
                           throw new RuntimeException(e.getMessage());
                        }
                    };
                    //this will be a future and we need to get the result
                    ex.submit(task).get();
                }

                //replace the removed edge so the paths can be correctly used
                graph.get(currentVertex).set(i, 1);
                graph.get(i).set(currentVertex, 1);
                //delete the path after it was checked
                path.set(--pathCount,-1);

            }

        }
    }

    //if vertex in path return true
    public boolean checkVisited(int vertex, ArrayList<Integer> path, int pathCount){
        for(int i=0; i < pathCount-1;i++)
            if(path.get(i) == vertex)
                return true;
        return false;
    }

}
