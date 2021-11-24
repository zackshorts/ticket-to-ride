package models.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class Graph {

    HashMap<String, Node> graph;

    public Graph() {
        this.graph = new HashMap();
    }

    public boolean pathExists(String source, String destination, Player player) {

        //To store the children of nodes visited
        LinkedList<Node> queue = new LinkedList<Node>();

        //To store the visited nodes
        HashSet<String> visited = new HashSet<String>();

        //adding node of startId in the linkedlist
        queue.add(getNode(source));

        while (!queue.isEmpty()) {
            Node parent = queue.remove();
            //Check if current node is the destination node
            if (parent.getId().equals(destination))
                return true;
            //add source to visited set
            if (visited.contains(parent.getId()))
                continue;
            visited.add(parent.getId());
            //add children to the queue
            for (Node child : parent.getAdjacent())
                if (player.getRoutesOwned().contains(child)) {
                    queue.add(child);
                }
        }

        return false;
    }

    public Integer longestPath(Player player) {


        return 0;
    }

    private Node getNode(String id) {
        if (graph.containsKey(id))
            return graph.get(id);
        else {
            Node node = new Node(id);
            graph.put(id, node);
            return node;
        }
    }

    public void add(String source, String destination) {

        //Get nodes corresponding to source and destination vertices.
        Node s = getNode(source);
        Node d = getNode(destination);
        //add nodes to adjacent list
        s.addAdjacent(d);
    }

}
