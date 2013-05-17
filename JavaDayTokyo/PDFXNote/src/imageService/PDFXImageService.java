/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package imageService;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;

/**
 *
 * @author chibayuuki
 */
public class PDFXImageService {
    static JChannel channel;
    static Address pdfx_address;
    static Projector projector;
    static ImageSender sender;
    
    static Robot robot;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        projector = new Projector();
        sender = new ImageSender();
        projector.setSender(sender);
        projector.postCreateMethod(false, 0);
        
        robot = new Robot(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice());
        
        channel = new JChannel("resources/udp.xml");
        channel.setReceiver(new ReceiverAdapter(){

            @Override
            public void receive(Message msg) {
                super.receive(msg);
                String content = (String) msg.getObject();
                if(content.startsWith("add:")){
                    pdfx_address = msg.getSrc();
                    int position_data_point = content.lastIndexOf("request:");
                    int start = position_data_point + 9;
                    int end = content.length()-1;
                    String position_data = content.substring(start, end);
                    System.out.println(position_data);
                    
                    String[] data = position_data.split(",");
                    int startx = Integer.parseInt(data[0]);
                    int starty = Integer.parseInt(data[1]);
                    int endx = Integer.parseInt(data[2]);
                    int endy = Integer.parseInt(data[3]);
                    
                    
                    BufferedImage image = robot.createScreenCapture(new Rectangle(startx, starty, endx-startx, endy-starty));
                    
                    sender.ImageSend(image);
                    projector.putChunkWithImage(image);
                }
            }
            
        });
        channel.connect("PDFXImageService");
    }
}
