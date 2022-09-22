package info.dailypractice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DbStatementsProvider {
    private static Logger LOG = LoggerFactory
            .getLogger(DdlExecutor.class);

    public DbStatementsProvider() {
    }

    //CREATE TABLE USER (ID INT, NAME VARCHAR(50));
    private String getSqlPrimaryKey(List<String> primaryKey) {
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

    private String getSqlFieldSchema(List<Map<String, String>> fields) {
        StringBuilder fieldDefinition = new StringBuilder();
        for (Map<String, String> field : fields) {
            String name = field.get("name");
            String type = field.get("type");
            String length = field.get("length");

            if (length.equals("-1")) {
                fieldDefinition.append(name + " " + type);
            } else {
                fieldDefinition.append(name + " " + type + "(" + length + ")");
            }
            fieldDefinition.append(", ");
        }
        return fieldDefinition.toString();
    }

    private boolean validateRowDataWithSchema(List<Map<String, String>> fields, String[] data) {
        boolean isValidRow = true;
        //length
        if (fields.size() != data.length) {
            isValidRow = false;
        }

        return isValidRow;

    }

    private String getSqlStringForValue(String input) {
        String escapeSingleQuotes = input.replace("'", "\'");
        String escapeDoubleQuotes = escapeSingleQuotes.replace('"', '\"');
        //TODO: Any other characters to be handled?
        return "'" + escapeDoubleQuotes + "'";
    }

    private String getSqlFieldValues(List<Map<String, String>> fields, String[] data) throws Exception {
        StringBuilder fieldValues = new StringBuilder();
        //'' NULL null  => handling for String data type field and other data type fields
        if (validateRowDataWithSchema(fields, data)) {
            for (int i = 0; i < fields.size(); i++) {
                Map<String, String> field = fields.get(i);
                String name = field.get("name");
                String type = field.get("type");
                String fieldValue = data[i].trim();
                //VARCHAR
                //INTEGER
                String finalValue = "";
                if (type.equals("INTEGER")) {
                    if (fieldValue.length() > 0) {
                        finalValue = fieldValue;
                    } else {
                        finalValue = "NULL";
                        //TODO: Is there any default value in configuration file for INTEGER
                    }
                }
                if (type.equals("VARCHAR")) {
                    if (fieldValue.length() > 0 && (!fieldValue.equalsIgnoreCase("NULL"))) {
                        finalValue = getSqlStringForValue(fieldValue);

                    } else {
                        finalValue = "NULL";
                        //TODO: Is there any default value in configuration file for VARCHAR
                    }
                }
                fieldValues.append(finalValue);
                fieldValues.append(",");
            }

        } else {
            throw new Exception("Row data - columns mismatch");
        }
        return fieldValues.substring(0, fieldValues.length() - 1);
    }

    public String getSqlCreateTable(TableConfiguration tc) {
        String CREATE_TABLE = "CREATE TABLE ";
        String fieldSchemaSql = getSqlFieldSchema(tc.getFields());
        String primaryKeySql = getSqlPrimaryKey(tc.getPrimaryKey());
        return CREATE_TABLE + tc.getTableName() + " (" + fieldSchemaSql + primaryKeySql + ");";
    }

    public String getSqlInsertInto(TableConfiguration tc, String[] data) throws Exception {
        String INSERT_INTO_TABLE = "INSERT INTO ";
        String fieldValues = getSqlFieldValues(tc.getFields(), data);
        String sqlInsertStatement = INSERT_INTO_TABLE + tc.getTableName() + " VALUES (" + fieldValues + ");";
        LOG.info(sqlInsertStatement);
        return sqlInsertStatement;
    }
}
