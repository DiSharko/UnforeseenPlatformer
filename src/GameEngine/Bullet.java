package GameEngine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Bullet extends Attack {
	public Bullet(CharacterTemplate current) {
		try{
			//energyCost = 10;
			//manaCost = 1;

			createAttack(current);

			if (!delete){
				registerCaster(3);
			}

		} catch (NullPointerException e){}

		if (!delete){
			BufferedImage newSprite = new BufferedImage(6, 3, BufferedImage.TYPE_INT_ARGB);
			Graphics2D graphicBuffer = newSprite.createGraphics();
			graphicBuffer.setColor(Color.white);
			graphicBuffer.fillRect(0,0,6,3);
			graphicBuffer.setColor(Color.black);
			graphicBuffer.drawRect(0, 0, 6, 3);
			sprite = newSprite;

			w = 6;
			h = 3;

			damage = 5;

			try{
				int xOffset = 0;
				if (caster.facing == -1){
					xOffset = caster.w;
				}
				startX = caster.x+xOffset+(caster.weapons.get(caster.currentWeapon).x+caster.weapons.get(caster.currentWeapon).w)*caster.facing;
				startY = caster.y+caster.weapons.get(caster.currentWeapon).y;
				x = startX;
				y = startY;

				facing = caster.facing;

				team = caster.team;

				dx = caster.dx + 5*caster.facing;
			} catch (NullPointerException e) {}
		}
		setDefaultHitBox();
	}
	public void move(){
		x += dx;
		System.out.println(dx);
	}
}
