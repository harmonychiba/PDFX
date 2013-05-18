/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FxPartComponent;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author yuki
 */
public class FpBrowser extends FxPartComponent{
    WebView view;
    Label textLabel;
    TextField textArea;
    private String text;

    public FpBrowser() {
        super();
        this.setTranslation_z(0);
        this.id = "FpBrowser";
    }

    @Override
    public void generateParameterEditor() {
        super.generateParameterEditor();
        textLabel = new Label("initial URL");
        if(textArea == null){
            textArea = new TextField(text);
        }
        this.parameter_editor.getChildren().addAll(textLabel,textArea);
    }

    @Override
    public void generateComponent() {
        super.generateComponent();
        this.root.getChildren().clear();
        view = new WebView();
        view.resize(this.getWidth_scale(), this.getHeight_scale());
        //view.setPrefSize(this.getWidth_scale(), this.getHeight_scale());
        view.getEngine().load(this.textArea.getText());
        this.root.getChildren().add(view);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(FpBrowser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public void generateComponentFromXml(){
        super.generateComponentFromXml();
        this.root.getChildren().clear();
        view = new WebView();
        view.resize(this.getWidth_scale(), this.getHeight_scale());
        //view.setPrefSize(this.getWidth_scale(), this.getHeight_scale());
        view.getEngine().load(text);
        this.root.getChildren().add(view);
    }
    @Override
    public void regenerateComponent() {
        this.root.getChildren().clear();
        view.resize(this.getWidth_scale(), this.getHeight_scale());
        //view.setPrefSize(this.getWidth_scale(), this.getHeight_scale());
        view.getEngine().load(this.textArea.getText());
        this.root.getChildren().add(view);
    }

    @Override
    public void createDetailXmlElement(Document doc, Element fxPartComponentElement) {
        super.createDetailXmlElement(doc, fxPartComponentElement);
        Element fileElement = doc.createElement("URL");
        if(textArea != null){
            this.text = textArea.getText();
        }
        Node fileNode = doc.createTextNode(this.text);
        fileElement.appendChild(fileNode);
        fxPartComponentElement.appendChild(fileElement);
    }

    @Override
    public void createParameter(Element element) {
        super.createParameter(element);
        if(element.getTagName().equalsIgnoreCase("URL")){
            Node fileNode = element.getChildNodes().item(0);
            this.text = fileNode.getTextContent();
        }
    }
    
    
}
