/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import EDD.SimpleList;
import GUI.Principal;
import java.util.concurrent.Semaphore;

/**
 *
 * @author andre
 */
public class AIProcessor extends Thread {

    private Administrator admin;
    private int starTrekWins;
    private int starWarsWins;
    private Semaphore semaphore;
    private String status;
    private int duration; //int multiplicado por 1000 para simular segundos
    private Character lastWinner;
    private int lastCombatRounds;

    public AIProcessor(Administrator admin, Semaphore semaphore, int duration) {
        this.admin = admin;
        this.semaphore = semaphore;
        this.duration = duration;
        this.status = "Waiting";
        this.starTrekWins = 0;
        this.starWarsWins = 0;
        this.lastWinner = null;
    }

    private Character determineInitiativeWinner(Character fighter1, Character fighter2) {//check contest de iniciativa para una ronda.
        int fighter1Initiative = fighter1.rollInitiative();
        int fighter2Initiative = fighter2.rollInitiative();

        return (fighter1Initiative >= fighter2Initiative) ? fighter1 : fighter2;
    }

    private void round(Character first, Character second, int roundCounter) {

        System.out.println("inicia ronda: " + roundCounter + " " + first.getName() + " vs " + second.getName());

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

                //Aprovechamos el sistema de status para que solo se ejecute un combate cuando este waiting.
                if (this.status.equals("Waiting")) {

                    //tomamos el semaforo
                    this.semaphore.acquire();

                    // Cambiamos el status a deciding y simulamos el tiempo transcurrido
                    this.setStatus("Deciding");
                    sleep(1000 * duration/2);

                    // Obtiene los peleadores desde el administrador
                    Character fighter1 = getAdmin().provideFighter("Star Wars");
                    Character fighter2 = getAdmin().provideFighter("Star Trek");

                    if (fighter1 == null || fighter2 == null) {
                        System.out.println("Error: No se pudo asignar un luchador válido para uno o ambos estudios.");
                        return;
                    }

                    int willThereBe = isThereCombat(fighter1, fighter2);

                    if (willThereBe == 0) {

                        // Asignar imagenes al GUI
                        Principal.getMainFrameInstance().updateStarWarsImageIcon(fighter1.getCharacterImage());
                        Principal.getMainFrameInstance().updateStarTrekImageIcon(fighter2.getCharacterImage());

                        //contador de la vida de los personajes
                        int auxHp1 = fighter1.getHealth_pts();
                        int auxHp2 = fighter2.getHealth_pts();

                        Character first = this.determineInitiativeWinner(fighter1, fighter2);
                        Character second = (first == fighter1) ? fighter2 : fighter1;

                        int roundCounter = 1;

                        while (fighter1.getHealth_pts() > 0 && fighter2.getHealth_pts() > 0) {
                            this.round(first, second, roundCounter);
                            roundCounter++;
                        }

                        this.setLastCombatRounds(roundCounter);

                        Character winner;
                        Character loser;

                        if (fighter1.getHealth_pts() > fighter2.getHealth_pts()) {
                            winner = fighter1;
                            winner.setHealth_pts(auxHp1);

                            loser = fighter2;
                        } else {
                            winner = fighter2;
                            winner.setHealth_pts(auxHp2);

                            loser = fighter1;
                        }

                        logWinner(winner);

                    } else if (willThereBe == 1) {
                        System.out.println("EMPATE: Ambos luchadores se encolan a la cola de NIVEL 1");
                        this.setLastWinner(null);
                    } else if (willThereBe == 2) {
                        System.out.println("Sin Combate: Ambos luchadores han sido ingresados a la cola de refuerzo");
                        this.setLastWinner(null);
                    }

                    //Cambiamos el estado a anunciando y devolvemos el semaforo para que lo agarre simulators
                    this.setStatus("Announcing");
                    sleep(1000 * duration/2);
                    this.getSemaphore().release();
                }
            }
        } catch (InterruptedException e) {
            System.out.println("AIProcessor interrumpido: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private int isThereCombat(Character fighter1, Character fighter2) {
        fighter1.setStarvation_counter(0);
        fighter2.setStarvation_counter(0);

        int outCome = (int) (Math.random() * 100);

        if (outCome < 40) {
            return 0;

        } else if (outCome < 67) { // 27% de empate
            getAdmin().enqueueFighter(fighter1, 0);
            getAdmin().enqueueFighter(fighter2, 0);
            return 1;

        } else {
            getAdmin().reinforceFighter(fighter1);
            getAdmin().reinforceFighter(fighter2);
            return 2;
        }
    }

    /**
     * @return the admin
     */
    public Administrator getAdmin() {
        return admin;
    }

    /**
     * @param admin the admin to set
     */
    public void setAdmin(Administrator admin) {
        this.admin = admin;
    }

    /**
     * @return the winnersQueue
     */
    /**
     * @return the semaphore
     */
    public Semaphore getSemaphore() {
        return semaphore;
    }

    /**
     * @param semaphore the semaphore to set
     */
    public void setSemaphore(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public int getStarWarsWins() {
        return starWarsWins;
    }

    public int getLastCombatRounds() {
        return lastCombatRounds;
    }

    public void setLastCombatRounds(int lastCombatRounds) {
        this.lastCombatRounds = lastCombatRounds;
    }

}
