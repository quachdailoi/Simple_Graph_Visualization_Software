/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package J1;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author DELL
 */
public class Graph {

    Vector<Vertex> vertices;
    Vector<Edge> edges;
    Vector<Vector<Vertex>> verticesLayer;
    String nameGraph;
    HashMap<String, String> adjMatrix;
    HashMap<String, String> adjUndirectedMatrix;
    final String DESCRIPTION_FORM = "Description: Name_Of_Graph{\n\t\\\\vertives\n\t\tVertex_Key[label=\"vertex_label\",color=\"color_of_vertex\"]\n\t\t...\n\t\\\\edges\n\t\tFrom_Vertex->To_Vertex[label=\"cost_of_edge\"]\n\t\t...";
    final String VERTEX_DESCRIPTION = "Vertex_Key[label=\"vertex_label\",color=\"color_of_vertex\"]";
    final String EDGE_DESCRIPTION = "From_Vertex->To_Vertex[label=\"cost_of_edge\"]";

    private final int X_CENTER_GRAPH = 200;
    private final int Y_CENTER_GRAPH = 200;
    private final int X_INIT_POINT = 80;
    private final int Y_INIT_POINT = 80;

    public Graph() {
        nameGraph = "";
    }

    public Graph(String description) {
        if ((nameGraph = analyzeNameGraph(description)) != null) {
            if ((vertices = analyzeVertex(description)) != null) {
                if ((edges = analyzeEdge(description)) != null) {
                    adjMatrix = createDirectedMatrix();
                    adjUndirectedMatrix = createUndirectedMatrix();
                    setCoordinatesVertices();
                }
            }
        }
    }

    private String analyzeNameGraph(String description) {
        description = description.trim();
        String name = "";
        for (int i = 0; i < description.length(); i++) {
            if (description.charAt(i) == '{') {
                break;
            }
            name += description.charAt(i);
        }
        if (name.length() == description.length() || !checkWordInLine("}", description)) {
            JOptionPane.showMessageDialog(null, DESCRIPTION_FORM);
            return null;
        }
        return name;
    }

    private boolean checkWordInLine(String word, String line) {
        word = word.toLowerCase();
        if (line.indexOf(word) == line.lastIndexOf(word) && line.contains(word)) {
            return true;
        }
        return false;
    }

    private boolean checkRightQuotes(String line) {
        int count = 0;
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '"') {
                count++;
            }
        }
        if (count == 4) {
            return true;
        }
        return false;
    }

    private boolean checkLineDesVertexOrEdge(String line) {
        if (checkWordInLine("[", line) && checkWordInLine("]", line)) {
            if (checkWordInLine("color", line)) {
                if (!checkWordInLine("label", line)) {
                    return false;
                } else {
                    if (checkRightQuotes(line)) {
                        return true;
                    }
                    return false;
                }
            } else {
                if (checkWordInLine("label", line)) {
                    if (checkWordInLine("->", line)) {
                        return true;
                    }
                    return false;
                }
            }
        }
        return false;
    }

    private boolean checkLineMayDesVertexAndEdge(String line) {
        if (line.isEmpty() || checkWordInLine("{", line) || checkWordInLine("}", line) || checkWordInLine("\\\\", line)) {
            return false;
        }
        return true;
    }

    private Vector<Vertex> analyzeVertex(String description) {
        Vector<Vertex> verticesR = null;
        verticesR = new Vector<>();
        int lineError = 0;
        description = description.trim();
        StringTokenizer st = new StringTokenizer(description, "\n");
        while (st.hasMoreTokens()) {
            lineError++;
            String tmp = st.nextToken().trim();
            String contentV = "";
            String color = "";
            Vertex vertex = new Vertex();
            if (checkLineMayDesVertexAndEdge(tmp)) {
                if (checkLineDesVertexOrEdge(tmp)) {
                    if (tmp.contains("label") && tmp.contains("color")) {
                        String label = "";
                        int i;
                        for (i = 0; i < 10; i++) {
                            if (tmp.charAt(i) != '[') {
                                label += tmp.charAt(i);
                            } else {
                                break;
                            }
                        }
                        System.out.println(label.isEmpty());
                        if (label.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Line error: " + lineError + "\nForm: " + VERTEX_DESCRIPTION + "\nVertex_Key must not be empty!");
                            return null;
                        }
                        vertex.setLabelV(label.trim());
                        for (i++; i < tmp.length(); i++) {
                            if (tmp.charAt(i) == '\"') {
                                for (i = i + 1; i < tmp.length(); i++) {
                                    if (tmp.charAt(i) != '\"') {
                                        contentV += tmp.charAt(i);
                                    } else {
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                        for (i++; i < tmp.length(); i++) {
                            if (tmp.charAt(i) == '\"') {
                                for (i = i + 1; i < tmp.length(); i++) {
                                    if (tmp.charAt(i) != '\"') {
                                        color += tmp.charAt(i);
                                    } else {
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                        vertex.setContentV(contentV);
                        vertex.setColor(color);
                        verticesR.add(vertex);
                    } else if (!(tmp.contains("->") && tmp.contains("label"))) {
                        JOptionPane.showMessageDialog(null, "Line error: " + lineError + "\nContent: " + tmp + "\n" + VERTEX_DESCRIPTION);
                        return null;
                    }
                }
            }
        }
        return verticesR;
    }

    private Vector<Edge> analyzeEdge(String description) {
        description = description.trim();
        Vector<Edge> edgesR = new Vector<>();
        StringTokenizer st = new StringTokenizer(description, "\n");
        int lineError = 0;
        System.out.println(vertices);
        while (st.hasMoreTokens()) {
            lineError++;
            String tmp = st.nextToken().trim();
            String labelCost = "";
            Vertex fromV = null;
            Vertex toV = null;
            Edge edge = new Edge();
            if (checkLineMayDesVertexAndEdge(tmp)) {
                if (checkLineDesVertexOrEdge(tmp)) {
                    if (tmp.contains("->") && tmp.contains("label")) {
                        int from = tmp.indexOf("->") - 1;
                        int to = tmp.lastIndexOf("->") + 2;
                        fromV = getVertexByLabel(tmp.charAt(from) + "");
                        toV = getVertexByLabel(tmp.charAt(to) + "");
                        System.out.println(from);
                        System.out.println(to);
                        if (fromV == null) {
                            JOptionPane.showMessageDialog(null, "Don't have vertex " + tmp.charAt(from) + " in Vertex list");
                            return null;
                        }
                        if (toV == null) {
                            JOptionPane.showMessageDialog(null, "Don't have vertex " + tmp.charAt(to) + "in Vetex list");
                            return null;
                        }
                        edge.setFromV(fromV);
                        edge.setToV(toV);
                        int i = 0;
                        for (i = 0; i < tmp.length(); i++) {
                            if (tmp.charAt(i) == '\"') {
                                for (i = i + 1; i < tmp.length(); i++) {
                                    if (tmp.charAt(i) != '\"') {
                                        labelCost += tmp.charAt(i);
                                    } else {
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                        edge.setLabelCost(labelCost);
                        edgesR.add(edge);
                    } else if (!(tmp.contains("label") && tmp.contains("color"))){
                        JOptionPane.showMessageDialog(null, "Line error: " + lineError + "\nContent: " + tmp + "\n" + EDGE_DESCRIPTION);
                        return null;
                    }
                }
            }
        }
        return edgesR;
    }

    private HashMap<String, String> createDirectedMatrix() {
        HashMap<String, String> adjMatrixR = new HashMap<>();
        for (Vertex vertice : vertices) {
            adjMatrixR.putIfAbsent(vertice.getLabelV(), "");
        }
        for (Map.Entry<String, String> entry : adjMatrixR.entrySet()) {
            for (Edge edge : edges) {
                if (entry.getKey().equalsIgnoreCase(edge.getFromV().getLabelV())) {
                    adjMatrixR.put(entry.getKey(), edge.getToV().getLabelV());
                }
            }
        }
        return adjMatrixR;
    }

    private HashMap<String, String> createUndirectedMatrix() {
        HashMap<String, String> adjMatrixR = new HashMap<>();
        for (Vertex vertice : vertices) {
            adjMatrixR.putIfAbsent(vertice.getLabelV(), "");
        }
        for (Map.Entry<String, String> entry : adjMatrixR.entrySet()) {
            for (Edge edge : edges) {
                if (entry.getKey().equalsIgnoreCase(edge.getFromV().getLabelV())) {
                    adjMatrixR.put(entry.getKey(), entry.getValue() + edge.getToV());
                } else if (entry.getKey().equalsIgnoreCase(edge.getToV().getLabelV())) {
                    adjMatrixR.put(entry.getKey(), entry.getValue() + edge.getFromV());
                }
            }
        }
        return adjMatrixR;
    }

    private void setCoordinatesVertices() {
        int totalVertices = vertices.size();
        double angleBetweenVetices = 2 * Math.PI / totalVertices;
        System.out.println("angle = " + angleBetweenVetices);
        int count = 0;
        for (Vertex vertice : vertices) {
            double x1 = X_INIT_POINT - X_CENTER_GRAPH;
            double y1 = Y_INIT_POINT - Y_CENTER_GRAPH;
            double x2 = x1 * Math.cos(angleBetweenVetices * count) - y1 * Math.sin(angleBetweenVetices * count);
            double y2 = x1 * Math.sin(angleBetweenVetices * count) + y1 * Math.cos(angleBetweenVetices * count);
            vertice.setCoordinate(new Coordinate((int) (x2 + X_CENTER_GRAPH), (int) (y2 + Y_CENTER_GRAPH)));
            count++;
        }
    }

    public Coordinate getCoordinatesByVertexLabel(String labelVertex) {
        for (Vertex vertice : vertices) {
            if (vertice.getLabelV().equalsIgnoreCase(labelVertex)) {
                return new Coordinate(vertice.getCoordinate().getX(), vertice.getCoordinate().getY());
            }
        }
        return null;
    }

    public Vertex getVertexByLabel(String label) {
        for (Vertex vertice : vertices) {
            if (vertice.getLabelV().equalsIgnoreCase(label)) {
                return vertice;
            }
        }
        return null;
    }

    public Vector<Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(Vector<Vertex> vertexes) {
        this.vertices = vertexes;
    }

    public Vector<Edge> getEdges() {
        return edges;
    }

    public void setEdges(Vector<Edge> edges) {
        this.edges = edges;
    }

    public String getNameGraph() {
        return nameGraph;
    }

    public void setNameGraph(String nameGraph) {
        this.nameGraph = nameGraph;
    }

    public HashMap<String, String> getAdjMatrix() {
        return adjMatrix;
    }

    public void setAdjMatrix(HashMap<String, String> adjMatrix) {
        this.adjMatrix = adjMatrix;
    }

    public HashMap<String, String> getAdjUndirectedMatrix() {
        return adjUndirectedMatrix;
    }

    public void setAdjUndirectedMatrix(HashMap<String, String> adjUndirectedMatrix) {
        this.adjUndirectedMatrix = adjUndirectedMatrix;
    }

}
