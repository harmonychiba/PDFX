/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pdfxnote;

/**
 *
 * @author Takafumi
 */
public class CaptureRequest {
    private int startx,starty,endx,endy;

    public void setStartx(int startx) {
        this.startx = startx;
    }

    public void setStarty(int starty) {
        this.starty = starty;
    }

    public void setEndx(int endx) {
        this.endx = endx;
    }

    public void setEndy(int endy) {
        this.endy = endy;
    }

    public int getStartx() {
        return startx;
    }

    public int getStarty() {
        return starty;
    }

    public int getEndx() {
        return endx;
    }

    public int getEndy() {
        return endy;
    }
    
}
