/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package J1;

/**
 *
 * @author DELL
 */
public class Edge {
    Vertex fromV; // edge from Vertex
    Vertex toV; // edge to Vertex
    String labelCost; // cost of vertex

    public Edge(){
        labelCost = "";
        fromV = toV = null;
    }
    
    public Edge(Vertex fromV, Vertex toV, String labelCost) {
        this.fromV = fromV;
        this.toV = toV;
        this.labelCost = labelCost;
    }

    public Vertex getFromV() {
        return fromV;
    }

    public void setFromV(Vertex fromV) {
        this.fromV = fromV;
    }

    public Vertex getToV() {
        return toV;
    }

    public void setToV(Vertex toV) {
        this.toV = toV;
    }
    

    public String getLabelCost() {
        return labelCost;
    }

    public void setLabelCost(String labelCost) {
        this.labelCost = labelCost;
    }

    @Override
    public String toString() {
        return "Edge{" + "fromV=" + fromV.toString() + ", toV=" + toV.toString() + ", labelCost=" + labelCost + '}';
    }
    
}
