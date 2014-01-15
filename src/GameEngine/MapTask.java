package GameEngine;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class MapTask extends SpecialObject {
	Board board;
	Map theMap;
	AudioInterface audio;
	int time1 = 0;
	int time2 = 0;
	int time3 = 0;

	String specificType = "";

	double speed = 1;


	double size = 0;
	byte direction = 1;
	int total = 0;

	double double1 = 0;

	boolean boolean1 = false;
	boolean boolean2 = false;
	boolean boolean3 = false;

	ArrayList<GameObject> container1 = new ArrayList<GameObject>();

	String type;


	public MapTask(Board b, Map map, String info){ // in case map is inside constructor
		try{
			board = b;
			theMap = map;
			audio = board.game.audio;

			everywhere = true;

			String[] props = info.split(";");

			boolean exit = false;
			for (int i = 0; !exit; i++){
				String[] settings = {};
				try{
					settings = props[i].split(":"); //"type:amble;x:100;floor:true;delay:1"

					if (settings[0].equals("type")){
						type = settings[1];
					} else if (settings[0].equals("x")){
						x = Double.parseDouble(settings[1]);
					} else if (settings[0].equals("y")){
						y = Double.parseDouble(settings[1]);
					} else if (settings[0].equals("w")){
						w = Integer.parseInt(settings[1]);
					} else if (settings[0].equals("h")){
						h = Integer.parseInt(settings[1]);
					} else if (settings[0].equals("t1")){
						time1 = Integer.parseInt(settings[1]);
					} else if (settings[0].equals("t2")){
						time2 = Integer.parseInt(settings[1]);
					} else if (settings[0].equals("time")){
						time = Integer.parseInt(settings[1]);
					} else if (settings[0].equals("trackDepth")){
						boolean1 = true; // vision radius depends on depth?
					} else if (settings[0].equals("size")){
						size = Double.parseDouble(settings[1]);
					} else if (settings[0].equals("direction")){
						direction = Byte.parseByte(settings[1]);
					} else if (settings[0].equals("total")){
						total = Integer.parseInt(settings[1]);
					} else if (settings[0].equals("speed")){
						speed = Double.parseDouble(settings[1]);
					} else if (settings[0].equals("specificType")){
						specificType = settings[1];
					}
				} catch (IndexOutOfBoundsException e){
					exit = true;
				} catch (Exception e){
					Console.out("Error with the value of \"" + settings[0]+ "\": "+settings[1]+".");
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		setDefaultHitBox();
		initializeTask();
	}

	public void initializeTask(){
		if (type.equals("lightningFlash")){
			alpha = 0;
			if (time1 == 0){
				time1 = 7;
			}
			if (time2 == 0){
				time2 = 22;
			}
			if (time3 == 0){
				time3 = 15;
			}
		} else if (type.equals("followTime")){
			if (board.game.quest.timeOfDay.equals("dawn")){
				theMap.bgColor = COLOR.dawn;
			} else if (board.game.quest.timeOfDay.equals("day")){
				theMap.bgColor = COLOR.day;
			} else if (board.game.quest.timeOfDay.equals("dusk")){
				theMap.bgColor = COLOR.dusk;
			} else if (board.game.quest.timeOfDay.equals("night")){
				theMap.bgColor = COLOR.night;
			}
		} else if (type.equals("reducedVision")){
			theMap.reducedVision = true;
			if (size == 0){
				size = 300;
			}
			if (boolean1){
				//double2 = 770;
				double1 = y;
			} else {
				theMap.xLeft = (int) (board.gameDimensions.width/2-size/2);
				theMap.xRight = (int) (board.gameDimensions.width/2+size/2);
				theMap.yTop = (int) (board.gameDimensions.height/2-size/2);
				theMap.yBottom = (int) (board.gameDimensions.height/2+size/2);
			}
		}
	}

	public void always(Map map, boolean[] KeysDown, int[]KeysDownTimer){
		if (type.equals("lightningFlash")){
			time++;
			if (time < time1){
				alpha = (float) (time*0.9/time1);
			} else if (time < time1+time2){
				alpha = (float)0.9;
			} else if (time < time1+time2+time3){
				alpha = (float) (0.9-(time-time1-time2)/(time3)*0.9);
			} else {
				delete = true;
			}
		} else if (type.equals("followTime")){

		} else if (type.equals("reducedVision")){
			if (boolean1){ // if following depth
				size = 640-speed*(board.game.player.y-double1);
				if (size < 250){
					size = 250;
				}
			}
		} else if (type.equals("sideSpawner")){
			if (container1.size()<total){
				if (time1 <= 0){
					if (specificType.equals("spirit1")){
						CharacterTemplate c = new Character(board, 1, specificType, x, y+(int)(Math.random()*h)*32,48,32,"facing:"+direction+";preset:spirit1");
						map.charData.add(c);
						container1.add(c);
						time1 = (int)(Math.random()*50+20);
					}
				} else {
					time1--;
				}
			}
			for (int i = 0; i < container1.size(); i++){
				if (!Function.fullHitTest(board.game.player,container1.get(i),640*2,480*2)){
					container1.get(i).delete = true;
					container1.remove(i);
				}
			}
		}

	}
	public void draw(Board _board, Map theMap, Graphics2D g2d) {
		if (type.equals("lightningFlash")){
			g2d.setColor(new Color(1,1,(float)0.9,alpha));
			g2d.fillRect(0,0,640,480);

		} else if (type.equals("followTime")){
			if (board.game.quest.timeOfDay.equals("night")){
				BufferedImage overlay = new BufferedImage(640,480, BufferedImage.TYPE_INT_ARGB);
				Graphics2D creator = overlay.createGraphics();
				creator.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
				creator.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);

				creator.setColor(new Color(0,0,0,(float)0.6));
				creator.fillRect(0, 0, 640, 480);

				creator.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
				for(int i = 0; i < board.game.theMap.lightSources.size(); i++){
					creator.fill(Function.mapPolygon(board.game.theMap.lightSources.get(i), board.game.theMap.xCamera, board.game.theMap.yCamera));
				}
				g2d.drawImage(overlay,0,0,null);
			}

		} else if (type.equals("reducedVision")){
			BufferedImage image = new BufferedImage(theMap.xRight-theMap.xLeft+128, theMap.yBottom-theMap.yTop+128, BufferedImage.TYPE_INT_ARGB);
			Graphics2D caveOverlay = image.createGraphics();
			caveOverlay.setColor(Color.BLACK);
			caveOverlay.fillRect(0, 0, theMap.xRight-theMap.xLeft+128, theMap.yBottom-theMap.yTop+128);
			caveOverlay.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
			caveOverlay.fillOval(64,64, (int)size, (int)size);
			g2d.drawImage(image,theMap.xLeft-64-(int)(board.game.theMap.xCamera-board.game.player.x+board.gameDimensions.width/2),  theMap.yTop-64-(int)(board.game.theMap.yCamera-board.game.player.y+board.gameDimensions.height/2),  null);
		}
	}
}
