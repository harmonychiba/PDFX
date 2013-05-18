/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pdfxnote;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author y_chiba
 */
public class EditorChangeEventHandler implements ChangeListener<String>{
    PDFXNote note;
    public void setPDFXNote(PDFXNote note){
        this.note = note;
    }
    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        System.out.println("changed");
        note.componentSelected(newValue);
    }
}

