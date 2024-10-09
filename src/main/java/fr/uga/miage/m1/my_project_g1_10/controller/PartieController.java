package fr.uga.miage.m1.my_project_g1_10.controller;

import fr.uga.miage.m1.my_project_g1_10.model.Joueur;
import fr.uga.miage.m1.my_project_g1_10.model.Partie;
import fr.uga.miage.m1.my_project_g1_10.strategies.*;
import org.springframework.web.bind.annotation.*;




import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/partie")
public class PartieController {

    private Map<Long, Partie> partiesEnCours = new HashMap<>();
    private long idCompteur = 1;
    private Map<Long, Boolean> joueur2Connecte = new HashMap<>();  // Pour vérifier si le joueur 2 est connecté

    @PostMapping("/nouvelle")
    public String nouvellePartie(@RequestParam String nomJoueur1, @RequestParam String strategieJoueur1, @RequestParam int nbTours) {
        // Création du joueur 1 et de la partie sans le joueur 2 au début
        Joueur joueur1 = new Joueur(nomJoueur1, creerStrategie(strategieJoueur1));
        Partie partie = new Partie(joueur1, null, nbTours);  // Joueur 2 sera ajouté plus tard

        partiesEnCours.put(idCompteur, partie);
        joueur2Connecte.put(idCompteur, false);  // Joueur 2 n'est pas encore connecté

        // Démarrer le timer pour le joueur 1
        partie.attendreDecisionJoueur1();

        // Retourne l'ID de la partie que le joueur 1 partagera
        return "Nouvelle partie créée avec l'ID : " + idCompteur++;
    }

    @PostMapping("/rejoindre")
    public String rejoindrePartie(@RequestParam Long id, @RequestParam String nomJoueur2, @RequestParam String strategieJoueur2) {
        Partie partie = partiesEnCours.get(id);

        if (partie == null) {
            return "Partie non trouvée.";
        }

        if (partie.getJoueur2() != null) {
            return "La partie a déjà un deuxième joueur.";
        }

        // Création du joueur 2 et ajout dans la partie
        Joueur joueur2 = new Joueur(nomJoueur2, creerStrategie(strategieJoueur2));
        partie.setJoueur2(joueur2);
        joueur2Connecte.put(id, true);  // Le joueur 2 est maintenant connecté

        // Démarrer le timer pour le joueur 2
        partie.attendreDecisionJoueur2();

        return "Vous avez rejoint la partie avec l'ID : " + id;
    }

    @PostMapping("/{id}/jouer")
    public String jouerTour(@PathVariable Long id, @RequestParam boolean decision, @RequestParam String joueur) {
        Partie partie = partiesEnCours.get(id);

        if (partie == null) {
            return "Partie non trouvée.";
        }

        if (joueur.equals("joueur1")) {
            if (partie.getJoueur2() == null || !joueur2Connecte.get(id)) {
                return "Le joueur 2 n'est pas encore connecté.";
            }
            partie.jouerTourInteractifJoueur1(decision);  // Joueur 1 joue
        } else if (joueur.equals("joueur2")) {
            partie.jouerTourInteractifJoueur2(decision);  // Joueur 2 joue
        } else {
            return "Joueur non reconnu.";
        }

        return "Tour joué. Score : Joueur 1 = " + partie.getJoueur1().getScore() + ", Joueur 2 = " + partie.getJoueur2().getScore();
    }

    // Méthode pour créer une stratégie en fonction du nom
    private Strategie creerStrategie(String nomStrategie) {
        switch (nomStrategie.toLowerCase()) {
            case "donnantdonnant":
                return new DonnantDonnant();
            case "aleatoire":
                return new Aleatoire();
            case "toujourscooperer":
                return new ToujoursCooperer();
            case "toujourstrahir":
                return new ToujoursTrahir();
            case "rancunier":
                return new Rancunier();
            default:
                throw new IllegalArgumentException("Stratégie inconnue : " + nomStrategie);
        }
    }
}
