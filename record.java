/*
Author: Danny Lu
Project: CIS 422 Project 2: Music Maker

Functions: start(), finish(), getFormat()
reference the Module Interface Specification to learn more about
how to use each function.

This file mainly handles connecting to microphone and recoring audio.
This file works to also specify the name of the audio file when saving.
*/
import javax.sound.sampled.*;
import java.io.*;
import java.util.Scanner;

public class record{
    //Set audio file format
    static AudioFileFormat.Type type = AudioFileFormat.Type.WAVE;
    //Specify where the audio will be recorded from
    static TargetDataLine line;
    //path to save audio file
    static File file;
    //Used to determine when to record and when to not record
    Thread recorder;

    static AudioFormat getFormat(){
        float sampleRate = 16000;
        int sampleSizeInBits = 8;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
        return format;
    }

    //Opens audio stream and starts the audio stream to record audio frequencies from the microphone, recods audio into a null file which can later be renamed.
    public static void start(){
        try{
            file = new File("null.wav");
            AudioFormat recordFormat = getFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, recordFormat);

            //Check if data link works
            if(!AudioSystem.isLineSupported(info)){
                System.out.println("Line not supported");
                System.exit(0);
            }
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open();
            AudioInputStream inStream = new AudioInputStream(line);
            line.start();
            System.out.println("Start Recording");
            Thread recorder = new Thread(){
                @Override
                public void run(){
                    try{
                        AudioSystem.write(inStream, type, file);
                    }catch(IOException ex){
                        ex.printStackTrace();
                    }
                }
            };
            recorder.start();
        } catch(Exception e){
            System.out.println(e);
        }
    }

    //Stops audio stream from recording frequeiencies and closes the audio stream.
    public static void finish(){
        //Stops the mic from recording anymore audio
        line.stop();
        //Close the input stream from the microphone
        System.out.println("Stop Recording");
        line.close();
    }
}
