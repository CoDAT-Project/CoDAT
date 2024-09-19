package eshmun.gui.kripke.utils.graphphysics;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eshmun.gui.utils.models.vanillakripke.State;
import eshmun.gui.utils.models.vanillakripke.Transition;

/**
 * Graph Physics, Automatically space out and position a graph in a nice way.
 * 
 * <p>This is done by considering the graph to be a physical systems, in which nodes are charged 
 * particles and edges are connecting cables or springs.</p>
 * 
 * <p>The simulation is carried out for a determined amount of iterations. It 
 * utilizes Coloumb's law and Hook's law. </p>
 * 
 * @author George E. Zakhour
 * @author Mohammad Ali Beydoun
 * @author Kinan Dak Al Bab
 * @since 1.0
 */
public class GraphPhysics {	
	/**
	 * Dictates the magnitude of attraction between Nodes that 
	 * are connected to by edge.
	 */
	private static final double ATTRACTION_CONSTANT = 0.045;
	
	/**
	 * Dictates the magnitude of repulsion between Nodes.
	 */
	public static final double REPULSION_CONSTANT = 500000.0;
	
	/**
	 * Time delta of the simulation, the simulated time passed 
	 * between one iteration and the other.
	 */
	private double time_scale;

	/**
	 * Create a GraphPhysics object that automatically space out a graph.
	 */
	public GraphPhysics() {
		//Empty, Default Constructor
		time_scale = 1.0;
	}
	
	/**
	 * Computes the attractive force between 2 nodes given their positions.
	 *
	 * @param a The position of node A
	 * @param b The position of node B
	 * @return the attraction force between A, B as a vector.
	 */
	private Vector attractiveForce(Vector a, Vector b) {
		return b.sub(a).normalize().mul(ATTRACTION_CONSTANT * a.distance(b));
	}

	/**
	 * Computes the repulsive force between 2 nodes given their positions.
	 *
	 * @param a The position of node A
	 * @param b The position of node B
	 * @return the repulsion force between A, B as a vector.
	 */
	private static Vector repulsiveForce(Vector a, Vector b) {
		return b.sub(a).normalize().mul(-REPULSION_CONSTANT / Math.pow(a.distance(b), 2));
	}

	/**
	 * Computes the number of iterations that would be appropriate for the simulation to reach an adequate state.
 	 *
 	 * <p>
 	 * This is done through the given equation: f(x, y) = 1000 + 500log(1 + 1000x)log(1 + 1000y) <br>
 	 * Where x, y are the number of nodes, edges respectively.
 	 * </p>
 	 *
 	 * @param nodeCount The number of nodes in the graph
 	 * @param edgeCount The number of edges in the graph
 	 * @return the number of iterations required.
 	 */
	private int calculateIterations(int nodeCount, int edgeCount) {
		return (int)(1000 + 500*Math.log(1 + 1000*nodeCount)*Math.log(1 + 1000*edgeCount)) / 2;
	}

	/**
	 * Simulates the graph as a physical system of repulsive and attraction force (by the use of Coulomb's and Hooke's laws).
	 * @param nodes The nodes in the graph mapped by their name (unique), the nodes will be modified in place with the new position.
	 * @param edges List of edges in the graph
	 * @param iterations the maximum number of iterations to run.
	 * @return the same mapping with positions updated.
	 */
	private HashMap<String, Node> transform(HashMap<String, Node> nodes, List<Edge> edges, int iterations) {
		//Construct an edge table.
		HashMap<String, ArrayList<Node>> edgeTable = new HashMap<>();
		for (String name : nodes.keySet()) {
			edgeTable.put(name, new ArrayList<Node>());
		}

		//Fill the table with adjacency data.
		for (Edge e : edges) {
			edgeTable.get(e.from.name).add(e.to);
			edgeTable.get(e.to.name).add(e.from);
		}

		//Force table for each node.
		Map<String, Vector> forces = new HashMap<String, Vector>();

		//Perform a fixed number of physics iterations.
		for (int i = 0; i < iterations; i++) {

			//Compute the current force values for all nodes.
			for (Node node : nodes.values()) {
				Vector force = new Vector(0.0, 0.0);

				//Accumulate the attractive forces.
				for (Node n : edgeTable.get(node.name)) {
					force = force.add(attractiveForce(node.position, n.position));
				}

				//Accumulate the repulsive forces.
				for (Node n : nodes.values()) {
					if (!n.name.equals(node.name)) {
						force = force.add(repulsiveForce(node.position, n.position));
					}
				}

				//Associate this node with the force vector.
				forces.put(node.name, force);
			}

			//Update positions based on forces.
			for (Node node : nodes.values()) {
				node.position = node.position.add(forces.get(node.name).mul(time_scale));
			}
		}

		return nodes;
	}
	
	/**
	 * Auto spaces the given state-transition graph, results are saved in place.
	 * 
	 * <p>Only change the position of the state.</p>
	 * 
	 * @param states Mapping of state names to states.
	 * @param transitions List of Transitions.
	 */
	public void autoSpace(HashMap<String, State> states, List<Transition> transitions) {
		//Pre-process, extract simple Node and Edge from State and Transition.
		HashMap<String, Node> nodes = new HashMap<String, Node>();
		ArrayList<Edge> edges = new ArrayList<Edge>();
		
		for(String name : states.keySet()) {
			State s = states.get(name);
			
			Node n = new Node(name, s.getLocation().x, s.getLocation().y);
			nodes.put(name, n);
		}
		
		for(Transition t : transitions) {
			if(t.isDoubled()) continue; //consider doubled transition only once.
			if(t.getTo().getName().equals(t.getFrom().getName())) continue; //disregard self-transition
			
			String from = t.getFrom().getName();
			String to = t.getTo().getName();
			
			Node nFrom = nodes.get(from);
			Node nTo = nodes.get(to);
			
			boolean fromNull = (nFrom != null);
			boolean toNull = (nTo != null);
			
			assert fromNull; //sanity check
			assert toNull; //sanity check
			
			Edge e = new Edge(nFrom, nTo);
			edges.add(e);
		}
		
		//auto space in multiple rounds.
		int iterations = calculateIterations(nodes.size(), edges.size());
		
		time_scale = 1.5;
		nodes = transform(nodes, edges, (iterations*2) / 3);
		
		time_scale = 0.5;
		nodes = transform(nodes, edges, iterations / 2);
				
		//Post-process, migrate the changes from simple Node to State.
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		for(String name : nodes.keySet()) {
			Node n = nodes.get(name);
			int x = (int) n.position.getX();
			int y = (int) n.position.getY();
			
			Point p = new Point(x, y);
			
			minX = Math.min(x, minX);
			minY = Math.min(y, minY);
			
			State s = states.get(name);
			
			s.setLocation(p);
		}
		
		//Shift graph to ``almost'' top-left corner (to the point (10, 10) to be exact).
		for(State s : states.values()) {
			Point p = s.getLocation();
			p.translate(10 - minX, 10 - minY);
			
			s.setLocation(p);
		}
	}
}




