package fr.uga.miage.m1.my_project_g1_10.strategies;

import fr.uga.miage.m1.my_project_g1_10.model.Joueur;

public class Rancunier implements Strategie {
    private boolean aEteTrahi = false;

    @Override
    public boolean decider(Joueur adversaire) {
        if (!adversaire.getDerniereDecision()) {
            aEteTrahi = true;
        }
        return !aEteTrahi; // Coopérer jusqu'à être trahi, puis toujours trahir
    }
}
