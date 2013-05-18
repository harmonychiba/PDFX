/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FxPartComponent;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author NPC
 */
public class FpPdfPagePanel extends FxPartComponent{
    File pdf_file;
    ImageView pdf_image;
    BufferedImage pdf_bimage;
    int page_number;
    
    TextField page_number_field;
    public FpPdfPagePanel(){
        this.id = "FpPdfPagePanel";
        this.page_number = 1;
        this.pdf_image = new ImageView();
        this.root.getChildren().add(pdf_image);
    }
    @Override
    public void generateParameterEditor() {
        super.generateParameterEditor();
        final TextField file_name_field = new TextField();
        file_name_field.setEditable(false);
        if(pdf_file!=null){
            file_name_field.setText(pdf_file.getAbsolutePath());
        }
        Button file_choose_button = new Button("Select PDF File");
        file_choose_button.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent t) {
                FileChooser fc = new FileChooser();
                fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
                pdf_file = fc.showOpenDialog(null);
                file_name_field.setText(pdf_file.getAbsolutePath());
                width.setText("1280");
                height.setText("745");
            }
        });
        Label page_number_label = new Label("Page Number");
        page_number_field = new TextField();
        page_number_field.setText(this.page_number+"");
        super.parameter_editor.getChildren().addAll(file_choose_button,file_name_field,page_number_label,page_number_field);
    }

    @Override
    public void generateComponent() {
        super.generateComponent();
        this.page_number = Integer.parseInt(page_number_field.getText());
        
        com.itextpdf.text.Document doc = new com.itextpdf.text.Document();
        doc.setPageSize(new Rectangle(1280, 745));
        try {
            doc.open();
            PdfDecoder pdf = new PdfDecoder();
            pdf.openPdfFile(this.pdf_file.getAbsolutePath());
            
            pdf_bimage = pdf.getPageAsImage(this.page_number);
            Image pdf_page_image = Page.createImage(pdf_bimage);
            this.pdf_image.setImage(pdf_page_image);
            //pdf_image.setViewport(new Rectangle2D(0, 0, this.getWidth_scale(), this.getHeight_scale()));
            pdf_image.setFitHeight(getHeight_scale());
            pdf_image.setFitWidth(getWidth_scale());
            
        } catch (IOException | PdfException ex) {
            Logger.getLogger(FpPdfPagePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void generateComponentFromXml() {
        super.generateComponentFromXml();
        this.page_number = page_number;
        
        com.itextpdf.text.Document doc = new com.itextpdf.text.Document();
        doc.setPageSize(new Rectangle(1280, 745));
        try {
            doc.open();
            PdfDecoder pdf = new PdfDecoder();
            pdf.openPdfFile(this.pdf_file.getAbsolutePath());
            
            pdf_bimage = pdf.getPageAsImage(this.page_number);
            Image pdf_page_image = Page.createImage(pdf_bimage);
            this.pdf_image.setImage(pdf_page_image);
            //pdf_image.setViewport(new Rectangle2D(0, 0, this.getWidth_scale(), this.getHeight_scale()));
            pdf_image.setFitHeight(getHeight_scale());
            pdf_image.setFitWidth(getWidth_scale());
        } catch (IOException | PdfException ex) {
            Logger.getLogger(FpPdfPagePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void regenerateComponent() {
        super.regenerateComponent();
        this.page_number = Integer.parseInt(page_number_field.getText());
        
        com.itextpdf.text.Document doc = new com.itextpdf.text.Document();
        doc.setPageSize(new Rectangle(1280, 745));
        try {
            doc.open();
            PdfDecoder pdf = new PdfDecoder();
            pdf.openPdfFile(this.pdf_file.getAbsolutePath());
            
            pdf_bimage = pdf.getPageAsImage(this.page_number);
            Image pdf_page_image = Page.createImage(pdf_bimage);
            this.pdf_image.setImage(pdf_page_image);
            //pdf_image.setViewport(new Rectangle2D(0, 0, this.getWidth_scale(), this.getHeight_scale()));
            pdf_image.setFitHeight(getHeight_scale());
            pdf_image.setFitWidth(getWidth_scale());
        } catch (IOException | PdfException ex) {
            Logger.getLogger(FpPdfPagePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public void createDetailXmlElement(Document doc, Element fxPartComponentElement) {
        super.createDetailXmlElement(doc, fxPartComponentElement);
        
        Element imageElement = doc.createElement("PdfImageFile");
        Node imageNode = doc.createTextNode(this.pdf_file.getAbsolutePath());
        imageElement.appendChild(imageNode);
        fxPartComponentElement.appendChild(imageElement);
        
        Element numberElement = doc.createElement("PageNumber");
        Node numberNode = doc.createTextNode(this.page_number+"");
        numberElement.appendChild(numberNode);
        fxPartComponentElement.appendChild(numberElement);
    }

    @Override
    public void createParameter(Element element) {
        super.createParameter(element);
        if(element.getTagName().equalsIgnoreCase("PdfImageFile")){
            this.pdf_file = new File(element.getChildNodes().item(0).getTextContent());
        }
        else if(element.getTagName().equalsIgnoreCase("PageNumber")){
            this.page_number = Integer.parseInt(element.getChildNodes().item(0).getTextContent());
            System.out.println("よばれてるよー");
        }
    }
    
}
