package GameEngine;

//import javax.swing.ImageIcon;

public class MachineGun extends Weapon {
	public MachineGun(Boolean a1, Boolean a2, Boolean a3){
		name = "Machine Gun";
		a1enabled = a1;
		a2enabled = a2;
		a3enabled = a3;
		
		try {
			//sprite = new ImageIcon(Board.class.getResource("/media/images/graphics/machineGun.png")).getImage();
		} catch(NullPointerException e){
			Console.out("SOMETHING HAPPENED TO OUR MACHINE GUN!!");
		}
		x = 17;
		y = 16;
		w = 16;
		h = 8;
	}
	public void handleAttack(Map theMap, CharacterTemplate host, boolean[] KeysDown, int[] KeysDownTimer){
		if (KeysDown[9]){
			theMap.specialData.add(new Bullet(host));
		}
	}
	public void attack1(Map map, CharacterTemplate current){
		if (a1enabled){
			current.attackNumber = 0;
			map.specialData.add(new Bullet(current));
		}
	}
}
