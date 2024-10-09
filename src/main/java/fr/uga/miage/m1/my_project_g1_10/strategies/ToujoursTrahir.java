package fr.uga.miage.m1.my_project_g1_10.strategies;

import fr.uga.miage.m1.my_project_g1_10.model.Joueur;

public class ToujoursTrahir implements Strategie {
    @Override
    public boolean decider(Joueur adversaire) {
        return false;
    }
}