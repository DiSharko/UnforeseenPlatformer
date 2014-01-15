package GameEngine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class SplashScreen extends JPanel {
	int time = 0;
	public SplashScreen(){repaint();};

	public void paint(Graphics g){
		super.paint(g);

		Graphics2D g2d = (Graphics2D) g;
		if (time == 0){
			drawBackground(g2d, Color.black);
			drawSmiley(g2d, Color.white);
			//drawSquares(g2d, Color.white);
		} else if (time >= 1){
			drawBackground(g2d, Color.black);
			drawLoading(g2d, Color.white);
			drawSquares(g2d, Color.white);
		}
		time++;
		repaint();
	}

	public void drawLoading(Graphics2D g2d, Color color){
		g2d.setColor(color);
		g2d.setFont(new Font("Synchro LET", Font.PLAIN, 24));
		g2d.drawString("Loading...", 50, 40);
	}

	public void drawSquares(Graphics2D g2d, Color color){
		for (int i = 10; i< 180; i+=20){
			g2d.setColor(color);
			g2d.drawRect(i, 55, 10, 10);
			if (time/6 >= i){
				g2d.fillRect(i, 55, 10, 10);
			}
		}
	}

	public void drawSmallSmiley(Graphics2D g2d, Color color){
		g2d.setColor(color);
		g2d.drawOval(75, 5, 40, 40);
		g2d.fillOval(85, 15, 5, 5);
		g2d.fillOval(100, 15, 5, 5);
		g2d.drawArc(80, 15, 30, 25, 210, 120);
	}

	public void drawSmiley(Graphics2D g2d, Color color){
		g2d.setColor(color);
		g2d.drawOval(70, 10, 60, 60);
		g2d.fillOval(80, 20, 10, 10);
		g2d.fillOval(110, 20, 10, 10);
		g2d.drawArc(80, 40, 40, 20, 210, 120);
	}

	public void drawBackground(Graphics2D g2d, Color color){
		g2d.setColor(color);
		g2d.fillRect(0,0,200,80);
	}
}
