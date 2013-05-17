/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pdfxnote;

import com.sun.javafx.collections.MappingChange;
import com.sun.javafx.collections.MappingChange.Map;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

/**
 *
 * @author y_chiba
 */
public class EditorMouseEventHandler implements EventHandler<MouseEvent>{
    PDFXNote note;
    public void setPDFXNote(PDFXNote note){
        this.note = note;
    }
    
    @Override
    public void handle(MouseEvent event) {
        
        String item_id = ((Node)(event.getSource())).getId();
        
        if(item_id.startsWith("page_prev_image_view")){
            System.out.println(item_id.substring(0, item_id.length()-1));
            if(note != null){
                int selected_index = Integer.parseInt(item_id.substring(20));
                //int selected_index = item_id.charAt(item_id.length()-1)-'0';
                this.note.pageSelected(selected_index);
            }
        }
        if(item_id.substring(0, 9).equalsIgnoreCase("Component")){
            System.out.println(item_id.substring(0,9));
            if(note != null){
                Dragboard db = ((Node)(event.getSource())).startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.putString(event.getSource().toString());
                db.setContent(content);
                this.note.dragDetectedOfComponent(item_id);
            }
        }
        event.consume();
    }
    
}
