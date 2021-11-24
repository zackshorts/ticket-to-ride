package models.data;

import java.util.LinkedList;

public class Node {

    private String id;
    private LinkedList<Node> adjacent;

    public Node(String id){
        this.id = id;
        adjacent = new LinkedList<Node>();
    }

    //Getter method for start vertex
    public String getId(){
        return id;
    }

    //Getter method for end vertex
    public LinkedList<Node> getAdjacent(){
        return adjacent;
    }

    //add node to the adajcent list
    public void addAdjacent(Node vertex){
        adjacent.add(vertex);
    }

    //To print Node
    public String toString(){
        String msg = id + " : ";
        for(Node node: adjacent)
            msg = msg + node.id + " ";
        return msg;
    }
}