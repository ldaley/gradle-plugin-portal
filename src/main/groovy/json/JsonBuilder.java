package json;

import java.util.LinkedList;

public class JsonBuilder {
    private enum Type {
        Root(false), Object(true), Array(true), Property(false), Value(false);

        final boolean multiValued;

        private Type(boolean multiValued) {
            this.multiValued = multiValued;
        }
    }

    private static class Element {
        private final Type type;
        private boolean empty = true;

        private Element(Type type) {
            this.type = type;
        }
    }

    private final StringBuilder dest;
    private final LinkedList<Element> stack = new LinkedList<>();

    public JsonBuilder(StringBuilder dest) {
        this.dest = dest;
        stack.add(new Element(Type.Root));
    }

    public JsonBuilder startObject() {
        startElement(Type.Object);
        dest.append("{");
        return this;
    }

    public JsonBuilder endObject() {
        endElement(Type.Object);
        dest.append("}");
        return this;
    }

    public JsonBuilder startArray() {
        startElement(Type.Array);
        dest.append("[");
        return this;
    }

    public JsonBuilder endArray() {
        endElement(Type.Array);
        dest.append("]");
        return this;
    }

    /**
     * Convenience method that calls {@link #startProperty(String)}, {@link #value(Object)} and {@link #endProperty()}.
     */
    public JsonBuilder property(String name, Object value) {
        startProperty(name);
        value(value);
        endProperty();
        return this;
    }

    public JsonBuilder startProperty(String name) {
        startElement(Type.Property);
        dest.append("\"");
        dest.append(name);
        dest.append("\":");
        return this;
    }

    public JsonBuilder endProperty() {
        endElement(Type.Property);
        return this;
    }

    public JsonBuilder value(Object value) {
        startElement(Type.Value);
        if (value instanceof Number) {
            dest.append(value.toString());
        } else if (value instanceof Boolean) {
            dest.append(value.toString());
        } else if (value != null) {
            dest.append("\"");
            dest.append(value.toString().replace("\\", "\\\\").replace("\"", "\\\""));
            dest.append("\"");
        } else {
            dest.append("null");
        }
        endElement(Type.Value);
        return this;
    }

    private void startElement(Type type) {
        Element currentElement = stack.getFirst();
        stack.addFirst(new Element(type));
        if (currentElement.empty) {
            currentElement.empty = false;
        } else {
            dest.append(",");
        }
    }

    private void endElement(Type expected) {
        Element currentElement = stack.getFirst();
        if (currentElement.type != expected) {
            throw new IllegalStateException(String.format("Expected current element to be a %s, instead is is a %s.",
                    expected, currentElement.type));
        }
        stack.removeFirst();
    }
}
