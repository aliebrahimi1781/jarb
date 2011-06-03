package org.jarb.constraint.database.column;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;

/**
 * Retrieves column meta data using JDBC.
 * 
 * @author Jeroen van Schagen
 * @since 30-05-2011
 */
public class JdbcColumnMetadataProvider implements ColumnMetadataProvider {
    private final DataSource dataSource;

    /**
     * Construct a new {@link JdbcColumnMetadataProvider}.
     * @param dataSource data source reference
     */
    public JdbcColumnMetadataProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ColumnMetadata> all() {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet columnResultSet = metaData.getColumns(null, null, null, null);
            return extractColumnInfo(columnResultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Extract column constraint information from our result set.
     * @param resultSet the result set containing our information
     * @return list of meta data extracted from the result set
     * @throws SQLException if any exception occurs
     */
    private List<ColumnMetadata> extractColumnInfo(ResultSet resultSet) throws SQLException {
        List<ColumnMetadata> columnMetaData = new ArrayList<ColumnMetadata>();
        while (resultSet.next()) {
            columnMetaData.add(mapToColumnMetadata(resultSet));
        }
        return columnMetaData;
    }

    /**
     * Convert a row inside the result set to column meta data.
     * @param resultSet the result set containing our information
     * @return row content as meta data
     * @throws SQLException if any exception occurs
     */
    private ColumnMetadata mapToColumnMetadata(ResultSet resultSet) throws SQLException {
        String schemaName = resultSet.getString("TABLE_SCHEM");
        if (schemaName == null) {
            // Some databses provide a catalog name, rather than a schema name, use this name
            schemaName = resultSet.getString("TABLE_CAT");
        }
        String tableName = resultSet.getString("TABLE_NAME");
        String columnName = resultSet.getString("COLUMN_NAME");
        ColumnReference columnReference = new ColumnReference(schemaName, tableName, columnName);
        ColumnMetadata columnMetadata = new ColumnMetadata(columnReference);
        columnMetadata.setDefaultValue(resultSet.getString("COLUMN_DEF"));
        columnMetadata.setMaximumLength(asInteger(resultSet, "COLUMN_SIZE"));
        columnMetadata.setFractionLength(asInteger(resultSet, "DECIMAL_DIGITS"));
        columnMetadata.setRadix(asInteger(resultSet, "NUM_PREC_RADIX"));
        columnMetadata.setRequired(resultSet.getString("IS_NULLABLE").equals("NO"));
        columnMetadata.setAutoIncrement((resultSet.getString("IS_AUTOINCREMENT").equals("YES")));
        return columnMetadata;
    }

    /**
     * Retrieve a column from the result set, as integer. Whenever the column
     * value is {@code null}, it will be returned as {@code null}.
     * @param resultSet the result set containing our information
     * @param columnName name of the numeric column
     * @return column value as integer
     * @throws SQLException if any exception occurs
     */
    public Integer asInteger(ResultSet resultSet, String columnName) throws SQLException {
        String numberAsString = resultSet.getString(columnName);
        if (StringUtils.isBlank(numberAsString)) {
            return null;
        }
        return Integer.parseInt(numberAsString);
    }
}