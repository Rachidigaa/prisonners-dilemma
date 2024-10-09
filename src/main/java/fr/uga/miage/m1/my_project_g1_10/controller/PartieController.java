package fr.uga.miage.m1.my_project_g1_10.controller;

import fr.uga.miage.m1.my_project_g1_10.model.Joueur;
import fr.uga.miage.m1.my_project_g1_10.model.Partie;
import fr.uga.miage.m1.my_project_g1_10.strategies.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/partie")
public class PartieController {

    private Map<Long, Partie> partiesEnCours = new HashMap<>();
    private long idCompteur = 1;
    private Map<Long, Boolean> joueur2Connecte = new HashMap<>();

    @PostMapping("/nouvelle")
    public String nouvellePartie(@RequestParam String nomJoueur1, @RequestParam String strategieJoueur1, @RequestParam int nbTours) {
        Joueur joueur1 = new Joueur(nomJoueur1, creerStrategie(strategieJoueur1));
        Partie partie = new Partie(joueur1, null, nbTours);

        partiesEnCours.put(idCompteur, partie);
        joueur2Connecte.put(idCompteur, false);

        partie.attendreDecisionJoueur1();
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

        Joueur joueur2 = new Joueur(nomJoueur2, creerStrategie(strategieJoueur2));
        partie.setJoueur2(joueur2);
        joueur2Connecte.put(id, true);

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
            partie.jouerTourInteractifJoueur1(decision);
        } else if (joueur.equals("joueur2")) {
            partie.jouerTourInteractifJoueur2(decision);
        } else {
            return "Joueur non reconnu.";
        }

        return "Tour joué. Score : Joueur 1 = " + partie.getJoueur1().getScore() + ", Joueur 2 = " + partie.getJoueur2().getScore();
    }

    @GetMapping("/fin/{id}")
    public Map<String, String> finPartie(@PathVariable Long id) {
        Partie partie = partiesEnCours.get(id);

        if (partie == null) {
            throw new IllegalArgumentException("Partie non trouvée.");
        }

        if (partie.estPartieTerminee()) {
            // On renvoie le score final et le résultat (vainqueur)
            Map<String, String> resultatFinal = new HashMap<>();
            resultatFinal.put("resultat", partie.obtenirResultatFinal());
            resultatFinal.put("scoreJoueur1", String.valueOf(partie.getJoueur1().getScore()));
            resultatFinal.put("scoreJoueur2", String.valueOf(partie.getJoueur2().getScore()));
            return resultatFinal;
        } else {
            throw new IllegalStateException("La partie n'est pas encore terminée.");
        }
    }


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
