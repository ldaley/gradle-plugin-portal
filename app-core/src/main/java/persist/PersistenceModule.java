package persist;

import com.google.code.guice.repository.configuration.JpaRepositoryModule;
import com.google.code.guice.repository.configuration.RepositoryBinder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Environment;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.ejb.AvailableSettings;
import org.hibernate.ejb.HibernateEntityManagerFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.service.jdbc.connections.spi.ConnectionProvider;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import user.registration.RegisteredUserRepository;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.File;
import java.util.Properties;

public class PersistenceModule extends AbstractModule {

    private final File dbDirectory;

    public PersistenceModule(File dbDirectory) {
        this.dbDirectory = dbDirectory;
    }

    @Override
    protected void configure() {
        install(new JpaRepositoryModule("core") {
            @Override
            protected void bindRepositories(RepositoryBinder binder) {
                binder.bind(RegisteredUserRepository.class).to("core");
            }

            @Override
            protected Properties getPersistenceUnitProperties(String persistenceUnitName) {
                Properties props = new Properties();
                props.put(AvailableSettings.NAMING_STRATEGY, ImprovedNamingStrategy.class.getName());
                props.put(Environment.HBM2DDL_AUTO, "none");
                props.put(Environment.URL, "jdbc:h2:" + dbDirectory);
                props.put(Environment.C3P0_MIN_SIZE, "5");
                props.put(Environment.C3P0_MAX_SIZE, "20");
                return props;
            }

        });
        bind(DbSchemaInitializer.class);
    }

    @Provides
    @Singleton
    RepositoryFactorySupport repositoryFactorySupport(EntityManager entityManager) {
        return new JpaRepositoryFactory(entityManager);
    }

    @Provides
    @Singleton
    DataSource getDataSource(SessionFactory sessionFactory) {
        if (sessionFactory instanceof SessionFactoryImplementor) {
            ConnectionProvider cp = ((SessionFactoryImplementor) sessionFactory).getConnectionProvider();
            return cp.unwrap(DataSource.class);
        }
        return null;
    }

    @Provides
    @Singleton
    SessionFactory sessionFactory(EntityManagerFactory entityManagerFactory) {
        HibernateEntityManagerFactory hibernateEntityManagerFactory = (HibernateEntityManagerFactory) entityManagerFactory;
        return hibernateEntityManagerFactory.getSessionFactory();
    }

}
