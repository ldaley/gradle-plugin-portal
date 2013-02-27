package json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import org.ratpackframework.app.Request;
import org.ratpackframework.app.Response;
import org.ratpackframework.handler.Handler;

import java.io.StringWriter;

public class Json {

    public static final String CONTENT_TYPE = "application/json";
    private final Gson gson = new Gson();

    public void object(Response response, Handler<JsonBuilder> builder) {
        StringWriter writer = new StringWriter();
        JsonBuilder json = new JsonBuilder(new JsonWriter(writer));
        json.beginObject();
        builder.handle(json);
        json.endObject();
        response.text("application/json", writer);
    }

    public JsonObject object(Request request) {
        JsonParser parser = new JsonParser();
        JsonElement parse = parser.parse(request.getText());
        return parse.getAsJsonObject();
    }

    public <T> T bind(Request request, Class<T> type) {
        return gson.fromJson(request.getText(), type);
    }

    public void render(Response response, Object object) {
        response.text(CONTENT_TYPE, gson.toJson(object));
    }
}
