package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPool {
    public static int count = 0;
    public static int aliveCount = 0;
    public static Hashtable<PreparedStatement, Boolean> contents = new Hashtable<>();
    public static ReentrantLock lock = new ReentrantLock();
    static Condition condition = lock.newCondition();

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            for (int i = 1; i <= 8; i++) {  //连接的数量
                addOne();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static PreparedStatement getOne() {
        PreparedStatement p = null;
        lock.lock();
        while (aliveCount == 0)
            try {
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            contents.forEach((k, v) -> {
//                if (v) {
//                    p[0] = k; //lambda 里只能操作引用类型，因为是变量副本
//                    v = false;
//                    aliveCount--; //不能自主结束遍历，不用lamabda了。
//                }
//            });
        for (Map.Entry<PreparedStatement, Boolean> entry : contents.entrySet()) {
            if (entry.getValue()) {
                p = entry.getKey(); //lambda 里只能操作引用类型，因为是变量副本
                entry.setValue(false);
                aliveCount--; //不能自主结束遍历，不用lamabda了。
                break;
            }
        }
        lock.unlock();
        //System.out.println("获得了一个连接");
        return p;
    }

    public static boolean addOne() {
        try {
            long timeBegin=System.nanoTime();
            Connection con = DriverManager.getConnection("JDBC:Mysql://localhost:3306/word", "root", "root");
            System.out.println("System.nanoTime()-timeBegin = " + ((System.nanoTime() - timeBegin)/1000000F));
            timeBegin=System.nanoTime();
            PreparedStatement p = con.prepareStatement("select word from english_words where word = ?");
            System.out.println("System.nanoTime()-timeBegin = " + ((System.nanoTime() - timeBegin)/1000000F));
            contents.put(p, true);
            lock.lock();
            count++;
            aliveCount++;
            lock.unlock();
            System.out.println("创建了一个新连接");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean realeaseOne(PreparedStatement p) {
        for (Map.Entry<PreparedStatement, Boolean> entry : contents.entrySet()) {
            if (p == entry.getKey()) {
                entry.setValue(true);
                lock.lock();
                aliveCount++;
                condition.signal();
                lock.unlock();
                //System.out.println("释放了一个连接");
                return true;
            }
        }
        return false;
    }
}
