/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Animation;

import java.util.ArrayList;

/**
 *
 * @author y_chiba
 */
public interface Animable {
    public String getAnimableId();
    public void loaded();
    public void clicked();
    public void finished();
    public void otherActionHasOccored();
    public void setOnLoaded(AnimationEventHandler handler);
    public void setOnClicked(AnimationEventHandler handler);
    public void setOnFinished(AnimationEventHandler handler);
    public void setOnOtherActionOccored(AnimationEventHandler handler);
    public ArrayList<Animation> getAnimationList();
}
