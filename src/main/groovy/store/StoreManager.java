package store;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.sdb.SDBFactory;
import com.hp.hpl.jena.sdb.Store;
import com.hp.hpl.jena.sdb.StoreDesc;
import com.hp.hpl.jena.sdb.sql.JDBC;
import com.hp.hpl.jena.sdb.sql.SDBConnection;
import com.hp.hpl.jena.sdb.store.DatabaseType;
import com.hp.hpl.jena.sdb.store.DatasetStore;
import com.hp.hpl.jena.sdb.store.LayoutType;
import com.hp.hpl.jena.sdb.util.StoreUtils;

import java.sql.SQLException;

public class StoreManager {

    private final Store store;

    public StoreManager() throws SQLException {
        StoreDesc storeDesc = new StoreDesc(LayoutType.LayoutTripleNodesHash, DatabaseType.H2);
        JDBC.loadDriverH2();
        String jdbcURL = "jdbc:h2:data";
        SDBConnection conn = new SDBConnection(jdbcURL, "sa", "");
        store = SDBFactory.connectStore(conn, storeDesc);

        if (!StoreUtils.isFormatted(store)) {
            store.getTableFormatter().create();
        }
    }

    public Store getStore() {
        return store;
    }

    public ResultSet query(String query) {
        Dataset ds = DatasetStore.create(store);
        QueryExecution qe = QueryExecutionFactory.create(query, ds);
        try {
            return qe.execSelect();
        } finally {
            qe.close();
        }
    }

    public Model getModel() {
        return SDBFactory.connectDefaultModel(store);
    }

    public QueryExecution queryExecution(String query, QuerySolutionMap initialBinding) {
        Dataset ds = DatasetStore.create(getStore());
        return QueryExecutionFactory.create(query, ds, initialBinding);
    }
}
