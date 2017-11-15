package zuhause.chart;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Eduardo Folly
 */
public class Data {

    private List<String> labels = new ArrayList<>();
    private List<DataSet> datasets = new ArrayList<>();

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public void addLabel(String label) {
        this.labels.add(label);
    }

    public List<DataSet> getDatasets() {
        return datasets;
    }

    public void setDatasets(List<DataSet> datasets) {
        this.datasets = datasets;
    }

    public void addDataSet(DataSet dataSet) {
        this.datasets.add(dataSet);
    }

}
