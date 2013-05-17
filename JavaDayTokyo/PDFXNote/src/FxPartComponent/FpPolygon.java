/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FxPartComponent;

import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author NPC
 */
public class FpPolygon extends FxPartComponent{
    Polygon polygon;
    ArrayList<HBox> editors;
    ArrayList<Integer> xlist;
    ArrayList<Integer> ylist;
    VBox editor_area;
    Group group;
    
    public FpPolygon(){
        super();
        this.id = "FpPolygon";
        
        editors = new ArrayList<>();
        xlist = new ArrayList<>();
        ylist = new ArrayList<>();
        editor_area = new VBox();
        group = new Group();
        
    }
    
    @Override
    public void generateParameterEditor() {
        super.generateParameterEditor();
    }

    @Override
    public void generateComponent() {
        super.generateComponent();
        final Stage stage = new Stage();
        StackPane pane = new StackPane();
        Scene scene = new Scene(pane,840,480);
        stage.setScene(scene);

        final ScrollPane aspane = new ScrollPane();
        aspane.setPrefSize(200, 480);
        aspane.setContent(editor_area);
        editor_area.setPrefWidth(200);
        ScrollPane spane = new ScrollPane();
        spane.setPrefSize(640, 480);
        
        group = new Group();
        Rectangle rec = new Rectangle(1000, 1000);rec.setFill(javafx.scene.paint.Color.WHEAT);
        group.getChildren().add(rec);
        spane.setContent(group);
        
                HBox rootb = new HBox();
                rootb.getChildren().addAll(spane,aspane);
                pane.getChildren().add(rootb);
        
        
        
        group.setOnMouseClicked(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent t) {
                int pointx = (int) t.getX();
                int pointy = (int) t.getY();
                xlist.add(pointx);
                ylist.add(pointy);
                double points[] = new double[xlist.size() * 2];
                for (int i = 0; i < xlist.size(); i++) {
                            points[2 * i] = xlist.get(i);
                            points[2 * i + 1] = ylist.get(i);
                        }
                group.getChildren().remove(polygon);
                        polygon= new Polygon(points);
                        group.getChildren().add(polygon);
                
                HBox editor = new HBox();
                final Label label = new Label(editors.size()+"");
                TextField fieldx = new TextField();
                fieldx.setText(pointx+"");
                TextField fieldy = new TextField();
                fieldy.setText(pointy+"");
                fieldx.textProperty().addListener(new ChangeListener<String>(){

                    @Override
                    public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                        double points[] = new double[editors.size() * 2];
                        xlist.set(Integer.parseInt(label.getText()),Integer.parseInt(t1));
                        for (int i = 0; i < editors.size(); i++) {
                            points[2 * i] = xlist.get(i);
                            points[2 * i + 1] = ylist.get(i);
                        }
                        group.getChildren().remove(polygon);
                        polygon= new Polygon(points);
                        group.getChildren().add(polygon);
                    }
                    
                });
                fieldy.textProperty().addListener(new ChangeListener<String>(){

                    @Override
                    public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                        double points[] = new double[editors.size() * 2];
                        ylist.set(Integer.parseInt(label.getText()),Integer.parseInt(t1));
                        for (int i = 0; i < editors.size(); i++) {
                            points[2 * i] = xlist.get(i);
                            points[2 * i + 1] = ylist.get(i);
                        }
                        group.getChildren().remove(polygon);
                        polygon= new Polygon(points);
                        group.getChildren().add(polygon);
                    }
                    
                });
                editor.getChildren().addAll(label,fieldx,fieldy);
                editors.add(editor);
                editor_area.getChildren().add(editor);
            }
            
        });
        Button ok_button = new Button("OK");
        ok_button.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent t) {
                group.getChildren().remove(polygon);
                polygon.setFill(new Color(color_red,color_green,color_blue,opacity));
                root.getChildren().add(polygon);
                
                aspane.setContent(null);
                stage.close();
            }
        });
        
        editor_area.getChildren().add(ok_button);
        
        stage.show();
    }

    @Override
    public void generateComponentFromXml() {
        super.generateComponentFromXml();
        for(int i = 0;i<xlist.size();i++){
            HBox editor = new HBox();
                final Label label = new Label(editors.size()+"");
                TextField fieldx = new TextField();
                fieldx.setText(xlist.get(i)+"");
                TextField fieldy = new TextField();
                fieldy.setText(ylist.get(i)+"");
                fieldx.textProperty().addListener(new ChangeListener<String>(){

                    @Override
                    public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                        double points[] = new double[editors.size() * 2];
                        xlist.set(Integer.parseInt(label.getText()),Integer.parseInt(t1));
                        for (int i = 0; i < editors.size(); i++) {
                            points[2 * i] = xlist.get(i);
                            points[2 * i + 1] = ylist.get(i);
                        }
                        group.getChildren().remove(polygon);
                        polygon= new Polygon(points);
                        group.getChildren().add(polygon);
                    }
                    
                });
                fieldy.textProperty().addListener(new ChangeListener<String>(){

                    @Override
                    public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                        double points[] = new double[editors.size() * 2];
                        ylist.set(Integer.parseInt(label.getText()),Integer.parseInt(t1));
                        for (int i = 0; i < editors.size(); i++) {
                            points[2 * i] = xlist.get(i);
                            points[2 * i + 1] = ylist.get(i);
                        }
                        group.getChildren().remove(polygon);
                        polygon= new Polygon(points);
                        group.getChildren().add(polygon);
                    }
                    
                });
                editor.getChildren().addAll(label,fieldx,fieldy);
                editors.add(editor);
                editor_area.getChildren().add(editor);
        }
        double[] array = new double[xlist.size()*2];
        for(int i =0;i<xlist.size();i++){
            array[i*2] = xlist.get(i);
            array[i*2+1] = ylist.get(i);
        }
        polygon = new Polygon(array);
        root.getChildren().add(polygon);
    }

    @Override
    public void regenerateComponent() {
        super.regenerateComponent();
        super.generateComponent();
        final Stage stage = new Stage();
        StackPane pane = new StackPane();
        Scene scene = new Scene(pane,840,480);
        stage.setScene(scene);
        final ScrollPane aspane = new ScrollPane();
        aspane.setPrefSize(200, 480);
        aspane.setContent(editor_area);
        editor_area.setPrefWidth(200);
        ScrollPane spane = new ScrollPane();
        spane.setPrefSize(640, 480);
        
        group = new Group();
        Rectangle rec = new Rectangle(1000, 1000);rec.setFill(javafx.scene.paint.Color.WHEAT);
                group.getChildren().add(rec);
                root.getChildren().remove(polygon);
                group.getChildren().add(polygon);
        spane.setContent(group);
        
        HBox rootb = new HBox();
                rootb.getChildren().addAll(spane,aspane);
                pane.getChildren().add(rootb);
        
        group.setOnMouseClicked(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent t) {
                int pointx = (int) t.getX();
                int pointy = (int) t.getY();
                xlist.add(pointx);
                ylist.add(pointy);
                
                double points[] = new double[xlist.size() * 2];
                for (int i = 0; i < xlist.size(); i++) {
                            points[2 * i] = xlist.get(i);
                            points[2 * i + 1] = ylist.get(i);
                        }
                group.getChildren().remove(polygon);
                        polygon= new Polygon(points);
                        group.getChildren().add(polygon);
                
                HBox editor = new HBox();
                final Label label = new Label(editors.size()+"");
                TextField fieldx = new TextField();
                fieldx.setText(pointx+"");
                TextField fieldy = new TextField();
                fieldy.setText(pointy+"");
                fieldx.textProperty().addListener(new ChangeListener<String>(){

                    @Override
                    public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                        double points[] = new double[editors.size() * 2];
                        xlist.set(Integer.parseInt(label.getText()),Integer.parseInt(t1));
                        for (int i = 0; i < editors.size(); i++) {
                            points[2 * i] = xlist.get(i);
                            points[2 * i + 1] = ylist.get(i);
                        }
                        group.getChildren().remove(polygon);
                        polygon= new Polygon(points);
                        group.getChildren().add(polygon);
                    }
                    
                });
                fieldy.textProperty().addListener(new ChangeListener<String>(){

                    @Override
                    public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                        double points[] = new double[editors.size() * 2];
                        ylist.set(Integer.parseInt(label.getText()),Integer.parseInt(t1));
                        for (int i = 0; i < editors.size(); i++) {
                            points[2 * i] = xlist.get(i);
                            points[2 * i + 1] = ylist.get(i);
                        }
                        group.getChildren().remove(polygon);
                        polygon= new Polygon(points);
                        group.getChildren().add(polygon);
                    }
                    
                });
                editor.getChildren().addAll(label,fieldx,fieldy);
                editors.add(editor);
                editor_area.getChildren().add(editor);
            }
            
        });
        Button ok_button = new Button("OK");
        ok_button.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent t) {
                group.getChildren().remove(polygon);
                polygon.setFill(new Color(color_red,color_green,color_blue,opacity));
                root.getChildren().add(polygon);
                
                aspane.setContent(null);
                stage.close();
            }
        });
        
        editor_area.getChildren().add(ok_button);
        
        stage.show();
    }

    @Override
    public void createDetailXmlElement(Document doc, Element fxPartComponentElement) {
        super.createDetailXmlElement(doc, fxPartComponentElement);
        for(int i = 0;i<this.xlist.size();i++){
            Element element = doc.createElement("Point");
            Element xelement = doc.createElement("X");
            Element yelement = doc.createElement("Y");
            Node xnode = doc.createTextNode(this.xlist.get(i)+"");
            Node ynode = doc.createTextNode(this.ylist.get(i)+"");
            xelement.appendChild(xnode);
            yelement.appendChild(ynode);
            element.appendChild(xelement);
            element.appendChild(yelement);
            fxPartComponentElement.appendChild(element);
        }
    }

    @Override
    public void createParameter(Element element) {
        super.createParameter(element);
        if(element.getTagName().equalsIgnoreCase("Point")){
            int x = Integer.parseInt(element.getChildNodes().item(0).getChildNodes().item(0).getTextContent());
            int y = Integer.parseInt(element.getChildNodes().item(1).getChildNodes().item(0).getTextContent());
            xlist.add(x);
            ylist.add(y);
        }
    }
    
}
