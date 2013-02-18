package json;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.ratpackframework.Handler;
import org.ratpackframework.Request;
import org.ratpackframework.Response;

import java.util.Map;

public class Json {

    public void object(Response response, Handler<JsonBuilder> builder) {
        StringBuilder sb = new StringBuilder();
        JsonBuilder json = new JsonBuilder(sb);
        json.startObject();
        builder.handle(json);
        json.endObject();
        response.text("application/json", sb);
    }

    public Map<String, Object> object(Request request) {
        JSONParser parser = new JSONParser();
        Object object = null;
        try {
            object = parser.parse(request.getText());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        if (object instanceof Map) {
            //noinspection unchecked
            return (Map<String, Object>) object;
        } else {
            throw new IllegalStateException("JSON root element is a " + object.getClass().getName() + ", not a Map");
        }
    }
}
