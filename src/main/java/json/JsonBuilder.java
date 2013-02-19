package json;

import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class JsonBuilder {

    private final JsonWriter writer;

    public JsonBuilder(JsonWriter writer) {
        this.writer = writer;
    }

    public JsonBuilder beginArray() {
        try {
            writer.beginArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public JsonBuilder endArray() {
        try {
            writer.endArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public JsonBuilder beginObject() {
        try {
            writer.beginObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public JsonBuilder endObject() {
        try {
            writer.endObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public JsonBuilder name(String name) {
        try {
            writer.name(name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public JsonBuilder value(String value) {
        try {
            writer.value(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public JsonBuilder nullValue() {
        try {
            writer.nullValue();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public JsonBuilder value(boolean value) {
        try {
            writer.value(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public JsonBuilder value(double value) {
        try {
            writer.value(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public JsonBuilder value(long value) {
        try {
            writer.value(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public JsonBuilder value(Number value) {
        try {
            writer.value(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }
}
