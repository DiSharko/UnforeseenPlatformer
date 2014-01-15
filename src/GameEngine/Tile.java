package GameEngine;

public class Tile extends GameObject {

	int slope = 0;
	
	public Tile(double _x, double _y, int _w, int _h, boolean _floor, boolean _ceiling, boolean _left, boolean _right) {
		w = _w;
		h = _h;
		x = _x;
		y = _y;
		floor = _floor;
		ceiling = _ceiling;
		left = _left;
		right = _right;
		setDefaultHitBox();
	}
	public Tile(Tile tile){
		x = tile.x;
		y = tile.y;
		w = tile.w;
		h = tile.h;
		floor = tile.floor;
		ceiling = tile.ceiling;
		left = tile.left;
		right = tile.right;
		setDefaultHitBox();
	}
}
