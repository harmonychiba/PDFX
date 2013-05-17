/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FxPartComponent;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author NPC
 */
public class FpTable extends FxPartComponent{
    private ArrayList<Item> items;
    private int number_of_line;
    private int number_of_row;

    private TextField number_of_line_editor;
    private TextField number_of_row_editor;
    
    private ArrayList<TextField> widthesOfRowEditors;
    private ArrayList<TextField> heightesOfLineEditors;
    
    private static int ITEM_WIDTH = 150;
    private static int ITEM_HEIGHT = 100;
    
    FontParameterEditor[] editors;
    public FpTable(){
        this.id = "FpTable";
        this.number_of_line = 2;
        this.number_of_row = 2;
    }
    @Override
    public void generateParameterEditor() {
        super.generateParameterEditor();
        Label number_of_line_label = new Label("Number of Line");
        number_of_line_editor = new TextField(number_of_line+"");
        Label number_of_row_label = new Label("Number of Row");
        number_of_row_editor = new TextField(number_of_row+"");
        this.parameter_editor.getChildren().addAll(number_of_line_label,this.number_of_line_editor,number_of_row_label,this.number_of_row_editor);        
    }

    @Override
    public void generateComponent() {
        super.generateComponent();
        
        items = new ArrayList<>();
        
        this.number_of_line = Integer.parseInt(number_of_line_editor.getText());
        this.number_of_row = Integer.parseInt(number_of_row_editor.getText());
        
        widthesOfRowEditors = new ArrayList<>();
        heightesOfLineEditors = new ArrayList<>();
        
        for(int i = 0;i<number_of_line;i++){
            TextField field = new TextField();
            heightesOfLineEditors.add(field);
        }
        for(int i = 0;i<number_of_row;i++){
            TextField field = new TextField();
            widthesOfRowEditors.add(field);
        }
        
        final Stage stage_item_setting = new Stage();
        //stage_item_setting.initStyle(StageStyle.UNDECORATED);
        StackPane item_setting_scene_root = new StackPane();
        Scene scene_item_setting = new Scene(item_setting_scene_root);
        stage_item_setting.setScene(scene_item_setting);
        
        VBox lines = new VBox(25);
        HBox widthesEditor = new HBox();
        for(int i = 0;i<number_of_row;i++){
            
            widthesOfRowEditors.get(i).setPromptText("width of "+i+"st row");
            widthesEditor.getChildren().add(widthesOfRowEditors.get(i));
        }
        lines.getChildren().add(widthesEditor);
        HBox[] frames = new HBox[number_of_line];
        
        //final FontParameterEditor[] 
        editors = new FontParameterEditor[number_of_line*number_of_row];
        VBox[] labeled_editor = new VBox[editors.length];
        for(int i = 0;i<frames.length;i++){
            frames[i] = new HBox(20);
            lines.getChildren().add(frames[i]);
            this.heightesOfLineEditors.get(i).setPromptText("height of"+i+"st line");
            frames[i].getChildren().add(heightesOfLineEditors.get(i));
        }
        for(int i = 0;i<editors.length;i++){
            editors[i] = new FontParameterEditor();
            editors[i].generate();
            labeled_editor[i] = new VBox();
            Label label = new Label((i/number_of_row+1)+" * "+(i%number_of_row+1));
            labeled_editor[i].getChildren().addAll(label,editors[i]);
            
            frames[i/number_of_row].getChildren().add(labeled_editor[i]);
        }
        Button OK_button = new Button("OK");
        OK_button.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                System.out.println("handled");
                VBox table = new VBox();
                int width = 0;
                int height = 0;
                for (int i = 0; i < number_of_line; i++) {
                    height+=Integer.parseInt(heightesOfLineEditors.get(i).getText());
                    HBox line = new HBox();
                    for (int j = 0; j < number_of_row; j++) {
                        width+=Integer.parseInt(widthesOfRowEditors.get(j).getText());
                        Text text = new Text();
                        editors[i*number_of_row+j].decided();
                        text.setFont(editors[i*number_of_row+j].getFont());
                        text.setText(editors[i*number_of_row+j].getText());
                        Item item = new Item(Integer.parseInt(widthesOfRowEditors.get(j).getText()),Integer.parseInt(heightesOfLineEditors.get(i).getText()),text);
                        line.getChildren().add(item);
                    }
                    table.getChildren().add(line);
                }
                setWidth_scale(width);
                setHeight_scale(height);
                root.getChildren().clear();
                root.getChildren().add(table);
                stage_item_setting.hide();
            }
        });
        
        lines.getChildren().add(OK_button);
        ScrollPane scr_panel = new ScrollPane();
        scr_panel.setContent(lines);
        item_setting_scene_root.getChildren().add(scr_panel);
        
        
        stage_item_setting.show();
        
    }
    @Override
    public void regenerateComponent() {
        super.generateComponent();
        
        items = new ArrayList<>();
        int pre_number_of_line = number_of_line;
        int pre_number_of_row = number_of_row;
        this.number_of_line = Integer.parseInt(number_of_line_editor.getText());
        this.number_of_row = Integer.parseInt(number_of_row_editor.getText());
        
        widthesOfRowEditors = new ArrayList<>();
        heightesOfLineEditors = new ArrayList<>();
        for(int i = 0;i<heightesOfLineEditors.size();i++){
            ((HBox)heightesOfLineEditors.get(i).getParent()).getChildren().remove(heightesOfLineEditors.get(i));
        }
        for(int i = 0;i<widthesOfRowEditors.size();i++){
            ((HBox)widthesOfRowEditors.get(i).getParent()).getChildren().remove(widthesOfRowEditors.get(i));
        }
        for(int i = heightesOfLineEditors.size();i<number_of_line+1;i++){
            TextField field = new TextField();
            heightesOfLineEditors.add(field);
        }
        for(int i = widthesOfRowEditors.size();i<number_of_row+1;i++){
            TextField field = new TextField();
            widthesOfRowEditors.add(field);
        }
        
        final Stage stage_item_setting = new Stage();
        //stage_item_setting.initStyle(StageStyle.UNDECORATED);
        StackPane item_setting_scene_root = new StackPane();
        Scene scene_item_setting = new Scene(item_setting_scene_root,900,600);
        stage_item_setting.setScene(scene_item_setting);
        
        VBox lines = new VBox(25);
        HBox widthesEditor = new HBox();
        for(int i = 0;i<number_of_row;i++){
            
            //widthesOfRowEditors.get(i).setPromptText("width of "+i+"st row");
            widthesEditor.getChildren().add(widthesOfRowEditors.get(i));
        }
        lines.getChildren().add(widthesEditor);
        HBox[] frames = new HBox[number_of_line];
        
        //final FontParameterEditor[]
        FontParameterEditor[] pre_editors = editors.clone();
        editors = new FontParameterEditor[number_of_line*number_of_row];
        for(int i = 0;i<pre_number_of_line;i++){
            for(int j = 0;j<pre_number_of_row;j++){
                editors[i*number_of_row+j] = pre_editors[i*pre_number_of_row+j];
            }
        }
        VBox[] labeled_editor = new VBox[editors.length];
        
        for(int i = 0;i<frames.length;i++){
            frames[i] = new HBox();
            //this.heightesOfLineEditors.get(i).setPromptText("height of"+i+"st line");
            //System.out.println(heightesOfLineEditors.get(i));
            frames[i].getChildren().add(heightesOfLineEditors.get(i));
        }
        
        for(int i = 0;i<frames.length;i++){
            frames[i] = new HBox(20);
            lines.getChildren().add(frames[i]);
            TextField heightEditor = new TextField();
            heightEditor.setPromptText("height of"+i+"st line");
            frames[i].getChildren().add(heightEditor);
            this.heightesOfLineEditors.add(heightEditor);
        }
        for(int i = 0;i<editors.length;i++){
            if(editors[i] == null) {
                editors[i] = new FontParameterEditor();
            }
            editors[i].generate();
            labeled_editor[i] = new VBox();
            Label label = new Label((i/number_of_row+1)+" * "+(i%number_of_row+1));
            labeled_editor[i].getChildren().addAll(label,editors[i]);
            
            frames[i/number_of_row].getChildren().add(labeled_editor[i]);
        }
        Button OK_button = new Button("OK");
        OK_button.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                System.out.println("handled");
                VBox table = new VBox();
                int width = 0;
                int height = 0;
                for (int i = 0; i < number_of_line; i++) {
                    height+=Integer.parseInt(heightesOfLineEditors.get(i).getText());
                    HBox line = new HBox();
                    for (int j = 0; j < number_of_row; j++) {
                        width+=Integer.parseInt(widthesOfRowEditors.get(j).getText());
                        Text text = new Text();
                        editors[i*number_of_row+j].decided();
                        text.setFont(editors[i*number_of_row+j].getFont());
                        text.setText(editors[i*number_of_row+j].getText());
                        Item item = new Item(Integer.parseInt(widthesOfRowEditors.get(i).getText()),Integer.parseInt(heightesOfLineEditors.get(j).getText()),text);
                        items.add(item);
                        line.getChildren().add(item);
                    }
                    table.getChildren().add(line);
                }
                setWidth_scale(width);
                setHeight_scale(height);
                root.getChildren().clear();
                root.getChildren().add(table);
                stage_item_setting.close();
            }
        });
        
        lines.getChildren().add(OK_button);
        ScrollPane scr_panel = new ScrollPane();
        scr_panel.setContent(lines);
        item_setting_scene_root.getChildren().add(scr_panel);

        stage_item_setting.show();
        
    }

    @Override
    public void generateComponentFromXml() {
        super.generateComponentFromXml();
        items = new ArrayList<>();
        VBox table = new VBox();
                int width = 0;
                int height = 0;
                for (int i = 0; i < number_of_line; i++) {
                    height+=Integer.parseInt(heightesOfLineEditors.get(i).getText());
                    HBox line = new HBox();
                    for (int j = 0; j < number_of_row; j++) {
                        width+=Integer.parseInt(widthesOfRowEditors.get(j).getText());
                        Text text = new Text();
                        //editors[i*number_of_row+j].decided();
                        text.setFont(editors[i*number_of_row+j].getFont());
                        text.setText(editors[i*number_of_row+j].getText());
                        Item item = new Item(Integer.parseInt(widthesOfRowEditors.get(j).getText()),Integer.parseInt(heightesOfLineEditors.get(i).getText()),text);
                        items.add(item);
                        line.getChildren().add(item);
                    }
                    table.getChildren().add(line);
                }
                setWidth_scale(width);
                setHeight_scale(height);
                root.getChildren().add(table);
             heightesOfLineEditors = new ArrayList<>();
        
        for(int i = 0;i<number_of_line;i++){
            TextField field = new TextField();
            heightesOfLineEditors.add(field);
        }
        for(int i = 0;i<number_of_row;i++){
            TextField field = new TextField();
            widthesOfRowEditors.add(field);
        }
        
        final Stage stage_item_setting = new Stage();
        //stage_item_setting.initStyle(StageStyle.UNDECORATED);
        StackPane item_setting_scene_root = new StackPane();
        Scene scene_item_setting = new Scene(item_setting_scene_root);
        stage_item_setting.setScene(scene_item_setting);
        
        VBox lines = new VBox(25);
        HBox widthesEditor = new HBox();
        for(int i = 0;i<number_of_row;i++){
            
            widthesOfRowEditors.get(i).setPromptText("width of "+i+"st row");
            widthesEditor.getChildren().add(widthesOfRowEditors.get(i));
        }
        lines.getChildren().add(widthesEditor);
        HBox[] frames = new HBox[number_of_line];
        
        //final FontParameterEditor[] 
        editors = new FontParameterEditor[number_of_line*number_of_row];
        VBox[] labeled_editor = new VBox[editors.length];
        for(int i = 0;i<frames.length;i++){
            frames[i] = new HBox(20);
            lines.getChildren().add(frames[i]);
            this.heightesOfLineEditors.get(i).setPromptText("height of"+i+"st line");
            frames[i].getChildren().add(heightesOfLineEditors.get(i));
        }
        for(int i = 0;i<editors.length;i++){
            editors[i] = new FontParameterEditor();
            editors[i].generate();
            labeled_editor[i] = new VBox();
            Label label = new Label((i/number_of_row+1)+" * "+(i%number_of_row+1));
            labeled_editor[i].getChildren().addAll(label,editors[i]);
            
            frames[i/number_of_row].getChildren().add(labeled_editor[i]);
        }
        Button OK_button = new Button("OK");
        OK_button.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                System.out.println("handled");
                VBox table = new VBox();
                int width = 0;
                int height = 0;
                for (int i = 0; i < number_of_line; i++) {
                    height+=Integer.parseInt(heightesOfLineEditors.get(i).getText());
                    HBox line = new HBox();
                    for (int j = 0; j < number_of_row; j++) {
                        width+=Integer.parseInt(widthesOfRowEditors.get(j).getText());
                        Text text = new Text();
                        editors[i*number_of_row+j].decided();
                        text.setFont(editors[i*number_of_row+j].getFont());
                        text.setText(editors[i*number_of_row+j].getText());
                        Item item = new Item(Integer.parseInt(widthesOfRowEditors.get(j).getText()),Integer.parseInt(heightesOfLineEditors.get(i).getText()),text);
                        line.getChildren().add(item);
                    }
                    table.getChildren().add(line);
                }
                setWidth_scale(width);
                setHeight_scale(height);
                root.getChildren().clear();
                root.getChildren().add(table);
                stage_item_setting.hide();
            }
        });
        
        lines.getChildren().add(OK_button);
        ScrollPane scr_panel = new ScrollPane();
        scr_panel.setContent(lines);
        item_setting_scene_root.getChildren().add(scr_panel);
        
           
                
    }

    @Override
    public void createDetailXmlElement(Document doc, Element fxPartComponentElement) {
        super.createDetailXmlElement(doc, fxPartComponentElement);
        Element numlineElement = doc.createElement("NumberOfLine");
        org.w3c.dom.Node numlineNode = doc.createTextNode(number_of_line+"");
        numlineElement.appendChild(numlineNode);
        fxPartComponentElement.appendChild(numlineElement);
        
        Element numrowElement = doc.createElement("NumberOfRow");
        org.w3c.dom.Node numrowNode = doc.createTextNode(number_of_row+"");
        numrowElement.appendChild(numrowNode);
        fxPartComponentElement.appendChild(numrowElement);
        Element itemListElement = doc.createElement("ItemList");
        for(int i = 0;i<number_of_line;i++){
            for(int j = 0;j<number_of_row;j++){
                Element itemElement = doc.createElement("Item");
                
                Element widthElement = doc.createElement("Width");
                org.w3c.dom.Node widthNode = doc.createTextNode(widthesOfRowEditors.get(j).getText());
                widthElement.appendChild(widthNode);
                itemElement.appendChild(widthElement);
                
                Element heightElement = doc.createElement("Height");
                org.w3c.dom.Node heightNode = doc.createTextNode(heightesOfLineEditors.get(i).getText());
                heightElement.appendChild(heightNode);
                itemElement.appendChild(heightElement);

                Element fontParameterElement = doc.createElement("FontParameter");
                editors[number_of_row * i + j].createXmlElement(doc, fontParameterElement);
                itemElement.appendChild(fontParameterElement);

                itemListElement.appendChild(itemElement);
            }
        }
        fxPartComponentElement.appendChild(itemListElement);
    }

    @Override
    public void createParameter(Element element) {
        super.createParameter(element);
        if (element.getTagName().equalsIgnoreCase("NumberOfLine")) {
            org.w3c.dom.Node node = element.getChildNodes().item(0);
            this.number_of_line = Integer.parseInt(node.getTextContent());
        } else if (element.getTagName().equalsIgnoreCase("NumberOfRow")) {
            org.w3c.dom.Node node = element.getChildNodes().item(0);
            this.number_of_row = Integer.parseInt(node.getTextContent());
        } else if (element.getTagName().equalsIgnoreCase("ItemList")) {
            ArrayList<FontParameterEditor> editors_temp = new ArrayList<>();
            ArrayList<Integer> width_list = new ArrayList<>();
            ArrayList<Integer> height_list = new ArrayList<>();
            for (int i = 0; i < element.getChildNodes().getLength(); i++) {
                for (int j = 0; j < element.getChildNodes().item(i).getChildNodes().getLength(); j++) {
                    Element item_detail_Element = (Element) element.getChildNodes().item(i).getChildNodes().item(j);
                    if (item_detail_Element.getTagName().equalsIgnoreCase("Width")) {
                        org.w3c.dom.Node node = item_detail_Element.getChildNodes().item(0);
                        width_list.add(Integer.parseInt(node.getTextContent()));
                    } else if (item_detail_Element.getTagName().equalsIgnoreCase("Height")) {
                        org.w3c.dom.Node node = item_detail_Element.getChildNodes().item(0);
                        height_list.add(Integer.parseInt(node.getTextContent()));
                    } else if (item_detail_Element.getTagName().equalsIgnoreCase("FontParameter")) {
                        FontParameterEditor editor = new FontParameterEditor();
                        for(int k = 0;k<item_detail_Element.getChildNodes().getLength();k++){
                            editor.createParameter((Element)(item_detail_Element.getChildNodes().item(k)));
                            
                        }
                        editors_temp.add(editor);
                    }
                }
            }
            editors = new FontParameterEditor[number_of_line * number_of_row];
            editors_temp.toArray(editors);
            this.widthesOfRowEditors = new ArrayList<>();
            this.heightesOfLineEditors = new ArrayList<>();
            for (int j = 0; j < number_of_row; j++) {
                TextField field = new TextField();
                field.setText(width_list.get(j) + "");
                this.widthesOfRowEditors.add(field);
                System.out.println("add");
            }
            for (int j = 0; j < number_of_line; j++) {
                TextField field = new TextField();
                field.setText(height_list.get(j*number_of_row) + "");
                this.heightesOfLineEditors.add(field);
                System.out.println("addd");
            }
        }
    }
    
    
    
}
class Item extends Group{
    public Item(int width,int height,Text text){
        text.setWrappingWidth(width);
        text.setTranslateX(-width/2);
        text.setTextOrigin(VPos.CENTER);
        text.setTextAlignment(TextAlignment.CENTER);
        Line upper_line = new Line(-width/2, -height/2, width/2, -height/2);
        Line bottom_line = new Line(-width/2,height/2,width/2,height/2);
        Line leftside_line = new Line(-width/2,-height/2,-width/2,height/2);
        Line rightside_line = new Line(width/2,height/2,width/2,-height/2);
        this.getChildren().addAll(upper_line,leftside_line,bottom_line,rightside_line,text);
    }
}
