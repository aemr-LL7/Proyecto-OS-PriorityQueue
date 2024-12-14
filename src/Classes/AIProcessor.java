/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import EDD.SimpleList;
import GUI.Principal;
import Main.App;
import Managers.ImagesManager;
import java.util.concurrent.Semaphore;
import javax.swing.ImageIcon;

/**
 *
 * @author andre
 */
public class AIProcessor extends Thread {

    private Administrator admin;
    private Character starWarsPlayer;
    private Character starTrekPlayer;
    private Semaphore semaphore;
    private String status;

    private int starTrekWins = 0;
    private int starWarsWins = 0;
    private int duration; //int multiplicado por 1000 para simular segundos

    private Character lastWinner;
    private int round;
    private int lastCombatRounds;

    public AIProcessor() {
        this.admin = App.getApp().getAdmin();
        this.semaphore = App.getApp().getSemaphore();
        this.duration = App.getApp().getBattleDuration();
        this.status = "Waiting";
        this.round = 0;
        this.lastWinner = null;
    }

    private Character determineInitiativeWinner(Character fighter1, Character fighter2) {//check contest de iniciativa para una ronda.
        int fighter1Initiative = fighter1.rollInitiative();
        int fighter2Initiative = fighter2.rollInitiative();

        return (fighter1Initiative >= fighter2Initiative) ? fighter1 : fighter2;
    }

    private void roundFight(Character first, Character second, int roundCounter) {
        // ACTUALIZAR ANUNCIADOR (GUI)
        System.out.println("inicia ronda: " + roundCounter + " " + first.getName() + " vs " + second.getName());
        Principal.getPrincipalInstance().getAnnouncerLabel().setText("INICIA RONDA: " + first.getName() + " vs " + second.getName());

        //Ataca el primer personaje.
        int firstAttackRoll = first.rollAttack();

        //Defiende el segundo personaje.
        int secondDefenceRoll = second.rollDefence();

        //Se resta la fuerza del primero menos la defensa del segundo contra el segundo si logra defender el ataque.
        if (secondDefenceRoll >= firstAttackRoll) {
            second.takeDamage(first.getStrength() - second.getBlock_factor());
        } else if (firstAttackRoll > secondDefenceRoll) {
            //Se resta toda la fuerza del primero si no logra defender el segundo.
            second.takeDamage(first.getStrength());
        }

        //Ataca el segundo personaje
        int secondAttackRoll = second.rollAttack();

        //Defiende el primer personaje
        int firstDefenceRoll = first.rollAttack();

        if (firstDefenceRoll >= secondAttackRoll) {
            first.takeDamage(second.getStrength() - first.getBlock_factor());
        } else if (secondAttackRoll > firstDefenceRoll) {
            first.takeDamage(second.getStrength());
        }

    }

//    private void logWinner(Character winner) {
//
//        if (winner.getSeries().equals("Star Wars")) {
//            this.starWarsWins++;
//        } else {
//            this.starTrekWins++;
//        }
//
//        this.setLastWinner(winner);
//        winner.addWin();
//
//    }

    @Override
    public void run() {
        while (true) {
            try {
                if (this.status.equals("Waiting")) {
                    this.semaphore.acquire();
                    this.setStatus("Deciding");

                    this.round += 1;

                    // Provisión de luchadores usando métodos centralizados
                    Character fighter1 = this.getStarWarsPlayer();
                    Character fighter2 = this.getStarTrekPlayer();

                    if (fighter1 != null && fighter2 != null) {
                        // SETTEAR NUMERO DE ROUND EN LA ARENA
                        Principal.getPrincipalInstance().getRoundCounterLabel().setText(String.valueOf(this.round));

                        double aux = Math.random();

                        if (aux <= 0.4) {
                            System.out.println("Combate entre " + fighter1.getName() + " y " + fighter2.getName());
                            // ACTUALIZAR ANUNCIADOR CON LA INFO DE LA BATALLA
                            Principal.getPrincipalInstance().getAnnouncerLabel().setText("Combate entre " + fighter1.getName() + " y " + fighter2.getName() + ".");

                            // MOSTRAR INFORMACION DE LOS PELEADORES (GUI)
                            this.updateFighterCardsUI(fighter1, fighter2);

                            Character first = determineInitiativeWinner(fighter1, fighter2);
                            Character second = (first == fighter1) ? fighter2 : fighter1;

                            while (fighter1.getHealth() > 0 && fighter2.getHealth() > 0) {
                                this.roundFight(first, second, this.round);
                                System.out.println("Rondita : " + this.round);
                            }

                            this.setStatus("Announcing");
                            Character winner = (fighter1.getHealth() > 0) ? fighter1 : fighter2;
                            Character loser = (fighter1.getHealth() <= 0) ? fighter1 : fighter2;
                            winner.incrementWins();

                            // Eliminar al perdedor de la simulacion
                            if (loser.getSeries().equalsIgnoreCase("Star Wars")) {
                                this.getAdmin().getFirstStudio().registerLoser(loser);
                                this.getAdmin().getFirstStudio().removeCharacter(loser);
                            } else {
                                this.getAdmin().getSecondStudio().registerLoser(loser);
                                this.getAdmin().getSecondStudio().removeCharacter(loser);
                            }
                            
                            Thread.sleep((long) ((this.getDuration() * 1000 * 0.3) * 0.6));

                        } else if (aux > 0.40 && aux <= 0.67) {
                            System.out.println("EMPATE: Ambos luchadores se encolan a la cola de NIVEL 1");
                            Principal.getPrincipalInstance().getAnnouncerLabel().setText("EMPATE: Ambos luchadores se encolan a la cola de NIVEL 1");

                            this.getAdmin().ReEnqueueFighter(fighter1, 1);
                            this.getAdmin().ReEnqueueFighter(fighter2, 1);

                            this.setLastWinner(null);
                            Thread.sleep((long) ((this.getDuration() * 1000 * 0.3) * 0.6));

                        } else {
                            System.out.println("Sin Combate: Ambos luchadores han sido ingresados a la cola de refuerzo");
                            Principal.getPrincipalInstance().getAnnouncerLabel().setText("Sin Combate: Ambos luchadores ingresan a la cola de refuerzo");
                            Thread.sleep(this.getDuration() * 1000);

                            this.getAdmin().reinforceFighter(fighter1);
                            this.getAdmin().reinforceFighter(fighter2);
                            this.setLastWinner(null);
                        }

                    } else {
                        this.handleMissingFighters();
                    }

                    // Actualizar fighter UI
                    // UI=>
                    //Delegar trabajos
                    Thread.sleep(1000 * duration / 2);
                    this.setStatus("Announcing");
                    this.semaphore.release();
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                System.out.println("AIProcessor interrumpido: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    public void handleMissingFighters() {
    }

    private void updateFighterCardsUI(Character fighter1, Character fighter2) {
        // MOSTRAR ATRIBUTOS DE LOS PELEADORES
        // STAR WARS
        Principal.getPrincipalInstance().getSwHPLabel().setText(String.valueOf(fighter1.getHealth()));
        Principal.getPrincipalInstance().getSwDefenseLabel().setText(String.valueOf(fighter1.getStrength()));
        Principal.getPrincipalInstance().getSwAgilityLabel().setText(String.valueOf(fighter1.getAgility()));
        Principal.getPrincipalInstance().getSwFighterNameLabel().setText(fighter1.getId());
        // STAR TREK
        Principal.getPrincipalInstance().getStHPLabel().setText(String.valueOf(fighter2.getHealth()));
        Principal.getPrincipalInstance().getStDefenseLabel().setText(String.valueOf(fighter2.getStrength()));
        Principal.getPrincipalInstance().getStAgilityLabel().setText(String.valueOf(fighter2.getAgility()));
        Principal.getPrincipalInstance().getStFighterNameLabel().setText(fighter2.getId());
        // INSERTAR IMAGENES DE LOS LUCHADORES
        ImagesManager IMGManager = Principal.getPrincipalInstance().getImagesManager();
        // Para star wars
        ImageIcon starWarsIcon = IMGManager.reScaleImage(fighter1.getCharacterImage(), 196, 237); // se redimensiona la imagen
        Principal.getPrincipalInstance().getSWImageLabel().setIcon(starWarsIcon);
        // Para star trek
        ImageIcon starTrekIcon = IMGManager.reScaleImage(fighter2.getCharacterImage(), 196, 237); // se redimensiona la imagen
        Principal.getPrincipalInstance().getSTImageLabel().setIcon(starTrekIcon);
    }

    public Administrator getAdmin() {
        return admin;
    }

    public void setAdmin(Administrator admin) {
        this.admin = admin;
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }

    public void setSemaphore(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStarTrekWins() {
        return starTrekWins;
    }

    public void setStarTrekWins(int starTrekWins) {
        this.starTrekWins = starTrekWins;
    }

    public int getStarWarsWins() {
        return starWarsWins;
    }

    public void setStarWarsWins(int starWarsWins) {
        this.starWarsWins = starWarsWins;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Character getLastWinner() {
        return lastWinner;
    }

    public void setLastWinner(Character lastWinner) {
        this.lastWinner = lastWinner;
    }

    public int getLastCombatRounds() {
        return lastCombatRounds;
    }

    public void setLastCombatRounds(int lastCombatRounds) {
        this.lastCombatRounds = lastCombatRounds;
    }

    /**
     * @return the starWarsPlayer
     */
    public Character getStarWarsPlayer() {
        return starWarsPlayer;
    }

    /**
     * @param starWarsPlayer the starWarsPlayer to set
     */
    public void setStarWarsPlayer(Character starWarsPlayer) {
        this.starWarsPlayer = starWarsPlayer;
    }

    /**
     * @return the starTrekPlayer
     */
    public Character getStarTrekPlayer() {
        return starTrekPlayer;
    }

    /**
     * @param starTrekPlayer the starTrekPlayer to set
     */
    public void setStarTrekPlayer(Character starTrekPlayer) {
        this.starTrekPlayer = starTrekPlayer;
    }

}
