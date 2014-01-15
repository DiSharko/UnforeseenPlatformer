package GameEngine;

//import javax.swing.ImageIcon;

public class Door extends SpecialObject {

	String location;
	String activation;
	String entrance;
	double newX;
	double newY;

	public Door (double _x, double _y, int _w, int _h, String _location,String _activation, String _entrance){
		//sprite = new ImageIcon(Board.class.getResource("/media/images/graphics/door_metal.png")).getImage();
		//sObjectType = "door";
		x = _x;
		y = _y;
		w = _w;
		h = _h;
		location = _location;
		activation = _activation;
		entrance = _entrance;
		setDefaultHitBox();
	}

	public void onHit(Board board, Map map, CharacterTemplate current){
		if (activation.equals("interact")){
			if (board.KeysDownTimer[1] == 1 && current.interacting){
				board.KeysDownTimer[1]++;
				map.nextMap = location;
				map.nextEntrance = entrance;
				map.reload = 5; // amount of time for the screen to go black before switching
			}
		} else {
			map.nextMap = location;
			map.nextEntrance = entrance;
			map.reload = 5; // amount of time for the screen to go black before switching
		}
	}
}
