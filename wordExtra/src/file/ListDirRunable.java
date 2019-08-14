package file;

import extra.Extra;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static file.MyFile.filter;
import static file.MyFile.readFile;

public class ListDirRunable implements Runnable {
    public static ThreadPoolExecutor es;
    public static FileFactory ff ;
    public static int count = 0;
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
                    es.submit(new ListDirRunable(file));
                else if (file.isFile()) {
                    count++;
                    //System.out.println("\nfileCount = " + count);
                    ff.main(file);
                }
            }
        }
    }
}
