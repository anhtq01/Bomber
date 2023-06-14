package audio;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Music {
    public static String soundThemeFile = "res/audio/audioTheme.wav";
    public static String soundExploisionFile = "res/audio/audioExplosion.wav";
    public static String soundGetItemFile = "res/audio/audioGetItem.wav";
    public static String soundGameOverFile = "res/audio/audioGameOver.wav";
    public static String soundNextLevelFile = "res/audio/ready.wav";
    public static String soundBomberDeadFile = "res/audio/audioBomberDead.wav";
    public static String soundBomberStep = "res/audio/audioStep.wav";
    public static String soundScreen = "res/audio/audioScreen.wav";
    public static String soundDelay = "res/audio/delay.wav";

    public static Clip clipTheme;
    public static Clip clipExploision;
    public static Clip clipGetItem;
    public static Clip clipGameOver;
    public static Clip clipNextLevel;
    public static Clip clipBomberDead;
    public static Clip clipBomberStep;
    public static Clip clipScreen;
    public static Clip clipDelay;


    public Music() {
        try {
            clipTheme = AudioSystem.getClip();
            clipExploision = AudioSystem.getClip();
            clipGetItem = AudioSystem.getClip();
            clipGameOver = AudioSystem.getClip();
            clipNextLevel = AudioSystem.getClip();
            clipBomberDead = AudioSystem.getClip();
            clipBomberStep = AudioSystem.getClip();
            clipScreen = AudioSystem.getClip();
            clipDelay = AudioSystem.getClip();

            clipTheme.open(AudioSystem.getAudioInputStream(new File(soundThemeFile)));
            clipExploision.open(AudioSystem.getAudioInputStream(new File(soundExploisionFile)));
            clipGetItem.open(AudioSystem.getAudioInputStream(new File(soundGetItemFile)));
            clipGameOver.open(AudioSystem.getAudioInputStream(new File(soundGameOverFile)));
            clipNextLevel.open(AudioSystem.getAudioInputStream(new File(soundNextLevelFile)));
            clipBomberDead.open(AudioSystem.getAudioInputStream(new File(soundBomberDeadFile)));
            clipBomberStep.open(AudioSystem.getAudioInputStream(new File(soundBomberStep)));
            clipScreen.open(AudioSystem.getAudioInputStream(new File(soundScreen)));
            clipDelay.open(AudioSystem.getAudioInputStream(new File(soundDelay)));


        } catch (LineUnavailableException e) {
        } catch (UnsupportedAudioFileException e) {
        } catch (IOException e) {
        }
    }

    public static void stopAll() {
        clipTheme.stop();
        clipExploision.stop();
        clipGetItem.stop();
        clipGameOver.stop();
        clipNextLevel.stop();
        clipBomberDead.stop();
        clipBomberStep.stop();
        clipScreen.stop();
        clipDelay.stop();
    }
    public void stop() {

    }

}
