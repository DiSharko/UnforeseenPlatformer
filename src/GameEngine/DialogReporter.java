package GameEngine;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class DialogReporter extends SpecialObject {
	String message = "";
	GameObject host;
	boolean withCharacter = false;
	CharacterTemplate cHost;
	int questAdvancer = -1;

	public DialogReporter (CharacterTemplate current, String m){
		withCharacter = true;
		host = current;
		cHost = current;

		cHost.dialogReporter = this;

		x = host.x;
		y = host.y-2;

		questAdvancer = formatMessage(m); // saves the message, and checks for quest indicators
		setDefaultHitBox();
	}

	public DialogReporter (SpecialObject current, String m){
		host = current;

		x = host.x;
		y = host.y-12;

		questAdvancer = formatMessage(m); // saves the message, and checks for quest indicators
		setDefaultHitBox();
	}

	public int formatMessage(String m){
		int quest = -1;
		for (int i = 0; i<m.length(); i++){
			if (m.charAt(i) == '@'){
				if (m.charAt(i+1) == 'q'){ // if it's a quest declaration
					quest = 0;
					int total = 0;
					for (int j = i+2; m.charAt(j)!='@'; j++){ // searching for the end of the quest declaration
						total++; // to see if it's a one or two digit number (etc).
					}
					int count = 0;
					for (int j = i+2; j < i+2+total; j++){ // a manual way of parsing a number from a string.
						quest += Integer.parseInt(String.valueOf(m.charAt(j)))*(Math.pow(10, total-count-1)); //I'm not sure how else to do it.
						count++;  // And I'm too lazy to check.
					}

					i+=total+2; // when copying the message, skip over all the quest declarations
				} else {  // if it's not a quest declaration, it doesn't matter in a dialog reporter
					i+=1; // so just skip over it
				}
			} else {
				message += m.charAt(i); // otherwise, keep copying the message
			}
		}

		w = (int)(message.length()*12);
		h = 18;

		return quest;
	}

	public void always(Map map, boolean[] KeysDown, int[]KeysDownTimer){
		time++;
		x = host.x;
		y = host.y-30;

		if (time >= 60){
			 alpha = (float)(1f*(80-time)/20);
		}
		if (time >= 80){
			delete = true;
			if (withCharacter){
				cHost.dialogReporter = null;
			}
		}
	}

	public void draw(Board _board, Map theMap, Graphics2D g2d) {
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		
		if (time >= 10){
			if (host != null){
				g2d.setColor(Color.black);
				int xpoints[] = {(int)(x-theMap.xCamera+w/3),(int)(x-theMap.xCamera+2*w/3),(int)(host.x-theMap.xCamera+host.w/2)};
				int ypoints[] = {(int)(y-theMap.yCamera)+h,(int)(y-theMap.yCamera)+h,(int)(host.y-theMap.yCamera-3)};
				g2d.fillPolygon(xpoints, ypoints, 3);
				g2d.setColor(Color.white);
				g2d.drawPolygon(xpoints, ypoints, 3);
			}

			g2d.setColor(new Color(0,0,0,(float)0.9));
			g2d.fillRect((int)x - (int) theMap.xCamera, (int)y - (int) theMap.yCamera, w, h);
			g2d.setColor(Color.white);
			g2d.drawRect((int)x - (int) theMap.xCamera, (int)y - (int) theMap.yCamera, w, h);

			g2d.setColor(Color.white);
			g2d.setFont(new Font("Courier", Font.PLAIN, 18));
			g2d.drawString(message, (int) x - (int) theMap.xCamera+3, (int) y - (int) theMap.yCamera + h-3);
		}
	}
}
