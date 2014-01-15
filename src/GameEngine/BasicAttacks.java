package GameEngine;

import GameEngine.CharacterTemplate.MoveState;

public class BasicAttacks extends Weapon {

	public BasicAttacks(Boolean a1, Boolean a2, Boolean a3){
		name = "Attacks";
		a1enabled = a1;
		a2enabled = a2;
		a3enabled = a3;
		
		/*try {
			sprite = new ImageIcon(Board.class.getResource("/media/images/bit16/graphics/fireballGun.png")).getImage();
		} catch(NullPointerException e){
			Console.out("SOMETHING HAPPENED TO OUR FIREBALL GUN!!");
		}*/
		//w = 22;
		//h = 16;
	}
	
	
	public void handleAttack(Map map, CharacterTemplate host, boolean[] KeysDown, int[] KeysDownTimer){	
		if (KeysDown[9]){
			if (KeysDownTimer[9] == 1){
				if (a1enabled){
					host.attackNumber = 0;
					map.specialData.add(new Punch(host, 10));
					host.moveState = MoveState.ATTACKING;
				}
				KeysDownTimer[9]++;
			}
		}
		if (KeysDown[8]){
			if (KeysDownTimer[8] == 1){
				if (a2enabled){
					host.attackNumber = 0;
					map.specialData.add(new Blink(map, KeysDown, host, 10));
					host.moveState = MoveState.ATTACKING;

				}
				KeysDownTimer[8]++;
			}
		}
		if (KeysDown[7]){
			if (KeysDownTimer[7] == 1){
				if (a3enabled){
					host.attackNumber = 0;
					map.specialData.add(new Blink(map, KeysDown, host, 10));
					host.moveState = MoveState.ATTACKING;
				}
				KeysDownTimer[7]++;
			}
		}
	}
}
