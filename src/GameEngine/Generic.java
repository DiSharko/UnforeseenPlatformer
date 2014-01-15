package GameEngine;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;

import javax.swing.ImageIcon;

public class Generic extends SpecialObject {

	Image sprite2;

	Boolean boolean1 = false;
	int int1 = 0;
	String string1 = "";
	String string2 = "";

	public Generic(Map _map, String _name, double _x, double _y, int _w, int _h, String _settings){
		map = _map;
		name = _name;
		x = _x;
		y = _y;
		w = _w;
		h = _h;
		String[] _settingsArray = _settings.split(";");
		setupSpecialObject(_settingsArray);
		setDefaultHitBox();
	}


	public void setupSpecialObject(String[] _settings){
		if (name.equals("streetlight2")){
			map.graphicData3.add(new Graphic("streetlight2",x,y, false));
			try {
				double _x = x+4;
				double _y = y+16;
				map.lightSources.add(new Polygon(new int[] {(int) (_x-26),(int) _x,(int) (_x+26)},new int[] {(int) (_y+48),(int) (_y),(int) (_y+48)},3));
			} catch (Exception e){
				e.printStackTrace();
			}
			//sprite2 = new ImageIcon(Board.class.getResource("/media/images/graphics/lightsource2.png")).getImage();

		} else if (name.equals("water")){
			
		} else if (name.equals("scrollBound")){
			string1 = _settings[5];
			
		} else if (name.equals("ladder")){
			for (int i = 0; i<_settings.length;i++){
				if (_settings[i].equals("autoLock")){
					boolean1 = true;
				}
			}

		} else if (name.equals("collectible")){
			string1 = _settings[0];
			try {
				sprite = new ImageIcon(Board.class.getResource("/media/images/graphics/collectible.png")).getImage();
			} catch(NullPointerException e){Console.out("SOMETHING HAPPENED TO OUR "+name+"!!");}

		} else if (name.equals("inventoryItem")){
			try {
				sprite = new ImageIcon(Board.class.getResource("/media/images/graphics/"+_settings[0]+".png")).getImage();
			} catch(NullPointerException e){Console.out("SOMETHING HAPPENED TO OUR "+name+"!!");}

		} else if (name.equals("ghost")){
			string1 = _settings[0];
			facing = Byte.parseByte(_settings[1]);
			if (facing == -1){
				x+=w;
			}
			alpha = (float) 0.5;
			//int1 = Integer.parseInt(_settings.get(1));
			try {
				sprite = new ImageIcon(Board.class.getResource("/media/images/characters/"+_settings[0]+"/still_1.png")).getImage();
			} catch(NullPointerException e){Console.out("SOMETHING HAPPENED TO OUR "+string1+" CHARACTER!!");}

		} else if (name.equals("talkTrigger")){
			string1 = _settings[0];
			string2 = _settings[1];
			
		} else if (name.equals("trackerTrigger")){
			string1 = _settings[0];
			string2 = _settings[1];
			
		} else {
			try {
				sprite = new ImageIcon(Board.class.getResource("/media/images/graphics/"+name+".png")).getImage();
			} catch(NullPointerException e){Console.out("SOMETHING HAPPENED TO OUR "+name+"!!");}	
			w = sprite.getWidth(null);
			h = sprite.getHeight(null);
		}

	}

	public void onHit(Board board, Map map,CharacterTemplate current){
		if (name.equals("streetlight2")){

		} else if (name.equals("water")){
			if (!current.physicsMode.equals("ladder")){
				current.physicsMode = "water";
			}
			if (current.dx != 0 || current.dy < -5){
				//current.swimming = true;
			} else {
				//current.swimming = false;
			}
		} else if (name.equals("ladder")){
			if ((board.KeysDown[0] && board.KeysDownTimer[0] < 3) || (board.KeysDown[1] && board.KeysDownTimer[1] < 3) || boolean1){
				current.physicsMode = "ladder";
				current.canJump = true;
				current.jumping = false;
				current.jumps = 0;
				current.jumpTime = 0;
				current.dx = 0;
				current.ladderLock = true;
				current.ladder = this;
			}
			if (current.ladderLock){
				current.physicsMode = "ladder";
			}
		} else if (name.equals("collectible")){
			if (current.equals(board.game.player)){
				if (!boolean1){
					boolean1 = true;
					int1 = 100;
					board.game.player.inventory.add(new Generic(map,"inventoryItem",0,0,0,0,string1));
				}
			}
		} else if (name.equals("talkTrigger")){
			if (current.equals(board.game.player)){
				CharacterTemplate _target = board.game.quest.getCharacter(string1);
				_target.facing = (byte) Math.signum(current.x-_target.x);
				_target.addPriorityTask("type:findConversation;conversation:"+string2);
				delete = true;
			}
			
		} else if (name.equals("trackerTrigger")){
			if (current.equals(board.game.player)){
				board.game.addTracker(string1, string2);
				delete = true;
			}
		} else {}
	}

	public void always(Map map, boolean[] KeysDown, int[]KeysDownTimer){
		if (name.equals("collectible")){
			if (boolean1){
				if (int1>0){
					int1--;
				} else {
					delete = true;
				}
			}
		} else if (name.equals("ghost")){
			if (alpha > 0){
				alpha -= 0.02;
			} else {
				delete = true;
			}
		}

	}

	public void draw(Board _board, Map theMap, Graphics2D g2d) {
		if (name.equals("streetlight2")){
			g2d.drawImage(sprite, (int)(x - theMap.xCamera), (int)(y - theMap.yCamera), null);
			/*if (board.myGame.quest.timeOfDay.equals("night")){
				g2d.drawImage(sprite2, (int)(x-12 - theMap.xCamera), (int)(y+16 - theMap.yCamera), null);
			}*/

		} else if (name.equals("water")){
			g2d.setColor(new Color(0,0,1,(float)0.5));
			g2d.fillRect((int) (x - theMap.xCamera), (int) (y - theMap.yCamera), w, h);

		} else {
			if (boolean1 && name.equals("collectible")){
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, int1));
			}
			if (sprite != null){
				g2d.drawImage(sprite, (int) x - (int) theMap.xCamera, (int) y - (int) theMap.yCamera, w*facing, h, null);
			}
		}

	}
}