/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FxPartComponent;


import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author Takafumi
 */
public class FpLine extends FxPartComponent{
    private Line line;
    private Label sxLabel;
    private Label syLabel;
    private Label exLabel;
    private Label eyLabel;
    private TextField eyValue;
    private TextField sxValue;
    private TextField syValue;
    private TextField exValue;
    private VBox vBox;
    private Label thickLabel;
    private TextField thickValue;
    double startX;
    double startY;
    double endX;
    double endY;
    double thickness;
    
    public FpLine(){
        super();
        this.id = "FpLine";
    
    }
    
    @Override
    public void generateParameterEditor(){
        super.generateParameterEditor();
        this.sxLabel = new Label("Start X");
        this.syLabel = new Label("Start Y");
        this.exLabel = new Label("End X");
        this.eyLabel = new Label("End Y");
        this.thickLabel = new Label("Thickness");
        this.sxValue = new TextField("0");
        this.syValue = new TextField("0");
        this.exValue = new TextField("0");
        this.eyValue = new TextField("0");
        this.thickValue = new TextField("1");
        this.vBox = new VBox();
        this.vBox.getChildren().addAll(sxLabel,sxValue,syLabel,syValue,exLabel,exValue,eyLabel,eyValue,thickLabel,thickValue);
        super.parameter_editor.getChildren().add(10, vBox);
        
    }
    
    @Override
    public void generateComponent(){
        super.generateComponent();
        this.line = new Line();
        this.root.getChildren().add(line);
        startX = Double.parseDouble(sxValue.getText());
        startY = Double.parseDouble(syValue.getText());
        endX = Double.parseDouble(exValue.getText());
        endY = Double.parseDouble(eyValue.getText());
        thickness = Double.parseDouble(thickValue.getText());
        line.setStartX(startX);
        line.setStartY(startY);
        line.setEndX(endX);
        line.setEndY(endY);
        line.setStrokeWidth(thickness);
        line.setFill(new Color(super.rSlider.getValue()/255,super.gSlider.getValue()/255,super.bSlider.getValue()/255,super.opacityLevelSlider.getValue()));
        super.setWidth_scale((int)(endX-startX));
        super.setHeight_scale((int)(endY-startY));
    }

    @Override
    public void regenerateComponent() {
        super.regenerateComponent();
    }

    @Override
    public void createDetailXmlElement(Document doc, Element fxPartComponentElement) {
        super.createDetailXmlElement(doc, fxPartComponentElement);
        Element sxElement = doc.createElement("StartX");
        Node sxNode = doc.createTextNode(this.startX+"");
        sxElement.appendChild(sxNode);        
        fxPartComponentElement.appendChild(sxElement);
        
        Element syElement = doc.createElement("StartY");
        Node syNode = doc.createTextNode(this.startY+"");
        syElement.appendChild(syNode);        
        fxPartComponentElement.appendChild(syElement);
        
        Element exElement = doc.createElement("EndX");
        Node exNode = doc.createTextNode(this.endX+"");
        exElement.appendChild(exNode);        
        fxPartComponentElement.appendChild(exElement);
        
        Element eyElement = doc.createElement("EndY");
        Node eyNode = doc.createTextNode(this.endY+"");
        eyElement.appendChild(eyNode);        
        fxPartComponentElement.appendChild(eyElement);
        
        Element thickElement = doc.createElement("Thickness");
        Node thickNode = doc.createTextNode(this.thickness+"");
        thickElement.appendChild(thickNode);        
        fxPartComponentElement.appendChild(thickElement);
    }

    @Override
    public void createParameter(Element element) {
        super.createParameter(element);
        if(element.getTagName().equalsIgnoreCase("StartX")){
            Node sxNode = element.getChildNodes().item(0);
            this.startX = Double.parseDouble(sxNode.getTextContent());
        }
        else if(element.getTagName().equalsIgnoreCase("StartY")){
            Node syNode = element.getChildNodes().item(0);
            this.startY = Double.parseDouble(syNode.getTextContent());
        }
        else if(element.getTagName().equalsIgnoreCase("EndX")){
            Node exNode = element.getChildNodes().item(0);
            this.endX = Double.parseDouble(exNode.getTextContent());
        }
        else if(element.getTagName().equalsIgnoreCase("EndY")){
            Node eyNode = element.getChildNodes().item(0);
            this.endY = Double.parseDouble(eyNode.getTextContent());
        }
        else if(element.getTagName().equalsIgnoreCase("Thickness")){
            Node thickNode = element.getChildNodes().item(0);
            this.thickness = Double.parseDouble(thickNode.getTextContent());
        }
    }

    @Override
    public void generateComponentFromXml() {
        super.generateComponentFromXml();
        line = new Line();
        this.root.getChildren().add(line);
        line.setStartX(startX);
        line.setStartY(startY);
        line.setEndX(endX);
        line.setEndY(endY);
        line.setFill(new Color(super.color_red/255,super.color_green/255,super.color_blue/255,super.opacity));
        super.setWidth_scale((int)(endX-startX));
        super.setHeight_scale((int)(endY-startY));
        
        
    }
    
    

    
}