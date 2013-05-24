/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package AudioNodes;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import menu.OptionsMenu;

/**
 *
 * @author Inferno
 */
public class MusicAudioNode extends AudioNode 
{
    public MusicAudioNode()
    {
        super();
    }
    
    public MusicAudioNode(AssetManager assetManager, String name)
    {
        super(assetManager, name);
        setVolume(OptionsMenu.currentMusicVolume);
    }
    
    public MusicAudioNode(AssetManager assetManager, String name, boolean stream)
    {
        super(assetManager, name, stream);
        setVolume(OptionsMenu.currentMusicVolume);
    }

}
