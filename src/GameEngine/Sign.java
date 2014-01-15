package GameEngine;

import java.util.ArrayList;

public class Sign extends SpecialObject {
	String message;

	int dialogType = 0;
	ArrayList <Conversation> conversations = new ArrayList<Conversation>();

	CharacterTemplate cHost;
	String cHostID;


	int lastHit;
	boolean willInteract = true;
	public Sign (double xpos, double ypos, int width, int height, ArrayList<Conversation> convos){
		//sObjectType = "sign";
		x = xpos;
		y = ypos;
		w = width;
		h = height;
		conversations = convos;
		//activate = a;
		//message = m;
		lastHit = 0;
		setDefaultHitBox();
	}
	public Sign (double xpos, double ypos, int width, int height, Conversation c){
		//sObjectType = "sign";
		x = xpos;
		y = ypos;
		w = width;
		h = height;
		conversations.add(c);
		//activate = a;
		//message = m;
		lastHit = 0;
		setDefaultHitBox();
	}

	public void onHit(Board board, Map map, CharacterTemplate current){
		if (current.equals(board.game.player) && !board.dialog.visible){
			int quest = board.game.quest.questProgress;
			int currentConversation = 0;
			int closestDifference = -1;
			for (int i = 0; i<conversations.size(); i++){
				if (quest >= conversations.get(i).questNumber){
					if (quest - conversations.get(i).questNumber < closestDifference || closestDifference == -1){
						currentConversation = i;
					}
				}
			}
			if (conversations.size()>0){
				if (conversations.get(currentConversation).activateType == 1){
					if (willInteract){
						willInteract = false;
						conversations.get(currentConversation).useConversation(board, this);
					}
				} else if (conversations.get(currentConversation).activateType == 0){
					if (board.KeysDownTimer[1] == 1){
						board.KeysDownTimer[1]++;
						current.interacting = false;
						conversations.get(currentConversation).useConversation(board, this);
					}
				}
			}

			/*if (board.KeysDownTimer[1] == 1){
			if (!board.dialog.visible){
				board.KeysDownTimer[1]++;
				current.interacting = false;
			}
		}*/

			lastHit = 15;
		}
	}




	public void always(Map map, boolean[] KeysDown, int[]KeysDownTimer){
		if (lastHit > 0){
			lastHit--;
		} else {
			willInteract = true;
		}
	}
}
