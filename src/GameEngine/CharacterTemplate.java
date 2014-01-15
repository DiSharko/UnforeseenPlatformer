package GameEngine;

import java.awt.Image;
import java.util.ArrayList;


public class CharacterTemplate extends GameObject {
	boolean arrowVisible = false;

	DamageReporter damageReporter;
	DamageReporter energyReporter;
	//DamageReporter manaReporter;
	DialogReporter dialogReporter;

	boolean interacting = false;
	Board board;

	// HEALTH/ENERGY/MANA STATS VARIABLES
	double energy = 0;		// Short term energy
	double energyRechargeSpeed = 0;
	double energyMax = 0;	// Short term energy capacity
	//double mana = 0;		// Long term energy
	//double manaRechargeSpeed = 0;
	//double manaMax = 0;		// Long term energy capacity
	double health = 100;		// Health
	double healthMax = 0;	// Health capacity

	double collisionDamage = 0;
	boolean displayDamage = true;

	// TIMING VARIABLES
	int time = 0;
	int deadTime = 0;
	int squishTimer = 0;

	// DRAWING INFORMATION VARIABLES
	int drawW;
	int drawH;
	int originalW;
	int originalH;
	int xDisplace = 0;
	int yDisplace = 0;
	byte facing = 1; // left = -1, right = 1;

	// BASIC SPRITE INFO VARIABLES
	String nickname;
	ArrayList<SpriteSequence> animation = new ArrayList<SpriteSequence>();

	ArrayList <ArrayList<Image>> spriteAttacks = new ArrayList<ArrayList<Image>>();

	public enum MoveState {
		STILL, WALKING, RUNNING, JUMPING, FALLING, FLYING, WALLCLIMBING, ATTACKING, DYING, SWIMMING, CLIMBING
	}
	MoveState moveState = MoveState.STILL;
	short moveStateFrame;
	Inventory inventory;

	// ASSORTED MOVEMENT STATE VARIABLES
	String physicsMode = "normal"; //normal, ladder, water, swamp, wallClimb
	int wallSide = 0;
	boolean ladderLock = false;
	Generic ladder;



	// PHYSICS: X MOVEMENT VARIABLES
	double dx = 0;	
	double xAcceleration = 0.04;
	double xMaxSpeed = .9;
	byte dirMov; // left = -1, right = 1;
	double airFriction = .97;
	double groundFriction = .8;

	// PHYSICS: Y MOVEMENT VARIABLES (& JUMPING)
	boolean applyPhysics = true;
	double dy = 0;
	double fallSpeed = 0.08;
	double fallMaxSpeed = 2.3;
	double jumpAcceleration;
	double jumpMaxSpeed;
	boolean jumping = false;
	boolean canJump;
	int jumpTime = 0;
	int jumps = 1;
	int totalJumps = 1;

	// CONTROL/TEAM VARIABLES
	int team;
	boolean controlled;

	// AI VARIABLES
	ArrayList<Task> tasks = new ArrayList<Task>();
	boolean aiInitialized = false;
	String aiMovementType;
	int aiInterval;
	int aiInterval2;
	int aiJumping;
	double aiJumpHeight;
	double aiSpeed;
	// AI CONVERSATIONS
	int dialogType = 0;
	boolean muted = false;
	ArrayList <Conversation> aiConversations = new ArrayList<Conversation>();

	// CONTROL/DISABLED VARIABLES
	int disabled = 0;
	int attackLocked = 0;
	Attack attackLocker;

	// ATTACKING VARIABLES
	int attackNumber = -1;
	int attackAnimated;
	ArrayList<Weapon> weapons = new ArrayList<Weapon>();
	int currentWeapon = 0;

	// COLLISION/INVINCIBILITY VARIABLES
	int invincibleTime;
	int invincibleMaxTime = 0;

	// COLLISION DETECTING VARIABLES
	boolean wallCollisions = true;
	boolean leftHit;
	boolean rightHit;
	boolean floorHit;
	boolean ceilingHit;
	//int xArea;
	//int yArea;


	public void setSprite(){
		try{
			if (health <= 0){
				moveState = MoveState.DYING;
			} else {
				if (moveState!=MoveState.FLYING){
					if (physicsMode.equals("wallClimb")){
						moveState = MoveState.WALLCLIMBING;
					} else if (physicsMode.equals("ladder")){
						moveState = MoveState.CLIMBING;
					} else if (attackAnimated != 0){
						if (attackAnimated>0){
							attackAnimated--;
						}
						moveState = MoveState.ATTACKING;
					} else {
						if (dy>=0.7){
							moveState = MoveState.FALLING;
						} else if (dy<0 || (dy<0.7 && dy>0)){
							moveState = MoveState.JUMPING;
						} else {
							if (dx != 0){
								moveState = MoveState.WALKING;
							} else {
								moveState = MoveState.STILL;
							}
						}
					}
				}
			}

			if (attackNumber != -1){
				if (moveState!= MoveState.ATTACKING){
					attackNumber = -1;
				}
			}

			if (moveState == MoveState.RUNNING){stateRun();}
			else if (moveState == MoveState.JUMPING){stateJump();}
			else if (moveState == MoveState.FALLING){stateFall();}
			else if (moveState == MoveState.WALKING){stateWalk();}
			else if (moveState == MoveState.WALLCLIMBING){stateWallClimb();}
			else if (moveState == MoveState.CLIMBING){stateClimb();}
			else if (moveState == MoveState.ATTACKING){stateAttack();}
			else if (moveState == MoveState.FLYING){stateFly();}
			else if (moveState == MoveState.DYING){stateDie();}
			else {stateStill();}

		} catch(NullPointerException e){
			try {
				stateStill();
			} catch (NullPointerException e2){
				//Console.out("Couldn't find the correct frame for "+moveState+" "+moveStateFrame+1+" nor a still image to display.");
			} catch (IndexOutOfBoundsException e2){
				//Console.out("Couldn't find the correct frame for "+moveState+" "+moveStateFrame+1+" nor a still image to display.");
			}
		} catch(IndexOutOfBoundsException e){
			try {
				stateStill();
			} catch (NullPointerException e2){
				//Console.out("Couldn't find the correct frame for "+moveState+" "+moveStateFrame+1+" nor a still image to display.");
			} catch (IndexOutOfBoundsException e2){
				//Console.out("Couldn't find the correct frame for "+moveState+" "+moveStateFrame+1+" nor a still image to display.");
			}
		}
	}
	public void stateStill(){
		setNormalStateProps();
		sprite = getSequence("still").get(0);
	}
	public void stateRun(){
		setNormalStateProps();
		if (moveStateFrame>=(getSequence("run").size())*30-1){
			moveStateFrame = 0;
		}
		sprite = getSequence("run").get((int)moveStateFrame/30);
		moveStateFrame++;
	}
	public void stateWalk(){
		setNormalStateProps();
		if (moveStateFrame>=(getSequence("run").size())*15-1){
			moveStateFrame = 0;
		}
		sprite = getSequence("run").get((int)moveStateFrame/15);
		moveStateFrame++;
	}
	public void stateFall(){
		setNormalStateProps();
		if (moveStateFrame>=(getSequence("fall").size())*30-1){
			moveStateFrame = 0;
		}
		sprite = getSequence("fall").get((int)moveStateFrame/30);
		moveStateFrame++;
	}
	public void stateJump(){
		setNormalStateProps();
		if (moveStateFrame>=(getSequence("jump").size())*30-1){
			moveStateFrame = 0;
		}
		sprite = getSequence("jump").get((int)moveStateFrame/30);
		moveStateFrame++;
	}
	public void stateWallClimb(){
		w = originalH;
		h = originalW;
		if (wallSide == -1){
			drawW = originalH;
			xDisplace = 0;
		} else if (wallSide == 1) {
			drawW = -originalH;
			xDisplace = originalH;
		}
		drawH = originalW*facing;
		if (dy != 0){
			if (moveStateFrame>=(getSequence("wallClimb").size())*50-1){
				moveStateFrame = 0;
			}
			sprite = getSequence("wallClimb").get((int)moveStateFrame/50);
			moveStateFrame++;		
		} else {
			sprite = getSequence("wallClimb").get(0);
		}
		if (facing == 1){yDisplace = 0;} else {yDisplace = originalW;}
	}
	public void stateAttack(){
		setNormalStateProps();
		if (moveStateFrame>=(spriteAttacks.get(attackNumber).size())*20-1){
			moveStateFrame = 0;
		}
		//sprite = spriteAttacks.get(attackNumber).get((int)moveStateFrame/20);
		moveStateFrame++;
	}
	public void stateFly(){
		setNormalStateProps();
		if (moveStateFrame>=(getSequence("fly").size())*4-1){
			moveStateFrame = 0;
		}
		sprite = getSequence("fly").get((int)moveStateFrame/4);
		moveStateFrame++;
	}
	public void stateClimb(){
		setNormalStateProps();
		if (moveStateFrame>=(getSequence("climb").size()*12-1)){
			moveStateFrame = 0;
		}
		sprite = getSequence("climb").get((int)moveStateFrame/12);
	}
	public void stateDie(){
		setNormalStateProps();
		if (deadTime == 0){
			dy = -1;
		} else if ((deadTime > 200 && Math.abs(dy) < 0.1 && Math.abs(dx) < 0.1) || deadTime > 1000){
			delete = true;
		}
		dx*=.999999;
		deadTime++;
		alpha = (float) (1f-Math.max((deadTime-100.0)/100.0,0)*1f);

		if (moveStateFrame>=(getSequence("die").size())*20-1){
			moveStateFrame = 0;
		}

		sprite = getSequence("die").get((int)moveStateFrame/20);
		moveStateFrame++;
	}
	public void setNormalStateProps(){
		w = originalW;
		h = originalH;
		drawW = w;
		drawH = h;
		drawW *= facing;
		yDisplace = 0;
		if (facing == 1){
			xDisplace = 0;
		} else {
			xDisplace = originalW;
		}
	}

	public void importSequence(String _state){
		animation.add(new SpriteSequence(this, _state));
	}
	public ArrayList<Image> getSequence(String _state){
		for (int i = 0; i<animation.size(); i++){
			if (_state.equals(animation.get(i).state)){
				return animation.get(i).sprite;
			}
		}
		return null;
	}


	public void addSettings(String info){     //c:blah blah blah:q:5;m:amble;
		if (info != null){
			String[] settings = info.split(";");
			for (int i = 0; i < settings.length; i++){
				addSetting(settings[i]);
			}
		}
	}
	private void addSetting(String info){      //m:amble
		String[] settings = info.split(":");
		if (settings[0].equals("preset")){
			usePreset(settings[1]);
		} else if (settings[0].equals("m")){
			aiMovementType = settings[1];
			initializeMovementAI();
		} else if (settings[0].equals("state")){
			importSequence(settings[1]);
		} else if (settings[0].equals("id")){
			id = settings[1];
		} else if (settings[0].equals("nn")){
			nickname = settings[1];
		} else if (settings[0].equals("facing")){
			facing = Byte.parseByte(settings[1]);	
		} else if (settings[0].equals("d")){
			collisionDamage = Double.parseDouble(settings[1]);
		} else if (settings[0].equals("c")){ 
			Conversation c = new Conversation(settings[1], "");
			boolean exit = false;
			try{
				for (int j = 2; !exit; j+=2){
					if (settings[j].equals("a")){
						c.setActivation(Integer.parseInt(settings[j+1]));
					} else if (settings[j].equals("d")){
						c.setDisplay(Integer.parseInt(settings[j+1]));
					} else if (settings[j].equals("q")){
						c.setQuest(Integer.parseInt(settings[j+1]));
					}
				}
			} catch (Exception e){
				exit = true;
			}
			aiConversations.add(c);
		} else if (settings[0].equals("health")){
			health = Double.parseDouble(settings[1]);
		} else if (settings[0].equals("")){
		} else {
			Console.out("--- Unknown setting: "+settings[0]+" ---");
		}


	}


	public void usePreset(String preset){
		if (preset.equals("ambler")){
			importSequence("still");
			importSequence("run");
			importSequence("fall");
			importSequence("jump");
			aiMovementType = "amble";
			initializeMovementAI();
		} else if (preset.equals("person")){
			importSequence("still");
			importSequence("run");
			importSequence("fall");
			importSequence("jump");
		} else if (preset.equals("soldier")){
			importSequence("still");
			importSequence("run");
			importSequence("attack1");
			team = 1;
			weapons.add(new MachineGun(true, false, false));
			addNextTask("type:amble;leftEnd:"+(x-96)+";rightEnd:"+(x+96)+";attackEnemies");
		} else if (preset.equals("turkey")){
			importSequence("still");
			importSequence("run");
			importSequence("fall");
			importSequence("die");
			dialogType = 1;
			team = -1;
			health = 5;
			aiConversations.add(new Conversation("Gobble!", "d:1"));
			aiMovementType = "crazy";
			initializeMovementAI();

		} else if (preset.equals("crow")){
			importSequence("still");
			importSequence("fly");
			dialogType = 1;
			health = 1;
			aiConversations.add(new Conversation("Caw!", "d:1"));
			aiMovementType = "flyAround";
			initializeMovementAI();

		} else if (preset.equals("rabbit")){
			importSequence("still");
			importSequence("jump");
			aiMovementType = "hop";
			health = 5;
			initializeMovementAI();

		} else if (preset.equals("dustball")){
			importSequence("still");
			importSequence("run");
			aiMovementType = "patrol";
			collisionDamage = 10;
			health = 20;
			initializeMovementAI();

		} else if (preset.equals("spirit1")){
			hitBox[0] = 3;hitBox[1] = 4;hitBox[2] = 48-6;hitBox[3] = 32-8;
			importSequence("still");
			importSequence("fly");
			aiMovementType = "drift";
			collisionDamage = 10;
			health = 5;
			fallMaxSpeed = 0;
			wallCollisions = false;
			moveState = MoveState.FLYING;
			initializeMovementAI();

		} else {
			Console.out("--- Unknown preset: "+preset+ " ---");
		}
	}

	public void initializeMovementAI(){
		if (aiMovementType.equals("crazy")){
			addNextTask("type:crazy;leftEnd:"+(x-64)+";rightEnd:"+(x+32)+";");
		} else if (aiMovementType.equals("flyAround")){
			addNextTask("type:flyAround;leftEnd:"+(x-128)+";rightEnd:"+(x+128)+";");
		} else if (aiMovementType.equals("amble")){
			addNextTask("type:amble;leftEnd:"+(x-96)+";rightEnd:"+(x+96)+";");
		} else if (aiMovementType.equals("patrol")){
			addNextTask("type:patrol;leftEnd:"+(x-64)+";rightEnd:"+(x+64)+";speed:1.5");
		} else if (aiMovementType.equals("drift")){
			addNextTask("type:drift;speed:"+(Math.random()*2+1)+"");
		} else {
			Console.out("--- Unknown AI Movement Type: "+aiMovementType+ " ---");
		}
	}
	public void addNextTask(String info){
		tasks.add(new Task(board, this, info));
	}
	public void addPriorityTask(String info){
		tasks.add(0, new Task(board, this, info));
	}
	public void endCurrentTask(){
		try{
			tasks.remove(0);
		} catch (IndexOutOfBoundsException e){
			Console.out("No tasks to remove.");
		}
	}
	public void addTasksList(String[] info){ // tasks are given in chronological order
		for (int i = 0; i<info.length; i++){
			addNextTask(info[i]);
		}
	}
	public void aiControl(){
		for (int i = 0; i<tasks.size(); i++){
			if (!tasks.get(i).delete){
				tasks.get(i).performTask();
				break;
			} else {
				tasks.remove(i);
				i--;
			}
		}
	}

	public void control(boolean[] KeysDown, int[] KeysDownTimer, Map theMap){
		if (disabled == 0 && attackLocked == 0){
			if (physicsMode.equals("wallClimb")){
				wallClimbControl(KeysDown, KeysDownTimer, theMap);
			} else if (physicsMode.equals("ladder")){
				ladderControl(KeysDown, KeysDownTimer, theMap);
			} else if (physicsMode.equals("water")) {
				waterControl(KeysDown, KeysDownTimer, theMap);
			} else {
				moveControl(KeysDown, KeysDownTimer, theMap);
			}
			if (weapons.size() > 0){
				weaponControl(KeysDown, KeysDownTimer, theMap);
			}
			if (KeysDownTimer[1]==1){
				interacting = true;
			} else {
				interacting = false;
			}
		} else {
			if (disabled>0){
				disabled--;
			}
			if (attackLocked>0){
				attackLocked--;
			}
		}
	}
	public void moveControl(boolean[] KeysDown, int[] KeysDownTimer, Map theMap){
		if (KeysDown[2] == true){
			facing = -1;
			//if (!leftHit){
			if(dx > -xMaxSpeed){
				dx -= xAcceleration;
				if (dx<0){
					dx*=1.5;
				} else {
					dx-= xAcceleration;
				}
			} else {
				dx = -xMaxSpeed;
			}
			if (dx <= 0){
				dirMov = -1;
			}
			//}
		}
		if (KeysDown[3] == true){
			facing = 1;
			//if (!rightHit){
			if (dx < xMaxSpeed){
				dx += xAcceleration;
				if (dx>0){
					dx*=1.5;
				} else {
					dx+= xAcceleration;		
				}
			} else {
				dx = xMaxSpeed;
			}
			if (dx >= 0){
				dirMov = 1;
			}
			//}
		}
		if (KeysDown[2]&&KeysDown[3]){
			facing = dirMov;
		}
		if (KeysDown[4] || KeysDown[0]){
			if (!jumping && canJump){
				jumping = true;
				jumps++;
				board.KeysDownTimer[0]++;
			}
		} else if (jumping){
			jumping = false;
			jumpTime = 0;
		}
	}

	public void wallClimbControl(boolean[] KeysDown, int[] KeysDownTimer, Map theMap){
		if (KeysDown[2] == true){
			if (wallSide == -1){
				if(dy > -2*xMaxSpeed){
					dy -= xAcceleration;
					if (dy<0){
						dy*=1.5;
					} else {
						dy-= xAcceleration;
					}
				}
				facing = -1;
				if (dy <= 0){
					dirMov = -1;
				}
			} else if (wallSide == 1){
				if(dy < 2*xMaxSpeed){
					dy += xAcceleration;
					if (dy>0){
						dy*=1.5;
					} else {
						dy+= xAcceleration;
					}
				}
				facing = 1;
				if (dy >= 0){
					dirMov = 1;
				}
			}
		}
		if (KeysDown[3] == true){
			if (wallSide == 1){
				if(dy > -2*xMaxSpeed){
					dy -= xAcceleration;
					if (dy<0){
						dy*=1.5;
					} else {
						dy-= xAcceleration;
					}
				}
				facing = -1;
				if (dy <= 0){
					dirMov = -1;
				}
			} else if (wallSide == -1){
				if(dy < 2*xMaxSpeed){
					dy += xAcceleration;
					if (dy>0){
						dy*=1.5;
					} else {
						dy+= xAcceleration;
					}
				}
				facing = 1;
				if (dy >= 0){
					dirMov = 1;
				}
			}
		}

		if (KeysDown[4] || KeysDown[0]){
			if (!jumping && canJump){
				jumping = true;
				jumps++;
			}
		} else if (jumping){
			jumping = false;
			jumpTime = 0;
		}

		if (!leftHit && wallSide == -1 || !rightHit && wallSide == 1 || KeysDown[1] || floorHit && dy > 0){
			physicsMode = "normal";
			wallSide = 0;
		}
	}
	public void waterControl(boolean[] KeysDown, int[] KeysDownTimer, Map theMap){
		if (KeysDown[2] == true){
			if (!leftHit){
				if(dx > -xMaxSpeed/2){
					dx -= xAcceleration/2;
					if (dx<0){
						dx*=1.5;
					} else {
						dx-= xAcceleration/2;
					}
				}
				facing = -1;
				if (dx <= 0){
					dirMov = -1;
				}
			}
		}
		if (KeysDown[3] == true){
			if (!rightHit){
				if (dx < xMaxSpeed){
					dx += xAcceleration/2;
					if (dx>0){
						dx*=1.5;
					} else {
						dx += xAcceleration/2;		
					}
				}
				facing = 1;
				if (dx >= 0){
					dirMov = 1;
				}
			}
		}
		if (KeysDown[2]&&KeysDown[3]){
			facing = dirMov;
		}
		if (KeysDown[4] || KeysDown[0]){
			if (!jumping && canJump){
				jumping = true;
			}
		} else if (jumping){
			jumping = false;
			jumpTime = 0;
		}
	}

	public void ladderControl(boolean[] KeysDown, int[] KeysDownTimer, Map theMap){
		if (ladderLock){
			if (KeysDown[0]){ // up, climb the ladder
				board.KeysDownTimer[0]++;
				dy = 0;
				dy = 0;
				y-=3;
				moveStateFrame++;
				while (!Function.fullHitTest(this, ladder)){
					y++;
				}
			}

			if (KeysDown[1]){ // down, slide down the ladder
				board.KeysDownTimer[1]++;
				y+=4;
				moveStateFrame++;
			}
			if (KeysDown[2]){
				facing = -1;
				x-=2;
				moveStateFrame++;
			}
			if (KeysDown[3]){
				facing = 1;
				x+=2;
				moveStateFrame++;
			}
		}
	}	
	public void physics(boolean[] KeysDown){
		if (invincibleTime>0){
			invincibleTime--;
			visible++;
			if (visible>3){
				visible = 1;
			}
			if (invincibleTime == 0){
				visible = 1;
			}
		}
		if (applyPhysics){
			if (physicsMode.equals("wallClimb")){
				wallClimbPhysics(KeysDown);
			} else if (physicsMode.equals("water")) {
				waterPhysics(KeysDown);
			} else if (physicsMode.equals("ladder")){
				if (floorHit){
					physicsMode = "normal";
				}
				if (ladderLock){
					dy = -fallSpeed;
				}

				normalPhysics(KeysDown);
				canJump = true;
				jumping = false;
				jumps = 0;
				jumpTime = 0;

			} else {
				ladderLock = false;
				normalPhysics(KeysDown);
			}
		}
	}
	public void waterPhysics(boolean[] KeysDown) {
		if ((!KeysDown[2] && dirMov == -1) || (!KeysDown[3] && dirMov == 1)){
			if (!floorHit){
				dx*=.97;
			} else {
				dx*=.85;
			}
		}

		// JUMPING
		if (jumping){
			if (jumpTime == 0){
				if (dy > 0){
					dy = 0;
				}
			}
			jumpTime++;
			if (dy > -jumpMaxSpeed/2){
				dy-= jumpAcceleration/jumpTime/4*3;
			} else {
				dy = -jumpMaxSpeed/2;
			}
		}
		if (!jumping && floorHit){
			canJump = true;
			jumping = false;
			jumps = 0;
			jumpTime = 0;
		}
		if (!floorHit && jumps == 0){
			jumps++;
		}
		if (jumps >= totalJumps){
			canJump = false;
		}
		// END JUMPING

		if (floorHit == false){
			if (dy<fallMaxSpeed/3){
				dy += fallSpeed/4*3;
			} else {
				dy = fallMaxSpeed/3;
			}
		}	

		if (Math.abs(dx) < xAcceleration/2){
			dx = 0;
			//moveStateFrame = 0;
		}
		if (dx < 0){
			dirMov = -1;
		} else if (dx > 0){
			dirMov = 1;
		}

		x+=dx;
		y+=dy;

	}
	public void normalPhysics(boolean[] KeysDown){
		if ((!KeysDown[2] && dirMov == -1) || (!KeysDown[3] && dirMov == 1)){
			if (!floorHit){
				dx*=airFriction;
			} else {
				dx*=groundFriction;
			}
		}

		// JUMPING
		if (jumping){
			if (jumpTime == 0){
				if (dy > 0){
					dy = 0;
				}
			}
			jumpTime++;
			if (dy > -jumpMaxSpeed){
				dy-= jumpAcceleration/jumpTime;
			}
			/*if (jumpTime > 2100){
				jumping = false;
			}*/
		}
		if (!jumping && floorHit){
			canJump = true;
			jumping = false;
			jumps = 0;
			jumpTime = 0;
		}
		if (!floorHit && jumps == 0){
			jumps++;
		}
		if (jumps >= totalJumps){
			canJump = false;
		}
		// END JUMPING

		if (!floorHit){
			if (dy<fallMaxSpeed){
				dy += fallSpeed;
			}
		}	

		if (Math.abs(dx) < xAcceleration){
			dx = 0;
			//moveStateFrame = 0;
		}
		if (dx < 0){
			dirMov = -1;
		} else if (dx > 0){
			dirMov = 1;
		}

		x+=dx;
		y+=dy;
	}
	public void wallClimbPhysics(boolean[] KeysDown){
		if (wallSide == 0){
			if (leftHit){
				wallSide = -1;
				facing = 1;
			} else if (rightHit) {
				wallSide = 1;
				facing = -1;
			} else {
				physicsMode = "normal";
			}
		}
		if ((!KeysDown[2] && dirMov == -1) || (!KeysDown[3] && dirMov == 1)){
			dy*=.6;
			if (Math.abs(dy)<xAcceleration){
				dy=0;
			}
		}
		// JUMPING
		if (jumping){
			if (jumpTime == 0){
				if (dy > 0){
					dy = 0;
				}
			}
			jumpTime++;
			if (dy > -jumpMaxSpeed){
				dy-= jumpAcceleration/jumpTime;
				Console.out("jumpaway");
				Console.out(wallSide);
				dx = -20*xAcceleration*wallSide;
			}
			physicsMode = "normal";
			wallSide = 0;
		}
		if (!jumping){
			canJump = true;
			jumping = false;
			jumps = 0;
			jumpTime = 0;
		}
		if (jumps == 0){
			jumps++;
		}
		if (jumps >= totalJumps){
			canJump = false;
		}
		// END JUMPING
		x+=dx;
		y+=dy;
	}

	public void weaponControl(boolean[] KeysDown, int[] KeysDownTimer, Map theMap){
		try {
			weapons.get(currentWeapon).handleAttack(theMap, this, KeysDown, KeysDownTimer);
			if (KeysDown[10]){
				if (KeysDownTimer[10] == 1){
					prevWeapon();
					KeysDownTimer[10]++;
				}
			}
			if (KeysDown[11]){
				if (KeysDownTimer[11] == 1){
					nextWeapon();
					KeysDownTimer[11 ]++;
				}
			}
		} catch (IndexOutOfBoundsException e){
			e.printStackTrace();
			Console.out("That weapon or attack is undefined.");
		}
	}
	public void nextWeapon(){
		if (currentWeapon < weapons.size()-1){
			currentWeapon++;
		} else {
			currentWeapon = 0;
		}
	}
	public void prevWeapon(){
		if (currentWeapon > 0){
			currentWeapon--;
		} else {
			currentWeapon = weapons.size()-1;
		}
	}

	public void takeDamage(Map map, Attack _attack){
		if (invincibleTime == 0){
			y-=2;
			dy-=1;
			x-=Math.signum(_attack.x-x)*3;

			health -= _attack.damage;
			if (health < 0){
				_attack.damage += health;
			}
			if (displayDamage){
				map.specialData.add(new DamageReporter(this, _attack.damage));
			}
			invincibleTime = invincibleMaxTime;
		}
	}
	public void takeDamage(Map map, CharacterTemplate _spriteHit){
		if (invincibleTime == 0){
			y-=2;
			dy-=1;
			x+=Math.signum(_spriteHit.x-x)*3;

			health -= _spriteHit.collisionDamage;
			if (health < 0){
				_spriteHit.collisionDamage += health;
			}
			map.specialData.add(new DamageReporter(this, _spriteHit.collisionDamage));
			invincibleTime = invincibleMaxTime;
		}
	}
	public void takeDamage(Map map, double _damage){
		if (invincibleTime == 0){
			y-=2;
			dy-=1;

			health -= _damage;
			if (health < 0){
				_damage += health;
			}
			map.specialData.add(new DamageReporter(this, _damage));
			invincibleTime = invincibleMaxTime;
		}
	}

	public void handleSpriteCollision(Board board, Map map, CharacterTemplate spriteHit){
		//Console.out("I, " + name + ", hit " + spriteHit.name + ".");
		if (deadTime == 0){
			if (team == -1){
				if (!muted && aiConversations.size() > 0){
					converse(board, spriteHit);
				}
				collide(map, spriteHit);
			} else if (spriteHit.team == team){
				if (!muted && aiConversations.size() > 0){
					converse(board, spriteHit);
				}
			} else {
				collide (map, spriteHit);
			}
		}
	}

	public void collide(Map map, CharacterTemplate spriteHit){
		if (collisionDamage>0 && deadTime == 0){
			spriteHit.takeDamage(map, this);
		}
		//if (spriteHit.collisionDamage>0){
		//takeDamage(map, spriteHit.collisionDamage);
		//}
	}

	public Conversation findConversation(String _id){
		for (int i = 0; i<aiConversations.size(); i++){
			if (aiConversations.get(i).id.equals(_id)){
				return aiConversations.get(i);
			}
		}
		return null;
	}
	public void removeConversation(String _id) {
		for (int i = 0; i<aiConversations.size(); i++){
			if (aiConversations.get(i).id.equals(_id)){
				aiConversations.remove(i);
			}
		}
	}

	public void converse(Board board, CharacterTemplate spriteHit){
		if (spriteHit.equals(board.game.player)){
			if (board.KeysDownTimer[1] == 1 && spriteHit.interacting){
				int quest = board.game.quest.questProgress;
				int currentConversation = 0;
				int closestDifference = -1;
				for (int i = 0; i<aiConversations.size(); i++){
					if (quest >= aiConversations.get(i).questNumber){
						if (quest - aiConversations.get(i).questNumber < closestDifference || closestDifference == -1){
							currentConversation = i;
						}
					}
				}
				aiConversations.get(currentConversation).useConversation(board, this);
				Function.faceSprites(this,spriteHit);

				spriteHit.interacting = false;
				board.KeysDownTimer[1]++;
			}
		}
	}

}