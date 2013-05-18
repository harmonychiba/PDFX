/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package imageService;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.imageio.ImageIO;
import jp.co.ricoh.pjs.pcscreen.RNCBHeader;
import org.apache.commons.httpclient.ChunkedOutputStream;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.protocol.Protocol;

/**
 *
 * @author y_chiba
 */
public class Projector {

    public static final int AUTO = 0;
    public static final int MANUAL = 1;
    private String host_ip = "169.254.101.206";
    private String host_url;
    private String SCHEME_HTTP = "http://";
    private String PORT = "80";
    private ChunkedOutputStream cos;
    private RNCBHeader rncb;
    private HttpConnection conn;
    private OutputStream os;
    private ImageSender sender;
    private boolean setIP = false;
    private boolean initialized;

    public void setSender(ImageSender sender) {
        this.sender = sender;
    }
    
    
    public String postCreateMethod(boolean exclusive, int update_method) throws InterruptedException {
        if(setIP == false){
            return "";
        }
        System.out.println("IP set");
        this.host_url = SCHEME_HTTP + host_ip;
        HttpClient httpclient = new HttpClient();
        HttpMethod http = null;
        http = new PostMethod(host_url + ":" + PORT + "/service/projection");
        try {
            System.out.println(http.getURI());
        } catch (URIException ex) {
            ex.printStackTrace();
        }
        http.setRequestHeader("Content-Type", "application/json");
        RequestEntity entity = null;
        String exclusive_text = "off";
        if (exclusive) {
            exclusive_text = "off";
        }
        try {
            entity = new StringRequestEntity("{\"exclusive\":\"" + exclusive_text + "\"}", "application/json", null);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (entity != null) {
            ((EntityEnclosingMethod) http).setRequestEntity(entity);
        }
        try {
            httpclient.executeMethod(http);
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(http.getURI());
        } catch (URIException ex) {
            ex.printStackTrace();
        }

        Header[] headerList = http.getResponseHeaders();

        String targetUrl = "";

        for (Header header : headerList) {
            if (header.getName().equals("Location")) {
                targetUrl = header.getValue();
                break;
            }
        }
        http.releaseConnection();
        this.conn = new HttpConnection(host_ip, Integer.parseInt(PORT), Protocol.getProtocol("http"));
        try {
            conn.open();
            int index = targetUrl.indexOf(host_url);
            index += (host_url).length();
            String projetor_resource = targetUrl.substring(index);
            System.out.println("projetor_resource: " + projetor_resource);
            conn.printLine("PUT " + projetor_resource + " " + "HTTP/1.1", "utf-8");
            conn.printLine("Host:" + " "+host_ip, "utf-8");
            conn.printLine("Content-Type:" + " video/x-rncb", "utf-8");
            conn.printLine("Transfer-Encoding:" + " chunked", "utf-8");
            //conn.printLine("Content-Type:" + "image/jpeg",CrLf);
            conn.printLine();
            conn.flushRequestOutputStream();
            os = conn.getRequestOutputStream();
            this.cos = new ChunkedOutputStream(os);
            this.rncb = new RNCBHeader();

        } catch (IOException e) {
            e.printStackTrace();
        }
        this.initialized = true;
        System.out.println(http.getStatusLine());
        return "" + http.getStatusLine();
    }

    public String putChunkWithImage(BufferedImage image) {
        if(this.setIP != true){
            return "";
        }
        System.out.println("call");
        if(this.initialized != true){
            try {
                this.postCreateMethod(false, 0);
            } catch (InterruptedException ex) {
                Logger.getLogger(Projector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (image != null) {
            try {
                ByteArrayOutputStream byte_array_stream = new ByteArrayOutputStream();
                //image = ImageIO.read(new File("/Users/Takafumi/Documents/JavaDayTokyo/JavaDayTokyo/vol8_pic16.jpg"));
                ImageIO.write(image, "jpg", byte_array_stream);
                if(rncb == null){
                    System.out.println("false");
                    return "failed";
                }
                System.out.println(byte_array_stream.size());
                byte[] out_array = this.rncb.createRNCBHeader(byte_array_stream);
                System.out.println(out_array.length);
                
                cos.write(out_array);
                
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("false2");
                return "fail";
            }
        }
        
        return null;
    }

    public void deleteMethod() {
    }

    public boolean requestSettingWithString(String name) {
        return false;
    }

    public Class getClassOfRequestedObject() {
        return null;
    }

    public Object getRequestedSetting() {
        return null;
    }

    public boolean setSettingOfString(String name, String value) {
        return false;
    }
    public void pleaseIp(){
        
        final Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        //final JFrame stage = new JFrame();
        StackPane root = new StackPane();
        //JPanel root = new JPanel();
        Scene scene = new Scene(root,300,200, Color.SKYBLUE);
        stage.setScene(scene);
        
        VBox vbox = new VBox();vbox.autosize();
        //root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        
        Text text = new Text("Please input Projecter IP.");
        //JLabel text = new JLabel("Please input Projector IP.");
        
        final TextField name_field = new TextField(host_ip);name_field.setPrefHeight(100);
        //final JTextField projector_ip_f = new JTextField();
        
        Text text_1 = new Text("Please input Servlet IP.");
        final TextField name_field_1 = new TextField("localhost");name_field_1.setPrefHeight(100);
        
        //JLabel text_1 = new JLabel("Please input Servlet IP.");
        
        //final TextField name_field = new TextField(host_ip);name_field.setPrefHeight(100);
        //final JTextField servlet_ip_f = new JTextField();
        HBox buttons = new HBox();buttons.setPrefHeight(100);
        Button OK_button = new Button("OK");
        OK_button.setPrefWidth(100);
        Button cancel_button = new Button("Cancel");cancel_button.setPrefWidth(100);
        
        OK_button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                host_ip = name_field_1.getText();
                sender.servlet_ip = "set";
                stage.close();
                setIP = true;
            }
        });
        /*K_button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                host_ip = projector_ip_f.getText();
                sender.servlet_ip = servlet_ip_f.getText();
                stage.setVisible(false);
                setIP = true;
            }
            });
        */
        cancel_button.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent e) {
                stage.hide();
                setIP = true;
            }
        });
        buttons.getChildren().addAll(OK_button,cancel_button);
        stage.setScene(scene);
        scene.setRoot(root);
        root.getChildren().addAll(text,name_field,text_1,name_field_1,buttons);
        stage.show();
    }
}