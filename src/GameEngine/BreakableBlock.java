package GameEngine;

import java.awt.Image;
import java.util.ArrayList;

public class BreakableBlock extends SpecialObject {
	boolean allBreak = false;
	Tile tile;
	Graphic graphic;
	Graphic graphic2;
	CharacterTemplate tracker;
	ArrayList<Image> breakAnimation;
	
	public BreakableBlock(Map _map, String _name, double _x, double _y, int _w, int _h){
		name = _name;
		
		x = _x;
		y = _y;
		w = _w;
		h = _h;
		
		tile = new Tile(x,y,w,h,true,true,true,true);
		graphic = new Graphic(name,x,y,false);
		if (allBreak){
			tracker = new Character(null, -2, "", x, y, w, h, "health:1;d:0");
		} else {
			tracker = new Character(null, 1, "", x, y, w, h, "health:1;d:0");
		}
		tracker.applyPhysics = tracker.displayDamage = tracker.wallCollisions = false;
		
		_map.tileData.add(tile);
		_map.graphicData3.add(graphic);
		_map.charData.add(tracker);
	}
	
	public void always(Map map, boolean[] KeysDown, int[]KeysDownTimer){
		if (tracker.health!=1){
			graphic.delete = true;
			if (graphic2!=null){
				graphic2.delete = true;
			}
			tile.floor = tile.ceiling = tile.right = tile.left = false;
			delete = true;
		}
	}
	public void addGraphic(String name){
		graphic2 = new Graphic(name,x,y,false);
		map.graphicData3.add(graphic2);
	}
}
