package info.dailypractice;

import java.util.List;
import java.util.Map;

public class DbStatements {
    //CREATE TABLE USER (ID INT, NAME VARCHAR(50));
    private static String getSqlPrimaryKey(List<String> primaryKey) {
        // , PRIMARY KEY(CLASS_CODE, DAY)
        StringBuilder primaryKeyDefinition = new StringBuilder();
        primaryKeyDefinition.append("PRIMARY KEY(");
        for (String key : primaryKey) {
            primaryKeyDefinition.append(key + ", ");
        }
        //Delete last blank space
        primaryKeyDefinition.deleteCharAt(primaryKeyDefinition.length() - 1);
        //Delete last comma
        primaryKeyDefinition.deleteCharAt(primaryKeyDefinition.length() - 1);

        primaryKeyDefinition.append(")");
        return primaryKeyDefinition.toString();
    }

    private static String getSqlFieldSchema(List<Map<String, String>> fields) {
        StringBuilder fieldDefinition = new StringBuilder();
        for (Map<String, String> field : fields) {
            String name = field.get("name");
            String type = field.get("type");
            String length = field.get("length");
            fieldDefinition.append(name + " " + type);
            if (length != "-1") {

                fieldDefinition.append("(" + length + ")");
            }
            fieldDefinition.append(", ");
        }
        return fieldDefinition.toString();
    }

    public static String getSqlCreateTable(TableConfiguration tc) {
        String CREATE_TABLE = "CREATE TABLE ";
        String fieldSchemaSql = getSqlFieldSchema(tc.getFields());
        String primaryKeySql = getSqlPrimaryKey(tc.getPrimaryKey());
        return CREATE_TABLE + tc.getTableName() + " (" + fieldSchemaSql + primaryKeySql + ");";
    }
}
