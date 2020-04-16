package db;

import org.apache.commons.dbcp.BasicDataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

public class DBUtils {
    private static String driver;
    private static String url;
    private static String username;
    private static String password;
    private static int initialSize;
    private static int maxActive;

    private static BasicDataSource ds;

    static{
        try {
            Properties cfg = new Properties();
            InputStream input = DBUtils.class.getClassLoader().getResourceAsStream("db.properties");
            cfg.load(input);
            driver = cfg.getProperty("jdbc.driver");
            url = cfg.getProperty("jdbc.url");
            username = cfg.getProperty("jdbc.username");
            password = cfg.getProperty("jdbc.password");
            initialSize = Integer.valueOf(cfg.getProperty("initialSize"));
            maxActive = Integer.valueOf(cfg.getProperty("maxActive"));

            ds = new BasicDataSource();
            ds.setDriverClassName(driver);
            ds.setUrl(url);
            ds.setUsername(username);
            ds.setPassword(password);
            ds.setInitialSize(initialSize);
            ds.setMaxActive(maxActive);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Connection getConnection(){
        Connection conn = null;
        try{
            conn = ds.getConnection();
            return conn;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("获取错误");
        }
    }

    public static void close(Connection conn){
        if(conn != null){
            try {
                conn.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
