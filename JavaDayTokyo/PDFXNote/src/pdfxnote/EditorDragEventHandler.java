/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pdfxnote;

import javafx.event.EventHandler;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;

/**
 *
 * @author y_chiba
 */
public class EditorDragEventHandler implements EventHandler<DragEvent> {
    PDFXNote note;

    void setPDFXNote(PDFXNote aThis) {
        this.note = aThis;
    }

    @Override
    public void handle(DragEvent event) {
        javafx.scene.Node source = (javafx.scene.Node) event.getSource();
        String item_id = source.getId();
        System.out.println("dragged");
        if(item_id.equalsIgnoreCase("Parameter Editor")){
            System.out.println("is parameter editor == true");
            if(event.isAccepted() == false){
                System.out.println("Drag is accepted");
                event.acceptTransferModes(TransferMode.ANY);
            }
            if(this.note!=null&&event.getEventType().getName().equalsIgnoreCase("DRAG_DROPPED")){
                System.out.println("Dropped");
                note.itemDragged();
            }
        }
        event.consume();
    }
    
}
