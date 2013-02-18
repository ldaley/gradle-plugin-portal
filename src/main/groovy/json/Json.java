package json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import org.ratpackframework.Handler;
import org.ratpackframework.Request;
import org.ratpackframework.Response;

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
