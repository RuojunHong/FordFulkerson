import java.util.List;
import java.util.ArrayList;

/**
 * 
 * @author rebeccahong
 *
 */
public class Node {

	private String label;
	private List<Edge> edges;
	private boolean isVisited = false;
	private Pixel p;
	
	public Node(String label) {
		this.label = label;
		edges = new ArrayList<Edge>();
	}

	public String getLabel() {
		return this.label;
	}
	
	public void visit(){
		this.isVisited=true;
	}
	
	public void reset(){
		this.isVisited=false;
	}
	public boolean isVisited(){
		return this.isVisited;
	}
	/**
	 * remove the edge to destination node dest
	 * 
	 * @param dest
	 */
	public Edge removeEdge(Node dest) {
		Edge temp = new Edge();
		for (int i = 0; i < edges.size(); i++) {
			if (edges.get(i).getDest() == dest) {
				temp = edges.get(i);
				edges.remove(i);
			}
		}
		return temp;
	}

	public boolean contains(Node dest) {
		for (Edge e : edges) {
			if (e.getDest() == dest)
				return true;
		}
		return false;
	}

	public List<Edge> getEdges() {
		return this.edges;
	}

	public String toString() {
		return this.label;
	}
	
}
