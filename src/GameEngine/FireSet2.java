package GameEngine;

public class FireSet2 extends Weapon {
	public FireSet2(){
		name = "Advanced Fire";
		w = 22;
		h = 16;
	}
	
	public void attack1(Map map, CharacterTemplate current){
		current.attackNumber = 0;
		map.specialData.add(new MeteorAttack(current, 9));
		//map.specialData.add(new FireSnake(current, 9));
	}
	public void attack2(Map map, CharacterTemplate current){
		current.attackNumber = 0;
		map.specialData.add(new FireSpiral(current));
	}
	public void attack3(Map map, CharacterTemplate current){
		current.attackNumber = 0;
		map.specialData.add(new FireSnake(current, 7));
	}
}
