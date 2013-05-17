/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Animation;
/**
 *
 * @author y_chiba
 */
public class AnimationEvent{
    public static final int LOADED = 1;
    public static final int CLICKED = 2;
    public static final int FINISHED = 3;
    public static final int OTHER = 4;
    private Animable source;
    private int status;
    public void setSource(Animable source){
        this.source = source;
    }
    public Animable getSource(){
        return this.source;
    }
    public void setStatus(int status){
        this.status = status;
    }
    public int getStatus(){
        return this.status;
    }
}
