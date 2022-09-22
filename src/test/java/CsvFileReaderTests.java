import info.dailypractice.CsvFileReader;
import info.dailypractice.TableConfiguration;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static org.junit.jupiter.api.Assertions.assertEquals;


public class CsvFileReaderTests {

    @Test
    public void csvWithHeaderWithoutTrailer() {
        String filepath = "src/test/resources/employee.csv";
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
        tc.setPrimaryKey(List.of("id"));
        try {
            CsvFileReader reader = new CsvFileReader(filepath, tc);
            int rowsCount = reader.getValidRows().size();
            assertEquals(3, rowsCount);
        } catch (IOException e) {
            assertEquals(1, -1);
        }
    }
}
