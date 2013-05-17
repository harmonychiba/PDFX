/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FxPartComponent;

import Animation.Animable;
import Animation.Animation;
import Animation.AnimationEvent;
import Animation.AnimationEventHandler;
import PdfPartComponent.PdfPartComponent;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;
import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import pdfxnote.FXImaging;
public class Page extends Group implements Animable{
    String id;
    public int page_number;
    public String project_name;
    
    ImageView pdf_image;
    ImageView sub_image_view;
    ArrayList<PdfPartComponent> pdf_part_components;

    ArrayList<FxPartComponent> fx_part_components;
    
    ArrayList<Animation> animation_list;
    
    private ArrayList<AnimationEventHandler> handler_loadevent;
    private ArrayList<AnimationEventHandler> handler_clickevent;
    private ArrayList<AnimationEventHandler> handler_finished;
    private ArrayList<AnimationEventHandler> handler_otherevent;

    public Page(String project_name,int page_number){
        this.setDepthTest(DepthTest.ENABLE);
        
        this.handler_loadevent = new ArrayList<>();
        this.handler_clickevent = new ArrayList<>();
        this.handler_finished = new ArrayList<>();
        this.handler_otherevent = new ArrayList<>();
        
        this.project_name = project_name;
        pdf_image = new ImageView();
        this.sub_image_view = new ImageView();
        this.page_number = page_number;
        pdf_part_components = new ArrayList<>();
        fx_part_components = new ArrayList<>();

        id = "page_"+project_name+page_number;
        
        this.redraw();
        
        animation_list = new ArrayList<>();
    }
    public void prepareForImage(){
        this.redraw();
    }
    private void redraw(){
        
        this.getChildren().clear();
        this.getChildren().add(this.pdf_image);
        
        PdfDecoder pdf = new PdfDecoder();
        //File tmp_file = new File(project_name+"/"+page_number+".pdf");
        Document doc = new Document();
        doc.setPageSize(new Rectangle(1280, 745));
        BufferedOutputStream output;
        ByteArrayOutputStream byte_output;
        
        try {
            byte_output = new ByteArrayOutputStream();
            output = new BufferedOutputStream(byte_output);
            PdfWriter writer = PdfWriter.getInstance(doc, output);
            doc.open();
            writer.open();
            for(int i = 0;i<pdf_part_components.size();i++){
                System.out.println(i);
                pdf_part_components.get(i).drawComponent(doc,writer);
            }
            for(int i = 0;i<fx_part_components.size();i++){
                System.out.println(i+"st fx_part_component have");
                System.out.println(fx_part_components.get(i).getRoot().getChildren().size()+" parts");
                this.getChildren().add(fx_part_components.get(i).getRoot());
                fx_part_components.get(i).loaded();
            }
            if(pdf_part_components.size()<1){
                writer.setPageEmpty(false);
            }
            try{
                doc.close();
            }catch(Exception e){
                writer.setPageEmpty(false);
                doc.close();
            }
            writer.close();
            output.close();
            
            //String filename = tmp_file.getAbsolutePath();
            pdf.openPdfArray(byte_output.toByteArray());
            //pdf.openPdfFile(filename);
            
            BufferedImage pdf_bimage = pdf.getPageAsImage(1);
            Image pdf_page1_image = createImage(pdf_bimage);
            pdf_bimage = pdf.getPageAsImage(1);
            Image sub_image = createImage(pdf_bimage);
            this.pdf_image.setImage(pdf_page1_image);
            this.sub_image_view.setImage(sub_image);
            
            this.createPreviewImage();
            
        }catch (DocumentException | IOException |PdfException ex) {
            Logger.getLogger(Page.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addPdfPartComponent(PdfPartComponent component){
        this.pdf_part_components.add(component);
        this.redraw();
    }
    public void addFxPartComponent(FxPartComponent component){
        this.fx_part_components.add(component);
        this.redraw();
    }
    public int getCountOfPdfPartComponent(){
        return pdf_part_components.size();
    }
    public int getCountOfFxPartComponent(){
        return fx_part_components.size();
    }
    public PdfPartComponent pdfPartComponentAtIndex(int index){
        return pdf_part_components.get(index);
    }
    public FxPartComponent fxPartComponentAtIndex(int index){
        return fx_part_components.get(index);
    }
    public void editPdfPartComponent(PdfPartComponent component){
        this.redraw();
    }
    public void editFxPartComponent(FxPartComponent component){
        this.redraw();
    }
    public static javafx.scene.image.Image createImage(java.awt.Image image) throws IOException {
    if (!(image instanceof RenderedImage)) {
      BufferedImage bufferedImage = new BufferedImage(image.getWidth(null),
              image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
      Graphics g = bufferedImage.createGraphics();
      g.drawImage(image, 0, 0, null);
      g.dispose();
 
      image = bufferedImage;
    }
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    ImageIO.write((RenderedImage) image, "png", out);
    out.flush();
    ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
    return new javafx.scene.image.Image(in);
  }

    public ImageView getPdfImage() {
        return this.pdf_image;
    }
    public ImageView getSubPdfImage(){
        this.createPreviewImage();
        return this.sub_image_view;
    }
    public void destoryImage(){
        this.pdf_image.setImage(null);
    }

    public ObservableList<String> getItem_List() {
        ArrayList<String> items_string = new ArrayList<>();
        for(int i = 0;i<this.fx_part_components.size();i++){
            items_string.add(this.fx_part_components.get(i).id);
        }
        for(int i = 0;i<this.pdf_part_components.size();i++){
            items_string.add(this.pdf_part_components.get(i).Id);
        }
        return FXCollections.observableArrayList(items_string);
    }

    @Override
    public void loaded() {
        AnimationEvent event = new AnimationEvent();
        event.setSource(this);
        event.setStatus(AnimationEvent.LOADED);
        for(int i = 0;i<this.handler_loadevent.size();i++){
            handler_loadevent.get(i).Handle(event);
        }
    }

    @Override
    public void clicked() {
        AnimationEvent event = new AnimationEvent();
        event.setSource(this);
        event.setStatus(AnimationEvent.CLICKED);
        for(int i = 0;i<this.handler_clickevent.size();i++){
            handler_clickevent.get(i).Handle(event);
        }
    }

    @Override
    public void finished() {
        AnimationEvent event = new AnimationEvent();
        event.setSource(this);
        event.setStatus(AnimationEvent.FINISHED);
        for(int i = 0;i<this.handler_finished.size();i++){
            handler_finished.get(i).Handle(event);
        }
    }

    @Override
    public void otherActionHasOccored() {
        AnimationEvent event = new AnimationEvent();
        event.setSource(this);
        event.setStatus(AnimationEvent.OTHER);
        for(int i = 0;i<this.handler_otherevent.size();i++){
            handler_otherevent.get(i).Handle(event);
        }
    }

    @Override
    public void setOnLoaded(AnimationEventHandler handler) {
        this.handler_loadevent.add(handler);
    }

    @Override
    public void setOnClicked(AnimationEventHandler handler) {
        this.handler_clickevent.add(handler);
    }

    @Override
    public void setOnFinished(AnimationEventHandler handler) {
        this.handler_finished.add(handler);
    }

    @Override
    public void setOnOtherActionOccored(AnimationEventHandler handler) {
        this.handler_otherevent.add(handler);
    }

    public Object getSelectedComponent(String id) {
        for(int i = 0;i<this.fx_part_components.size();i++){
            if(fx_part_components.get(i).id.equalsIgnoreCase(id)){
                return fx_part_components.get(i);
            }
        }
        for(int i = 0;i<this.pdf_part_components.size();i++){
            if(pdf_part_components.get(i).Id.equalsIgnoreCase(id)){
                return pdf_part_components.get(i);
            }
        }
        return null;
    }

    public void deletePdfPartComponent(PdfPartComponent pp_component) {
        this.pdf_part_components.remove(pp_component);
        redraw();
    }

    public void deteleFxPartComponent(FxPartComponent fp_component) {
        this.fx_part_components.remove(fp_component);
        redraw();
    }
    @Override
    public String getAnimableId(){
        return id;
    }

    @Override
    public ArrayList<Animation> getAnimationList() {
        return animation_list;
    }
    
    public ArrayList<PdfPartComponent> getPdf_part_components() {
        return pdf_part_components;
    }

    public ArrayList<FxPartComponent> getFx_part_components() {
        return fx_part_components;
    }

    public Element createXmlElement(org.w3c.dom.Document doc,Element pageElement) {
        Element idElement = doc.createElement("Id");
        Node idNode = doc.createTextNode(id);
        idElement.appendChild(idNode);
                
        pageElement.appendChild(idElement);
        
        Element numberElement = doc.createElement("PageNumber");
        Node numberNode = doc.createTextNode(page_number+"");
        numberElement.appendChild(numberNode);
        
        pageElement.appendChild(numberNode);
        
        Element animationListElement = doc.createElement("AnimationList");
        for(int i = 0;i<this.animation_list.size();i++){
            Element animationElement = animation_list.get(i).createXmlElement(doc,doc.createElement("Animation"));
            animationListElement.appendChild(animationElement);
        }
        Element pdfPartComponentListElement = doc.createElement("PdfPartComponentList");
        for(int i = 0;i<this.pdf_part_components.size();i++){
            Element pdfPartComponentElement = this.pdf_part_components.get(i).createXmlElement(doc,doc.createElement("PdfPartComponent"));
            pdfPartComponentListElement.appendChild(pdfPartComponentElement);
        }
        pageElement.appendChild(pdfPartComponentListElement);
        
        Element fxPartComponentListElement = doc.createElement("FxPartComponentList");
        for(int i = 0;i<this.fx_part_components.size();i++){
            Element fxPartComponentElement = this.fx_part_components.get(i).createXmlElement(doc,doc.createElement("FxPartComponent"));
            fxPartComponentListElement.appendChild(fxPartComponentElement);
        }
        pageElement.appendChild(fxPartComponentListElement);
        
        pageElement.appendChild(animationListElement);
        return pageElement;
    }
    public void createPreviewImage(){
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setViewport(new Rectangle2D(0, 0, 1280, 745));
        WritableImage wi = new WritableImage(1280, 745);
        WritableImage result = this.snapshot(parameters, wi);
        this.sub_image_view.setImage(result);
        /*FXImaging toImage = new FXImaging();
        File imageFile = new File("Image");
        Group tempGroup = new Group();
        toImage.nodeToImage(this,tempGroup.getChildren() , imageFile);
        this.sub_image_view = (ImageView) tempGroup.getChildren().get(0);*/
    }
    public static Page createPage(Node item,String project_name,int number) {
        Page page = new Page(project_name,number);
        Element fxPartComponentListElement;
        Element pdfPartComponentListElement;
        Element animationListlement;
        for(int i = 0;i<item.getChildNodes().getLength();i++){
            Element element = null;
            System.out.println(item.getChildNodes().item(i).getNodeName());
            try{
                element = (Element) (item.getChildNodes().item(i));
            }catch(ClassCastException e){
                continue;
            }
            if(element.getTagName().equalsIgnoreCase("PdfPartComponentList")){
                for(int j = 0;j<element.getChildNodes().getLength();j++){
                    
                    page.pdf_part_components.add(PdfPartComponent.createComponent(element.getChildNodes().item(j)));
                }
            }
            else if(element.getTagName().equalsIgnoreCase("FxPartComponentList")){
                for(int j = 0;j<element.getChildNodes().getLength();j++){
                    System.out.println(element.getChildNodes().item(j).getNodeName()+"maybe fxpartcomponent");
                    FxPartComponent component = FxPartComponent.createComponent(element.getChildNodes().item(j));
                    component.generateComponentFromXml();
                    page.fx_part_components.add(component);
                }
            }
            else if(element.getTagName().equalsIgnoreCase("AnimationList")){
                for(int j = 0;j<element.getChildNodes().getLength();j++){
                    Animation animation = new Animation();
                    for(int k = 0;k<element.getChildNodes().item(j).getChildNodes().getLength();k++){
                        animation.createParameterFromXmlElement((Element)(element.getChildNodes().item(j).getChildNodes().item(k)));
                        
                    }
                    page.animation_list.add(animation);
                }
            }
        }
        
        return page;
    }

    public void clearListener() {
        this.handler_clickevent.clear();
        this.handler_finished.clear();
        this.handler_loadevent.clear();
        this.handler_otherevent.clear();
    }
}