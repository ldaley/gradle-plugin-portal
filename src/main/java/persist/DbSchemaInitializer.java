package persist;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.hibernate.SessionFactory;
import org.hibernate.engine.jdbc.spi.JdbcConnectionAccess;
import org.hibernate.engine.spi.SessionImplementor;

public class DbSchemaInitializer implements Runnable {

    private final DataSource dataSource;

    @Inject
    public DbSchemaInitializer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void run() {
        try {
            Connection connection = dataSource.getConnection();
            try {
                runLiquibaseUpdate(connection);
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void runLiquibaseUpdate(Connection connection) {
        try {
            ResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor();
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase(getChangeLogFile(), resourceAccessor, database);

            liquibase.update(getContext());
        } catch (LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }

    private String getContext() {
        return "production";
    }

    private String getChangeLogFile() {
        return "db/changelog/db.changelog-master.xml";
    }
}
