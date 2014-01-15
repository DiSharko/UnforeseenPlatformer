package GameEngine;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class PlatformerMain extends JFrame{

	
	public PlatformerMain(int window){
		if (window == 0){
			add(new SplashScreen());
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setSize(200, 80+22+16*Global.OS);
			setLocationRelativeTo(null);
			setResizable(false);
			setTitle("by Andrew DiMarco");
			//setUndecorated(true);
			
			setVisible(true);
		
		} else if (window == 1){
			add(new Board());
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setSize(640, 480 + 22*Global.titleBar + 16*Global.OS);
			setResizable(false);
			setLocationRelativeTo(null);
			
			if (Global.titleBar == 1){
				setTitle("Game");
			} else {
				setUndecorated(true);
			}

			setVisible(true);
		}
	}
	public static void main(String[] args) {
		JFrame splash = new PlatformerMain(0);
		new PlatformerMain(1);
		splash.dispose();
	}
}
