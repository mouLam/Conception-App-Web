package fr.univlyon1.m1if.m1if03.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import fr.univlyon1.m1if.m1if03.classes.Candidat;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe statique qui renvoie une liste de candidats. Vous n'avez pas à vous préoccuper du fonctionnement de cette classe.
 */
public class CandidatListGenerator {
    public static Map<String, Candidat> getCandidatList() throws IOException {
        // Lecture du ficher de candidats avec Jackson
        ObjectMapper mapper = new ObjectMapper();
        // Voir : https://www.baeldung.com/jackson-collection-array
        CollectionType javaType = mapper.getTypeFactory().constructCollectionType(List.class, Item.class);
        List<Item> items = mapper.readValue(new URL("https://perso.liris.cnrs.fr/lionel.medini/enseignement/M1IF03/TP/candidats.json"), javaType);

        // Création d'une liste de candidats
        Map<String, Candidat> resultat = new HashMap<>();
        for (Item i : items) {
            resultat.put(i.getNom(), new Candidat(i.getPrenom(), i.getNom()));
        }
        return resultat;
    }

    private static class Item {
        String prenom, nom;

        public Item() {
        }

        public String getPrenom() {
            return prenom;
        }

        public void setPrenom(String prenom) {
            this.prenom = prenom;
        }

        public String getNom() {
            return nom;
        }

        public void setNom(String nom) {
            this.nom = nom;
        }
    }
}
