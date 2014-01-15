package GameEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import java.io.PrintStream;
import java.util.ArrayList;

public class Game {
	Board board;

	//boolean PAUSED_game = false;
	int PAUSED_dialog = 0;
	int WAITING_dialog = 0;
	boolean PAUSED_inventory = false;
	
	Quest quest;
	ArrayList<String[]> trackers;

	int save = 0;
	Map theMap;
	AudioInterface audio;
	ArrayList<String> currentSongs = new ArrayList<String>();

	CharacterTemplate focus;
	CharacterTemplate player;

	double deathResetTimer = 0;
	String restoreMap = "null";
	double restoreX = 0;
	double restoreY = 0;

	Bar healthBar;
	Bar energyBar;
	//Bar manaBar;



	public Game(Board b){
		board = b;
		audio = new AudioInterface();

		theMap = new Map(board, null);
		trackers = new ArrayList<String[]>();
		preloadOperations();
		readSaveSettings(1);
	}



	public void preloadOperations(){
		Character c = new Character(board, -1, "", 10000000, 10000000, 1, 1, null);
		new Fireball1(c);
		new DamageReporter(c, 0);
		new DialogReporter(c, "");
		FileOutputStream out;
		PrintStream p;
		try {
			out = new FileOutputStream("");
			p = new PrintStream(out); 
			p.close();
		} catch (FileNotFoundException e){}
	}
	public void reloadFromSave(){
		deathResetTimer = 0;
		readSaveSettings(1);
		reloadMap();
	}
	public void resetMap(){
		theMap.nextMap = theMap.currentMap;
		theMap.nextEntrance = theMap.currentEntrance;
		theMap.reload = 1;
		player.health = player.healthMax;
		memoryOperations();
	}

	public void loadNullMap(){
		theMap.nextMap = "null";
		theMap.nextEntrance = "default";
		theMap.reload = 1;
	}
	public void memoryOperations(){
		try {
			player.damageReporter.delete = true;
		} catch (NullPointerException e){}
	}

	public void reloadMap(){
		memoryOperations();
		String nextMap = theMap.nextMap;

		String nextEntrance = theMap.nextEntrance;
		double _x = 0;
		double _y = 0;
		boolean restoring = false;
		try{
			if (theMap.currentMap.equals("null")){
				restoring = true;
				nextMap = restoreMap;
				_x = restoreX;
				_y = restoreY;
			}
		} catch (NullPointerException e){
			loadNullMap();
		}

		try{
			if (!nextMap.equals(null)){
				Console.out("Loading the "+nextMap+" level.");
			}
		} catch (NullPointerException e){
			loadNullMap();
			Console.out("Why isn't there a new Level to load?");
		}
		try{
			if (!nextEntrance.equals(null)){
				Console.out("Entering at the "+nextEntrance+" entrance.");
			}
		} catch (NullPointerException e){
			nextEntrance = "default";
			Console.out("Entering at the "+nextEntrance+" entrance.");
		}

		theMap = null;
		theMap = new Map(board, nextMap);

		if (theMap.songs.size() == 0){
			audio.endSongs();
		}
		// If any songs were playing that aren't in the new level, stop them.
		for (int i = 0; i<audio.songNames.size(); i++){
			boolean foundSong = false;
			for (int j = 0; j<theMap.songs.size(); j++){
				if (audio.songNames.get(i).equals(theMap.songs.get(j))){
					foundSong = true;
				}
			}
			if (!foundSong){
				audio.endSong(audio.songNames.get(i));
				i--;
			}
		}
		// Start playing all songs that weren't already playing.
		for (int i = 0; i<theMap.songs.size(); i++){
			boolean foundSong = false;
			for (int j = 0; j<audio.songNames.size(); j++){
				if (theMap.songs.get(i).equals(audio.songNames.get(j))){
					foundSong = true;
				}
			}
			if (!foundSong){
				audio.playSong(theMap.songs.get(i));
			}
		}

		quest.checkMapInitScripts(nextMap, theMap);

		if (!theMap.loadSuccess){
			loadNullMap();
		} else {
			int temp = -1;
			if (!restoring){
				if (theMap.entrances.size()>0){
					for (int i = 0; i < theMap.entrances.size(); i++){
						if (theMap.entrances.get(i).get(0).equals(nextEntrance)){
							temp = i;
						}
					}
					try {
						_x = Double.parseDouble(theMap.entrances.get(temp).get(1));
						_y = Double.parseDouble(theMap.entrances.get(temp).get(2));
					} catch (Exception e){
						Console.out("Error using the "+ nextEntrance +" entrance.  Using default instead.");
						for (int i = 0; i < theMap.entrances.size(); i++){
							if (theMap.entrances.get(i).get(0).equals("default")){
								temp = i;
							}
						}
						try {
							_x = Double.parseDouble(theMap.entrances.get(temp).get(1));
							_y = Double.parseDouble(theMap.entrances.get(temp).get(2));
						} catch (Exception e1){
							Console.out("Error using the default entrance.  Loading null level.");
							loadNullMap();
						}
					}
				}
			}
			try{
				if (!player.equals(null)){
					if (theMap.currentMap.equals("null")){
						restoreX = player.x;
						restoreY = player.y+player.h;
					}
					player.x = _x;
					player.y = _y-player.h;
					player.dx = 0;
					player.dy = 0;
				}
			} catch(NullPointerException e){
				Console.out("Uh oh, no player was loaded through the quest.");
				//player = new Player1(0, x, y);
				//player = new Bird1(0, x, y);
			}
			focus = player;
			theMap.charData.add(player);
			//mainCharacters.add(player);
			theMap.xCamera = _x-320;
			theMap.yCamera = _y-240;
			theMap.scrollControl(this);
			if (!theMap.currentMap.equals("null")){
				restoreMap = theMap.currentMap;
			}
			theMap.currentMap = nextMap;
		} 
	}
	
	
	
	
	public void addTracker(String _id, String _value){
		boolean _found = false;
		for (int i = 0; i < trackers.size(); i++){
			if (trackers.get(i)[0].equals(_id)){
				trackers.get(i)[1] = _value;
				_found = true;
			}
		}
		if (!_found){
			trackers.add(new String[]{_id,_value});
		}
	}
	public String getTracker(String _id){
		for (int i = 0; i<trackers.size(); i++){
			if (trackers.get(i)[0].equals(_id)){
				return trackers.get(i)[1];
			}
		}
		return "";
	}
	public double getTrackerNumber(String _id){
		for (int i = 0; i<trackers.size(); i++){
			if (trackers.get(i)[0].equals(_id)){
				return Double.parseDouble(trackers.get(i)[1]);
			}
		}
		return 0;
	}
	
	
	

	public void readSaveSettings(int save) {
		int questNumber = -1;
		if (Global.saveEnabled){
			try {
				//Get the relative file path:
				File f = new File(Board.class.getResource("/media").getFile());
				String path = f.getAbsolutePath();

				String[] pathPieces = path.split("/");
				path = "";
				for (int i = 0; i <pathPieces.length-2; i++){
					if (pathPieces[i].equals("file:")){
						break;
					}
					path += pathPieces[i]+"/";
				}
				path+= "save"+save+".txt";
				Console.out(path);

				FileReader reader = new FileReader(path);
				BufferedReader in = new BufferedReader(reader);

				String string;
				boolean _trackers = false;

				while ((string = in.readLine()) != null){//Separates each component in the string as an array
					String[] settings;
					settings = string.split(":");
					if (!_trackers){
						if (settings[0].equals("quest")){
							questNumber = Integer.parseInt(settings[1]);
						} else if (settings[0].equals("map")){
							theMap.nextMap = settings[1];
						} else if (settings[0].equals("entrance")){
							theMap.nextEntrance = settings[1];
						} else if (settings[0].equals("trackers")){
							_trackers = true;
						}
					} else {
						trackers.add(new String[]{settings[0],settings[1]});
					}
				}
			} catch (NullPointerException e) {
				Console.out("Error processing save file "+save+ ".");
			} catch (IOException e) {
				Console.out("Could not find save file "+save+ ".");
			}
		}

		if (Global.quest.equals("hourglass")){
			quest = new Hourglass_Quest(board, this, questNumber);
		} else if (Global.quest.equals("minigame")){
			quest = new Minigame_Quest(board, this, questNumber);
		} else if (Global.quest.equals("unforeseen")){
			quest = new Unforeseen_Quest(board, this, questNumber);
		}
		if (questNumber == -1){
			theMap.nextMap = quest.startingMap;
			theMap.nextEntrance = quest.startingEntrance;
		}
	}
	public void writeSaveSettings(int save, SaveModule location){
		try {
			FileOutputStream out;
			PrintStream p;
			out = new FileOutputStream("save"+save+".txt");
			p = new PrintStream(out); 

			p.println("quest:"+quest.questProgress);
			p.println("map:"+theMap.currentMap);
			p.println("entrance:"+location.id);

			p.println("trackers");

			for (int i = 0; i < trackers.size(); i++){
				p.println(trackers.get(i)[0] + ":" + trackers.get(i)[1]);
			}
			p.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

