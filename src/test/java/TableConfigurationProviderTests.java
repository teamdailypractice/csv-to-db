import info.dailypractice.TableConfiguration;
import info.dailypractice.TableConfigurationProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TableConfigurationProviderTests {

    @Test
    public void shouldHaveOneTableConfiguration() throws IOException {
        String filepath = "src/test/resources/tableSchema.json";
        TableConfigurationProvider tcp = new TableConfigurationProvider();
        assertEquals(tcp.getTableConfiguration(filepath).size(), 1);
        System.out.println(tcp.getTableConfiguration(filepath).get(0));
    }

    @Test
    public void noTableSchemaExistsInJson() throws IOException {
        String filepath = "src/test/resources/tableSchemaEmpty.json";
        TableConfigurationProvider tcp = new TableConfigurationProvider();
        assertEquals(tcp.getTableConfiguration(filepath).size(), 0);
    }

    @Test
    public void invalidFieldTableThrowsException() throws IOException {

        Exception thrown = Assertions.assertThrows(Exception.class, () -> {
            String filepath = "src/test/resources/tableSchemaInvalid.json";
            TableConfigurationProvider tcp = new TableConfigurationProvider();
            List<TableConfiguration> tableConfiguration = tcp.getTableConfiguration(filepath);
        });

        Assertions.assertTrue(thrown.getMessage().contains("Unrecognized field \"table\""));
    }
    @Test
    public void invalidFieldFieldThrowsException() throws IOException {

        Exception thrown = Assertions.assertThrows(Exception.class, () -> {
            String filepath = "src/test/resources/tableSchemaFieldInvalid.json";
            TableConfigurationProvider tcp = new TableConfigurationProvider();
            List<TableConfiguration> tableConfiguration = tcp.getTableConfiguration(filepath);
        });

        Assertions.assertTrue(thrown.getMessage().contains("Unrecognized field \"field\""));
    }
    @Test
    public void shouldHaveThreeTableConfigurations() throws IOException {
        String filepath = "src/test/resources/tableSchemaMultiples.json";
        TableConfigurationProvider tcp = new TableConfigurationProvider();
        assertEquals(tcp.getTableConfiguration(filepath).size(), 3);
    }
}
