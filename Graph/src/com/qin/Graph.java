package com.qin;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.algorithms.shortestpath.MinimumSpanningForest;
import edu.uci.ics.jung.graph.*;
import edu.uci.ics.jung.graph.util.EdgeType;
import org.apache.commons.collections15.Transformer;
import processing.core.*;

public class Graph extends PApplet {

    public static SparseMultigraph<Vertex, Edge> graph = null;
    public static int nodeCount = 0;

    public static String title = null;
    public static boolean directed = false;
    public static boolean weighted = false;

    public static Vertex selectedVertex = null; //鼠标按下是为选中的节点，鼠标放开时为null，用于绘图
    public static Vertex rightButtonSelectedVertex = null; //右键选择，不会因为鼠标放开而变成null，用于菜单操作
    public static Edge selectedEdge = null; //这两个同上
    public static Edge rightButtonSelectedEdge = null;

    private ArrayList<Vertex> vertexAnimationList = null; //某个算法执行完毕后按照这个顺序画动画
    private LinkedList<Edge> edgeAnimationList = null;
    private int startTime = 0;
    private boolean vertexAnimationStarted = false;
    private boolean edgeAnimationStarted = false;
    private int currentLocation = 0;

    private JPopupMenu vertexMenu = null;
    private JPopupMenu edgeMenu = null;
    private JPopupMenu blankMenu = null;

    public Graph(String title, boolean directed, boolean weighted) {
        graph = new SparseMultigraph<Vertex, Edge>();
        vertexAnimationList = new ArrayList<Vertex>();
        edgeAnimationList = new LinkedList<Edge>();

        Graph.title = title;
        Graph.directed = directed;
        Graph.weighted = weighted;

        JMenuItem BFSItem = new JMenuItem("BFS");
        JMenuItem DFSItem = new JMenuItem("DFS");
        JMenuItem addEdgeItem = new JMenuItem("Add edge from this vertex...");
        JMenuItem editVertexItem = new JMenuItem("Edit vertex...");
        JMenuItem deleteVertexItem = new JMenuItem("Delete vertex...");
        JMenuItem DijkstraItem = new JMenuItem("Dijkstra...");
        JMenuItem PrimItem = new JMenuItem("Prim");

        BFSItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                stopAnimation();
                resetVertex();
                BFS(rightButtonSelectedVertex);
                vertexAnimationStarted = true;
            }
        });

        DFSItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                stopAnimation();
                resetVertex();
                rightButtonSelectedVertex.setVisited(true);
                vertexAnimationList.add(rightButtonSelectedVertex);
                DFS(rightButtonSelectedVertex);
                vertexAnimationStarted = true;
            }
        });

        addEdgeItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                stopAnimation();
                String label = JOptionPane.showInputDialog(null,
                        "Please enter the label of the target vertex:",
                        "Add edge",
                        JOptionPane.DEFAULT_OPTION);
                if (label.equals("")) {
                    JOptionPane.showMessageDialog(null,
                            "Please enter a label!",
                            "Add edge",
                            JOptionPane.DEFAULT_OPTION);
                    return;
                }
                Vertex target = findVertex(label);
                if (target == null) {
                    String message = "No vertex named \"" + label + "\" is found!";
                    JOptionPane.showMessageDialog(null,
                            message,
                            "Add edge",
                            JOptionPane.DEFAULT_OPTION);
                    return;
                }
                addEdge(rightButtonSelectedVertex.getLabel(), label, 0);
            }
        });

        editVertexItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                stopAnimation();
                String newLabel = JOptionPane.showInputDialog(null,
                        "Please enter the new label:",
                        "Edit vertex",
                        JOptionPane.DEFAULT_OPTION);
                if (newLabel.equals("")) {
                    JOptionPane.showMessageDialog(null,
                            "Please enter a label!",
                            "Edit vertex",
                            JOptionPane.DEFAULT_OPTION);
                    return;
                }
                for (Vertex v : graph.getVertices()) {
                    if (newLabel.equals(v.getLabel()) && !rightButtonSelectedVertex.getLabel().equals(v.getLabel())) {
                        String message = "com.qin.Vertex named \"" + newLabel + "\" already exists!";
                        JOptionPane.showMessageDialog(null,
                                message,
                                "Edit vertex",
                                JOptionPane.DEFAULT_OPTION);
                        return;
                    }
                }
                rightButtonSelectedVertex.setLabel(newLabel);
            }
        });

        deleteVertexItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                stopAnimation();
                int confirm = JOptionPane.showConfirmDialog(null,
                        "Are you sure?",
                        "Delete vertex",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.DEFAULT_OPTION);
                if (confirm == 0) {
                    graph.removeVertex(rightButtonSelectedVertex);
                }
            }
        });

        DijkstraItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                stopAnimation();
                resetEdge();
                String label = JOptionPane.showInputDialog(null,
                        "Please enter the label of the target vertex:",
                        "Dijkstra",
                        JOptionPane.DEFAULT_OPTION);
                if (label.equals("")) {
                    JOptionPane.showMessageDialog(null,
                            "Please enter a label!",
                            "Dijkstra",
                            JOptionPane.DEFAULT_OPTION);
                    return;
                }
                Vertex target = findVertex(label);
                if (target == null) {
                    String message = "No vertex named \"" + label + "\" is found!";
                    JOptionPane.showMessageDialog(null,
                            message,
                            "Dijkstra",
                            JOptionPane.DEFAULT_OPTION);
                    return;
                }
                resetEdge();
                Dijkstra(rightButtonSelectedVertex, findVertex(label));
                edgeAnimationStarted = true;
            }
        });

        PrimItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                stopAnimation();
                resetEdge();
                Prim(rightButtonSelectedVertex);
            }
        });

        vertexMenu = new JPopupMenu();
        vertexMenu.add(BFSItem);
        vertexMenu.add(DFSItem);
        vertexMenu.addSeparator();
        vertexMenu.add(editVertexItem);
        vertexMenu.add(deleteVertexItem);
        vertexMenu.add(addEdgeItem);
        vertexMenu.addSeparator();
        vertexMenu.add(DijkstraItem);
        vertexMenu.add(PrimItem);

        JMenuItem editEdgeItem = new JMenuItem("Edit edge...");
        JMenuItem deleteEdgeItem = new JMenuItem("Delete edge...");

        editEdgeItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                stopAnimation();
                String newWeight = JOptionPane.showInputDialog(null,
                        "Please enter the new weight:",
                        "Edit edge",
                        JOptionPane.DEFAULT_OPTION);
                if (newWeight.equals("")) {
                    JOptionPane.showMessageDialog(null,
                            "Please enter a weight value!",
                            "Edit edge",
                            JOptionPane.DEFAULT_OPTION);
                    return;
                }
                if (!Constants.isNumber(newWeight)) {
                    JOptionPane.showMessageDialog(null,
                            "Please enter a valid value!",
                            "Edit edge",
                            JOptionPane.DEFAULT_OPTION);
                    return;
                }
                rightButtonSelectedEdge.setWeight(Float.parseFloat(newWeight));
            }
        });

        deleteEdgeItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                stopAnimation();
                int confirm = JOptionPane.showConfirmDialog(null, "Are you sure?", "Delete edge", JOptionPane.YES_NO_OPTION, JOptionPane.DEFAULT_OPTION);
                if (confirm == 0) {
                    graph.removeEdge(rightButtonSelectedEdge);
                }
            }
        });

        edgeMenu = new JPopupMenu();
        if (weighted) {
            edgeMenu.add(editEdgeItem);
        }
        edgeMenu.add(deleteEdgeItem);

        JMenuItem blankAddVertexItem = new JMenuItem("Add vertex...");
        JMenuItem blankResetItem = new JMenuItem("Reset graph...");
        JMenuItem blankEmptyItem = new JMenuItem("Empty graph...");

        blankAddVertexItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                stopAnimation();
                String label = JOptionPane.showInputDialog(null, "Please enter the label:", "Add vertex", JOptionPane.DEFAULT_OPTION);
                if (label.equals("")) {
                    JOptionPane.showMessageDialog(null, "Please enter a label!", "Add edge", JOptionPane.DEFAULT_OPTION);
                    return;
                }
                for (Vertex v : graph.getVertices()) {
                    if (label.equals(v.getLabel())) {
                        String message = "com.qin.Vertex named \"" + label + "\" already exists!";
                        JOptionPane.showMessageDialog(null, message, "Add vertex", JOptionPane.DEFAULT_OPTION);
                        return;
                    }
                }
                addVertex(label);
            }
        });
        blankResetItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                stopAnimation();
                int confirm = JOptionPane.showConfirmDialog(null, "Are you sure?", "Reset graph", JOptionPane.YES_NO_OPTION, JOptionPane.DEFAULT_OPTION);
                if (confirm == 0) {
                    resetAll();
                }
            }
        });
        blankEmptyItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                stopAnimation();
                int confirm = JOptionPane.showConfirmDialog(null, "Are you sure?", "Empty graph", JOptionPane.YES_NO_OPTION, JOptionPane.DEFAULT_OPTION);
                if (confirm == 0) {
                    empty();
                }
            }
        });

        blankMenu = new JPopupMenu();
        blankMenu.add(blankAddVertexItem);
        blankMenu.addSeparator();
        blankMenu.add(blankResetItem);
        blankMenu.add(blankEmptyItem);
    }

    public void stopAnimation()
    {
        vertexAnimationStarted = false;
        edgeAnimationStarted = false;
        vertexAnimationList.clear();
        edgeAnimationList.clear();
        currentLocation = 0;
    }

    public void resetVertex()
    {
        for (Vertex v : graph.getVertices()){
            v.setFixed(false);
            v.setVisited(false);
        }
    }

    public void resetEdge()
    {
        for (Edge e : graph.getEdges()) {
            e.setHighlightened(false);
        }
    }

    public void resetAll()
    {
        for (Vertex v : graph.getVertices()){
            v.setFixed(false);
            v.setVisited(false);
        }
        for (Edge e : graph.getEdges()) {
            e.setHighlightened(false);
        }
        rightButtonSelectedVertex = null;
        rightButtonSelectedEdge = null;
    }

    public void empty()
    {
       graph = new SparseMultigraph<Vertex, Edge>();
    }

    public void addEdge(String s1, String s2, float weight)
    {
        Vertex v1 = findVertex(s1);
        Vertex v2 = findVertex(s2);
        if (v1 == null) {
            v1 = new Vertex(s1, this);
            graph.addVertex(v1);
        }
        if (v2 == null) {
            v2 = new Vertex(s2, this);
            graph.addVertex(v2);
        }
        Edge e = new Edge(v1, v2, weight, this);
        if (directed) {
            graph.addEdge(e, v1, v2, EdgeType.DIRECTED);
        } else {
            graph.addEdge(e, v1, v2, EdgeType.UNDIRECTED);
        }
        nodeCount = graph.getVertexCount();
    }

    public void addVertex(String label)
    {
        graph.addVertex(new Vertex(label, this));
    }

    private Vertex findVertex(String label)
    {
        for (Vertex v : graph.getVertices()) {
            if (v.getLabel().equals(label)) {
                 return v;
            }
        }
        return null;
    }

    //processing

    public void setup()
    {
        size(600, 600);
        smooth();
    }

    public void draw()
    {
        background(255);
        for (Edge e : graph.getEdges()) {
            e.relax();
        }
        for (Vertex v : graph.getVertices()) {
            v.relax();
        }
        for (Vertex v : graph.getVertices()) {
            v.update();
        }
        for (Edge e : graph.getEdges()) {
            e.draw();
        }
        for (Vertex v : graph.getVertices()) {
            v.draw();
        }
        if (millis() - startTime > Constants.animationInterval && (vertexAnimationStarted || edgeAnimationStarted)) {
            if (vertexAnimationStarted) {
                if (currentLocation > vertexAnimationList.size() - 1) {
                    currentLocation = 0;
                    vertexAnimationStarted = false;
                    return;
                }
            }
            if (edgeAnimationStarted) {
                if (currentLocation > edgeAnimationList.size() - 1) {
                    currentLocation = 0;
                    edgeAnimationStarted = false;
                    return;
                }
            }
            if (vertexAnimationStarted) {
                vertexAnimationList.get(currentLocation++).setFixed(true);
            }
            if (edgeAnimationStarted) {
                edgeAnimationList.get(currentLocation++).setHighlightened(true);
            }
            startTime = millis();
        }
    }

    public void mousePressed()
    {
        boolean selectEdge = false;
        boolean blank = false;
        for (Vertex v : graph.getVertices()) {
            float d = sqrt(pow(mouseX - v.getX(), 2) + pow(mouseY - v.getY(), 2));
            if (d <= v.getR()) {
                selectedVertex = v;
                rightButtonSelectedVertex = v;
            }
        }
        if (selectedVertex != null) {
            if (mouseButton == LEFT) {
                selectedVertex.setFixed(!selectedVertex.isFixed());
            } else if (mouseButton == RIGHT) {
                vertexMenu.show(this, mouseX, mouseY);
            }
        } else {
            selectEdge = true;
        }
        if (selectEdge) {
            for (Edge e : graph.getEdges()) {
                float x1 = e.getFrom().getX();
                float y1 = e.getFrom().getY();
                float x2 = e.getTo().getX();
                float y2 = e.getTo().getY();
                double d = (abs((y2 - y1) * mouseX + (x1 - x2) * mouseY + ((x2 * y1) - (x1 * y2)))) / (sqrt(pow(y2 - y1, 2) + pow(x1 - x2, 2)));
                if ((d <= Constants.edgeWidth) && (((mouseX - x1) * (mouseX - x2) <= 0) && ((mouseY - y1) * (mouseY - y2) <= 0))) {
                    selectedEdge = e;
                    rightButtonSelectedEdge = e;
                }
            }
            if (selectedEdge != null) {
                if (mouseButton == RIGHT) {
                    edgeMenu.show(this, mouseX, mouseY);
                }
            } else {
                blank = true;
            }
        }
        if (blank && mouseButton == RIGHT) {
            blankMenu.show(this, mouseX, mouseY);
        }
    }

    public void mouseDragged()
    {
        if (selectedVertex != null) {
            if (mouseX > Constants.maxX) {
                mouseX = Constants.maxX;
            }
            if (mouseY > Constants.maxY) {
                mouseY = Constants.maxY;
            }
            if (mouseX < Constants.minX) {
                mouseX = Constants.minX;
            }
            if (mouseY < Constants.minY) {
                mouseY = Constants.minY;
            }
            selectedVertex.setX(mouseX);
            selectedVertex.setY(mouseY);
            selectedVertex.setFixed(true);
        }
    }

    public void mouseReleased()
    {
        selectedVertex = null;
        selectedEdge = null;
    }

    //algorithms

    private void DFS(Vertex v)
    {
        for (Edge e : graph.getIncidentEdges(v)) {
            if (directed) {
                if (!e.getTo().isVisited() && !e.getTo().getLabel().equals(v.getLabel())) {
                    e.getTo().setVisited(true);
                    vertexAnimationList.add(e.getTo());
                    DFS(e.getTo());
                }
            } else {
                Vertex temp;
                if (e.getTo().getLabel().equals(v.getLabel())) {
                    temp = e.getFrom();
                } else {
                    temp = e.getTo();
                }
                if (!temp.isVisited()) {
                    temp.setVisited(true);
                    vertexAnimationList.add(temp);
                    DFS(temp);
                }
            }
        }
    }

    private void BFS(Vertex v)
    {
        LinkedList<Vertex> q = new LinkedList<Vertex>();
        q.addLast(v);
        while (!q.isEmpty()) {
            Vertex v2 = q.pollFirst();
            v2.setVisited(true);
            vertexAnimationList.add(v2);
            for (Edge e : graph.getIncidentEdges(v2)) {
                if (directed) {
                    if (!e.getTo().isVisited() && !e.getTo().getLabel().equals(v2.getLabel())) {
                        e.getTo().setVisited(true);
                        q.addLast(e.getTo());
                    }
                } else {
                    Vertex temp;
                    if (e.getTo().getLabel().equals(v2.getLabel())) {
                        temp = e.getFrom();
                    } else {
                        temp = e.getTo();
                    }
                    if (!temp.isVisited()) {
                        temp.setVisited(true);
                        q.addLast(temp);
                    }
                }
            }
        }
    }

    private Transformer<Edge, Float> transformer = new Transformer<Edge, Float>() {
        public Float transform(Edge e) {
            return e.getWeight();
        }
    };

    private void Dijkstra(Vertex v1, Vertex v2)
    {
        DijkstraShortestPath<Vertex, Edge> path = new DijkstraShortestPath<Vertex, Edge>(graph, transformer);
        edgeAnimationList = (LinkedList<Edge>)path.getPath(v1, v2);
    }

    private void Prim(Vertex root)
    {
        HashMap<Edge, Double> weightMap = new HashMap<Edge, Double>();
        for (Edge e : graph.getEdges()) {
            weightMap.put(e, (double)e.getWeight());
        }
        DelegateForest<Vertex, Edge> forest = new DelegateForest<Vertex, Edge>();
        MinimumSpanningForest<Vertex, Edge> tree = new MinimumSpanningForest<Vertex, Edge>(graph, forest, root, weightMap);
        for (Edge e : forest.getEdges()) {
            e.setHighlightened(true);
        }
    }

}