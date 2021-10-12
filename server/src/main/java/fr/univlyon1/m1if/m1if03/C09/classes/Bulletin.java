package fr.univlyon1.m1if.m1if03.C09.classes;

public class Bulletin {
    Candidat candidat;
    boolean blanc = false;

    public Bulletin(Candidat candidat) {
        this.candidat = candidat;
    }

    public Bulletin(boolean blanc) {
        this.blanc = blanc;
    }

    public Candidat getCandidat() {
        return candidat;
    }

    public boolean getVoteBlanc() { return blanc; }
}
