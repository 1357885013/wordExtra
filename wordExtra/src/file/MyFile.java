package file;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MyFile implements IMyFile {

    public static File selectFold() {
        File dir = null;
        JFileChooser jf = new JFileChooser();
        //jf.setSelectedFile(new File("c:\\我的报表.xls"));
        jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int value = jf.showSaveDialog(null);
        jf.setFileHidingEnabled(false);
        if (value == JFileChooser.APPROVE_OPTION) {
            dir = jf.getSelectedFile();
        }
        return dir;
    }

    public static File selectFile(FileDialog openDialog) {
        openDialog.setVisible(true);
        String dirPath = openDialog.getDirectory();
        String fileName = openDialog.getFile();
        System.out.println(dirPath + "...." + fileName);
        if (dirPath == null || fileName == null)
            return null;
        else
            return new File(dirPath, fileName);

    }

    public static String readFile(File file) {
        String s = "";
        try {
            BufferedReader bufr = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = bufr.readLine()) != null) {
                s += (line + " ");
            }
            bufr.close();
        } catch (IOException e2) {
            throw new RuntimeException("打开异常");
        }
        return s;
    }

    public static boolean filter(String fileName) {
        if (fileName.contains(".")) {
            String[] strs = fileName.split("\\.");
            fileName = strs[strs.length - 1].toLowerCase();
            switch (fileName) {
                case "txt":
                case "java":
                case "html":
                case "css":
                case "js":
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }
}
