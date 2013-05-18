/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FxPartComponent;

import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author Takafumi
 */
public class FontParameterEditor extends VBox{
    
    private String text;
    private Font font;
    private String font_weight;
    private String font_posture;
    
    private ArrayList<String> font_type_list;
    private ArrayList<String> font_weight_list;
    private ArrayList<String> font_posture_list;
    private ArrayList<FontWeight> font_weight_list_substance;
    private ArrayList<FontPosture> font_posture_list_substance;
    private TextArea text_editor;
    private ComboBox<String> font_type_editor;
    private TextField size_editor;
    private ComboBox<String> font_weight_editor;
    private ComboBox<String> font_posture_editor;
    
    private boolean generated;
    private String fontType;
    private double fontSize;
    
    public FontParameterEditor(){
        font_weight = "NORMAL";
        font_posture = "REGULAR";
        generated = false;
        
        collecteWeightList();
        collectePostureList();
        collecteFontType();
        setDefaultFont();
    }
    private void collecteFontType(){
        this.font_type_list = new ArrayList<>();
        String fonts[]= GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        font_type_list.addAll(Arrays.asList(fonts));
    }
    
    private void collecteWeightList(){
        this.font_weight_list = new ArrayList<>();
        font_weight_list.add("BLACK");
        font_weight_list.add("BOLD");
        font_weight_list.add("EXTRA_BOLD");
        font_weight_list.add("EXTRA_LIGHT");
        font_weight_list.add("LIGHT");
        font_weight_list.add("MEDIUM");
        font_weight_list.add("NOMAL");
        font_weight_list.add("SEMI_BOLD");
        font_weight_list.add("THIN");
        this.font_weight_list_substance = new ArrayList<>();
        font_weight_list_substance.add(FontWeight.BLACK);
        font_weight_list_substance.add(FontWeight.BOLD);
        font_weight_list_substance.add(FontWeight.EXTRA_BOLD);
        font_weight_list_substance.add(FontWeight.EXTRA_LIGHT);
        font_weight_list_substance.add(FontWeight.LIGHT);
        font_weight_list_substance.add(FontWeight.MEDIUM);
        font_weight_list_substance.add(FontWeight.NORMAL);
        font_weight_list_substance.add(FontWeight.SEMI_BOLD);
        font_weight_list_substance.add(FontWeight.THIN);
    }
    private void collectePostureList(){
        this.font_posture_list = new ArrayList<>();
        font_posture_list.add("REGULAR");
        font_posture_list.add("ITALIC");
        this.font_posture_list_substance = new ArrayList<>();
        font_posture_list_substance.add(FontPosture.REGULAR);
        font_posture_list_substance.add(FontPosture.ITALIC);
    }
    private void setDefaultFont(){
        font = Font.getDefault();
    }

    public Font getFont(){
        return this.font;
    }

    public String getText() {
        return text;
    }
    
    public void generate(){
        if(generated == false){
       
        Label text_editor_lbl = new Label("Text");
        Label font_type_editor_lbl = new Label("Family Name");
        Label font_weight_editor_lbl = new Label("Weight");
        Label font_size_editor_lbl = new Label("Size");
        Label font_posture_editor_lbl = new Label("Posture");
        text_editor = new TextArea();
        text_editor.setWrapText(true);
        font_type_editor = new ComboBox<>();
        ObservableList<String> font_type_obs_list = FXCollections.observableArrayList(this.font_type_list);
        font_type_editor.setItems(font_type_obs_list);
        font_type_editor.getSelectionModel().select(getSelectedIndexOfFamily());
        font_weight_editor = new ComboBox<>();
        ObservableList<String> font_weight_obs_list = FXCollections.observableArrayList(this.font_weight_list);
        font_weight_editor.setItems(font_weight_obs_list);
        font_weight_editor.getSelectionModel().select(getSelectedIndexOfWeight());
        size_editor = new TextField();
        size_editor.setText(this.font.getSize()+"");
         font_posture_editor = new ComboBox<>();
        ObservableList<String> font_posture_obs_list = FXCollections.observableArrayList(this.font_posture_list);
        font_posture_editor.setItems(font_posture_obs_list);
        font_posture_editor.getSelectionModel().select(getSelectedIndexOfPosture());
        this.getChildren().addAll(text_editor_lbl,text_editor,
                font_type_editor_lbl,font_type_editor,
                font_weight_editor_lbl,font_weight_editor,
                font_size_editor_lbl,size_editor,
                font_posture_editor_lbl,font_posture_editor);
        generated = true;
        }
    }
    private int getSelectedIndexOfFamily(){
        for(int i = 0;i<this.font_type_list.size();i++){
            if(this.font.getFamily().equalsIgnoreCase(font_type_list.get(i))){
                return i;
            }
        }
        return 0;
    }
    private int getSelectedIndexOfWeight(){
        for(int i = 0;i<this.font_weight_list.size();i++){
            if(this.font_weight.equalsIgnoreCase(this.font_weight_list.get(i))){
                return i;
            }
        }
        return 0;
    }
    private int getSelectedIndexOfPosture(){
        for(int i = 0;i<this.font_posture_list.size();i++){
            if(this.font_posture.equalsIgnoreCase(this.font_posture_list.get(i))){
                return i;
            }
        }
        return 0;
    }
    public void decided(){
        double size = Font.getDefault().getSize();
        if(Double.parseDouble(this.size_editor.getText())>0){
            size = Double.parseDouble(this.size_editor.getText());
        }
        font_weight = font_weight_list.get(font_weight_editor.getSelectionModel().getSelectedIndex());
        font_posture = font_posture_list.get(font_posture_editor.getSelectionModel().getSelectedIndex());
        font_posture = font_posture_list.get(this.getSelectedIndexOfPosture());
        font = Font.font(this.font_type_list.get(this.getSelectedIndexOfFamily()), font_weight_list_substance.get(this.getSelectedIndexOfWeight()),font_posture_list_substance.get(this.getSelectedIndexOfPosture()) ,size);
        
        this.text = this.text_editor.getText();
    }

    void createXmlElement(Document doc, Element fxPartComponentElement) {
        Element textElement = doc.createElement("Text");
        Node textNode = doc.createTextNode(text);
        textElement.appendChild(textNode);
        fxPartComponentElement.appendChild(textElement);
        
        Element fontTypeElement = doc.createElement("FontType");
        Node fontTypeNode = doc.createTextNode(this.font.getFamily());
        fontTypeElement.appendChild(fontTypeNode);
        fxPartComponentElement.appendChild(fontTypeElement);
        
        Element fontSizeElement = doc.createElement("FontSize");
        Node fontSizeNode = doc.createTextNode(this.font.getSize()+"");
        fontSizeElement.appendChild(fontSizeNode);
        fxPartComponentElement.appendChild(fontSizeElement);
        
        Element fontWeightElement = doc.createElement("FontWeight");
        Node fontWeightNode = doc.createTextNode(font_weight);
        fontWeightElement.appendChild(fontWeightNode);
        fxPartComponentElement.appendChild(fontWeightElement);
        
        Element fontPostureElement = doc.createElement("FontPosture");
        Node fontPostureNode = doc.createTextNode(font_posture);
        fontPostureElement.appendChild(fontPostureNode);
        fxPartComponentElement.appendChild(fontPostureElement);
    }

    void createParameter(Element element) {
        if(element.getTagName().equalsIgnoreCase("Text")){
            Node node = element.getChildNodes().item(0);
            this.text = node.getTextContent();
        }
        else if(element.getTagName().equalsIgnoreCase("FontType")){
            Node node = element.getChildNodes().item(0);
            this.fontType = node.getTextContent();
            this.font = Font.font(this.fontType, font_weight_list_substance.get(this.getSelectedIndexOfWeight()),font_posture_list_substance.get(this.getSelectedIndexOfPosture()) ,this.fontSize);
        }
        else if(element.getTagName().equalsIgnoreCase("FontSize")){
            Node node = element.getChildNodes().item(0);
            this.fontSize = Double.parseDouble(node.getTextContent());
            this.font = Font.font(this.fontType, font_weight_list_substance.get(this.getSelectedIndexOfWeight()),font_posture_list_substance.get(this.getSelectedIndexOfPosture()) ,this.fontSize);
        }
        else if(element.getTagName().equalsIgnoreCase("FontSize")){
            Node node = element.getChildNodes().item(0);
            this.font_weight = node.getTextContent();
            this.font = Font.font(this.fontType, font_weight_list_substance.get(this.getSelectedIndexOfWeight()),font_posture_list_substance.get(this.getSelectedIndexOfPosture()) ,this.fontSize);
        }
        else if(element.getTagName().equalsIgnoreCase("FontPosture")){
            Node node = element.getChildNodes().item(0);
            this.font_posture = node.getTextContent();
            this.font = Font.font(this.fontType, font_weight_list_substance.get(this.getSelectedIndexOfWeight()),font_posture_list_substance.get(this.getSelectedIndexOfPosture()) ,this.fontSize);
        }
    }
}
