package GameEngine;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class CrumbleBlock extends SpecialObject {

	ArrayList<CrumbleBlockInstance> blocks = new ArrayList<CrumbleBlockInstance>();

	public CrumbleBlock (String string){
		//String n, double tx, double ty, int tw, int th, boolean f, boolean c, boolean l, boolean r, int t

		String[] blocksList = string.split(";;");
		for (int i = 0; i<blocksList.length; i++){
			String[] props = blocksList[i].split(";");
			double tx = Integer.parseInt(props[1]);
			double ty = Integer.parseInt(props[2]);
			int tw = Integer.parseInt(props[3]);
			int th = Integer.parseInt(props[4]);
			boolean f = Boolean.parseBoolean(props[5]);
			boolean c = Boolean.parseBoolean(props[6]);
			boolean l = Boolean.parseBoolean(props[7]);
			boolean r = Boolean.parseBoolean(props[8]);
			String name = props[9];
			int time = Integer.parseInt(props[10]);

			blocks.add(new CrumbleBlockInstance(name,tx, ty, tw, th,f,c,l,r,time));
		}
		setSize();
		setDefaultHitBox();
	}

	public void setSize(){
		for (int i = 0; i<blocks.size();i++){
			CrumbleBlockInstance b = blocks.get(i);
			if (i == 0){
				x = b.x;
				y = b.y;
				w = b.w;
				h = b.h;
			} else {
				if (b.x<x){
					w = (int)Math.abs(x+w-b.x);
					x = b.x;
				} else if (b.x > x){
					w = (int)Math.abs(b.x+b.w-x);
				}
				if (b.y<y){
					h = (int)Math.abs(y+h-b.y);
					y = b.y;
				} else if (b.y > y){
					h = (int)Math.abs(b.y+b.h-y);
				}
			}
		}
		Console.out("x: "+x+"  y: "+y+"  w: "+w+"  h: "+h);
	}

	public void onHit(Board board, Map map, CharacterTemplate current){
		Boolean broken = false;
		for (int i = 0; i< blocks.size(); i++){
			if (!broken){
				broken = blocks.get(i).checkHit(board, current);
				if (broken){
					i = -1;
				}
			} else {
				blocks.get(i).breaking = true;
			}
		}

	}
	public void always(Map map, boolean[] KeysDown, int[]KeysDownTimer){
		for (int i = 0; i< blocks.size(); i++){
			blocks.get(i).always(map, KeysDown, KeysDownTimer);
		}
	}

	public void draw(Board _board, Map theMap, Graphics2D g2d) {
		for (int i = 0; i< blocks.size(); i++){
			blocks.get(i).draw(_board, theMap, g2d);
		}
	}
}
