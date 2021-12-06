package fr.univlyon1.m1if.m1if03.classes;

public class ResultatCandidat {
    private String nomCandidat;
    private Integer votes;

    public ResultatCandidat(String nomCandidat, Integer votes) {
        this.nomCandidat = nomCandidat;
        this.votes = votes;
    }

    public ResultatCandidat() {
    }

    public String getNomCandidat() {
        return nomCandidat;
    }

    public Integer getVotes() {
        return votes;
    }
}
