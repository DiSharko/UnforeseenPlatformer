package GameEngine;

public class Conversation {
	int questNumber = -1;
	int displayType = 0;
	int activateType = 0;
	String id = "";

	String text;
	public Conversation (String _text, String _props){
		text = _text;

		String[] _settings = _props.split(";");
		for (int i = 0; i<_settings.length;i++){
			String[] _setting = _settings[i].split(":");
			try{
				if (_setting[0].equals("q")){
					questNumber = Integer.parseInt(_setting[1]);
				
				} else if (_setting[0].equals("a")){
					activateType = Integer.parseInt(_setting[1]);
				
				} else if (_setting[0].equals("d")){
					displayType = Integer.parseInt(_setting[1]);
				
				} else if (_setting[0].equals("id")){
					id = _setting[1];
				}

			} catch (Exception e){};
		}
	}

	public void setActivation(int a){activateType = a;}
	public void setDisplay(int d){displayType = d;}
	public void setQuest(int q){questNumber = q;}

	public void useConversation(Board board, CharacterTemplate player){
		int questAdvancer = -1;
		if (board.game.PAUSED_dialog == 0){
			if (displayType == 0){
				questAdvancer = board.dialog.newMessage(player, text);
			} else if (displayType == 1){
				DialogReporter d = new DialogReporter(player, text);
				board.game.theMap.specialData.add(d);
				questAdvancer = d.questAdvancer;
			}
		}
		if (questAdvancer != -1){
			Console.out("Advanced quest to "+questAdvancer);
			board.game.quest.advanceQuest(questAdvancer);
		}
	}

	public void useConversation(Board board, Sign sign){
		int questAdvancer = -1;
		if (displayType == 0){
			if (board.game.PAUSED_dialog == 0){
				questAdvancer = board.dialog.newMessage(sign.cHost, text);
			}
		} else if (displayType == 1){
			DialogReporter d = new DialogReporter(sign, text);
			board.game.theMap.specialData.add(d);
			questAdvancer = d.questAdvancer;
		}
		if (questAdvancer != -1){
			Console.out("Advanced quest to "+questAdvancer);
			board.game.quest.advanceQuest(questAdvancer);
		}
	}
}
