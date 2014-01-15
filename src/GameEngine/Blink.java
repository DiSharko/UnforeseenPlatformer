package GameEngine;

public class Blink extends Attack {

	double dir = 0;
	int dirTime = 10;
	int key;
	int phase = 1;
	int count = 1;
	boolean done = false;
	Boolean xAxis = true;

	public Blink (Map map, boolean[] KeysDown, CharacterTemplate current, int k){
		energyCost = 100;
		//manaCost = 5;

		createAttack(current);

		if (!delete){
			lockCaster();
			key = k;
			x = caster.x+caster.hitBox[0];
			y = caster.y+caster.hitBox[1];
			w = caster.hitBox[2];
			h = caster.hitBox[3]-2;
			if (KeysDown[0] && !(KeysDown[2]||KeysDown[3])){
				xAxis = false;
			} else {
				dir = caster.facing;
			}
			//map.frontSpecialData.add(new Animation("blink",current.x, current.y, current.w, current.h,"changeDelay:0;singleCycle"));
		}
		setDefaultHitBox();
	}
	public void always(Map map, boolean[] KeysDown, int[] KeysDownTimer){
		caster.dx = 0;
		caster.dy = -caster.fallSpeed;

		if (KeysDown[0] && !(KeysDown[2]||KeysDown[3])){
			xAxis = false;
		} else if (!KeysDown[0] && (KeysDown[2]||KeysDown[3])){
			xAxis = true;
			dir = caster.facing;
		}
		if (phase == 1){

			if (count < 5){
				count++;
			} else {
				count = 1;
				phase = 2;
			}
		} else if (phase == 2){
			int _distance = 384;
			for (double i = 1; i < _distance; i++){
				for (int j = 0; j < map.tileData.size(); j++){
					if (!xAxis){
						if (map.tileData.get(j).ceiling){
							if (Function.ceilingHitTest(this, map.tileData.get(j))){
								done = true;
								break;
							}
						}
					} else if (dir == 1){
						if (map.tileData.get(j).left){
							if (Function.leftHitTest(this, map.tileData.get(j))){
								done = true;
								break;
							}
						}
					} else if (dir == -1){
						if (map.tileData.get(j).right){
							if (Function.rightHitTest(this, map.tileData.get(j))){
								done = true;
								break;
							}
						}
					}

				}
				if (done){
					break;
				}
				if (xAxis){
					x+=dir/2;
					if ((i/2)%(caster.w)==1){
						//						map.frontSpecialData.add(new Animation("blink",x, y-caster.h/2, caster.w, caster.h,"changeDelay:0;singleCycle"));
						Generic ghost = new Generic(map, "ghost",x, caster.y, caster.w, caster.h,caster.name+";"+caster.facing);
						ghost.alpha = (float)(0.3+i/_distance/2);
						map.specialData.add(ghost);
					}
				} else {
					y-=0.5;
					if ((i/2)%(caster.h)==0){
						//map.frontSpecialData.add(new Animation("blink",x, y-caster.h/2, caster.w, caster.h,"changeDelay:0;singleCycle"));
						map.specialData.add(new Generic(map, "ghost",x-caster.hitBox[0], y-caster.hitBox[1], caster.w, caster.h,caster.name+";"+caster.facing));
					}
				}

			}
			if (xAxis){
				caster.x = x;	
			} else {
				caster.y=y;
			}
			count = 1;
			phase = 3;
		} else if (phase == 3){
			if (count < 15){
				count++;
			} else {
				delete();
			}
		}
	}
	public void onHit(Board board, Map map, CharacterTemplate current){}
}
