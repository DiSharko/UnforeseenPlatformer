package GameEngine;

//import javax.swing.ImageIcon;

public class FireSet1 extends Weapon {
	public FireSet1(){
		name = "Basic Fire";
		/*try {
			sprite = new ImageIcon(Board.class.getResource("/media/images/bit16/graphics/fireballGun.png")).getImage();
		} catch(NullPointerException e){
			Console.out("SOMETHING HAPPENED TO OUR FIREBALL GUN!!");
		}*/
		w = 22;
		h = 16;
	}
	public void attack1(Map map, CharacterTemplate current){
		current.attackNumber = 0;
		map.specialData.add(new Fireball1(current));
	}
	public void attack2(Map map, CharacterTemplate current){
		current.attackNumber = 0;
		map.specialData.add(new FireBurst(current));
	}
}
