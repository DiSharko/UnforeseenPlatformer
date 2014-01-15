package GameEngine;

public class Punch extends Attack {
	double dir = 0;
	int count = 1;
	boolean done = false;
	int range = 20;
	
	public Punch (CharacterTemplate current, double _damage){
		energyCost = 20;
		name = "punch";
		
		createAttack(current);
	
		if (!delete){
			registerCaster(-1);
			damage = _damage;
			team = caster.team;
			
			dir = caster.facing;
			caster.dx+=dir*2;
			caster.x+=dir*2;
		
			x = caster.x+caster.hitBox[0];
			y = caster.y+caster.hitBox[1]+caster.h/2;
			w = 2;
			h = 2;
			if (dir == -1){
				x-=caster.hitBox[2];
			}
			range+=caster.w;
		}
		setDefaultHitBox();
	}
	public void always(Map map, boolean[] KeysDown, int[]KeysDownTimer){
		try{
			caster.sprite = caster.getSequence("punch").get(0);
		} catch (Exception e){
			e.printStackTrace();
		};
		w+=dir*3;
		if (dir == -1){
			x-=dir*3;
		}
		range-=3;
		if (range <= 0){
			delete();
		}
		setDefaultHitBox();
	}
}
