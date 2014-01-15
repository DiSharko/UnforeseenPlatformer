package GameEngine;

import java.awt.Polygon;

public abstract class Function {


	/////////////////////////////////////////////////////////////////////
	/////////////////  --------HIT TESTS------  /////////////////////////
	/////////////////////////////////////////////////////////////////////

	public static boolean floorHitTest(GameObject test, GameObject floor){
		double x = test.x+test.hitBox[0];
		double y = test.y+test.hitBox[1];
		double w = test.hitBox[2]-test.hitBox[0];
		double h = test.hitBox[3]-test.hitBox[1];

		double fx = floor.x;
		double fy = floor.y;
		double fw = floor.w;
		double fh = floor.h;

		if (y+h < fy)
			return false;
		if (y+h > fy+fh/2)
			return false;
		if (x > fx + fw-1)
			return false;
		if (x+w < fx+1)
			return false;

		return true;

	}
	public static boolean ceilingHitTest(GameObject test, GameObject ceiling){
		double x = test.x+test.hitBox[0];
		double y = test.y+test.hitBox[1];
		double w = test.hitBox[2]-test.hitBox[0];
		//double h = test.hitBox[3]-test.hitBox[1];

		double cx = ceiling.x;
		double cy = ceiling.y;
		double cw = ceiling.w;
		double ch = ceiling.h;

		if (y+1 > cy+ch)
			return false;
		if (y < cy)
			return false;
		if (x > cx + cw-2)
			return false;
		if (x+w < cx+2)
			return false;

		return true;
	}
	public static boolean leftHitTest(GameObject test, GameObject left){
		double x = test.x+test.hitBox[0];
		double y = test.y+test.hitBox[1];
		double w = test.hitBox[2]-test.hitBox[0];
		double h = test.hitBox[3]-test.hitBox[1];

		double lx = left.x;
		double ly = left.y;
		double lw = left.w;
		double lh = left.h;

		if (x+w > lx+lw/2)
			return false;
		if (x+w < lx)
			return false;
		if (y > ly+lh-2)
			return false;
		if (y+h < ly+2)
			return false;

		return true;
	}
	public static boolean rightHitTest(GameObject test, GameObject right){
		double x = test.x+test.hitBox[0];
		double y = test.y+test.hitBox[1];
		//double w = test.hitBox[2]-test.hitBox[0];
		double h = test.hitBox[3]-test.hitBox[1];

		double rx = right.x;
		double ry = right.y;
		double rw = right.w;
		double rh = right.h;

		if (x < rx+rw/2)
			return false;
		if (x > rx+rw)
			return false;
		if (y > ry+rh-2)
			return false;
		if (y+h < ry+2)
			return false;

		return true;
	}
	
	
	public static boolean fullHitTest(GameObject test1, GameObject test2){
		double x1 = test1.x+test1.hitBox[0] - test1.dx;
		double y1 = test1.y+test1.hitBox[1] - test1.dy;
		double w1 = test1.hitBox[2] + test1.dx;
		double h1 = test1.hitBox[3] + test1.dy;

		double x2 = test2.x+test2.hitBox[0] - test2.dx;
		double y2 = test2.y+test2.hitBox[1] - test2.dy;
		double w2 = test2.hitBox[2] + test2.dx;
		double h2 = test2.hitBox[3] + test2.dy;

		return actualHitTest('f', x1, y1, w1, h1, x2, y2, w2, h2, 0, 0, 0, 0);
	}
	public static boolean fullHitTest(GameObject test1, GameObject test2, double xPadding, double yPadding){
		double x1 = test1.x+test1.hitBox[0] - test1.dx;
		double y1 = test1.y+test1.hitBox[1] - test1.dy;
		double w1 = test1.hitBox[2] + test1.dx;
		double h1 = test1.hitBox[3] + test1.dy;

		double x2 = test2.x+test2.hitBox[0] - test2.dx;
		double y2 = test2.y+test2.hitBox[1] - test2.dy;
		double w2 = test2.hitBox[2] + test2.dx;
		double h2 = test2.hitBox[3] + test2.dy;

		return actualHitTest('f', x1, y1, w1, h1, x2, y2, w2, h2, 0, 0, 0, 0);
	}
	public static boolean fullHitTest(GameObject test1, GameObject test2, double topPadding, double bottomPadding, double leftPadding, double rightPadding){
		double x1 = test1.x+test1.hitBox[0] - test1.dx;
		double y1 = test1.y+test1.hitBox[1] - test1.dy;
		double w1 = test1.hitBox[2] + test1.dx;
		double h1 = test1.hitBox[3] + test1.dy;

		double x2 = test2.x+test2.hitBox[0] - test2.dx;
		double y2 = test2.y+test2.hitBox[1] - test2.dy;
		double w2 = test2.hitBox[2] + test2.dx;
		double h2 = test2.hitBox[3] + test2.dy;


		return actualHitTest('f', x1, y1, w1, h1, x2, y2, w2, h2, topPadding, bottomPadding, leftPadding, rightPadding);
	}
	public static boolean fullHitTest(double x1, double y1, double w1, double h1, GameObject test2){
		double x2 = test2.x+test2.hitBox[0] - test2.dx;
		double y2 = test2.y+test2.hitBox[1] - test2.dy;
		double w2 = test2.hitBox[2] + test2.dx;
		double h2 = test2.hitBox[3] + test2.dy;

		return actualHitTest('f', x1, y1, w1, h1, x2, y2, w2, h2, 0, 0, 0, 0);
	}


	private static boolean actualHitTest(char type, double x1, double y1, double w1, double h1, double x2, double y2, double w2, double h2, double leftPadding, double rightPadding, double topPadding, double bottomPadding){
		x1 -= leftPadding;
		y1 -= topPadding;
		w1 += leftPadding+rightPadding;
		h1 += topPadding+bottomPadding;

		x2 -= leftPadding;
		y2 -= topPadding;
		w2 += leftPadding+rightPadding;
		h2 += topPadding+bottomPadding;

		if (type == 'f'){
			if (x1+w1<x2)
				return false;
			if (x1>x2+w2)
				return false;
			if (y1>y2+h2)
				return false;
			if (y1+h1<y2)
				return false;

			return true;
		} else {
			
		}
		
		return false;
	}



	public static Polygon mapPolygon(Polygon _original, double xCamera, double yCamera){

		Polygon _new = new Polygon(_original.xpoints, _original.ypoints, _original.npoints);
		for (int j = 0; j<_new.npoints; j++){
			_new.xpoints[j] -= xCamera;
		}
		for (int j = 0; j<_new.npoints; j++){
			_new.ypoints[j] -= yCamera;
		}

		return _new;
	}


	public static void faceSprites(CharacterTemplate sprite1, CharacterTemplate sprite2){
		if (sprite1.x < sprite2.x){
			sprite2.facing = -1;
			sprite2.x+=3;
		} else if (sprite1.x > sprite2.x){
			sprite2.x-=3;
			sprite2.facing = 1;
		}
		if (sprite1.x > sprite2.x){
			sprite1.facing = -1;
			sprite1.x+=3;
		} else if (sprite1.x < sprite2.x){
			sprite1.facing = 1;
			sprite1.x-=3;
		}
	}

	public static boolean checkForDrawing(Board board, GameObject current){
		if (board.game.theMap.reducedVision){
			if (current.everywhere ||
					(current.x 			   <= board.game.theMap.xCamera + board.game.theMap.xRight  -(board.game.theMap.xCamera-board.game.player.x+board.gameDimensions.width/2)
							&& current.x + current.w >= board.game.theMap.xCamera + board.game.theMap.xLeft   -(board.game.theMap.xCamera-board.game.player.x+board.gameDimensions.width/2)
							&& current.y + current.h >= board.game.theMap.yCamera + board.game.theMap.yTop    -(board.game.theMap.yCamera-board.game.player.y+board.gameDimensions.height/2)
							&& current.y 			   <= board.game.theMap.yCamera + board.game.theMap.yBottom -(board.game.theMap.yCamera-board.game.player.y+board.gameDimensions.height/2))){
				return true;
			}	
		} else {
			if (current.everywhere || (current.x<= board.game.theMap.xCamera+board.game.theMap.xRight && current.x + current.w >= board.game.theMap.xCamera + board.game.theMap.xLeft && current.y + current.h >= board.game.theMap.yCamera + board.game.theMap.yTop && current.y <= board.game.theMap.yCamera + board.game.theMap.yBottom)){
				return true;
			}
		}
		return false;
	}
}
