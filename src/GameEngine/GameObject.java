package GameEngine;

import java.awt.Image;

public class GameObject {
	
	int w;
	int h;
	double x;
	double y;
	
	double dx = 0;
	double dy = 0;
	

	boolean delete = false;
	boolean managed = false;
	
	String name = "";
	String description = name;
	String id = "";
	
	
	Image sprite;
	float alpha = 1f;
	int visible = 1;
	boolean everywhere = false;
		
	
	boolean floor;
	boolean ceiling;
	boolean left;
	boolean right;
	
	
	int[] hitBox = {0,0,0,0};
	
	
	public void setDefaultHitBox(){
		hitBox[0] = 0;
		hitBox[1] = 0;
		hitBox[2] = w;
		hitBox[3] = h;
	}
}
