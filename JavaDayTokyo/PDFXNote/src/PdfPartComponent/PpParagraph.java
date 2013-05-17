/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package PdfPartComponent;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 *
 * @author y_chiba
 */
public class PpParagraph extends PdfPartComponent{
    private String text;
    private String font_type;
    private String code_type;
    private int font_size;
    private int font_detail;
    
    ArrayList<String> font_type_list;
    ArrayList<String> font_code_list;
    ArrayList<String> font_detail_list;
    
    private TextArea text_editor;
    private ComboBox<String> font_type_editor;
    private ComboBox<String> font_code_editor;
    private TextField size_editor;
    private ComboBox<String> font_detail_editor;
    
    public PpParagraph(){
        this.setDefaultFont();
        collecteFontType();
        collecteFontCode();
        collecteDetailList();
        this.Id = "PpParagraph";
    }
    private void collecteFontType(){
        this.font_type_list = new ArrayList<>();
        font_type_list.add("COURIER");
        font_type_list.add("HELVETICA");
        font_type_list.add("TIMES");
    }
    private void collecteFontCode(){
        this.font_code_list = new ArrayList<>();
        font_code_list.add("defaultEncoding");
    }
    
    private void collecteDetailList(){
        this.font_detail_list = new ArrayList<>();
        font_detail_list.add("DEFAULT");
        font_detail_list.add("BOLD");
        font_detail_list.add("BOLDITALIC");
        font_detail_list.add("ITALIC");
        font_detail_list.add("STRIKETHRU");
        font_detail_list.add("UNDERLINE");
    }
    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    private void setDefaultFont(){
        font_type = FontFactory.TIMES_ROMAN;
        code_type = FontFactory.defaultEncoding;
        font_size = 20;
        font_detail = Font.BOLD;
    }
    @Override
    public void drawComponent(Document doc,PdfWriter writer) {
        super.drawComponent(doc,writer);
        System.out.println("Draw");
        try {
            Font font = new Font(BaseFont.createFont(font_type, code_type, BaseFont.NOT_EMBEDDED),font_size,font_detail);
            Paragraph pg = new Paragraph(text,font);
            doc.add(pg);
            this.width_scale = (int) doc.getPageSize().getWidth();
            this.height_scale = (int) doc.getPageSize().getHeight();
            
            System.out.println("drawed");
        } catch (IOException | DocumentException ex) {
            Logger.getLogger(PpParagraph.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void generateParameterEditor() {
        super.generateParameterEditor();
        Label text_editor_lbl = new Label("Text");
        Label font_type_editor_lbl = new Label("Font Type");
        Label font_code_editor_lbl = new Label("Codec Type");
        Label font_size_editor_lbl = new Label("Font Size");
        Label font_detail_editor_lbl = new Label("Font Form");
        text_editor = new TextArea();
        text_editor.setWrapText(true);
        font_type_editor = new ComboBox<>();
        ObservableList<String> font_type_obs_list = FXCollections.observableArrayList(this.font_type_list);
        font_type_editor.setItems(font_type_obs_list);
        font_type_editor.getSelectionModel().select(getSelectedIndexOfType());
        font_code_editor = new ComboBox<>();
        ObservableList<String> font_code_obs_list = FXCollections.observableArrayList(this.font_code_list);
        font_code_editor.setItems(font_code_obs_list);
        font_code_editor.getSelectionModel().select(getSelectedIndexOfCode());
        size_editor = new TextField();
        size_editor.setText(this.font_size+"");
         font_detail_editor = new ComboBox<>();
        ObservableList<String> font_detail_obs_list = FXCollections.observableArrayList(this.font_detail_list);
        font_detail_editor.setItems(font_detail_obs_list);
        font_detail_editor.getSelectionModel().select(getSelectedIndexOfDefault());
        this.parameter_editor.getChildren().addAll(text_editor_lbl,text_editor,
                font_type_editor_lbl,font_type_editor,
                font_code_editor_lbl,font_code_editor,
                font_size_editor_lbl,size_editor,
                font_detail_editor_lbl,font_detail_editor);
    }
    private int getSelectedIndexOfType(){
        if(this.font_type.equalsIgnoreCase(FontFactory.COURIER)){
            return 0;
        }
        else if(this.font_type.equalsIgnoreCase(FontFactory.HELVETICA)){
            return 1;
        }
        else if(this.font_type.equalsIgnoreCase(FontFactory.TIMES_ROMAN)){
            return 2;
        }
        else{
            return 3;
        }
    }
    private int getSelectedIndexOfCode(){
        if(this.code_type.equalsIgnoreCase(FontFactory.defaultEncoding)){
            return 0;
        }
        else{
            return 1;
        }
    }
    private int getSelectedIndexOfDefault(){
        
        if(this.font_detail == Font.BOLD){
            return 1;
        }
        else if(this.font_detail == Font.BOLDITALIC){
            return 2;
        }
        else if(this.font_detail == Font.ITALIC){
            return 3;
        }
        else if(this.font_detail == Font.STRIKETHRU){
            return 4;
        }
        else if(this.font_detail == Font.UNDERLINE){
            return 5;
        }
        else{
            return 0;
        }
    }
    
    /**
     * @return the font_type
     */
    public String getFont_type() {
        return font_type;
    }

    /**
     * @param font_type the font_type to set
     */
    public void setFont_type(String font_type) {
        this.font_type = font_type;
    }

    /**
     * @return the code_type
     */
    public String getCode_type() {
        return code_type;
    }

    /**
     * @param code_type the code_type to set
     */
    public void setCode_type(String code_type) {
        this.code_type = code_type;
    }

    /**
     * @return the font_size
     */
    public int getFont_size() {
        return font_size;
    }

    /**
     * @param font_size the font_size to set
     */
    public void setFont_size(int font_size) {
        this.font_size = font_size;
    }

    /**
     * @return the font_detail
     */
    public int getFont_detail() {
        return font_detail;
    }

    /**
     * @param font_detail the font_detail to set
     */
    public void setFont_detail(int font_detail) {
        this.font_detail = font_detail;
    }
    @Override
    public void prepareForAdd(){
        super.prepareForAdd();
        this.font_type = getSelectedType();
        this.text = text_editor.getText();
        if(size_editor.getText() != null){
            this.font_size = Integer.parseInt(size_editor.getText());
        }
        this.font_detail = getSelectedDetail();
    }
    @Override
    public void edit(){
         this.font_type = getSelectedType();
        this.text = text_editor.getText();
        if(size_editor.getText() != null){
            this.font_size = Integer.parseInt(size_editor.getText());
        }
        this.font_detail = getSelectedDetail();
    }
    private String getSelectedType(){
        if(this.font_type_editor.getSelectionModel().isSelected(0)){
            return FontFactory.COURIER;
        }
        else if(this.font_type_editor.getSelectionModel().isSelected(1)){
            return FontFactory.HELVETICA;
        }
        else if(this.font_type_editor.getSelectionModel().isSelected(2)){
            return FontFactory.TIMES_ROMAN;
        }
        return this.font_type;
    }

    private int getSelectedDetail() {
        if(this.font_detail_editor.getSelectionModel().isSelected(0)){
            return Font.UNDEFINED;
        }
        else if(this.font_detail_editor.getSelectionModel().isSelected(1)){
            return Font.BOLD;
        }
        else if(this.font_detail_editor.getSelectionModel().isSelected(2)){
            return Font.BOLDITALIC;
        }
        else if(this.font_detail_editor.getSelectionModel().isSelected(3)){
            return Font.ITALIC;
        }
        else if(this.font_detail_editor.getSelectionModel().isSelected(4)){
            return Font.STRIKETHRU;
        }
        else if(this.font_detail_editor.getSelectionModel().isSelected(5)){
            return Font.UNDERLINE;
        }
        else{
            return Font.UNDEFINED;
        }
    }
}
