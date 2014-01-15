package GameEngine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Inventory {

	Board board;

	int x;
	int y;
	int wGiven = 600;
	int hGiven = 390;
	int w = wGiven;
	int h = hGiven;

	int arc = 15;
	//at least 32 for the images
	int originalSpaceW = 200;
	int originalSpaceH = 200;
	int buffer = 8;

	int spaceW = originalSpaceW;
	int spaceH = originalSpaceH;

	int X_LIMIT = w/(spaceW+buffer);
	int Y_LIMIT = h/(spaceH+buffer);


	int selectorX = 0;
	int selectorY = 0;

	ArrayList<GameObject> objects = new ArrayList<GameObject>();
	CharacterTemplate host;
	boolean visible = false;

	public Inventory(Board b, CharacterTemplate tHost){
		host = tHost;
		board = b;
	}

	public void setupInventory(){
		spaceW = originalSpaceW;
		spaceH = originalSpaceH;
		X_LIMIT = wGiven/(spaceW+buffer);
		Y_LIMIT = hGiven/(spaceH+buffer);


		while (objects.size() > X_LIMIT*Y_LIMIT){
			spaceW--;
			spaceH--;
			X_LIMIT = wGiven/(spaceW+buffer);
			Y_LIMIT = hGiven/(spaceH+buffer);
		}

		int yNum = (int)Math.ceil(objects.size()/(double)X_LIMIT);
		if (yNum > Y_LIMIT){
			yNum = Y_LIMIT;
		}

		w = (spaceW+buffer)*(X_LIMIT)+buffer;
		h = (spaceH+buffer)*(yNum)+buffer;
		x = (640-w)/2;
		y = (480-h)/2;
		if (objects.size() == 0){
			h = 30;
		}
		visible = true;
	}
	public void hideInventory(){
		visible = false;
	}

	public void drawInventory(Graphics2D g2d){
		if (visible){
			controlSelector();
		}

		g2d.setColor(Color.gray);
		g2d.fillRoundRect(x+10, y-37, w-20, 45, arc, arc);
		g2d.setColor(Color.black);
		g2d.drawRoundRect(x+10, y-37, w-20, 45, arc, arc);
		g2d.setFont(new Font("Synchro LET", Font.PLAIN, 30));
		g2d.drawString("Inventory", x+w/2-80, y-5);
		
		if (objects.size() != 0){
			g2d.setColor(Color.gray);
			g2d.fillRoundRect(x+10, y+h-8, w-20, 45, arc, arc);
			g2d.setColor(Color.black);
			g2d.drawRoundRect(x+10, y+h-8, w-20, 45, arc, arc);
		}


		g2d.setColor(Color.darkGray);
		g2d.fillRoundRect(x, y, w, h, arc, arc);
		g2d.setColor(Color.black);
		g2d.drawRoundRect(x, y, w, h, arc, arc);
		int row = 0;
		int column = 0;
		if (objects.size() == 0){
			g2d.setFont(new Font("Synchro LET", Font.PLAIN, 20));
			g2d.drawString("No items...", x+5, y+20);
		} else{
			for (int i = 0; i<objects.size();i++){
				boolean selected = false;
				if (column > X_LIMIT-1){
					column = 0;
					row++;
					if (row > Y_LIMIT-1){
						break;
					}
				}
				if (column == selectorX && row == selectorY){
					selected = true;
					g2d.setColor(Color.black);
					g2d.setFont(new Font("Synchro LET", Font.PLAIN, 18));
					g2d.drawString(objects.get(i).description, x+15, y+h+24);
				}
				if (selected){
					g2d.setColor(Color.cyan);
				} else {
					g2d.setColor(Color.gray);
				}
				g2d.fillRoundRect(column*(spaceW+buffer)+buffer+x, row*(spaceH+buffer)+buffer+y, spaceW, spaceH, arc, arc);
				g2d.drawImage(objects.get(i).sprite, column*(spaceW+buffer)+(spaceW-32)/2+buffer+x, row*(spaceH+buffer)+(spaceH-32)/2+buffer+y, null);
				if (selected){
					g2d.setColor(Color.blue);
				} else {
					g2d.setColor(Color.black);
				}
				g2d.drawRoundRect(column*(spaceW+buffer)+buffer+x, row*(spaceH+buffer)+buffer+y, spaceW, spaceH, arc, arc);
				column++;
			}
		}

	}

	public void add(GameObject object){
		objects.add(object);
	}


	public void controlSelector(){
		if (board.KeysDown[0]){
			if (keyRepeatDelay(board.KeysDownTimer[0])){
				selectorY--;
			}
			board.KeysDownTimer[0]++;
		}
		if (board.KeysDown[1]){
			if (keyRepeatDelay(board.KeysDownTimer[1])){
				selectorY++;
			}
			board.KeysDownTimer[1]++;
		}
		if (board.KeysDown[2]){
			if (keyRepeatDelay(board.KeysDownTimer[2])){
				selectorX--;
			}
			board.KeysDownTimer[2]++;
		}
		if (board.KeysDown[3]){
			if (keyRepeatDelay(board.KeysDownTimer[3])){
				selectorX++;
			}
			board.KeysDownTimer[3]++;
		}


		if (selectorX < 0){
			if (selectorY>0){
				selectorX = X_LIMIT-1;
				selectorY--;
			} else {
				selectorX = 0;
			}
		} else if (selectorX >= X_LIMIT){
			selectorX = 0;
			selectorY++;
		}
		if (selectorY < 0){
			selectorY = 0;
		} else if (selectorY >= Y_LIMIT){
			selectorY = Y_LIMIT-1;
		}
		if (selectorY*X_LIMIT+selectorX >= objects.size()){
			selectorY = (int)(objects.size()/X_LIMIT);
			selectorX = objects.size()%X_LIMIT-1;
		}
	}

	public boolean keyRepeatDelay(int keyTimer){
		int initialDelay = 6;
		int delayBetween = 3;

		if (keyTimer == 1){
			return true;
		}
		if (keyTimer >= initialDelay && (keyTimer%delayBetween == 0)){
			return true;
		}
		return false;
	}
}
