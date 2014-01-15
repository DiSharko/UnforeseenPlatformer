package GameEngine;

import java.awt.Polygon;

import javax.swing.ImageIcon;

public class TimedObject extends SpecialObject {
	Board board;
	String time;
	String type;
	String location;
	
	public TimedObject(Board _board, Map _map, double _x, double _y, double _w, double _h, String _type, String _location){
		x = _x;
		y = _y;
		w = (int) _w;
		h = (int) _h;
		board = _board;
		map = _map;
		time = board.game.quest.timeOfDay;
		type = _type;
		location = _location;
		
		if (location.equals("outdoor")){
			if (time.equals("day")){
				sprite = new ImageIcon(Board.class.getResource("/media/images/graphics/"+type+"/dawn.png")).getImage();
			} else if (time.equals("night")){
				sprite = new ImageIcon(Board.class.getResource("/media/images/graphics/"+type+"/day.png")).getImage();
				try{
					map.lightSources.add(new Polygon(new int[] {(int) (x),(int) (x+w),(int) (x+w), (int) (x)},new int[] {(int) (y),(int) (y),(int) (y+h),(int) (y+h)},4));
				}catch(Exception e){
					e.printStackTrace();
				}
			} else {
				sprite = new ImageIcon(Board.class.getResource("/media/images/graphics/"+type+"/dusk.png")).getImage();
			}
		} else if (location.equals("indoor")){
			sprite = new ImageIcon(Board.class.getResource("/media/images/graphics/"+type+"/"+time+".png")).getImage();
		}
		setDefaultHitBox();
		
		Graphic _graphic = new Graphic(null,x,y, true);
		_graphic.sprite = sprite;
		_graphic.w = w;
		_graphic.h = h;
		
		map.graphicData3.add(_graphic);
	}
}
