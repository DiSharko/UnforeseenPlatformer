package GameEngine;

import java.util.ArrayList;

public class GraphicBlock extends SpecialObject {
	public GraphicBlock(Map map, int layer, String type, double tx, double ty, int tw, int th){
		ArrayList<Graphic> temp = new ArrayList<Graphic>();
		for (int i = 0; i < tw/32; i++){
			int tI = i*32;
			for (int j = 0; j < th/32; j++){
				temp.add(new Graphic(type, tx+tI, ty+j*32, true));
			}
		}
		if (layer == 1){
			map.graphicData1.addAll(temp);
		} else if (layer == 2){
			map.graphicData2.addAll(temp);
		} else if (layer == 3){
			map.graphicData3.addAll(temp);
		} else if (layer == 4){
			map.graphicData4.addAll(temp);
		} else {
			Console.out("Layer "+layer+" is not an acceptable layer choice for GraphicBlock (type: "+type+").");
		}
		delete = true;
	}
}
