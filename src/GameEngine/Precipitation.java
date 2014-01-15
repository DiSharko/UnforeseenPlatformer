package GameEngine;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Precipitation extends SpecialObject {
	String type = "";
	int intensity = 0;
	double speed = 0;
	double angle = 0;

	byte dir = 0;

	Board board;
	Map theMap;

	double mapX = 0;
	double mapY = 0;
	double mapW = 0;
	double mapH = 0;
	double dx;
	double dy;
	int xOffset = 0;
	int yOffset = 0;
	boolean staggered = false;
	double staggering = 2;
	BasicStroke pieceSize;

	ArrayList<double[]> pieces = new ArrayList<double[]>();

	public Precipitation (Board b, Map map, String t, int i, double s, byte d, double a, boolean starting){
		try {
			board = b;
			theMap = map; //must be obtained separately because at this point, the board does not see the new map (it might still be in the constructor)
			mapX = theMap.leftXbound;
			mapY = theMap.topYbound;
			mapW = theMap.rightXbound - theMap.leftXbound;
			mapH = theMap.bottomYbound - theMap.topYbound;
			x = mapX;
			y = mapY;
			w = (int) mapW;
			h = (int) mapH;
			type = t;
			intensity = i;
			speed = s;
			dir = d;
			angle = Math.toRadians(a);
			dx = speed*Math.sin(angle);
			dy = speed*Math.cos(angle);
			xOffset = (int)(5*Math.sin(angle));
			yOffset = (int)(5*Math.cos(angle));
			pieceSize = new BasicStroke((float)2);
			if (starting == false){
				while (pieces.size()<intensity){addNewPiece(false);}
			} else {
				staggered = true;
			}

			if (type.equals("rain") || type.equals("thunderstorm")){
				board.game.audio.playSong("rain");
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		setDefaultHitBox();
	}

	public void always(Map map, boolean[] KeysDown, int[]KeysDownTimer){
		while (pieces.size() < intensity){
			if (staggered == true){
				int count = 0;
				while (count < staggering){
					addNewPiece(true);
					count++;
				}
				staggering+=0.1;
				if (staggering >= intensity || pieces.size()>intensity){
					staggered = false;
				}
				break;
			} else {
				addNewPiece(true);
			}
		}


		for (int i = 0; i< pieces.size(); i++){
			if (type.equals("rain") || type.equals("thunderstorm")){
				pieces.get(i)[0] += pieces.get(i)[2]*dir;
				pieces.get(i)[1] += pieces.get(i)[3];
			} else if (type.equals("snow")){
				pieces.get(i)[0] += pieces.get(i)[2]*pieces.get(i)[4];
				pieces.get(i)[1] += pieces.get(i)[3];
			}

			if (pieces.get(i)[1] > mapY+mapH || pieces.get(i)[0] > mapX+mapW || pieces.get(i)[0] < mapX){
				pieces.remove(i);
			}
		}
		
		
		if (type.equals("thunderstorm")){
			double r = Math.random();
			if (r < 0.0007){
				map.specialData.add(new MapTask(board, map, "type:lightningFlash"));
				board.game.audio.playSound("thunder");
			}
		}
	}

	public void draw(Board _board, Map theMap, Graphics2D g2d) {
		g2d.rotate(rotation);
		if (type.equals("rain") || type.equals("thunderstorm")){
			g2d.setColor(Color.darkGray);
			g2d.setStroke(pieceSize);
			for (int i = 0; i < pieces.size(); i++){
				if (pieces.get(i)[0] <= theMap.xCamera+640 && pieces.get(i)[0] >= theMap.xCamera && pieces.get(i)[1] >= theMap.yCamera && pieces.get(i)[1] <= theMap.yCamera+ 480){
					g2d.drawLine((int)(pieces.get(i)[0]-theMap.xCamera), (int)(pieces.get(i)[1]-theMap.yCamera), (int)(pieces.get(i)[0]-theMap.xCamera)+xOffset*dir, (int)(pieces.get(i)[1]-theMap.yCamera)+5+(5-yOffset));
				}
			}
		} else if (type.equals("snow")){
			g2d.setColor(Color.white);
			g2d.setStroke(new BasicStroke());
			for (int i = 0; i < pieces.size(); i++){
				if (pieces.get(i)[0] <= theMap.xCamera+640 && pieces.get(i)[0] >= theMap.xCamera && pieces.get(i)[1] >= theMap.yCamera && pieces.get(i)[1] <= theMap.yCamera+ 480){
					int aX = (int)(pieces.get(i)[0]-theMap.xCamera);
					int aY = (int)(pieces.get(i)[1]-theMap.yCamera);
					g2d.drawLine(aX, aY-2, aX, aY+2);
					g2d.drawLine(aX-2, aY, aX+2, aY);
					g2d.drawLine(aX-1, aY+1, aX+1, aY-1);
					g2d.drawLine(aX-1, aY-1, aX+1, aY+1);
				}
			}
		}
	}

	public void addNewPiece(boolean startFromTop){
		if (type.equals("rain") || type.equals("thunderstorm")){
			double y = Math.random()*mapH+mapY+1;
			if (startFromTop){
				y = mapY+1;
			}
			pieces.add(new double[] {Math.random()*(mapW+200)+mapX-100, y, Math.random()*dx+4*dx/5, Math.random()*dy+4*dy/5});
		} else if (type.equals("snow")){
			double y = Math.random()*mapH+mapY+1;
			if (startFromTop){
				y = mapY+1;
			}
			pieces.add(new double[] {Math.random()*(mapW+200)+mapX-100, y, Math.random()*dx*2+dx/2, Math.random()*dy*2+dy/2, (Math.pow(-1, (int)(Math.random()*2)))});

		}
	}

	public void usePreset(String p){

	}
}
