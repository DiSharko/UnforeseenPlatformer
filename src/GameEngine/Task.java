package GameEngine;

import GameEngine.CharacterTemplate.MoveState;

public class Task {

	CharacterTemplate host;
	boolean delete = false;

	String type;
	Board board;
	Conversation conversation;
	String text = "";
	double x;
	double y;
	byte direction = 0;
	String movement = "";

	boolean amble = false;
	boolean attackEnemies = false;

	int time = 0;

	int aiInterval1 = 0;
	int aiInterval2 = 0;
	int aiInterval3 = 0;

	String string1;
	String target;
	String targetValue;

	boolean face = true;

	double jumpHeight;

	double leftEnd;
	double rightEnd;

	double originalSpeed = 2;
	double speed;

	public Task(Board b, CharacterTemplate h, String p){

		host = h;
		board = b;

		originalSpeed = host.aiSpeed;

		String[] props = p.split(";");
		boolean exit = false;
		for (int i = 0; !exit; i++){
			String[] settings = {};
			try{
				settings = props[i].split(":"); //"type:amble;x:100;floor:true;delay:1"

				if (settings[0].equals("type")){
					type = settings[1];
				} else if (settings[0].equals("x")){
					x = Double.parseDouble(settings[1]);
				} else if (settings[0].equals("y")){
					y = Double.parseDouble(settings[1]);
				} else if (settings[0].equals("direction")){
					direction = Byte.parseByte(settings[1]);
				} else if (settings[0].equals("movement")){
					movement = settings[1];
				} else if (settings[0].equals("leftEnd")){
					leftEnd = Double.parseDouble(settings[1]);				
				} else if (settings[0].equals("rightEnd")){
					rightEnd = Double.parseDouble(settings[1]);
				} else if (settings[0].equals("speed")){
					originalSpeed = Double.parseDouble(settings[1]);
				} else if (settings[0].equals("time")){
					time = Integer.parseInt(settings[1]);
				} else if (settings[0].equals("text")){
					text = settings[1];
				} else if (settings[0].equals("face")){
					face = Boolean.parseBoolean(settings[1]);
				} else if (settings[0].equals("conversation")){
					string1 = settings[1];
				} else if (settings[0].equals("attackEnemies")){
					attackEnemies = true;
				} else if (settings[0].equals("target")){
					target = settings[1];
					targetValue = settings[2];
				} else if (settings[0].equals("amble")){
					amble = true;
				}
			} catch (IndexOutOfBoundsException e){
				exit = true;
			} catch (Exception e){
				Console.out("Error with the value of \"" + settings[0]+ "\": "+settings[1]+".");
			}
		}

		speed = originalSpeed;

		initializeTask();
		

	}

	public void initializeTask(){
		try{
			if (type.equals("amble")){
			} else if (type.equals("crazy")){
				speed = Math.pow(Math.random(),2)*2.5+1;
				aiInterval1 = (int)(Math.random()*10+20);
				jumpHeight = Math.random()*1.5+1;
			} else if (type.equals("walkTo")){
			} else if (type.equals("flyAround")){
				aiInterval1 = (int)(Math.random()*200+50);
				aiInterval2 = (int)(Math.random()*100+20);
				host.dirMov = randomDir();
				host.facing = host.dirMov;
			} else if (type.equals("moveTo")){
			} else if (type.equals("patrol")){
			} else if (type.equals("mute")){
			} else if (type.equals("drift")){
			} else if (type.equals("unmute")){
			} else if (type.equals("face")){
			} else if (type.equals("remove")){
			} else if (type.equals("converse")){
				conversation = new Conversation(text, "");
			} else if (type.equals("findConversation")){
			} else if (type.equals("tracker")){
			} else if (type.equals("waitFor")){
			} else {
				delete = true;
				Console.out("Couldn't find the \""+type+"\" task.");
			}
		} catch (NullPointerException e){
			delete = true;
			Console.out("No type given for one of "+host.name+"'s tasks.");
		}
	}
	public void amble(){
		checkHits(true);
		if (attackEnemies){
			checkForEnemies();
		}
		if (time>=aiInterval1){
			if (time == aiInterval1){
				host.dirMov = randomDir();
				host.facing = host.dirMov;
			}
			if (host.dirMov!=0){
				host.facing = host.dirMov;
			}
			host.dx = host.dirMov*speed;
			if (time>aiInterval1+aiInterval2){
				time = 0;
				speed = Math.random()*0.5+0.25;
				aiInterval1 = (int)(Math.random()*200+100);
				aiInterval2 = (int)(Math.random()*20+40);
			}
		}
		time++;
	}
	
	public void performTask(){
		//Console.out(host.name+": "+type);
		
		if (type.equals("amble")){
			amble();


		} else if (type.equals("flyAround")){
			time++;
			if (host.dy != 0 || host.dx !=0){
				host.moveState = MoveState.FLYING;
			} else {
				host.moveState = MoveState.STILL;
			}
			checkHits(false);
			host.facing = host.dirMov;

			if (aiInterval3 == 0){
				if (host.dy == 0){
					if (Function.fullHitTest(host, board.game.player, 64,64) && (board.game.player.dx != 0 || board.game.player.dy != 0)){
						time = aiInterval1+1;
						aiInterval3 = 20;
						host.dy -= 0.2;
						if (board.game.player.x>host.x){	host.dirMov = -1;	} else {	host.dirMov = 1;	}
					}
				}
			} else {	if (aiInterval3>0){	  aiInterval3--;	}	}
			
			if (time > aiInterval1){
				if (time < aiInterval2+aiInterval1){
					host.dy-=Math.random()*(.2);
					host.dx+=host.dirMov*(Math.random()*.1);
				} else if (time >= aiInterval2+aiInterval1){
					host.dx+=host.dirMov*Math.random()*(.08);
					host.dy-=host.dirMov*Math.random()*(.05);
					if (Math.abs(host.dy) < 0.01){
						host.dy = 0;
						aiInterval1 = (int)(Math.random()*500+100);
						aiInterval2 = (int)(Math.random()*100+20);
						host.dirMov = randomDir();
						host.facing = host.dirMov;
						time = 0;
					}
				}
			} else if (Math.random()<.01){	host.dirMov = randomDir();	}

			
		} else if (type.equals("crazy")){
			checkHits(true);
			if (time> aiInterval1 && host.floorHit){
				time = 0;
				host.dy -= jumpHeight;
				aiInterval1 = (int)(Math.random()*10+20);
				jumpHeight = Math.random()*1.5+1;
			}
			time++;
			host.dx = host.dirMov*speed;

			
		} else if (type.equals("patrol")){
			checkHits(true);
			time++;
			host.dx = host.dirMov*speed;

			
		} else if (type.equals("drift")){	host.dx = host.facing*speed;

		
		} else if (type.equals("walkTo")){
			speed = originalSpeed;

			if (Math.abs(host.x-x) < speed){
				if (movement.equals("decelerate")){speed = Math.abs(host.x-x)/2;					
				} else {speed = Math.abs(host.x-x);}}

			if (Math.abs(host.x-x) < speed/8){	host.x = x;  }			
			
			if (host.x > x){ 			host.dx = -speed; host.facing = -1;
			} else if (host.x < x){ 	host.dx = speed; host.facing = 1;
			} else {					host.dx = 0;}

			if (host.leftHit || host.rightHit){host.dy = -1;}

			if (Math.abs(host.x-x) <= speed && Math.abs(host.y-y) <= 1){delete = true;}
			
			if (Math.abs(host.x-x) <= originalSpeed){
				time++;
				if (time == 10){ delete = true;	  }
			} else {	time = 0;	 }

			
		} else if (type.equals("moveTo")){
			try {
				host.x = x;
				host.y = y;
			} catch (NullPointerException e){Console.out("A coordinate was undefined for moveTo.");}
			delete = true;
			
			
		} else if (type.equals("mute")){
			host.muted = true;
			delete = true;
		
		
		} else if (type.equals("unmute")){
			host.muted = false;
			delete = true;
		
			
		} else if (type.equals("face")){
			if (direction != 0){	host.facing = direction;
			} else if (x > host.x){	host.facing = 1;
			} else if (x < host.x){ host.facing = -1; }
			delete = true;
			
		
		} else if (type.equals("remove")){
			host.delete = true;
			delete = true;
		
		
		} else if (type.equals("converse")){
			if (!board.dialog.visible){
				CharacterTemplate _target = board.game.quest.getCharacter(target);
				if (_target == null){	_target = board.game.player;	}
				if (face){	Function.faceSprites(_target,host);	}
				conversation.useConversation(board, host);
				delete = true;
			}
		
		
		} else if (type.equals("findConversation")){
			Function.faceSprites(host, board.game.player);
			host.findConversation(string1).useConversation(board, host);
			host.removeConversation(string1);
			delete = true;
		
		
		} else if (type.equals("waitFor")){
			if (amble){amble();}
			if (board.game.getTracker(target).equals(targetValue)){		delete = true;		}
		
		
		} else if (type.equals("tracker")){
			board.game.addTracker(target, targetValue);		delete = true;

		
		}
	}

	
	
	public byte randomDir(){return (byte) (Math.pow(-1, (int)(Math.random()*2)));	}
	
	
	public void checkHits(boolean checkBounds){
		if ((checkBounds && host.x < leftEnd) || host.leftHit){
			host.dirMov = 1;
			host.facing = 1;
		} else if ((checkBounds && host.x > rightEnd) || host.rightHit){
			host.dirMov = -1;
			host.facing = -1;
		} else if (host.dirMov == 0){
			host.dirMov = randomDir();
			host.facing = host.dirMov;
		}
	}
	public void checkForEnemies(){
		for (int i = 0; i<board.game.theMap.charData.size(); i++){
			CharacterTemplate current = board.game.theMap.charData.get(i);
			if (current.team != host.team){
				if (Function.fullHitTest(current, host,128,0)){
					try{
						host.weapons.get(host.currentWeapon).attack1();
					}catch(Exception e){e.printStackTrace();}
				}
			}
		}
	}
}