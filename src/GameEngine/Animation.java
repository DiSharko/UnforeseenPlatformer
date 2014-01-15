package GameEngine;

import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Animation extends SpecialObject {

	int repeatTime = 0;
	int changeDelay = -1;
	int repeatDelay = -1;
	int initialDelay = 0;
	int frame = 0;
	boolean singleCycle = false;
	ArrayList<Image> state = new ArrayList<Image>();


	public Animation(String _name, double _x, double _y, int _w, int _h, String _properties){
		name = _name;
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
		x = _x;
		y = _y;
		w = _w;
		h = _h;
		setDefaultHitBox();

		String[] _properties2 = _properties.split(";");
		for (int j = 0; j < _properties2.length; j++){
			String[] _settings = _properties2[j].split(":");

			if (_settings[0].equals("changeDelay")){
				changeDelay = Integer.parseInt(_settings[1]);
			} else if (_settings[0].equals("repeatDelay")){
				repeatDelay = Integer.parseInt(_settings[1]);
			} else if (_settings[0].equals("initialDelay")){
				initialDelay = Integer.parseInt(_settings[1]);
			} else if (_settings[0].equals("singleCycle")){
				singleCycle = true;
			}
		}
	}

	public void always(Map map, boolean[] KeysDown, int[]KeysDownTimer){
		if (initialDelay<=0){
			if (time>changeDelay){
				if (frame + 1 >= state.size()){
					if (!singleCycle){
						if (repeatTime > repeatDelay){
							repeatTime = 0;
							time = 0;
							frame = 0;
							sprite = state.get(frame);
						} else {
							repeatTime++;
						}
					} else {
						delete = true;
					}
				} else {
					time = 0;
					frame++;
					sprite = state.get(frame);
				}
			} else {
				time++;
			}
		} else{
			initialDelay--;
		}
	}
}
