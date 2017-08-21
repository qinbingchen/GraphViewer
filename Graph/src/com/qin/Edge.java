package com.qin;

import processing.core.*;

public class Edge {

    private PApplet parent;

    private Vertex from;
    private Vertex to;
    private float weight;

    private float len;

    private boolean highlightened = false;

    public Vertex getFrom()
    {
        return from;
    }

    public void setFrom(Vertex from)
    {
        this.from = from;
    }

    public Vertex getTo()
    {
        return to;
    }

    public void setTo(Vertex to)
    {
        this.to = to;
    }

    public float getWeight()
    {
        return weight;
    }

    public void setWeight(float weight)
    {
        this.weight = weight;
    }

    public boolean isHighlightened()
    {
        return highlightened;
    }

    public void setHighlightened(boolean highlightened)
    {
        this.highlightened = highlightened;
    }

    public Edge(Vertex from, Vertex to, float weight, PApplet p)
    {
        this.parent = p;
        this.from = from;
        this.to = to;
        this.weight = weight;
        this.len = 100;
    }

    public void relax()
    {
        float vx = to.getX() - from.getX();
        float vy = to.getY() - from.getY();
        float d = parent.mag(vx, vy);
        if (d > 0) {
            float f = (len - d) / (d * 30); //初始值为3，此值越大节点间斥力越大，数字越大增长越不明显
            float dx = f * vx;
            float dy = f * vy;
            to.dx += dx;
            to.dy += dy;
            from.dx -= dx;
            from.dy -= dy;
        }
    }

    public void draw()
    {
        if (Graph.selectedEdge == this || highlightened) {
            parent.stroke(Constants.selectedEdgeColor);
            parent.strokeWeight(Constants.selectedEdgeWidth);
            parent.fill(Constants.selectedEdgeColor);
        } else {
            parent.stroke(Constants.edgeColor);
            parent.strokeWeight(Constants.edgeWidth);
            parent.fill(Constants.edgeColor);
        }
        parent.line(from.getX(), from.getY(), to.getX(), to.getY());
        if (Graph.directed) {
            parent.pushMatrix();
            parent.translate(to.getX(), to.getY());
            parent.rotate(parent.atan2(to.getY() - from.getY(), to.getX() - from.getX()));
            parent.triangle(0 - to.getR(), 0, -10 - to.getR(), 5, -10 - to.getR(), -5);
            parent.popMatrix();
        }
        if (Graph.weighted) {
            float d = parent.dist(from.getX(), from.getY(), to.getX(), to.getY());
            float deltaX = to.getX() - from.getX();
            float deltaY = to.getY() - from.getY();
            float centerX = (from.getX() + to.getX()) / 2 - (to.getR() - from.getR()) / d * deltaX;
            float centerY = (from.getY() + to.getY()) / 2 - (to.getR() - from.getR()) / d * deltaY;
            float wX = centerX + 15 / d * deltaY;
            float wY = centerY - 15 / d * deltaX;

            parent.textAlign(parent.CENTER, parent.CENTER);
            parent.text(String.valueOf(weight), wX, wY);
        }
    }

}