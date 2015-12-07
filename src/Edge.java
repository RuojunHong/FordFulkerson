/**
 * 
 * @author rebeccahong
 *
 */
public class Edge {
	private double cost;
	private Node dest;
	private Node src;
	private Edge reverseEdge;
	boolean isReverse;

	public Edge(Node src, Node dest, double cost) {
		this.src = src;
		this.dest = dest;
		this.cost = cost;
	}
	public Edge(){
		
	}
	
	public Node getSrc(){
		return this.src;
	}
	public Node getDest() {
		return this.dest;
	}

	public void setDest(Node newDest) {
		this.dest = newDest;
	}

	public double getCost() {
		return this.cost;
	}
	public void setCost(double newCost){
		this.cost=newCost;
	}
	public void setReverseEdge(Edge e){
		this.reverseEdge = e;
	}
	
	public Edge getReverseEdge(){
		return this.reverseEdge;
	}
	public String toString(){
		return this.src.toString()+" "+this.dest.toString();
	}
	
}
