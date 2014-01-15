package GameEngine;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public class AudioSample {
	boolean delete = false;
	String name;
	String type;  // "song" or "sound" (effect)
	Clip audio;
	public AudioSample (String n, String t){
		name = n;
		type = t;

		if (!type.equals("song") && !type.equals("sound")){
			delete = true;
		}

		if (!delete){
			try{
				//audio = copyAudio();
			} catch (Exception e){
				e.printStackTrace();
				delete = true;
			}
		}

	}
	public Clip copyAudio(){
		AudioInputStream sound;
		Clip a = null;
		try {
			sound = AudioSystem.getAudioInputStream(this.getClass().getResourceAsStream("/media/audio/"+type+"s/"+name+".wav"));
		
			DataLine.Info info = new DataLine.Info(Clip.class, sound.getFormat());
			a = (Clip) AudioSystem.getLine(info);
			a.open(sound);

		} catch (Exception e){
			e.printStackTrace();
			delete = true;
		}

		return a;

	}
}
