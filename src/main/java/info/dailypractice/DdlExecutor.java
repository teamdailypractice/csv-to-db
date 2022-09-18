package info.dailypractice;

import freemarker.template.TemplateException;
import info.dailypractice.service.DbTablesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.io.IOException;

@ShellComponent
public class DdlExecutor {
    private static Logger LOG = LoggerFactory
            .getLogger(DdlExecutor.class);

    private TableConfigurationProvider tableConfigurationProvider;
    private DbStatementsProvider dbStatementsProvider;
    private DbTablesService dbTablesService;

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


}
