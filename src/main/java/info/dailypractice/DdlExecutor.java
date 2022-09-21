package info.dailypractice;

import freemarker.template.TemplateException;
import info.dailypractice.service.DbTablesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ShellComponent
public class DdlExecutor {
    private static Logger LOG = LoggerFactory
            .getLogger(DdlExecutor.class);

    private TableConfigurationProvider tableConfigurationProvider;
    private DbStatementsProvider dbStatementsProvider;
    private DbTablesService dbTablesService;
    private Map<String, TableConfiguration> tableConfigurationMap = new HashMap<>();

    public DdlExecutor(TableConfigurationProvider tableConfigurationProvider,
                       DbStatementsProvider dbStatementsProvider,
                       DbTablesService dbTablesService) {
        this.tableConfigurationProvider = tableConfigurationProvider;
        this.dbStatementsProvider = dbStatementsProvider;
        this.dbTablesService = dbTablesService;

    }


    @ShellMethod("create required tables")
    public void createTables(String tableSchemaFilepath) throws IOException, RuntimeException {
        tableConfigurationProvider.getTableConfiguration(tableSchemaFilepath).
                forEach(tableConfiguration -> {
                    try {
                        tableConfigurationMap.put(tableConfiguration.getTableName(), tableConfiguration);
                        doProcess(tableConfiguration);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private void doProcess(TableConfiguration tc) throws IOException {
        String sqlCreateTable = dbStatementsProvider.getSqlCreateTable(tc);
        LOG.info("table to be created: " + sqlCreateTable);
//        System.out.println(sqlCreateTable);
        dbTablesService.createTable(sqlCreateTable);
        System.out.println("Table: " + tc.getTableName() + " - created");
    }

    private void loadData(String csvFilepath, String tableName) throws Exception {

        if (tableConfigurationMap.size() == 0) {
            throw new Exception("createTables command must be run before running loadData");
        }
        TableConfiguration tableConfiguration = tableConfigurationMap.get(tableName);
        if (tableConfiguration == null) {
            throw new Exception(String.format("Table: %s schema is not defined in the configuration file", tableName));
        }
        //Validate schema with data - Read CSV file
        CsvFileReader reader = new CsvFileReader(csvFilepath);
        reader.getValidRows();
    }


}
