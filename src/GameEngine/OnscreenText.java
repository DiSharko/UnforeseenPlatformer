package GameEngine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class OnscreenText extends SpecialObject {
	Color textColor = Color.black;
	String font = "Synchro LET";
	int style = Font.PLAIN;
	int fontSize = 12;
	String text = "";

	Font textFont =	new Font(font, style, fontSize);
	

	public OnscreenText(double tX, double tY, String info){
		x=tX;y=tY;//w=tW;h=tH;

		String[] props = info.split(";");
		for (int i = 0; i< props.length; i++){
			try{
				String[] prop = props[i].split(":");

				if (prop[0].equals("text")){
					text = prop[1];
				} else if (prop[0].equals("color")){
					textColor = Color.getColor(prop[1]);
				} else if (prop[0].equals("font")){
					font = prop[1];
				} else if (prop[0].equals("size")){
					fontSize = Integer.parseInt(prop[1]);
				} else if (prop[0].equals("")){
					
				} else if (prop[0].equals("")){
				} else if (prop[0].equals("")){
				} else if (prop[0].equals("")){
				} else if (prop[0].equals("")){

				} else {
					Console.out("Unknown OnscreenText property: "+props[i]);
				}
			} catch (Exception e){
				Console.out("Error occurred in OnscreenText property: "+props[i]);
			}
		}
		textFont = new Font(font, style, fontSize);
		setDefaultHitBox();
	}


	public void draw(Board _board, Map theMap, Graphics2D g2d) {
		int fComp = 0;
		if (facing == -1)
			fComp = w;
		g2d.rotate(rotation);
		g2d.setColor(textColor);
		g2d.setFont(textFont);
		g2d.drawString(text, (int) x - (int) theMap.xCamera + fComp, (int) y - (int) theMap.yCamera);
	}

}

