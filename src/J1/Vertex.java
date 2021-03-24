/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package J1;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;
import javafx.util.Pair;

/**
 *
 * @author DELL
 */
public class Vertex implements Comparable<Vertex>{
    Vector<Vertex> toVList;
    Vector<Vertex> fromVList;
    String labelV;
    String contentV;
    Coordinate coordinate;
    String color;
    int sizeOfVertex;
    
    public Vertex() {
        this.labelV = "";
        this.coordinate = null;
        this.color = "";
        this.contentV = "";
        sizeOfVertex = 0;
    }
    
    public Vertex(String labelV, Coordinate coordinate, String color, String contentV, int sizeOfVertex) {
        this.labelV = labelV;
        this.coordinate = coordinate;
        this.color = color;
        this.contentV = contentV;
        this.sizeOfVertex = sizeOfVertex;
    }

    public int getSizeOfVertex() {
        return sizeOfVertex;
    }

    public void setSizeOfVertex(int sizeOfVertex) {
        this.sizeOfVertex = sizeOfVertex;
    }
    
    public String getLabelV() {
        return labelV;
    }

    public void setLabelV(String labelV) {
        this.labelV = labelV;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getContentV() {
        return contentV;
    }

    public void setContentV(String contentV) {
        this.contentV = contentV;
    }
    
    public void addVertexTo(Vertex toVertex){
        for (Vertex vertex : toVList) {
            if(vertex.equals(toVertex)) return;
        }
        toVList.add(toVertex);
    }
    
    public void addVertexFrom(Vertex fromVertex){
        for (Vertex vertex : fromVList) {
            if(vertex.equals(fromVertex)) return;
        }
        toVList.add(fromVertex);
    }

    @Override
    public int compareTo(Vertex o) {
        if(o.getLabelV().equals(this.labelV)) return 0;
        else return -1;
    }

    @Override
    public String toString() {
        return "Vertex{" + "labelV=" + labelV + ", contentV=" + contentV + ", color=" + color + '}';
    }
    
}
