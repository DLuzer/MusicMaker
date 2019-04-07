/*
Author: Danny Lu
Project: CIS 422 Project 2: Music Maker

Functions: getNum(), removeAll(), getMax(), addClip(), playAll(), pauseAll(), stopAll(), volumeForAll(), panForAll()
reference the Module Interface Specification to learn more about
how to use each function.

This file mainly handles using frunctions from other files and apply them all together to work with multiple audio files at once.
*/
import java.io.*;
import java.util.*;
import javax.sound.sampled.*;

public class soundManager{
    private ArrayList<play> clips = new ArrayList<play>();
    private double max = 0d;
    private int num = 0;
    boolean isPause = false;
    boolean allPlaying = false;

    //Number of all clips in arraylist, used to make sure the user doesn't add too many clips
    public int getNum(){
    	return num;
    }

    //Reset clips by clearing the arraylist and reset number back to zero
    public void removeAll(){
        clips.clear();
        num = 0;
    }

    //Returns the maximum longth of duration of all the clips in the arraylist
    public double getMax(){
    	return max;
    }

    //Adds clip to arraylist, if the clip is already in the arraylist, do nothing. Check if the clip's duration is longer than any of the clips currently in the arraylist
    public void addClip(play clip){
        if (num >= 20){
            return;
        }
    	for (int i = 0; i < clips.size(); i++){
    		if(clip.getFileName() == clips.get(i).getFileName()){
    			return;
    		}
    	}
    	double temp = clip.getFile().length() / 16000 / 2.0;
    	if (temp > max){
			max = temp;
			System.out.println("max is " + max);
		}
        clips.add(clip);
        num++;
        System.out.println("max " + max);
    }

    //Play all clips in the arraylist to have audio played concurrently
    public void playAll() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        for (int i = 0; i < clips.size(); i++){
            clips.get(i).startPlaying();
        }
    }

    //Pause all clips in the arraylist and save their timestamps to be resumed.
    public void pauseAll(){
        for(int i = 0; i < clips.size(); i++){
            clips.get(i).pause();
        }
        isPause = true;
    }

    //Stop all clips and restarts the streams
    public void stopAll(){
    	for(int i = 0; i < clips.size(); i++){
            clips.get(i).stop();
        }
    	allPlaying = true;
    }

    //Adjust the volume for all the selected clips
    public void volumeForAll(float changeVolume){
    	for (int i = 0; i < clips.size(); i++){
    		edit.volume(changeVolume, clips.get(i).getClip());
    	}
    }

    // Adjusts the pan of stereo audio
    public void panForAll(float changePan){
    	for (int i = 0; i < clips.size(); i++){
    		edit.panControl(changePan, clips.get(i).getClip());
    	}
    }
}
