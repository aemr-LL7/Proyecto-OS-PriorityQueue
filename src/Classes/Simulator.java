/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import EDD.SimpleList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author andre
 */
public class Simulator extends Thread {

    private Studio firstStudio;
    private Studio secondStudio;
    private Administrator admin;
    private AIProcessor AI;
    private int duration;
    private int cycleCheck;
    private Semaphore semaphore;
    private SimpleList<Character> winners_list; //la lista de ganadores puede tener repetidos, para propositos del conteo de las victorias

    public Simulator(String labelStudio1, String labelStudio2, int battleDuration) {
        this.firstStudio = new Studio(labelStudio1);
        this.secondStudio = new Studio(labelStudio2);
        this.semaphore = new Semaphore(1); // Control para gestionar el recurso compartido entre Simulator y AIProcessor
        this.admin = new Administrator(getFirstStudio(), getSecondStudio(), this.getCycleCheck());
        this.AI = new AIProcessor(getAdmin(), getSemaphore(), this.duration);
        this.duration = battleDuration;
        this.winners_list = new SimpleList<>();
    }

    @Override
    public void run() {
        try {
            //Tomar semaphore
            this.getSemaphore().acquire();
            
            // Creación de los personajes iniciales usando getters y setters
            System.out.println("Iniciando la creación de personajes...");
            for (int i = 0; i < 20; i++) {
                Character character1 = getFirstStudio().createCharacter();
                Character character2 = getSecondStudio().createCharacter();
                System.out.println("Personaje creado para " + getFirstStudio().getStudioLabel() + ": " + character1.getId());
                System.out.println("Personaje creado para " + getSecondStudio().getStudioLabel() + ": " + character2.getId());
            }

            // Iniciar el hilo de AIProcessor
            getAI().start();

            // Ciclo de control de simulación
            while (!this.isDone()) {

                if (this.getAI().getStatus().equals("Waiting")) {
                    // Liberar el semáforo para permitir un ciclo de combate en AIProcessor
                    getSemaphore().release();
                }
                if (this.getAI().getStatus().equals("Announcing")) {

                    getSemaphore().acquire();

                    this.cycleCheck++;

                    //logica de colas
                    getAdmin().updateQueues(cycleCheck);

                    Character winner = this.getAI().getLastWinner();
                    if (winner != null) {
                        Studio winnerStudio = (winner.getSeries().equals("Star Wars")) ? firstStudio : secondStudio;
                        winner.setPrio_level(2);
                        winnerStudio.getPrior2_queue().insert(winner);//devolver al personaje a la cola de prio 2
                        if(!this.getWinners_list().contains(winner)){
                            this.getWinners_list().addStart(winner);//Anadirlo en la lista de ganadores si no esta ya
                        }
                        

                    }

                    //Actualizamos el estado de la inteligencia y devolvemos el semaforo
                    this.getAI().setStatus("Waiting");
                    getSemaphore().release();
                }

            }

            // Mostrar el estado final
            System.out.println("Simulación finalizada: tiempo de batalla completado.");

            // Mostrar los personajes restantes en cada estudio
            System.out.println("\nEstado final de los personajes:");
            System.out.println(getFirstStudio().getStudioLabel() + " tiene los siguientes personajes:");
            for (int i = 0; i < getFirstStudio().getChr_list().getSize(); i++) {
                System.out.println("- " + getFirstStudio().getChr_list().getValueByIndex(i).getId());
            }

            System.out.println(getSecondStudio().getStudioLabel() + " tiene los siguientes personajes:");
            for (int i = 0; i < getSecondStudio().getChr_list().getSize(); i++) {
                System.out.println("- " + getSecondStudio().getChr_list().getValueByIndex(i).getId());
            }

            // Mostrar las colas de prioridad y refuerzo
            System.out.println("\nColas de prioridad:");
            System.out.println("Cola de " + getFirstStudio().getStudioLabel() + ": " + getFirstStudio().getPrior0_queue());
            System.out.println("Cola de " + getFirstStudio().getStudioLabel() + ": " + getFirstStudio().getPrior1_queue());
            System.out.println("Cola de " + getFirstStudio().getStudioLabel() + ": " + getFirstStudio().getPrior2_queue());
            System.out.println("Cola de " + getSecondStudio().getStudioLabel() + ": " + getSecondStudio().getPrior0_queue());
            System.out.println("Cola de " + getSecondStudio().getStudioLabel() + ": " + getSecondStudio().getPrior1_queue());
            System.out.println("Cola de " + getSecondStudio().getStudioLabel() + ": " + getSecondStudio().getPrior2_queue());

            System.out.println("\nColas de refuerzo:");
            System.out.println("Cola de refuerzo de " + getFirstStudio().getStudioLabel() + ": " + getFirstStudio().getReinforcement_queue());
            System.out.println("Cola de refuerzo de " + getSecondStudio().getStudioLabel() + ": " + getSecondStudio().getReinforcement_queue());
        } catch (InterruptedException ex) {
            Logger.getLogger(Simulator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean isDone() {
        return false;//cambiar para que revise si alguno de los dos estudios se quedo sin cola
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
     * @return the AI
     */
    public AIProcessor getAI() {
        return AI;
    }

    /**
     * @param AI the AI to set
     */
    public void setAI(AIProcessor AI) {
        this.AI = AI;
    }

    /**
     * @return the duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * @return the cycleCheck
     */
    public int getCycleCheck() {
        return cycleCheck;
    }

    /**
     * @param cycleCheck the cycleCheck to set
     */
    public void setCycleCheck(int cycleCheck) {
        this.cycleCheck = cycleCheck;
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

    public SimpleList<Character> getWinners_list() {
        return winners_list;
    }

}
