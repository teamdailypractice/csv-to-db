package info.dailypractice;

import freemarker.template.TemplateException;
import info.dailypractice.service.DbTablesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ShellComponent
public class DdlExecutor {
    private static Logger LOG = LoggerFactory
            .getLogger(DdlExecutor.class);

    private TableConfigurationProvider tableConfigurationProvider;
    private DbStatementsProvider dbStatementsProvider;
    private DbTablesService dbTablesService;
    private Map<String, TableConfiguration> tableConfigurationMap = new HashMap<String, TableConfiguration>();

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
                        tableConfigurationMap.putIfAbsent(tableConfiguration.getTableName(), tableConfiguration);
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

    @ShellMethod("load data from the file to table")
    public void loadData(String csvFilepath, String tableName) throws Exception {
        if (tableConfigurationMap.size() == 0) {
            throw new Exception("createTables command must be run before running loadData");
        }
        System.out.println("tableConfigurationMap: " + tableConfigurationMap.toString());
        System.out.println("tableConfigurationMap size: " + tableConfigurationMap.size());
        System.out.println("tableName :" + tableName);
        TableConfiguration tableConfiguration = tableConfigurationMap.get(tableName);
        System.out.println("tableConfiguration: " + tableConfiguration);
        if (tableConfiguration == null) {
            throw new Exception(String.format("Table: %s schema is not defined in the configuration file", tableName));
        }
        //Validate schema with data - Read CSV file
        CsvFileReader reader = new CsvFileReader(csvFilepath, tableConfiguration);
        List<String[]> rows = reader.getValidRows();
        LOG.info("File : " + csvFilepath + " valid rows count: " + rows.size());
        for (String[] row : rows) {
            String sqlStatement = dbStatementsProvider.getSqlInsertInto(tableConfiguration, row);
            dbTablesService.insertIntoTable(sqlStatement);
        }
        LOG.info("table: " + tableConfiguration.getTableName() + "number of rows inserted: " + rows.size());

    }


}
