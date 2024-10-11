package fr.uga.miage.m1.my_project_g1_10.model;

import fr.uga.miage.m1.my_project_g1_10.strategies.Strategie;

import java.util.ArrayList;
import java.util.List;

public class Joueur {
    private String nom;
    private int score;
    private Strategie strategie;
    private List<Boolean> historiqueChoix;

    public Joueur(String nom, Strategie strategie) {
        this.nom = nom;
        this.strategie = strategie;
        this.score = 0;
        this.historiqueChoix = new ArrayList<>();
    }

    public boolean jouerTour(Joueur adversaire) {
        boolean decision = strategie.decider(adversaire);
        ajouterChoix(decision);
        return decision;
    }

    public void ajouterChoix(boolean choix) {
        historiqueChoix.add(choix);
    }

    public boolean getDerniereDecision() {
        if (historiqueChoix.isEmpty()) {
            return true;
        }
        return historiqueChoix.get(historiqueChoix.size() - 1);
    }

    public void ajouterScore(int points) {
        this.score += points;
    }

    public String getNom() {
        return nom;
    }

    public int getScore() {
        return score;
    }

    public List<Boolean> getHistoriqueChoix() {
        return historiqueChoix;
    }
}
