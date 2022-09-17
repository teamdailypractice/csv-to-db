import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import info.dailypractice.TableConfiguration;
import org.junit.jupiter.api.Test;


import java.util.Map;


import static org.junit.jupiter.api.Assertions.assertEquals;


public class TableConfigurationTests {

    @Test
    public void jsonStringToPageConfigurationObject() {
        String data = "{\n" +
                "        \"tableName\": \"employee\",\n" +
                "        \"fields\": [\n" +
                "            {\n" +
                "                \"name\": \"id\",\n" +
                "                \"type\": \"INTEGER\",\n" +
                "                \"length\": \"-1\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"name\": \"name\",\n" +
                "                \"type\": \"VARCHAR\",\n" +
                "                \"length\": \"255\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }";

        TableConfiguration tableConfiguration = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            tableConfiguration = mapper.readValue(data, TableConfiguration.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(tableConfiguration.toString());
        assertEquals("employee", tableConfiguration.getTableName());
        assertEquals(2, tableConfiguration.getFields().size());

        Map<String, String> firstFieldSchema = tableConfiguration.getFields().get(0);
        assertEquals(firstFieldSchema.get("name"), "id");
        assertEquals(firstFieldSchema.get("type"), "INTEGER");
        assertEquals(firstFieldSchema.get("length"), "-1");

        Map<String, String> secondFieldSchema = tableConfiguration.getFields().get(1);
        assertEquals(secondFieldSchema.get("name"), "name");
        assertEquals(secondFieldSchema.get("type"), "VARCHAR");
        assertEquals(secondFieldSchema.get("length"), "255");
        assertEquals(1, 1);

    }
}
