/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FxPartComponent;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author NPC
 */
public class FpMoviePlayer extends FxPartComponent{
    File file;
    MediaView mediaview;
    MediaPlayer player;
    private boolean playing;
    Button open_button;
    CheckBox use_as_video_size;
    private String url;
    
    public FpMoviePlayer(){
        super();
        this.id = "FpMoviePlayer";
        playing = false;
    }
    
    @Override
    public void generateParameterEditor() {
        super.generateParameterEditor();
        this.open_button = new Button();
        this.use_as_video_size = new CheckBox();
        open_button.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent t) {
                FileChooser chooser = new FileChooser();
                chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP4", "*.mp4"));
                chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("AVI", "*.avi"));
                file = chooser.showOpenDialog(null);
            }
        });
        super.parameter_editor.getChildren().addAll(open_button,use_as_video_size);
    }

    @Override
    public void generateComponent() {
        super.generateComponent();
        Media media = new Media("file:///"+file.getAbsolutePath());
        player = new MediaPlayer(media);
        mediaview = new MediaView(player);
        if(use_as_video_size.isSelected()){
            mediaview.setFitWidth(media.getWidth());
            mediaview.setFitHeight(media.getHeight());
            setWidth_scale((int)mediaview.getFitWidth());
            setHeight_scale((int)mediaview.getFitHeight());
        }
        else{
            mediaview.setFitWidth(getWidth_scale());
            mediaview.setFitHeight(getHeight_scale());
        }
        player.setOnEndOfMedia(new Runnable(){

            @Override
            public void run() {
                finished();
            }
        });
        root.getChildren().add(mediaview);
    }
    public void operation(){
        if(this.playing == true){
            this.loaded();
            System.out.println("movie paused");
            player.pause();
            playing = false;
        }
        else if(this.playing == false){
            player.play();
            System.out.println("movie played");
            playing = true;
        }
    }

    @Override
    public void regenerateComponent() {
        super.regenerateComponent();
        root.getChildren().clear();
        Media media = new Media("file:///"+file.getAbsolutePath());
        player = new MediaPlayer(media);
        mediaview = new MediaView(player);
        if(use_as_video_size.isSelected()){
            mediaview.setFitWidth(media.getWidth());
            mediaview.setFitHeight(media.getHeight());
        }
        else{
            mediaview.setFitWidth(getWidth_scale());
            mediaview.setFitHeight(getHeight_scale());
        }
        player.setOnEndOfMedia(new Runnable(){

            @Override
            public void run() {
                finished();
            }
        });

        root.getChildren().add(mediaview);
    }

    @Override
    public void generateComponentFromXml() {
        super.generateComponentFromXml();
        //Media media = new Media("file:///"+file.getAbsolutePath());
        Media media = new Media(url);
        player = new MediaPlayer(media);
        mediaview = new MediaView(player);
            mediaview.setFitWidth(getWidth_scale());
            mediaview.setFitHeight(getHeight_scale());
        player.setOnEndOfMedia(new Runnable(){

            @Override
            public void run() {
                finished();
            }
        });
        root.getChildren().add(mediaview);
    }

    @Override
    public void createDetailXmlElement(Document doc, Element fxPartComponentElement) {
        super.createDetailXmlElement(doc, fxPartComponentElement);
        Element element = doc.createElement("File");
        Node filenode = doc.createTextNode(this.file.getAbsolutePath());
        element.appendChild(filenode);
        fxPartComponentElement.appendChild(element);
        
    }

    @Override
    public void createParameter(Element element) {
        super.createParameter(element);
        if(element.getTagName().equalsIgnoreCase("File")){
            Node node = element.getChildNodes().item(0);
            this.file = new File(node.getTextContent());
            this.url = node.getTextContent();
        }
    }
}
