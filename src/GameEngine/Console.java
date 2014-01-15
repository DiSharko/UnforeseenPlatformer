package GameEngine;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

public abstract class Console {
	
	static ArrayList<Object> messages = new ArrayList<Object>();
	static ArrayList<String> fps = new ArrayList<String>();
	static int height = 330;
	
	public static void draw(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(new Color((float)0.2,(float)0.2,(float)0.2,(float)0.7));
		g2d.fillRect(0,0,640,height);
		g2d.setStroke(new BasicStroke());
		g2d.setColor(Color.cyan);
		g2d.drawLine(60, 5, 60, height-5);
		
		g2d.setFont(new Font("Courier", Font.BOLD, 10));
		
		int j = 0;
		for (int i = fps.size()-1; i>=0; i--){
			g2d.drawString(fps.get(i), 5, height-j*20-5);
			if (height-j*20 < 0){
				break;
			}
			j++;
		}
		j = 0;
		for (int i = messages.size()-1; i>=0; i--){
			g2d.drawString(""+messages.get(i)+"", 70, height-j*20-5);
			if (height-j*20 < 0){
				break;
			}
			j++;
		}
	}
	public static void out(Object m){
		System.out.println(m);
		messages.add(m);
	}
	public static void outFPS(String f){
		System.out.println(f);
		fps.add(f);
	}
}
