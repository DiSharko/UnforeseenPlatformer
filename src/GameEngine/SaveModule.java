package GameEngine;

public class SaveModule extends SpecialObject {
	public SaveModule(double _x, double _y, int _w, int _h, String _id){
		x = _x;
		y = _y;
		w = _w;
		h = _h;
		id = _id;
		setDefaultHitBox();
	}
	public void onHit(Board board, Map map,CharacterTemplate current){
		if (Global.saveEnabled){
			if (board.KeysDownTimer[1] == 1 && current.interacting){
				board.game.writeSaveSettings(1, this);
				board.game.theMap.specialData.add(new DialogReporter(this, "Game Saved"));
				board.KeysDownTimer[1]++;
			}
		}
	}

}
