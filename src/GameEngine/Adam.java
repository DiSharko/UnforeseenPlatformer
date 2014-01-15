package GameEngine;

public class Adam extends CharacterTemplate {
	public Adam(Board b, int t, double xpos, double ypos){
		board = b;

		if (t == 0){
			controlled = true;
		} else {
			controlled = false;
		}
		team = t;
		x = xpos;
		y = ypos;
		
		//setupHourglass();
		setupUnforeseen();
	}
	
	public void setupUnforeseen(){
		name = "adam";
		nickname = "Adam";

		//weapons.add(new FireSet1());
		//weapons.add(new FireSet2());

		importSequence("still");
		importSequence("run");
		importSequence("jump");
		importSequence("fall");
		importSequence("climb");
		importSequence("attack1");
		importSequence("attack2");
		importSequence("punch");

		//importSequence("walk");
		//importSequence("wallClimb");

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
		//manaMax = 100;
		//manaRechargeSpeed = 0.01;
		//mana = manaMax;
		invincibleMaxTime = 150;


		hitBox[0] = 3; // taken off the left side (each side)
		hitBox[1] = 1; // lower than head
		hitBox[2] = w-hitBox[0]; // so that it takes the same off the right side
		hitBox[3] = h; // so that it rests on the bottom exactly

		inventory = new Inventory(board, this);
	}





































public void setupHourglass(){
	name = "main_boy";

	//weapons.add(new FireSet1());
	//weapons.add(new FireSet2());
	//weapons.add(new AgilitySet1());
	importSequence("still");
	importSequence("run");
	importSequence("jump");
	importSequence("fall");
	importSequence("attack1");
	importSequence("attack2");

	//importSequence("walk");
	//importSequence("wallClimb");

	originalW = 26;
	originalH = 30;
	w = originalW;
	h = originalH;
	drawW = w;
	drawH = h;
	totalJumps = 1;
	canJump = true;

	xAcceleration = 0.07;
	xMaxSpeed = 2.0;
	jumpAcceleration = 1.9;
	jumpMaxSpeed = 4.2;
	fallSpeed = 0.2;
	fallMaxSpeed = 2.9;

	moveState = MoveState.STILL;
	moveStateFrame = 0;
	facing = 1;
	healthMax = 25;
	health = healthMax;
	energyMax = 100;
	energyRechargeSpeed = 0.4;
	energy = energyMax;
	//manaMax = 100;
	//manaRechargeSpeed = 0.01;
	//mana = manaMax;
	invincibleTime = 150;


	hitBox[0] = 3; // taken off the left side (each side)
	hitBox[1] = 1; // lower than head
	hitBox[2] = w-hitBox[0]; // so that it takes the same off the right side
	hitBox[3] = h; // so that it rests on the bottom exactly

	inventory = new Inventory(board, this);
}
}