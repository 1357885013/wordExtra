package file;

import util.ThreadPool;

import java.io.File;
import java.util.concurrent.locks.ReentrantLock;

public class ListDirRunable implements Runnable {
    public static FileAction action;
    public static int count = 0;
    public static ReentrantLock lock = new ReentrantLock();
    private File dir;

    public ListDirRunable(File dir) {
        this.dir = dir;
    }

    @Override
    public void run() {
        if (!dir.exists())
            throw new IllegalArgumentException("目录：" + dir + "不存在.");
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException(dir + "不是目录。");
        }

        File[] files = dir.listFiles();

        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isDirectory())
                    ThreadPool.pool.submit(new ListDirRunable(file));
                else if (file.isFile()) {
                    try {
                        action.main(file);  //调用对文件的操作
                        lock.lock();
                        count++;
                    } finally {
                        lock.unlock();
                    }
                    //System.out.println("\nfileCount = " + count);
                }
            }
        }
    }
}

//ListDirRunable.es.getTaskCount() = 3831
//        ListDirRunable.es.getCompletedTaskCount() = 3830
//        fileCount = 31498
//        words count = 25639