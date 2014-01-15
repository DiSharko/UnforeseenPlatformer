package GameEngine;


public class Character extends CharacterTemplate {
	public Character(Board _board, int _team, String _name, double _x, double _y, int _w, int _h, String _settings){
		board = _board;
		name = _name;
		nickname = name;
		team = _team;
		
		if (name!=null){if(!name.equals("")){importSequence("still");}} else {Console.out("Character created with null name!");};
		
		x = _x;
		y = _y;

		originalW = _w;
		originalH = _h;
		w = originalW;
		h = originalH;
		drawW = w;
		drawH = h;

		moveState = MoveState.RUNNING;
		moveStateFrame = 0;
		facing = 1;
		health = 100;
		
		hitBox[0] = 0;
		hitBox[1] = 0;
		hitBox[2] = w;
		hitBox[3] = h;

		addSettings(_settings);
	}
}