package GameEngine;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class FireSpiral extends Attack {
	public FireSpiral(CharacterTemplate current) {
		energyCost = 10;
		//manaCost = 5;
		
		if (!createAttack(current)){
			delete();
		}
		if (!delete){
			lockCaster();

			particles = new ArrayList<SpecialObject>();
			pLimit = 10;
			pDelay = 1;
			startX = caster.x;
			startY = caster.y+caster.h/2-11;
			everywhere = true;
			facing = caster.facing;

			team = caster.team;

			damage = 2;
		}
		setDefaultHitBox();
	}

	public void always(Map map, boolean[] KeysDown, int[]KeysDownTimer){

		caster.dx = 0;
		caster.dy = 0;

		if (pTimer>0){
			pTimer--;
		}
		if (pTimer == 0){
			if (pProduced<pLimit){
				particles.add(new Generic(null,"fireball2",0,0,0,0,""));
				pProduced++;

				int n = particles.size()-1;

				particles.get(n).x = startX;
				particles.get(n).y = startY;
				particles.get(n).facing = facing;
				pTimer = pDelay;
			} else {
				pTimer = -1;
			}
		}
		
		for (int i = 0; i < particles.size(); i++){
			SpecialObject current = particles.get(i);

			current.time++;

			double tT = current.time/6;

			//current.x = startX + (-3*tT*Math.sin(tT))*current.facing;
			current.x += -(Math.sin(tT)+tT*Math.cos(tT))/2*current.facing;
			//current.y = startY + (3*tT*Math.cos(tT));
			current.y += (Math.cos(tT)-tT*Math.sin(tT))/2;

			if (Math.abs(current.x - startX) > 1280 || Math.abs(current.y - startY)>1280){
				current.delete = true;
			}
			if (current.time > 300){
				current.delete = true;
			}

			/*for (int j = 0; j < map.tileData.size(); j++){
				Tile tile = map.tileData.get(j);
				if (tile.left || tile.right){
					if (Physics.fullHitTest(current, tile,1,1)){
						current.delete = true;
					}
				}
			}*/

			if (current.delete){
				particles.remove(i);
			}
			if (particles.size() == 0 && (pDelay == -1 || pTimer == -1)){
				delete();
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
