package com.qin;

import java.io.*;
import javax.swing.*;

public class TextFileWriter {

    private JFileChooser chooser = null;
    private File textFile = null;

    public TextFileWriter()
    {
        chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setFileHidingEnabled(true);
        chooser.setMultiSelectionEnabled(false);
        chooser.setDialogTitle("Please choose a location");
    }

    public boolean chooseFile()
    {
        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            textFile = chooser.getSelectedFile();
            return true;
        }
        return false;
    }

    public void write(String s)
    {
        try {
            FileWriter writer = new FileWriter(textFile);
            writer.write(s);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}