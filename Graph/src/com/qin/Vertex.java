package com.qin;

import processing.core.*;

public class Vertex {

    private PApplet parent;

    private String label;

    private float x, y;
    private float r;

    float dx, dy;

    private boolean fixed = false;
    private boolean visited = false;

    public Vertex(String label, PApplet p)
    {
        this.parent = p;
        this.label = label;
        this.x = parent.random(Constants.maxX);
        this.y = parent.random(Constants.maxY);
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public float getX()
    {
        return x;
    }

    public void setX(float x)
    {
        this.x = x;
    }

    public float getY()
    {
        return y;
    }

    public void setY(float y)
    {
        this.y = y;
    }

    public float getR()
    {
        return r;
    }

    public void setR(float r)
    {
        this.r = r;
    }

    public boolean isFixed()
    {
        return fixed;
    }

    public void setFixed(boolean fixed)
    {
        this.fixed = fixed;
    }

    public boolean isVisited()
    {
        return visited;
    }

    public void setVisited(boolean visited)
    {
        this.visited = visited;
    }

    public void relax()
    {
        float ddx = 0;
        float ddy = 0;
        int a = 1;
        for (Vertex v : Graph.graph.getVertices()) {
            if (v != this) {
                float vx = x - v.x;
                float vy = y - v.y;
                float lensq = vx * vx + vy * vy;
                if (lensq == 0.0f) {
                    ddx += parent.random(1);
                    ddy += parent.random(1);
                } else if (lensq < 10000.0f) {
                    ddx += vx / lensq;
                    ddy += vy / lensq;
                }
            }
        }
        float dlen = parent.mag(ddx, ddy) / 2;
        if (dlen > 0) {
            dx += ddx / dlen;
            dy += ddy / dlen;
        }
    }

    public void update()
    {
        if (!fixed) {
            x += parent.constrain(dx, -5, 5);
            y += parent.constrain(dy, -5, 5);
            x = parent.constrain(x, 0, parent.width);
            y = parent.constrain(y, 0, parent.height);
        }
        dx /= 2;
        dy /= 2;
    }

    public void draw()
    {
        if (Graph.selectedVertex == this) {
            parent.fill(Constants.selectedVertexColor);
        } else if (fixed) {
            parent.fill(Constants.fixedVertexColor);
        } else {
            parent.fill(Constants.vertexColor);
        }
        parent.stroke(0);
        parent.strokeWeight(0.5f);

        parent.ellipseMode(parent.CENTER);
        float d = parent.textWidth(label) + 10;
        r = d / 2;
        parent.ellipse(x, y, d, d);
        parent.fill(0);
        parent.textAlign(parent.CENTER, parent.CENTER);
        parent.text(label, x, y);
    }

}