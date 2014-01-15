package GameEngine;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class Attack extends SpecialObject {
	double energyCost = 0;
	//double manaCost = 0;
	CharacterTemplate caster;

	ArrayList<SpecialObject> particles;
	int pProduced = 0;
	int pLimit;
	int pDelay;
	int pTimer;

	double damage;
	int team;

	double dx;
	double dy;
	public void move(){
		x += dx;
		y += dy;
	}
	public void onHit(Board board, Map map, CharacterTemplate current){
		if (team != current.team && current.deadTime == 0){
			if (!delete){
				current.takeDamage(map, this);
				delete();
			}
		}
	}
	public void always(Map map, boolean[] KeysDown, int[]KeysDownTimer){
		time++;
		move();
		if (Math.abs(x - startX) > 1280 || Math.abs(y - startY)>1280){
			delete();
		}
		
		for (int i = 0; i < map.tileData.size(); i++){
			if (Function.fullHitTest(this, map.tileData.get(i),-1,-1)){
				delete();
			}
		}
	}
	public void draw(Board _board, Map theMap, Graphics2D g2d) {
		int fComp = 0;
		if (facing == -1)
			fComp = w;
		g2d.rotate(rotation);
		g2d.drawImage(sprite, (int) x - (int) theMap.xCamera + fComp, (int) y - (int) theMap.yCamera, w*facing, h, null);
	}
	public boolean createAttack(CharacterTemplate current){
		if (current.energy >= energyCost){// && current.mana >= manaCost){
			
			current.energy -= energyCost;
			//current.mana -= manaCost;
			
			caster = current;
			return true;
		} else {
			caster = current;
			registerCaster();
			current.attackNumber = -1;
			delete();
			return false;
		}
	}
	public void registerCaster(){
		caster.attackLocker = this;
	}
	public void registerCaster(int time){
		caster.attackLocker = this;
		caster.attackAnimated = time;
	}
	public void lockCaster(){
		caster.dx = 0;
		caster.attackLocked = -1;
		caster.attackLocker = this;
		caster.attackAnimated = -1;
	}
	public void lockCaster(int time){
		caster.dx = 0;
		caster.attackLocker = this;
		caster.attackLocked = time;
		caster.attackAnimated = time;
	}
	public void releaseCaster(){
		if (caster.attackLocker == this){
			caster.attackNumber = -1;
			caster.attackAnimated = 0;
			caster.attackLocked = 0;
			//Console.out("RELEASED");
		} else {
			//Console.out("FAILED TO RELEASE");
		}
	}
	
	public void delete(){
		delete = true;
		releaseCaster();
	}	
}