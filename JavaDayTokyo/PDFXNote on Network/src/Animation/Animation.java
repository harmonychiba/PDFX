/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Animation;

import FxPartComponent.FpGraph;
import FxPartComponent.FpMoviePlayer;
import FxPartComponent.FxPartComponent;
import FxPartComponent.Page;
import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.util.Duration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author NPC
 */
public class Animation implements Animable{
    public String id;
    private String object_item;
    private String cause_item;
    private int Trigger;
    private int Animation_type;
    private long timer;
    private boolean autorepeat;
    private ArrayList<AnimationEventHandler> loaded_handler;
    private ArrayList<AnimationEventHandler> clicked_handler;
    private ArrayList<AnimationEventHandler> finished_handler;
    private ArrayList<AnimationEventHandler> other_handler;
    private Animable parent;
    
    public static String[] ANIMATION_TYPE_LIST = {"BACK_PAGE","SEND_PAGE","PLAY","PAUSE","TRANSLATION_X","TRANSLATION_Y","TRANSLATION_Z","ROTATION_X","ROTATION_Y","ROTATION_Z","SCALE_WIDTH","SCALE_HEIGHT","OPACITY","CHART_SEND","CHART_BACK"};
    private double propertyValue;

    public Animation(){
        this.loaded_handler = new ArrayList<>();
        this.clicked_handler = new ArrayList<>();
        this.finished_handler = new ArrayList<>();
        this.other_handler = new ArrayList<>();
    }
    public long getTimer() {
        return timer;
    }
    public void setTimer(long timer) {
        this.timer = timer;
    }
    public String getObject_item() {
        return object_item;
    }

    public void setObject_item(String object_item) {
        this.object_item = object_item;
    }

    public String getCause_item() {
        return cause_item;
    }

    public void setCause_item(String cause_item) {
        this.cause_item = cause_item;
    }

    public int getTrigger() {
        return Trigger;
    }

    public void setTrigger(int Trigger) {
        this.Trigger = Trigger;
    }

    public int getAnimation_type() {
        return Animation_type;
    }

    public void setAnimation_type(int Animation_type) {
        this.Animation_type = Animation_type;
    }
    public void play(){
        this.loaded();
    }

    public boolean isAutorepeat() {
        return autorepeat;
    }

    public void setAutorepeat(boolean autorepeat) {
        this.autorepeat = autorepeat;
    }

    @Override
    public void loaded() {
        AnimationEvent event = new AnimationEvent();
        event.setSource(this);
        event.setStatus(AnimationEvent.LOADED);
        for(int i = 0;i<loaded_handler.size();i++){
            loaded_handler.get(i).Handle(event);
        }
    }

    @Override
    public void clicked() {
        AnimationEvent event = new AnimationEvent();
        event.setSource(this);
        event.setStatus(AnimationEvent.CLICKED);
        for(int i = 0;i<clicked_handler.size();i++){
            clicked_handler.get(i).Handle(event);
        }
    }

    @Override
    public void finished() {
        AnimationEvent event = new AnimationEvent();
        event.setSource(this);
        event.setStatus(AnimationEvent.FINISHED);
        for(int i = 0;i<finished_handler.size();i++){
            finished_handler.get(i).Handle(event);
        }
    }

    @Override
    public void otherActionHasOccored() {
        AnimationEvent event = new AnimationEvent();
        event.setSource(this);
        event.setStatus(AnimationEvent.OTHER);
        for(int i = 0;i<other_handler.size();i++){
            other_handler.get(i).Handle(event);
        }
    }

    @Override
    public void setOnLoaded(AnimationEventHandler handler) {
        this.loaded_handler.add(handler);
    }

    @Override
    public void setOnClicked(AnimationEventHandler handler) {
        this.clicked_handler.add(handler);
    }

    @Override
    public void setOnFinished(AnimationEventHandler handler) {
        this.finished_handler.add(handler);
    }

    @Override
    public void setOnOtherActionOccored(AnimationEventHandler handler) {
        this.other_handler.add(handler);
    }
    @Override
    public String getAnimableId(){
        return this.id;
    }

    @Override
    public ArrayList<Animation> getAnimationList() {
        return null;
    }

    public void setValue(double value) {
        this.propertyValue = value;
    }

    public double getValue() {
        return this.propertyValue;
    }
    public Animable getParent() {
        return parent;
    }

    public void setParent(Animable parent) {
        this.parent = parent;
    }

    public Element createXmlElement(Document doc, Element animationElement) {
        Element idElement = doc.createElement("Id");
        Node idNode = doc.createTextNode(id);
        idElement.appendChild(idNode);
        animationElement.appendChild(idElement);
        
        Element objectIdElement = doc.createElement("ObjectItemId");
        Node objectIdNode = doc.createTextNode(this.object_item);
        objectIdElement.appendChild(objectIdNode);
        animationElement.appendChild(objectIdElement);
        
        Element causeIdElement = doc.createElement("CauseItemId");
        Node causeIdNode = doc.createTextNode(cause_item);
        causeIdElement.appendChild(causeIdNode);
        animationElement.appendChild(causeIdElement);
        
        Element triggerTypeElement = doc.createElement("TriggerType");
        Node triggerTypeNode = doc.createTextNode(this.Trigger+"");
        triggerTypeElement.appendChild(triggerTypeNode);
        animationElement.appendChild(triggerTypeElement);
        
        Element animationTypeElement = doc.createElement("AnimationType");
        Node animationTypeNode = doc.createTextNode(this.Animation_type+"");
        animationTypeElement.appendChild(animationTypeNode);
        animationElement.appendChild(animationTypeElement);
        
        Element propertyValueElement = doc.createElement("PropertyValue");
        Node propertyValueNode = doc.createTextNode(this.propertyValue+"");
        propertyValueElement.appendChild(propertyValueNode);
        animationElement.appendChild(propertyValueElement);
        
        Element timerValueElement = doc.createElement("Timer");
        Node timerValueNode = doc.createTextNode(this.timer+"");
        timerValueElement.appendChild(timerValueNode);
        animationElement.appendChild(timerValueElement);
        
        Element autoRepeatValueElement = doc.createElement("AutoRepeat");
        Node autoRepeatValueNode = doc.createTextNode(this.autorepeat+"");
        autoRepeatValueElement.appendChild(autoRepeatValueNode);
        animationElement.appendChild(autoRepeatValueElement);
        
        return animationElement;
    }
    public void createParameterFromXmlElement(Element element){
        if(element.getTagName().equalsIgnoreCase("Id")){
            Node node = element.getChildNodes().item(0);
            this.id = node.getTextContent();
        }
        else if(element.getTagName().equalsIgnoreCase("ObjectItemId")){
            Node node = element.getChildNodes().item(0);
            this.object_item = node.getTextContent();
        }
        else if(element.getTagName().equalsIgnoreCase("CauseItemId")){
            Node node = element.getChildNodes().item(0);
            this.cause_item = node.getTextContent();
        }
        else if(element.getTagName().equalsIgnoreCase("TriggerType")){
            Node node = element.getChildNodes().item(0);
            this.Trigger = Integer.parseInt(node.getTextContent());
        }
        else if(element.getTagName().equalsIgnoreCase("AnimationType")){
            Node node = element.getChildNodes().item(0);
            this.Animation_type = Integer.parseInt(node.getTextContent());
        }
        else if(element.getTagName().equalsIgnoreCase("PropertyValue")){
            Node node = element.getChildNodes().item(0);
            this.propertyValue = Double.parseDouble(node.getTextContent());
        }
        else if(element.getTagName().equalsIgnoreCase("Timer")){
            Node node = element.getChildNodes().item(0);
            this.timer = Integer.parseInt(node.getTextContent());
        }
        else if(element.getTagName().equalsIgnoreCase("AutoRepeat")){
            Node node = element.getChildNodes().item(0);
            this.autorepeat = Boolean.parseBoolean(node.getTextContent());
        }
    }

    public void play(Animable object_item) {
        Group object_group = null;
        if(object_item == null)return;
        if(object_item.getClass().equals(FpMoviePlayer.class)){
            if(this.getAnimation_type() == 2){
                FpMoviePlayer player = (FpMoviePlayer) object_item;
                player.operation();
                return;
            }
            else if(this.getAnimation_type() == 3){
                FpMoviePlayer player = (FpMoviePlayer) object_item;
                player.operation();
                return;
            }
        }
        if(object_item.getClass().equals(FpGraph.class)){
            if(this.getAnimation_type() == 13){
                FpGraph graph = (FpGraph) object_item;
                graph.nextChart();
                return;
            }
            else if(this.getAnimation_type() == 14){
                FpGraph graph = (FpGraph) object_item;
                graph.prevChart();
                return;
            }
        }
        if(object_item.getClass().equals(Page.class)){
            Page object = (Page)object_item;
            object_group = object;
        }
        else if(object_item.getClass().getSuperclass().equals(FxPartComponent.class)){
            FxPartComponent object = (FxPartComponent)object_item;
            object_group = object.getRoot();
        }
        Timeline animation = new Timeline();
        KeyValue value = null;
        if(this.getAnimation_type() == 4){
            value = new KeyValue(object_group.translateXProperty(),this.propertyValue);
        }
        else if(this.getAnimation_type() == 5){
            value = new KeyValue(object_group.translateYProperty(),this.propertyValue);
        }
        else if(this.getAnimation_type() == 6){
            value = new KeyValue(object_group.translateZProperty(),this.propertyValue);
        }
        if(this.getAnimation_type() == 7){
            object_group.setRotationAxis(new Point3D(1,0,0));
            value = new KeyValue(object_group.rotateProperty(),this.propertyValue);
        }
        else if(this.getAnimation_type() == 8){
            object_group.setRotationAxis(new Point3D(0,1,0));
            value = new KeyValue(object_group.rotateProperty(),this.propertyValue);
        }
        else if(this.getAnimation_type() == 9){
            object_group.setRotationAxis(new Point3D(0,0,1));
            value = new KeyValue(object_group.rotateProperty(),this.propertyValue);
        }
        else if(this.getAnimation_type() == 11){
            value = new KeyValue(object_group.scaleXProperty(),this.propertyValue);
        }
        else if(this.getAnimation_type() == 10){
            value = new KeyValue(object_group.scaleYProperty(),this.propertyValue);
        }
        else if(this.getAnimation_type() == 12){
            value = new KeyValue(object_group.opacityProperty(),this.propertyValue);
        }
        if(autorepeat == true){
            animation.setCycleCount(Timeline.INDEFINITE);
        }
        animation.getKeyFrames().add(new KeyFrame(Duration.millis(this.timer),value));
        animation.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                finished();
            }
        });
        animation.play();
        this.loaded();
    }
    public void clearListener() {
        this.clicked_handler.clear();
        this.loaded_handler.clear();
        this.finished_handler.clear();
        this.other_handler.clear();
    }
}
