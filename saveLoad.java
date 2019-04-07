/*
Author: Danny Lu
Project: CIS 422 Project 2: Music Maker

Functions: save(), load()
reference the Module Interface Specification to learn more about
how to use each function.

This file mainly handles the UI for the user to locate files and look
through directories.
*/
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;

import javax.swing.JFileChooser;

public class saveLoad{
    //Save recording to the computer and checks if there's another file that has the same name
    public static String save() throws IOException{

    	JFileChooser fileChooser = new JFileChooser();
		File selectedFile = null;
	    int returnValue = fileChooser.showOpenDialog(null);
	    if (returnValue == JFileChooser.APPROVE_OPTION) {
	        selectedFile = fileChooser.getSelectedFile();
	    }
	    String fileName = selectedFile.getName();

		File file = new File(fileName);
		try {
			Files.copy(selectedFile.toPath(), file.toPath());
		} catch (FileAlreadyExistsException e1) {
			System.out.println("song already exist");
		}

        return fileName;
    }

    //Gets the name/path of a file and returns it as a string. User gets to search through directories with a built in UI to get teh filename
    public static String load() throws IOException{
    	JFileChooser fileChooser = new JFileChooser();
        fileChooser.showOpenDialog(null);
        String fileName = fileChooser.getSelectedFile().getAbsolutePath();

        return fileName;
    }
}
