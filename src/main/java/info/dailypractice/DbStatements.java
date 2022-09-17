package info.dailypractice;

import java.util.List;
import java.util.Map;

public class DbStatements {
    //CREATE TABLE USER (ID INT, NAME VARCHAR(50));
    private static String getPrimaryKeySql(List<String> primaryKey) {
        return "";
    }

    private static String getFieldSchemaSql(List<Map<String, String>> fields) {
        return "";
    }

    public static String getCreateTableSql(TableConfiguration tc) {
        String CREATE_TABLE = "CREATE TABLE ";
        String fieldSchemaSql = getFieldSchemaSql(tc.getFields());
        String primaryKeySql = getPrimaryKeySql(tc.getPrimaryKey());
        return CREATE_TABLE + tc.getTableName() + " (" + fieldSchemaSql + primaryKeySql + ");";
    }
}
