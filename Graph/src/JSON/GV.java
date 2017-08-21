//package visualizer;
//
//import java.awt.*;
//import javax.swing.*;
//import edu.uci.ics.jung.graph.*;
//import edu.uci.ics.jung.graph.util.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.*;
//import java.util.concurrent.*;
//import processing.core.*;
//
///**
// *
// * @author szm
// */
//public class GraphView extends PApplet {
//
//    //private UndirectedSparseMultigrapg<com.qin.Vertex, com.qin.Edge> g = null;
//    private SparseMultigraph<com.qin.Vertex, com.qin.Edge> graph = null;
//    int startTime;
//    final int animationInterval = 1000; //1s
//    private com.qin.Vertex selected = null;
//
//    private JPopupMenu menu = null;
//
//    public GraphView() {
//        graph = new SparseMultigraph<>();
//        this.setPreferredSize(new Dimension(800, 800));
//        //
//        menu = new JPopupMenu();
//        JMenuItem item1 = new JMenuItem("Item1");
//        JMenuItem item2 = new JMenuItem("Item2");
//        menu.add(item1);
//        menu.add(item2);
//        //
//        item1.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                bfs();
//            }
//        });
//    }
//
//    public void initGraphData() {
//        addEdge("joe", "food");
//        addEdge("joe", "dog");
//        addEdge("joe", "tea");
//        addEdge("joe", "cat");
//        addEdge("joe", "table");
//        addEdge("table", "plate");
//        addEdge("plate", "food");
//        addEdge("food", "mouse");
//        addEdge("food", "dog");
//        addEdge("mouse", "cat");
//        addEdge("table", "cup");
//        addEdge("cup", "tea");
//        addEdge("dog", "cat");
//        addEdge("cup", "spoon");
//        addEdge("plate", "fork");
//        addEdge("dog", "flea1");
//        addEdge("dog", "flea2");
//        addEdge("flea1", "flea2");
//        addEdge("plate", "knife");
//    }
//
//    public void addEdge(String s1, String s2) {
//        com.qin.Vertex v1 = null;
//        com.qin.Vertex v2 = null;
//        for (com.qin.Vertex v : graph.getVertices()) {
//            if (v.label.equals(s1)) {
//                v1 = v;
//            }
//            if (v.label.equals(s2)) {
//                v2 = v;
//            }
//        }
//        if (v1 == null) {
//            v1 = new com.qin.Vertex(s1, this);
//            graph.addVertex(v1);
//        }
//        if (v2 == null) {
//            v2 = new com.qin.Vertex(s2, this);
//            graph.addVertex(v2);
//        }
//        addEdge(v1, v2);
//    }
//
//    public void addEdge(com.qin.Vertex v1, com.qin.Vertex v2) {
//        com.qin.Edge e = new com.qin.Edge(v1, v2, this);
//        graph.addEdge(e, v1, v2, EdgeType.UNDIRECTED);
//    }
//
//    @Override
//    public void setup() {
//        //noLoop();
//        size(800, 800);
//        smooth();
//        initGraphData();
//    }
//
//    @Override
//    public void draw() {
//        background(255);
//        for (com.qin.Edge e : this.getGraph().getEdges()) {
//            e.relax();
//        }
//        for (com.qin.Vertex v : this.getGraph().getVertices()) {
//            v.relax();
//        }
//        for (com.qin.Vertex v : this.getGraph().getVertices()) {
//            v.update();
//        }
//        for (com.qin.Edge e : this.getGraph().getEdges()) {
//            e.draw();
//        }
//        for (com.qin.Vertex v : this.getGraph().getVertices()) {
//            v.draw();
//        }
////        if (millis() - startTime > animationInterval && DFSStarted) {
////            DFSNodeList.get(currentLocation++).fixed = true;
////            startTime = millis();
////        }
//    }
//
//    @Override
//    public void mousePressed() {
//
//        float d = 400;
//
//        for(com.qin.Vertex v : graph.getVertices()) {
////            float r = (mouseX-v.x)*(mouseX-v.x) + (mouseY-v.y)*(mouseY-v.y);
////            if(r<d) {
////                d = r;
////                selected = v;
////            }
//            if(2*Math.abs(mouseX-v.x)<v.w && 2*Math.abs(mouseY-v.y)<v.h) {
//                selected = v;
//            }
//        }
//        if(selected!=null) {
//            if(mouseButton==LEFT) {
//                selected.fixed = !selected.fixed;
//            } else {
//                menu.show(this, mouseX, mouseY);
//            }
//        }
//    }
//
//    @Override
//    public void mouseDragged() {
//        if(selected!=null) {
//            selected.x = mouseX;
//            selected.y = mouseY;
//        }
//    }
//
//    @Override
//    public void mouseReleased() {
//        selected = null;
//    }
//
//    public SparseMultigraph<com.qin.Vertex, com.qin.Edge> getGraph() {
//        return graph;
//    }
//
//    public void setGraph(SparseMultigraph<com.qin.Vertex, com.qin.Edge> graph) {
//        this.graph = graph;
//    }
//
//    public void bfs() {
//        int time = 0;
//        Collection<com.qin.Vertex> vertices = graph.getVertices();
//        Collection<com.qin.Vertex> visited = new HashSet<>();
//        Queue<com.qin.Vertex> q = new LinkedBlockingQueue<>();
//        for(com.qin.Vertex v : vertices) {
//            q.offer(v);
//            break;
//        }
//        while(!q.isEmpty()) {
//            com.qin.Vertex v = q.poll();
//            for(com.qin.Vertex s : graph.getSuccessors(v)) {
//                if(!visited.contains(s)) {
//                    q.offer(s);
//                }
//            }
//            if(time == 300000) {
//                v.visit();
//                visited.add(v);
//                time = 0;
//            }
//            ++time;
//        }
//    }
//
//}