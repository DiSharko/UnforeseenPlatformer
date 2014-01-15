package GameEngine;

import java.util.ArrayList;

public class Unforeseen_Quest extends Quest {

	public Unforeseen_Quest(Board _board, Game _game, int _quest){ // require both board and game because on startup, board.game is still undefined.
		startingMap = "town1a_house02";
		startingEntrance = "default";
		timeOfDay = "day";

		if (_quest<0){_quest=0;}
		board = _board;
		game = _game;
		
		if (_quest < 100){
			game.player = new Moglie(board, 0, 0, 0);
		} else if (_quest >= 100){
			game.player = new Adam(board, 0, 0, 0);
		}

		game.healthBar = new Bar(game,5,5,100,20,game.player.healthMax,true);
		game.energyBar = new Bar(game,5,25,100,5,game.player.energyMax,false);
		
		game.player.weapons.add(new BasicAttacks(false,true,false));

		questProgress = _quest;


		/////////////
		// TESTING //
		/////////////
		boolean debug = false;
		if (Global.DEBUG_MODE == 1 || Global.DEBUG_MODE == 2){
			if (debug){
				startingMap = "town1a_house02";
				startingEntrance = "default";
				timeOfDay = "day";
				game.player = new Moglie(board, 0, 0, 0);
			}
		}

	}


	public void checkMapInitScripts(String mapName, Map _theMap){
		try {
			theMap = _theMap;


			/////////////////////////////////////////
			// INITIALIZATION SCRIPTS FOR EACH MAP //
			/////////////////////////////////////////
			
			if (mapName.equals("town1a")){
				for (int i = 0; i<theMap.charData.size(); i++){
					if (theMap.charData.get(i).name.equals("crow")){
						if (Math.random() < 0.3){

							theMap.charData.get(i).delete = true;
						}
					}
				}

			} else if (mapName.equals("town1a_house02")){
				if (questProgress >= 0 && questProgress < 10000){
					theMap.charData.add(lily(288,400,"preset:ambler;"+	
							"c:Why don't you play outside today?:q:0;"
							,null));
					
				}

			} else if (mapName.equals("town1a_house07")){
				theMap.specialData.add(new BreakableBlock(theMap, "brick_column_right", 96,-256,32,32));

				ArrayList<Graphic> _graphics = new ArrayList<Graphic>();
				_graphics.add(new Graphic("brick_column_right",96,-288,false));
				_graphics.add(new Graphic("brick_column_right",96,-224,false));
				_graphics.add(new Graphic("brick",64,-288,false));
				_graphics.add(new Graphic("brick",64,-256,false));
				_graphics.add(new Graphic("brick",64,-224,false));
				_graphics.add(new Graphic("brick",32,-288,false));
				_graphics.add(new Graphic("brick",32,-256,false));
				_graphics.add(new Graphic("brick",32,-224,false));
				_graphics.add(new Graphic("brick",96,-256,false));
				ArrayList<Tile> _tiles = new ArrayList<Tile>();
				_tiles.add(new Tile(96,-256,24,32,false,false,false,false));
				_tiles.add(new Tile(64,-256,32,32,false,false,false,false));
				_tiles.add(new Tile(32,-256,32,32,false,false,false,false));
				theMap.specialData.add(new CoverGraphics(theMap, _graphics, _tiles));
			}
		} catch (NullPointerException e){Console.out("No mapName passed in!");}



		/////////////////////////
		// QUEST-BASED SCRIPTS //
		/////////////////////////

		if (Global.questTriggersEnabled){
			if (questProgress >= 0 && questProgress < 10000){
				if (questProgress == 0){
					
					
					
				}
				
				if (questProgress == 100){
					if (mapName.equals("town1a_house02")){try{
						CharacterTemplate _lily = getCharacter("lily");
						_lily.endCurrentTask();
						createTrackerTrigger("lilyGreet","1",416,224,96,32);
						_lily.addTasksList(new String[]{"type:waitFor;target:lilyGreet:1;amble","type:converse;text:Good morning, Adam!","type:tracker;target:lilyGreet:2","type:waitFor;target:lilyGreet:3","type:converse;text:It's another new week!@p Go out and enjoy the sunshine!@q102@"});
						createTrackerTrigger("lilyGreet","3",352,288,32,128);
						board.game.player.addTasksList(new String[]{"type:waitFor;target:lilyGreet:2","type:converse;text:Hey, sis!"});
						
						
					} catch(Exception e){e.printStackTrace();}}
				} 
				if (mapName.equals("town1a")){
					if (questProgress == 102){
						
						CharacterTemplate _mark = mark(1120,128,"facing:1",null);
						CharacterTemplate _liam = liam(5568,544,"facing:-1",null);
						createTalkBuffer(1120, "Come on, race me!", _mark, "markRace", "");

						double _firstChase = game.getTrackerNumber("firstChase");
						if (_firstChase == 0){	_mark.addTasksList(new String[]{"type:converse;text:@fHEY!! Adam!!","type:mute","type:tracker;target:firstChase:1","type:walkTo;x:1400;y:58;speed:2.7;movement:decelerate"});
						
						} if (_firstChase<=1){	game.player.addTasksList(new String[]{"type:waitFor;target:firstChase:2;","type:face;direction:1"});
													  _mark.addTasksList(new String[]{"type:moveTo;x:1400;y:50","type:converse;text:@fRace you to the library!@w200@","type:tracker;target:firstChase:2","type:walkTo;x:3776;y:690;speed:3.7"});								
						
						} if (_firstChase<=3){	createTrackerTrigger("firstChase","3",2592,-100,32,500); 
												_mark.addTasksList(new String[]{"type:moveTo;x:3776;y:690","type:waitFor;target:firstChase:3","type:converse;text:@fCome on, try to keep up!","type:tracker;target:firstChase:4","type:walkTo;speed:4;x:5504;y:544"});
						
						} if (_firstChase<=4){	_mark.addTasksList(new String[]{"type:moveTo;x:5504;y:544",  "type:waitFor;target:firstChase:5","type:converse;text:@fWhat's up, Liam?;target:liam;","type:tracker;target:talk:liam","type:waitFor;target:talk:mark"});
																		   _liam.addTasksList(new String[]{	 "type:waitFor;target:talk:liam",   "type:converse;text:@fI'm gonna go help some people move in, wanna come?@d40@;target:mark;", "type:tracker;target:talk:mark"});
												createTrackerTrigger("firstChase","5",5376,-100,32,1000);
						} if (_firstChase<=6){
							
						}
												
												
						theMap.charData.add(_mark);
						theMap.charData.add(_liam);
						
						
					}
				}
			}
		}	
	}



	public void advanceQuest(int newQuestProgress){
		if (Global.questTriggersEnabled){
			if (questProgress < newQuestProgress){
				questProgress = newQuestProgress;
				if (questProgress == 15){
				}
			}
		}
	}

















	public CharacterTemplate sydney	(double _x, double _y, String _props, String[] _tasks){return person("sydney", _x,_y,26,30, _props+";nn:Sydney", _tasks);}
	public CharacterTemplate mark	(double _x, double _y, String _props, String[] _tasks){return person("mark",   _x,_y,26,30, _props+";nn:Mark", 	_tasks);}
	public CharacterTemplate liam	(double _x, double _y, String _props, String[] _tasks){return person("liam",   _x,_y,26,30, _props+";nn:Liam", 	_tasks);}
	public CharacterTemplate cody	(double _x, double _y, String _props, String[] _tasks){return person("cody",   _x,_y,20,28, _props+";nn:Cody", 	_tasks);}
	public CharacterTemplate lily	(double _x, double _y, String _props, String[] _tasks){return person("lily",   _x,_y,26,30, _props+";nn:Lily", 	_tasks);}

	public CharacterTemplate person(String _name, double _x, double _y, double _w, double _h, String _props, String[] _tasks){
		CharacterTemplate c = new Character(board, 0, _name,_x,_y,(int) _w,(int) _h, "preset:person;id:"+_name+";"+_props+"");
		if (_tasks != null){c.addTasksList(_tasks);}return c;
	}
}