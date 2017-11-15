package zuhause.chart;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Eduardo Folly
 */
public class DataSet {

    private float lineTension = 0;
    private String label;
    private List<Float> data = new ArrayList<>();
    private int pointRadius = 0;
    private String borderColor = "rgba(40, 53, 147, 1)";
    private String backgroundColor = "rgba(40, 53, 147, 0.2)";

    public float getLineTension() {
        return lineTension;
    }

    public void setLineTension(float lineTension) {
        this.lineTension = lineTension;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Float> getData() {
        return data;
    }

    public void setData(List<Float> data) {
        this.data = data;
    }

    public void addData(Float d) {
        this.data.add(d);
    }

    public int getPointRadius() {
        return pointRadius;
    }

    public void setPointRadius(int pointRadius) {
        this.pointRadius = pointRadius;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

}
