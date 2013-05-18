package pdfxnote;

import Animation.Animable;
import Animation.Animation;
import Animation.AnimationEvent;
import FxPartComponent.*;
import PdfPartComponent.PdfPartComponent;
import PdfPartComponent.PpParagraph;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PDFXEditor extends HBox {

    public final static float PAGE_PREVIEW_SECTION_WIDTH_RATE = 0.12f;
    public final static float MAIN_EDITOR_SECTION_WIDTH_RATE = 0.68f;
    public final static float OBJECT_EDITOR_SECTION_WIDTH_RATE = 0.2f;
    public final static float PARAMETER_EDITOR_SECTION_HEIGHT_RATE = 0.5f;
    public final static float OBJECT_SELECTOR_SECTION_HEIGHT_RATE = 0.5f;
    
    public static int OBJECT_SELECTION_STATE = 0;
    public static int CAUSE_SELECTION_STATE = 1;
    public static int SELECTION_LOCK_STATE = 2;
    
    ArrayList<Page> pdfx_pages;
    int displayed_page;
    PdfPartComponent pp_component;
    FxPartComponent fp_component;
    private EditorActionEventHandler eaehandler;
    private EditorMouseEventHandler emehandler;
    private EditorDragEventHandler edehandler;
    private EditorChangeEventHandler ecehandler;
    private ScrollPane page_preview_scrp;
    private ScrollPane main_editor_scrp;
    private Group main_editor_apage;
    private VBox object_editor_vert_box;
    private ScrollPane parameter_editor_scrp;
    private Button parameter_editor_add_pp_button;
    private Button parameter_editor_add_fp_button;
    private Button parameter_editor_edit_pp_button;
    private Button parameter_editor_edit_fp_button;
    private Button parameter_editor_delete_pp_button;
    private Button parameter_editor_delete_fp_button;
    private ScrollPane object_selector_scrp;
    private VBox page_preview_box;
    private Button page_preview_add_button;
    private VBox object_selector_vbox;
    private Group frame_group;
    Stage component_list_stage;
    Scene component_list_scene;
    StackPane component_list_root;
    ScrollPane component_list_scrp;
    ListView<String> component_list;
    
    Stage animation_editor_stage;
    Scene animation_editor_scene;
    StackPane animation_editor_root;
    HBox animation_editor_box;
    ScrollPane animation_editor_tree_scr;
    TreeView animation_editor_tree;
    VBox animation_editor_detail_box;
    
    int animation_editor_selection_state;

    public PDFXEditor() {
        page_preview_scrp = new ScrollPane();
        page_preview_scrp.setBlendMode(BlendMode.SRC_OVER);
        page_preview_box = new VBox();
        page_preview_box.setBlendMode(BlendMode.SRC_OVER);
        page_preview_add_button = new Button();
        main_editor_scrp = new ScrollPane();
        main_editor_scrp.setBlendMode(BlendMode.SRC_ATOP);
        main_editor_apage = new Group();
        main_editor_scrp.setContent(main_editor_apage);
        object_editor_vert_box = new VBox();
        parameter_editor_scrp = new ScrollPane();
        parameter_editor_add_pp_button = new Button("Add Component");
        parameter_editor_add_fp_button = new Button("Add Component");
        parameter_editor_edit_pp_button = new Button("Edit Component");
        parameter_editor_edit_fp_button = new Button("Edit Component");
        parameter_editor_delete_pp_button = new Button("Delete Component");
        parameter_editor_delete_fp_button = new Button("Delete Component");
        object_selector_scrp = new ScrollPane();
        object_selector_vbox = new VBox();
        frame_group = new Group();
        
        animation_editor_stage = new Stage();
        animation_editor_root = new StackPane();
        animation_editor_scene = new Scene(animation_editor_root,700,500);
        animation_editor_box = new HBox();
        animation_editor_tree_scr = new ScrollPane();
        animation_editor_tree = new TreeView();
        animation_editor_detail_box  = new VBox();
        
        component_list_stage = new Stage();//StageStyle.TRANSPARENT);
        component_list_root = new StackPane();
        component_list_scene = new Scene(component_list_root, 200, 300);
        component_list_scrp = new ScrollPane();
        component_list = new ListView<>();
        component_list.setItems(null);
        component_list_root.getChildren().add(component_list_scrp);
        component_list_scrp.setContent(component_list);
        component_list_stage.setScene(component_list_scene);
        component_list_stage.setX(100);
        component_list_stage.show();
    }

    public void generateUIComponents() {
        page_preview_scrp.setContent(page_preview_box);

        object_editor_vert_box.getChildren().addAll(parameter_editor_scrp, object_selector_scrp);
        this.getChildren().addAll(page_preview_scrp, main_editor_scrp, object_editor_vert_box);

        page_preview_add_button.setText("Add new page");
        page_preview_add_button.setId("page_preview_add_button");
        page_preview_add_button.setOnAction(this.eaehandler);

        parameter_editor_scrp.setId("Parameter Editor");
        parameter_editor_scrp.setOnDragOver(this.edehandler);
        parameter_editor_scrp.setOnDragDropped(edehandler);
        parameter_editor_add_pp_button.setId("parameter_editor_add_pp_button");
        parameter_editor_add_pp_button.setOnAction(eaehandler);
        parameter_editor_add_fp_button.setId("parameter_editor_add_fp_button");
        parameter_editor_add_fp_button.setOnAction(eaehandler);
        parameter_editor_edit_pp_button.setId("parameter_editor_edit_pp_button");
        parameter_editor_edit_pp_button.setOnAction(eaehandler);
        parameter_editor_edit_fp_button.setId("parameter_editor_edit_fp_button");
        parameter_editor_edit_fp_button.setOnAction(eaehandler);
        parameter_editor_delete_pp_button.setId("parameter_editor_delete_pp_button");
        parameter_editor_delete_pp_button.setOnAction(eaehandler);
        parameter_editor_delete_fp_button.setId("parameter_editor_delete_fp_button");
        parameter_editor_delete_fp_button.setOnAction(eaehandler);
        

        object_selector_scrp.setContent(object_selector_vbox);

        createObjectList();
        createAnimationEditor();
    }
    private void createObjectList() {
        Label label_PpParagraph = new Label("Paragraph      PdfPart");
        Image ppLabelImage = new Image("resources/text.jpeg");
        ImageView ppLabelImageView = new ImageView(ppLabelImage);
        ppLabelImageView.setFitWidth(50);
        ppLabelImageView.setFitHeight(50);
        label_PpParagraph.setGraphic(ppLabelImageView);
        label_PpParagraph.setId("ComponentPpParagraph");
        label_PpParagraph.setOnDragDetected(emehandler);
        object_selector_vbox.getChildren().addAll(label_PpParagraph);
        
        Separator separator = new Separator(Orientation.HORIZONTAL);
        separator.setPrefHeight(30);
        object_selector_vbox.getChildren().add(separator);
        
        Label label_FxLabel = new Label("Label      FxPart");
        Image fpLabelImage = new Image("resources/text.jpeg");
        ImageView fpLabelImageView = new ImageView(fpLabelImage);
        fpLabelImageView.setFitWidth(50);
        fpLabelImageView.setFitHeight(50);
        label_FxLabel.setGraphic(fpLabelImageView);
        label_FxLabel.setId("ComponentFxLabel");
        label_FxLabel.setOnDragDetected(emehandler);
        object_selector_vbox.getChildren().addAll(label_FxLabel);
        
        Label label_FxTable = new Label("Table      FxPart");
        Image fpTableImage = new Image("resources/table.jpeg");
        ImageView fpTableImageView = new ImageView(fpTableImage);
        fpTableImageView.setFitWidth(50);
        fpTableImageView.setFitHeight(50);
        label_FxTable.setGraphic(fpTableImageView);
        label_FxTable.setId("ComponentFxTable");
        label_FxTable.setOnDragDetected(emehandler);
        object_selector_vbox.getChildren().addAll(label_FxTable);
        
        Label label_FxCircle = new Label("Circle      FxPart");
        Image fpCircleImage = new Image("resources/circle.jpeg");
        ImageView fpCircleImageView = new ImageView(fpCircleImage);
        fpCircleImageView.setFitWidth(50);
        fpCircleImageView.setFitHeight(50);
        label_FxCircle.setGraphic(fpCircleImageView);
        label_FxCircle.setId("ComponentFxCircle");
        label_FxCircle.setOnDragDetected(emehandler);
        object_selector_vbox.getChildren().addAll(label_FxCircle);
        
        Label label_FxPdfPageImage = new Label("PdfPagePanel      FxPart");
        Image fpPdfImage = new Image("resources/pdf.jpeg");
        ImageView fpPdfImageView = new ImageView(fpPdfImage);
        fpPdfImageView.setFitWidth(50);
        fpPdfImageView.setFitHeight(50);
        label_FxPdfPageImage.setGraphic(fpPdfImageView);
        label_FxPdfPageImage.setId("ComponentFxPdfPagePanel");
        label_FxPdfPageImage.setOnDragDetected(emehandler);
        object_selector_vbox.getChildren().addAll(label_FxPdfPageImage);
        
        Label label_FxMoviePlayer = new Label("MoviePlayer      FxPart");
        Image fpMovieImage = new Image("resources/movie.jpeg");
        ImageView fpMovieImageView = new ImageView(fpMovieImage);
        fpMovieImageView.setFitWidth(50);
        fpMovieImageView.setFitHeight(50);
        label_FxMoviePlayer.setGraphic(fpMovieImageView);
        label_FxMoviePlayer.setId("ComponentFxMoviePlayer");
        label_FxMoviePlayer.setOnDragDetected(emehandler);
        object_selector_vbox.getChildren().addAll(label_FxMoviePlayer);
        
        Label label_FxPolygon = new Label("Polygon      FxPart");
        Image fpPolygonImage = new Image("resources/polygon.jpeg");
        ImageView fpPolygonImageView = new ImageView(fpPolygonImage);
        fpPolygonImageView.setFitWidth(50);
        fpPolygonImageView.setFitHeight(50);
        label_FxPolygon.setGraphic(fpPolygonImageView);
        label_FxPolygon.setId("ComponentFxPolygon");
        label_FxPolygon.setOnDragDetected(emehandler);
        object_selector_vbox.getChildren().addAll(label_FxPolygon);
        
        Label label_fxLine = new Label("Line       FxPart");
        Image fpLineImage = new Image("resources/line.jpeg");
        ImageView fpLineImageView = new ImageView(fpLineImage);
        fpLineImageView.setFitWidth(50);
        fpLineImageView.setFitHeight(50);
        label_fxLine.setGraphic(fpLineImageView);
        label_fxLine.setId("ComponentFxLine");
        label_fxLine.setOnDragDetected(emehandler);
        object_selector_vbox.getChildren().addAll(label_fxLine);
        
        Label label_FxImage = new Label("Image      FxPart");
        Image fpPictureImage = new Image("resources/picture.jpeg");
        ImageView fpPictureImageView = new ImageView(fpPictureImage);
        fpPictureImageView.setFitWidth(50);
        fpPictureImageView.setFitHeight(50);
        label_FxImage.setGraphic(fpPictureImageView);
        label_FxImage.setId("ComponentFxImage");
        label_FxImage.setOnDragDetected(emehandler);
        object_selector_vbox.getChildren().addAll(label_FxImage);
        
        Label label_FxGraph = new Label("Graph      FxPart");
        Image fpGraphImage = new Image("resources/chart.jpeg");
        ImageView fpGraphImageView = new ImageView(fpGraphImage);
        fpGraphImageView.setFitWidth(50);
        fpGraphImageView.setFitHeight(50);
        label_FxGraph.setGraphic(fpGraphImageView);
        label_FxGraph.setId("ComponentFxGraph");
        label_FxGraph.setOnDragDetected(emehandler);
        object_selector_vbox.getChildren().addAll(label_FxGraph);
        
        Label label_FxBrowser = new Label("Browser      FxPart");
        Image fpBrowserImage = new Image("resources/browser.jpeg");
        ImageView fpBrowserImageView = new ImageView(fpBrowserImage);
        fpBrowserImageView.setFitWidth(50);
        fpBrowserImageView.setFitHeight(50);
        label_FxBrowser.setGraphic(fpBrowserImageView);
        label_FxBrowser.setId("ComponentFxBrowser");
        label_FxBrowser.setOnDragDetected(emehandler);
        object_selector_vbox.getChildren().addAll(label_FxBrowser);
    }

    public void resize(int width, int height) {
        System.out.println(width + "," + height);
        this.setPrefSize(width, height);

        page_preview_scrp.setPrefSize(PAGE_PREVIEW_SECTION_WIDTH_RATE * width, height);
        page_preview_scrp.setHmax(this.page_preview_scrp.widthProperty().getValue());
        page_preview_box.setPrefSize(PAGE_PREVIEW_SECTION_WIDTH_RATE * width * 0.9, height * 0.9);

        main_editor_scrp.setPrefSize(MAIN_EDITOR_SECTION_WIDTH_RATE * width, height);

        object_editor_vert_box.setPrefSize(OBJECT_EDITOR_SECTION_WIDTH_RATE * width, height);

        parameter_editor_scrp.setPrefSize(object_editor_vert_box.getPrefWidth(), PARAMETER_EDITOR_SECTION_HEIGHT_RATE * height);

        object_selector_scrp.setPrefSize(object_editor_vert_box.getPrefWidth(), OBJECT_SELECTOR_SECTION_HEIGHT_RATE * height);
    }

    public void pageEdited(ArrayList<Page> pdfx_pages, int displayed_page_index) {
        this.pdfx_pages = pdfx_pages;
        this.displayed_page = displayed_page_index;

        reloadMainWindow();
        createSumbnails();
        
        settingUpAnimationEditor();
    }

    private void reloadMainWindow() {
        for (int i = 0; i < pdfx_pages.size(); i++) {
            pdfx_pages.get(i).destoryImage();
        }
        this.main_editor_apage.getChildren().clear();
        //this.main_editor_apage.getChildren().add(pdfx_pages.get(this.displayed_page).getPdfImage());
        this.main_editor_apage.getChildren().add(pdfx_pages.get(displayed_page));
        pdfx_pages.get(displayed_page).loaded();
        pdfx_pages.get(displayed_page).prepareForImage();
    }

    private void createSumbnails() {
        this.page_preview_box.getChildren().clear();

        for (int i = 0; i < this.pdfx_pages.size(); i++) {
            Text page_number_text = new Text(i + "");
            ImageView page_prev_image_view = pdfx_pages.get(i).getSubPdfImage();
            float size_rate = (float) (this.page_preview_scrp.getPrefWidth() / page_prev_image_view.getImage().getWidth());
            page_prev_image_view.setFitWidth(page_prev_image_view.getImage().getWidth() * size_rate);
            page_prev_image_view.setFitHeight(page_prev_image_view.getImage().getHeight() * size_rate);
            page_prev_image_view.setId("page_prev_image_view" + i);
            page_prev_image_view.setOnMouseClicked(this.emehandler);
            this.page_preview_box.getChildren().addAll(page_number_text, page_prev_image_view);
        }
        page_preview_box.getChildren().add(page_preview_add_button);

    }

    public void setEditorActionEventHandler(EditorActionEventHandler e_event_handler) {
        this.eaehandler = e_event_handler;
    }

    public void setEditorMouseEventHandler(EditorMouseEventHandler e_event_handler) {
        this.emehandler = e_event_handler;
    }

    public void setEditorDragEventHandler(EditorDragEventHandler e_event_handler) {
        this.edehandler = e_event_handler;
    }
    void setEditorChangeEventHandler(EditorChangeEventHandler e_event_handler) {
        this.ecehandler = e_event_handler;
        component_list.getSelectionModel().selectedItemProperty().addListener(ecehandler);
    }

    void pageSelected(int index) {
        System.out.println("index sent");
        this.displayed_page = index;
        reloadMainWindow();
        createSumbnails();
        this.createComponentListOfPage();
    }

    void itemDragged(String drag_detected_component) {
        System.out.println(drag_detected_component);
        parameter_editor_scrp.setContent(null);
        if (drag_detected_component.equalsIgnoreCase("ComponentPpParagraph")) {
            pp_component = new PpParagraph();
            pp_component.Id+=this.pdfx_pages.get(displayed_page).getCountOfPdfPartComponent();
            pp_component.generateParameterEditor();
            pp_component.getParameter_editor().setFillWidth(true);
            this.parameter_editor_scrp.setContent(pp_component.getParameter_editor());
            pp_component.getParameter_editor().getChildren().add(parameter_editor_add_pp_button);
        }
        
        else if(drag_detected_component.equalsIgnoreCase("ComponentFxLabel")){
            fp_component = new FpLabel();
            fp_component.id+=this.pdfx_pages.get(displayed_page).getCountOfFxPartComponent();
            fp_component.generateParameterEditor();
            fp_component.getParameterEditor().setFillWidth(true);
            this.parameter_editor_scrp.setContent(fp_component.getParameterEditor());
            fp_component.getParameterEditor().getChildren().add(parameter_editor_add_fp_button);
        }
        
        else if(drag_detected_component.equalsIgnoreCase("ComponentFxTable")){
            fp_component = new FpTable();
            fp_component.id+=this.pdfx_pages.get(displayed_page).getCountOfFxPartComponent();
            fp_component.generateParameterEditor();
            fp_component.getParameterEditor().setFillWidth(true);
            this.parameter_editor_scrp.setContent(fp_component.getParameterEditor());
            fp_component.getParameterEditor().getChildren().add(parameter_editor_add_fp_button);
        }
        
        else if(drag_detected_component.equalsIgnoreCase("ComponentFxCircle")){
            fp_component = new FpCircle();
            fp_component.id+=this.pdfx_pages.get(displayed_page).getCountOfFxPartComponent();
            fp_component.generateParameterEditor();
            fp_component.getParameterEditor().setFillWidth(true);
            this.parameter_editor_scrp.setContent(fp_component.getParameterEditor());
            fp_component.getParameterEditor().getChildren().add(parameter_editor_add_fp_button);
        }
        
        else if(drag_detected_component.equalsIgnoreCase("ComponentFxPdfPagePanel")){
            fp_component = new FpPdfPagePanel();
            fp_component.id+=this.pdfx_pages.get(displayed_page).getCountOfFxPartComponent();
            fp_component.generateParameterEditor();
            fp_component.getParameterEditor().setFillWidth(true);
            this.parameter_editor_scrp.setContent(fp_component.getParameterEditor());
            fp_component.getParameterEditor().getChildren().add(parameter_editor_add_fp_button);
        }
        else if(drag_detected_component.equalsIgnoreCase("ComponentFxMoviePlayer")){
            fp_component = new FpMoviePlayer();
            fp_component.id+=this.pdfx_pages.get(displayed_page).getCountOfFxPartComponent();
            fp_component.generateParameterEditor();
            fp_component.getParameterEditor().setFillWidth(true);
            this.parameter_editor_scrp.setContent(fp_component.getParameterEditor());
            fp_component.getParameterEditor().getChildren().add(parameter_editor_add_fp_button);
        }
        else if(drag_detected_component.equalsIgnoreCase("ComponentFxPolygon")){
            fp_component = new FpPolygon();
            fp_component.id+=this.pdfx_pages.get(displayed_page).getCountOfFxPartComponent();
            fp_component.generateParameterEditor();
            fp_component.getParameterEditor().setFillWidth(true);
            this.parameter_editor_scrp.setContent(fp_component.getParameterEditor());
            fp_component.getParameterEditor().getChildren().add(parameter_editor_add_fp_button);
        }
        else if(drag_detected_component.equalsIgnoreCase("ComponentFxImage")){
            fp_component = new FpImage();
            fp_component.id+=this.pdfx_pages.get(displayed_page).getCountOfFxPartComponent();
            fp_component.generateParameterEditor();
            fp_component.getParameterEditor().setFillWidth(true);
            this.parameter_editor_scrp.setContent(fp_component.getParameterEditor());
            fp_component.getParameterEditor().getChildren().add(parameter_editor_add_fp_button);
        }
        else if(drag_detected_component.equalsIgnoreCase("ComponentFxLine")){
            fp_component = new FpLine();
            fp_component.id+=this.pdfx_pages.get(displayed_page).getCountOfFxPartComponent();
            fp_component.generateParameterEditor();
            this.fp_component.getParameterEditor().setFillWidth(true);
            this.parameter_editor_scrp.setContent(fp_component.getParameterEditor());
            fp_component.getParameterEditor().getChildren().add(parameter_editor_add_fp_button);
        }
        else if(drag_detected_component.equalsIgnoreCase("ComponentFxGraph")){
            fp_component = new FpGraph();
            fp_component.id+=this.pdfx_pages.get(displayed_page).getCountOfFxPartComponent();
            fp_component.generateParameterEditor();
            this.fp_component.getParameterEditor().setFillWidth(true);
            this.parameter_editor_scrp.setContent(fp_component.getParameterEditor());
            fp_component.getParameterEditor().getChildren().add(parameter_editor_add_fp_button);
        }
        else if(drag_detected_component.equalsIgnoreCase("ComponentFxBrowser")){
            fp_component = new FpBrowser();
            fp_component.id+=this.pdfx_pages.get(displayed_page).getCountOfFxPartComponent();
            fp_component.generateParameterEditor();
            this.fp_component.getParameterEditor().setFillWidth(true);
            this.parameter_editor_scrp.setContent(fp_component.getParameterEditor());
            fp_component.getParameterEditor().getChildren().add(parameter_editor_add_fp_button);
        }
    }

    public void addPdfPartComponentToPage() {
        pp_component.prepareForAdd();
        this.pdfx_pages.get(displayed_page).addPdfPartComponent(pp_component);
        System.out.println("add");
        pp_component = null;
        this.createComponentListOfPage();
    }
    public void addFxPartComponentToPage() {
        fp_component.generateComponent();
        this.pdfx_pages.get(displayed_page).addFxPartComponent(fp_component);
        fp_component.generateParameterEditor();
        this.parameter_editor_scrp.setContent(fp_component.getParameterEditor());
        fp_component.getParameterEditor().getChildren().add(parameter_editor_edit_fp_button);
        fp_component.getParameterEditor().getChildren().add(parameter_editor_delete_fp_button);
        if (frame_group.getParent() != null) {
            ((Page)(frame_group.getParent())).getChildren().remove(frame_group);
        }
        frame_group.getChildren().clear();
        frame_group.getChildren().add(fp_component.getFrameForComponent(FxPartComponent.FRAME_TYPE_MAIN_SELECTION));
        pdfx_pages.get(displayed_page).getChildren().add(frame_group);
        this.createComponentListOfPage();
        this.settingUpAnimationEditor();
        
        this.settingUpAnimationEditor();
    }

    private void createComponentListOfPage() {
        this.component_list.setItems(null);
        this.component_list.setItems(this.pdfx_pages.get(displayed_page).getItem_List());
    }
    public void componentSelected(String id){
        Object selected = pdfx_pages.get(displayed_page).getSelectedComponent(id);
        if(selected != null){
            if(selected.getClass().getSuperclass().equals(FxPartComponent.class)){
                fp_component = (FxPartComponent)selected;
                fp_component.generateParameterEditor();
                this.parameter_editor_scrp.setContent(fp_component.getParameterEditor());
                fp_component.getParameterEditor().getChildren().add(parameter_editor_edit_fp_button);
                fp_component.getParameterEditor().getChildren().add(parameter_editor_delete_fp_button);
                if(frame_group.getParent() != null) {
                    ((Page)(frame_group.getParent())).getChildren().remove(frame_group);
                }
                frame_group.getChildren().clear();
                frame_group.getChildren().add(fp_component.getFrameForComponent(FxPartComponent.FRAME_TYPE_MAIN_SELECTION));
                pdfx_pages.get(displayed_page).getChildren().add(frame_group);
            }
            else if(selected.getClass().getSuperclass().equals(PdfPartComponent.class)){
                pp_component = (PdfPartComponent)selected;
                pp_component.generateParameterEditor();
                this.parameter_editor_scrp.setContent(pp_component.getParameter_editor());
                pp_component.getParameter_editor().getChildren().add(parameter_editor_edit_pp_button);
                pp_component.getParameter_editor().getChildren().add(parameter_editor_delete_pp_button);
                if(frame_group.getParent() != null) {
                    ((Page)(frame_group.getParent())).getChildren().remove(frame_group);
                }
                frame_group.getChildren().clear();
                frame_group.getChildren().add(pp_component.getFrameForComponent(PdfPartComponent.FRAME_TYPE_MAIN_SELECTION));
                pdfx_pages.get(displayed_page).getChildren().add(frame_group);
            }
        }
    }

    void editPdfPartComponentOnPage() {
        pp_component.edit();
        this.pdfx_pages.get(displayed_page).editPdfPartComponent(pp_component);
    }

    void editFxPartComponentOnPage() {
        fp_component.regenerateComponent();
        this.pdfx_pages.get(displayed_page).editFxPartComponent(fp_component);
        settingUpAnimationEditor();
    }

    void deletePdfPartComponentFromPage() {
        this.pdfx_pages.get(displayed_page).deletePdfPartComponent(pp_component);
        this.parameter_editor_scrp.setContent(null);
    }

    void deleteFxPartComponentFromPage() {
        this.pdfx_pages.get(displayed_page).deteleFxPartComponent(fp_component);
        this.parameter_editor_scrp.setContent(null);
        settingUpAnimationEditor();
    }
    private void createAnimationEditor(){
        this.animation_editor_selection_state = OBJECT_SELECTION_STATE;
        
        animation_editor_stage.setScene(animation_editor_scene);
        animation_editor_stage.widthProperty().addListener(new ChangeListener(){

            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                animation_editor_tree_scr.setPrefViewportWidth(animation_editor_stage.getWidth()/2);
                animation_editor_tree_scr.setPrefViewportHeight(animation_editor_stage.getHeight());
                animation_editor_detail_box.setPrefSize(animation_editor_stage.getWidth()/2, animation_editor_stage.getHeight());
            }
        });
        animation_editor_stage.heightProperty().addListener(new ChangeListener(){

            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                animation_editor_tree_scr.setPrefViewportWidth(animation_editor_stage.getWidth()/2);
                animation_editor_tree_scr.setPrefViewportHeight(animation_editor_stage.getHeight());
                animation_editor_detail_box.setPrefSize(animation_editor_stage.getWidth()/2, animation_editor_stage.getHeight());
            }
        });
                
        animation_editor_root.getChildren().add(animation_editor_box);
        animation_editor_box.getChildren().addAll(animation_editor_tree_scr,animation_editor_detail_box);
        animation_editor_tree_scr.setContent(animation_editor_tree);
        
        animation_editor_stage.show();
    }
    private void settingUpAnimationEditor(){
        TreeItem<String> rootItem = new TreeItem<>(this.pdfx_pages.get(0).project_name);
        for(int i = 0;i<this.pdfx_pages.size();i++){
            TreeItem<String> pageItem = new TreeItem<>(this.pdfx_pages.get(i).getAnimableId());
            rootItem.getChildren().add(pageItem);
            AnimationTreeCell pageCell = new AnimationTreeCell(pdfx_pages.get(i).getAnimableId(),new int[]{i,0});
            pageItem.getChildren().add(pageCell);
            for(int j = 0;j<pdfx_pages.get(i).getAnimationList().size();j++){
                AnimationTreeCell animationCell = new AnimationTreeCell(this.pdfx_pages.get(i).getAnimationList().get(j).getAnimableId(),new int[]{i,j+1});
                pageItem.getChildren().add(animationCell);
            }
            for(int j = 0;j<pdfx_pages.get(i).getCountOfFxPartComponent();j++){
                TreeItem<String> componentItem = new TreeItem<>(this.pdfx_pages.get(i).getFx_part_components().get(j).getAnimableId());
                pageItem.getChildren().add(componentItem);
                AnimationTreeCell componentCell = new AnimationTreeCell(this.pdfx_pages.get(i).getFx_part_components().get(j).getAnimableId(), new int[]{i,j,0});
                componentItem.getChildren().add(componentCell);
                for(int k = 0;k<pdfx_pages.get(i).getFx_part_components().get(j).getAnimationList().size();k++){
                    AnimationTreeCell animationCell = new AnimationTreeCell(this.pdfx_pages.get(i).getFx_part_components().get(j).getAnimationList().get(k).getAnimableId(),new int[]{i,j,k+1});
                    componentItem.getChildren().add(animationCell);
                }
            }
        }
        animation_editor_tree.setRoot(rootItem);
        animation_editor_tree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                if(t1.getClass().equals(AnimationTreeCell.class)) {
                    
                    AnimationTreeCell clicked_cell = (AnimationTreeCell)t1;
                    clicked_cell.selected();
                    
                }
            }
        });
    }
    private void TreeItemClicked(String id_data,int[] position){
        Animable clickedItem = null;
        if(position.length == 2){
            if(position[1] == 0){
                clickedItem = this.pdfx_pages.get(position[0]);
            }
            else{
                clickedItem = this.pdfx_pages.get(position[0]).getAnimationList().get(position[1]-1);
            }
        }
        else if(position.length == 3){
            if(position[2] == 0){
                clickedItem = this.pdfx_pages.get(position[0]).getFx_part_components().get(position[1]);
            }
            else{
                clickedItem = this.pdfx_pages.get(position[0]).getFx_part_components().get(position[1]).getAnimationList().get(position[2]-1);
            }
        }
        if(clickedItem!=null) {
            System.out.println(id_data+","+clickedItem.getAnimableId()+","+this.animation_editor_selection_state);
            if(this.animation_editor_selection_state == PDFXEditor.OBJECT_SELECTION_STATE){
                this.createAnimationEditorDetailBox(clickedItem);
            }
            else if(this.animation_editor_selection_state == PDFXEditor.CAUSE_SELECTION_STATE){
                this.causeItemSelected(clickedItem);
            }
        }
    }

    private void createAnimationEditorDetailBox(Animable clickedItem) {
        this.animation_editor_selection_state = PDFXEditor.SELECTION_LOCK_STATE;
        animation_editor_detail_box.getChildren().clear();
        Label id_label = new Label("ID");
        final TextField idField = new TextField();
        Label objectLabel = new Label("Object Item");
        final TextField objectField = new TextField();
        objectField.setEditable(false);
        Label causeLabel = new Label("Cause Item");
        final TextField causeField = new TextField();
        causeField.setEditable(false);
        causeField.setId("causeField");
        final HBox causeButtons = new HBox();
        final Button settingButton = new Button("Setting Cause Item");
        Label timerLabel = new Label("Timer");
        final TextField timerField = new TextField();
        timerField.setPromptText("Timer by milli seconds");
        Label valueLabel = new Label("Value");
        final TextField valueField = new TextField();
        valueField.setPromptText("Property Value by float number");
        final CheckBox reverseCheck = new CheckBox("Auto Reverse");
        settingButton.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent t) {
                animation_editor_selection_state = PDFXEditor.CAUSE_SELECTION_STATE;
                final Button OK_button = new Button("OK");
                OK_button.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent t) {
                        causeButtons.getChildren().remove(OK_button);
                        causeButtons.getChildren().add(settingButton);
                        animation_editor_selection_state = PDFXEditor.SELECTION_LOCK_STATE;
                    }
                });
                causeButtons.getChildren().remove(settingButton);
                causeButtons.getChildren().add(OK_button);
            }
        });
        causeButtons.getChildren().add(settingButton);
        Label triggerTypeLabel = new Label("Trigger Type");
        final ComboBox<String> triggerTypeBox = new ComboBox();
        ObservableList<String> triggerList = FXCollections.observableArrayList("Loaded","Clicked","Finished","Others");
        triggerTypeBox.setItems(triggerList);
        triggerTypeBox.getSelectionModel().select(0);
        Label animationTypeLabel = new Label("Animation Type");
        final ComboBox<String> animationTypeBox = new ComboBox();
        ArrayList<String> typelist = new ArrayList<>();
        typelist.addAll(Arrays.asList(Animation.ANIMATION_TYPE_LIST));
        ObservableList<String> animationTypeList = FXCollections.observableArrayList(typelist);
        animationTypeBox.setItems(animationTypeList);
        animationTypeBox.getSelectionModel().select(0);
        
        HBox buttons = new HBox();
        Button ok_button = new Button("OK");
        Button cancel_button = new Button("Cancel");
        cancel_button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                animation_editor_selection_state = PDFXEditor.OBJECT_SELECTION_STATE;
                animation_editor_detail_box.getChildren().clear();
            }
        });
        buttons.getChildren().addAll(ok_button,cancel_button);
        
        this.animation_editor_detail_box.getChildren().addAll(id_label,idField,objectLabel,objectField,causeLabel,causeField,causeButtons,triggerTypeLabel,triggerTypeBox,animationTypeLabel,animationTypeBox,timerLabel,timerField,valueLabel,valueField,reverseCheck,buttons);

        if(clickedItem.getClass().equals(Page.class)){
            final Page clicked_page = (Page)clickedItem;
            idField.setText(clicked_page.getAnimableId()+"Animation"+clicked_page.getAnimationList().size());
            objectField.setText(clicked_page.getAnimableId());
            ok_button.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {

                    if (causeField.getText() != null && timerField.getText() != null && valueField.getText() != null) {

                        Animation animation = new Animation();
                        animation.id = idField.getText();
                        animation.setObject_item(clicked_page.getAnimableId());
                        animation.setCause_item(causeField.getText());
                        if (triggerTypeBox.getSelectionModel().getSelectedItem().equalsIgnoreCase("Loaded")) {
                            animation.setTrigger(AnimationEvent.LOADED);
                        } else if (triggerTypeBox.getSelectionModel().getSelectedItem().equalsIgnoreCase("Clicked")) {
                            animation.setTrigger(AnimationEvent.CLICKED);
                        } else if (triggerTypeBox.getSelectionModel().getSelectedItem().equalsIgnoreCase("Finished")) {
                            animation.setTrigger(AnimationEvent.FINISHED);
                        } else if (triggerTypeBox.getSelectionModel().getSelectedItem().equalsIgnoreCase("Other")) {
                            animation.setTrigger(AnimationEvent.OTHER);
                        }
                        animation.setAnimation_type(animationTypeBox.getSelectionModel().getSelectedIndex());
                        animation.setTimer(Long.parseLong(timerField.getText()));
                        animation.setValue(Double.parseDouble(valueField.getText()));
                        
                        animation.setAutorepeat(reverseCheck.isSelected());
                        
                        clicked_page.getAnimationList().add(animation);
                        animation.setParent(clicked_page);
                        
                        animation_editor_selection_state = PDFXEditor.OBJECT_SELECTION_STATE;
                        animation_editor_detail_box.getChildren().clear();
                        
                        settingUpAnimationEditor();
                    }
                }
            });
        }
        else if(clickedItem.getClass().getSuperclass().equals(FxPartComponent.class)){
            final FxPartComponent clicked_parts = (FxPartComponent)clickedItem;
            idField.setText(clicked_parts.getAnimableId()+"Animation"+clicked_parts.getAnimationList().size());
            objectField.setText(clicked_parts.getAnimableId());
            ok_button.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {

                    if (causeField.getText() != null && timerField.getText() != null && valueField.getText() != null) {

                        Animation animation = new Animation();
                        animation.id = idField.getText();
                        animation.setObject_item(clicked_parts.getAnimableId());
                        animation.setCause_item(causeField.getText());
                        if (triggerTypeBox.getSelectionModel().getSelectedItem().equalsIgnoreCase("Loaded")) {
                            animation.setTrigger(AnimationEvent.LOADED);
                        } else if (triggerTypeBox.getSelectionModel().getSelectedItem().equalsIgnoreCase("Clicked")) {
                            animation.setTrigger(AnimationEvent.CLICKED);
                        } else if (triggerTypeBox.getSelectionModel().getSelectedItem().equalsIgnoreCase("Finished")) {
                            animation.setTrigger(AnimationEvent.FINISHED);
                        } else if (triggerTypeBox.getSelectionModel().getSelectedItem().equalsIgnoreCase("Other")) {
                            animation.setTrigger(AnimationEvent.OTHER);
                        }
                        animation.setAnimation_type(animationTypeBox.getSelectionModel().getSelectedIndex());
                        animation.setTimer(Long.parseLong(timerField.getText()));
                        animation.setValue(Double.parseDouble(valueField.getText()));
                        animation.setAutorepeat(reverseCheck.isSelected());
                        clicked_parts.getAnimationList().add(animation);
                        animation.setParent(clicked_parts);
                        
                        animation_editor_selection_state = PDFXEditor.OBJECT_SELECTION_STATE;
                        animation_editor_detail_box.getChildren().clear();
                        
                        settingUpAnimationEditor();
                    }
                }
            });
        }
        else if(clickedItem.getClass().equals(Animation.class)){
            final Animation clicked_anim = (Animation)clickedItem;
            objectField.setText(clicked_anim.getAnimableId());
            
            idField.setText(clicked_anim.id);
            objectField.setText(clicked_anim.getObject_item());
            causeField.setText(clicked_anim.getCause_item());
            int index = 0;
            if(clicked_anim.getTrigger() == AnimationEvent.CLICKED){
                index = 1;
            }
            else if(clicked_anim.getTrigger() == AnimationEvent.FINISHED){
                index = 2;
            }
            else if(clicked_anim.getTrigger() == AnimationEvent.OTHER){
                index = 3;
            }
            triggerTypeBox.getSelectionModel().select(index);
            
            animationTypeBox.getSelectionModel().select(clicked_anim.getAnimation_type());
            
            timerField.setText(clicked_anim.getTimer()+"");
            valueField.setText(clicked_anim.getValue()+"");
            reverseCheck.setSelected(clicked_anim.isAutorepeat());
            ok_button.setText("Edit");
            Button deleteButton = new Button("Delete");
            buttons.getChildren().add(deleteButton);
            ok_button.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {

                    if (causeField.getText() != null && timerField.getText() != null && valueField.getText() != null) {

                        Animation animation = clicked_anim;
                        animation.id = idField.getText();
                        animation.setObject_item(objectField.getText());
                        animation.setCause_item(causeField.getText());
                        if (triggerTypeBox.getSelectionModel().getSelectedItem().equalsIgnoreCase("Loaded")) {
                            animation.setTrigger(AnimationEvent.LOADED);
                        } else if (triggerTypeBox.getSelectionModel().getSelectedItem().equalsIgnoreCase("Clicked")) {
                            animation.setTrigger(AnimationEvent.CLICKED);
                        } else if (triggerTypeBox.getSelectionModel().getSelectedItem().equalsIgnoreCase("Finished")) {
                            animation.setTrigger(AnimationEvent.FINISHED);
                        } else if (triggerTypeBox.getSelectionModel().getSelectedItem().equalsIgnoreCase("Other")) {
                            animation.setTrigger(AnimationEvent.OTHER);
                        }
                        animation.setAnimation_type(animationTypeBox.getSelectionModel().getSelectedIndex());
                        animation.setTimer(Long.parseLong(timerField.getText()));
                        animation.setValue(Double.parseDouble(valueField.getText()));
                        animation.setAutorepeat(reverseCheck.isSelected());
                        animation_editor_selection_state = PDFXEditor.OBJECT_SELECTION_STATE;
                        animation_editor_detail_box.getChildren().clear();
                        
                        settingUpAnimationEditor();
                    }
                }
            });
            deleteButton.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
                    clicked_anim.getParent().getAnimationList().remove(clicked_anim);
                    animation_editor_selection_state = PDFXEditor.OBJECT_SELECTION_STATE;
                    animation_editor_detail_box.getChildren().clear();
                        
                    settingUpAnimationEditor();
                }
            });
            
        }
        
    }

    private void causeItemSelected(Animable clickedItem) {
        System.out.println("clicked");
        for(int i = 0;i<this.animation_editor_detail_box.getChildren().size();i++){
            System.out.println(animation_editor_detail_box.getChildren().get(i).getId());
            if(animation_editor_detail_box.getChildren().get(i).getId()!=null) {
                if(animation_editor_detail_box.getChildren().get(i).getId().equalsIgnoreCase("causeField")){
                    ((TextField)(animation_editor_detail_box.getChildren().get(i))).setText(clickedItem.getAnimableId());
                }
            }
        }
    }

    void close() {
        this.main_editor_apage.getChildren().clear();
    }
    class AnimationTreeCell extends TreeItem<String>{
        String id_data;
        int[] position;
        AnimationTreeCell(final String id_data,final int[] position){
            super(id_data);
            this.id_data = id_data;
            this.position = position;
        }
        public void selected(){
            System.out.println("super");
            TreeItemClicked(id_data,position);
        }
    }
}