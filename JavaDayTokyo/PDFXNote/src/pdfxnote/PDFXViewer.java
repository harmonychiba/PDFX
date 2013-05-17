/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pdfxnote;

import Animation.Animable;
import Animation.Animation;
import Animation.AnimationEvent;
import Animation.AnimationEventHandler;
import FxPartComponent.FxPartComponent;
import FxPartComponent.Page;
import imageService.ImageSender;
import imageService.Projector;
import java.awt.AWTException;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javax.imageio.ImageIO;
import jp.co.ricoh.pjs.pcscreen.RNCBHeader;
import org.jgroups.JChannel;
import org.jgroups.Message;

/**
 *
 * @author NPC
 */
public class PDFXViewer implements RequestHandler {

    ArrayList<Page> pdfx_pages;
    Stage viewerStage;
    Scene viewerScene;
    StackPane viewerPane;
    int viewingPage;
    private RNCBHeader header;
    AnimationEventHandler animation_listener;
    private Timeline thread;
    BufferedImage screen_capture;
    JChannel channel;
    private boolean listenerSet;
    private final Projector projector;
    private final ImageSender sender;
    
    private ImageView view;
    private BufferedImage resultimage;

    PDFXViewer() {
        this.header = new RNCBHeader();
        projector = new Projector();
        sender = new ImageSender();
        projector.setSender(sender);

    }

    public void setPdfxPages(ArrayList<Page> pages) {
        this.pdfx_pages = pages;
        this.setUpListener();
    }

    public void viewFirstPage() {
        //this.setUpListener();
        viewerStage.show();
        viewerPane.getChildren().clear();
        pdfx_pages.get(viewingPage).prepareForImage();
        viewerPane.getChildren().add(pdfx_pages.get(viewingPage));
        viewerPane.getChildren().add(view);
        System.out.println("load page start");
        pdfx_pages.get(viewingPage).loaded();
        System.out.println("load page");
        projector.pleaseIp();
        String result;
        try {
            result = projector.postCreateMethod(false, 0);
            System.out.println(result);
        } catch (InterruptedException ex) {
            Logger.getLogger(PDFXViewer.class.getName()).log(Level.SEVERE, null, ex);
        }

        thread.play();
    }

    public void AnimationEventHandled(String causeId, int trigger) {
        ArrayList<Animation> occuredAnimations = this.searchAnimation(causeId, trigger);
        for (int i = 0; i < occuredAnimations.size(); i++) {
            Animation anim_handled = occuredAnimations.get(i);
            Animable object_item = this.searchAnimableFromObjectId(anim_handled.getObject_item());
            if (anim_handled.getAnimation_type() == 0) {
                this.backPage();
            } else if (anim_handled.getAnimation_type() == 1) {
                this.sendPage();
            } else {
                anim_handled.play(object_item);
            }
        }
    }

    public ArrayList<Animation> searchAnimation(String causeId, int trigger) {
        ArrayList<Animation> anim_list = new ArrayList<>();
        for (int i = 0; i < this.pdfx_pages.size(); i++) {
            Page page_checked = pdfx_pages.get(i);
            for (int j = 0; j < page_checked.getAnimationList().size(); j++) {
                Animation anim_checked = page_checked.getAnimationList().get(j);
                if (anim_checked.getCause_item().equalsIgnoreCase(causeId) && anim_checked.getTrigger() == trigger) {
                    anim_list.add(anim_checked);
                }
            }
            for (int j = 0; j < page_checked.getFx_part_components().size(); j++) {
                FxPartComponent component_checked = page_checked.getFx_part_components().get(j);
                for (int k = 0; k < component_checked.getAnimationList().size(); k++) {
                    Animation anim_checked = component_checked.getAnimationList().get(k);
                    if (anim_checked.getCause_item().equalsIgnoreCase(causeId) && anim_checked.getTrigger() == trigger) {
                        anim_list.add(anim_checked);
                        System.out.println("animation discovered:" + anim_checked.id);
                    }
                }
            }
        }
        return anim_list;
    }

    public Animable searchAnimableFromObjectId(String objectId) {
        Animable causeItem = null;
        for (int i = 0; i < this.pdfx_pages.size(); i++) {
            Page page_checked = pdfx_pages.get(i);
            if (page_checked.getAnimableId().equalsIgnoreCase(objectId)) {
                causeItem = page_checked;
                return causeItem;
            }
            for (int j = 0; j < page_checked.getFx_part_components().size(); j++) {
                FxPartComponent component_checked = page_checked.getFx_part_components().get(j);
                if (component_checked.getAnimableId().equalsIgnoreCase(objectId)) {
                    causeItem = component_checked;
                    return causeItem;
                }
            }
        }
        return causeItem;

    }

    public void sendPage() {
        if (this.viewingPage < this.pdfx_pages.size() - 1) {
            pdfx_pages.get(viewingPage).finished();
            viewingPage++;
            viewerPane.getChildren().clear();
            pdfx_pages.get(viewingPage).prepareForImage();
            viewerPane.getChildren().add(pdfx_pages.get(viewingPage));
            pdfx_pages.get(viewingPage).loaded();
        } else {
            pdfx_pages.get(viewingPage).finished();
            viewingPage = 0;
            viewerPane.getChildren().clear();
            pdfx_pages.get(viewingPage).prepareForImage();
            viewerPane.getChildren().add(pdfx_pages.get(viewingPage));
            pdfx_pages.get(viewingPage).loaded();
        }
    }

    public void backPage() {
        if (this.viewingPage > 0) {
            pdfx_pages.get(viewingPage).finished();
            viewingPage--;
            viewerPane.getChildren().clear();
            pdfx_pages.get(viewingPage).prepareForImage();
            viewerPane.getChildren().add(pdfx_pages.get(viewingPage));
            pdfx_pages.get(viewingPage).loaded();
        } else {
            pdfx_pages.get(viewingPage).finished();
            this.viewingPage = this.pdfx_pages.size() - 1;
            viewerPane.getChildren().clear();
            pdfx_pages.get(viewingPage).prepareForImage();
            viewerPane.getChildren().add(pdfx_pages.get(viewingPage));
            pdfx_pages.get(viewingPage).loaded();
        }
    }

    private void setUpListener() {
        System.out.println("yes");
        this.animation_listener = new AnimationEventHandler() {
            @Override
            public void Handle(AnimationEvent event) {
                System.out.println("event has handled");
                Animable causeItem = event.getSource();
                System.out.println(causeItem);
                int Trigger = event.getStatus();
                System.out.println(Trigger);
                AnimationEventHandled(causeItem.getAnimableId(), Trigger);
            }
        };
        for (int i = 0; i < pdfx_pages.size(); i++) {
            System.out.println(pdfx_pages.get(i).getAnimableId() + "listener set");
            pdfx_pages.get(i).clearListener();
            pdfx_pages.get(i).setOnLoaded(animation_listener);
            pdfx_pages.get(i).setOnClicked(animation_listener);
            pdfx_pages.get(i).setOnFinished(animation_listener);
            pdfx_pages.get(i).setOnOtherActionOccored(animation_listener);
            for (int j = 0; j < pdfx_pages.get(i).getAnimationList().size(); j++) {
                System.out.println(pdfx_pages.get(i).getAnimationList().get(j).getAnimableId() + "listener set");
                pdfx_pages.get(i).getAnimationList().get(j).clearListener();
                pdfx_pages.get(i).getAnimationList().get(j).setOnLoaded(animation_listener);
                pdfx_pages.get(i).getAnimationList().get(j).setOnClicked(animation_listener);
                pdfx_pages.get(i).getAnimationList().get(j).setOnFinished(animation_listener);
                pdfx_pages.get(i).getAnimationList().get(j).setOnOtherActionOccored(animation_listener);
            }
            for (int k = 0; k < pdfx_pages.get(i).getFx_part_components().size(); k++) {
                pdfx_pages.get(i).getFx_part_components().get(k).clearListener();
                System.out.println(pdfx_pages.get(i).getFx_part_components().get(k).getAnimableId() + "listener set");
                pdfx_pages.get(i).getFx_part_components().get(k).setOnLoaded(animation_listener);
                pdfx_pages.get(i).getFx_part_components().get(k).setOnClicked(animation_listener);
                pdfx_pages.get(i).getFx_part_components().get(k).setOnFinished(animation_listener);
                pdfx_pages.get(i).getFx_part_components().get(k).setOnOtherActionOccored(animation_listener);
                for (int l = 0; l < pdfx_pages.get(i).getFx_part_components().get(k).getAnimationList().size(); l++) {
                    pdfx_pages.get(i).getFx_part_components().get(k).getAnimationList().get(l).clearListener();
                    System.out.println(pdfx_pages.get(i).getFx_part_components().get(k).getAnimationList().get(l).getAnimableId() + "listener set");
                    pdfx_pages.get(i).getFx_part_components().get(k).getAnimationList().get(l).setOnLoaded(animation_listener);
                    pdfx_pages.get(i).getFx_part_components().get(k).getAnimationList().get(l).setOnClicked(animation_listener);
                    pdfx_pages.get(i).getFx_part_components().get(k).getAnimationList().get(l).setOnFinished(animation_listener);
                    pdfx_pages.get(i).getFx_part_components().get(k).getAnimationList().get(l).setOnOtherActionOccored(animation_listener);
                }
            }
        }
    }

    @Override
    public void handleEvent(CaptureRequest request) {
        SnapshotParameters parameters = new SnapshotParameters();
            parameters.setViewport(new Rectangle2D(0, 0, viewerPane.getPrefWidth(), viewerPane.getPrefHeight()));
            WritableImage temp = new WritableImage((int) viewerPane.getPrefWidth(), (int) viewerPane.getPrefHeight());
            WritableImage result = viewerPane.snapshot(parameters, temp);
            screen_capture = new BufferedImage(1280,800, BufferedImage.TYPE_INT_RGB);
            resultimage = SwingFXUtils.fromFXImage(result, screen_capture);
        Thread sendthread = new Thread(new Runnable() {

            @Override
            public void run() {
                
            System.out.println("screen_capture:"+screen_capture);
            //projector.putChunkWithImage(resultimage);
            sender.ImageSend(resultimage);
            }
        });
            
        sendthread.start();    
        
    }

    void setStage(Stage primaryStage) {
        viewerStage = primaryStage;
        viewerPane = new StackPane();
        viewerScene = new Scene(viewerPane, 1280, 745);
        viewerScene.setFill(Color.BLACK);
        PerspectiveCamera camera = new PerspectiveCamera();
        viewerScene.setCamera(camera);
        viewerStage.setScene(viewerScene);
        viewerPane.setPrefWidth(1280);
        viewerPane.setPrefHeight(748);
        view = new ImageView();
        view.setFitHeight(200);
        view.setFitWidth(300);
        viewerPane.getChildren().add(view);
        viewerPane.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {

                System.out.println("key pressed");

                if (t.getCode().equals(KeyCode.RIGHT)) {
                    sendPage();
                } else if (t.getCode().equals(KeyCode.LEFT)) {
                    backPage();
                } else if (t.getCode().equals(KeyCode.ESCAPE)) {
                    viewerStage.hide();
                } else if (t.getCode().equals(KeyCode.F)) {
                    if (viewerStage.isFullScreen()) {
                        viewerStage.setFullScreen(false);
                    } else {
                        viewerStage.setFullScreen(true);
                    }
                }
            }
        });
        EventHandler<ActionEvent> aeh = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                CaptureRequest request = new CaptureRequest();
                request.setStartx((int) viewerStage.getX());
                request.setStarty((int) viewerStage.getY());
                request.setEndx((int) viewerStage.getWidth() + (int) viewerStage.getX());
                request.setEndy((int) viewerStage.getHeight() + (int) viewerStage.getY());
                handleEvent(request);
                thread.stop();
                thread = new Timeline();
                KeyValue value = new KeyValue(viewerPane.opacityProperty(), 1.0f);
                KeyFrame frame = new KeyFrame(new Duration(2000), value);
                thread.getKeyFrames().add(frame);
                thread.setOnFinished(this);
                thread.play();
            }
        };
        this.thread = new Timeline();
        KeyValue value = new KeyValue(viewerPane.opacityProperty(), 1.0f);
        KeyFrame frame = new KeyFrame(new Duration(2000), value);
        thread.getKeyFrames().add(frame);
        thread.setOnFinished(aeh);
        thread.play();
        System.out.println("thread start");
    }
}
