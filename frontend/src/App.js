import React, { useState } from 'react';
import PartieService from './services/partieService';
import { Form, Button, Container, Row, Col } from 'react-bootstrap';
import './App.css'; // Fichier CSS pour l'animation

function App() {
    const [idPartie, setIdPartie] = useState(null); // ID de la partie
    const [joueur, setJoueur] = useState(''); // Nom du joueur
    const [strategie, setStrategie] = useState(''); // Stratégie du joueur
    const [decision, setDecision] = useState(true); // Décision pour chaque tour (coopérer ou trahir)
    const [isJoueur1, setIsJoueur1] = useState(false); // Savoir si c'est le joueur 1 ou 2
    const [partieCreer, setPartieCreer] = useState(false); // Partie créée ou rejointe

    // Créer une nouvelle partie (Joueur 1)
    const handleCreerPartie = () => {
        PartieService.creerNouvellePartie(joueur, strategie, 5) // Nombre de tours fixé à 5
            .then((response) => {
                const id = response.data.split(':')[1].trim(); // Récupérer l'ID de la partie
                setIdPartie(id);
                setIsJoueur1(true);
                setPartieCreer(true);
                alert(`Nouvelle partie créée avec l'ID : ${id}`);
            })
            .catch((error) => {
                console.error('Erreur lors de la création de la partie', error);
            });
    };

    // Rejoindre une partie (Joueur 2)
    const handleRejoindrePartie = () => {
        PartieService.rejoindrePartie(idPartie, joueur, strategie)
            .then(() => {
                setPartieCreer(true);
                alert(`Vous avez rejoint la partie avec succès (ID : ${idPartie})`);
            })
            .catch((error) => {
                console.error('Erreur lors de la connexion à la partie', error);
            });
    };

    // Jouer un tour
    const handleJouerTour = () => {
        const joueurIdentifiant = isJoueur1 ? 'joueur1' : 'joueur2';
        PartieService.jouerTour(idPartie, decision, joueurIdentifiant)
            .then((response) => {
                alert(`Tour joué : ${response.data}`);
            })
            .catch((error) => {
                console.error('Erreur lors du tour', error);
            });
    };

    return (
        <Container className="App">
            <h1 className="animate__animated animate__fadeInDown">Dilemme du Prisonnier - Jeu Multijoueur</h1>

            {!partieCreer && (
                <Row className="mb-5">
                    <Col>
                        <h2>{isJoueur1 ? "Créer une partie" : "Rejoindre une partie"}</h2>
                        <Form>
                            <Form.Group>
                                <Form.Label>Nom du joueur</Form.Label>
                                <Form.Control
                                    type="text"
                                    placeholder="Entrez votre nom"
                                    value={joueur}
                                    onChange={(e) => setJoueur(e.target.value)}
                                />
                            </Form.Group>

                            <Form.Group>
                                <Form.Label>Stratégie du joueur</Form.Label>
                                <Form.Control
                                    as="select"
                                    value={strategie}
                                    onChange={(e) => setStrategie(e.target.value)}
                                >
                                    <option value="">Sélectionnez une stratégie</option>
                                    <option value="donnantdonnant">Donnant Donnant</option>
                                    <option value="aleatoire">Aléatoire</option>
                                    <option value="toujourscooperer">Toujours Coopérer</option>
                                    <option value="toujourstrahir">Toujours Trahir</option>
                                    <option value="rancunier">Rancunier</option>
                                </Form.Control>
                            </Form.Group>

                            {/* Si c'est le joueur 1, il crée une partie */}
                            {isJoueur1 ? (
                                <Button variant="primary" onClick={handleCreerPartie}>
                                    Créer Partie
                                </Button>
                            ) : (
                                <>
                                    <Form.Group>
                                        <Form.Label>ID de la partie</Form.Label>
                                        <Form.Control
                                            type="text"
                                            placeholder="Entrez l'ID de la partie"
                                            value={idPartie}
                                            onChange={(e) => setIdPartie(e.target.value)}
                                        />
                                    </Form.Group>
                                    <Button variant="success" onClick={handleRejoindrePartie}>
                                        Rejoindre Partie
                                    </Button>
                                </>
                            )}
                        </Form>
                    </Col>
                </Row>
            )}

            {/* Interface pour jouer un tour après avoir rejoint ou créé une partie */}
            {partieCreer && (
                <Row>
                    <Col>
                        <h2>Jouer un tour</h2>
                        <Form>
                            <Form.Group>
                                <Form.Check
                                    type="radio"
                                    label="Coopérer"
                                    value="true"
                                    checked={decision === true}
                                    onChange={() => setDecision(true)}
                                />
                                <Form.Check
                                    type="radio"
                                    label="Trahir"
                                    value="false"
                                    checked={decision === false}
                                    onChange={() => setDecision(false)}
                                />
                            </Form.Group>

                            <Button variant="warning" onClick={handleJouerTour}>
                                Jouer Tour
                            </Button>
                        </Form>
                    </Col>
                </Row>
            )}

            {!isJoueur1 && !partieCreer && (
                <Button variant="secondary" onClick={() => setIsJoueur1(true)}>
                    Je suis le joueur 1
                </Button>
            )}
        </Container>
    );
}

export default App;
