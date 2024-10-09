import React, { useState } from 'react';
import PartieService from './services/partieService';
import { Form, Button, Container, Row, Col } from 'react-bootstrap';
import './App.css';

function App() {
    const [idPartie, setIdPartie] = useState(null);
    const [joueur, setJoueur] = useState('');
    const [strategie, setStrategie] = useState('');
    const [nbTours, setNbTours] = useState(0);
    const [tourActuel, setTourActuel] = useState(0);
    const [decision, setDecision] = useState(true);
    const [isJoueur1, setIsJoueur1] = useState(false);
    const [partieCreer, setPartieCreer] = useState(false);
    const [partieTerminee, setPartieTerminee] = useState(false);
    const [scoreJoueur1, setScoreJoueur1] = useState(0);
    const [scoreJoueur2, setScoreJoueur2] = useState(0);
    const [messageFinPartie, setMessageFinPartie] = useState('');

    const handleCreerPartie = () => {
        PartieService.creerNouvellePartie(joueur, strategie, nbTours)
            .then((response) => {
                const id = response.data.split(':')[1].trim();
                setIdPartie(id);
                setIsJoueur1(true);
                setPartieCreer(true);
                setTourActuel(0);
                alert(`Nouvelle partie créée avec l'ID : ${id}`);
            })
            .catch((error) => {
                console.error('Erreur lors de la création de la partie', error);
            });
    };


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
                setTourActuel((prevTour) => prevTour + 1);
                if (tourActuel + 1 >= nbTours) {
                    setPartieTerminee(true);
                    PartieService.finPartie(idPartie)
                        .then((res) => {
                            console.log('Réponse des scores:', res.data);
                            const scoreJ1 = res.data.scoreJoueur1 || 0;
                            const scoreJ2 = res.data.scoreJoueur2 || 0;

                            setScoreJoueur1(scoreJ1);
                            setScoreJoueur2(scoreJ2);

                            if (scoreJ1 > scoreJ2) {
                                setMessageFinPartie(isJoueur1 ? 'Vous avez gagné !' : 'Vous avez perdu !');
                            } else if (scoreJ1 < scoreJ2) {
                                setMessageFinPartie(isJoueur1 ? 'Vous avez perdu !' : 'Vous avez gagné !');
                            } else {
                                setMessageFinPartie('Match nul !');
                            }
                        })
                        .catch((error) => {
                            console.error('Erreur lors de la récupération des scores', error);
                        });
                } else {
                    alert(`Tour joué : ${response.data}`);
                }
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

                            {isJoueur1 && (
                                <Form.Group>
                                    <Form.Label>Nombre de tours</Form.Label>
                                    <Form.Control
                                        type="number"
                                        placeholder="Entrez le nombre de tours"
                                        value={nbTours}
                                        onChange={(e) => setNbTours(e.target.value)}
                                    />
                                </Form.Group>
                            )}

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
            {partieCreer && !partieTerminee && (
                <Row>
                    <Col>
                        <h2>Jouer un tour (Tour {tourActuel + 1}/{nbTours})</h2>
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
            {partieTerminee && (
                <div className="message-fin animate__animated animate__zoomIn">
                    <h2>{messageFinPartie}</h2>
                    <h4>Score final : Joueur 1 - {scoreJoueur1} | Joueur 2 - {scoreJoueur2}</h4>
                </div>
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
