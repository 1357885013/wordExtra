package main;

import extra.Extra;
import file.ListDirRunable;
import file.MyFile;
import util.ThreadPool;

import java.awt.*;
import java.io.*;

import static file.MyFile.filter;
import static file.MyFile.readFile;

public class Actions {
    static void extraFile(TextArea textArea, FileDialog openDialog) {
        textArea.setRows(0);
        textArea.append(readFile(MyFile.selectFile(openDialog)) + "\n");
        Extra.wordMeaningless.clear();
        Extra.wordMeaningful.clear();
        Extra.extra(textArea.getText());
        textArea.append("无意义的词：" + Extra.wordMeaningless.toString() + "\n");
        textArea.append("有意义的词：" + Extra.wordMeaningful.toString() + "\n");
    }

    static void extraFold(TextArea textArea) {
        Extra.wordMeaningless.clear();
        Extra.wordMeaningful.clear();
        java.io.File fold = MyFile.selectFold();
        textArea.append("Extra fold : " + fold.toString() + "\n");
        //调用 多线程递归方法。
        //初始化线程池
        ListDirRunable.count = 0;

        ListDirRunable.action = file -> { //处理每一个文件的方法
            if (filter(file.getName())) {
                Extra.extra(readFile(file));
                System.out.println(ThreadPool.pool.getQueue().size() + "  " + file.getName());
            }

            return null;
        };
        ThreadPool.pool.submit(new ListDirRunable(fold)); //创建提交任务

        while (true)  //死循环监视是否结束，目前还有问题
            if (ThreadPool.pool.getQueue().size() <= 0 && ThreadPool.pool.getActiveCount() == 0 && ThreadPool.pool.getCompletedTaskCount() >= 1) {
                extraFoldEnd(textArea);
                textArea.append("有意义的：" + Extra.wordMeaningless.toString() + "\n");
                textArea.append("无意义的：" + Extra.wordMeaningful.toString() + "\n");
                System.out.println("完成");
                break;
            }
    }

    static void extraFoldEnd(TextArea textArea) { //结束后输出信息
        System.out.println("任务队列任务数 = " + ThreadPool.pool.getQueue().size());
        System.out.println("正在执行任务的线程数 = " + ThreadPool.pool.getActiveCount());
        System.out.println("总任务数量 = " + ThreadPool.pool.getTaskCount());
        System.out.println("完成的任务数量 = " + ThreadPool.pool.getCompletedTaskCount());
        //System.out.println("the count of extraed" + count);
        int count;
        ListDirRunable.lock.lock();
        count = ListDirRunable.count;
        ListDirRunable.lock.unlock();
        System.out.println("\n未过滤文件数 = " + count);

        //textArea.append(Extra.wordMeaningless.toString() + "\n");
        textArea.append("提取出的单词数量：  有意义" + Extra.wordMeaningful.size() + "无意义" + Extra.wordMeaningless.size());
        //textArea.append("file count = " + count + "\n\n\n");

    }

    static void extraText(TextArea textArea) {
        Extra extra = new Extra();
        textArea.setRows(0);
        Extra.wordMeaningless.clear();
        Extra.wordMeaningful.clear();
        Extra.extra(textArea.getText());
        textArea.append("有意义的：" + Extra.wordMeaningless.toString() + "\n");
        textArea.append("无意义的：" + Extra.wordMeaningful.toString() + "\n");
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
