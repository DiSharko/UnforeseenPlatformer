package GameEngine;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public abstract class Texture {

	static ArrayList<Graphic> textures = new ArrayList<Graphic>();	
	static ArrayList<String> textureSets = new ArrayList<String>();


	public static void loadTextures(){
		try{
			InputStream file = Board.class.getResourceAsStream("/media/editor/setsIndex.txt");
			InputStreamReader reader = new InputStreamReader(file);
			BufferedReader in = new BufferedReader(reader);
			String _string;
			while ((_string = in.readLine()) != null){
				try {
					String _name = _string.split(",")[0];

					if (!_name.equals("special") && !_name.equals("tile")){
						textureSets.add(_name);
					}
				}catch (NullPointerException e){
					Console.out("PROBLEM READING SETS INDEX");
				}
			}

			in.close();
		}catch (IOException e){
			Console.out("Do we ever get this error? <setsIndex>");
		}catch (NullPointerException e){
			Console.out("NO SETS INDEX FILE!!!");
		}
		//Read the sets
		for (int i = 0; i < textureSets.size(); i++){
			if (!textureSets.get(i).equals("character")){
				try{
					InputStream file = Board.class.getResourceAsStream("/media/editor/"+textureSets.get(i)+"Set.txt");
					InputStreamReader reader = new InputStreamReader(file);
					BufferedReader in = new BufferedReader(reader);
					String string;
					while ((string = in.readLine()) != null){
						String[] blockProperties;
						blockProperties = string.split(",");
						try{
							textures.add(new Graphic(blockProperties[0],0,0, true));
						} catch (Exception e) {
							Console.out("Bad tile in the tileSet");
							e.printStackTrace();
						}
					}
				}catch (IOException e){
					Console.out("Do we ever get this error? <tileSet reader>");

				}catch (NullPointerException e){
					Console.out("NO TILE SET "+textureSets.get(i)+" FILE!!!");
				}
			}
		}
	}

	public static Image getImage(String _name){
		Image _image = null;
		for (int i = 0; i<textures.size(); i++){
			if (textures.get(i).name.equals(_name)){
				_image = textures.get(i).sprite;
				break;
			}
		}
		return _image;
	}
}
