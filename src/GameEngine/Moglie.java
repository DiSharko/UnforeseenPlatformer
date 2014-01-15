package GameEngine;

public class Moglie extends CharacterTemplate {
	public Moglie(Board _board, int _team, double _x, double _y){
		board = _board;

		if (_team == 0){controlled = true;} else {controlled = false;}
		
		team = _team;
		x = _x;
		y = _y;
		
		setupUnforeseen();
	}
	
	public void setupUnforeseen(){
		name = "moglie";
		nickname = "Moglie";

		importSequence("still");
		importSequence("run");
		importSequence("jump");
		importSequence("fall");
		importSequence("climb");
		importSequence("attack1");
		importSequence("attack2");
		importSequence("punch");

		originalW = 26;
		originalH = 30;
		w = originalW;
		h = originalH;
		drawW = w;
		drawH = h;
		totalJumps = 1;
		canJump = true;

		xAcceleration = 0.2;
		xMaxSpeed = 4.3;
		jumpAcceleration = 3.6;
		jumpMaxSpeed = 80000.2;
		fallSpeed = 0.7;
		fallMaxSpeed = 7;

		moveState = MoveState.STILL;
		moveStateFrame = 0;
		facing = 1;
		healthMax = 25;
		health = healthMax;
		energyMax = 100;
		energyRechargeSpeed = 0.4;
		energy = energyMax;
		
		invincibleMaxTime = 150;


		hitBox[0] = 3; // taken off the left side (each side)
		hitBox[1] = 1; // lower than head
		hitBox[2] = w-hitBox[0]; // so that it takes the same off the right side
		hitBox[3] = h; // so that it rests on the bottom exactly

		inventory = new Inventory(board, this);
	}

}