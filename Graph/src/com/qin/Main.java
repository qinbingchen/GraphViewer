package com.qin;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import JSON.*;

public class Main extends JFrame {

    Graph graphView = null;

    public void init()
    {
        TextFileReader reader = new TextFileReader();
        while (!reader.chooseFile()) {
            Object[] options = {"Choose file", "Exit"};
            int selection = JOptionPane.showOptionDialog(null,
                    "Please choose a file!",
                    "Choose file",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.DEFAULT_OPTION,
                    null,
                    options,
                    options[0]);
            if (selection == 1) {
                exit();
            }
        }
        String text = reader.read();
        JSONObject obj = new JSONObject(text);
        JSONArray arr = obj.getJSONArray("edges");
        graphView = new Graph(obj.getString("title"), obj.getBoolean("directed"), obj.getBoolean("weighted"));
        for (int i = 0; i < arr.length(); i++) {
            String from = arr.getJSONObject(i).getString("from");
            String to = arr.getJSONObject(i).getString("to");
            if (graphView.weighted) {
                float weight = (float)arr.getJSONObject(i).getDouble("weight");
                graphView.addEdge(from, to, weight);
            } else {
                graphView.addEdge(from, to, 0);
            }
        }

        JPanel panel = new JPanel(new BorderLayout());
        panel.setSize(Constants.maxX, Constants.maxY);
        panel.setPreferredSize(new Dimension(Constants.maxX, Constants.maxY));
        graphView.init();
        panel.add(graphView, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem newItem = new JMenuItem("New...");
        JMenuItem openItem = new JMenuItem("Open...");
        JMenuItem saveItem = new JMenuItem("Save...");
        JMenuItem quitItem = new JMenuItem("Exit");

        newItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                int confirm = JOptionPane.showConfirmDialog(null, "Are you sure?", "Delete edge", JOptionPane.YES_NO_OPTION, JOptionPane.DEFAULT_OPTION);
                if (confirm == 0) {
                    graphView.empty();
                }
            }
        });

        openItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                TextFileReader reader = new TextFileReader();
                if (reader.chooseFile()) {
                    graphView.empty();
                    String text = reader.read();
                    JSONObject obj = new JSONObject(text);
                    graphView.title = obj.getString("title");
                    graphView.directed = obj.getBoolean("directed")   ;
                    graphView.weighted = obj.getBoolean("weighted");
                    JSONArray arr = obj.getJSONArray("edges");
                    for (int i = 0; i < arr.length(); i++) {
                        String from = arr.getJSONObject(i).getString("from");
                        String to = arr.getJSONObject(i).getString("to");
                        if (graphView.weighted) {
                            float weight = (float)arr.getJSONObject(i).getDouble("weight");
                            graphView.addEdge(from, to, weight);
                        } else {
                            graphView.addEdge(from, to, 0);
                        }
                    }
                }
            }
        });

        saveItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                TextFileWriter writer = new TextFileWriter();
                if (writer.chooseFile()) {
                    JSONObject obj = new JSONObject();
                    obj.put("title", Graph.title);
                    obj.put("directed", Graph.directed);
                    obj.put("weighted", Graph.weighted);
                    JSONArray edgeList = new JSONArray();
                    for (Edge e : Graph.graph.getEdges()) {
                        JSONObject o = new JSONObject();
                        o.put("from", e.getFrom().getLabel());
                        o.put("to", e.getTo().getLabel());
                        o.put("weight", e.getWeight());
                        edgeList.put(o);
                    }
                    obj.put("edges", edgeList);
                    writer.write(obj.toString());
                }
            }
        });

        quitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                exit();
            }
        });

        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(quitItem);
        menuBar.add(fileMenu);

        this.setJMenuBar(menuBar);
        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);
        this.setTitle(graphView.title);
        this.setSize(Constants.maxX, Constants.maxY);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.pack();
    }

    public void exit()
    {
        System.exit(0);
    }

    public static void main(String[] args)
    {
        Main app = new Main();
        app.init();
    }

}