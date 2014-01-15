package GameEngine;

public class Minigame_Quest extends Quest {
	public Minigame_Quest(Board b, Game g, int q){
		//startingMap = "minigame";
		//startingEntrance = "";

		questProgress = 30;

		//if (Global.DEBUG_MODE == 0){
			startingMap = "minigame_chooser";
			startingEntrance = "";
		//}

		if (q<0){q=0;}
		board = b;
		game = g;
		
		
		game.player = new Adam(board, 0, 0, 0);

		
		game.energyBar = new Bar(game,5,5,100,5,game.player.energyMax,false);
		
		//if (questProgress > 2){
		game.player.weapons.add(new BasicAttacks(false,true,false));
		//}
	}
	public void checkMapInitScripts(String mapName, Map theMap){
		try {
			if (mapName.equals("minigame")){
				for (int i = 0; i< 1000; i++){
					addPlatform(theMap);
				}
			} else if (mapName.equals("minigame_chooser")){
				theMap.specialData.add(new OnscreenText(200,100,"text:Use the arrow keys to move.;size:16"));
				theMap.specialData.add(new OnscreenText(100,130,"text:Up arrow to jump, down arrow to interact with objects.;size:16"));
				theMap.specialData.add(new OnscreenText(50,160,"text:Press 'D' to teleport vertically, and 'S' to teleport horizontally.;size:16"));
			}
		} catch (NullPointerException e){
			Console.out("No mapName passed in!");
		}
	}


	public void addPlatform(Map theMap){
		int x = (int)(Math.random()*3104-1184);
		double speed = (Math.random()*2);
		int level = (int)(Math.random()*200+2);
		int length = (int)(Math.random()*6+1);
		theMap.specialData.add(new MovingPlatform(board, theMap, "x:"+x+";y:"+(288-(level-1)*32)+";left:-1184;right:1920;movement:boundaries;dx:"+speed+";length:"+length+";style:office;preset:normal"));
	}

}
