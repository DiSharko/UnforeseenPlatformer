package GameEngine;

import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class SpriteSequence {
	String state;
	ArrayList<Image> sprite;
	CharacterTemplate host;
	
	public SpriteSequence(CharacterTemplate _host, String _state){
		state = _state;
		host = _host;
		
		sprite = new ArrayList<Image>();
		boolean allImported = false;
		short i = 1;
		while (!allImported){
			try{
				sprite.add(new ImageIcon(Board.class.getResource("/media/images/characters/"+ host.name +"/"+state+"_"+i+".png")).getImage());
				i++;
			} catch (NullPointerException e){
				allImported = true;
				if (i == 1){
					Console.out("Could not find "+state+" frames for "+host.name+".");
				}
			}
		}
	}
}
