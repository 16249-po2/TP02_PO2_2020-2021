package pt.ipbeja.estig.boulderdash.model;

public class Rockford {
    private static Rockford instance = new Rockford();

    private Rockford() {
        // Exists only to defeat instantiation.
    }

    public static Rockford getInstance() {
        return instance;
    }
}