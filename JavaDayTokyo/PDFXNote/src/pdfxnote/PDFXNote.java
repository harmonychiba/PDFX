/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pdfxnote;

import FxPartComponent.Page;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class PDFXNote extends Application {
    public static int SAVE_MODE = 10;
    public static int OPEN_MODE = 20;
    public static int OPEN_PDF_MODE = 30;
    Stage primaryStage;
    Scene scene;
    
    String project_name;
    int displayed_page_index;
    ArrayList<Page> pdfx_pages;
    
    Stage stage_for_menubar;
    Scene scene_for_menubar;
    StackPane root_for_menubar;
    
    MenuBar menubar;
    MenubarEventHandler mb_event_handler;
    EditorActionEventHandler e_action_event_handler;
    EditorMouseEventHandler e_mouse_event_handler;
    EditorDragEventHandler e_drag_event_handler;
    EditorChangeEventHandler e_change_event_handler;
    
    private int display_width;
    private int display_height;
    private int main_window_x;
    private int main_window_y;
    private int main_window_width;
    private int main_window_height;
    
    private PDFXEditor editor;
    private PDFXViewer viewer;
    private StackPane root;
    
    private String drag_detected_component;
    
    private File file;
    
    public void initialize(){
        java.awt.GraphicsEnvironment env = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment();
        //java.awt.DisplayMode displayMode = env.getDefaultScreenDevice().getDisplayMode();
        this.display_width = 1366;//displayMode.getWidth();
        this.display_height = 768;//displayMode.getHeight();

        e_action_event_handler = new EditorActionEventHandler();
        e_action_event_handler.setPDFXNote(this);
        e_mouse_event_handler = new EditorMouseEventHandler();
        e_mouse_event_handler.setPDFXNote(this);
        e_drag_event_handler = new EditorDragEventHandler();
        e_drag_event_handler.setPDFXNote(this);
        e_change_event_handler = new EditorChangeEventHandler();
        e_change_event_handler.setPDFXNote(this);
        editor = new PDFXEditor();
        editor.setEditorActionEventHandler(e_action_event_handler);
        editor.setEditorMouseEventHandler(e_mouse_event_handler);
        editor.setEditorDragEventHandler(e_drag_event_handler);
        editor.setEditorChangeEventHandler(e_change_event_handler);
        editor.generateUIComponents();
        viewer = new PDFXViewer();
        create_UIs_Menubar();
    }
    public void create_UIs_Menubar(){
        mb_event_handler = new MenubarEventHandler(this);
        
        stage_for_menubar = new Stage(StageStyle.TRANSPARENT);
        root_for_menubar = new StackPane();
        scene_for_menubar = new Scene(root_for_menubar,this.display_width,30);
        
        menubar = new MenuBar();
        menubar.setPrefHeight(30);
        this.root_for_menubar.getChildren().add(menubar);
        
        Menu menu_file = new Menu("File");
        MenuItem mitem_new = new MenuItem("New");mitem_new.setId("mitem_new");mitem_new.setOnAction(mb_event_handler);
        MenuItem mitem_new_from_pdf = new MenuItem("New from .pdf");mitem_new_from_pdf.setId("mitem_new_from_pdf");mitem_new_from_pdf.setOnAction(mb_event_handler);
        MenuItem mitem_open = new MenuItem("Open");mitem_open.setId("mitem_open");mitem_open.setOnAction(mb_event_handler);
        MenuItem mitem_save = new MenuItem("Save");mitem_save.setId("mitem_save");mitem_save.setOnAction(mb_event_handler);
        MenuItem mitem_saveas = new MenuItem("Save as");mitem_saveas.setId("mitem_save");mitem_saveas.setOnAction(mb_event_handler);
        MenuItem mitem_quit = new MenuItem("Quit");mitem_quit.setId("mitem_quit");mitem_quit.setOnAction(mb_event_handler);
        menu_file.getItems().addAll(mitem_new,mitem_new_from_pdf,mitem_open,mitem_save,mitem_saveas,mitem_quit);
        
        Menu menu_edit = new Menu("Edit");
        Menu menu_view = new Menu("View");
        MenuItem mitem_viewer = new MenuItem("View PDFX");mitem_viewer.setId("mitem_viewer");mitem_viewer.setOnAction(mb_event_handler);
        MenuItem mitem_set_fullscreen = new MenuItem("Full Screen Mode");mitem_set_fullscreen.setId("mitem_set_fullscreen");mitem_set_fullscreen.setOnAction(mb_event_handler);
        menu_view.getItems().addAll(mitem_viewer,mitem_set_fullscreen);
        Menu menu_presentation = new Menu("Presentation");
        Menu menu_help = new Menu("Help");
        
        menubar.getMenus().addAll(menu_file,menu_edit,menu_view,menu_presentation,menu_help);
        
        stage_for_menubar.setX(0);
        stage_for_menubar.setY(20);
        
        stage_for_menubar.setScene(scene_for_menubar);
        stage_for_menubar.show();
    }
    public void file_generated(String project_name){
        this.pdfx_pages = new ArrayList<>();
        viewer.setPdfxPages(pdfx_pages);
        
        this.displayed_page_index = 0;
        
        this.project_name = project_name;
        Page page = new Page(project_name,0);
        this.pdfx_pages.add(page);
        
        pageEdited();
    }
    public void file_opened(){
        this.pdfx_pages = new ArrayList<>();
        //viewer.setPdfxPages(pdfx_pages);
        //this.displayed_page_index = 0;
    }
    public void addPage(){
        this.displayed_page_index = this.pdfx_pages.size();
        Page page = new Page(project_name,displayed_page_index);
        this.pdfx_pages.add(page);
        
        pageEdited();
    }
    private void pageEdited(){
        this.editor.pageEdited(pdfx_pages,this.displayed_page_index);
    }
    public void pageSelected(int index){
        this.editor.pageSelected(index);
    }
    public void file_opened(File opened_file){
        
    }
    public void viewFullScreen() {
        
        primaryStage.setFullScreen(true);
        editor.resize(display_width, display_height);
        
    }
    @Override
    public void start(final Stage primaryStage) {
        //System.setProperty("java.awt.headless", "true");
        try {
    Field defaultHeadlessField = java.awt.GraphicsEnvironment.class.getDeclaredField("defaultHeadless");
    defaultHeadlessField.setAccessible(true);
    defaultHeadlessField.set(null,Boolean.FALSE);
    Field headlessField = java.awt.GraphicsEnvironment.class.getDeclaredField("headless");
    headlessField.setAccessible(true);
    headlessField.set(null,Boolean.TRUE);
} catch (IllegalAccessException e) {
    e.printStackTrace();
} catch (NoSuchFieldException e) {
    e.printStackTrace();
}
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        System.out.println("HeadlessMode:"+ge.isHeadless());
        this.initialize();
        
        
        this.primaryStage = primaryStage;
        
        root = new StackPane();
        root.getChildren().add(editor);
        
        scene = new Scene(root,this.display_width*3/5,this.display_height*3/5,true);
        
        primaryStage.setX(this.display_width/2-scene.getWidth()/2);
        primaryStage.setY(display_height/2-scene.getHeight()/2);
        
        scene.setCamera(new PerspectiveCamera());
        
        //primaryStage.initStyle(StageStyle.UNDECORATED);     
        primaryStage.setTitle("PDFXNote");
        primaryStage.setScene(scene);
        
        this.main_window_x = (int) primaryStage.getX();
        this.main_window_y = (int) primaryStage.getY();
        this.main_window_width = (int) scene.getWidth();
        this.main_window_height = (int) scene.getHeight();
                
        primaryStage.widthProperty().addListener(new ChangeListener(){

            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                main_window_width = (int) primaryStage.getWidth();
                main_window_height = (int) primaryStage.getHeight();
                editor.resize(main_window_width, main_window_height);
            }
        });
        primaryStage.heightProperty().addListener(new ChangeListener(){

            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                main_window_width = (int) primaryStage.getWidth();
                main_window_height = (int) primaryStage.getHeight();
                editor.resize(main_window_width, main_window_height);
            }
        });
        
        primaryStage.show();
        editor.resize(main_window_width, main_window_height);
    }
    public static void main(String[] args) {
        launch(args);
    }

    public void dragDetectedOfComponent(String item_id) {
        this.drag_detected_component = item_id;
    }

    public void itemDragged() {
        this.editor.itemDragged(this.drag_detected_component);
    }

    public void addPdfPartComponentToPage() {
        this.editor.addPdfPartComponentToPage();
    }

    public void addFxPartComponentToPage() {
        this.editor.addFxPartComponentToPage();
    }

    public void componentSelected(String newValue) {
        this.editor.componentSelected(newValue);
    }

    void editPdfPartComponentOnPage() {
        this.editor.editPdfPartComponentOnPage();
    }

    void editFxPartComponentOnPage() {
        this.editor.editFxPartComponentOnPage();
    }

    void deletePdfPartComponentFromPage() {
        this.editor.deletePdfPartComponentFromPage();
    }

    void deleteFxPartComponentFromPage() {
        this.editor.deleteFxPartComponentFromPage();
    }

    void saveFile() throws TransformerConfigurationException, TransformerException {
        if(file == null){
            this.selectFile(PDFXNote.SAVE_MODE);
        }
        try {
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            
            System.out.println(project_name);
            Element rootElement = doc.createElement(this.project_name);
            doc.appendChild(rootElement);

            for (int i = 0; i < pdfx_pages.size(); i++) {
                Element pageElement = pdfx_pages.get(i).createXmlElement(doc,doc.createElement("Page"));
                rootElement.appendChild(pageElement);
            }
            try {
                TransformerFactory transFactory = TransformerFactory.newInstance();
                Transformer transformer = transFactory.newTransformer();
                
                DOMSource source = new DOMSource(doc);
                FileOutputStream os = new FileOutputStream(file);
                StreamResult result = new StreamResult(os);
                transformer.transform(source, result);
                
            } catch (IOException ex) {
                Logger.getLogger(PDFXNote.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(PDFXNote.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void selectFile(int mode) {
        FileChooser fc = new FileChooser();
        fc.setTitle("select file");
        //fc.setInitialDirectory(new File(System.getProperty("user.home")));
        if(mode == PDFXNote.SAVE_MODE){
            fc.getExtensionFilters().add(new ExtensionFilter("PDFX", "*.pdfx"));
            file = fc.showSaveDialog(this.stage_for_menubar);
        }
        else if(mode == PDFXNote.OPEN_MODE){
            fc.getExtensionFilters().add(new ExtensionFilter("PDFX", "*.pdfx"));
            file = fc.showOpenDialog(this.stage_for_menubar);
        }
        else if(mode == PDFXNote.OPEN_MODE){
            fc.getExtensionFilters().add(new ExtensionFilter("PDF", "*.pdf"));
            file = fc.showOpenDialog(this.stage_for_menubar);
        }
        


    }

    
    
    
    void openFile() {
        try {
            this.file_opened();
            
            this.selectFile(OPEN_MODE);
            DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = fact.newDocumentBuilder();
            Document doc = builder.parse(file);
            
            Element rootElement = (Element) doc.getChildNodes().item(0);
            this.project_name = rootElement.getTagName();
            System.out.println(this.project_name);
            for(int i = 0;i<rootElement.getChildNodes().getLength();i++){
                System.out.println(rootElement.getChildNodes().item(i).toString()+"generate page number "+i);
                Page page = Page.createPage(rootElement.getChildNodes().item(i),project_name,i);
            this.pdfx_pages.add(page);
            }

            this.pageEdited();
            
        } catch (SAXException | IOException | ParserConfigurationException ex) {
            Logger.getLogger(PDFXNote.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void selectPdfFile() {
        
    }

    void presentation() {
        editor.close();
        this.viewer.setStage(this.primaryStage);
        viewer.setPdfxPages(this.editor.pdfx_pages);
        this.viewer.viewFirstPage();
        
    }
}
