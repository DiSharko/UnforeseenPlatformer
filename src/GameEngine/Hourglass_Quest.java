package GameEngine;

import java.util.ArrayList;

public class Hourglass_Quest extends Quest {

	public Hourglass_Quest(Board b, Game g, int q){ // require both board and game because on startup, game is still undefined.
		startingMap = "village1a_house6";
		startingEntrance = "start";

		if (q<0){q=0;}
		board = b;
		game = g;

		game.player = new Adam(board, 0, 0, 0);
		Weapon weapon = new BasicAttacks(false,false,false);
		//Weapon weapon = new MachineGun(true,true,true);
		if (questProgress >= 45){
			weapon.a1enabled = true;
		}

		game.player.weapons.add(weapon);

		game.healthBar = new Bar(game,5,5,100,20,game.player.healthMax,true);
		game.energyBar = new Bar(game,5,25,100,5,game.player.energyMax,false);
		//manaBar = new Bar(5,30,50,5,player.manaMax,false);

		if (Global.DEBUG_MODE == 0){
			questProgress = q;
		}

		///////////////////////////////////////////
		///////////TESTING PURPOSES////////////////
		///////////////////////////////////////////
		if (Global.DEBUG_MODE == 1 || Global.DEBUG_MODE == 2){
			startingMap = "village1b_library";
			startingMap = "village1a";
			//startingMap = "crow_killtest";
			startingEntrance = "from_house1";
			questProgress = 30;
			timeOfDay = "night";

			weapon.a1enabled = true;
		}

	}


	public void checkMapInitScripts(String mapName, Map theMap){
		if (Global.questTriggersEnabled){
			try {
				if (mapName.equals("crow_killtest")){
					for (int i = 0; i<650; i++){ // 330 is good (for now)
						theMap.charData.add(new Character(board, 0, "crow", Math.random()*512-48, 306, 16, 14, "preset:crow"));
					}

				} else if (mapName.equals("village1a")){

					//theMap.middleSpecialData.add(new Precipitation(board, theMap, "thunderstorm", 15000, 5, (byte) 1, 35, false));

					// OLD MAN WHO UNLOCKS THE TOWER
					int oldManX = 1126;
					int oldManY = 770;
					String oldManMovement = "ambler";
					if (questProgress == 15){
						oldManX = 1472;
						oldManY = 608;
						oldManMovement = "person";
					}
					theMap.charData.add(new Character(board, 0, "oldMan1",oldManX,oldManY,26,30,"preset:"+oldManMovement+";id:oldMan;facing:-1;" +
							"c:Hum dee dumÉ:q:5;" +
							"c:@fYou kids shouldn't be playing near the tower.@p  Agh, but I remember when we used to play there years back.@p  Well, I guess it couldn't hurt to let you in for a bit.@p  Just promise you won't stray up into the hills over yonder.@q20@:q:15;" +
					"c:Don't tell anyone I let you in to the tower!:q:20"));
					if (questProgress == 20){
						createQuestTrigger(25, 1635,32,32,32);
					}
					if (questProgress >= 20){
						// DOORS ON TOWER
						theMap.specialData.add(new Door(1572,576,24,24, "village1a_tower1", "interact", "from_village1a_bottom"));
						theMap.specialData.add(new Door(1600,32,24,32, "village1a_tower1","interact","from_village1a_top"));
					} else if (questProgress <= 15 && questProgress >=0){
						// LOCK ON TOWER
						ArrayList<Conversation> con = new ArrayList<Conversation>();
						Conversation c = new Conversation("It's locked...@q10@","q:5");
						c.setDisplay(1);
						Conversation c1 = new Conversation("It's locked...","q:10");
						c1.setDisplay(1);
						con.add(c);
						con.add(c1);
						SpecialObject sign = new Sign(1572,576,24,24, con);
						sign.id = "tower_lock";
						theMap.specialData.add(sign);

						// BOYS HANGING AROUND
						int x1 = 896;
						int y1 = 768;
						int x2 = 608;
						int y2 = 768;
						int x3 = 1632;
						int y3 = 608;
						String m = "ambler";

						if (questProgress == 15){
							x1 = 1440;
							y1 = 608;
							x2 = 1504;
							y2 = 576;
							x3 = 1536;
							y3 = 608;
							m = "person";
						}
						String settings = "preset:"+m+";" +
						"c:Hey, let's go explore the tower today!:q:5;" +
						"c:Go get someone to let us in the tower!:q:10;" +
						"c:Hurray!  Let's go exploring!:q:20;";

						theMap.charData.add(boy1(x1,y1,settings+"facing:1",null));
						theMap.charData.add(boy2(x2,y2,settings+"facing:-1",null));
						theMap.charData.add(boy3(x3,y3,settings+"facing:-1",null));
					}
					// FORT LOCKS
					if (questProgress < 1000){
						Sign fortTopLock = new Sign(-732,648,24,24, new Conversation("It's locked...","d:1"));
						fortTopLock.id = "fortTopLock";
						theMap.specialData.add(fortTopLock);
					} else {
						theMap.specialData.add(new Door(-732, 648, 24, 24, "village1a_fort1a", "interact", "from_village1a"));
					}

					if (questProgress < 1000){
						Sign fortBottomLock = new Sign(-448,1328,32,32, new Conversation("It's locked...", "d:1"));
						fortBottomLock.id = "fortBottomLock";
						theMap.specialData.add(fortBottomLock);
					} else {
						theMap.specialData.add(new Door(-448, 1328, 32, 32, "village1a_fort1b", "interact", "from_village1a"));
					}


				} else if (mapName.equals("village1_cave1a")){
					if (questProgress == 30){
						theMap.charData.add(boy1(704,738,"c:How big IS this place?",null));
						theMap.charData.add(boy2(660,738,"c:I can't see very far...",null));
						theMap.charData.add(boy3(608,738,"c:You go first!",null));
						createTalkBuffer(804, "COME BACK!!",  getCharacter("minor_boy3"), "caveBlock",  "");
					}
					if (questProgress < 1000){
						// SIMPLIFYING THE CAVE
						String n = "office_cube";
						theMap.graphicData3.add(new Graphic(n,928,832, false));
						theMap.graphicData3.add(new Graphic(n,1184,864, false));
						theMap.graphicData3.add(new Graphic(n,1184,896, false));
						theMap.graphicData3.add(new Graphic(n,1216,864, false));
						theMap.graphicData3.add(new Graphic(n,1216,896, false));
						theMap.graphicData3.add(new Graphic(n,1216,928, false));
						theMap.graphicData3.add(new Graphic(n,1248,864, false));
						theMap.graphicData3.add(new Graphic(n,1248,896, false));
						theMap.graphicData3.add(new Graphic(n,1248,928, false));
						theMap.graphicData3.add(new Graphic(n,1280,896, false));
						theMap.graphicData3.add(new Graphic(n,1344,896, false));
						theMap.tileData.add(new Tile(928,832,32,32,true,true,true,true));
						theMap.tileData.add(new Tile(1184,864,32,32,true,true,true,true));
						theMap.tileData.add(new Tile(1184,896,32,32,true,true,true,true));
						theMap.tileData.add(new Tile(1216,864,32,32,true,true,true,true));
						theMap.tileData.add(new Tile(1216,896,32,32,true,true,true,true));
						theMap.tileData.add(new Tile(1216,928,32,32,true,true,true,true));
						theMap.tileData.add(new Tile(1248,864,32,32,true,true,true,true));
						theMap.tileData.add(new Tile(1248,896,32,32,true,true,true,true));
						theMap.tileData.add(new Tile(1248,928,32,32,true,true,true,true));
						theMap.tileData.add(new Tile(1280,896,32,32,true,true,true,true));
						theMap.tileData.add(new Tile(1344,896,32,32,true,true,true,true));
					}
				} else if (mapName.equals("village1a_tower1")){
					if (questProgress == 20){
						theMap.charData.add(boy1(128,546,"facing:-1;c:I'll race you to the top!",null));
						theMap.charData.add(boy2(128,450,"facing:-1;c:I'm gonna get to the top first!",null));
						theMap.charData.add(boy3(32,546,"facing:1;c:Woohoo! This place is awesome!",null));
					}
				} else if (mapName.equals("village1b")){
					if (questProgress < 1000){
						theMap.specialData.add(new Buffer(1616,272,16,464,"force:3;disable:1"));
						theMap.tileData.add(new Tile(1618,272,16,464,false,false,true,false));

						theMap.charData.add(new Character(board,0, "oldMan1", 1616, 704,26,30, "preset:person;nn:Old Man;facing:-1;id:old_blocker;c:You shouldn't be playing all the way out here.  Run along!"));

						Conversation c = new Conversation("You shouldn't be playing all the way out here.  Run along!", "a:1");

						Sign s = new Sign(1612,272,16,464, c);
						s.cHost = getCharacter("old_blocker");
						theMap.specialData.add(s);

					}
					if (questProgress == 30){
						theMap.charData.add(boy1(-32,304, "", new String[] {"type:converse;text:We made it out alive!@q35@","type:mute","type:walkTo;x:1184;y:944;speed:2.4","type:converse;text:@fLet's check out this old building!@q40@","type:walkTo;x:1216;y:944;speed:2.3", "type:remove"}));
						theMap.charData.add(boy2(-32,304, "", new String[] {"type:mute","type:walkTo;x:1216;y:944;speed:2.3","type:remove"}));
						theMap.charData.add(boy3(-32,304, "", new String[] {"type:mute","type:walkTo;x:1216;y:944;speed:2.2","type:remove"}));


						//CharacterTemplate moglie = new Character(board, 1, "moglie",200,304,26,30, "preset:ambler;c:Hi there!");
						//theMap.charData.add(moglie);

					} else if (questProgress == 35){
						theMap.charData.add(boy1(1184,944,"",new String[] {"type:converse;text:@fLet's check out this old building!@q40@","type:walkTo;x:1216;y:944;speed:2.4","type:remove"}));
						theMap.charData.add(boy2(1170,944,"",new String[] {"type:walkTo;x:1216;y:944;speed:2.3","type:remove"}));
						theMap.charData.add(boy3(1160,944,"",new String[] {"type:walkTo;x:1216;y:944;speed:2.2","type:remove"}));
					}
				} else if (mapName.equals("village1b_library")){
					if (questProgress >= 40 && questProgress < 55){
						theMap.charData.add(boy1(96,960,
								"c:Look what this book says!@p 'To kill dust monsters, press '"+Global.attack1Key+"' to shoot fireballs'.@p What the heck is a 'd'?@q45@:q:40;" +
								"c:If I find anything else cool, I'll let you know!:q:45;" +
								"c:Heh, heh, heh...@p Check these comics out! @q55@:q:50", null));
						theMap.charData.add(boy2(512,256,"c:Ahh! Dust monsters!:q:40", null));
						theMap.charData.add(boy3(645,672,"c:I'll tell you if I find anything cool!:q:40;" +
								"c:Check this out! @q50@:q:45;"+
								"c:Looking through all these books is booooooring!:q:50;" +
								"c:Can we go now? I'm bored.:q:55", null));

						if (questProgress == 40){
							getCharacter("minor_boy1").addTasksList(new String[] {"type:converse;text:@fHey Andrew!  Come check this out!"});
						} else if (questProgress == 45){
							getCharacter("minor_boy3").addTasksList(new String[] {"type:converse;text:@fHey!! I found something!"});
						} else if (questProgress == 50){
							getCharacter("minor_boy1").addTasksList(new String[] {"type:converse;text:@fCome check this out!"});
						}

					}
				}
			} catch (NullPointerException e){
				Console.out("No mapName passed in!");
			}
		}
	}

	public void advanceQuest(int newQuestProgress){
		if (Global.questTriggersEnabled){
			if (questProgress < newQuestProgress){
				questProgress = newQuestProgress;
				if (questProgress == 15){
					// GROUPING KIDS AROUND OLD MAN
					for (int i = 0; i<game.theMap.charData.size(); i++){
						CharacterTemplate c = game.theMap.charData.get(i);
						if (c.id.equals("minor_boy1")){
							c.x = 1440;
							c.y = 608;
							c.endCurrentTask();
							c.facing = 1;
						} else if (c.id.equals("minor_boy2")){
							c.x = 1504;
							c.y = 576;
							c.endCurrentTask();
							c.facing = -1;
						} else if (c.id.equals("minor_boy3")){
							c.x = 1536;
							c.y = 608;
							c.endCurrentTask();
							c.facing = -1;
						} else if (c.id.equals("oldMan")){
							c.x = 1472;
							c.y = 608;
							c.endCurrentTask();
							c.facing = -1;
						}
					}
					createQuestTrigger(17, -50000, -50000, 100000, 100000);

				} else if (questProgress == 17){
					getCharacter("oldMan").aiConversations.get(1).useConversation(board, getCharacter("oldMan"));

				} else if (questProgress == 20){
					// UNLOCKING TOWER
					removeObject("tower_lock");
					game.theMap.specialData.add(new Door(1572,576,24,24, "village1a_tower1", "interact", "from_village1a_bottom"));

				} else if (questProgress == 25){
					game.theMap.charData.add(boy1(2176,354,"",new String[] {"type:converse;text:@fHey! Over here!  You gotta come see this!@q27@"}));
					game.theMap.charData.add(boy2(2304,322,"", null));
					game.theMap.charData.add(boy3(2208,354,"", null));
					deleteQuestTrigger(25);
					createQuestTrigger(30,1760,-1024,32,2048);

				} else if (questProgress == 30){
					removeCharacter("minor_boy1");
					removeCharacter("minor_boy2");
					removeCharacter("minor_boy3");
					deleteQuestTrigger(30);

				} else if (questProgress == 45){
					getMainCharacterWeapon("Attacks").a1enabled = true;
					getCharacter("minor_boy3").addTasksList(new String[] {"type:converse;text:@fHey!! I found something!"});
				} else if (questProgress == 50){
					getCharacter("minor_boy1").addTasksList(new String[] {"type:converse;text:@fCome check this out!"});
				} else if (questProgress == 55){
					createQuestTrigger(60,-400,896,5000,32);
					createQuestTrigger(60,192,0,32,5000);
				} else if (questProgress == 60){
					deleteQuestTrigger(60);
					getCharacter("minor_boy2").addTasksList(new String[] {"type:converse;text:@fIt's getting dark!  I gotta be home soon!"});
				}
			} else {
				Console.out("TOLD TO ADVANCE QUEST TO "+newQuestProgress+", BUT QUEST IS ALREADY AT "+questProgress+"!");
			}
		}
	}


	public CharacterTemplate boy1(double x, double y, String props, String[] tasks){
		CharacterTemplate c = new Character(board, 0, "minor_boy1",x,y,26,30, "preset:person;nn:Boy1;id:minor_boy1;"+props+"");
		if (tasks != null){
			c.addTasksList(tasks);
		}
		return c;
	}
	public CharacterTemplate boy2(double x, double y, String props, String[] tasks){
		CharacterTemplate c = new Character(board, 0, "minor_boy2",x,y,26,30, "preset:person;nn:Boy2;id:minor_boy2;"+props+"");
		if (tasks != null){
			c.addTasksList(tasks);
		}
		return c;
	}
	public CharacterTemplate boy3(double x, double y, String props, String[] tasks){
		CharacterTemplate c = new Character(board, 0, "minor_boy3",x,y,26,30, "preset:person;nn:Boy3;id:minor_boy3;"+props+"");
		if (tasks != null){
			c.addTasksList(tasks);
		}
		return c;
	}













}