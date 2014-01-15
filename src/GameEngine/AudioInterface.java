package GameEngine;

import java.util.ArrayList;

import javax.sound.sampled.Clip;

public class AudioInterface {
	ArrayList<AudioSample> songs = new ArrayList<AudioSample>();
	ArrayList<AudioSample> sounds = new ArrayList<AudioSample>();

	ArrayList<Clip> soundsPlaying = new ArrayList<Clip>();
	ArrayList<Boolean> soundTrackers = new ArrayList<Boolean>();

	ArrayList<Clip> songsPlaying = new ArrayList<Clip>();
	ArrayList<String> songNames = new ArrayList<String>();
	ArrayList<Boolean> songTrackers = new ArrayList<Boolean>();

	Clip currentSong;
	boolean currentSongRestartable = true;

	public AudioInterface(){
		if (Global.audioEnabled){
			importSong("cats_cradle");
			importSong("liveLab");
			importSong("apollo");
			importSong("knoll");
			importSong("rolling");
			importSong("rain");
			
			importSound("thunder");
			importSound("AfroString");
			importSound("Chord");
		}
	}
	public void importSong(String name){
		AudioSample a = new AudioSample(name, "song");
		if (!a.delete){
			songs.add(a);
		} else {
			Console.out("Failed to create song "+name+".");
		}
	}
	public void importSound(String name){
		AudioSample a = new AudioSample(name, "sound");
		if (!a.delete){
			sounds.add(a);
		} else {
			Console.out("Failed to create sound "+name+".");
		}
	}

	public void playSound(String name){
		if (Global.audioEnabled){
			for (int i = 0; i<sounds.size(); i++){
				if (sounds.get(i).name.equals(name)){
					Clip audio = sounds.get(i).copyAudio();
					audio.setFramePosition(0);
					soundsPlaying.add(audio);
					soundTrackers.add(false);
					audio.start();
					break;
				}
			}
		}
	}

	public void manageSounds(){
		if (Global.audioEnabled){
			for (int i = 0; i<soundsPlaying.size(); i++){
				if (soundsPlaying.get(i).getFramePosition() == 0 || /*for windows?*/ soundsPlaying.get(i).getFrameLength() == soundsPlaying.get(i).getFramePosition()){
					if (soundTrackers.get(i)){
						soundsPlaying.get(i).close();
						soundsPlaying.remove(i);
						soundTrackers.remove(i);
					} else {
						soundTrackers.set(i, true);
					}
				}
			}
		}
	}

	/////// SONGS ///////

	public void playSong(String _name){
		if (Global.audioEnabled){
			try {
				boolean found = false;
				for (int i = 0; i<songs.size(); i++){
					if (songs.get(i).name.equals(_name)){
						Clip audio = songs.get(i).copyAudio();
						audio.setFramePosition(0);
						songsPlaying.add(audio);
						songTrackers.add(true);
						songNames.add(_name);
						audio.start();
						found = true;
						break;
					}
				}
				if (!found){
					Console.out("Could not find the song \""+_name+"\" to play.");
				}
			} catch (NullPointerException e) {
				Console.out("This should only occur in exported versions, because the spelling/capitalization doesn't match.");
			}
		}
	}
	public void pauseSongs(){
		if (Global.audioEnabled){
			for (int i = 0; i < songsPlaying.size(); i++){
				if (songsPlaying.get(i) != null){
					songsPlaying.get(i).stop();
				} else {
					Console.out("I FOUND A NULL SONG??");
				}
			}
		}
	}
	public void resumeSongs(){
		if (Global.audioEnabled){
			for (int i = 0; i < songsPlaying.size(); i++){
				if (songsPlaying.get(i) != null){
					songsPlaying.get(i).start();
				} else {
					Console.out("I FOUND A NULL SONG??");
				}
			}
		}
	}
	public void endSong(String _name){
		if (Global.audioEnabled){
			Console.out("ending song "+_name);
			for (int i = 0; i < songNames.size(); i++){
				if (songNames.get(i).equals(_name)){
					songsPlaying.get(i).stop();
					songsPlaying.get(i).close();
					songsPlaying.remove(i);
					songTrackers.remove(i);
					songNames.remove(i);
				}
			}
		}
	}
	public void endSongs(){
		if (Global.audioEnabled){
			for (int i = 0; i < songsPlaying.size(); i++){
				if (songsPlaying.get(i) != null){
					songsPlaying.get(i).stop();
					songsPlaying.get(i).close();
					songsPlaying.remove(i);
					songTrackers.remove(i);
					songNames.remove(i);
				}
			}
		}
	}
	public void manageMusic(){
		if (Global.audioEnabled){
			try{
				for (int i = 0; i < songsPlaying.size(); i++){
					if (songsPlaying.get(i) != null){
						if (songsPlaying.get(i).getFramePosition() == 0 && songTrackers.get(i) || /*for windows?*/ songsPlaying.get(i).getFrameLength() == songsPlaying.get(i).getFramePosition()){
							Console.out("Restarting a song.");
							songsPlaying.get(i).setFramePosition(0);
							songsPlaying.get(i).start();
							songTrackers.set(i, false);
						} else if (songsPlaying.get(i).getFramePosition() > 0){
							songTrackers.set(i, true);
						}
					}
				}
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}
}
