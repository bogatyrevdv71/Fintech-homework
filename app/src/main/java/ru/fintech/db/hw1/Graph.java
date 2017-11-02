package ru.fintech.db.hw1;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by DB on 31.10.2017. так-то
 */

public class Graph {

    private float[][] values;
    private int hash;
    private String horizontalLabel = null;
    private String verticalLabel = null;
    private Integer color;
    private String title = null;
    private boolean horizontalSteps = false;
    private boolean verticalSteps = false;
    private float minX;
    private float minY;
    private float maxX;
    private float maxY;

    Graph(float[][] points) {
      addPoints(points);
    }

    void addPoints(float[][] points) {
        this.values=points.clone();
        Arrays.sort(points, new Comparator<float[]>() {
            @Override
            public int compare(float[] o1, float[] o2) {
                return (int) Math.signum(o1[0]-o2[0]);
            }
        });
        minX=points[0][0];
        maxX=points[points.length-1][0];
        minY=points[0][1];
        maxY=points[0][1];
        for (int i=1;i<points.length;i++) {
            if (points[i][1]>maxY) {
                maxY=points[i][1];
            }
            if (points[i][1]<minY) {
                minY=points[i][1];
            }
        }
        hash = Arrays.deepHashCode(values);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    float getMaxX() {
        return maxX;
    }

    float getMinX() {
        return minX;
    }

    float getMaxY() {
        return maxY;
    }

    float getMinY() {
        return minY;
    }

    int getHash () {
        return hash;
    }
    int getLength(){
        return values.length;
    }
    float getX(int idX) {
        return values[idX][0];
    }
    float getY(int idY) {
        return values[idY][1];
    }
    void setHorizontalSteps(boolean b) {
        this.horizontalSteps=b;
    }
    void setVerticalSteps(boolean b) {
        this.verticalSteps=b;
    }

    public void setColor(int color) {
        this.color = color;
    }

    String getHorizontalLabel() {
        return horizontalLabel;
    }

    void setHorizontalLabel(String horizontalLabel) {
        this.horizontalLabel = horizontalLabel;
    }

    String getVerticalLabel() {
        return verticalLabel;
    }

    void setVerticalLabel(String verticalLabel) {
        this.verticalLabel = verticalLabel;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    boolean isHorizontalSteps() {
        return horizontalSteps;
    }

    boolean isVerticalSteps() {
        return verticalSteps;
    }
}
