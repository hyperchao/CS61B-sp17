package db;

public class Attribute {
    public final String name;
    public final Value.Type type;

    public Attribute(String name, Value.Type type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Attribute attribute = (Attribute) o;
        return name.equals(attribute.name) && type == attribute.type;
    }
}
