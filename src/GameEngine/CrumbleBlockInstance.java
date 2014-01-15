package GameEngine;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class CrumbleBlockInstance extends SpecialObject {

	int solidTime;
	int breakTime;
	boolean breaking = false;
	boolean broken = false;

	ArrayList<Image> state = new ArrayList<Image>();




	public CrumbleBlockInstance(String n, double tx, double ty, int tw, int th, boolean f, boolean c, boolean l, boolean r, int t){
		name = n;
		boolean allImp = false;
		short i = 1;
		while (!allImp){
			try {
				state.add(new ImageIcon(Board.class.getResource("/media/images/graphics/"+ name +"/"+i+".png")).getImage());
				i++;
			} catch (NullPointerException e){
				allImp = true;
			}
		}
		sprite = state.get(0);
		x = tx;
		y = ty;
		w = tw;
		h = th;
		solidTime = t;
		floor = f;
		ceiling = c;
		left = l;
		right = r;
		setDefaultHitBox();
	}



	public boolean checkHit(Board board, CharacterTemplate current){
		if (!broken){
			board.characterSidesTest(current, this);
			if (Function.floorHitTest(current, this)){
				breaking = true;
				return true;
			}
		}
		return false;
	}
	public void always(Map map, boolean[] KeysDown, int[]KeysDownTimer){
		if (breaking && !broken){
			if (solidTime>0){
				solidTime--;
			} else {
				broken = true;
			}
		}
		if (broken){
			if (breakTime < (state.size()-1)*200){
				sprite = state.get((int)breakTime/200+1);
				breakTime++;
			}
		}
	}

	public void draw(Board _board, Map theMap, Graphics2D g2d) {
		int fComp = 0;
		if (facing == -1)
			fComp = w;
		g2d.rotate(rotation);
		if (Function.checkForDrawing(_board, this)){
			g2d.drawImage(sprite, (int) x - (int) theMap.xCamera + fComp, (int) y - (int) theMap.yCamera, w*facing, h, null);
		}
	}



}
