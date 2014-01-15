package GameEngine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class DialogBox extends SpecialObject {
	boolean visible = false;
	boolean finished = true;
	boolean paused = false;

	int animationTime = 0;

	Board board;
	CharacterTemplate originalFocus;
	CharacterTemplate cHost;

	int currentCharacter = 0;
	int STANDARD_LINE = 30;
	int STANDARD_INTERVAL = 2;
	int lineStart = 0;

	int charInterval = STANDARD_INTERVAL;
	int charTimer = charInterval;

	String rawFullMessage;
	String finalFullMessage = "";
	StringBuffer messageBuffer;
	ArrayList<String> formattedMessage;
	int showLine;
	
	int pauseAtEnd = 0;
	int waitAtEnd = 0;
	//Generic dialogNotification;

	public DialogBox(Board _board){
		board = _board;
		rawFullMessage = "";
		formattedMessage = new ArrayList<String>();
		x = 0;
		y = 380;
		w = 256;
		h = 95;
		setDefaultHitBox();
	}



	public int newMessage(CharacterTemplate current, String message){

		if (finished &&!visible){


			charInterval = STANDARD_INTERVAL;
			rawFullMessage = message;
			finalFullMessage = "";
			formattedMessage = new ArrayList<String>();
			lineStart = 0;
			currentCharacter = 0;
			finished = false;
			visible = true;
			
			board.game.PAUSED_dialog = -1;
			board.game.WAITING_dialog = 0;
			
			waitAtEnd = 0;
			pauseAtEnd = 0;
			
			originalFocus = board.game.focus;
			if (current != null){
				cHost = current;
			} else {
				cHost = board.game.player;
			}
		}
		int quest = -1;
		quest = parseMessage();
		setPosition();
		return quest;
	}


	public void setPosition(){
		if (cHost != null){
			if (finalFullMessage.length()<STANDARD_LINE){
				w = (int)(finalFullMessage.length()*12);
				h = 48;
			} else {
				w = 320;
				h = 72;
			}
			if (w < cHost.nickname.length()*12){	w = cHost.nickname.length()*12;	}
			
			if (cHost.facing == -1){
				x = cHost.x+64-w-board.game.theMap.xCamera;
			} else { x = cHost.x - 64-board.game.theMap.xCamera; }
			
			
			if (x < 32){x = 32;} else if (x + w > 608){x = 608 - w;}

			y = cHost.y - h-20-board.game.theMap.yCamera;
			if (y + h < 32) { y = 32 - h; } else if (y > 448){ y = 448;	}

		} else {
			x = 0;
			y = 380;
			w = 640;
			h = 105;
		}
	}


	public void drawDialog(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		
		if (visible || board.game.WAITING_dialog != 0 || board.game.PAUSED_dialog != 0){if (animationTime < 8){animationTime++;	}}	else 	{	if (animationTime>0){animationTime--;}	}
		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, 640, animationTime*4);
		g2d.fillRect(0, 480-animationTime*4, 640, animationTime*4);
		
		if (visible){
			setPosition();
			if (cHost != null){
				g2d.setColor(new Color((float)0.5,(float)0.5,(float)0.5,(float)0.8));
				g2d.fillRect((int)x,(int)y,w,20);
				g2d.setColor(Color.black);
				g2d.setFont(new Font("Courier", Font.BOLD, 16));
				g2d.drawString(cHost.nickname,(int)x+5,(int)y+15);
				g2d.setColor(Color.white);
				g2d.drawRect((int)x, (int)y, w, 20);

				g2d.setColor(Color.black);
				//int xpoints[] = {(int)(cHost.x-board.game.theMap.xCamera+cHost.facing*4), (int)(cHost.x-board.game.theMap.xCamera+cHost.w+cHost.facing*4), (int)(cHost.x-board.game.theMap.xCamera+cHost.w/2+cHost.facing*4)};
				int xpoints[] = {(int)(x+w/3),(int)(x+2*w/3),(int)(cHost.x-board.game.theMap.xCamera+cHost.w/2+cHost.facing*6)};
				int ypoints[] = {(int)y+h,(int)y+h,(int)(cHost.y-board.game.theMap.yCamera-3)};
				g2d.fillPolygon(xpoints, ypoints, 3);
				g2d.setColor(Color.white);
				g2d.drawPolygon(xpoints, ypoints, 3);

			}

			g2d.setColor(new Color(0,0,0,(float)0.9));
			g2d.fillRect((int)x, (int)y+20, w, h-20);
			g2d.setColor(Color.white);
			g2d.drawRect((int)x, (int)y+20, w, h-20);


			g2d.setColor(Color.white);
			g2d.setFont(new Font("Courier", Font.PLAIN, 18));
			int charsDrawn = 0;
			int tempLine = lineStart;
			finished = true;
			while (tempLine>0){
				charsDrawn+=formattedMessage.get(tempLine-1).length()-1;
				tempLine--;
			}
			for (int i = lineStart; i < formattedMessage.size(); i++){	
				if (i-lineStart>1){
					lineStart++;
				}
				try{
					if (formattedMessage.get(i).charAt(currentCharacter-charsDrawn) == ('@')){		
						if (formattedMessage.get(i).charAt(currentCharacter-charsDrawn+1) == ('p')){
							paused = true;
							messageBuffer = new StringBuffer(formattedMessage.get(i));
							messageBuffer.delete(currentCharacter-charsDrawn, currentCharacter-charsDrawn + 2);
							formattedMessage.set(i,messageBuffer.toString());
						}
					}
				} catch (IndexOutOfBoundsException e){finished = true;}
				if (formattedMessage.get(i).length()+charsDrawn<currentCharacter){
					charsDrawn += formattedMessage.get(i).length();
					g2d.drawString(formattedMessage.get(i),(int)x+5, (int)y+40+24*(i-lineStart));
				} else {
					finished = false;
					g2d.drawString(formattedMessage.get(i).substring(0, currentCharacter-charsDrawn),(int)x+5, (int)y+40+24*(i-lineStart));
					break;
				}
			}
		}
	}

	public void always(Map map, boolean[] KeysDown, int[]KeysDownTimer){
		if (KeysDownTimer[1] == 1){
			if (!finished){
				if (paused){
					paused = false;
					charInterval = STANDARD_INTERVAL;
					KeysDownTimer[1]++;
				} else {
					charInterval = 0;
					KeysDownTimer[1]++;
				}
			} else {
				close();
				KeysDownTimer[1]++;
			}
		}
		if (!finished&&!paused){
			if (charTimer++ >= charInterval){
				charTimer = 0;
			}
			if (charTimer == 0){
				currentCharacter++;
			}
		}
	}




	public int parseMessage(){
		int quest = -1;

		ArrayList<String> semiFormattedMessage = new ArrayList<String>();
		String tempLine = "";
		for (int i = 0; i<rawFullMessage.length();i++){
			if (rawFullMessage.charAt(i)!='@'){
				tempLine += rawFullMessage.charAt(i);
			} else {
				if (rawFullMessage.charAt(i+1) == 'n'){
					semiFormattedMessage.add(tempLine);
					tempLine = "";
					i++;
				} else if (rawFullMessage.charAt(i+1) == 'f') {
					board.game.focus = cHost;
					i++;
				} else if (rawFullMessage.charAt(i+1) == 'w') {
					waitAtEnd = 0;
					int total = 0;
					for (int j = i+2; rawFullMessage.charAt(j)!='@'; j++){
						total++;
					}
					int count = 0;
					for (int j = i+2; j < i+2+total; j++){
						waitAtEnd += Integer.parseInt(String.valueOf(rawFullMessage.charAt(j)))*(Math.pow(10, total-count-1));
						count++;
					}
					i+=total+2;
				} else if (rawFullMessage.charAt(i+1) == 'd') {
					pauseAtEnd = 0;
					int total = 0;
					for (int j = i+2; rawFullMessage.charAt(j)!='@'; j++){
						total++;
					}
					int count = 0;
					for (int j = i+2; j < i+2+total; j++){
						pauseAtEnd += Integer.parseInt(String.valueOf(rawFullMessage.charAt(j)))*(Math.pow(10, total-count-1));
						count++;
					}
					i+=total+2;
				} else if (rawFullMessage.charAt(i+1) == 'q'){
					quest = 0;
					int total = 0;
					for (int j = i+2; rawFullMessage.charAt(j)!='@'; j++){
						total++;
					}
					int count = 0;
					for (int j = i+2; j < i+2+total; j++){
						quest += Integer.parseInt(String.valueOf(rawFullMessage.charAt(j)))*(Math.pow(10, total-count-1));
						count++;
					}
					i+=total+2;
				} else {
					tempLine += rawFullMessage.charAt(i);
				}
			}
		}
		semiFormattedMessage.add(tempLine);

		tempLine = "";
		int prevEnd;
		for (int i = 0; i<semiFormattedMessage.size(); i++){
			prevEnd = 0;
			while (semiFormattedMessage.get(i).length()-prevEnd>STANDARD_LINE){
				int backChar = prevEnd+STANDARD_LINE;

				while (semiFormattedMessage.get(i).charAt(backChar) != ' ') {
					backChar--;
					if (backChar < prevEnd){
						Console.out("You son of a bitch, your words are too big to fit!!");
						backChar = prevEnd+STANDARD_LINE;
						break;
					}
				}

				formattedMessage.add(semiFormattedMessage.get(i).substring(prevEnd,backChar));
				prevEnd = backChar;

			}
			formattedMessage.add(semiFormattedMessage.get(i).substring(prevEnd));
		}

		for (int i = 0; i<formattedMessage.size();i++){
			for (int j = 0; j < formattedMessage.get(i).length(); j++){
				if (formattedMessage.get(i).charAt(j) != '@'){
					finalFullMessage += formattedMessage.get(i).charAt(j);
				} else {
					j++;
				}
			}
		}

		return quest;
	}




	public void close() {
		board.game.PAUSED_dialog = pauseAtEnd;
		board.game.WAITING_dialog = waitAtEnd;
		rawFullMessage = "";
		board.game.focus = originalFocus;
		formattedMessage = new ArrayList<String>();
		currentCharacter = 0;
		lineStart = 0;
		finished = true;
		visible = false;
	}
}

