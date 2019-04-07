/*
Author: Danny Lu
Project: CIS 422 Project 2: Music Maker

Functions: getFile(), getClip(), getFileName(), setFrame(), play(), startPlaying(), pause(), stop()
reference the Module Interface Specification to learn more about
how to use each function.

This file mainly handles playing audio clips.
*/
import javax.sound.sampled.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;

public class play{
    Long frame = 0l;
    Clip clip;
    String status;
    AudioFormat format;
    DataLine.Info info;
    AudioInputStream inStream;
    String fileName;
    File file;

    //
    public File getFile(){
    	return file;
    }

    //Used in edit to alter sound of clip
    public Clip getClip(){
        return clip;
    }

    //Used to initilize objects of class play and used to be displayed to user in user interface
    public String getFileName(){
        return fileName;
    }

    //Can be used to start audio at a specific time, may be potentially executed in edit but so far this function has no use.
    public void setFrame(long timestamp){
        frame = timestamp;
    }

    //Initialize object of classs play. Gets filename and loads the audio file. Gets the format of the audio file.
    public play(String fileNameInput){
        try{
            fileName = fileNameInput;
            file = new File(fileName);
            inStream = AudioSystem.getAudioInputStream(file);
            format = inStream.getFormat();
            info = new DataLine.Info(Clip.class, format);
            clip = (Clip) AudioSystem.getLine(info);
        } catch(UnsupportedAudioFileException uae){
            System.out.println(uae);
        } catch(IOException ioe){
            System.out.println(ioe);
        } catch(LineUnavailableException lua){
            System.out.println(lua);
        }
    }

    //Opens the stream and starts playing if the clip hasn't already been played and resumes audio if the clip has already been played
    public void startPlaying() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        try{
            if (frame == 0l){
                clip.open(inStream);
                clip.start();
            } else {
                clip.setMicrosecondPosition(frame);
                clip.start();
            }
            status = "playing";
        } catch(Exception e){
            System.out.println("Error with starting clip");
        }
    }

    //Stops audio and save the time that it was stopped to be resumed.
    public void pause(){
        if (status.equals("paused")){
            System.out.println("Audio is already paused");
            return;
        }
        frame = clip.getMicrosecondPosition();
        clip.stop();
        status = "paused";
    }

    //Stops audio stream and restarts the clip if the clip needs to be played again.
    public void stop(){
        if(status.equals("paused") || status.equals("stopped")){
            System.out.println("Clip is already not playing");
            return;
        }
        clip.stop();
        status = "stopped";
    }
}
