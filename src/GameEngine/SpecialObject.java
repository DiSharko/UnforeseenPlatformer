package GameEngine;

import java.awt.Graphics2D;
import java.awt.Image;

public class SpecialObject extends GameObject {
	
	Map map;
	
	double startX;
	double startY;
	
	Image specialSquare;
	
	double rotation = 0;
	double time = 0;
	String type = "";
	
	byte facing = 1; //left = -1, right = 1 (default is right)
	
	public void onHit(Board board, Map map,CharacterTemplate current){}
	public void always(Map map, boolean[] KeysDown, int[]KeysDownTimer){}
	public void draw(Board _board, Map theMap, Graphics2D g2d) {
//		int fComp = 0;
//		if (facing == -1)
//			fComp = w;
//		g2d.rotate(rotation);
//		if (Global.DEBUG_MODE != 2 || sprite!=null){
//			g2d.drawImage(sprite, (int) Math.round(x - theMap.xCamera + fComp), (int) Math.round(y - theMap.yCamera), w*facing, h, null);
//		} else {
//			specialSquare = new ImageIcon(Board.class.getResource("/media/images/graphics/specialSquare.png")).getImage();
//			g2d.drawImage(specialSquare, (int) x - (int) theMap.xCamera + fComp, (int) y - (int) theMap.yCamera, w*facing, h, null);
//		}
	}
}
