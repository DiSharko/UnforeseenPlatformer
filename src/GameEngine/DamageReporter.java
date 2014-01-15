package GameEngine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class DamageReporter extends SpecialObject {

	double amount;
	CharacterTemplate host;

	public DamageReporter (CharacterTemplate current, double p){
		try {

			host = current;
			
			if (!host.damageReporter.delete){ // basically check if the reporter is not null
				host.damageReporter.amount += p;
				if (host.damageReporter.time < 10){
					host.damageReporter.time = 0;
				}
				if (host.damageReporter.time > 30){
					host.damageReporter.time = 30;
				}
				delete = true;
			} else {
				throw new NullPointerException();
			}
		} catch (NullPointerException e){
			host.damageReporter = this;

			x = host.x;
			y = host.y-2;

			amount = p;
		}
		setDefaultHitBox();
	}

	public void always(Map map, boolean[] KeysDown, int[]KeysDownTimer){
		time++;
		x = host.x;
		if (time < 60){
			y = host.y-2;
		}
		if (time >= 60){
			y = host.y-.5*(time-60)-2;
			alpha = (float) (1f-Math.max((time-60.0)/20.0,0)*1f);
		}
		if (time > 80){
			delete = true;
			host.damageReporter = null;
		}
	}

	public void draw(Board _board, Map theMap, Graphics2D g2d) {
		if (time >= 10){
			g2d.setColor(Color.red);
			g2d.setFont(new Font("Synchro LET", Font.BOLD, 25));
			String displayDamage = "-"+(int)amount+"";
			g2d.drawString(displayDamage, (int) x - (int) theMap.xCamera, (int) y - (int) theMap.yCamera);
		}
	}
}
