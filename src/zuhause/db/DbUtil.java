package zuhause.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Eduardo Folly
 */
public class DbUtil {

    public static Connection getConnection(DbConfig config) throws ClassNotFoundException, SQLException, Exception {
        Class.forName(config.getDriver());

        try {
            return DriverManager.getConnection(config.getUrl(),
                    config.getUser(), config.getPassword());
        } catch (SQLException e) {
            throw new Exception("Erro ao conectar com o servidor.", e);
        }
    }

    /**
     *
     * @param rs
     */
    public static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception ex) {

            }
        }
    }

    /**
     *
     * @param s
     */
    public static void close(Statement s) {
        if (s != null) {
            try {
                s.close();
            } catch (Exception ex) {

            }
        }
    }

    /**
     *
     * @param c
     */
    public static void close(Connection c) {
        if (c != null) {
            try {
                c.close();
            } catch (Exception ex) {

            }
        }
    }
}
