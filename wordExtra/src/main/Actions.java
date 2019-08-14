package main;

import extra.Extra;
import file.FileFactory;
import file.ListDirRunable;
import file.MyFile;

import java.awt.*;
import java.io.*;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static file.MyFile.filter;
import static file.MyFile.readFile;

public class Actions {
    static void extraFile(TextArea textArea, FileDialog openDialog) {
        textArea.setRows(0);
        textArea.append(readFile(MyFile.selectFile(openDialog)) + "\n");
        Extra.s.clear();
        Extra.extra(textArea.getText());
        textArea.append(Extra.s.toString() + "\n");
    }

    static void extraFold(TextArea textArea) {
        Extra.s.clear();
        java.io.File fold = MyFile.selectFold();
        textArea.append("Extra fold : " + fold.toString() + "\n");
        //调用 多线程递归方法。
        //初始化线程池
        ListDirRunable.es = new ThreadPoolExecutor(10, 20, 2, TimeUnit.MINUTES, new SynchronousQueue<>());
        FileFactory ff = file -> {
            if (filter(file.getName())) {
                Extra.extra(readFile(file));
                System.out.println(file);
            }
            if(ListDirRunable.es.getTaskCount()==0 && ListDirRunable.es.getActiveCount()==0){
                extraFoldEnd(textArea);
            }
            return null;
        };
        ListDirRunable.ff = ff;
        ListDirRunable run = new ListDirRunable(fold);//创建任务
        ListDirRunable.es.submit(run); //提交任务
    }

    static void extraFoldEnd(TextArea textArea) {
        //System.out.println("the count of extraed" + count);
        System.out.println("\nfileCount = " + ListDirRunable.count);

        textArea.append(Extra.s.toString() + "\n");
        textArea.append("words count = " + Extra.s.size() + "\n");
        //textArea.append("file count = " + count + "\n\n\n");

        System.out.println("down");
    }

    static void extraText(TextArea textArea) {
        Extra extra = new Extra();
        textArea.setRows(0);
        Extra.s.clear();
        Extra.extra(textArea.getText());
        textArea.append(Extra.s.toString() + "\n");
    }

    static File saveFile(File file, TextArea textArea, FileDialog saveDialog) {
        if (file == null) {
            saveDialog.setVisible(true);
            String dirPath = saveDialog.getDirectory();
            String fileName = saveDialog.getFile();
            if (dirPath == null || fileName == null)
                return null;
            file = new File(dirPath, fileName);
        }
        try {
            BufferedWriter bufw = new BufferedWriter(new FileWriter(file));
            String text = textArea.getText();
            bufw.write(text);
            bufw.close();
        } catch (IOException e2) {
            throw new RuntimeException("保存失败");
        } finally {
            return file;
        }
    }

    static File openFile(File file, TextArea textArea, FileDialog openDialog) {
        openDialog.setVisible(true);
        String dirPath = openDialog.getDirectory();
        String fileName = openDialog.getFile();
        System.out.println(dirPath + "...." + fileName);
        if (dirPath == null || fileName == null)
            return null;
        textArea.setText("");
        file = new File(dirPath, fileName);
        //dir = null;
        try {
            BufferedReader bufr = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = bufr.readLine()) != null) {
                textArea.append(line + "\r\n");

            }
            bufr.close();
        } catch (IOException e2) {
            throw new RuntimeException("打开异常");
        } finally {
            return file;
        }
    }

    static void openFold(TextArea textArea) {
//        textArea.setText("");
//        MyFile.listDirectory(MyFile.selectFold(), new FileFactory() {
//            int count = 0;
//
//            @Override
//            public Object main(File file) {
//                textArea.append(String.valueOf(file) + "\n");
//                return null;
//            }
//        });
//        System.out.println("done");
    }
}
