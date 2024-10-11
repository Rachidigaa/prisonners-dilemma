package fr.uga.miage.m1.my_project_g1_10.strategies;

import fr.uga.miage.m1.my_project_g1_10.model.Joueur;

import java.security.SecureRandom;

public class Aleatoire implements Strategie {
    private static final SecureRandom secureRandom = new SecureRandom();

    @Override
    public boolean decider(Joueur adversaire) {
        return secureRandom.nextBoolean();
    }
}