/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package PdfPartComponent;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.scene.Group;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author y_chiba
 */
public class PdfPartComponent {
    static final int UNDEFINED_PDFPART_COMPONENT = 0;
    
    public static int FRAME_TYPE_MAIN_SELECTION = 1;
    public static int FRAME_TYPE_SUB_SELECTION = 2;
    
    public float translation_x;
    public float translation_y;
    public float depth_z;
    public int color_red;
    public int color_green;
    public int color_blue;
    public int opacity;
    
    int width_scale;
    int height_scale;
    
    public String Id;
    public TextField id_editor;
    
    VBox parameter_editor;

    public VBox getParameter_editor() {
        return parameter_editor;
    }
    
    int ComponentType;
    public PdfPartComponent(){
        this.ComponentType = UNDEFINED_PDFPART_COMPONENT;
    }
    public void drawComponent(Document doc,PdfWriter writer){
        
    }
    public void generateParameterEditor(){
        this.parameter_editor = new VBox();
        this.id_editor = new TextField();
        this.parameter_editor.getChildren().add(id_editor);
        this.id_editor.setText(Id);
        
    }
    /**
     * @return the translation_x
     */
    public float getTranslation_x() {
        return translation_x;
    }

    /**
     * @param translation_x the translation_x to set
     */
    public void setTranslation_x(float translation_x) {
        this.translation_x = translation_x;
    }

    /**
     * @return the translation_y
     */
    public float getTranslation_y() {
        return translation_y;
    }

    /**
     * @param translation_y the translation_y to set
     */
    public void setTranslation_y(float translation_y) {
        this.translation_y = translation_y;
    }

    /**
     * @return the depth_z
     */
    public float getDepth_z() {
        return depth_z;
    }

    /**
     * @param depth_z the depth_z to set
     */
    public void setDepth_z(float depth_z) {
        this.depth_z = depth_z;
    }

    /**
     * @return the color_red
     */
    public int getColor_red() {
        return color_red;
    }

    /**
     * @param color_red the color_red to set
     */
    public void setColor_red(int color_red) {
        this.color_red = color_red;
    }

    /**
     * @return the color_green
     */
    public int getColor_green() {
        return color_green;
    }

    /**
     * @param color_green the color_green to set
     */
    public void setColor_green(int color_green) {
        this.color_green = color_green;
    }

    /**
     * @return the color_blue
     */
    public int getColor_blue() {
        return color_blue;
    }

    /**
     * @param color_blue the color_blue to set
     */
    public void setColor_blue(int color_blue) {
        this.color_blue = color_blue;
    }

    /**
     * @return the opacity
     */
    public int getOpacity() {
        return opacity;
    }

    /**
     * @param opacity the opacity to set
     */
    public void setOpacity(int opacity) {
        this.opacity = opacity;
    }
    public void prepareForAdd(){
        this.Id = id_editor.getText();
    }
    public void edit(){
        this.Id = id_editor.getText();
    }
    public Group getFrameForComponent(int type){
        Group frame = new Group();
        Line upper = new Line(-this.width_scale/2,-this.height_scale/2,this.width_scale/2,-this.height_scale/2);upper.setStrokeDashOffset(2);
        Line bottom = new Line(-this.width_scale/2,this.height_scale/2,this.width_scale/2,this.height_scale/2);bottom.setStrokeDashOffset(2);
        Line leftside = new Line(-this.width_scale/2,-this.height_scale/2,-this.width_scale/2,this.height_scale/2);leftside.setStrokeDashOffset(2);
        Line rightside = new Line(this.width_scale/2,this.height_scale/2,this.width_scale/2,-this.height_scale/2);rightside.setStrokeDashOffset(2);
        Paint paint = Color.BLUE;
        if(type == FRAME_TYPE_MAIN_SELECTION){
            paint = Color.BLACK;
        }
        else if(type == FRAME_TYPE_SUB_SELECTION){
            paint = Color.RED;
        }
        upper.setStroke(paint);
        bottom.setStroke(paint);
        rightside.setStroke(paint);
        leftside.setStroke(paint);
        frame.getChildren().addAll(upper,bottom,leftside,rightside);
        frame.setTranslateX(translation_x);
        frame.setTranslateY(translation_y);
        frame.setTranslateZ(depth_z);
        return frame;
    }

    public Element createXmlElement(org.w3c.dom.Document doc, Element pdfPartComponentElement) {
        return pdfPartComponentElement;
    }
    public static PdfPartComponent createComponent(Node item) {
        PdfPartComponent component = new PdfPartComponent();
        return component;
    }
}
