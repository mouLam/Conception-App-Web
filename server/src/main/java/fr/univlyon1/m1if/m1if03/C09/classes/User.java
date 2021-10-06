package fr.univlyon1.m1if.m1if03.C09.classes;

import java.util.Objects;

public class User {
    private final String login;
    private String nom;

    public User(String login, String nom) {
        this.login = login;
        this.nom = nom;
    }

    public String getLogin() {
        return login;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return login.equals(user.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }
}
