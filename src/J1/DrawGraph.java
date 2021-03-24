/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package J1;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.lang.reflect.Field;
import java.util.Vector;

/**
 *
 * @author DELL
 */
public class DrawGraph extends Canvas {

    Graph graph;

    public DrawGraph(Graph graph) {
        this.graph = graph;
    }

    DrawGraph() {
        graph = null;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    @Override
    public void paint(Graphics g) {
        g.create();
        initGraphics(g);
        if (graph != null) {
            analyzeDrawing((Graphics2D) g);
        }
    }
    
    // draw all elements in graph
    public void analyzeDrawing(Graphics2D g2) {
        setSizeForVertices(g2, graph.getVertices());
        drawLinksBetweenVertices(g2, graph.getEdges());
        fillBackgroundVertices(g2, graph.getVertices());
        drawVertices(g2, graph.getVertices());
        drawCostOfLink(g2, graph.getEdges(), graph);
    }
    // init graphics for canvas
    private void initGraphics(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setBackground(Color.WHITE);
        g2.clearRect(0, 0, 440, 470);
    }
    // draw arrow links among vertices
    // traversal Edges: get center of from-vertex, get angel of link(by coordintes), get cordinates of endpoint of link, use class LineArrow to draw.
    public void drawLinksBetweenVertices(Graphics2D g2, Vector<Edge> edges) {
        for (Edge edge : edges) {
            Vertex centerOfVertex = graph.getVertexByLabel(edge.getToV().getLabelV());
            g2.setStroke(new BasicStroke(2));
            Coordinate coorFromV = graph.getCoordinatesByVertexLabel(edge.getFromV().getLabelV());
            Coordinate coorToV = graph.getCoordinatesByVertexLabel(edge.getToV().getLabelV());
            double angle = Math.atan2(coorToV.getY() - coorFromV.getY(), coorToV.getX() - coorFromV.getX());
            int x_End_Point = (int) (coorToV.getX() - centerOfVertex.getSizeOfVertex() * Math.cos(angle));
            int y_End_point = (int) (coorToV.getY() - centerOfVertex.getSizeOfVertex() * Math.sin(angle));
            g2.drawLine(coorFromV.getX(), coorFromV.getY(), coorToV.getX(), coorToV.getY());
            LineArrow la = new LineArrow(coorFromV.getX(), coorFromV.getY(), x_End_Point, y_End_point, 2);
            la.draw(g2);
        }
    }
    // draw cost of link
    // traversal egdes: get coordinates of from-vertex and to-vertex => coordinates of cost to draw (set font...) => use drawString
    public void drawCostOfLink(Graphics2D g2, Vector<Edge> edges, Graph graphV) {
        for (Edge edge : edges) {
            g2.setStroke(new BasicStroke(2));
            Vertex fromV = graphV.getVertexByLabel(edge.getFromV().getLabelV());
            Vertex toV = graphV.getVertexByLabel(edge.getToV().getLabelV());
            int xOfCost = (fromV.getCoordinate().getX() + toV.getCoordinate().getX()) / 2;
            int yOfCost = (fromV.getCoordinate().getY() + toV.getCoordinate().getY()) / 2;
            String cost = edge.getLabelCost();
            g2.setFont(new Font("Times New Roman", Font.BOLD, 15));
            g2.drawString(cost, xOfCost, yOfCost);
        }
    }
    //fill background of vertex with white color
    //traversal vertices: setColor with white, fillOval with use size of vertex
    //note: fillOval => x, y of upper left corner, height and weight
    public void fillBackgroundVertices(Graphics2D g2, Vector<Vertex> vertices) {
        for (Vertex vertice : vertices) {
            int w = vertice.getSizeOfVertex();
            g2.setColor(Color.WHITE);
            g2.fillOval(vertice.getCoordinate().getX() - w, vertice.getCoordinate().getY() - w, 2 * w, 2 * w);
        }
    }
    //set size for all vertices
    // size is fm.stringWidth(vertice.getContentV()) / 3 * 2 + 10;
    public void setSizeForVertices(Graphics2D g2, Vector<Vertex> vertices) {
        for (Vertex vertice : vertices) {
            FontMetrics fm = g2.getFontMetrics();
            int w = fm.stringWidth(vertice.getContentV()) / 3 * 2 + 10;
            vertice.setSizeOfVertex(w);
        }
    }
    // draw Vertices
    // 
    public void drawVertices(Graphics2D g2, Vector<Vertex> vertices) {
        for (Vertex vertice : vertices) {
            Color colorBorder;
            try {
                Field field = Class.forName("java.awt.Color").getField(vertice.getColor());
                colorBorder = (Color) field.get(null);
            } catch (Exception e) {
                colorBorder = null; // Not defined
            }
            int w = vertice.getSizeOfVertex();
            //int h = fm.getAscent();
            float thickness = 2;
            Stroke oldStroke = g2.getStroke();
            g2.setStroke(new BasicStroke(thickness));
            g2.setColor(colorBorder); // set color
            g2.drawOval(vertice.getCoordinate().getX() - w, vertice.getCoordinate().getY() - w, 2 * w, 2 * w);

            g2.setStroke(oldStroke);
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Times New Roman", Font.BOLD, 13));
            g2.drawString(vertice.getContentV(), vertice.getCoordinate().getX() - w / 2, vertice.getCoordinate().getY() + w / 4);

        }
    }

}
