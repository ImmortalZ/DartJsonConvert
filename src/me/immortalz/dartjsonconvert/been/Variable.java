package me.immortalz.dartjsonconvert.been;

public class Variable {
    public String type;
    public String name;
    public boolean iterator = false;

    public Variable(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public Variable(String type, String name, boolean iterator) {
        this.type = type;
        this.name = name;
        this.iterator = iterator;
    }

    @Override
    public String toString() {
        return "Variable{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", iterator=" + iterator +
                '}';
    }
}
