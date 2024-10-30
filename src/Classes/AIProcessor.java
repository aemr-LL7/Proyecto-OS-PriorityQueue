/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import EDD.OurQueue;
import java.util.Random;

/**
 *
 * @author andre
 */
public class AIProcessor extends Thread {

    private Administrator admin;
    private Studio firstStudio; // Star Wars
    private Studio secondStudio; // Star Trek
    private OurQueue<Character> winnersQueue;
    private Object lock = new Object();

    public AIProcessor(Administrator admin, Studio firstStudio, Studio secondStudio) {
        this.admin = admin;
        this.firstStudio = firstStudio;
        this.secondStudio = secondStudio;
        this.winnersQueue = new OurQueue<>();
    }

    private Character determineWinner(Character fighter1, Character fighter2) {
        if (fighter1.getStrength_pts() > fighter2.getStrength_pts()) {
            return fighter1;
        }
        if (fighter1.getStrength_pts() < fighter2.getStrength_pts()) {
            return fighter2;
        }
        return (fighter1.getAgility_pts() >= fighter2.getAgility_pts()) ? fighter1 : fighter2;
    }

    private void logWinner(Character winner) {
        this.getWinnersQueue().insert(winner);
    }

    public void executeCombat(Character fighter1, Character fighter2) throws InterruptedException {
        synchronized (lock) {

            if (fighter1 == null || fighter2 == null) {
                System.out.println("No hay suficientes personajes para iniciar un combate.");
                return;
            }

            System.out.println("Procesando combate entre " + fighter1.getId() + " (Star Wars) y " + fighter2.getId() + " (Star Trek)...");

            // Simula el tiempo que toma la IA en procesar el combate (10 segundos)
            Thread.sleep(10000);

            Random randomNum = new Random();
            int outcome = randomNum.nextInt(100);

            if (outcome < 40) { // 40% de que haya un ganador
                Character winner = determineWinner(fighter1, fighter2);
                this.logWinner(winner);

                System.out.println("GANADOR: " + winner.getId() + " de " + (winner == fighter1 ? "Star Wars" : "Star Trek"));
                System.out.println("El perdedor ha sido eliminado de la simulación.");

                // Eliminar el perdedor de la simulación
                if (winner.getId().equals(fighter1.getId())) {
                    secondStudio.removeCharacter(fighter2);
                } else {
                    firstStudio.removeCharacter(fighter1);
                }

            } else if (outcome < 67) { // 27% de empate
                firstStudio.getPrior1_queue().insert(fighter1);
                secondStudio.getPrior1_queue().insert(fighter2);
                System.out.println("EMPATE: Ambos luchadores se encolan a la cola de NIVEL 1");

            } else { // 33% de no llevarse a cabo el combate
                firstStudio.getReinforcement_queue().insert(fighter1);
                secondStudio.getReinforcement_queue().insert(fighter2);
                System.out.println("Sin Combate: Ambos luchadores han sido ingresado a la cola de refuerzo");
            }
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
     * @return the firstStudio
     */
    public Studio getFirstStudio() {
        return firstStudio;
    }

    /**
     * @param firstStudio the firstStudio to set
     */
    public void setFirstStudio(Studio firstStudio) {
        this.firstStudio = firstStudio;
    }

    /**
     * @return the secondStudio
     */
    public Studio getSecondStudio() {
        return secondStudio;
    }

    /**
     * @param secondStudio the secondStudio to set
     */
    public void setSecondStudio(Studio secondStudio) {
        this.secondStudio = secondStudio;
    }

    /**
     * @return the winnersQueue
     */
    public OurQueue<Character> getWinnersQueue() {
        return winnersQueue;
    }

    /**
     * @param winnersQueue the winnersQueue to set
     */
    public void setWinnersQueue(OurQueue<Character> winnersQueue) {
        this.winnersQueue = winnersQueue;
    }

}
