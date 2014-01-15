package GameEngine;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.RenderingHints;


import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

//import java.util.ArrayList;
import java.util.Random;

@SuppressWarnings("serial")

public class Board extends JPanel
{

	Timer hitTimer = new Timer(12, new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			repaint();
			hitTimer.restart();
		}
	});
	Timer drawTimer = new Timer(24, new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			performDrawing = true;
			drawTimer.restart();
		}
	});

	Timer time = new Timer(1000, new ActionListener(){
		public void actionPerformed(ActionEvent e){
			if (gameState == 1){
				Console.outFPS("FPS = " + count);
			}
			count = 0;
			clock++;
			time.restart();
		}
	});

	boolean performDrawing = false;

	int count = 0;
	int clock = 0;

	//int cut = 0;

	Random generator = new Random();
	Dimension gameDimensions;
	int gameState = 1;
	double changingLevelTime = 0;
	Image pauseScreen;// = new ImageIcon(Board.class.getResource("/media/images/graphics/pauseScreen.png")).getImage();
	boolean drawConsole = false;


	Animation characterArrow;// = new Animation("arrow",0,0,16,16, "changeDelay:2;repeatDelay:35");

	//						0		1	  2		3		4	   5	  6		 7		8	   9	 10		11
	//					    UP	  DOWN	 LEFT  RIGHT	Z	   X	  W		 A		S	   D	  Q		 E
	boolean[] KeysDown = {false, false, false, false, false, false, false, false, false, false, false, false};
	int[] KeysDownTimer = { 0,	   0,	  0,	 0,		0,		0,	  0,	 0,		0,	   0,	  0,	0};

	Game game;
	DialogBox dialog = new DialogBox(this);

	String currentLevel;

	public Board(){
		//characterArrow.initialDelay = 35;

		Texture.loadTextures();

		hitTimer.start();
		drawTimer.start();
		time.start();
		generator.setSeed(12346000);
		//gameState = 1;
		gameDimensions = new Dimension(640,480);
		

		if (gameState == 1){
			startGame();
		}


		addKeyListener(new TAdapter());
		setFocusable(true);

	}

	public void startGame(){
		game = new Game(this);
		game.reloadMap();
	}


	public void paint(Graphics g)
	{

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
		g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);

		if (gameState == 0){
			g2d.setColor(COLOR.dawn);
			g2d.fillRect(0, 0, gameDimensions.width, gameDimensions.height);
			g2d.setColor(COLOR.black);
			g2d.setFont(new Font("Courier",Font.BOLD, 30));
			g2d.drawString("Game.", 250, 200);


		} else if (gameState == 1){
			if (game.theMap.reload > 0){
				int changeLevelType = 2;
				if (changeLevelType == 1){
					changingLevelTime+=0.05;
					g.setColor(Color.black);
					for (int i = 0; i< 640; i+=32){
						for (int j = 0; j<480; j+=32){
							if (Math.random()<changingLevelTime){
								g.fillRect(i, j, 32, 32);
							}
						}
					}
				} else if (changeLevelType == 2){
					changingLevelTime+=0.0335;
					for (int i = 0; i<15; i++){
						int alpha = 0;
						if (i <= (int) (changingLevelTime/0.067)){
							alpha = 255;
						} else {
							alpha = 100/(i - (int) (changingLevelTime/0.067));
						}
						Color grayScale = new Color(0, 0, 0, alpha);
						g.setColor(grayScale);
						g.fillRect(0, i*32, 640, 32);
					}
				} else if (changeLevelType == 3){
					changingLevelTime+=0.03;
					int alpha = (int) (150*changingLevelTime);
					Color grayScale = new Color(0, 0, 0, alpha);
					g.setColor(grayScale);
					g.fillRect(0, 0, 640, 480);
				}
				if (changingLevelTime >= 1){
					game.theMap.reload = 0;
				}
			} else if (game.theMap.reload == 0){
				changingLevelTime = 0;
				game.reloadMap();

			} else if (game.player.health <=0) {
				game.player.visible=1;
				if (performDrawing){
					game.theMap.scrollControl(game);
					drawInGame(g2d);
					drawOverlays(g2d);
					int alpha = (int)(game.deathResetTimer/5*4);
					Color redTint = new Color(143,0,0,alpha);
					g.setColor(redTint);
					g.fillRect(0,0,640,480);
				}
				if (game.deathResetTimer == 100){
					game.reloadFromSave();
				} else {
					game.deathResetTimer++;
				}



			} else { // Do this normally:
				if (!game.PAUSED_inventory){
					if (game.WAITING_dialog > 0){	game.WAITING_dialog--;	}
					if (game.PAUSED_dialog > 0){	game.PAUSED_dialog--;	}
					
					if (game.PAUSED_dialog == 0 && game.WAITING_dialog == 0){
						game.player.control(KeysDown, KeysDownTimer, game.theMap);
					}

					if (game.PAUSED_dialog == 0){	update();	 }

					game.theMap.scrollControl(game);
				}
				game.audio.manageSounds();
				game.audio.manageMusic();


				if (performDrawing){
					count++;
					drawInGame(g2d);
					drawOverlays(g2d);
				}
			}

		} else if (gameState == 2) {
			g.setColor(Color.black);
			g.fillRect(0, 0, 640, 480);
			g.setColor(Color.white);
			g.setFont(new Font("Synchro LET", Font.PLAIN, 128));
			g.drawImage(pauseScreen,0,0,null);
			//g.drawString("PAUSED", 100, 260);
		}


		if (drawConsole && performDrawing){
			Console.draw(g2d);
		}
		if (performDrawing){
			performDrawing = false;
		}
	}


	public void setAlpha(float a, Graphics2D g2d){
		if (a != 1f){
			a = Math.min(Math.max(a, 0), 1f);
		}
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, a));
	}
	public void drawInGame(Graphics2D g2d){		
		super.paint(g2d);

		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, gameDimensions.width, gameDimensions.height);
		g2d.setColor(game.theMap.bgColor);
		if (!game.theMap.reducedVision){
			g2d.fillRect(game.theMap.xLeft, game.theMap.yTop, game.theMap.xRight-game.theMap.xLeft, game.theMap.yBottom-game.theMap.yTop);
		} else {
			g2d.fillRect(game.theMap.xLeft-64-(int)(game.theMap.xCamera-game.player.x+gameDimensions.width/2), game.theMap.yTop-64-(int)(game.theMap.yCamera-game.player.y+gameDimensions.height/2), game.theMap.xRight-game.theMap.xLeft+128, game.theMap.yBottom-game.theMap.yTop+128);
		}

		// Method 1
		if (Global.DEBUG_MODE < 2){
			for (int i = 0; i < game.theMap.graphicData4.size(); i++){
				Graphic current = game.theMap.graphicData4.get(i);
				if (current.delete){
					game.theMap.graphicData4.remove(i);i--;
				} else {
					if (Function.checkForDrawing(this, current)){
						setAlpha(current.alpha, g2d);
						Image _sprite = Texture.getImage(current.name);if (current.useSprite){_sprite = current.sprite;}
						g2d.drawImage(_sprite, (int) Math.round(current.x - game.theMap.xCamera), (int) Math.round(current.y - game.theMap.yCamera), null);
					}
				}

			}
			for (int i = 0; i < game.theMap.graphicData3.size(); i++){
				Graphic current = game.theMap.graphicData3.get(i);
				if (current.delete){
					game.theMap.graphicData3.remove(i);i--;
				} else {
					if (Function.checkForDrawing(this, current)){
						setAlpha(current.alpha, g2d);
						Image _sprite = Texture.getImage(current.name);if (current.useSprite){_sprite = current.sprite;}
						g2d.drawImage(_sprite, (int) Math.round(current.x - game.theMap.xCamera), (int) Math.round(current.y - game.theMap.yCamera), null);
					}
				}
			}
		}

		for (int i = 0; i < game.theMap.charData.size(); i++){
			CharacterTemplate current = game.theMap.charData.get(i);
			if (current != null && current.sprite != null && !current.delete){
				if (current.visible == 1 && (Function.checkForDrawing(this, current))){
					int snap = 1;
					int tempx = ((int)current.x/snap)*snap+current.xDisplace;
					int tempy = ((int)current.y/snap)*snap+current.yDisplace;

					setAlpha(current.alpha, g2d);
					int xDisplace = 0;
					if (current.facing == -1){
						xDisplace = -current.sprite.getWidth(null);
					}
					try {
						g2d.drawImage(current.sprite, (int) (tempx - game.theMap.xCamera), (int) (tempy - game.theMap.yCamera)+1, current.sprite.getWidth(null)*current.facing, current.sprite.getHeight(null), null);
						Weapon tW = current.weapons.get(current.currentWeapon);
						try{
							g2d.drawImage(tW.sprite, (int)(tW.x*current.facing +xDisplace + tempx - game.theMap.xCamera), (int) (tW.y + tempy - game.theMap.yCamera), tW.w*current.facing, tW.h, null);
						} catch (NullPointerException e){};
					} catch (IndexOutOfBoundsException e){}
					catch (NullPointerException e){};
				}
			}
		}

		if (Global.DEBUG_MODE < 2){
			for (int i = 0; i < game.theMap.graphicData2.size(); i++){
				Graphic current = game.theMap.graphicData2.get(i);
				if (current.delete){
					game.theMap.graphicData2.remove(i);i--;
				} else {
					if (Function.checkForDrawing(this, current)){
						setAlpha(current.alpha, g2d);
						Image _sprite = Texture.getImage(current.name);if (current.useSprite){_sprite = current.sprite;}
						g2d.drawImage(_sprite, (int) Math.round(current.x - game.theMap.xCamera), (int) Math.round(current.y - game.theMap.yCamera), null);
					}
				}
			}

			for (int i = 0; i < game.theMap.graphicData1.size(); i++){
				Graphic current = game.theMap.graphicData1.get(i);
				if (current.delete){
					game.theMap.graphicData1.remove(i);i--;
				} else {
					if (Function.checkForDrawing(this, current)){
						setAlpha(current.alpha, g2d);
						Image _sprite = Texture.getImage(current.name);if (current.useSprite){_sprite = current.sprite;}
						g2d.drawImage(_sprite, (int) Math.round(current.x - game.theMap.xCamera), (int) Math.round(current.y - game.theMap.yCamera), null);
					}
				}
			}
			for (int i = 0; i < game.theMap.overlays.size(); i++){
				g2d.drawImage(game.theMap.overlays.get(i), 0, 0, null);
			}
		}


		for (int i = 0; i < game.theMap.specialData.size(); i++){
			SpecialObject current = game.theMap.specialData.get(i);
			if (Function.checkForDrawing(this, current)){
				setAlpha(current.alpha, g2d);
				game.theMap.specialData.get(i).draw(this, game.theMap, g2d);
			}
		}

		////////////////////////////////////////
		////////////////////////////////////////

		if (Global.DEBUG_MODE == 2){
			Image sprite = new ImageIcon(Board.class.getResource("/media/images/graphics/indicatorSquare.png")).getImage();

			for (int i = 0; i < game.theMap.tileData.size(); i++){
				Tile current = game.theMap.tileData.get(i);
				if (current.everywhere || (current.x<= game.theMap.xCamera+640 && current.x + current.w >= game.theMap.xCamera && current.y + current.h >= game.theMap.yCamera && current.y <= game.theMap.yCamera+ 480)){
					g2d.drawImage(sprite, (int) current.x - (int) game.theMap.xCamera, (int) current.y - (int) game.theMap.yCamera, current.w, current.h, null);
				}
			}
		}
	}

	public void drawOverlays(Graphics2D g2d){
		try{
			if (game.PAUSED_dialog == 0 && game.WAITING_dialog == 0){
				game.healthBar.setValue(game.player.health);
				game.healthBar.animateBar();
				game.healthBar.draw(g2d);
			}
		} catch (NullPointerException e){}

		try{
			if (game.PAUSED_dialog == 0 && game.WAITING_dialog == 0){
				game.energyBar.setValue(game.player.energy);
				game.energyBar.animateBar();
				game.energyBar.draw(g2d);
			}
		} catch (NullPointerException e){}

		if (game.PAUSED_dialog != 0){
			dialog.always(game.theMap, KeysDown, KeysDownTimer);
		}
		dialog.drawDialog(g2d);

		if (game.PAUSED_inventory){
			game.player.inventory.drawInventory(g2d);
		}
	}

	public void update(){
		for (int i=0; i<game.theMap.charData.size(); i++){
			CharacterTemplate current = game.theMap.charData.get(i);
			spriteCollisions(current);

			if (!current.equals(game.player)){
				if (current.delete && !current.managed){
					game.theMap.charData.remove(i);i--;
					continue;
				}
			}
			if (current.health>0){
				current.aiControl();
			}

			current.setSprite();
			current.physics(KeysDown);

			if (current.energy<current.energyMax){
				current.energy += current.energyRechargeSpeed;
			}
		}
		for (int i=0; i<game.theMap.specialData.size(); i++){
			if (game.theMap.specialData.get(i).delete && !game.theMap.specialData.get(i).managed){
				game.theMap.specialData.remove(i);i--;
				continue;
			}
			game.theMap.specialData.get(i).always(game.theMap, KeysDown, KeysDownTimer);
		}
	}

	public void spriteCollisions(CharacterTemplate current){
		current.floorHit = false;
		current.ceilingHit = false;
		current.leftHit = false;
		current.rightHit = false;
		current.arrowVisible = false;

		current.physicsMode = "normal";

		if (current.wallCollisions){
			for(int j=0; j<game.theMap.tileData.size(); j++){
				characterSidesTest(current,game.theMap.tileData.get(j));
			}
		}

		if (game.theMap.bossCharacterTestMode == 0){
			for(int j=0; j<game.theMap.charData.size(); j++){	
				if (!game.theMap.charData.get(j).equals(current)){
					if (Function.fullHitTest(current, game.theMap.charData.get(j))){
						game.theMap.charData.get(j).handleSpriteCollision(this, game.theMap, current);
					}
				}
			}
		} else {
			if (!game.player.equals(current)){
				if (Function.fullHitTest(current, game.player)){
					current.handleSpriteCollision(this, game.theMap, game.player);
				}
			}
		}


		for(int j = 0;j<game.theMap.specialData.size();j++){
			if (Function.fullHitTest(current, game.theMap.specialData.get(j))){
				game.theMap.specialData.get(j).onHit(this, game.theMap, current);
			}
		}


		if (current.floorHit || current.ceilingHit || current.leftHit || current.rightHit){
			if (current.disabled == -1){
				current.disabled = 0;
			}
		}
	}
	public void characterSidesTest(CharacterTemplate c, GameObject k){
		boolean foundHit = false;
		if (k.left == true){
			//if (c.dx >= 0){
			if (Function.leftHitTest(c, k)){
				/*if (c.dx>0){
					c.dx = 0;
				}*/
				c.x = k.x-c.hitBox[2];
				c.rightHit = true;
				foundHit = true;
			}
			//}
		}
		if (foundHit == false){
			if (k.right == true){
				//if (c.dx <= 0){
				if (Function.rightHitTest(c, k)){
					/*if (c.dx<0){
						c.dx = 0;
					}*/
					c.x = k.x+k.w-c.hitBox[0];
					c.leftHit = true;
					foundHit = true;
				}
				//}
			}
		}

		if (foundHit == false){
			if (k.floor == true){
				//if (c.dy >= 0){
				if (Function.floorHitTest(c, k)){

					if (c.dy > 0){
						c.dy = 0;
					}
					if (c.y+c.hitBox[1]+c.hitBox[3] > k.y){
						c.y = k.y-c.hitBox[3];
					}
					c.floorHit = true;
					foundHit = true;
				}
				//}
			}

		}
		if (foundHit == false){
			if (k.ceiling == true){
				//if (c.dy<=0){
				if (Function.ceilingHitTest(c, k)){
					if (c.dy < 0){
						c.dy = 0;
					}
					c.y = k.y+k.h - c.hitBox[1];
					c.ceilingHit = true;
					//current.canJump = false;
					foundHit = true;
				}
				//}
			}
		}
	}

	public void toggleInventory(){
		if (game.player.inventory.visible){
			game.player.inventory.hideInventory();
			game.PAUSED_inventory = false;
		} else {
			game.player.inventory.setupInventory();
			game.PAUSED_inventory = true;
		}
	}



	class TAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();

			if (gameState == 1)
			{
				if (key == KeyEvent.VK_UP)
				{
					KeysDown[0] = true;
					KeysDownTimer[0]++;
				}
				if (key == KeyEvent.VK_DOWN){
					KeysDown[1] = true;
					KeysDownTimer[1]++;
				}
				if (key == KeyEvent.VK_LEFT){
					KeysDown[2] = true;
					KeysDownTimer[2]++;
				}
				if (key == KeyEvent.VK_RIGHT){
					KeysDown[3] = true;
					KeysDownTimer[3]++;
				}
				if (key ==  KeyEvent.VK_Z){
					KeysDown[4] = true;
				}
				if (key ==  KeyEvent.VK_X){
					KeysDown[5] = true;
					KeysDownTimer[5]++;
				}
				if (key == KeyEvent.VK_W){
					KeysDown[6] = true;
					KeysDownTimer[6]++;
				}
				if (key == KeyEvent.VK_A){
					KeysDown[7] = true;
					KeysDownTimer[7]++;
				}
				if (key == KeyEvent.VK_S){
					KeysDown[8] = true;
					KeysDownTimer[8]++;
				}
				if (key == KeyEvent.VK_D){
					KeysDown[9] = true;
					KeysDownTimer[9]++;
				}
				if (key == KeyEvent.VK_Q){
					KeysDown[10] = true;
					KeysDownTimer[10]++;
				}
				if (key == KeyEvent.VK_E){
					KeysDown[11] = true;
					KeysDownTimer[11]++;
				}
				if (key == KeyEvent.VK_I){
					if (game.PAUSED_dialog == 0){
						toggleInventory();
					}
				}
				if (key == KeyEvent.VK_P){
					game.audio.playSound("thunder");

				}
				if (Global.DEBUG_MODE > 0){
					if (key == KeyEvent.VK_N){
						game.resetMap();
					}

					if (key == KeyEvent.VK_T){
						game.player.inventory.add(new Graphic("brick",0,0, true));
						game.player.inventory.setupInventory();
					}
					/*	if (key == KeyEvent.VK_O){
						cut--;
					}
					if (key == KeyEvent.VK_U){
						cut++;
					}*/
					if (key == KeyEvent.VK_V){
						if (Global.DEBUG_MODE == 1){
							Global.DEBUG_MODE = 2;
						} else {
							Global.DEBUG_MODE = 1;
						}
					}
				}
			}
			if (gameState >= 1) {
				if (key == KeyEvent.VK_SPACE){

					if (gameState == 1){
						gameState = 2;
						game.audio.pauseSongs();
					} else if (gameState == 2){
						gameState = 1;
						game.audio.resumeSongs();
					}
				}
			} else {
				if (key == KeyEvent.VK_SHIFT){

				}
			}

			if (key == KeyEvent.VK_ESCAPE){
				if (game != null){
					game.audio.endSongs();
				}
				System.exit(0);
			}
			if (key == 192){
				drawConsole = !drawConsole;
			}
		}
		public void keyReleased(KeyEvent e) {
			int key = e.getKeyCode();

			if (key == KeyEvent.VK_UP){
				KeysDown[0] = false; 
				KeysDownTimer[0] = 0;
			}
			if (key ==  KeyEvent.VK_DOWN){
				KeysDown[1] = false; 
				KeysDownTimer[1] = 0;
			}
			if (key ==  KeyEvent.VK_LEFT){
				KeysDown[2] = false; 
				KeysDownTimer[2] = 0;
			}
			if (key ==  KeyEvent.VK_RIGHT){
				KeysDown[3] = false;
				KeysDownTimer[3] = 0;
			}
			if (key ==  KeyEvent.VK_Z){
				KeysDown[4] = false;
			}
			if (key ==  KeyEvent.VK_X){
				KeysDown[5] = false;
				KeysDownTimer[5] = 0;
			}
			if (key ==  KeyEvent.VK_W){
				KeysDown[6] = false;
				KeysDownTimer[6] = 0;
			}
			if (key ==  KeyEvent.VK_A){
				KeysDown[7] = false;
				KeysDownTimer[7] = 0;
			}
			if (key ==  KeyEvent.VK_S){
				KeysDown[8] = false;
				KeysDownTimer[8] = 0;
			}
			if (key ==  KeyEvent.VK_D){
				KeysDown[9] = false;
				KeysDownTimer[9] = 0;
			}
			if (key ==  KeyEvent.VK_Q){
				KeysDown[10] = false;
				KeysDownTimer[10] = 0;
			}
			if (key ==  KeyEvent.VK_E){
				KeysDown[11] = false;
				KeysDownTimer[11] = 0;
			}
		}
	}
}