/*
Author: Yanhua Luo
Project: CIS 422 Project 2: Music Maker

Functions: ain()
reference the Module Interface Specification to learn more about
how to use each function.

Launches the frame and the whole application
*/
import java.awt.EventQueue;

public class main{
    //Launch the application.
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    frame1 frame = new frame1();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
