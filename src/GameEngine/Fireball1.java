package GameEngine;

import javax.swing.ImageIcon;

public class Fireball1 extends Attack {
	public Fireball1(CharacterTemplate current) {
		try{
			energyCost = 10;
			//manaCost = 1;
			
			createAttack(current);

			if (!delete){
				registerCaster(3);
			}
			
		} catch (NullPointerException e){}
		
		if (!delete){
			try {
				sprite = new ImageIcon(Board.class.getResource("/media/images/graphics/fireball2.png")).getImage();
			} catch(NullPointerException e){
				Console.out("SOMETHING HAPPENED TO OUR FIREBALL!!");
			}

			w = 32;
			h = 22;

			damage = 20;

			try{
				startX = caster.x+caster.w*caster.facing;
				startY = caster.y+caster.h/2-11;
				x = startX;
				y = startY;

				facing = caster.facing;

				team = caster.team;

				dx = caster.dx + 3*facing;
			} catch (NullPointerException e) {

			}
		}
		setDefaultHitBox();
	}
	public void move(){
		//x = startX + (30*Math.sin(time/15)+time)*facing;
		//y = startY + (-30*Math.cos(time/15));
		x = startX + (4*time*facing);
		y = startY + (-200*Math.cos(time/10+4.7)/(time/10+4.7));
	}
}
