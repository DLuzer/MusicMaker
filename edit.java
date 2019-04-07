/*
Author: Danny Lu
Project: CIS 422 Project 2: Music Maker

Functions: volume(), panControl(), loop()
reference the Module Interface Specification to learn more about
how to use each function.

This file mainly handles manipulating the audio of specified clips.
*/
import javax.sound.sampled.*;
import javax.swing.JLabel;

import java.awt.BorderLayout;
import java.util.Hashtable;
import java.util.Scanner;



public class edit{

	frame1 frame1 = new frame1();
	//Controls the master volume of the clips
    public static void volume(float volume, Clip editClip){
    	try{
        	FloatControl gainControl = (FloatControl) editClip.getControl(FloatControl.Type.MASTER_GAIN);
    		final float min = gainControl.getMinimum();
    		final float max = gainControl.getMaximum();
    		final float width = max - min;
    		float fval = gainControl.getValue();
    		float i = volume;
    		float f = min + (i*width/1000.0f);

    		if (volume == 0){
    			gainControl.setValue(min);
    		}else if (volume == 100){
    			gainControl.setValue(max);
    		}else if (volume > 50 && volume < 100){
    			float a = (float) ((volume - 50) * 0.12);
    			gainControl.setValue(a);
    		}else {
    			float b = (float) ((50 - volume) * (-1.6));
    			gainControl.setValue(b);
    		}
    	}catch (NullPointerException e){
    		System.out.println("No Song is Play at this Point");
		}
    }

    //Set the audio to play strictly in the left or right or anywhere in between
    public static void panControl(float fps, Clip editClip){
		try{
		   	FloatControl panControl = (FloatControl) editClip.getControl(FloatControl.Type.PAN);
			final float min = panControl.getMinimum();
			final float max = panControl.getMaximum();
			final float width = max - min;

			System.out.println(min);
			float i = fps;
			float f = min + (i*width/1000.0f);
			panControl.setValue(f);
		}catch (NullPointerException e){
    		System.out.println("No Song is Play at this Point");
		}
    }

    //Sets an audio clip to play over and over again. Is not implemented but is left here to potentially be implemented in next interation
    public void loop(Clip editClip){
        editClip.loop(Clip.LOOP_CONTINUOUSLY);
    }
}
