package main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static main.Actions.*;

/**
 * 实现菜单栏
 */
public class TextNote {
    private Frame frame;
    private MenuItem closeItem, openItem, openFoldItem, saveItem, subItem1, subItem, extraFile, extraFold, extraTextArea, endExtra;
    private FileDialog openDialog, saveDialog;

    private TextArea textArea;
    private java.io.File file, dir;

    private TextNote() {
        init();
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        new TextNote();
    }

    private void init() {
        frame = new Frame("text note");
        frame.setBounds(300, 100, 800, 500);
        //frame.setLayout();

        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("file");

        closeItem = new MenuItem("exit");
        openItem = new MenuItem("open file");
        openFoldItem = new MenuItem("open directory");
        saveItem = new MenuItem("save");

        Menu subMenu = new Menu("new");
        subItem1 = new MenuItem("Web Project");
        subItem = new MenuItem("Java Project");
        subMenu.add(subItem);
        subMenu.add(subItem1);
        menu.add(subMenu);

        menu.add(openItem);
        menu.add(openFoldItem);
        menu.add(saveItem);
        menu.add(closeItem);
        menuBar.add(menu);

        Menu menuExtra = new Menu("Extra words");
        extraFile = new MenuItem("extra file");
        extraFold = new MenuItem("extra fold");
        extraTextArea = new MenuItem("extra the content which in text area");
        endExtra = new MenuItem("end extra");
        menuExtra.add(extraFile);
        menuExtra.add(extraFold);
        menuExtra.add(extraTextArea);
        menuExtra.add(endExtra);
        menuBar.add(menuExtra);

        openDialog = new FileDialog(frame, "我要打开", FileDialog.LOAD);
        saveDialog = new FileDialog(frame, "我要保存", FileDialog.SAVE);

        textArea = new TextArea();

        frame.add(textArea);
        frame.setMenuBar(menuBar);
        myEvent();

        frame.setVisible(true);
    }

    private void myEvent() {
        //解析文件
        extraFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                extraFile(textArea, openDialog);
            }
        });
        //解析目录
        extraFold.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                extraFold(textArea);
            }
        });
        //解析文本
        extraTextArea.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                extraText(textArea);
            }
        });
        endExtra.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                extraFoldEnd(textArea);
            }
        });

        saveItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                file = saveFile(file, textArea, saveDialog);
            }
        });

        //打开菜单
        openItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                file = openFile(file, textArea, openDialog);
            }
        });

        //打开目录
        openFoldItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                openFold(textArea);
            }
        });

        closeItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                System.exit(0);
            }
        });

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}

