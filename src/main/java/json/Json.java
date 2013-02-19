package json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import org.ratpackframework.app.Request;
import org.ratpackframework.app.Response;
import org.ratpackframework.handler.Handler;

import java.io.StringWriter;

public class Json {

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
}
