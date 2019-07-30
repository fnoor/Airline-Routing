package opt_project;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class Main_BFS {

	public static void main(String[] args) {
        try {
            ReadFile readFile = new ReadFile("src/opt_project/smaller_matrix_c.txt"); // or big_matrix.txt 
            ArrayList<String> matrix = readFile.openFile();
            Graph<Integer> graph = new Graph<>();
            HashMap<String, Integer> flights = new HashMap<>();
            //System.out.println(matrix.size());
            for (int i = 0; i < matrix.size(); i++) {
                String[] adjacencyList = matrix.get(i).split("\\s+");
                graph.addVertex(i);
                for (int j = 0; j < adjacencyList.length; j++) {
                    int isEdge = Integer.parseInt(adjacencyList[j]);
                    if (isEdge > 0) {
                        graph.addEdge(i, j);
                        flights.put(""+i+j,isEdge); 
                    }
                }
            }
            System.out.println("Welcome to the Flight Pairing Optimizer. This program will find a set of pairings that will produce the least cost."); 
            System.out.println("Heauristic: Select pairing with least total cost, find the next pairing with least and compare whether flights have");
            System.out.println("used in the previously selected set of pairings, only select pairing if all flights within this pairing are unique ");
            System.out.println("from the selected pairings to avoid overlapping flights. Continue until comaprisons are complete, and a set of ");
            System.out.println("pairings of cheapest cost are chosen." + "\n"); 
            System.out.println("The following are the costs of each flight. The first two-digit numbers represent start and end destinations:"); 
            System.out.println(flights + "\n");
            System.out.println("This is a map of all the connections each city has:"); 
            System.out.println(graph.toString());
			List<List<Integer>> paths = graph.getPaths(0, (matrix.size()-1)); //THIS NEEDS TO BE START TO LENGTH OF YOUR MATRIX 
            HashMap<Integer, List<Integer>> pathList = new HashMap<>(); //changes 
            int count = 0; 
            HashMap<Integer, Integer> pathCosts = new HashMap<>(); //changes 
            HashMap<Integer, Integer> costs = new HashMap<>(); //fixed 
            HashMap<Integer, List<Integer>> optimalPaths = new HashMap<>(); 
            System.out.println("Using BFS algorithm, all possible pairings stated in the adjaceny matrix are found as follows:");
            for (List<Integer> path : paths) {
            	String flight; 
            	int sum = 0; 
            	for(int i = 0; i < path.size(); i++){
            		int end = i + 1; 
            		if(end!=path.size()){
            			flight = ""+path.get(i)+path.get(i+1); 
            			sum += flights.get(flight); 
            		}
            	}
                for (int node : path) {
                    System.out.print(node + " ");
                }
                System.out.print("\n");
                pathCosts.put(count,sum);
                pathList.put(count, path); 
                count++; 
            }
            costs = (HashMap)pathCosts.clone(); 
            System.out.println("\nPairings Indexed:\n"+pathList); 
            System.out.println("\nTotal Costs Coressponding to Pairings:\n"+costs+"\n\n"); 
            System.out.println("STEPS OF PROGRAM. PLEASE READ OR SCROLL DOWN TO BOTTOM TO GET OPTIMAL SET OF PAIRINGS:");
            System.out.println("The program first selects the cheapest pairing.");
            insertPath(optimalPaths, getMin(pathCosts), pathList); 
            removeCostAndPath(pathCosts, pathList, getMin(pathCosts)); 
            System.out.println("The first optimal pairing is:"); 
            System.out.println(optimalPaths);
            getAllOptimalPaths(optimalPaths,pathCosts,pathList); 
            System.out.println("\n\nConclusion: The set of optimal pairings are as follows:"); 
            System.out.println(optimalPaths);
            System.out.println("Costs for each optimal pairings:"); 
            int totalCost = 0; 
            for ( int key : optimalPaths.keySet() ) {
                System.out.print(key + "= $"+costs.get(key)+" "); 
                totalCost += costs.get(key); 
            }
            System.out.println("\n"+"Total Cost of Pairing Set: $" + totalCost); 
            //System.out.println(pathCosts);
            //System.out.println(costs);
            //System.out.println(pathList);
            //System.out.println(paths);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
	
	public static int getMin(HashMap<Integer, Integer> costs){
		 Entry<Integer, Integer> min = null;
         for (Entry<Integer, Integer> entry : costs.entrySet()) {
             if (min == null || min.getValue() > entry.getValue()) {
                 min = entry;
             }
         }
         return min.getKey(); 
	}
	public static void insertPath(HashMap<Integer, List<Integer>> map, int index, HashMap<Integer, List<Integer>> paths){
		map.put(index, paths.get(index)); 
		
	}
	
	public static void getAllOptimalPaths(HashMap<Integer, List<Integer>> optiMap, HashMap<Integer, Integer> costs, HashMap<Integer, List<Integer>> paths){
	//iterate through 
	
		for(int j = 0; j < paths.size(); j++){
			int stop = 0; 
			int index = getMin(costs); 
			System.out.println("Number of pairings not used in optimal set: " + paths.size());
			List<Integer> path = paths.get(index); 
			System.out.println("Next least costly pairing: " + path); 
			for(int i = 1; i< path.size()-1; i++){
				int node = path.get(i);
				System.out.println("Check node (city) " + node+ " in pairing: "+path); 
				for (List<Integer> usedNodes: optiMap.values()) {
					System.out.println("Comparing it to optimal pairings list: " + usedNodes); 
				    Boolean isUsed = usedNodes.contains(node);
				    System.out.println("This node is already used in optimal pairings list:  " + isUsed);
				    if(isUsed){
				    	stop = 1 ; 
				    	break; 
				    }
				}
			}
			if(stop>0){
				System.out.println("Removing this pairing from unchecked pairings list, not an optimal pairing."); 
				removeCostAndPath(costs, paths, index); 
			}else{
				System.out.println("Inserting this pairing into optimal pairings list. Removing it from unchecked pairings list."); 
				insertPath(optiMap, index, paths); 
				System.out.println("Updated optimal pairings list: " + optiMap + "\n"); 
				removeCostAndPath(costs, paths, index); 
			}
		}
		
	}
	
	public static void removeCostAndPath(HashMap<Integer, Integer> costs, HashMap<Integer, List<Integer>> paths, int index){
		costs.remove(index); 
		paths.remove(index); 
	}
	
}
