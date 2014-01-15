package GameEngine;

import java.util.ArrayList;

public class CoverGraphics extends SpecialObject {
	ArrayList<Graphic> graphics;
	ArrayList<Tile> trackers;
	int triggered = 0;


	public CoverGraphics (Map _map, ArrayList<Graphic> _graphics, ArrayList<Tile> _trackers){
		map = _map;

		graphics = _graphics;
		map.graphicData1.addAll(graphics);
		trackers = _trackers;

		for (int i = 0; i<trackers.size(); i++){
			if (i == 0){
				x = trackers.get(i).x;
				y = trackers.get(i).y;
				w = trackers.get(i).w;
				h = trackers.get(i).h;
			} else {
				if (trackers.get(i).x < x){
					w += x-trackers.get(i).x;
					x = trackers.get(i).x;
				}
				if (trackers.get(i).y < y){
					h += y - trackers.get(i).y;
					y = trackers.get(i).y;
				}
				if (trackers.get(i).x+trackers.get(i).w > x+w){
					w = (int)(trackers.get(i).x+trackers.get(i).w - x);
				}
				if (trackers.get(i).y+trackers.get(i).h > y+h){
					h = (int)(trackers.get(i).y+trackers.get(i).h - y);
				}
			}
		}
		
		setDefaultHitBox();
	}

	public void onHit(Board board, Map map,CharacterTemplate current){
		if (current.equals(board.game.player)){
			for (int i = 0; i < trackers.size(); i++){
				if (Function.fullHitTest(trackers.get(i), current)){
					triggered = 2;
					break;
				}
			}
			for (int i = 0; i < graphics.size(); i++){
				graphics.get(i).alpha = (float)(1f/2);
			}
		}
	}
	
	public void always(Map map, boolean[] KeysDown, int[]KeysDownTimer){
		if (triggered > 1){
			triggered--;
		} else if (triggered == 1){
			for (int i = 0; i < graphics.size(); i++){
				graphics.get(i).alpha = 1f;
			}
			triggered = 0;
		}
	}
}
