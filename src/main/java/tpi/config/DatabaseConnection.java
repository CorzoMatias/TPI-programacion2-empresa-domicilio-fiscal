package tpi.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DatabaseConnection {
    private static final String PROP_FILE = "application.properties";
    private static String url, user, password, driver;

    static {
        try (InputStream is = DatabaseConnection.class.getClassLoader().getResourceAsStream(PROP_FILE)) {
            Properties props = new Properties();
            if (is == null) throw new IllegalStateException("No se encontró " + PROP_FILE + " en resources/");
            props.load(is);
            driver = props.getProperty("db.driver");
            url = props.getProperty("db.url");
            user = props.getProperty("db.user");
            password = props.getProperty("db.password");
            if (driver != null && !driver.isBlank()) Class.forName(driver);
        } catch (IOException e) {
            throw new RuntimeException("Error leyendo properties", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver JDBC no encontrado", e);
        }
    }

    private DatabaseConnection() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
