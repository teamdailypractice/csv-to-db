package info.dailypractice.service;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.sql.PreparedStatement;

@Component
public class DbTablesService {
    private JdbcTemplate jdbcTemplate;

    public DbTablesService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void createTable(String sqlStatement) throws DataAccessException {
        jdbcTemplate.execute(sqlStatement);
    }

    @Transactional
    public void create(String userId, Integer amount) {
        System.out.println("Is a database transaction open? = " +
                TransactionSynchronizationManager.isActualTransactionActive());
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "insert into invoices (user_id, pdf_url, amount) values " +
                            "(?, ?, ?)"
            );
            ps.setString(1, userId);
//            ps.setString(2, generatedPdfUrl);
            ps.setInt(3, amount);
            return ps;
        });
    }
}
