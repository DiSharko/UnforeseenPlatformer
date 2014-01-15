package GameEngine;

public class Buffer extends SpecialObject {
	double force = 2;
	int disable = 0;

	public Buffer (double _x, double _y, int _w, int _h, String _props){
		type = "buffer";

		floor = true;
		ceiling = true;
		left = true;
		right = true;

		String[] _propList = _props.split(";");
		for (int i = 0; i<_propList.length; i++){
			try{
				String[] prop = _propList[i].split(":");
				if (prop[0].equals("floor")){
					floor = Boolean.parseBoolean(prop[1]);
				} else if (prop[0].equals("ceiling")){
					ceiling = Boolean.parseBoolean(prop[1]);
				} else if (prop[0].equals("left")){
					left = Boolean.parseBoolean(prop[1]);
				} else if (prop[0].equals("right")){
					right = Boolean.parseBoolean(prop[1]);
				} else if (prop[0].equals("disable")){
					disable = (int)Double.parseDouble(prop[1]);
				} else if (prop[0].equals("force")){
					force = Double.parseDouble(prop[1]);
				}
			} catch (Exception e){Console.out("Error with buffer setting "+_propList[i]);}
		}


		x = _x;
		y = _y;
		w = _w;
		h = _h;



		setDefaultHitBox();
	}
	public void onHit(Board board, Map map, CharacterTemplate current){
		if (this.floor){
			if (Function.floorHitTest(current, this)){
				current.dy = -force;
				current.disabled = disable;
			}
		}
		if (this.ceiling){
			if (Function.ceilingHitTest(current, this)){
				current.dy = force;
				current.disabled = disable;
			}
		}
		if (this.left){
			if (Function.leftHitTest(current, this)){
				current.dx = -force;
				current.disabled = disable;
			}
		}
		if (this.right){
			if (Function.rightHitTest(current, this)){
				current.dx = force;
				current.disabled = disable;
			}
		}
	}
}
