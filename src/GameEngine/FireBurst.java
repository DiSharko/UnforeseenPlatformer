package GameEngine;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class FireBurst extends Attack {
	public FireBurst(CharacterTemplate current) {
		energyCost = 50;
		//manaCost = 5;

		createAttack(current);

		if (!delete){
			lockCaster(1000);
			particles = new ArrayList<SpecialObject>();
			pLimit = 6;
			startX = caster.x;
			startY = caster.y+caster.h/2-11;
			x = -500000+caster.x;
			y = -500000+caster.y;
			w = 1000000;
			h = 1000000;
			facing = caster.facing;

			team = caster.team;

			damage = 20;
		}
		setDefaultHitBox();
	}
	public void always(Map map, boolean[] KeysDown, int[]KeysDownTimer){
		if (caster.attackLocked > 0){
			caster.dx = 0;
			caster.dy = 0;
		}
		if (pProduced<pLimit){
			particles.add(new Generic(null,"fireball2",0,0,0,0,""));
			pProduced++;

			int n = particles.size()-1;
			particles.get(n).id = ""+n;

			particles.get(n).x = startX;
			particles.get(n).y = startY;
			particles.get(n).facing = facing;
		}
		for (int i = 0; i < particles.size(); i++){
			SpecialObject current = particles.get(i);

			current.time++;
			current.x = startX + current.facing*(2.5*current.time*Math.sin(Integer.parseInt(current.id)+1));
			current.y = startY + (2.5*current.time*Math.cos(Integer.parseInt(current.id)+1));

			if (Math.abs(current.x - startX) > 1280 || Math.abs(current.y - startY)>1280){
				current.delete = true;
			}

			for (int j = 0; j < map.tileData.size(); j++){
				if (Function.fullHitTest(current, map.tileData.get(j),-1,-1)){
					current.delete = true;
				}
			}

			if (current.delete){
				particles.remove(i);
				if (particles.size() == 0 && pProduced == pLimit){
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
				if (Function.fullHitTest(current, particles.get(j),1,1)){
					particles.get(j).delete = true;
					current.health -= damage;
					map.specialData.add(new DamageReporter(current, damage));
				}
			}
		}
	}
}
