package bank.repositories;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface Repository<T, K>{

    K genNewId();
    List<T> read();
    T readById(K id);
    T create(T item);
    T update(T item);
    boolean delete(T item);
    boolean exists(K id);

    /**
     * Execute Query and return Result
     * @param connection Database connection
     * @param query query string to execute
     * @return ResultSet object if success, else null
     */
    static ResultSet executeQuery(Connection connection, String query) {
        ResultSet resultSet = null;

        try {
            Statement stmt = connection.createStatement();
            resultSet = stmt.executeQuery(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());  // TODO (shubham): Implement logger
        }

        return resultSet;
    }

    /**
     * Execute given query string
     * @param connection Database connection
     * @param query query to execute
     * @return rows affected when update
     * or inserted id when insert
     */
    static int prepareAndExecuteQuery(Connection connection, String query) {
        int id = -1;

        try {
            PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try {
                    ResultSet resultSet = pstmt.getGeneratedKeys();
                    if (resultSet.next()) {
                        id = resultSet.getInt(1);
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return id;
    }

    /**
     * Prepare insert query using the required columns HashMap
     * @param query query part to prepare
     * @param requiredColumns required column & values
     * @return String of prepared query
     */
    static String prepareInsertQuery(StringBuilder query, HashMap<String, String> requiredColumns) {
        List<String> columns = new ArrayList<String>(requiredColumns.keySet());
        query.append(" (").append(String.join(", ", columns)).append(") VALUES (");

        for (String col : columns) {
            query.append("'").append(requiredColumns.get(col)).append("'").append(",");
        }
        query.deleteCharAt(query.length() - 1);
        query.append(");");

        return query.toString();
    }

    /**
     * Prepare update query using columns to update HashMap
     * @param query query part to prepare
     * @param columnToUpdate columns to update
     * @param idColumn id column name
     * @param id id of the record to update
     * @return String of prepared query
     */
    static String prepareUpdateQuery(StringBuilder query, HashMap<String, String> columnToUpdate, String idColumn, int id) {
        List<String> columns = new ArrayList<>(columnToUpdate.keySet());
        query.append(" SET ");

        for (String col : columns) {
            query.append(col).append(" = '").append(columnToUpdate.get(col)).append("', ");
        }
        query.deleteCharAt(query.length() - 1);
        query.deleteCharAt(query.length() - 1);
        query.append("WHERE ").append(idColumn).append(" = '").append(id).append("';");

        return query.toString();
    }

    /**
     * Prepare delete query using id column
     * @param query query part to prepare
     * @param idColumn id column name
     * @param id id of the record to delete
     * @return String of prepared query
     */
    static String prepareDeleteQuery(StringBuilder query, String idColumn, int id) {
        query.append(" WHERE ").append(idColumn).append(" = '").append(id).append("';");
        return query.toString();
    }

}
