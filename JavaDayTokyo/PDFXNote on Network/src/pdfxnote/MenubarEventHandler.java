/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pdfxnote;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author y_chiba
 */
public class MenubarEventHandler implements EventHandler<ActionEvent> {
    PDFXNote note;
    String ip = null;
    MenubarEventHandler(PDFXNote aThis) {
        note = aThis;
    }
    @Override
    public void handle(ActionEvent event) {
       MenuItem mitem = (MenuItem) event.getSource();
       String item_id = mitem.getId();
       System.out.println(item_id+" is pressed.");
       if(item_id.equalsIgnoreCase("mitem_new")){
           this.pressedItemNew();
       }
       else if(item_id.equalsIgnoreCase("mitem_new_from_pdf")){
           this.pressedItemNewFromPdf();
       }
       else if(item_id.equalsIgnoreCase("mitem_open")){
           this.pressedItemOpen();
       }
       else if(item_id.equalsIgnoreCase("mitem_save")){
           this.pressedItemSave();
       }
       else if(item_id.equalsIgnoreCase("mitem_saveas")){
           this.pressedItemSaveAs();
       }
       else if(item_id.equalsIgnoreCase("mitem_quit")){
           this.pressedItemQuit();
       }
       else if(item_id.equalsIgnoreCase("mitem_viewer")){
           this.pressedItemView();
       }
       else if(item_id.equalsIgnoreCase("mitem_set_fullscreen")){
           this.pressedItemFullScreen();
       }
    }
    private void pressedItemNew(){
        final Stage stage = new Stage(StageStyle.TRANSPARENT);
        StackPane root = new StackPane();
        Scene scene = new Scene(root,300,200, Color.SKYBLUE);
        stage.setScene(scene);
        
        VBox vbox = new VBox();vbox.autosize();
        Text text = new Text("Please input Project name.");
        final TextField name_field = new TextField("NewPdfxProject");name_field.setPrefHeight(100);
        HBox buttons = new HBox();buttons.setPrefHeight(100);
        Button OK_button = new Button("OK");OK_button.setPrefWidth(100);
        Button cancel_button = new Button("Cancel");cancel_button.setPrefWidth(100);
        
        OK_button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if(note != null){
                    note.file_generated(name_field.getText());
                    stage.close();
                }
            }
        });
        cancel_button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                stage.hide();
            }
        });
        
        buttons.getChildren().addAll(OK_button,cancel_button);
        vbox.getChildren().addAll(text,name_field,buttons);
        
        root.getChildren().add(vbox);
        
        stage.show();
    }

    private void pressedItemNewFromPdf() {
        note.selectPdfFile();
        this.pressedItemNew();
    }

    private void pressedItemOpen() {
        note.openFile();
    }

    private void pressedItemSave() {
        try {
            note.saveFile();
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(MenubarEventHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(MenubarEventHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void pressedItemSaveAs() {
        note.selectFile(PDFXNote.SAVE_MODE);
        try {
            note.saveFile();
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(MenubarEventHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(MenubarEventHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void pressedItemQuit() {
        System.exit(0);
    }

    private void pressedItemFullScreen() {
        if(this.note != null){
            note.viewFullScreen();
        }
    }

    private void pressedItemView() {
        if(this.note != null){
            note.presentation();
        }
    }
    public void pressedItemSetIP(){
        final Stage stage = new Stage(StageStyle.TRANSPARENT);
        StackPane root = new StackPane();
        Scene scene = new Scene(root,300,200, Color.SKYBLUE);
        stage.setScene(scene);
        
        VBox vbox = new VBox();vbox.autosize();
        Text text = new Text("Please input Project IP.");
        final TextField ip_field = new TextField("0.0.0.0");ip_field.setPrefHeight(100);
        HBox buttons = new HBox();buttons.setPrefHeight(100);
        Button OK_button = new Button("OK");OK_button.setPrefWidth(100);
        Button cancel_button = new Button("Cancel");cancel_button.setPrefWidth(100);
        
        OK_button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                    ip = ip_field.getText();
                    stage.close();
                
            }
        });
        cancel_button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                stage.hide();
            }
        });
        
        buttons.getChildren().addAll(OK_button,cancel_button);
        vbox.getChildren().addAll(text,ip_field,buttons);
        
        root.getChildren().add(vbox);
        
        stage.show();
    }
    public String getIP(){
        return ip;
        
    }
}
