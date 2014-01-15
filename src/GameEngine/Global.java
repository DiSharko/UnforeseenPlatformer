package GameEngine;

public class Global {
	public static int DEBUG_MODE = 0; // 0 for normal export
	
	
	public static boolean EXTERNAL = false;
	
	
	public static int titleBar = 1;
	public static boolean audioEnabled = true;
	public static boolean saveEnabled = true;
	public static boolean questTriggersEnabled = false;

	// 0 = Mac, 1 = Windows
	public static int OS = 0;
	
	
	public static String quest = "unforeseen";
//	public static String quest = "hourglass";
	//public static String quest = "minigame";
	
	
		
	// ENGINE SETTINGS
	public static char attack1Key = 'd';
	public static char attack2Key = 's';
	public static char attack3Key = 'a';
	public static boolean downFloorArrow = false;
}