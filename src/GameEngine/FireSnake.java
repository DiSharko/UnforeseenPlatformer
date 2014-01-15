package GameEngine;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class FireSnake extends Attack {
	int key;

	public FireSnake(CharacterTemplate current, int k) {
		energyCost = 8;
		//manaCost = 1;

		if (!createAttack(current)){
			delete();
		}
		if (!delete){
			lockCaster();

			particles = new ArrayList<SpecialObject>();
			//pLimit = 20;
			pDelay = 10;

			startX = current.x+current.w*current.facing;
			startY = current.y+current.h/2-11;
			x = -500000+current.x;
			y = -500000+current.y;

			w = 1000000;
			h = 1000000;

			facing = current.facing;

			team = current.team;

			damage = 10;

			key = k;
			
		}
		setDefaultHitBox();
	}
	public void always(Map map, boolean[] KeysDown, int[]KeysDownTimer){

		if (pTimer>0){
			pTimer--;
		}
		if (caster.dx != 0 || caster.dy!= 0){
			pTimer = -1;
		}
		if (pTimer == 0){
			//if (pProduced<pLimit && KeysDown[key]){
			if (KeysDown[key]){
				if (/*caster.mana >= manaCost && */caster.energy >= energyCost){
					caster.energy -= energyCost = 20;
					//caster.mana -= manaCost;
					
					particles.add(new Generic(null,"fireball2",0,0,0,0,""));
					pProduced++;

					int n = particles.size()-1;
					particles.get(n).x = startX;
					particles.get(n).y = startY;
					particles.get(n).facing = facing;
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
			current.x = startX + current.facing*(20*Math.sin(current.time/16)+2*current.time);
			current.y = startY + (20*Math.cos(current.time/15)-20);

			if (Math.abs(current.x - startX) > 1280 || Math.abs(current.y - startY)>1280){
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
			if (facing == -1)
				fComp = current.w;
			if (Function.checkForDrawing(_board, current)){
				g2d.drawImage(current.sprite, (int) current.x - (int) theMap.xCamera + fComp, (int) current.y - (int) theMap.yCamera, current.w*facing, current.h, null);
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
