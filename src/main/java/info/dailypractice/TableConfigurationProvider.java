package info.dailypractice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TableConfigurationProvider {
    public List<TableConfiguration> getTableConfiguration(String dataFilepath) throws IOException {
        List<TableConfiguration> tableConfigurations = new ArrayList<>();

        //System.out.println(Path.of(dataFilepath).toAbsolutePath().toString());
        String data = Files.readAllLines(Path.of(dataFilepath), StandardCharsets.UTF_8)
                .stream().collect(Collectors.joining("\n"));
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Jdk8Module());
        try {
            List<TableConfiguration> tableConfiguration = mapper.readValue(data,
                    new TypeReference<List<TableConfiguration>>() {
                    });
            tableConfigurations.addAll(tableConfiguration);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return tableConfigurations;
    }
}
