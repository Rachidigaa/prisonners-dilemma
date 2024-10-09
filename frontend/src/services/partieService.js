import axios from 'axios';

const API_URL = 'http://localhost:8080/partie'; // URL de votre backend Spring Boot

class PartieService {
    // Créer une nouvelle partie
    creerNouvellePartie(nomJoueur1, strategieJoueur1, nbTours) {
        return axios.post(`${API_URL}/nouvelle`, null, {
            params: {
                nomJoueur1: nomJoueur1,
                strategieJoueur1: strategieJoueur1,
                nbTours: nbTours
            }
        });
    }

    // Rejoindre une partie existante
    rejoindrePartie(id, nomJoueur2, strategieJoueur2) {
        return axios.post(`${API_URL}/rejoindre`, null, {
            params: {
                id: id,
                nomJoueur2: nomJoueur2,
                strategieJoueur2: strategieJoueur2
            }
        });
    }

    // Jouer un tour
    jouerTour(id, decision, joueur) {
        return axios.post(`${API_URL}/${id}/jouer`, null, {
            params: {
                decision: decision,
                joueur: joueur
            }
        });
    }
}

export default new PartieService();
