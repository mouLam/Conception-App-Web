package fr.univlyon1.m1if.m1if03.classes;

public class Ballot {
    Bulletin bulletin;

    public Ballot(Bulletin bulletin) {
        this.bulletin = bulletin;
    }

    public Bulletin getBulletin() {
        return bulletin;
    }

    public void setBulletin(Bulletin bulletin) {
        this.bulletin = bulletin;
    }
}
