package com.qin;

import java.io.*;
import javax.swing.*;

public class TextFileReader {

    private JFileChooser chooser = null;
    private File textFile = null;

    public TextFileReader()
    {
        chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setFileHidingEnabled(true);
        chooser.setMultiSelectionEnabled(false);
        chooser.setDialogTitle("Please choose a JSON file");
    }

    public boolean chooseFile()
    {
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            textFile = chooser.getSelectedFile();
            return true;
        }
        return false;
    }

    public String read()
    {
        FileInputStream fs = null;
        try {
            byte[] buff = new byte[(int)textFile.length()];
            fs = new FileInputStream(textFile);
            fs.read(buff);
            fs.close();
            String s = new String(buff, "UTF-8");
            return s;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}