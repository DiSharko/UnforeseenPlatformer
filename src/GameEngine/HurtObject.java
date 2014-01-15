package GameEngine;

public class HurtObject extends SpecialObject {
	
	double damage = 0;
	
	public HurtObject (double xpos, double ypos, int width, int height, double d){
		//sObjectType = "hurtObject";
		damage = d;
		x = xpos;
		y = ypos;
		w = width;
		h = height;
		setDefaultHitBox();
	}
	
	public void onHit(Board board, Map map, CharacterTemplate current){
		if (current.invincibleTime == 0){
			current.takeDamage(map, damage);
		}
	}
}
