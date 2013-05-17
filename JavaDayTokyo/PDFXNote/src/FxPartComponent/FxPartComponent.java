/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FxPartComponent;

import Animation.Animable;
import Animation.Animation;
import Animation.AnimationEvent;
import Animation.AnimationEventHandler;
import java.awt.Transparency;
import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
/**
 *
 * @author y_chiba
 */
public class FxPartComponent implements Animable{
    
    public static int FRAME_TYPE_MAIN_SELECTION = 1;
    public static int FRAME_TYPE_SUB_SELECTION = 2;
    
    private float translation_x;
    private float translation_y;
    private float translation_z;
    public int color_red;
    public int color_green;
    public int color_blue;
    public float opacity;
    
    private float width_scale;
    private float height_scale;
    
    private Text opacityLabel;
    Slider opacityLevelSlider;
    private Label opacityValue;
    
    Label colorChangerLabel;
    private Text rLabel;
    private Text gLabel;
    private Text bLabel;
    Slider rSlider;
    Slider gSlider;
    Slider bSlider;
    private Text rValue;
    private Text gValue;
    private Text bValue;
    public Rectangle cMonitor;
    
    private Label widthLabel;
    TextField width;
    private Label heightLabel;
    TextField height;
    
    private Label rotateXLabel;
    private Slider rotateXSlider;
    Text rotateXValue;
    private Label rotateYLabel;
    private Slider rotateYSlider;
    Text rotateYValue;
    private Label rotateZLabel;
    private Slider rotateZSlider;
    Text rotateZValue;
    int rotateX;
    int rotateY;
    int rotateZ;
    
    private Label transXLabel;
    private Label transYLabel;
    private Label transZLabel;
    private TextField transX;
    private TextField transY;
    private TextField transZ;
    
    
    public String id;
    TextField id_editor;
    private Text idLabel;
    
    
    Group root;
    VBox parameter_editor;
    HBox red;
    HBox green;
    HBox blue;
    
    private ArrayList<AnimationEventHandler> handler_loadevent;
    private ArrayList<AnimationEventHandler> handler_clickevent;
    private ArrayList<AnimationEventHandler> handler_finished;
    private ArrayList<AnimationEventHandler> handler_otherevent;
    
    private ArrayList<Animation> animation_list;
    
    public FxPartComponent(){
        this.handler_loadevent = new ArrayList<>();
        this.handler_clickevent = new ArrayList<>();
        this.handler_finished = new ArrayList<>();
        this.handler_otherevent = new ArrayList<>();
        
        root = new Group();
        root.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                clicked();
                System.out.println("root has clicked");
            }
        });
        
        id = "";
        
        this.setDefaultSettings();
        animation_list = new ArrayList<>();
    }
    public void generateParameterEditor(){
        this.parameter_editor = new VBox();
        this.idLabel = new Text("ID");
        this.parameter_editor.getChildren().add(idLabel);
        this.id_editor = new TextField(id);
        this.parameter_editor.getChildren().add(id_editor);
        this.opacityLabel = new Text("Opacity Level");
        this.parameter_editor.getChildren().add(opacityLabel);
        this.opacityLevelSlider = new Slider(0,1,1);opacityLevelSlider.setValue(opacity);
        this.parameter_editor.getChildren().add(opacityLevelSlider);
        this.opacityValue = new Label(this.opacity+"");
        this.parameter_editor.getChildren().add(opacityValue);
        this.colorChangerLabel = new Label("Color Changer");
        this.parameter_editor.getChildren().add(colorChangerLabel);
        this.red = new HBox();
        this.green = new HBox();
        this.blue = new HBox();
        this.rLabel = new Text("R");
        this.gLabel = new Text("G");
        this.bLabel = new Text("B");
        this.rSlider = new Slider(0,255,0);rSlider.setValue(color_red);
        this.gSlider = new Slider(0,255,0);gSlider.setValue(color_green);
        this.bSlider = new Slider(0,255,0);bSlider.setValue(color_blue);
        this.rValue = new Text(this.color_red+"");
        this.gValue = new Text(this.color_green+"");
        this.bValue = new Text(this.color_blue+"");
        this.red.getChildren().addAll(rLabel,rSlider,rValue);
        this.green.getChildren().addAll(gLabel,gSlider,gValue);
        this.blue.getChildren().addAll(bLabel,bSlider,bValue);
        this.parameter_editor.getChildren().add(red);
        this.parameter_editor.getChildren().add(green);
        this.parameter_editor.getChildren().add(blue);
        this.cMonitor = new Rectangle();
        this.cMonitor.setWidth(40);
        this.cMonitor.setHeight(40);
        this.cMonitor.setArcWidth(20);
        this.cMonitor.setArcHeight(20);
        cMonitor.setFill(new Color(rSlider.getValue()/255, gSlider.getValue()/255, bSlider.getValue()/255,opacityLevelSlider.getValue()));
        this.parameter_editor.getChildren().add(cMonitor);
        this.widthLabel = new Label("Width");
        this.width = new TextField(this.width_scale+"");
        this.parameter_editor.getChildren().add(widthLabel);
        this.parameter_editor.getChildren().add(width);
        this.heightLabel = new Label("Height");
        this.height = new TextField(this.height_scale+"");
        this.parameter_editor.getChildren().add(heightLabel);
        this.parameter_editor.getChildren().add(height);
        this.rotateXLabel = new Label("Rotate_X");
        this.rotateXSlider = new Slider(0,360,0);rotateXSlider.setValue(rotateX);
        this.rotateXValue = new Text(this.rotateX+"");
        this.rotateYLabel = new Label("Rotate_Y");
        this.rotateYSlider = new Slider(0,360,0);rotateYSlider.setValue(rotateY);
        this.rotateYValue = new Text(this.rotateY+"");
        this.rotateZLabel = new Label("Rotate_Z");
        this.rotateZSlider = new Slider(0,360,0);rotateZSlider.setValue(rotateZ);
        this.rotateZValue = new Text(this.rotateZ+"");
        this.parameter_editor.getChildren().addAll(rotateXLabel,rotateXSlider,rotateXValue);
        this.parameter_editor.getChildren().addAll(rotateYLabel,rotateYSlider,rotateYValue);
        this.parameter_editor.getChildren().addAll(rotateZLabel,rotateZSlider,rotateZValue);
        this.transXLabel = new Label("Translation X");
        this.transYLabel = new Label("Translation Y");
        this.transZLabel = new Label("Translation Z");
        this.transX = new TextField(this.translation_x+"");
        this.transY = new TextField(this.translation_y+"");
        this.transZ = new TextField(this.translation_z+"");
        this.parameter_editor.getChildren().addAll(transXLabel,transX,transYLabel,transY,transZLabel,transZ);
        

        opacityLevelSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                    //cappuccino.setOpacity(new_val.doubleValue());
                    opacityValue.setText(String.format("%.2f", new_val));
                    cMonitor.setFill(new Color(rSlider.getValue()/255, gSlider.getValue()/255, bSlider.getValue()/255,opacityLevelSlider.getValue()));
            }
        });
        ChangeListener<Number> colorChangeListener = new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                rValue.setText(""+(int)rSlider.getValue());
                gValue.setText(""+(int)gSlider.getValue());
                bValue.setText(""+(int)bSlider.getValue());
                cMonitor.setFill(new Color(rSlider.getValue()/255, gSlider.getValue()/255, bSlider.getValue()/255,opacityLevelSlider.getValue()));
            }
        };
        rSlider.valueProperty().addListener(colorChangeListener);
        gSlider.valueProperty().addListener(colorChangeListener);
        bSlider.valueProperty().addListener(colorChangeListener);
        
        ChangeListener<Number> rotateSliderListener = new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                rotateXValue.setText(""+(int)rotateXSlider.getValue());
                rotateX = (int)rotateXSlider.getValue();
                rotateYValue.setText(""+(int)rotateYSlider.getValue());
                rotateY = (int)rotateYSlider.getValue();
                rotateZValue.setText(""+(int)rotateZSlider.getValue());
                rotateZ = (int)rotateZSlider.getValue();
            }
        };
        rotateXSlider.valueProperty().addListener(rotateSliderListener);
        rotateYSlider.valueProperty().addListener(rotateSliderListener);
        rotateZSlider.valueProperty().addListener(rotateSliderListener);

    }
                
    public VBox getParameterEditor(){
        return this.parameter_editor;
    }
    public void generateComponent() {
        this.id = id_editor.getText();
        this.translation_x = Float.parseFloat(this.transX.getText());
        this.translation_y = Float.parseFloat(this.transY.getText());
        this.translation_z = Float.parseFloat(this.transZ.getText());
        this.root.setTranslateX(this.translation_x);
        this.root.setTranslateY(this.translation_y);
        this.root.setTranslateZ(this.translation_z);
        Rotate rotatex = new Rotate(this.rotateX,Rotate.X_AXIS);
        Rotate rotatey = new Rotate(this.rotateY,Rotate.Y_AXIS);
        Rotate rotatez = new Rotate(this.rotateZ,Rotate.Z_AXIS);
        this.root.getTransforms().addAll(rotatex,rotatey,rotatez);
        System.out.println(this.translation_z);
        this.color_red = Integer.parseInt(this.rValue.getText());
        this.color_green = Integer.parseInt(this.gValue.getText());
        this.color_blue = Integer.parseInt(this.bValue.getText());
        this.width_scale = Float.parseFloat(this.width.getText());
        this.height_scale = Float.parseFloat(this.height.getText());
        
    }
    public void generateComponentFromXml(){
        this.root.setTranslateX(this.translation_x);
        this.root.setTranslateY(this.translation_y);
        this.root.setTranslateZ(this.translation_z);
        Rotate rotatex = new Rotate(this.rotateX,Rotate.X_AXIS);
        Rotate rotatey = new Rotate(this.rotateY,Rotate.Y_AXIS);
        Rotate rotatez = new Rotate(this.rotateZ,Rotate.Z_AXIS);
        this.root.getTransforms().addAll(rotatex,rotatey,rotatez);
    }
    public Group getRoot(){
        return root;
    }
    public void regenerateComponent(){
        this.id = id_editor.getText();
        this.translation_x = Float.parseFloat(this.transX.getText());
        this.translation_y = Float.parseFloat(this.transY.getText());
        this.translation_z = Float.parseFloat(this.transZ.getText());
        this.root.setTranslateX(this.translation_x);
        this.root.setTranslateY(this.translation_y);
        this.root.setTranslateZ(this.translation_z);
        Rotate rotatex = new Rotate(this.rotateX,Rotate.X_AXIS);
        Rotate rotatey = new Rotate(this.rotateY,Rotate.Y_AXIS);
        Rotate rotatez = new Rotate(this.rotateZ,Rotate.Z_AXIS);
        this.root.getTransforms().addAll(rotatex,rotatey,rotatez);
        System.out.println(this.translation_z);
        this.color_red = Integer.parseInt(this.rValue.getText());
        this.color_green = Integer.parseInt(this.gValue.getText());
        this.color_blue = Integer.parseInt(this.bValue.getText());
        this.width_scale = Float.parseFloat(this.width.getText());
        this.height_scale = Float.parseFloat(this.height.getText());
    }
     /**
     * @return the translation_x
     */
    public float getTranslation_x() {
        return translation_x;
    }

    /**
     * @param translation_x the translation_x to set
     */
    public void setTranslation_x(float translation_x) {
        this.translation_x = translation_x;
    }

    /**
     * @return the translation_y
     */
    public float getTranslation_y() {
        return translation_y;
    }

    /**
     * @param translation_y the translation_y to set
     */
    public void setTranslation_y(float translation_y) {
        this.translation_y = translation_y;
    }

    /**
     * @return the translation_z
     */
    public float getTranslation_z() {
        return translation_z;
    }

    /**
     * @param translation_z the translation_z to set
     */
    public void setTranslation_z(float translation_z) {
        this.translation_z = translation_z;
    }

    /**
     * @return the color_red
     */
    public int getColor_red() {
        return color_red;
    }

    /**
     * @param color_red the color_red to set
     */
    public void setColor_red(int color_red) {
        this.color_red = color_red;
    }

    /**
     * @return the color_green
     */
    public int getColor_green() {
        return color_green;
    }

    /**
     * @param color_green the color_green to set
     */
    public void setColor_green(int color_green) {
        this.color_green = color_green;
    }

    /**
     * @return the color_blue
     */
    public int getColor_blue() {
        return color_blue;
    }

    /**
     * @param color_blue the color_blue to set
     */
    public void setColor_blue(int color_blue) {
        this.color_blue = color_blue;
    }

    /**
     * @return the opacity
     */
    public float getOpacity() {
        return opacity;
    }

    /**
     * @param opacity the opacity to set
     */
    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    @Override
    public void loaded() {
        AnimationEvent event = new AnimationEvent();
        event.setSource(this);
        event.setStatus(AnimationEvent.LOADED);
        for(int i = 0;i<this.handler_loadevent.size();i++){
            handler_loadevent.get(i).Handle(event);
        }
    }

    @Override
    public void clicked() {
        AnimationEvent event = new AnimationEvent();
        event.setSource(this);
        event.setStatus(AnimationEvent.CLICKED);
        for(int i = 0;i<this.handler_clickevent.size();i++){
            handler_clickevent.get(i).Handle(event);
        }
    }

    @Override
    public void finished() {
        AnimationEvent event = new AnimationEvent();
        event.setSource(this);
        event.setStatus(AnimationEvent.FINISHED);
        for(int i = 0;i<this.handler_finished.size();i++){
            handler_finished.get(i).Handle(event);
        }
    }

    @Override
    public void otherActionHasOccored() {
        AnimationEvent event = new AnimationEvent();
        event.setSource(this);
        event.setStatus(AnimationEvent.OTHER);
        for(int i = 0;i<this.handler_otherevent.size();i++){
            handler_otherevent.get(i).Handle(event);
        }
    }

    @Override
    public void setOnLoaded(AnimationEventHandler handler) {
        this.handler_loadevent.add(handler);
    }

    @Override
    public void setOnClicked(AnimationEventHandler handler) {
        this.handler_clickevent.add(handler);
    }

    @Override
    public void setOnFinished(AnimationEventHandler handler) {
        this.handler_finished.add(handler);
    }

    @Override
    public void setOnOtherActionOccored(AnimationEventHandler handler) {
        this.handler_otherevent.add(handler);
    }

    private void setDefaultSettings() {
        translation_x = 0;
        translation_y = 0;
        translation_z = 0;
        color_red = 0;
        color_green = 0;
        color_blue = 0;
        opacity = 1.0f;
        rotateX = 0;
        rotateY = 0;
        rotateZ = 0;
        
        this.width_scale = 100;
        this.height_scale = 100;
    }
    public Group getFrameForComponent(int type){
        Group frame = new Group();
        Line upper = new Line(0,0,width_scale,0);upper.setStrokeDashOffset(10);
        Line bottom = new Line(0,height_scale,width_scale,height_scale);bottom.setStrokeDashOffset(10);
        Line leftside = new Line(0,0,0,height_scale);leftside.setStrokeDashOffset(10);
        Line rightside = new Line(width_scale,0,width_scale,height_scale);rightside.setStrokeDashOffset(10);
        Paint paint = Color.BLUE;
        if(type == FRAME_TYPE_MAIN_SELECTION){
            paint = Color.BLACK;
        }
        else if(type == FRAME_TYPE_SUB_SELECTION){
            paint = Color.RED;
        }
        upper.setStroke(paint);
        bottom.setStroke(paint);
        rightside.setStroke(paint);
        leftside.setStroke(paint);
        frame.getChildren().addAll(upper,bottom,leftside,rightside);
        frame.setTranslateX(translation_x);
        frame.setTranslateY(translation_y);
        frame.setTranslateZ(translation_z);
        return frame;
    }

    @Override
    public String getAnimableId() {
        return this.id;
    }

    @Override
    public ArrayList<Animation> getAnimationList() {
        return animation_list;
    }
    public Element createXmlElement(Document doc, Element fxPartComponentElement){
        Element idElement = doc.createElement("Id");
        Node idNode = doc.createTextNode(id);
        idElement.appendChild(idNode);
        fxPartComponentElement.appendChild(idElement);
        
        Element classElement = doc.createElement("ClassType");
        Node classNode = doc.createTextNode(this.getClass().toString());
        classElement.appendChild(classNode);
        fxPartComponentElement.appendChild(classElement);
        
        Element translationXElement = doc.createElement("TranslationX");
        Node translationXNode = doc.createTextNode(translation_x+"");
        translationXElement.appendChild(translationXNode);
        fxPartComponentElement.appendChild(translationXElement);
        
        Element translationYElement = doc.createElement("TranslationY");
        Node translationYNode = doc.createTextNode(translation_y+"");
        translationYElement.appendChild(translationYNode);
        fxPartComponentElement.appendChild(translationYElement);
        
        Element translationZElement = doc.createElement("TranslationZ");
        Node translationZNode = doc.createTextNode(translation_z+"");
        translationZElement.appendChild(translationZNode);
        fxPartComponentElement.appendChild(translationZElement);
        
        Element rotationXElement = doc.createElement("RotationX");
        Node rotationXNode = doc.createTextNode(rotateX+"");
        rotationXElement.appendChild(rotationXNode);
        fxPartComponentElement.appendChild(rotationXElement);
        
        Element rotationYElement = doc.createElement("RotationY");
        Node rotationYNode = doc.createTextNode(rotateY+"");
        rotationYElement.appendChild(rotationYNode);
        fxPartComponentElement.appendChild(rotationYElement);
        
        Element rotationZElement = doc.createElement("RotationZ");
        Node rotationZNode = doc.createTextNode(rotateZ+"");
        rotationZElement.appendChild(rotationZNode);
        fxPartComponentElement.appendChild(rotationZElement);
        
        Element colorRedElement = doc.createElement("ColorRed");
        Node colorRedNode = doc.createTextNode(color_red+"");
        colorRedElement.appendChild(colorRedNode);
        fxPartComponentElement.appendChild(colorRedElement);
        
        Element colorGreenElement = doc.createElement("ColorGreen");
        Node colorGreenNode = doc.createTextNode(color_green+"");
        colorGreenElement.appendChild(colorGreenNode);
        fxPartComponentElement.appendChild(colorGreenElement);
        
        Element colorBlueElement = doc.createElement("ColorBlue");
        Node colorBlueNode = doc.createTextNode(color_blue+"");
        colorBlueElement.appendChild(colorBlueNode);
        fxPartComponentElement.appendChild(colorBlueElement);
        
        Element opacityElement = doc.createElement("Opacity");
        Node opacityNode = doc.createTextNode(opacity+"");
        opacityElement.appendChild(opacityNode);
        fxPartComponentElement.appendChild(opacityElement);
        
        Element widthElement = doc.createElement("Width");
        Node widthNode = doc.createTextNode(width_scale+"");
        widthElement.appendChild(widthNode);
        fxPartComponentElement.appendChild(widthElement);
        
        Element heightElement = doc.createElement("Height");
        Node heightNode = doc.createTextNode(height_scale+"");
        heightElement.appendChild(heightNode);
        fxPartComponentElement.appendChild(heightElement);
        
        this.createDetailXmlElement(doc, fxPartComponentElement);
        
        Element animationListElement = doc.createElement("AnimationList");
        for(int i = 0;i<this.animation_list.size();i++){
            Element animationElement = animation_list.get(i).createXmlElement(doc,doc.createElement("Animation"));
            animationListElement.appendChild(animationElement);
        }
        fxPartComponentElement.appendChild(animationListElement);
        
        return fxPartComponentElement;
    }
    public void createDetailXmlElement(Document doc,Element fxPartComponentElement){
        
    }
    static FxPartComponent createComponent(Node item) {
        FxPartComponent component = null;
        System.out.println(item.getTextContent());
        Element componentElement = (Element) item;
        for(int i = 0;i<componentElement.getChildNodes().getLength();i++){
            Element element = (Element) componentElement.getChildNodes().item(i);
//            System.out.println(element.getNodeName());
//            System.out.println(element.getChildNodes().item(0).getNodeValue());
            if(element.getTagName().equalsIgnoreCase("ClassType")){
                Node classNode = element.getChildNodes().item(0);
                component = FxPartComponent.createComponentOfClass(classNode.getTextContent());
            }
        }
        for(int i = 0;i<componentElement.getChildNodes().getLength();i++){
            Element element = (Element)componentElement.getChildNodes().item(i);
            System.out.println(element.getTagName());
            if(element.getTagName().equalsIgnoreCase("Id")){
                Node idNode = element.getChildNodes().item(0);
                component.id = idNode.getTextContent();
            }
            else if(element.getTagName().equalsIgnoreCase("TranslationX")){
                Node idNode = element.getChildNodes().item(0);
                if(component == null){
                    System.out.println("NULLだよ");
                }
                component.translation_x = Float.parseFloat(idNode.getTextContent());
            }
            else if(element.getTagName().equalsIgnoreCase("TranslationY")){
                Node idNode = element.getChildNodes().item(0);
                component.translation_y = Float.parseFloat(idNode.getTextContent());
            }
            else if(element.getTagName().equalsIgnoreCase("TranslationZ")){
                Node idNode = element.getChildNodes().item(0);
                component.translation_z = Float.parseFloat(idNode.getTextContent());
            }
            else if(element.getTagName().equalsIgnoreCase("ColorRed")){
                Node idNode = element.getChildNodes().item(0);
                component.color_red = Integer.parseInt(idNode.getTextContent());
            }
            else if(element.getTagName().equalsIgnoreCase("ColorGreen")){
                Node idNode = element.getChildNodes().item(0);
                component.color_green = Integer.parseInt(idNode.getTextContent());
            }
            else if(element.getTagName().equalsIgnoreCase("ColorBlue")){
                Node idNode = element.getChildNodes().item(0);
                component.color_blue = Integer.parseInt(idNode.getTextContent());
            }
            else if(element.getTagName().equalsIgnoreCase("Opacity")){
                Node idNode = element.getChildNodes().item(0);
                component.opacity = Float.parseFloat(idNode.getTextContent());
            }
            else if(element.getTagName().equalsIgnoreCase("RotateX")){
                Node idNode = element.getChildNodes().item(0);
                component.rotateX = Integer.parseInt(idNode.getTextContent());
            }
            else if(element.getTagName().equalsIgnoreCase("RotateY")){
                Node idNode = element.getChildNodes().item(0);
                component.rotateY = Integer.parseInt(idNode.getTextContent());
            }
            else if(element.getTagName().equalsIgnoreCase("RotateZ")){
                Node idNode = element.getChildNodes().item(0);
                component.rotateZ = Integer.parseInt(idNode.getTextContent());
            }
            else if(element.getTagName().equalsIgnoreCase("Width")){
                Node idNode = element.getChildNodes().item(0);
                component.width_scale = Float.parseFloat(idNode.getTextContent());
            }
            else if(element.getTagName().equalsIgnoreCase("Height")){
                Node idNode = element.getChildNodes().item(0);
                component.height_scale = Float.parseFloat(idNode.getTextContent());
            }
            else if(element.getTagName().equalsIgnoreCase("ClassType")){
            
            }
            else if(element.getTagName().equalsIgnoreCase("AnimationList")){
                for(int j = 0;j<element.getChildNodes().getLength();j++){
                    Animation animation = new Animation();
                    for(int k = 0;k<element.getChildNodes().item(j).getChildNodes().getLength();k++){
                        animation.createParameterFromXmlElement((Element)(element.getChildNodes().item(j).getChildNodes().item(k)));
                        
                    }
                    component.animation_list.add(animation);
                }
            }
            else{
                component.createParameter(element);
            }
        }
        return component;
    }
    private static FxPartComponent createComponentOfClass(String textContent) {
        FxPartComponent component = null;
        if(textContent.equalsIgnoreCase(FpLabel.class.toString())){
            component = new FpLabel();
            System.out.println("label");
        }
        else if(textContent.equalsIgnoreCase(FpTable.class.toString())){
            component = new FpTable();
        }
        else if(textContent.equalsIgnoreCase(FpCircle.class.toString())){
            component = new FpCircle();
        }
        else if(textContent.equalsIgnoreCase(FpLine.class.toString())){
            component = new FpLine();
        }
        else if(textContent.equalsIgnoreCase(FpImage.class.toString())){
            component = new FpImage();
        }
        else if(textContent.equalsIgnoreCase(FpMoviePlayer.class.toString())){
            component = new FpMoviePlayer();
        }
        else if(textContent.equalsIgnoreCase(FpPdfPagePanel.class.toString())){
            component = new FpPdfPagePanel();
        }
        else if(textContent.equalsIgnoreCase(FpPolygon.class.toString())){
            component = new FpPolygon();
        }
        else if(textContent.equalsIgnoreCase(FpGraph.class.toString())){
            component = new FpGraph();
        }
        else if(textContent.equalsIgnoreCase(FpBrowser.class.toString())){
            component = new FpBrowser();
        }
        return component;
    }

    public void createParameter(Element element) {
        
    }

    public float getWidth_scale() {
        return width_scale;
    }

    public float getHeight_scale() {
        return height_scale;
    }

    public void setWidth_scale(float width_scale) {
        this.width_scale = width_scale;
    }

    public void setHeight_scale(float height_scale) {
        this.height_scale = height_scale;
    }
    public void setWidthText(String width){
        this.width.setText(width);
    }
    public void setHeightText(String height){
        this.height.setText(height+"");
    }
    public void clearListener() {
        this.handler_clickevent.clear();
        this.handler_finished.clear();
        this.handler_loadevent.clear();
        this.handler_otherevent.clear();
    }
}
