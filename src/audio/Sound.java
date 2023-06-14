package audio;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class Sound {
    public File clap;
    Clip clip;

    public Sound(String name_file) {
        clap = new File(name_file);

    }

    public void play() {
        try {
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(clap));
            clip.start();
            // Thread.sleep(clip.getMicrosecondLength()/10000);
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            System.out.println("Error: " + e);
        }
    }

    public void stop() {
        clip.stop();
    }

    public boolean isRun() {
        return clip.isRunning();
    }
}
