/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FxPartComponent;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author Takafumi
 */
public class FpCircle extends FxPartComponent{
    Circle circle;
    private Label radiusLabel;
    private TextField radiusValue;
    double radius;
    
    public FpCircle(){
        super();
        this.id = "FpCircle";
    }
    
    @Override
     public void generateParameterEditor(){
         super.generateParameterEditor();
         radiusLabel = new Label("Radius");
         radiusValue = new TextField();
         super.parameter_editor.getChildren().addAll(radiusLabel,radiusValue);
         
     }
    @Override
    public void generateComponent(){
        super.generateComponent();
        circle = new Circle();
        this.radius = Float.parseFloat(this.radiusValue.getText());
        this.root.getChildren().add(circle);
        this.circle.setCenterX(Float.parseFloat(this.radiusValue.getText()));
        this.circle.setCenterY(Float.parseFloat(this.radiusValue.getText()));
        this.circle.setRadius(Float.parseFloat(this.radiusValue.getText()));
        this.circle.setFill(new Color(super.rSlider.getValue() / 255,
                super.gSlider.getValue() / 255,
                super.bSlider.getValue() / 255,
                super.opacityLevelSlider.getValue()));
        super.setWidth_scale((int)radius*2);
        super.setHeight_scale((int)radius*2);
    }
    
    @Override
    public void regenerateComponent(){
        super.generateComponent();
    }

    @Override
    public void generateComponentFromXml() {
        super.generateComponentFromXml();
        circle = new Circle();
        this.root.getChildren().add(circle);
        circle.setCenterX(radius);
        circle.setCenterY(radius);
        circle.setRadius(radius);
        
        this.circle.setFill(new Color(super.color_red/255.0,
                super.color_green / 255.0,
                super.color_blue/ 255.0,
                super.opacity));
        super.setWidth_scale((int)radius*2);
        super.setHeight_scale((int)radius*2);
    }

    @Override
    public void createDetailXmlElement(Document doc, Element fxPartComponentElement) {
        super.createDetailXmlElement(doc, fxPartComponentElement);
        
        Element radiusElement = doc.createElement("Radius");
        Node radiusNode = doc.createTextNode(this.radius+"");
        radiusElement.appendChild(radiusNode);
        
        fxPartComponentElement.appendChild(radiusElement);
    }

    @Override
    public void createParameter(Element element) {
        super.createParameter(element);
        
        if(element.getTagName().equalsIgnoreCase("Radius")){
            Node radiusNode = element.getChildNodes().item(0);
            this.radius = Double.parseDouble(radiusNode.getTextContent());
        }
    }
    
}
