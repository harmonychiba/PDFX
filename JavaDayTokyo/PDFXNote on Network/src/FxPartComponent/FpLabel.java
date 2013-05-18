/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FxPartComponent;

//import com.itextpdf.text.Font;
import javafx.geometry.Point3D;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author y_chiba
 */
public class FpLabel extends FxPartComponent {

    String text;
    float label_width;
    float label_height;
    Label textLabel;
    TextArea textArea;
    Point3D X;
    Point3D Y;
    Point3D Z;
    String font_type;
    double font_size;
    
    FontParameterEditor fontPE;
    
    Font font;

    public FpLabel() {
        super();
        this.setTranslation_z(0);
        fontPE = new FontParameterEditor();
        this.id = "FpLabel";
    }

    @Override
    public void generateParameterEditor() {
        super.generateParameterEditor();
        fontPE.generate();
        super.parameter_editor.getChildren().add(fontPE);
        /*
         * this.textLabel = new Label("TEXT");
        this.textArea = new TextArea();
        super.parameter_editor.getChildren().add(2, textLabel);
        super.parameter_editor.getChildren().add(3, textArea);
        */


    }

    @Override
    public void generateComponent() {
        super.generateComponent();
        fontPE.decided();
        this.root.getChildren().clear();
        Label label = new Label();
        //label.setText(fontPE.getText());
        //label.setRotate(super.rotateZ);
        /*super.rotateX = (int) Double.parseDouble(super.rotateXValue.getText());
        System.out.println(this.rotateX);
        super.rotateY = (int) Double.parseDouble(super.rotateYValue.getText());
        super.rotateZ = (int) Double.parseDouble(super.rotateZValue.getText());*/
        this.X = new Point3D(1,0,0);
        this.Y = new Point3D(0,1,0);
        this.Z = new Point3D(0,0,1);
        label.setRotationAxis(X);
        label.setRotate(rotateX);
        label.setRotationAxis(Y);
        label.setRotate(rotateY);
        label.setRotationAxis(Z);
        label.setRotate(rotateZ);
        label.setTextFill(new Color(super.rSlider.getValue() / 255,
                super.gSlider.getValue() / 255,
                super.bSlider.getValue() / 255,
                super.opacityLevelSlider.getValue()));
        label.setFont(fontPE.getFont());
        label.setText(fontPE.getText());
        System.out.println(label.getText());
        this.root.getChildren().add(label);
    }
    @Override
    public void generateComponentFromXml(){
        super.generateComponentFromXml();
        Label label = new Label();
        label.setTextFill(new Color(super.color_red/255,
                super.color_green/ 255,
                super.color_blue/ 255,
                super.opacity));
        label.setFont(fontPE.getFont());
        label.setText(fontPE.getText());
        System.out.println(label.getText());
        this.root.getChildren().add(label);
    }
    @Override
    public void regenerateComponent() {
        super.generateComponent();
        fontPE.decided();
        this.root.getChildren().clear();
        Label label = new Label();
        //label.setText(fontPE.getText());
        //label.setRotate(super.rotateZ);
        super.rotateX = (int) Double.parseDouble(super.rotateXValue.getText());
        System.out.println(this.rotateX);
        super.rotateY = (int) Double.parseDouble(super.rotateYValue.getText());
        super.rotateZ = (int) Double.parseDouble(super.rotateZValue.getText());
        /*this.X = new Point3D(1,0,0);
        this.Y = new Point3D(0,1,0);
        this.Z = new Point3D(0,0,1);
        label.setRotationAxis(X);
        label.setRotate(rotateX);
        label.setRotationAxis(Y);
        label.setRotate(rotateY);
        label.setRotationAxis(Z);
        label.setRotate(rotateZ);*/
        label.setTextFill(new Color(super.rSlider.getValue() / 255,
                super.gSlider.getValue() / 255,
                super.bSlider.getValue() / 255,
                super.opacityLevelSlider.getValue()));
        label.setFont(fontPE.getFont());
        label.setText(fontPE.getText());
        System.out.println(label.getText());
        this.root.getChildren().add(label);
    }

    @Override
    public void createDetailXmlElement(Document doc, Element fxPartComponentElement) {
        fontPE.createXmlElement(doc,fxPartComponentElement);
    }

    @Override
    public void createParameter(Element element) {
        super.createParameter(element);
        fontPE.createParameter(element);
        Label label = new Label();
        label.setText(fontPE.getText());
        label.setFont(fontPE.getFont());
        label.setTextFill(new Color(super.color_red/255.0,super.color_green/255.0,super.color_blue/255.0,super.opacity/100.0));
        this.root.getChildren().clear();
        this.root.getChildren().add(label);
    }
    
    
    
}
