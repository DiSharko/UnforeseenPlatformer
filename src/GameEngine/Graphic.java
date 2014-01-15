package GameEngine;


import javax.swing.ImageIcon;

public class Graphic extends GameObject {	
	
	boolean useSprite = false;
	
	public Graphic(String _name, double _x, double _y, boolean _useSprite) {
		try{
			name = _name;
			useSprite = _useSprite;
			x = _x;
			y = _y;
			
			if (_useSprite){
				sprite = new ImageIcon(Board.class.getResource("/media/images/graphics/"+_name +".png")).getImage();
				w = sprite.getWidth(null);
				h = sprite.getHeight(null);
			} else {
				w=32;
				h=32;
			}
			
		} catch (NullPointerException e){if (!_name.equals("null")){Console.out("SOMETHING HAPPENED TO OUR "+name+" GRAPHIC!!!!!!");}}
	}
}
