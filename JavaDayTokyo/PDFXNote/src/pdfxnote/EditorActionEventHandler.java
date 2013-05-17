/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pdfxnote;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;

/**
 *
 * @author y_chiba
 */
public class EditorActionEventHandler implements EventHandler<ActionEvent>{
    PDFXNote note;
    public void setPDFXNote(PDFXNote note){
        this.note = note;
    }
    
    @Override
    public void handle(ActionEvent event) {
        String item_id = ((Node)(event.getSource())).getId();
        if(item_id.equalsIgnoreCase("page_preview_add_button")){
            if(note != null){
                note.addPage();
            }
        }
        else if(item_id.equalsIgnoreCase("parameter_editor_add_pp_button")){
            if(note != null){
                note.addPdfPartComponentToPage();
            }
        }
        else if(item_id.equalsIgnoreCase("parameter_editor_add_fp_button")){
            if(note != null){
                note.addFxPartComponentToPage();
            }
        }
        else if(item_id.equalsIgnoreCase("parameter_editor_edit_pp_button")){
            if(note != null){
                note.editPdfPartComponentOnPage();
            }
        }
        else if(item_id.equalsIgnoreCase("parameter_editor_edit_fp_button")){
            if(note != null){
                note.editFxPartComponentOnPage();
            }
        }
        else if(item_id.equalsIgnoreCase("parameter_editor_delete_pp_button")){
            if(note != null){
                note.deletePdfPartComponentFromPage();
            }
        }
        else if(item_id.equalsIgnoreCase("parameter_editor_delete_fp_button")){
            if(note != null){
                note.deleteFxPartComponentFromPage();
            }
        }
        event.consume();
    }
    
}
