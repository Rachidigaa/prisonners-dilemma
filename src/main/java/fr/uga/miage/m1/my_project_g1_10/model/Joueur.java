package fr.uga.miage.m1.my_project_g1_10.model;

import fr.uga.miage.m1.my_project_g1_10.strategies.Strategie;

public class Joueur {
    private String nom;
    private int score;
    private Strategie strategie;
    private boolean derniereDecision;

    public Joueur(String nom, Strategie strategie) {
        this.nom = nom;
        this.strategie = strategie;
        this.score = 0;
        this.derniereDecision = true; // Coopérer au début
    }

    public boolean jouerTour(Joueur adversaire) {
        boolean decision = strategie.decider(adversaire);
        this.derniereDecision = decision;
        return decision;
    }

    public void ajouterScore(int points) {
        this.score += points;
    }

    public boolean getDerniereDecision() {
        return this.derniereDecision;
    }

    public String getNom() {
        return nom;
    }

    public int getScore() {
        return score;
    }
}
