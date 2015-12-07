import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Directed Graph This graph implements:
 * 
 * 
 * @author rebeccahong
 *
 */
public class Graph {

	private static final String NEWLINE = System.getProperty("line.separator");
	private List<Node> nodes = new ArrayList<Node>();// nodes list
	private Map<String, Node> lookup = new HashMap<String, Node>();
	private boolean pathExists = false;
	private boolean isResidual = false;

	/**
	 * getter method
	 * 
	 * @return
	 */
	public List<Node> getNodes() {
		return this.nodes;
	}

	public boolean pathExists() {
		return this.pathExists;
	}

	/**
	 * add a node to graph
	 * 
	 * @param label
	 * @return
	 */
	public Node addNode(String label) {
		if (lookup.containsKey(label))
			return lookup.get(label);

		Node newNode = new Node(label);
		lookup.put(label, newNode);
		nodes.add(newNode);

		return newNode;
	}

	/**
	 * Add edge and error checking
	 * 
	 * @param srcLabel
	 * @param destLabel
	 * @param cost
	 * @return
	 */
	public Edge addEdge(String srcLabel, String destLabel, double cost) {
		Node srcNode = addNode(srcLabel);
		Node destNode = addNode(destLabel);
		Edge newEdge = new Edge(srcNode, destNode, cost);
		newEdge.isReverse = false;
		// Edge reverseEdge = new Edge(destNode, srcNode, cost);
		// reverseEdge.isReverse = true;
		// set pointers pointing to its reverse edge
		// newEdge.setReverseEdge(reverseEdge);
		// reverseEdge.setReverseEdge(newEdge);
		srcNode.getEdges().add(newEdge);
		// destNode.getEdges().add(reverseEdge);
		// destNode.setIndegree(destNode.getIndegree() + 1);
		return newEdge;
	}

	/**
	 * simply remove the node from list
	 * 
	 * @param n
	 */
	public void remove(Node n) {
		this.nodes.remove(n);
	}

	/**
	 * deletion of individual villages. After the deletion:
	 * 
	 * (1)any roads that went through the village's route to other villages are
	 * direct (2)Roads cost are summed up
	 * 
	 * @param label
	 * @return
	 */
	public Node removeNode(String label) {
		Node temp = lookup.get(label);

		if (temp.getEdges().isEmpty()) {
			// remove all the edges to temp
			for (Node n : nodes) {
				n.removeEdge(temp);
			}
		} else {
			// Create temporary list for backup
			List<Node> tempDests = new ArrayList<Node>();
			List<Double> tempCosts = new ArrayList<Double>();
			for (Edge e : temp.getEdges()) {
				tempDests.add(e.getDest());
				tempCosts.add(e.getCost());
			}
			for (Node n : nodes) {
				if (n.contains(temp)) {
					double tempcost = n.removeEdge(temp).getCost();
					for (int i = 0; i < tempDests.size(); i++) {
						// add the integrated road with updated(sum) cost
						addEdge(n.getLabel(), tempDests.get(i).getLabel(),
								tempCosts.get(i) + tempcost);
					}
				}
			}
		}
		nodes.remove(temp);
		// update the indegree
		System.out.println("Removed node " + temp.getLabel() + ".");
		return temp;

	}

	public String toString() {
		StringBuilder str = new StringBuilder();

		for (Node n : nodes) {
			str.append(n.getLabel() + ":");
			for (Edge e : n.getEdges()) {
				str.append(e.getDest().getLabel() + " ");

			}
			str.append(NEWLINE);
		}

		return str.toString();

	}

	public void writeToFile(String fileName) throws FileNotFoundException {
		PrintWriter out = new PrintWriter(fileName);

		for (Node n : nodes) {
			for (Edge e : n.getEdges()) {
				out.println(n.getLabel() + " " + e.getDest().getLabel() + " "
						+ e.getCost());
			}
		}

		out.close();
	}

	public void displayDotFile(String fileName) throws FileNotFoundException {
		PrintWriter out = new PrintWriter(fileName);
		out.println("digraph G {");
		out.println("rankdir=LR;");
		for (Node n : nodes) {
			for (Edge e : n.getEdges()) {
				out.print("\""+n.getLabel()+"\"->" + "\""+e.getDest().getLabel() + "\"[");
				if (e.isReverse)
					out.print("style=dashed,");
				out.println("label=" + e.getCost() + "];");
			}
		}
		out.println("}");
		out.close();

	}

	public Map<String, Node> getLookUp() {
		return this.lookup;
	}

	/**
	 * 
	 * @param s
	 * @param t
	 * @param path
	 */
	public void findPathRec(Node s, Node t, List<Node> path) {
		s.visit();
		if (s == t) {
			this.pathExists = true;
			return;
		}

		FOR: for (Edge e : s.getEdges()) {
			if (this.pathExists)
				break;
			if (e.getDest().isVisited()) {
				continue FOR;
			}
			path.add(e.getDest());
			findPathRec(e.getDest(), t, path);
			if (!this.pathExists) {
				path.remove(e.getDest());
			}
		}
	}

	/**
	 * 
	 * @param s
	 * @param t
	 * @return
	 */
	public boolean hasPath(Node s, Node t) {
		List<Node> pathNode = new ArrayList<Node>();
		findPathRec(s, t, pathNode);
		//System.out.println("path" + pathNode);
		this.pathExists = false;
		for (Node n : pathNode) {
			n.reset();
		}
		if(pathNode.isEmpty()){
			return false;
		}
		else{
			return true;
		}
	}

	/**
	 * 
	 * @param s
	 * @param t
	 * @return
	 */
	public List<Edge> findPath(Node s, Node t) {
		List<Node> pathNode = new ArrayList<Node>();
		findPathRec(s, t, pathNode);
		this.pathExists = false;
		for (Node n : pathNode) {
			n.reset();
		}
		List<Edge> pathEdge = new ArrayList<Edge>();
		if (!pathNode.isEmpty()) {
			for (Edge e : s.getEdges()) {
				if (e.getDest() == pathNode.get(0)) {
					pathEdge.add(e);
				}
			}
			for (int i = 0; i < (pathNode.size() - 1); i++) {
				for (Edge e : pathNode.get(i).getEdges()) {
					if (e.getDest() == pathNode.get(i + 1)) {
						pathEdge.add(e);
					}
				}
			}
		}
		return pathEdge;
	}

	public Graph(String fileName) throws Exception {
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				String[] str = sCurrentLine.split(" ");
				this.addEdge(str[0], str[1], Double.parseDouble(str[2]));
			}

		} catch (IOException e) {
			System.out.println("No such file.");
			return;
		}

	}

	/**
	 * test client code for directed graph abstract data structure
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Graph g = new Graph("graph");
		System.out.println(g);
		g.findPath(g.lookup.get("s"), g.lookup.get("t"));
		System.out.println(g.hasPath(g.lookup.get("s"), g.lookup.get("t")));

	}
}
