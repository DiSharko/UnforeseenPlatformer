package GameEngine;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class MovingPlatform extends SpecialObject {
	Board board;
	Map theMap;
	
	ArrayList <String[]> path = new ArrayList<String[]>();
	ArrayList <Graphic> graphics = new ArrayList<Graphic>();
	ArrayList <Tile> tiles = new ArrayList<Tile>();

	protected boolean retrace = true; //aka default behavior is to retrace path backwards.
	String movementType = "nodes";

	double dx = 1;
	double dy = 1;
	double xDir = 0;
	double yDir = 0;
	double damage = 0;
	boolean starting = true;

	int hitCount = 0;


	double leftSide = 0.01030307;
	double rightSide = 0.01030307;
	double top = 0.01030307;
	double bottom = 0.01030307;

	int numPieces = 0;
	String style = "";

	int currentNode = 0;
	int counter = 1;

	String preset = "";
	
	// "length:5;preset:train;node:200:350;node:250:350;speed:10"
	public MovingPlatform(Board _board, Map _map, String string){
		board = _board;
		theMap = _map;

		floor = true;
		ceiling = true;
		left = true;
		right = true;

		x = 0.01030307;
		y = 0.01030307;
		String[] propList = string.split(";");
		for (int i = 0; i<propList.length; i++){
			String[] prop = propList[i].split(":");
			if (prop[0].equals("preset")){
				preset = prop[1];
			} else if (prop[0].equals("length")){
				numPieces = Integer.parseInt(prop[1]);
			} else if (prop[0].equals("style")){
				style = prop[1];
			} else if (prop[0].equals("movement")){
				movementType = prop[1];
			} else if (prop[0].equals("x")){
				x = Double.parseDouble(prop[1]);
			} else if (prop[0].equals("y")){
				y = Double.parseDouble(prop[1]);
			} else if (prop[0].equals("dx")){
				dx = Double.parseDouble(prop[1]);
			} else if (prop[0].equals("dy")){
				dy = Double.parseDouble(prop[1]);
			} else if (prop[0].equals("node")){
				path.add(new String[]{});
			} else if (prop[0].equals("delay")){
				//path.add(propList[i]);
			} else if (prop[0].equals("retrace")){
				if (prop[1].equals("true")){
					retrace = true;
				} else if (prop[1].equals("false")){
					retrace = false;
				}
			} else if (prop[0].equals("left")){
				leftSide = Double.parseDouble(prop[1]);
			} else if (prop[0].equals("right")){
				rightSide = Double.parseDouble(prop[1]);
			} else if (prop[0].equals("top")){
				top = Double.parseDouble(prop[1]);
			} else if (prop[0].equals("bottom")){
				bottom = Double.parseDouble(prop[1]);
			} else if (prop[0].equals("movingPlatform")){
			} else {
				Console.out("Unknown setting for MovingPlatform: "+propList[i]);
			}
		}
		
		if (movementType.equals("nodes")){
			if (x == 0.01030307){
				try {
					//x = path.get(0)[0];
				} catch (Exception e){
					Console.out("Node initialization failure.");
					delete = true;
				}
			}
			if (y == 0.01030307){
				try {
					//y = path.get(0)[1];
				} catch (Exception e){
					Console.out("Node initialization failure.");
					delete = true;
				}
			}
		}
		
		usePreset(preset);
		
		theMap.tileData.addAll(tiles);
		setDefaultHitBox();
	}

	public void always(Map map, boolean[] KeysDown, int[]KeysDownTimer){
		/*if (movementType.equals("nodes")){
			if (Math.abs(x-path.get(currentNode)[0])<dx && Math.abs(y-path.get(currentNode)[1])<dy || starting){
				starting = false;
				currentNode += counter;
				if (currentNode >= path.size()){
					if (retrace){
						counter = -1;
						currentNode -= 2;
					} else {
						currentNode = 0;
					}
				} else if (currentNode<0){
					counter = 1;
					currentNode = 1;
				}
				double xDist = x-path.get(currentNode)[0];
				if (xDist>dx){
					xDir = -1;
				} else if (xDist<-dx){
					xDir = 1;
				} else {
					xDir = 0;
				}
				double yDist = y-path.get(currentNode)[1];
				if (yDist>dy){
					yDir = -1;
				} else if (yDist<-dy){
					yDir = 1;
				} else {
					yDir = 0;
				}
			} else {
				for (int i = 0; i<tiles.size(); i++){
					changeX(dx*xDir);
					changeY(dy*yDir);
				}
			}

		
		
		
		} else if (movementType.equals("boundaries")){
			if (xDir == 0 && yDir == 0){
				if (top != 0.01030307){
					yDir = -1;
				} else if (bottom != 0.01030307){
					yDir = 1;		
				}
				if (leftSide != 0.01030307){
					xDir = -1;
				} else if (rightSide != 0.01030307){
					xDir = 1;
				}
			}
			if (x < leftSide && leftSide != 0.01030307){
				x = leftSide;
				xDir = 1;
			} else if (x > rightSide && rightSide != 0.01030307){
				x = rightSide;
				xDir = -1;
			}
			if (y < top && top != 0.01030307){
				y = top;
				yDir = 1;
			} else if (y > bottom && bottom != 0.01030307){
				y = bottom;
				yDir = -1;
			}
			for (int i = 0; i < theMap.tileData.size(); i++){
				Tile t = theMap.tileData.get(i);
				if (Function.fullHitTest(this, t)){
					if (t.floor || t.ceiling){
						if (xDir > 0 && Function.leftHitTest(this, t)){
							xDir = -1;
						} else if (xDir < 0 && Function.rightHitTest(this, t)){
							xDir = 1;
						} else if (Function.floorHitTest(this, t) || Function.ceilingHitTest(this, t)){
							yDir *= -1;
						}
					}
				}
			}
			if (hitCount > 10){
				hitCount = 0;
			} else {
				hitCount++;
			}
			if (hitCount == 0){
				for (int i = 0; i < theMap.specialData.size(); i++){
					SpecialObject s = theMap.specialData.get(i);
					if (Function.fullHitTest(this, s, 0, -2)){

						if (xDir > 0 && Function.leftHitTest(this, s)){
							xDir = -1;
						} else if (xDir < 0 && Function.rightHitTest(this, s)){
							xDir = 1;
						} else if (Function.floorHitTest(this, s) || Function.ceilingHitTest(this, s)){
							yDir *= -1;
						}
					}
				}
			} 

			x += dx*xDir;
			y += dy*yDir;
		}*/
	}

	public void onHit(Board board, Map map, CharacterTemplate current){
		boolean touched = false;
		for (int i = 0; i<tiles.size(); i++){
			Tile t = new Tile (tiles.get(i));
			board.characterSidesTest(current, t);
			if (Function.floorHitTest(current, t)){
				touched = true;
			}
			if (current.floorHit && current.ceilingHit){
				if (!(current.x+current.w/2<x || current.x+current.w/2 > x + w)){
					current.health = 0;
				}
			}
		}
		if (touched){
			current.x += dx*xDir;
			current.y += dy*yDir;
			if (damage > 0){
				current.takeDamage(map, damage);
			}
		}

	}
	public void draw(Board _board, Map theMap, Graphics2D g2d) {
		for (int i = 0; i < graphics.size(); i++){
			Graphic test = new Graphic(graphics.get(i).name, graphics.get(i).x+x, graphics.get(i).y+y, false);
			test.w = graphics.get(i).w;test.h = graphics.get(i).h;
			if ((Function.checkForDrawing(board, test))){
				g2d.drawImage(graphics.get(i).sprite, (int)(test.x-theMap.xCamera), (int)(test.y-theMap.yCamera), null);
			}
		}
	}

	
	
	
	public void usePreset(String preset){
		if (preset.equals("train")){
			if (numPieces == 0){
				numPieces = 5;
			}
			int tX = 0;
			graphics.add(new Graphic("train_left",tX,0, true));
			tiles.add(new Tile(tX,0,64,32,true,true,true,true));
			for (int i = 0; i<numPieces-2; i++){
				tX+=64;
				graphics.add(new Graphic("train_mid",tX, 0, true));
				tiles.add(new Tile(tX,0,64,32,true,true,false,false));
			}
			tX+=64;
			graphics.add(new Graphic("train_right",tX,0, true));
			tiles.add(new Tile(tX,0,64,32,true,true,true,true));
			w = tX+64;
			h = 32;
			//dx = 20;
			//damage = 100;
		} else if (preset.equals("normal") || preset.equals("office")){
			if (numPieces == 0){
				numPieces = 3;
			}
			if (style.equals("")){
				style = "office";
			}
			int _x = 0;
			graphics.add(new Graphic(style+"_platform_left",_x,0, true));
			tiles.add(new Tile(x,y,32,32,true,true,true,true));
			for (int i = 0; i<numPieces-2; i++){
				_x+=32;
				graphics.add(new Graphic(style+"_platform",_x, 0, true));
				tiles.add(new Tile(x+_x,y,32,32,true,true,false,false));
			}
			_x+=32;
			graphics.add(new Graphic(style+"_platform_right",_x,0, true));
			tiles.add(new Tile(x+_x,y,32,32,true,true,true,true));
			w = _x+32;
			h = 32;
		}
	}
	
	
	
	public void changeX(double _value){
		x+=_value;
		for (int i = 0; i < tiles.size(); i++){
			tiles.get(i).x += _value;
		}	
	}
	public void changeY(double _value){
		y+=_value;
		for (int i = 0; i < tiles.size(); i++){
			tiles.get(i).y += _value;
		}	
	}
}
