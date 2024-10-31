/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import EDD.OurQueue;
import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 *
 * @author andre
 */
public class AIProcessor extends Thread {

    private Administrator admin;
    private OurQueue<Character> winnersQueue;
    private Semaphore semaphore;
    private Object lock = new Object();

    public AIProcessor(Administrator admin, Semaphore semaphore) {
        this.admin = admin;
        this.semaphore = semaphore;
        this.winnersQueue = new OurQueue<>();
    }

    private Character determineWinner(Character fighter1, Character fighter2) {
        if (fighter1.getStrength_pts() > fighter2.getStrength_pts()) {
            return fighter1;
        }
        if (fighter1.getStrength_pts() < fighter2.getStrength_pts()) {
            return fighter2;
        }
        return (fighter1.getAgilityModifier() >= fighter2.getAgilityModifier()) ? fighter1 : fighter2;
    }

    private void logWinner(Character winner) {
        this.getWinnersQueue().insert(winner);
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Espera señal del semáforo antes de iniciar un combate
                this.getSemaphore().acquire();

                // Obtiene los peleadores desde el administrador
                Character fighter1 = getAdmin().provideFighter("Star Wars");
                Character fighter2 = getAdmin().provideFighter("Star Trek");

                this.executeCombat(fighter1, fighter2);

            } catch (InterruptedException e) {
                System.out.println("AIProcessor interrumpido: " + e.getMessage());
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void executeCombat(Character fighter1, Character fighter2) throws InterruptedException {
        synchronized (lock) {

            if (fighter1 == null || fighter2 == null) {
                System.out.println("Uno de los estudios se ha quedado sin peleadores. Finalizando simulacion--------");
                return;
            }

            System.out.println("Procesando combate entre " + fighter1.getId() + " (Star Wars) y " + fighter2.getId() + " (Star Trek)...");

            // Simula el tiempo que toma la IA en procesar el combate (10 segundos)
            Thread.sleep(10000);

            Random randomNum = new Random();
            int outcome = randomNum.nextInt(100);

            if (outcome < 40) { // 40% de que haya un ganador
                Character winner = determineWinner(fighter1, fighter2);
                logWinner(winner);

                System.out.println("GANADOR: " + winner.getId() + " de " + (winner == fighter1 ? "Star Wars" : "Star Trek"));

                // Eliminar el perdedor de la simulación
                if (winner.getId().equals(fighter1.getId())) {
                    getAdmin().removeFighter(fighter2);
                    System.out.println("El perdedor ha sido eliminado de la simulacion");
                } else {
                    getAdmin().removeFighter(fighter1);
                    System.out.println("El perdedor ha sido eliminado de la simulacion");
                }

            } else if (outcome < 67) { // 27% de empate
                getAdmin().enqueueFighter(fighter1, 0);
                getAdmin().enqueueFighter(fighter2, 0);
                System.out.println("EMPATE: Ambos luchadores se encolan a la cola de NIVEL 1");

            } else { // 33% de no llevarse a cabo el combate
                getAdmin().reinforceFighter(fighter1);
                getAdmin().reinforceFighter(fighter2);
                System.out.println("Sin Combate: Ambos luchadores han sido ingresados a la cola de refuerzo");
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

}
