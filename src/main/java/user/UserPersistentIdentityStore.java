package user;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.QuerySolutionMap;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import store.StoreManager;

import javax.inject.Inject;
import java.math.BigInteger;
import java.security.SecureRandom;

public class UserPersistentIdentityStore {

    public static final String PROP_USERNAME = "prop-username";
    public static final String PROP_SESSION = "prop-session";
    public static final String PROP_SECRET = "prop-secret";
    public static final String PROP_SERIES = "prop-series";

    private final StoreManager storeManager;
    private SecureRandom random = new SecureRandom();

    @Inject
    public UserPersistentIdentityStore(StoreManager storeManager) {
        this.storeManager = storeManager;
    }

    public PersistentUserToken initiate(String username) {
        Model model = storeManager.getModel();
        PersistentUserToken token = new PersistentUserToken(generateRandomString(), generateRandomString());
        Resource user = getOrCreateUser(username);
        model.begin();

        try {
            user.addProperty(
                    model.getProperty(PROP_SESSION),
                    model.createResource("session/" + token.getSeries())
                            .addProperty(model.getProperty(PROP_SECRET), token.getSecret())
                            .addProperty(model.getProperty(PROP_SERIES), token.getSeries())
            );
            return token;
        } finally {
            model.commit();
        }
    }

    public PersistentUserToken refresh(String username, PersistentUserToken token) {
        Model model = storeManager.getModel();
        Resource session = getSession(username, token.getSeries());
        if (session == null) {
            return null;
        }

        Property secretProperty = model.getProperty(PROP_SECRET);

        Statement statement = session.getProperty(secretProperty);
        if (statement == null) {
            return null;
        }

        if (!token.getSecret().equals(statement.getString())) {
            return null;
        }

        session.removeAll(secretProperty);
        String newSecret = generateRandomString();
        session.addLiteral(secretProperty, newSecret);

        return new PersistentUserToken(token.getSeries(), newSecret);
    }

    public void clear(String username) {
        Model model = storeManager.getModel();
        Resource resource = getUser(username);
        if (resource == null) {
            return;
        }

        resource.removeAll(model.getProperty(PROP_SESSION));
    }

    private Resource getSession(String username, String series) {
        Model model = storeManager.getModel();
        QuerySolutionMap initialBinding = new QuerySolutionMap();
        initialBinding.add("username", model.createLiteral(username));
        initialBinding.add("series", model.createLiteral(series));
        initialBinding.add("pu", model.createProperty(PROP_USERNAME));
        initialBinding.add("ps1", model.createProperty(PROP_SESSION));
        initialBinding.add("ps2", model.createProperty(PROP_SERIES));
        String query = "SELECT ?session WHERE { ?user ?pu ?username. ?user ?ps1 ?session. ?session ?ps2 ?series } LIMIT 1";
        QueryExecution queryExecution = storeManager.queryExecution(query, initialBinding);

        try {
            ResultSet resultSet = queryExecution.execSelect();

            if (resultSet.hasNext()) {
                QuerySolution next = resultSet.next();
                return next.getResource("session");
            } else {
                return null;
            }
        } finally {
            queryExecution.close();
        }
    }

    private String generateRandomString() {
        return new BigInteger(130, random).toString(32);
    }

    private Resource getUser(String username) {
        Model model = storeManager.getModel();
        QuerySolutionMap initialBinding = new QuerySolutionMap();
        initialBinding.add("username", model.createLiteral(username));
        initialBinding.add("pu", model.createProperty(PROP_USERNAME));
        String query = "SELECT ?user WHERE { ?user ?pu ?username } LIMIT 1";
        QueryExecution queryExecution = storeManager.queryExecution(query, initialBinding);

        try {
            ResultSet resultSet = queryExecution.execSelect();

            if (resultSet.hasNext()) {
                QuerySolution next = resultSet.next();
                return next.getResource("user");
            } else {
                return null;
            }
        } finally {
            queryExecution.close();
        }
    }

    private Resource createUser(String username) {
        Model model = storeManager.getModel();
        Resource resource = model.createResource("user/" + generateRandomString());
        resource.addProperty(model.getProperty(PROP_USERNAME), username);
        return resource;
    }

    private Resource getOrCreateUser(String username) {
        Resource user = getUser(username);
        if (user == null) {
            user = createUser(username);
        }
        return user;
    }
}
