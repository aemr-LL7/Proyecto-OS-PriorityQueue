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
    private Studio starWarsStudio;
    private Studio starTrekStudio;
    private Semaphore semaphore;
    private String status;

    private int starTrekWins = 0;
    private int starWarsWins = 0;
    private int duration; //int multiplicado por 1000 para simular segundos

    private Character lastWinner;
    private int lastCombatRounds;

    public AIProcessor() {
        this.admin = App.getApp().getAdmin();
        this.semaphore = App.getApp().getSemaphore();
        this.duration = App.getApp().getBattleDuration();
        this.status = "Waiting";
        this.lastWinner = null;
    }

    private Character determineInitiativeWinner(Character fighter1, Character fighter2) {//check contest de iniciativa para una ronda.
        int fighter1Initiative = fighter1.rollInitiative();
        int fighter2Initiative = fighter2.rollInitiative();

        return (fighter1Initiative >= fighter2Initiative) ? fighter1 : fighter2;
    }

    private void round(Character first, Character second, int roundCounter) {
        // ACTUALIZAR ANUNCIADOR (GUI)
        System.out.println("inicia ronda: " + roundCounter + " " + first.getName() + " vs " + second.getName());
        Principal.getPrincipalInstance().getAnnouncerLabel().setText("INICIA RONDA: " + first.getName() + " vs " + second.getName());

        //Ataca el primer personaje.
        int firstAttackRoll = first.rollAttack();

        //Defiende el segundo personaje.
        int secondDefenceRoll = second.rollDefence();

        //Se resta la fuerza del primero menos la defensa del segundo contra el segundo si logra defender el ataque.
        if (secondDefenceRoll >= firstAttackRoll) {
            second.takeDamage(first.getStrength_pts() - second.getBlock_factor());
        } else if (firstAttackRoll > secondDefenceRoll) {
            //Se resta toda la fuerza del primero si no logra defender el segundo.
            second.takeDamage(first.getStrength_pts());
        }

        //Ataca el segundo personaje
        int secondAttackRoll = second.rollAttack();

        //Defiende el primer personaje
        int firstDefenceRoll = first.rollAttack();

        if (firstDefenceRoll >= secondAttackRoll) {
            first.takeDamage(second.getStrength_pts() - first.getBlock_factor());
        } else if (secondAttackRoll > firstDefenceRoll) {
            first.takeDamage(second.getStrength_pts());
        }

    }

    private void logWinner(Character winner) {

        if (winner.getSeries().equals("Star Wars")) {
            this.starWarsWins++;
        } else {
            this.starTrekWins++;
        }

        this.setLastWinner(winner);
        winner.addWin();

    }

    @Override
    public void run() {
        try {
            while (true) {
                if (this.status.equals("Waiting")) {
                    this.semaphore.acquire();

                    this.setStatus("Deciding");
                    // ACTUALIZAR ESTADO DE AI EN LA GUI
                    Principal.getPrincipalInstance().getIAStatusLabel().setText("Deciding");
                    Thread.sleep(1000 * duration / 2);

                    Character fighter1 = getAdmin().provideFighter("Star Wars");
                    Character fighter2 = getAdmin().provideFighter("Star Trek");

                    System.out.println("PERSONAJESSSSSSS: " + fighter1.getName() + " " + fighter2.getName());

                    if (fighter1 != null && fighter2 != null) {
                         processCombat(fighter1, fighter2);
                    } else {
                        System.out.println("No se pudieron obtener luchadores válidos.");
                        this.setStatus("Waiting");
                        // ACTUALIZAR ESTADO DE AI EN LA GUI
                        Principal.getPrincipalInstance().getIAStatusLabel().setText("Waiting");
                        this.semaphore.release();
                        continue;
                    }

                    Thread.sleep(1000 * duration / 2);
                    this.setStatus("Announcing");
                    // ACTUALIZAR ESTADO DE AI EN LA GUI
                    Principal.getPrincipalInstance().getIAStatusLabel().setText("Announcing");
                    this.semaphore.release();
                }
            }
        } catch (InterruptedException e) {
            System.out.println("AIProcessor interrumpido: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private void processCombat(Character fighter1, Character fighter2) {
        int outcome = isThereCombat(fighter1, fighter2);

        if (outcome == 0) {
            System.out.println("Combate entre " + fighter1.getName() + " y " + fighter2.getName());
            // ACTUALIZAR ANUNCIADOR CON LA INFO DE LA BATALLA
            Principal.getPrincipalInstance().getAnnouncerLabel().setText("Combate entre " + fighter1.getName() + " y " + fighter2.getName() + ".");

            // MOSTRAR INFORMACION DE LOS PELEADORES (GUI)
            this.updateFighterCardsUI(fighter1, fighter2);

            Character first = determineInitiativeWinner(fighter1, fighter2);
            Character second = (first == fighter1) ? fighter2 : fighter1;

            // SETTEAR NUMERO DE ROUND EN LA ARENA
            int roundCounter = 1;
            Principal.getPrincipalInstance().getRoundCounterLabel().setText(String.valueOf(roundCounter));

            while (fighter1.getHealth_pts() > 0 && fighter2.getHealth_pts() > 0) {
                round(first, second, roundCounter);
                roundCounter++;
                System.out.println("Rondita : " + roundCounter);;
            }

            this.setStatus("Announcing");
            Character winner = (fighter1.getHealth_pts() > 0) ? fighter1 : fighter2;
            logWinner(winner);

        } else if (outcome == 1) {
            System.out.println("EMPATE: Ambos luchadores se encolan a la cola de NIVEL 1");
            Principal.getPrincipalInstance().getAnnouncerLabel().setText("EMPATE: Ambos luchadores se encolan a la cola de NIVEL 1");

            getAdmin().ReEnqueueFighter(fighter1, 0);
            getAdmin().ReEnqueueFighter(fighter2, 0);
            this.setStatus("Announcing");
            this.setLastWinner(null);
        } else if (outcome == 2) {
            System.out.println("Sin Combate: Ambos luchadores han sido ingresados a la cola de refuerzo");
            Principal.getPrincipalInstance().getAnnouncerLabel().setText("Sin Combate: Ambos luchadores ingresan a la cola de refuerzo");

            getAdmin().reinforceFighter(fighter1);
            getAdmin().reinforceFighter(fighter2);
            this.setLastWinner(null);
            this.setStatus("Announcing");
        }

    }

    private int isThereCombat(Character fighter1, Character fighter2) {
        fighter1.setStarvation_counter(0);
        fighter2.setStarvation_counter(0);

        int outCome = (int) (Math.random() * 100);

        if (outCome < 40) {
            return 0;

        } else if (outCome < 67) { // 27% de empate

            return 1;

        } else {
            return 2;
        }
    }

    private void updateFighterCardsUI(Character fighter1, Character fighter2) {
        // MOSTRAR ATRIBUTOS DE LOS PELEADORES
        // STAR WARS
        Principal.getPrincipalInstance().getSwHPLabel().setText(String.valueOf(fighter1.getHealth_pts()));
        Principal.getPrincipalInstance().getSwDefenseLabel().setText(String.valueOf(fighter1.getStrength_pts()));
        Principal.getPrincipalInstance().getSwAgilityLabel().setText(String.valueOf(fighter1.getAttackModifier()));
        Principal.getPrincipalInstance().getSwFighterNameLabel().setText(fighter1.getId());
        // STAR TREK
        Principal.getPrincipalInstance().getStHPLabel().setText(String.valueOf(fighter2.getHealth_pts()));
        Principal.getPrincipalInstance().getStDefenseLabel().setText(String.valueOf(fighter2.getStrength_pts()));
        Principal.getPrincipalInstance().getStAgilityLabel().setText(String.valueOf(fighter2.getAttackModifier()));
        Principal.getPrincipalInstance().getStFighterNameLabel().setText(fighter2.getId());
        // INSERTAR IMAGENES DE LOS LUCHADORES
        ImagesManager IMGManager = Principal.getPrincipalInstance().getImagesManager();
        // Para star wars
        ImageIcon starWarsIcon = IMGManager.reScaleImage(fighter1.getCharacterImage(), 204, 214); // se redimensiona la imagen
        Principal.getPrincipalInstance().getSWImageLabel().setIcon(starWarsIcon);
        // Para star trek
        ImageIcon starTrekIcon = IMGManager.reScaleImage(fighter2.getCharacterImage(), 204, 214); // se redimensiona la imagen
        Principal.getPrincipalInstance().getSTImageLabel().setIcon(starTrekIcon);
    }

    public Administrator getAdmin() {
        return admin;
    }

    public void setAdmin(Administrator admin) {
        this.admin = admin;
    }

    public Studio getStarWarsStudio() {
        return starWarsStudio;
    }

    public void setStarWarsStudio(Studio starWarsStudio) {
        this.starWarsStudio = starWarsStudio;
    }

    public Studio getStarTrekStudio() {
        return starTrekStudio;
    }

    public void setStarTrekStudio(Studio starTrekStudio) {
        this.starTrekStudio = starTrekStudio;
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

}
