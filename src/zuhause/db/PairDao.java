package zuhause.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Eduardo Folly
 */
public class PairDao {

    private final DbConfig dbConfig;

    /**
     *
     * @param dbConfig
     */
    public PairDao(DbConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    /**
     *
     * @param table
     * @param key
     * @param value
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public boolean insert(String table, String key, String value)
            throws SQLException, ClassNotFoundException {

        try (Connection connection = DbUtil.getConnection(dbConfig)) {

            String query = "INSERT INTO `pairs` "
                    + "(`tab`, `key`, `value`) VALUES (?, ?, ?);";

            try (PreparedStatement prepareStatement = connection.prepareStatement(query)) {
                prepareStatement.setString(1, table);
                prepareStatement.setString(2, key);
                prepareStatement.setString(3, value);

                return prepareStatement.execute();
            }

        }
    }

    /**
     *
     * @param pair
     * @return boolean
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public boolean saveOrUpdate(Pair pair)
            throws ClassNotFoundException, SQLException {

        try (Connection connection = DbUtil.getConnection(dbConfig)) {
            if (pair.getId() > 0) {
                String query = "UPDATE `pairs` "
                        + "SET `tab` = ?, `key` = ?, `value` = ? "
                        + "WHERE `id` = ?";

                try (PreparedStatement prepareStatement = connection.prepareStatement(query)) {

                    prepareStatement.setString(1, pair.getTab());
                    prepareStatement.setString(2, pair.getKey());
                    prepareStatement.setString(3, pair.getValue());
                    prepareStatement.setLong(4, pair.getId());

                    return prepareStatement.executeUpdate() > 0;
                }
            } else {
                String query = "INSERT INTO `pairs` "
                        + "(`tab`, `key`, `value`) VALUES (?, ?, ?);";

                try (PreparedStatement prepareStatement = connection.prepareStatement(query)) {
                    prepareStatement.setString(1, pair.getTab());
                    prepareStatement.setString(2, pair.getKey());
                    prepareStatement.setString(3, pair.getValue());

                    return prepareStatement.execute();
                }
            }
        }
    }

    /**
     *
     * @param tab
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public List<Pair> selectTab(String tab)
            throws ClassNotFoundException, SQLException {

        return select("tab = ?", new String[]{tab});
    }

    /**
     *
     * @param tab
     * @param key
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public String getValue(String tab, String key)
            throws ClassNotFoundException, SQLException {

        List<Pair> pairs = select("`tab` = ? AND `key` = ?",
                new String[]{tab, key}, "`when` DESC", "1");

        if (pairs.isEmpty()) {
            return null;
        }

        return pairs.get(0).getValue();
    }

    /**
     *
     * @param whereClause
     * @param whereArgs
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public List<Pair> select(String whereClause, String[] whereArgs)
            throws ClassNotFoundException, SQLException {

        return select(whereClause, whereArgs, null, null);
    }

    /**
     *
     * @param whereClause
     * @param whereArgs
     * @param orderBy
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public List<Pair> select(String whereClause, String[] whereArgs,
            String orderBy)
            throws ClassNotFoundException, SQLException {

        return select(whereClause, whereArgs, orderBy, null);
    }

    /**
     *
     * @param whereClause
     * @param whereArgs
     * @param orderBy
     * @param limit
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public List<Pair> select(String whereClause, String[] whereArgs,
            String orderBy, String limit)
            throws ClassNotFoundException, SQLException {

        boolean where = false;

        try (Connection connection = DbUtil.getConnection(dbConfig)) {

            StringBuilder query = new StringBuilder();
            query.append("SELECT * FROM `pairs`");

            if (whereClause != null && !whereClause.isEmpty()) {
                query.append(" WHERE ");
                query.append(whereClause);
                where = true;
            }

            if (orderBy != null && !orderBy.isEmpty()) {
                query.append(" ORDER BY ");
                query.append(orderBy);
            }

            if (limit != null && !limit.isEmpty()) {
                query.append(" LIMIT ");
                query.append(limit);
            }

            try (PreparedStatement prepareStatement = connection
                    .prepareStatement(query.toString())) {

                if (where) {
                    int i = 1;
                    for (String arg : whereArgs) {
                        prepareStatement.setString(i, arg);
                        i++;
                    }
                }

                ResultSet rs = prepareStatement.executeQuery();

                List<Pair> lista = new ArrayList();

                while (rs.next()) {
                    Pair pair = new Pair();
                    pair.setId(rs.getLong("id"));
                    pair.setTab(rs.getString("tab"));
                    pair.setKey(rs.getString("key"));
                    pair.setValue(rs.getString("value"));
                    pair.setWhen(rs.getTimestamp("when"));

                    lista.add(pair);
                }

                return lista;
            }
        }
    }
}
