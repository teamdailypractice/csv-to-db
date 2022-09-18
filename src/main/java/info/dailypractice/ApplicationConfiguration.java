package info.dailypractice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.h2.jdbcx.JdbcDataSource;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
@ComponentScan(basePackageClasses = Main.class)
@PropertySource("classpath:/application.properties")
@PropertySource(value = "classpath:/application-${spring.profiles.active}.properties",
        ignoreResourceNotFound = true)
@EnableTransactionManagement
public class ApplicationConfiguration {
    private Properties properties;

    public ApplicationConfiguration() throws IOException {
        this.properties = load("application.properties");
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    @Bean
    public DataSource dataSource() {
        String dbFilePath = properties.getProperty("db.h2.file.path");
        //Paths.get(new URL(url).toURI())
        JdbcDataSource ds = new JdbcDataSource();
//        ds.setURL("jdbc:h2:" + dbFilePath + ";INIT=RUNSCRIPT FROM 'classpath:schema.sql'");
        ds.setURL("jdbc:h2:" + dbFilePath);
        ds.setUser("immuser");
        ds.setPassword("immuser");
        return ds;
    }
    @Bean
    public TransactionManager platformTransactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public TableConfigurationProvider tableConfigurationProvider() {
        return new TableConfigurationProvider();
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

    private Properties load(String filename) throws IOException {
        Properties properties = new Properties();
        try (InputStream input = ApplicationConfiguration.class.getClassLoader().getResourceAsStream(filename)) {
            if (input == null) {
                throw new FileNotFoundException(filename);
            }
            properties.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
            throw ex;
        }
        return properties;
    }
}
