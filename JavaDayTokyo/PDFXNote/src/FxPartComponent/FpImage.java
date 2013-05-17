/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FxPartComponent;

import java.io.File;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author Takafumi
 */
public class FpImage extends FxPartComponent {

    File file;
    Image image;
    Label imagePickLabel;
    Button imagePickButton;
    ImageView imageView;
    ImageView imagePreview;
    Stage imageSetStage;
    Scene imageSetScene;
    Pane imageSetPane;
    HBox imageSetHBox;
    VBox imageSetVBox;
    private Label imageWidthLabel;
    private TextField imageWidthValue;
    private Label imageHeightLabel;
    private TextField imageHeightValue;
    private CheckBox aspectCheckBox;
    private Label aspectLabel;
    private HBox aspectHBox;
    private Button applyButton;
    private Button okButton;
    private Button cancelButton;
    private Pane imageViewPane;
    private StackPane imageSetStackPane;
    private String url;

    public FpImage() {
        super();
        this.id = "FpImage";
    }

    @Override
    public void generateParameterEditor() {
        super.generateParameterEditor();
        parameter_editor.getChildren().removeAll(red,green,blue,colorChangerLabel,cMonitor);
        imageView = new ImageView();
        imagePickLabel = new Label("Image");
        imagePickButton = new Button("OPEN");
        super.parameter_editor.getChildren().addAll(imagePickLabel, imagePickButton);
        this.imagePickButton.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent t) {
                imageEdit();
                /*
                 * imageSetStage = new Stage(); StackPane imageSetPane = new
                 * StackPane(); Scene imageSetScene = new Scene(imageSetPane);
                 * imageSetStage.setScene(imageSetScene);
                 */
            }
        });


    }

    @Override
    public void generateComponent() {
        super.generateComponent();
        //this.parameter_editor.getChildren().remove(imageView);
        this.root.getChildren().add(imageView);
        this.root.setOpacity(opacityLevelSlider.getValue());


    }

    void imageEdit() {
        FileChooser fc = new FileChooser();
        fc.setTitle("File Image");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image", "*.jpg"));
        file = fc.showOpenDialog(null);
        this.image = new Image("file://"+file.getAbsolutePath());
        this.imageView = new ImageView();
        this.imageView.setImage(image);
        super.setWidth_scale((int)image.getWidth());
        super.setHeight_scale((int)image.getHeight());
        this.imageSetStage = new Stage();
        this.imageSetStackPane = new StackPane();
        this.imageSetScene = new Scene(imageSetStackPane);
        this.imageSetPane = new Pane();
        this.imageViewPane = new Pane();
        this.imageSetHBox = new HBox();
        this.imageSetVBox = new VBox();
        this.imageWidthLabel = new Label("Width");
        this.imageWidthValue = new TextField(image.getWidth() + "");
        this.imageHeightLabel = new Label("Height");
        this.imageHeightValue = new TextField(image.getHeight() + "");
        this.aspectHBox = new HBox();
        this.aspectLabel = new Label("Fixed Aspect Ratio");
        this.applyButton = new Button("Apply");
        this.okButton = new Button("OK");
        this.cancelButton = new Button("Cancel");
        this.aspectCheckBox = new CheckBox();
        this.aspectHBox.getChildren().addAll(aspectLabel, aspectCheckBox);
        this.imageSetScene.setFill(Color.GRAY);
        this.imageSetPane.getChildren().add(imageSetVBox);
        this.imageSetVBox.getChildren().addAll(imageWidthLabel, imageWidthValue, imageHeightLabel, imageHeightValue, aspectHBox, applyButton, okButton, cancelButton);
        this.imageViewPane.getChildren().add(imageView);
        this.imageSetHBox.getChildren().addAll(imageViewPane, imageSetPane);
        imageSetStackPane.getChildren().add(imageSetHBox);
        this.imageSetStage.setTitle("Image Editor");
        imageSetStage.setResizable(true);
        imageSetStage.setScene(imageSetScene);
        imageSetStage.show();
        this.aspectCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {

            public void changed(ObservableValue<? extends Boolean> ov,
                    Boolean old_val, Boolean new_val) {
                imageWidthValue.lengthProperty().addListener(new ChangeListener<Number>(){
                    //@Override
                    public void changed(ObservableValue<? extends Number>  ov, Number t, Number t1) {
                        if("".equals(imageWidthValue.getText())){
                            imageWidthValue.setText("0");
                        }
                        int w1 = (int) image.getWidth();
                        int h1 = (int) image.getHeight();
                        int w2 = Integer.parseInt(imageWidthValue.getText());
                        int h2 = (h1*w2)/w1;
                        imageHeightValue.setText(h2+"");
                        setWidth_scale(w2);
                        setHeight_scale(h2);
                    }



                    
                });
                
            }
        });
                
        this.cancelButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                //throw new UnsupportedOperationException("Not supported yet.");
                imageSetStage.close();

            }
        });
        this.applyButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                //throw new UnsupportedOperationException("Not supported yet.");
                imageView.setFitHeight(Double.parseDouble(imageHeightValue.getText()));
                imageView.setFitWidth(Double.parseDouble(imageWidthValue.getText()));
                //setHeight_scale(Float.parseFloat(imageHeightValue.getText()));
                //setWidth_scale(Float.parseFloat(imageWidthValue.getText()));
                
                
            }
        });
        this.okButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                //throw new UnsupportedOperationException("Not supported yet.");
                imageView.setFitHeight(Double.parseDouble(imageHeightValue.getText()));
                imageView.setFitWidth(Double.parseDouble(imageWidthValue.getText()));
                setHeight_scale(Float.parseFloat(imageHeightValue.getText()));
                setWidth_scale(Float.parseFloat(imageWidthValue.getText()));
                setHeightText(imageHeightValue.getText());
                setWidthText(imageWidthValue.getText());
                imageViewPane.getChildren().remove(imageView);
                imagePreview = new ImageView();
                imagePreview.setImage(image);
                parameter_editor.getChildren().add(imagePreview);
                imageSetStage.close();

            }
        });
    }
    
    @Override
    public void generateComponentFromXml() {
        super.generateComponentFromXml();
        
        //this.image = new Image("file:///"+file.getAbsolutePath());
        
        this.image = new Image(url);
        this.imageView = new ImageView();
        this.imageView.setImage(image);
        this.imageView.setFitWidth(this.getWidth_scale());
        this.imageView.setFitHeight(this.getHeight_scale());
        this.root.getChildren().add(imageView);
    }

    @Override
    public void createDetailXmlElement(Document doc, Element fxPartComponentElement) {
        super.createDetailXmlElement(doc, fxPartComponentElement);
        Element fileElement = doc.createElement("File");
        Node fileNode = doc.createTextNode(file.getAbsolutePath());
        fileElement.appendChild(fileNode);
        fxPartComponentElement.appendChild(fileElement);
    }

    @Override
    public void createParameter(Element element) {
        super.createParameter(element);
        if(element.getTagName().equalsIgnoreCase("File")){
            Node fileNode = element.getChildNodes().item(0);
            this.url = fileNode.getTextContent();
            this.file = new File(fileNode.getTextContent());
        }
    }
    
}
