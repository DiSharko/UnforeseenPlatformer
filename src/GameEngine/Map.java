package GameEngine;

import java.util.ArrayList;
import java.awt.Color;
import java.awt.Image;
import java.awt.Polygon;
import java.io.*;

import javax.swing.ImageIcon;

public class Map {
	int reload = -1;
	boolean loadSuccess = false;

	Board board;

	Color bgColor = Color.BLACK;
	ArrayList<String> songs = new ArrayList<String>();
	int bossCharacterTestMode = 1;

	String currentMap;
	String currentEntrance;
	String nextMap;
	String nextEntrance;
	int areaW;
	int areaH;

	int xLeft = 0;
	int xRight = 640;
	int yTop = 0;
	int yBottom = 480;
	boolean reducedVision = false;

	int leftXbound = 1337;
	int rightXbound = -1337;
	int topYbound = 1337;
	int bottomYbound = -1337;

	ArrayList<ArrayList<String>> entrances = new ArrayList<ArrayList<String>>();
	//ArrayList <ArrayList<ArrayList<Integer>>> tileDataIndex = new ArrayList<ArrayList<ArrayList<Integer>>>();
	ArrayList <Tile> tileData = new ArrayList<Tile>();

	ArrayList <Graphic> graphicData1 = new ArrayList<Graphic>();
	ArrayList <Graphic> graphicData2 = new ArrayList<Graphic>();

	ArrayList <CharacterTemplate> charData  = new ArrayList<CharacterTemplate>();

	ArrayList <Graphic> graphicData3 = new ArrayList<Graphic>();
	ArrayList <Graphic> graphicData4 = new ArrayList<Graphic>();

	ArrayList <SpecialObject> specialData = new ArrayList<SpecialObject>(); //used for overlays
	ArrayList <Generic> scrollBounds = new ArrayList<Generic>();

	ArrayList <Image> overlays = new ArrayList<Image>();
	ArrayList <Polygon> lightSources = new ArrayList<Polygon>();
	//ArrayList<Dimension> tileAreas;

	double xCamera;
	double yCamera;
	double dxCamera = 0;
	double dyCamera = 0;


	public Map(Board _board, String _levelName){
		board = _board;
		try{
			currentMap = _levelName;
			if (!equals(null)){
				areaW = 640;
				areaH = 480;
				xCamera = 0;
				yCamera = 0;

				//tileAreas = new ArrayList<Dimension>();
				if (Global.EXTERNAL){
					loadExternalLevel();
				} else {
					loadLevel(_levelName);
				}
			}
		}catch(NullPointerException e){}
	}



	public void loadExternalLevel(){
		loadLevel("EXTERNAL");

		if (leftXbound == 1337 && rightXbound == -1337){
			leftXbound = -133700;
			rightXbound = 133700;
			topYbound = -133700;
			bottomYbound = 133700;
		}
	}

	public void loadLevel(String _levelName){
		try{
			currentMap = _levelName;

			BufferedReader _in = null;
			if (!_levelName.equals("EXTERNAL")){
				InputStream _file = this.getClass().getResourceAsStream("/media/levels/"+Global.quest+"/"+_levelName+".txt");
				InputStreamReader _reader = new InputStreamReader(_file);
				_in = new BufferedReader(_reader);
			} else {
				File f = new File(Board.class.getResource("/media").getFile());
				String path = f.getAbsolutePath();
				String[] pathPieces = path.split("/");
				path = "";
				for (int i = 0; i <pathPieces.length-2; i++){
					if (pathPieces[i].equals("file:")){break;}
					path += pathPieces[i]+"/";
				}
				path += "level.txt";
				FileReader reader = new FileReader(path);
				_in = new BufferedReader(reader);
			}

			String _stage = "none";

			String _string;
			while ((_string = _in.readLine()) != null){
				String[] _properties;
				_properties = _string.split(";");
				if (_properties[0].equals("---STARTING NEW SECTION")){
					if (_properties[1].equals("SETTINGS")){_stage = "SETTINGS";
					} else if (_properties[1].equals("ENTRANCES")){_stage = "ENTRANCES";
					} else if (_properties[1].equals("CHARACTERS")){_stage = "CHARACTERS";
					} else if (_properties[1].equals("SPECIALS")){_stage = "SPECIALS";
					} else if (_properties[1].equals("TILES")){_stage = "TILES";
					} else if (_properties[1].equals("GRAPHICS1")){_stage = "GRAPHICS1";
					} else if (_properties[1].equals("GRAPHICS1")){_stage = "GRAPHICS1";
					} else if (_properties[1].equals("GRAPHICS2")){_stage = "GRAPHICS2";
					} else if (_properties[1].equals("GRAPHICS3")){_stage = "GRAPHICS3";
					} else if (_properties[1].equals("GRAPHICS4")){_stage = "GRAPHICS4";}
				} else {

					if (_stage.equals("SETTINGS")){
						String[] _settings = _string.split(":");
						if (_settings[0].equals("overlay")){
							overlays.add(new ImageIcon(Board.class.getResource("/media/images/overlays/"+ _settings[1] +".png")).getImage());
						}
						if (_settings[0].equals("bgColor") || _settings[0].equals("background")){
							if (_settings[1].equals("BLUE")){
								bgColor = COLOR.blue;
							} else if (_settings[1].equals("BLACK")){
								bgColor = COLOR.black;
							} else if (_settings[1].equals("WHITE")){
								bgColor = COLOR.white;
							} else if (_settings[1].equals("RED")){
								bgColor = COLOR.red;
							} else if (_settings[1].equals("YELLOW")){
								bgColor = COLOR.yellow;
							} else if (_settings[1].equals("GREEN")){
								bgColor = COLOR.green;
							} else if (_settings[1].equals("DAY")){
								bgColor = COLOR.day;
							}
						}
						if (_settings[0].equals("song")){
							songs.add(_settings[1]);
						}
						if (_settings[0].equals("followTime")){
							specialData.add(new MapTask(board, this, "type:followTime"));
						}
						if (_settings[0].equals("reducedVision")){
							specialData.add(new MapTask(board, this, "type:reducedVision;trackDepth;speed:3;y:770"));
						}
						if (_settings[0].equals("caveVision")){
							specialData.add(new MapTask(board, this, "type:reducedVision;size:"+Double.parseDouble(_settings[1])));
						}


					} else if (_stage.equals("ENTRANCES")){
						int num = 0;
						try{
							entrances.add(num, new ArrayList<String>());
							entrances.get(num).add(_properties[0]);
							entrances.get(num).add(_properties[1]);
							entrances.get(num).add(_properties[2]);
							num++;
						} catch (IndexOutOfBoundsException e){
							Console.out("Corrupted map entrance data");
						}

					} else if (_stage.equals("CHARACTERS")){
						try {
							int team = Integer.parseInt(_properties[0]);
							String name = _properties[1];
							int x = Integer.parseInt(_properties[2]);
							int y = Integer.parseInt(_properties[3]);
							int	w = Integer.parseInt(_properties[4]);
							int	h = Integer.parseInt(_properties[5]);
							Character character = new Character(board, team, name, x, y, w, h, "");
							boolean exit = false;
							for (int i = 6; !exit; i++){try{character.addSettings(_properties[i]);} catch (Exception e){exit = true;}}
							charData.add(character);
						} catch (Exception e) {Console.out("Error creating a new character.");}

					} else if (_stage.equals("SPECIALS")){
						String className = null;
						className = _properties[0];

						if (className.equals("door")){
							try {
								// name, x, y, w, h, newLocation, activation type (interact), entrance to come in on
								double x = Integer.parseInt(_properties[1]);
								double y = Integer.parseInt(_properties[2]);
								int w = Integer.parseInt(_properties[3]);
								int h = Integer.parseInt(_properties[4]);

								String transLocation = _properties[5];
								String activate = _properties[6];
								String entrance = _properties[7];
								specialData.add(new Door(x, y, w, h, transLocation, activate, entrance));
							} catch (Exception e) {Console.out("SOMETHING WENT HORRIBLY WRONG MAKING A NEW DOOR!!!");}

						} else if (className.equals("hurtObject")){
							try {
								// name, x, y, w, h, damage
								double x = Integer.parseInt(_properties[1]);
								double y = Integer.parseInt(_properties[2]);
								int w = Integer.parseInt(_properties[3]);
								int h = Integer.parseInt(_properties[4]);
								double damage = Double.parseDouble(_properties[5]);
								specialData.add(new HurtObject(x, y, w, h,damage));
							} catch (Exception e) {Console.out("SOMETHING WENT HORRIBLY WRONG MAKING A NEW HURTOBJECT!!!");}

						} else if (className.equals("slope")){
							try {
								// name, x, y, w, h, direction
								double x = Integer.parseInt(_properties[1]);
								double y = Integer.parseInt(_properties[2]);
								int w = Integer.parseInt(_properties[3]);
								int h = Integer.parseInt(_properties[4]);
								String direction = _properties[5];
								specialData.add(new Slope(x, y, w, h, direction));
							} catch (Exception e) {Console.out("SOMETHING WENT HORRIBLY WRONG MAKING A NEW SLOPE!!!");}

						} else if (className.equals("crumbleBlock")){
							try {
								// name, x, y, w, h, floor, ceiling, left, right, time
								specialData.add(new CrumbleBlock(_string));
							} catch (Exception e) {Console.out("SOMETHING WENT HORRIBLY WRONG MAKING A NEW CRUMBLEBLOCK!!!");}

						} else if (className.equals("animation")){
							try {
								int layer = 1;
								double x = Integer.parseInt(_properties[1]);
								double y = Integer.parseInt(_properties[2]);
								int w = Integer.parseInt(_properties[3]);
								int h = Integer.parseInt(_properties[4]);
								String name = _properties[5];
								String addSettings = "";
								boolean exit = false;
								for (int i = 6; !exit; i++){
									String[] settings;
									try{
										settings = _properties[i].split(":");
										if (settings[0].equals("L")){
											layer = Integer.parseInt(settings[1]);
										}
										addSettings+=_properties[i];
									} catch (Exception e){
										exit = true;
									}
								}
								Animation anim = new Animation(name,x, y, w, h, addSettings);

								if (layer == 1){specialData.add(anim);
								} else if (layer == -1){specialData.add(anim);
								} else if (layer == 2){specialData.add(anim);
								} else {Console.out("Layer \""+layer+"\" is not an acceptable animation layer.");}
							} catch (Exception e) {Console.out("SOMETHING WENT HORRIBLY WRONG MAKING A NEW ANIMATION!!!");}

						} else if (className.equals("buffer")){
							try {
								// name, x, y, w, h, floor, ceiling, left, right, force, disable time
								double x = Double.parseDouble(_properties[1]);
								double y = Double.parseDouble(_properties[2]);
								int w = Integer.parseInt(_properties[3]);
								int h = Integer.parseInt(_properties[4]);
								specialData.add(new Buffer(x, y, w, h,_string));
							} catch (Exception e) {Console.out("SOMETHING WENT HORRIBLY WRONG MAKING A NEW BUFFER!!!");}

						} else if (className.equals("downFloor")){
							try {
								// name, x, y, w, h
								double x = Double.parseDouble(_properties[1]);
								double y = Double.parseDouble(_properties[2]);
								int w = Integer.parseInt(_properties[3]);
								int h = Integer.parseInt(_properties[4]);
								specialData.add(new DownFloor(x, y, w, h));
							} catch (Exception e){Console.out("SOMETHING WENT HORRIBLY WRONG MAKING A NEW DOWNFLOOR!!!");}

						} else if (className.equals("sign")){
							try {
								// name, x, y, w, h, c:Conversation,
								double x = Integer.parseInt(_properties[1]);
								double y = Integer.parseInt(_properties[2]);
								int w = Integer.parseInt(_properties[3]);
								int h = Integer.parseInt(_properties[4]);
								String cHostID = "";
								ArrayList<Conversation> convos = new ArrayList<Conversation>();
								boolean exit = false;
								for (int i = 5; !exit; i++){
									String[] settings;
									try{
										settings = _properties[i].split(":");
										if (settings[0].equals("c")){ //settings[1] is the quest number
											Conversation c = new Conversation(settings[1], "");
											boolean exit2 = false;
											try{
												for (int j = 2; !exit2; j+=2){
													if (settings[j].equals("a")){
														c.setActivation(Integer.parseInt(settings[j+1]));
													} else if (settings[j].equals("d")){
														c.setDisplay(Integer.parseInt(settings[j+1]));
													} else if (settings[j].equals("q")){
														c.setQuest(Integer.parseInt(settings[j+1]));
													}
												}
											} catch (IndexOutOfBoundsException e){
												exit2 = true;
											}
											convos.add(c);
										} else if (settings[0].equals("cHostID")){
											cHostID = settings[1];
										}
									} catch (Exception e){
										exit = true;
									}
								}
								Sign s = new Sign(x, y, w, h, convos);

								if (!cHostID.equals("")){
									s.cHostID = cHostID;
								}
								specialData.add(s);

							} catch (Exception e) {Console.out("SOMETHING WENT HORRIBLY WRONG MAKING A NEW SIGN!!!");}

						} else if (className.equals("saveModule")){
							try {
								double x = Integer.parseInt(_properties[1]);
								double y = Integer.parseInt(_properties[2]);
								int w = Integer.parseInt(_properties[3]);
								int h = Integer.parseInt(_properties[4]);
								String id = _properties[5];
								specialData.add(new SaveModule(x, y, w, h, id));
							} catch (Exception e) {Console.out("SOMETHING WENT HORRIBLY WRONG MAKING A NEW SAVE MODULE!!!");}	

						} else if (className.equals("timedObject")){
							try {
								double x = Integer.parseInt(_properties[1]);
								double y = Integer.parseInt(_properties[2]);
								int w = Integer.parseInt(_properties[3]);
								int h = Integer.parseInt(_properties[4]);
								String type = _properties[5];
								String destination = _properties[6];
								specialData.add(new TimedObject(board, this, x, y, w, h, type, destination));
							} catch (Exception e) {Console.out("SOMETHING WENT HORRIBLY WRONG MAKING A NEW TIMED OBJECT!!!");}

						} else if (className.equals("warp")){
							try {
								double x = Integer.parseInt(_properties[1]);
								double y = Integer.parseInt(_properties[2]);
								int w = Integer.parseInt(_properties[3]);
								int h = Integer.parseInt(_properties[4]);

								specialData.add(new Warp(x, y, w, h, _string));
							} catch (Exception e) {Console.out("SOMETHING WENT HORRIBLY WRONG MAKING A NEW WARP!!!");}

						} else if (className.equals("graphicBlock")){
							try {
								double x = Integer.parseInt(_properties[1]);
								double y = Integer.parseInt(_properties[2]);
								int w = Integer.parseInt(_properties[3]);
								int h = Integer.parseInt(_properties[4]);
								String type = _properties[5];
								int layer = Integer.parseInt(_properties[6]);
								specialData.add(new GraphicBlock(this, layer, type, x, y, w, h));
							} catch (Exception e) {Console.out("SOMETHING WENT HORRIBLY WRONG MAKING A NEW GRAPHIC BLOCK!!!");}

						} else if (className.equals("movingPlatform")){
							try{
								String[] _settings = _string.split(";");
								String _newSettings = "";
								for (int i = 5; i < _settings.length; i++){
									_newSettings += _settings[i]+";";
								}
								specialData.add(new MovingPlatform(board, this, _newSettings));
							} catch (Exception e) {Console.out("SOMETHING WENT HORRIBLY WRONG MAKING A NEW MOVING PLATFORM!!!");}	

						} else if (className.equals("scrollBound")){
							try {
								double x = Integer.parseInt(_properties[1]);
								double y = Integer.parseInt(_properties[2]);
								int w = Integer.parseInt(_properties[3]);
								int h = Integer.parseInt(_properties[4]);

								scrollBounds.add(new Generic(this, "scrollBound",x, y, w, h, _string));
							} catch (Exception e) {Console.out("SOMETHING WENT HORRIBLY WRONG MAKING A NEW SCROLLBOUND!!!");}

						} else {
							try {
								double x = Double.parseDouble(_properties[1]);
								double y = Double.parseDouble(_properties[2]);
								int w = Integer.parseInt(_properties[3]);
								int h = Integer.parseInt(_properties[4]);

								String settings = "";
								for (int i = 5; i<_properties.length; i++){
									settings += _properties[i]+";";
								}

								specialData.add(new Generic(this, className, x, y, w, h, settings));
							} catch(Exception e){Console.out("SOMETHING WENT HORRIBLY WRONG MAKING \""+className+"\"!!!");}
						}

					} else if (_stage.equals("TILES")){
						try {
							int x = Integer.parseInt(_properties[0]);
							int y = Integer.parseInt(_properties[1]);
							int w = Integer.parseInt(_properties[2]);
							int h = Integer.parseInt(_properties[3]);
							boolean f = Boolean.valueOf(_properties[4]);
							boolean c = Boolean.valueOf(_properties[5]);
							boolean l = Boolean.valueOf(_properties[6]);
							boolean r = Boolean.valueOf(_properties[7]);
							tileData.add(new Tile(x, y, w, h, f, c, l, r));
						} catch (Exception e) {Console.out("Error creating a new tile.");}

					} else if (_stage.equals("GRAPHICS1") || _stage.equals("GRAPHICS2") || _stage.equals("GRAPHICS3") || _stage.equals("GRAPHICS4")){
						try {
							String graphicName = _properties[0];
							if (graphicName.equals("GRAPHICBLOCK")){
								int x = Integer.parseInt(_properties[1]);
								int y = Integer.parseInt(_properties[2]);
								int _totalWidth = Integer.parseInt(_properties[3]);
								int _totalHeight = Integer.parseInt(_properties[4]);
								String _individualName = _properties[5];
								int _individualWidth = Integer.parseInt(_properties[6]);
								int _individualHeight = Integer.parseInt(_properties[7]);
								for (int i = 0; i<_totalWidth/_individualWidth; i++){
									for (int j = 0; j<_totalHeight/_individualHeight; j++){
										if (_stage.equals("GRAPHICS1")){graphicData1.add(new Graphic(_individualName,x+i*_individualWidth,y+j*_individualHeight, false));
										} else if (_stage.equals("GRAPHICS2")){graphicData2.add(new Graphic(_individualName,x+i*_individualWidth,y+j*_individualHeight, false));
										} else if (_stage.equals("GRAPHICS3")){graphicData3.add(new Graphic(_individualName,x+i*_individualWidth,y+j*_individualHeight, false));
										} else if (_stage.equals("GRAPHICS4")){graphicData4.add(new Graphic(_individualName,x+i*_individualWidth,y+j*_individualHeight, false));}
									}
								}
							} else {
								int x = Integer.parseInt(_properties[1]);
								int y = Integer.parseInt(_properties[2]);
								if (graphicName.equals("null")){
									if (x<leftXbound){leftXbound = x;}
									if (x+32>rightXbound){rightXbound = x+32;}
									if (y<topYbound){topYbound = y;}
									if (y+32>bottomYbound){bottomYbound = y+32;}
								} else {
									if (_stage.equals("GRAPHICS1")){graphicData1.add(new Graphic(graphicName, x, y, false));
									} else if (_stage.equals("GRAPHICS2")){graphicData2.add(new Graphic(graphicName, x, y, false));
									} else if (_stage.equals("GRAPHICS3")){graphicData3.add(new Graphic(graphicName, x, y, false));
									} else if (_stage.equals("GRAPHICS4")){graphicData4.add(new Graphic(graphicName, x, y, false));}
								}
							}

						} catch (Exception e) {Console.out("Error creating a new graphic in layer 1.");}
					}
				}
			}
			if (leftXbound == 1337 && rightXbound == -1337){
				leftXbound = -133700;
				rightXbound = 133700;
				topYbound = -133700;
				bottomYbound = 133700;
			}
			loadSuccess = true;
			_in.close();
		} catch (IOException e){Console.out("**Error reading the specified level \""+_levelName+"\"**");
		} catch (NullPointerException e){Console.out("**Could not find the specified level \""+_levelName+"\"**");}
	}






















	public void scrollControl(Game _game){
		double normalX = _game.focus.x - xCamera;
		double normalY = _game.focus.y - yCamera;

		if (normalX > 384){
			dxCamera = (normalX-384);
		} else if (normalX<256){
			dxCamera = (normalX-256);
		} else {
			dxCamera*=.9;
		}
		if (normalY > 288){
			dyCamera = (normalY-288);
		} else if (normalY < 192){
			dyCamera = (normalY-192);
		} else {
			dyCamera*=.9;
		}

		int maxSpeed = 15;
		double modifier = 1;
		if (Math.abs(dxCamera)>maxSpeed){
			modifier = Math.abs(dxCamera/maxSpeed);
			dxCamera = maxSpeed*Math.signum(dxCamera);

		}
		if (modifier != 1){
			dyCamera /= modifier;
			modifier = 1;
		}
		if (Math.abs(dyCamera)>maxSpeed){
			modifier = Math.abs(dyCamera/maxSpeed);
			dyCamera = maxSpeed*Math.signum(dyCamera);
		}
		if (modifier != 1){
			dxCamera /= modifier;
			modifier = 1;
		}

		applyScrolling();
	}

	public void applyScrolling(){

		xCamera += dxCamera;
		yCamera += dyCamera;

		if (scrollBounds.size()>0){
			Generic xClosest = null;
			double xDistance = 1000000000;
			Generic yClosest = null;
			double yDistance = 1000000000;

			for (int i = 0; i<scrollBounds.size(); i++){
				Generic current = scrollBounds.get(i);

				if (current.y-yCamera>=0 && current.y+current.h-yCamera<=480 && current.x-xCamera>=0 && current.x+current.w-xCamera <= 640){
					if (current.string1.equals("left")){
						if (Math.abs(current.x-xCamera)<xDistance){
							xDistance = Math.abs(current.x-xCamera);
							xClosest = current;
						}
					} else if (current.string1.equals("right")){
						if (Math.abs(current.x+current.w-xCamera-640) <xDistance){
							xDistance = Math.abs(current.x+current.w-xCamera-640);
							xClosest = current;
						}
					}

					if (current.string1.equals("top")){
						if (Math.abs(current.y-yCamera) < yDistance){
							yDistance = Math.abs(current.y-yCamera);
							yClosest = current;
						}
					} else if (current.string1.equals("bottom")){
						if (Math.abs(current.y+current.h-yCamera-480)<yDistance){
							yDistance = Math.abs(current.y+current.h-yCamera-480);
							yClosest = current;
						}
					}
				}
			}
			
			if (xClosest != null){
				if (xClosest.string1.equals("left")){
					xCamera = xClosest.x;
				} else if (xClosest.string1.equals("right")){
					xCamera = xClosest.x+xClosest.w-640;
				}
			}

			if (yClosest != null){
				if (yClosest.string1.equals("top")){
					yCamera = yClosest.y;
				} else if (yClosest.string1.equals("bottom")){
					yCamera = yClosest.y+yClosest.h-480;
				}
			}
		}

		if (xCamera <= leftXbound){
			xCamera = leftXbound;
		}
		if (xCamera+640 >= rightXbound){
			xCamera = rightXbound-640;
		}
		if (yCamera <= topYbound){
			yCamera = topYbound;
		}
		if (yCamera+480>=bottomYbound){
			yCamera = bottomYbound-480;
		}

	}




}






































//	////////////////////////////////////////////
//	// OLD // OLD // OLD // OLD // OLD // OLD //
//	////////////////////////////////////////////
//	// OLD // OLD // OLD // OLD // OLD // OLD //
//	////////////////////////////////////////////
//	// OLD // OLD // OLD // OLD // OLD // OLD //
//	////////////////////////////////////////////
//
//
//
//	public void oldLoadLevel(String mapName){
//		currentMap = mapName;
//
//		String tileFile = "/media/levels/"+Global.quest+"/"+mapName+"/tileData.txt";
//		String charFile = "/media/levels/"+Global.quest+"/"+mapName+"/charData.txt";
//		String specialFile = "/media/levels/"+Global.quest+"/"+mapName+"/specialData.txt";
//		String entranceFile = "/media/levels/"+Global.quest+"/"+mapName+"/entranceData.txt";
//		String settingsFile = "/media/levels/"+Global.quest+"/"+mapName+"/settings.txt";
//
//		String graphicFile1 = "/media/levels/"+Global.quest+"/"+mapName+"/graphicData1.txt";
//		String graphicFile2 = "/media/levels/"+Global.quest+"/"+mapName+"/graphicData2.txt";
//		String graphicFile3 = "/media/levels/"+Global.quest+"/"+mapName+"/graphicData3.txt";
//		String graphicFile4 = "/media/levels/"+Global.quest+"/"+mapName+"/graphicData4.txt";
//
//		readGraphics(this, graphicFile1, 1);
//		readGraphics(this, graphicFile2, 2);
//		readGraphics(this, graphicFile3, 3);
//		readGraphics(this, graphicFile4, 4);
//		readTiles(this, tileFile);
//		readCharacters(this, charFile);
//		readSpecialData(this,specialFile);
//		readEntranceData(this, entranceFile);
//		readSettings(this, settingsFile);
//	}
//
//
//
//	public void loadExternalMap(){
//		currentMap = "test";
//
//		File f = new File(Board.class.getResource("/media").getFile());
//		String path = f.getAbsolutePath();
//
//		String[] pathPieces = path.split("/");
//		path = "";
//		for (int i = 0; i <pathPieces.length-2; i++){
//			if (pathPieces[i].equals("file:")){
//				break;
//			}
//			path += pathPieces[i]+"/";
//		}
//		path += "DATA/";
//
//		for (int i = 1; i <=4; i++){
//			readGraphics(this, ""+path+"graphicData"+i+".txt", i);
//		}
//
//		String tileFile = "" + path + "tileData.txt";
//		String charFile = "" + path + "charData.txt";
//		String specialFile = "" + path + "specialData.txt";
//		String entranceFile = "" + path + "entranceData.txt";
//		String settingsFile = "" + path + "settings.txt";
//
//		String graphicFile1 = "" + path + "graphicData1.txt";
//		String graphicFile2 = "" + path + "graphicData2.txt";
//		String graphicFile3 = "" + path + "graphicData3.txt";
//		String graphicFile4 = "" + path + "graphicData4.txt";
//
//		readGraphics(this, graphicFile1, 1);
//		readGraphics(this, graphicFile2, 2);
//		readGraphics(this, graphicFile3, 3);
//		readGraphics(this, graphicFile4, 4);
//		if (leftXbound == 1337 && rightXbound == -1337){
//			leftXbound = -133700;
//			rightXbound = 133700;
//			topYbound = -133700;
//			bottomYbound = 133700;
//		}
//		readTiles(this, tileFile);
//		readCharacters(this, charFile);
//		readSpecialData(this,specialFile);
//		readEntranceData(this, entranceFile);
//		readSettings(this, settingsFile);
//	}
//
//
//
//
//
//
//
//	public void readSettings(Map map, String fileName){
//		try{
//			BufferedReader in;
//			try{
//				InputStream file = getClass().getResourceAsStream(fileName);
//				InputStreamReader reader = new InputStreamReader(file);
//				in = new BufferedReader(reader);
//			} catch (Exception e){
//				FileReader reader = new FileReader(fileName);
//				in = new BufferedReader(reader);
//			}
//			String string;
//			while ((string = in.readLine()) != null){//Separates each component in the string as an array
//				String[] settings;
//				settings = string.split(":");
//				if (settings[0].equals("overlay")){
//					overlays.add(new ImageIcon(Board.class.getResource("/media/images/overlays/"+ settings[1] +".png")).getImage());
//				}
//				if (settings[0].equals("bgColor") || settings[0].equals("background")){
//					if (settings[1].equals("BLUE")){
//						bgColor = COLOR.blue;
//					} else if (settings[1].equals("BLACK")){
//						bgColor = COLOR.black;
//					} else if (settings[1].equals("RED")){
//						bgColor = COLOR.red;
//					} else if (settings[1].equals("YELLOW")){
//						bgColor = COLOR.yellow;
//					} else if (settings[1].equals("GREEN")){
//						bgColor = COLOR.green;
//					} else if (settings[1].equals("DAY")){
//						bgColor = COLOR.day;
//					}
//				}
//				if (settings[0].equals("song")){
//					songs.add(settings[1]);
//				}
//				if (settings[0].equals("followTime")){
//					specialData.add(new MapTask(board, this, "type:followTime"));
//				}
//				if (settings[0].equals("reducedVision")){
//					specialData.add(new MapTask(board, this, "type:reducedVision;trackDepth;speed:3;y:770"));
//				}
//				if (settings[0].equals("caveVision")){
//					specialData.add(new MapTask(board, this, "type:reducedVision;size:"+Double.parseDouble(settings[1])));
//				}
//			}
//		} catch (NullPointerException e){
//			Console.out("This level doesn't have a settings file.");
//		} catch (IOException e) {
//			Console.out("SETTINGS CORRUPTION!!!");
//		}
//	}
//	public void readGraphics(Map map, String fileName, int num){
//		try{
//			BufferedReader in;
//			try{
//				InputStream file = getClass().getResourceAsStream(fileName);
//				InputStreamReader reader = new InputStreamReader(file);
//				in = new BufferedReader(reader);
//			} catch (Exception e){
//				FileReader reader = new FileReader(fileName);
//				in = new BufferedReader(reader);
//			}
//			String string;
//			while ((string = in.readLine()) != null){//Separates each component in the string as an array
//				String[] blockProperties;
//				blockProperties = string.split(";");
//				String graphicName = null;
//				try {
//					graphicName = blockProperties[0];
//					int x = Integer.parseInt(blockProperties[1]);
//					int y = Integer.parseInt(blockProperties[2]);
//					//int w = Integer.parseInt(blockProperties[3]);
//					//int h = Integer.parseInt(blockProperties[4]);
//
//					try{
//						if (num == 1){
//							this.graphicData1.add(new Graphic(graphicName, x, y, false));
//						} else if (num == 2){
//							this.graphicData2.add(new Graphic(graphicName, x, y, false));
//						} else if (num == 3){
//							this.graphicData3.add(new Graphic(graphicName, x, y, false));
//						} else if (num == 4){
//							this.graphicData4.add(new Graphic(graphicName, x, y, false));
//						}
//						if (graphicName.equals("null")){
//							if (x<leftXbound){
//								leftXbound = x;
//							}
//							if (x+32>rightXbound){
//								rightXbound = x+32;
//							}
//							if (y<topYbound){
//								topYbound = y;
//							}
//							if (y+32>bottomYbound){
//								bottomYbound = y+32;
//							}
//						}
//					} catch (Exception e) {
//						Console.out("WE SHOULDN'T BE GETTING THIS ERROR!!!");
//						//e.printStackTrace();
//					}
//				} catch(NumberFormatException e){
//					Console.out("GRAPHIC DATA CORRUPTION!!!");
//
//				}
//			}
//			in.close();
//			loadSuccess = true;
//		}catch (IOException e){
//			Console.out("**This level doesn't have a graphics list**");
//		}catch (NullPointerException e){
//			Console.out("**This level doesn't have a graphics " + num +" list**");
//		}
//	}	
//	public void readTiles(Map map, String fileName){//Reads each line and separates it into a different string
//		try{
//			BufferedReader in;
//			try{
//				InputStream file = getClass().getResourceAsStream(fileName);
//				InputStreamReader reader = new InputStreamReader(file);
//				in = new BufferedReader(reader);
//			} catch (Exception e){
//				FileReader reader = new FileReader(fileName);
//				in = new BufferedReader(reader);
//			}
//			String string;
//			while ((string = in.readLine()) != null){//Separates each component in the string as an array
//				String[] blockProperties;
//				blockProperties = string.split(";");
//				try {
//					int x = Integer.parseInt(blockProperties[0]);
//					int y = Integer.parseInt(blockProperties[1]);
//					int w = Integer.parseInt(blockProperties[2]);
//					int h = Integer.parseInt(blockProperties[3]);
//					boolean f = Boolean.valueOf(blockProperties[4]);
//					boolean c = Boolean.valueOf(blockProperties[5]);
//					boolean l = Boolean.valueOf(blockProperties[6]);
//					boolean r = Boolean.valueOf(blockProperties[7]);
//
//					try{
//						tileData.add(new Tile(x, y, w, h, f, c, l, r));
//						//int xArea = (int) Math.ceil(x/areaW)+200;
//						//int yArea = (int) Math.ceil(y/areaH)+200;
//
//						/*try{
//								tileDataIndex.get(xArea).get(yArea).add(new Integer(tileData.size()-1));
//							} catch (Exception e){
//								try{
//									tileDataIndex.get(xArea).add(yArea,new ArrayList<Integer>());
//									tileDataIndex.get(xArea).get(yArea).add(new Integer(tileData.size()-1));
//								} catch (Exception e2){
//									try{
//										tileDataIndex.add(xArea,new ArrayList<ArrayList<Integer>>());
//										tileDataIndex.get(xArea).add(yArea,new ArrayList<Integer>());
//										tileDataIndex.get(xArea).get(yArea).add(new Integer(tileData.size()-1));
//									} catch (Exception e3) {
//										e3.printStackTrace();
//										//Console.out("xArea: "+xArea+"  yArea: "+yArea);
//										Console.out("SOMETHING WENT HORRIBLY WRONG INDEXING A TILE!!!");
//										//System.exit(1);
//									}
//								}
//							}*/
//						/*try{
//								tileData2.get(xArea).get(yArea).add(new Tile(tileName, x, y, w, h, f, c, l, r));
//							} catch (Exception e){
//								try{
//									tileData2.get(xArea).add(yArea,new ArrayList<Tile>());
//									tileData2.get(xArea).get(yArea).add(new Tile(tileName, x, y, w, h, f, c, l, r));
//								} catch (Exception e2){
//									try{
//										tileData2.add(xArea,new ArrayList<ArrayList<Tile>>());
//										tileData2.get(xArea).add(yArea,new ArrayList<Tile>());
//										tileData2.get(xArea).get(yArea).add(new Tile(tileName, x, y, w, h, f, c, l, r));
//									} catch (Exception e3) {
//										Console.out("SOMETHING WENT HORRIBLY WRONG ADDING A TILE!!!");
//									}
//								}
//							}*/
//					} catch (Exception e) {
//						Console.out("WE SHOULDN'T BE GETTING THIS ERROR!!!");
//						//e.printStackTrace();
//					}
//				} catch(NumberFormatException e){
//					Console.out("TILE DATA CORRUPTION!!!");
//				}
//			}
//			in.close();
//			loadSuccess = true;
//		}catch (IOException e){
//			Console.out("**This level doesn't have a tiles list**");
//		}catch (NullPointerException e){
//			Console.out("**This level doesn't have a tiles list**");
//		}
//	}
//	public void readCharacters(Map map, String fileName){
//		try{
//			BufferedReader in;
//			try{
//				InputStream file = getClass().getResourceAsStream(fileName);
//				InputStreamReader reader = new InputStreamReader(file);
//				in = new BufferedReader(reader);
//			} catch (Exception e){
//				FileReader reader = new FileReader(fileName);
//				in = new BufferedReader(reader);
//			}
//			String string;
//			while ((string = in.readLine()) != null){//Separates each component in the string as an array
//				String[] charProperties;
//				charProperties = string.split(";");
//				String name = null;
//				try {
//					int team = Integer.parseInt(charProperties[0]);
//					name = charProperties[1];
//					int x = Integer.parseInt(charProperties[2]);
//					int y = Integer.parseInt(charProperties[3]);
//					int	w = Integer.parseInt(charProperties[4]);
//					int	h = Integer.parseInt(charProperties[5]);
//
//					Character character = new Character(board, team, name, x, y, w, h, "");
//
//					boolean exit = false;
//					for (int i = 6; !exit; i++){
//						try{
//							character.addSettings(charProperties[i]);
//						} catch (Exception e){
//							exit = true;
//						}
//					}
//
//					charData.add(character);
//
//				} catch(NumberFormatException e){
//					Console.out("CHARACTER DATA CORRUPTION!!!");
//				}
//			}
//			in.close();
//		}catch (IOException e){
//			Console.out("**This level doesn't have a characters list**");
//			//e.printStackTrace();
//		}catch (NullPointerException e){
//			Console.out("**This level doesn't have a characters list**");
//			//e.printStackTrace();
//		}
//	}
//	public void readSpecialData(Map map, String fileName){//Reads each line and separates it into a different string
//		try{
//			//File file = new File(fileName);
//			//FileReader reader = new FileReader(file);
//			BufferedReader in;
//			try{
//				InputStream file = getClass().getResourceAsStream(fileName);
//				InputStreamReader reader = new InputStreamReader(file);
//				in = new BufferedReader(reader);
//			} catch (Exception e){
//				FileReader reader = new FileReader(fileName);
//				in = new BufferedReader(reader);
//			}
//			String string;
//			while ((string = in.readLine()) != null){//Separates each component in the string as an array
//				String[] props = null;
//				props = string.split(";");
//
//				String className = null;
//				className = props[0];
//
//				if (className.equals("door")){
//					//Console.out("Makin' a door");
//					try {
//						// name, x, y, w, h, newLocation, activation type (interact), entrance to come in on
//
//						double x = Integer.parseInt(props[1]);
//						double y = Integer.parseInt(props[2]);
//						int w = Integer.parseInt(props[3]);
//						int h = Integer.parseInt(props[4]);
//
//						String transLocation = props[5];
//						String activate = props[6];
//						String entrance = props[7];
//						specialData.add(new Door(x, y, w, h, transLocation, activate, entrance));
//					} catch (Exception e) {
//						Console.out("SOMETHING WENT HORRIBLY WRONG MAKING A NEW DOOR!!!");
//						//e.printStackTrace();
//					}
//				} else if (className.equals("hurtObject")){
//					//Console.out("Makin' a hurtObject");
//					try {
//						// name, x, y, w, h, damage
//
//						double x = Integer.parseInt(props[1]);
//						double y = Integer.parseInt(props[2]);
//						int w = Integer.parseInt(props[3]);
//						int h = Integer.parseInt(props[4]);
//						double damage = Double.parseDouble(props[5]);
//
//						specialData.add(new HurtObject(x, y, w, h,damage));
//					} catch (Exception e) {
//						Console.out("SOMETHING WENT HORRIBLY WRONG MAKING A NEW HURTOBJECT!!!");
//					}
//				} else if (className.equals("slope")){
//					//Console.out("Makin' a slope");
//					try {
//						// name, x, y, w, h, direction
//
//						double x = Integer.parseInt(props[1]);
//						double y = Integer.parseInt(props[2]);
//						int w = Integer.parseInt(props[3]);
//						int h = Integer.parseInt(props[4]);
//						String direction = props[5];
//
//						specialData.add(new Slope(x, y, w, h, direction));
//					} catch (Exception e) {
//						Console.out("SOMETHING WENT HORRIBLY WRONG MAKING A NEW SLOPE!!!");
//						//e.printStackTrace();
//					}
//				} else if (className.equals("crumbleBlock")){
//					try {
//						// name, x, y, w, h, floor, ceiling, left, right, time
//						specialData.add(new CrumbleBlock(string));
//					} catch (Exception e) {
//						Console.out("SOMETHING WENT HORRIBLY WRONG MAKING A NEW CRUMBLEBLOCK!!!");
//						//e.printStackTrace();
//					}
//				} else if (className.equals("animation")){
//					try {
//						/*int layer = 1;
//						int cD = -1;
//						int rD = -1;
//						double x = Integer.parseInt(props[1]);
//						double y = Integer.parseInt(props[2]);
//						int w = Integer.parseInt(props[3]);
//						int h = Integer.parseInt(props[4]);
//
//						String name = props[5];
//						boolean exit = false;
//						for (int i = 6; !exit; i++){
//							String[] settings;
//							try{
//								settings = props[i].split(":");
//								if (settings[0].equals("cD")){
//									cD = Integer.parseInt(settings[1]);
//								} else if (settings[0].equals("rD")){
//									rD = Integer.parseInt(settings[1]);
//								} else if (settings[0].equals("L")){
//									layer = Integer.parseInt(settings[1]);
//								}
//							} catch (Exception e){
//								exit = true;
//							}
//						}
//
//						Animation anim = new Animation(name,x, y, w, h);
//						anim.changeDelay = cD;
//						anim.repeatDelay = rD;
//						if (layer == 1){
//							middleSpecialData.add(anim);
//						} else if (layer == -1){
//							backSpecialData.add(anim);
//						} else if (layer == 2){
//							frontSpecialData.add(anim);
//						} else {
//							Console.out("Layer \""+layer+"\" is not an acceptable animation layer.");
//						}*/
//
//
//					} catch (Exception e) {
//						Console.out("SOMETHING WENT HORRIBLY WRONG MAKING A NEW ANIMATION!!!");
//						//e.printStackTrace();
//					}
//				} else if (className.equals("buffer")){
//					try {
//						// name, x, y, w, h, floor, ceiling, left, right, force, disable time
//
//						double x = Double.parseDouble(props[1]);
//						double y = Double.parseDouble(props[2]);
//						int w = Integer.parseInt(props[3]);
//						int h = Integer.parseInt(props[4]);
//						boolean f = Boolean.parseBoolean(props[5]);
//						boolean c = Boolean.parseBoolean(props[6]);
//						boolean l = Boolean.parseBoolean(props[7]);
//						boolean r = Boolean.parseBoolean(props[8]);
//						double force = Double.parseDouble(props[9]);
//						int disable = Integer.parseInt(props[10]);
//
//						specialData.add(new Buffer(x, y, w, h,f,c,l,r,force,disable));
//					} catch (Exception e) {
//						Console.out("SOMETHING WENT HORRIBLY WRONG MAKING A NEW BUFFER!!!");
//						//e.printStackTrace();
//					}
//				} else if (className.equals("downFloor")){
//					try {
//						// name, x, y, w, h
//
//						double x = Double.parseDouble(props[1]);
//						double y = Double.parseDouble(props[2]);
//						int w = Integer.parseInt(props[3]);
//						int h = Integer.parseInt(props[4]);
//
//						specialData.add(new DownFloor(x, y, w, h));
//					}catch(Exception e){
//						Console.out("SOMETHING WENT HORRIBLY WRONG MAKING A NEW DOWNFLOOR!!!");
//						//e.printStackTrace();
//					}
//				} else if (className.equals("sign")){
//					try {
//						// name, x, y, w, h, c:Conversation,
//
//						double x = Integer.parseInt(props[1]);
//						double y = Integer.parseInt(props[2]);
//						int w = Integer.parseInt(props[3]);
//						int h = Integer.parseInt(props[4]);
//
//						String cHostID = "";
//
//						ArrayList<Conversation> convos = new ArrayList<Conversation>();
//						boolean exit = false;
//						for (int i = 5; !exit; i++){
//							String[] settings;
//							try{
//								settings = props[i].split(":");
//								if (settings[0].equals("c")){ //settings[1] is the quest number
//									Conversation c = new Conversation(settings[1], "");
//
//									boolean exit2 = false;
//									try{
//										for (int j = 2; !exit2; j+=2){
//											if (settings[j].equals("a")){
//												c.setActivation(Integer.parseInt(settings[j+1]));
//											} else if (settings[j].equals("d")){
//												c.setDisplay(Integer.parseInt(settings[j+1]));
//											} else if (settings[j].equals("q")){
//												c.setQuest(Integer.parseInt(settings[j+1]));
//											}
//											//Console.out(settings[j]);
//										}
//									} catch (IndexOutOfBoundsException e){
//										exit2 = true;
//									}
//									convos.add(c);
//								} else if (settings[0].equals("cHostID")){
//									cHostID = settings[1];
//								}
//							} catch (Exception e){
//								exit = true;
//							}
//						}
//						Sign s = new Sign(x, y, w, h, convos);
//
//						if (!cHostID.equals("")){
//							s.cHostID = cHostID;
//						}
//						specialData.add(s);
//
//					} catch (Exception e) {
//						Console.out("SOMETHING WENT HORRIBLY WRONG MAKING A NEW SIGN!!!");
//						//e.printStackTrace();
//					}
//				} else if (className.equals("saveModule")){
//					try {
//						double x = Integer.parseInt(props[1]);
//						double y = Integer.parseInt(props[2]);
//						int w = Integer.parseInt(props[3]);
//						int h = Integer.parseInt(props[4]);
//						String id = props[5];
//						specialData.add(new SaveModule(x, y, w, h, id));
//					} catch (Exception e) {
//						Console.out("SOMETHING WENT HORRIBLY WRONG MAKING A NEW SAVE MODULE!!!");
//						//e.printStackTrace();
//					}	
//				} else if (className.equals("timedObject")){
//					try {
//						double x = Integer.parseInt(props[1]);
//						double y = Integer.parseInt(props[2]);
//						int w = Integer.parseInt(props[3]);
//						int h = Integer.parseInt(props[4]);
//						String type = props[5];
//						String destination = props[6];
//
//						specialData.add(new TimedObject(board, this, x, y, w, h, type, destination));
//					} catch (Exception e) {
//						Console.out("SOMETHING WENT HORRIBLY WRONG MAKING A NEW TIMED OBJECT");
//						//e.printStackTrace();
//					}
//				} else if (className.equals("graphicBlock")){
//					try {
//						double x = Integer.parseInt(props[1]);
//						double y = Integer.parseInt(props[2]);
//						int w = Integer.parseInt(props[3]);
//						int h = Integer.parseInt(props[4]);
//						String type = props[5];
//						int layer = Integer.parseInt(props[6]);
//
//						specialData.add(new GraphicBlock(this, layer, type, x, y, w, h));
//						//Console.out("added a graphic block");
//					} catch (Exception e) {
//						Console.out("SOMETHING WENT HORRIBLY WRONG MAKING A NEW GRAPHIC BLOCK!!!");
//						//e.printStackTrace();
//					}
//				} else if (className.equals("movingPlatform")){
//					try{
//						specialData.add(new MovingPlatform(board, this, string));
//					} catch (Exception e) {
//						Console.out("SOMETHING WENT HORRIBLY WRONG MAKING A NEW MOVING PLATFORM!!!");
//						e.printStackTrace();
//					}	
//
//				} else {
//					/*try {
//						double x = Double.parseDouble(props[1]);
//						double y = Double.parseDouble(props[2]);
//						int w = Integer.parseInt(props[3]);
//						int h = Integer.parseInt(props[4]);
//
//						ArrayList<String> settings = new ArrayList<String>();
//						for (int i = 5; i<props.length; i++){
//							settings.add(props[i]);
//						}
//
//						middleSpecialData.add(new Generic(this, className, x, y, w, h, settings));
//					} catch(Exception e){
//						Console.out("SOMETHING WENT HORRIBLY WRONG MAKING \""+className+"\"!!!");
//						//e.printStackTrace();
//					}*/
//				}
//			}
//			in.close();
//			loadSuccess = true;
//		}catch (IOException e){
//			Console.out("**This level doesn't have a specialObjects list**");
//			//e.printStackTrace();
//		}catch (NullPointerException e){
//			Console.out("**This level doesn't have a specialObjects list**");
//		}
//	}
//	public void readEntranceData(Map map, String fileName){//Reads each line and separates it into a different string
//		try{
//			BufferedReader in;
//			try{
//				InputStream file = getClass().getResourceAsStream(fileName);
//				InputStreamReader reader = new InputStreamReader(file);
//				in = new BufferedReader(reader);
//			} catch (Exception e){
//				FileReader reader = new FileReader(fileName);
//				in = new BufferedReader(reader);
//			}
//			String string;
//			int num = 0;
//			while ((string = in.readLine()) != null){//Separates each component in the string as an array
//				try{
//					String[] entranceProperties = null;
//					entranceProperties = string.split(";");
//					entrances.add(num, new ArrayList<String>());
//					entrances.get(num).add(entranceProperties[0]);
//					entrances.get(num).add(entranceProperties[1]);
//					entrances.get(num).add(entranceProperties[2]);
//					num++;
//				} catch (IndexOutOfBoundsException e){
//					Console.out("Corrupted map entrance data");
//				}
//			}
//			in.close();
//		}catch (IOException e){
//			Console.out("This level doesn't have an entranceData file.");
//			//e.printStackTrace();
//		}catch (NullPointerException e){
//			Console.out("This level doesn't have an entranceData file.");
//		}
//	}
//}

