/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FxPartComponent;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author yuki
 */
public class FpGraph extends FxPartComponent {

    Circle circle;
    double radius;
    ArrayList<ObservableList<PieChart.Data>> datas;
    ArrayList<String> dataNames;
    private Label dataNamesLabel;
    private Label dataLabel;
    private Label dataIdsLabel;
    private TextField dataNamesField;
    private TextField dataField;
    private TextField dataIdsField;
    private PieChart chart;
    private int currentIndex;
    private String chartss;
    private String dataNamess;
    private String datass;

    public FpGraph() {
        super();
        this.id = "FpGraph";
        chartss = "";
        dataNamess = "";
        datass = "";
    }

    @Override
    public void generateParameterEditor() {
        super.generateParameterEditor();
        dataNamesLabel = new Label("Data Names(splited by ,)");
        if (dataNamesField == null) {
            dataNamesField = new TextField(dataNamess);
        }
        dataLabel = new Label("Datas(Datalist must be splited by , and Datas must be splited by space)");
        if (dataField == null) {
            dataField = new TextField(datass);
        }
        dataIdsLabel = new Label("Data ids(splited by space)");
        if (dataIdsField == null) {
            dataIdsField = new TextField(chartss);
        }
        super.parameter_editor.getChildren().addAll(dataNamesLabel, dataNamesField, dataIdsLabel, dataIdsField, dataLabel, dataField);
    }

    @Override
    public void generateComponent() {
        super.generateComponent();
        this.chart = new PieChart();
        this.dataNames = new ArrayList<>();
        this.datas = new ArrayList<>();
        String[] datanames = this.dataNamesField.getText().split(",");
        for (int i = 0; i < datanames.length; i++) {
            this.dataNames.add(datanames[i]);
        }
        String[] datas = this.dataField.getText().split(",");
        String[] ids = this.dataIdsField.getText().split(" ");
        for (int i = 0; i < datas.length; i++) {
            ObservableList<Data> data = javafx.collections.FXCollections.observableArrayList();
            String[] nums = datas[i].split(" ");
            System.out.println("data:" + datas[i]);
            for (int j = 0; j < nums.length; j++) {
                System.out.println("num:" + nums[j]);
                Data cdata = new Data(ids[j], Double.parseDouble(nums[j]));
                data.add(cdata);
            }
            this.datas.add(data);
        }

        chart.setData(this.datas.get((currentIndex = 0)));
        this.root.getChildren().add(chart);
        super.setWidth_scale(Float.parseFloat(this.width.getText()));
        super.setHeight_scale(Float.parseFloat(this.height.getText()));
        this.chart.setPrefSize(this.getWidth_scale(), this.getHeight_scale());

    }

    @Override
    public void regenerateComponent() {
        super.regenerateComponent();
        super.generateComponent();
        this.chart = new PieChart();
        String[] datanames = this.dataNamesField.getText().split(",");
        for (int i = 0; i < datanames.length; i++) {
            this.dataNames.add(datanames[i]);
        }
        String[] datas = this.dataField.getText().split(",");
        String[] ids = this.dataIdsField.getText().split(" ");
        for (int i = 0; i < datas.length; i++) {
            ObservableList<Data> data = javafx.collections.FXCollections.observableArrayList();
            String[] nums = datas[i].split(" ");
            for (int j = 0; j < nums.length; j++) {
                Data cdata = new Data(ids[j], Double.parseDouble(nums[j]));
                data.add(cdata);
            }
            this.datas.add(data);
        }

        chart.setData(this.datas.get((currentIndex = 0)));
        this.root.getChildren().add(chart);
        super.setWidth_scale(Float.parseFloat(this.width.getText()));
        super.setHeight_scale(Float.parseFloat(this.height.getText()));
        this.chart.setPrefSize(this.getWidth_scale(), this.getHeight_scale());
    }

    @Override
    public void generateComponentFromXml() {
        super.generateComponentFromXml();
        
        super.setWidth_scale((int) radius * 2);
        super.setHeight_scale((int) radius * 2);
        
        
    }

    @Override
    public void createDetailXmlElement(Document doc, Element fxPartComponentElement) {
        super.createDetailXmlElement(doc, fxPartComponentElement);

        Element chartsElement = doc.createElement("ChartNames");
        Node chartsNode = doc.createTextNode(this.dataNamesField.getText() + "");
        chartsElement.appendChild(chartsNode);

        fxPartComponentElement.appendChild(chartsElement);

        Element dataNamesElement = doc.createElement("DataNames");
        Node dataNamesNode = doc.createTextNode(this.dataIdsField.getText());
        dataNamesElement.appendChild(dataNamesNode);

        fxPartComponentElement.appendChild(dataNamesElement);

        Element datasElement = doc.createElement("Datas");
        Node datasNode = doc.createTextNode(this.dataField.getText());
        datasElement.appendChild(datasNode);

        fxPartComponentElement.appendChild(datasElement);
    }

    @Override
    public void createParameter(Element element) {
        super.createParameter(element);
        if (element.getTagName().equalsIgnoreCase("ChartNames")) {
            Node chartsNode = element.getChildNodes().item(0);
            chartss = chartsNode.getTextContent();
        }
        if (element.getTagName().equalsIgnoreCase("DataNames")) {
            Node dataNamesNode = element.getChildNodes().item(0);
            dataNamess = dataNamesNode.getTextContent();
        }
        if (element.getTagName().equalsIgnoreCase("Datas")) {
            Node datasNode = element.getChildNodes().item(0);
            datass = datasNode.getTextContent();
        }
        if (!datass.isEmpty() && !dataNamess.isEmpty() && !chartss.isEmpty()) {
            this.chart = new PieChart();
            this.dataNames = new ArrayList<>();
            this.datas = new ArrayList<>();

            String[] datanames;
            datanames = chartss.split(",");
            this.dataNames.addAll(Arrays.asList(datanames));
            String[] datas;
            datas = datass.split(",");
            String[] ids;
            ids = dataNamess.split(" ");
            for (int i = 0; i < datas.length; i++) {
                ObservableList<Data> data = javafx.collections.FXCollections.observableArrayList();
                String[] nums = datas[i].split(" ");
                for (int j = 0; j < nums.length; j++) {
                    Data cdata = new Data(ids[j], Double.parseDouble(nums[j]));
                    data.add(cdata);
                }
                this.datas.add(data);
            }

            chart.setData(this.datas.get((currentIndex = 0)));
            this.root.getChildren().add(chart);
            this.chart.setPrefSize(this.getWidth_scale(), this.getHeight_scale());
            
            dataNamesField = new TextField(chartss);
        dataIdsField = new TextField(dataNamess);
        dataField = new TextField(datass);
        }
    }

    public void nextChart() {
        this.currentIndex++;
        if (currentIndex == this.datas.size()) {
            currentIndex = 0;
        }
        this.chart.setData(this.datas.get(currentIndex));
    }

    public void prevChart() {
        this.currentIndex--;
        if (currentIndex == -1) {
            currentIndex = this.datas.size() - 1;
        }
        this.chart.setData(this.datas.get(currentIndex));
    }
}
