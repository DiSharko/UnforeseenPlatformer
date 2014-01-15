package GameEngine;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Bar extends GameObject {
	double totalValue = 100;
	double trueValue = 100;
	double drawValue = 100;
	Color backgroundBarColor = Color.GRAY;
	boolean animate;
	Game game;

	public Bar(Game _game, double _x, double _y, int _w, int _h, double _totalValue, boolean _animate){
		game = _game;
		x = _x;
		y = _y;
		w = _w;
		h = _h;
		totalValue = _totalValue;
		trueValue = totalValue;
		drawValue = trueValue;
		animate = _animate;
	}
	public Color setBarColor(){
		Color color;
		if (drawValue < totalValue/4){
			color = Color.red;
		} else if (drawValue < totalValue/2){
			color = Color.yellow;
		} else {
			color = Color.green;
		}
		
		return color;
	}

	public void draw(Graphics g){
		Graphics2D g2d = (Graphics2D) g;

		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
	
		g2d.setStroke(new BasicStroke());
		g2d.setColor(backgroundBarColor);
		g2d.fillRoundRect((int)x, (int)y, w, h, 4, 4);
		g2d.setColor(setBarColor());
		g2d.fillRoundRect((int)x, (int)y, (int)((drawValue/totalValue)*w), h, 4, 4);
		g2d.setColor(Color.BLACK);
		g2d.drawRoundRect((int)x, (int)y, w, h, 4, 4);
		if (animate){
			g2d.setFont(new Font("Synchro LET", Font.BOLD, h-4));
			g2d.drawString((int)drawValue+"/"+(int)totalValue, (int) x + (int) w/2 - 14 - (int)(2*(int)Math.log(drawValue)+2*(int)Math.log(totalValue)), (int) y + (int) h - 2);
		}
		
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

	}
	public void animateBar(){
		if (animate){
			if (drawValue<trueValue){
				drawValue++;
				//drawValue+=(drawValue-trueValue)/5;
				if (trueValue-drawValue<1){
					drawValue = trueValue;
				}
			}
			if (drawValue>trueValue){
				//drawValue-=(drawValue-trueValue)/5;
				drawValue--;
				if (trueValue-drawValue>-1){
					drawValue = trueValue;
				}
			}
		} else{
			drawValue = trueValue;
		}
		if (drawValue < 0){
			drawValue = 0;
		}
		if (Function.fullHitTest(x-5+game.theMap.xCamera,y-5+game.theMap.yCamera,w+32,h+64, game.player)){
			alpha = (float)0.3;
		} else {
			alpha = 1f;
		}
	}
	public void setValue(double value){
		trueValue = value;
	}
}
