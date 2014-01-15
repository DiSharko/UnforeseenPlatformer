package GameEngine;

import java.awt.Color;
import java.awt.Graphics2D;

public class Warp extends SpecialObject {
	String target;
	String trigger = "interact"; // interact, instant
	String offSet = "none";

	double triggered = 0;
	double animationTimer = 0;

	public Warp(double _x, double _y, double _w, double _h, String _props){
		type = "warp";
		everywhere = true;

		x = _x;
		y = _y;
		w = (int)_w;
		h = (int)_h;

		String[] _propList = _props.split(";");
		for (int i = 0; i<_propList.length; i++){
			try{
				String[] prop = _propList[i].split(":");
				if (prop[0].equals("name")){
					name = prop[1];
				} else if (prop[0].equals("target")){
					target = prop[1];
				} else if (prop[0].equals("offSet")){
					offSet = prop[1];
				} else if (prop[0].equals("instant")){
					trigger = "instant";
				} else if (prop[0].equals("trigger")){
					trigger = prop[1];
				}
			} catch (Exception e){Console.out("Error with warp setting "+_propList[i]);}
		}

		setDefaultHitBox();
	}

	public void onHit(Board _board, Map _map, CharacterTemplate _current){
		if (triggered == 0){
			if (_current.equals(_board.game.player)){
				if (trigger.equals("instant")){
					triggered = 1;
				} else if (trigger.equals("interact") && _board.KeysDownTimer[1] == 1 && _current.interacting){
					_board.KeysDownTimer[1]++;
					triggered = 1;
				}
			}
		}
	}


	public void draw(Board _board, Map _map, Graphics2D g2d) {
		if (triggered == 1){
			animationTimer+=0.2;
			if (animationTimer>=1){
				triggered = 2;
				animationTimer = 1;
			}

			Color grayScale = new Color(0, 0, 0, (int)(225*animationTimer));
			g2d.setColor(grayScale);
			g2d.fillRect(0, 0, 640, 480);


		} else if (triggered == 2){
			g2d.setColor(Color.black);
			g2d.fillRect(0, 0, 640, 480);
			warpPlayer(_board, _map);
			triggered = 3;
		} else if (triggered == 3){

			animationTimer-=0.1;
			if (animationTimer<=0){
				triggered = 0;
				animationTimer = 0;
			}

			Color grayScale = new Color(0, 0, 0, (int)(225*animationTimer));
			g2d.setColor(grayScale);
			g2d.fillRect(0, 0, 640, 480);

			if (animationTimer<=0){
				triggered = 0;
				animationTimer = 0;
			}
		}
	}


	public void warpPlayer(Board _board, Map _map){
		for (int i = 0; i < _map.specialData.size(); i++){
			SpecialObject _warp = _map.specialData.get(i);
			if (_warp.type.equals("warp")){
				if (_warp.name.equals(target)){
					int _xOffSet = 0;
					int _yOffSet = 0;
					if (offSet.equals("bottom")){
						_yOffSet = _board.game.player.h+1;
					} else if (offSet.equals("left")){
						_xOffSet = -_board.game.player.w-1;
					} else if (offSet.equals("right")){
						_xOffSet = _board.game.player.w+1;
					}
					_board.game.player.x = _warp.x + _xOffSet;
					_board.game.player.y = _warp.y+_warp.h-_board.game.player.h + _yOffSet;
					_board.game.theMap.xCamera = _board.game.player.x-320;
					_board.game.theMap.yCamera = _board.game.player.y-240;
				}
			}
		}
	}
}
