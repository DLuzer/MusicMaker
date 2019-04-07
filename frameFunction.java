/*
Author: Yanhua Luo
Project: CIS 422 Project 2: Music Maker

Functions: getSongLength(), convertToMinHour(), paintComponent(), MyTreeCellRenderer(), getBackground()
reference the Module Interface Specification to learn more about
how to use each function.

This file contains micellaneous functions that is used in Frame1. These functions are used to display selected audio clips and
trivial getters and conversions that are needed.
*/
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.File;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

public class frameFunction {
    public static Color selectedColor = new Color(115,164,209);

    public static double getSongLength(String songName){
    	File file = new File(songName+".wav");
    	double na = file.length() / 16000 / 2.0;
    	Math.round(na);

    	return na;
    }

    public static String convertToMinHour(int numb){
        int sec = numb % 60;
        int min = (numb / 60)%60;
        String result = String.format("%02d:%02d", min, sec);

        return result;
    }

    //Functions for overriding the paintComonent of JTree. Mainly use to change the color and length when selecting clip
    public static class MyTree extends JTree {
        protected void paintComponent(Graphics g) {
             // paint background
             g.setColor(getBackground());
             g.fillRect(0, 0, getWidth(), getHeight());

             //Paint selected node's background and border
             int fromRow = getRowForPath( getSelectionPath());
             if (fromRow != -1) {
                  int toRow = fromRow + 1;
                  Rectangle fromBounds = getRowBounds(fromRow);
                  Rectangle toBounds = getRowBounds(toRow - 1);
                  if (fromBounds != null && toBounds != null) {
                       g.setColor(selectedColor);
                       g.fillRect(0, fromBounds.y, getWidth(), toBounds.y - fromBounds.y + toBounds.height);
                       g.setColor(selectedColor);
                       g.drawRect(0, fromBounds.y, getWidth() - 1, toBounds.y - fromBounds.y + toBounds.height);
                  }
             }
             //Perform operation of superclass
             setOpaque(false); //Trick not to paint background
             super.paintComponent(g);
        }
   }

    public static class MyTreeCellRenderer extends DefaultTreeCellRenderer {
        public MyTreeCellRenderer() {
             setBackgroundNonSelectionColor(null);
        }
        public Color getBackground() {
             return null;
        }
   }
}
