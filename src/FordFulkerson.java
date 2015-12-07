import java.io.FileNotFoundException;
import java.util.List;

public class FordFulkerson {

	private Graph g;
	private Graph residual;
	private double maxFlow = 0;

	/**
	 * 
	 * @param s
	 * @throws Exception
	 */
	public FordFulkerson(String s) throws Exception {
		this.g = new Graph(s);
		this.residual = new Graph(s);
	}

	/**
	 * 
	 * @param s
	 * @param t
	 */
	public void pushFlow(Node s, Node t) {

		List<Edge> path = this.residual.findPath(s, t);
		double bottleNeck = this.findBottleNeck(path);
		this.maxFlow += bottleNeck;
		for (Edge e : path) {
			if (e.isReverse) {
				e.getReverseEdge().setCost(e.getCost() + bottleNeck);
				e.setCost(e.getCost() - bottleNeck);
				if (e.getCost() == 0) {
					e.getSrc().getEdges().remove(e);
				}
			} else {
				e.setCost(e.getCost() - bottleNeck);
				if (e.getReverseEdge() == null) {
					Edge reverse = residual.addEdge(e.getDest().getLabel(), e
							.getSrc().getLabel(), bottleNeck);
					reverse.isReverse = true;
					reverse.setReverseEdge(e);
					e.setReverseEdge(reverse);
				} else {
					e.getReverseEdge().setCost(
							e.getReverseEdge().getCost() + bottleNeck);
				}
			}
			if (e.getCost() == 0) {
				e.getSrc().getEdges().remove(e);
			}
		}
	}

	// }
	/**
	 * 
	 * @param s
	 * @param path
	 * @return
	 */
	public double findBottleNeck(List<Edge> path) {
		double bottleNeck = Double.MAX_VALUE;
		for (Edge e : path) {
			if (e.getCost() < bottleNeck) {
				bottleNeck = e.getCost();
			}
		}
		return bottleNeck;
	}
	/**
	 * 
	 * @throws FileNotFoundException
	 */
	public void displayDotFile() throws FileNotFoundException {
		g.displayDotFile("originalGraph.dot");
		residual.displayDotFile("residualGraph.dot");
	}

	public void run(String src, String dest) {
		Node s = residual.getLookUp().get(src);
		Node t = residual.getLookUp().get(dest);
		while (this.residual.hasPath(s, t)) {
			this.pushFlow(s, t);
			//this.pushFlow(s, t);
		}
		System.out.println("Algrithm finished. max flow = "+this.maxFlow);
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		FordFulkerson f = new FordFulkerson("graph");

		f.run("s", "t");
		
		f.displayDotFile();
	}

}
