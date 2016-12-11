package br.garca.model.spring;


public class PlayerJSON {

    private int id;
    private String name;

    public PlayerJSON(String name) {
        this.name = name;
    }

    public PlayerJSON() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
