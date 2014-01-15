package GameEngine;

import java.util.ArrayList;

public class Quest {
	int questProgress;
	ArrayList<String[]> randomProperties = new ArrayList<String[]>();
	
	String startingMap;
	String startingEntrance;
	
	String timeOfDay = "day";

	Board board;
	Game game;
	Map theMap;
	
	public void checkMapInitScripts(String mapName, Map theMap){}
	public void advanceQuest(int newQuestProgress){}
	
	public void removeCharacter(String id){
		for (int i = 0; i<theMap.charData.size(); i++){
			CharacterTemplate c = theMap.charData.get(i);
			if (c.id.equals(id)){
				theMap.charData.remove(i);
				break;
			}
		}
	}
	public int getCharacterNumber(String id){
		for (int i = 0; i<theMap.charData.size(); i++){
			CharacterTemplate c = theMap.charData.get(i);
			if (c.id.equals(id)){
				return i;
			}
		}
		return -1;
	}
	public CharacterTemplate getCharacter(String id){
		for (int i = 0; i<theMap.charData.size(); i++){
			CharacterTemplate c = theMap.charData.get(i);
			if (c.id.equals(id)){
				return c;
			}
		}
		return null;
	}
	public SpecialObject getObject(String id){
		for (int i = 0; i<theMap.specialData.size(); i++){
			SpecialObject s = theMap.specialData.get(i);
			if (s.id.equals(id)){
				return s;
			}
		}
		return null;
	}
	public void removeObject(String id){
		for (int i = 0; i<theMap.specialData.size(); i++){
			SpecialObject s = theMap.specialData.get(i);
			if (s.id.equals(id)){
				theMap.specialData.remove(i);
			}
		}
	}
	
	public Weapon getMainCharacterWeapon(String name){
		for (int i = 0; i<game.player.weapons.size(); i++){
			Weapon w = game.player.weapons.get(i);
			if (w.name.equals(name)){
				return w;
			}
		}
		return null;
	}
	
	
	public void createQuestTrigger(int _q, double _x, double _y, int _w, int _h){
		SpecialObject sign = new Sign(_x,_y,_w,_h,new Conversation("@q"+_q+"@", "d:1;a:1"));
		sign.id = "quest"+_q+"_trigger";
		theMap.specialData.add(sign);
	}
	public void deleteQuestTrigger(int _q){
		for (int i = 0; i<theMap.specialData.size(); i++){
			SpecialObject sObject = theMap.specialData.get(i);
			if (sObject.id.equals("quest"+_q+"_trigger")){
				theMap.specialData.remove(i);
				i--;
			}
		}
	}
	public void createTrackerTrigger(String _id, String _value, double _x, double _y, int _w, int _h){
		theMap.specialData.add(new Generic(theMap, "trackerTrigger", _x, _y, _w, _h, _id+";"+_value));
	}
	
	public void createTalkBuffer(double _x, String _text, CharacterTemplate _host, String _id, String _props){
		createTalkBuffer(_x-64, -10000000, 8, 20000000, _text, _host, _id, _props);
	}
	public void createTalkBuffer(double _x, double _y, double _w, double _h, String _text, CharacterTemplate _host, String _id, String _props){
		Buffer _buffer = new Buffer(_x, _y, (int)_w, (int)_h, "force:4;disable:1");
		String[] props = _props.split(";");
		boolean exit = false;
		for (int i = 0; !exit; i++){
			String[] settings = {};
			try{
				settings = props[i].split(":");
				if (settings[0].equals("side")){
					if (settings[1].equals("right")){
						_buffer.right = true;
					} else if (settings[1].equals("left")){
						_buffer.left = true;
					}
				}
			} catch (IndexOutOfBoundsException e){exit = true;
			} catch (Exception e){System.out.println("Error with the value of \"" + settings[0]+ "\": "+settings[1]+".");}
		}
		if (!(_buffer.right || _buffer.left)){
			_buffer.right = true;
			_buffer.left = true;
		}
		Sign _sign = new Sign(_x-1,_y,(int)_w+2,(int)_h, new Conversation(_text, "a:1"));
		_sign.cHost = _host;
		
		theMap.specialData.add(_buffer);
		theMap.specialData.add(_sign);
	}
	
	public void createTalkTrigger(double _x, double _y, double _w, double _h, CharacterTemplate _host, Conversation _conversation){
		if (_conversation.id.equals("")){
			_conversation.id = ""+(int)(Math.random()*1000);
		}
		_host.aiConversations.add(_conversation);
		theMap.specialData.add(new Generic(theMap, "talkTrigger",_x,_y,(int)_w,(int)_h,_host.id+";"+_conversation.id));
	}
}













