package fr.uga.miage.m1.my_project_g1_10.model;

import java.util.Timer;
import java.util.TimerTask;
public class Partie {
    private Joueur joueur1;
    private Joueur joueur2;
    private int nbTours;
    private int tourActuel = 0;
    private boolean joueur1Pret = false;
    private boolean joueur2Pret = false;
    private boolean decisionJoueur1;
    private boolean decisionJoueur2;
    private final int T = 5; // Trahison
    private final int D = 0; // Duperie
    private final int C = 3; // Coopération mutuelle
    private final int P = 1; // Trahison mutuelle

    private Timer timer1;
    private Timer timer2;

    public Partie(Joueur joueur1, Joueur joueur2, int nbTours) {
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
        this.nbTours = nbTours;
    }

    // Fonction qui démarre le timer et applique la stratégie automatiquement si le joueur ne joue pas
    public void attendreDecisionJoueur1() {
        timer1 = new Timer();
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!joueur1Pret) {
                    System.out.println("Joueur 1 n'a pas joué à temps. Utilisation de la stratégie.");
                    decisionJoueur1 = joueur1.jouerTour(joueur2);
                    joueur1Pret = true;
                    verifierSiTousLesJoueursOntJoue();
                }
            }
        }, 10000);  // Délai de 2 secondes
    }

    public void attendreDecisionJoueur2() {
        timer2 = new Timer();
        timer2.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!joueur2Pret) {
                    System.out.println("Joueur 2 n'a pas joué à temps. Utilisation de la stratégie.");
                    decisionJoueur2 = joueur2.jouerTour(joueur1);
                    joueur2Pret = true;
                    verifierSiTousLesJoueursOntJoue();
                }
            }
        }, 10000);  // Délai de 2 secondes
    }

    public void jouerTourInteractifJoueur1(boolean decision) {
        this.decisionJoueur1 = decision;
        this.joueur1Pret = true;
        timer1.cancel();  // Annuler le timer si le joueur a joué à temps
        verifierSiTousLesJoueursOntJoue();
    }

    public void jouerTourInteractifJoueur2(boolean decision) {
        this.decisionJoueur2 = decision;
        this.joueur2Pret = true;
        timer2.cancel();  // Annuler le timer si le joueur a joué à temps
        verifierSiTousLesJoueursOntJoue();
    }

    private void verifierSiTousLesJoueursOntJoue() {
        if (joueur1Pret && joueur2Pret) {
            // Si les deux joueurs ont joué, calculer les scores
            calculerScore(decisionJoueur1, decisionJoueur2);

            joueur1Pret = false;  // Remet à zéro l'état des joueurs pour le prochain tour
            joueur2Pret = false;
            tourActuel++;

            afficherResultatTour();
            if (tourActuel < nbTours) {
                attendreDecisionJoueur1();  // Attendre pour le prochain tour
                attendreDecisionJoueur2();  // Attendre pour le prochain tour
            } else {
                System.out.println("La partie est terminée.");
            }
        }
    }

    private void calculerScore(boolean decision1, boolean decision2) {
        if (decision1 && decision2) { // Coopération mutuelle [c, c]
            joueur1.ajouterScore(C);
            joueur2.ajouterScore(C);
        } else if (!decision1 && !decision2) { // Trahison mutuelle [t, t]
            joueur1.ajouterScore(P);
            joueur2.ajouterScore(P);
        } else if (decision1 && !decision2) { // Joueur 1 coopère, joueur 2 trahit [c, t]
            joueur1.ajouterScore(D);
            joueur2.ajouterScore(T);
        } else { // Joueur 1 trahit, joueur 2 coopère [t, c]
            joueur1.ajouterScore(T);
            joueur2.ajouterScore(D);
        }
    }

    private void afficherResultatTour() {
        System.out.println("Tour " + tourActuel + " : ");
        System.out.println(joueur1.getNom() + " a choisi " + (decisionJoueur1 ? "Coopérer" : "Trahir"));
        System.out.println(joueur2.getNom() + " a choisi " + (decisionJoueur2 ? "Coopérer" : "Trahir"));
        System.out.println("Score de " + joueur1.getNom() + " : " + joueur1.getScore());
        System.out.println("Score de " + joueur2.getNom() + " : " + joueur2.getScore());
    }

    public Joueur getJoueur1() {
        return joueur1;
    }

    public Joueur getJoueur2() {
        return joueur2;
    }

    public void setJoueur2(Joueur joueur2) {
        this.joueur2 = joueur2;
    }
}
