import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


public class AudioPlayer {
    private Clip sound;

    public void playMusic(String FilePath) {
        try {
            File musicFile = new File(FilePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
            sound = AudioSystem.getClip();
            sound.open(audioStream);
            sound.start();
            sound.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void playSound(String FilePath) {
        try {
            File soundFile = new File(FilePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            sound = AudioSystem.getClip();
            sound.open(audioStream);
            sound.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void stopMusic() {
        if (sound != null && sound.isRunning()) {
            sound.stop();
        //    sound.flush();
            sound.close();
        }
    }
}
