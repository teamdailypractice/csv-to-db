import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import info.dailypractice.DbStatements;
import info.dailypractice.TableConfiguration;
import org.junit.jupiter.api.Test;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class DbStatementsTest {

    @Test
    public void jsonStringToPageConfigurationObject() {
        TableConfiguration tc = new TableConfiguration();
        tc.setTableName("employee");
        Map<String, String> field1 = new HashMap<>();
        field1.put("name", "id");
        field1.put("type", "INTEGER");
        field1.put("length", "-1");
        Map<String, String> field2 = new HashMap<>();
        field2.put("name", "name");
        field2.put("type", "VARCHAR");
        field2.put("length", "255");
        tc.setFields(List.of(field1, field2));

        List<String> primaryKey = List.of("id");
        tc.setPrimaryKey(primaryKey);
        String sqlCreateTable = DbStatements.getSqlCreateTable(tc);

        //CREATE TABLE USER (ID INT, NAME VARCHAR(50));
        assertEquals("CREATE TABLE employee (id INTEGER, name VARCHAR(255), PRIMARY KEY(id));", sqlCreateTable);
    }
}

