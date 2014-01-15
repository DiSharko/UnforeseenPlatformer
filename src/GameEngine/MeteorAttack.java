package GameEngine;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class MeteorAttack extends Attack {
	int key;
	//int[] directions = {6,1,5,2,4,3};
	int[] directions = {4,5,6};

	public MeteorAttack(CharacterTemplate current, int k) {
		//manaCost = 3;
		energyCost = 2;
		if (!createAttack(current)){
			delete();
		}
		if (!delete){
			registerCaster();

			//lockCaster();

			particles = new ArrayList<SpecialObject>();
			pDelay = 10;

			startX = current.x+current.w*current.facing;
			startY = current.y+current.h/2-11;
			everywhere = true;

			facing = current.facing;

			team = current.team;

			damage = 20;

			key = k;
		}
	}
	public void always(Map map, boolean[] KeysDown, int[] KeysDownTimer){

		if (pTimer>0){
			pTimer--;
		}
		/*if (caster.dx != 0 || caster.dy!= 0){
			pTimer = -1;
		}*/
		if (pTimer == 0){
			if (KeysDown[key]){
				if (/*caster.mana >= manaCost &&*/ caster.energy >= energyCost){
					caster.energy -= energyCost;
					//caster.mana -= manaCost;
					particles.add(new Generic(null,"fireball",0,0,0,0,""));
					pProduced++;

					//int n = particles.size()-1;
					SpecialObject current = particles.get(particles.size()-1);
					current.id = ""+directions[pProduced%3];

					current.x = caster.x;
					current.y = caster.y;
					current.startX = caster.x;
					current.startY = caster.y;
					current.facing = caster.facing;
					pTimer = pDelay;
				}
			} else {
				pTimer = -1;
			}
		}
		if (pTimer == -1){
			releaseCaster();
		}

		for (int i = 0; i < particles.size(); i++){
			SpecialObject current = particles.get(i);

			current.time++;
			current.x = current.startX + current.facing*0.02*Math.pow(current.time,2);
			current.y = current.startY - current.time*(3-Integer.parseInt(current.id));

			if (Math.abs(current.x - current.startX) > 1280 || Math.abs(current.y - current.startY)>1280){
				current.delete = true;
			}

			for (int j = 0; j < map.tileData.size(); j++){
				if (Function.fullHitTest(current, map.tileData.get(j),1,1)){
					current.delete = true;
				}
			}

			if (current.delete){
				particles.remove(i);
				if (particles.size() == 0 && (pDelay == -1 || pTimer == -1)){
					delete();
				}
				break;
			}
		}
	}
	public void draw(Board _board, Map theMap, Graphics2D g2d) {
		for (int i = 0; i< particles.size(); i++){
			SpecialObject current = particles.get(i);
			int fComp = 0;
			if (current.facing == -1)
				fComp = current.w;
			if (Function.checkForDrawing(_board, current)){
				g2d.drawImage(current.sprite, (int) current.x - (int) theMap.xCamera + fComp, (int) current.y - (int) theMap.yCamera, current.w*current.facing, current.h, null);
			}
		}
	}
	public void onHit(Board board, Map map, CharacterTemplate current){
		if (team != current.team && current.deadTime == 0){
			for (int j = 0; j < particles.size(); j++){
				if (Function.fullHitTest(current, particles.get(j),-1,-1)){
					particles.get(j).delete = true;
					current.health -= damage;
					map.specialData.add(new DamageReporter(current, damage));
				}
			}
		}
	}
}
