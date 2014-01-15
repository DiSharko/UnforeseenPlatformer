package GameEngine;

import java.util.ArrayList;

public class DownFloor extends SpecialObject {
	int broken;
	ArrayList<CharacterTemplate> brokenCharacters = new ArrayList<CharacterTemplate>();
	ArrayList<Integer> brokenTimes = new ArrayList<Integer>();

	public DownFloor(double tx, double ty, int tw, int th){
		x = tx;
		y = ty;
		w = tw;
		h = th;

		broken = 0;

		floor = true;
		setDefaultHitBox();
	}
	public void onHit(Board board, Map map, CharacterTemplate current){
		//Console.out(board.downDown);
		int found = -1;
		for (int i = 0; i<brokenCharacters.size(); i++){
			if (current.equals(brokenCharacters.get(i))){
				found = i;
			}
		}
		if (found == -1){
			if (current.interacting){
				brokenCharacters.add(current);
				//board.KeysDownTimer[1]++;
				brokenTimes.add(50);
				if (current.dy == 0){
					current.dy++;
				}
			} else {
				if (current.dy>=0){
					board.characterSidesTest(current, this);
					if (current.floorHit){
						current.arrowVisible = true;
					}
				}
			}
		}

	}
	public void always(Map map, boolean[] KeysDown, int[]KeysDownTimer){
		for (int i = 0; i<brokenCharacters.size(); i++){
			if (brokenTimes.get(i) > 0){
				int temp = brokenTimes.get(i) - 1;
				brokenTimes.set(i, temp);
			} else {
				brokenCharacters.remove(i);
				brokenTimes.remove(i);
			}
		}
	}
}

