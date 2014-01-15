package GameEngine;

import javax.swing.ImageIcon;

public class Slope extends SpecialObject {
	int direction = 0;

	int broken = 0;
	boolean fallThrough = false;
	double maxSpeedFactor;

	public Slope(double _x, double _y, int _w, int _h, String _direction){createSlope(_x,_y,_w,_h,_direction);}
	public Slope(double _x, double _y, int _w, int _h, String _direction, boolean _fallThrough){createSlope(_x,_y,_w,_h,_direction);if (_fallThrough){fallThrough = true;}}
	public void createSlope(double _x, double _y, int _w, int _h, String _direction){
		x = _x-4;y = _y;w = _w+8;h = _h;
		setDefaultHitBox();
		
		maxSpeedFactor = 1-0.4*(h/w);
		if (maxSpeedFactor<0.1){
			maxSpeedFactor = 0.1;
		}
	
		try{
			if (_direction.equals("left")){
				if (Global.DEBUG_MODE==2){
					sprite = new ImageIcon(Board.class.getResource("/media/images/graphics/special_32_16_L.png")).getImage();
				}
				direction = -1;
			} else if (_direction.equals("right")){
				if (Global.DEBUG_MODE==2){
					sprite = new ImageIcon(Board.class.getResource("/media/images/graphics/special_32_16_R.png")).getImage();
				}
				direction = 1;
			} else {Console.out("\""+_direction+"\" IS NOT A VALID SLOPE DIRECTION");}
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public void onHit(Board board, Map map, CharacterTemplate current){
		if (current.wallCollisions){
			double x1 = current.x+current.hitBox[0];
			double y1 = current.y+current.hitBox[1];
			double w1 = current.hitBox[2]-current.hitBox[0];
			double h1 = current.hitBox[3]-current.hitBox[1];
			double xLeft = x1;//+w1/2;
			double xRight = x1+w1;
			//Console.out("HITTING");
			if (fallThrough){
				if (board.KeysDown[1] && current.interacting && board.KeysDownTimer[1] == 1){
					if (broken == 0){
						board.KeysDownTimer[1]++;
						broken = 3;
						current.dy++;
					}
				}
				if (current.y>y){
					if ((direction == 1 && current.x<x) || (direction == -1 && current.x+current.w>x+w)){
						broken = 3;
					}
				}
			}
			if (broken == 0){
				int yBoost = 3;
				if (y1+h1<=y+h+1 && current.dy >= 0){
					boolean success = false;
					double yPoint = 0;

					if (direction == 1){ 		// right to left going up  |\
						if (xLeft<=x+w && xLeft>=x){
							success = true;
							yPoint = (y+h)-((x+w-xLeft)*h/w)-yBoost;	
						}

						if (current.dx < -current.xMaxSpeed*maxSpeedFactor){
							current.dx = -current.xMaxSpeed*maxSpeedFactor;
						}
					} else if (direction == -1){ // left to right going up  /|
						if (xRight>=x && xRight<=x+w){
							success = true;
							yPoint = (y+h)-((xRight-x)*h/w)-yBoost;
						}
						if (current.dx > current.xMaxSpeed*maxSpeedFactor){
							current.dx = current.xMaxSpeed*maxSpeedFactor;
						}
					}

					if (success){
						if (y1 + h1 > yPoint-yBoost){
							current.floorHit = true;
							current.y = yPoint - yBoost+1 - h1 - current.hitBox[1];
							current.dy = 0;
						}
					}
				}
			} else {
				broken = 3;
			}
		}
	}
	public void always(Map map, boolean[] KeysDown, int[]KeysDownTimer){
		if (broken > 0){
			broken--;
		}
	}
}
