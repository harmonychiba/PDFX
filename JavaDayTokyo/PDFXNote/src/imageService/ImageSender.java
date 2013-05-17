/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package imageService;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;

/**
 *
 * @author Takafumi
 */
public class ImageSender {

    public String servlet_ip;

    public ImageSender() {
        this.servlet_ip = "";
    }

    public void ImageSend(BufferedImage image) {
        if (servlet_ip.isEmpty()) {
            return;
        }
        try {
            URL url = new URL("http://harmonychiba.ddo.jp/PDFXServer/ImageServer");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "image/jpeg");
            con.setRequestMethod("POST");
            try (OutputStream ost = con.getOutputStream()) {
                if (image == null) {
                    System.out.println("image is null");
                    return;
                }
                ImageIO.write(image, "png", ost);
            }

            System.out.println("finish posting");
            
             InputStreamReader ir1 = new InputStreamReader(con.getInputStream());
             BufferedReader br1 = new BufferedReader(ir1);

             //	１行ずつ書き出す
             String line;
             while ((line = br1.readLine()) != null) {
             //改行がカットされてるので、printlnになる。
             System.out.println(line);
             }
             
            //	クローズ
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
