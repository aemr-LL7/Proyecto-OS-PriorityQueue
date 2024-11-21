/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import EDD.OurQueue;
import EDD.SimpleList;
import GUI.Principal;
import Main.App;
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

    private int cycleCheck;
    private Semaphore semaphore;
    private SimpleList<Character> winners_list; //la lista de ganadores puede tener repetidos, para propositos del conteo de las victorias

    public Simulator(AIProcessor ai, Administrator admin, Semaphore mutex) {
        this.admin = App.getApp().getAdmin();
        this.AI = App.getApp().getIa();
        this.semaphore = mutex; // Control para gestionar el recurso compartido entre Simulator y AIProcessor
        this.winners_list = new SimpleList<>();
    }

    public void initializeSimulation() {
        // Creación de los personajes iniciales y asignacion de colas correspondientes (STUDIO)
        System.out.println("Iniciando la creación de personajes...");
        for (int i = 0; i < 20; i++) {
            Character character1 = getFirstStudio().createAndEnqueueCharacter();
            Character character2 = getSecondStudio().createAndEnqueueCharacter();
        }

        System.out.println("Personaje creado para el estudio: " + this.getFirstStudio().getStudioLabel());
        for (int i = 0; i < this.getFirstStudio().getChr_list().getSize(); i++) {
            Character starChar = this.getFirstStudio().getChr_list().getValueByIndex(i);
            System.out.println("ID: " + starChar.getId() + "\nNombre: " + starChar.getName() + "\nPrioridad: " + starChar.getPrio_level());
        }

        System.out.println("COLAS DE PRIORIDAD:\n    Cola 0 size: " + this.getFirstStudio().getPrior0_queue().getSize() + "\n    Cola 1 size: " + this.getFirstStudio().getPrior1_queue().getSize() + "\n    Cola 2 size: " + this.getFirstStudio().getPrior2_queue().getSize());

        System.out.println("Personaje creado para el estudio: " + this.getSecondStudio().getStudioLabel());
        for (int i = 0; i < this.getSecondStudio().getChr_list().getSize(); i++) {
            Character starChar = this.getSecondStudio().getChr_list().getValueByIndex(i);
            System.out.println("ID: " + starChar.getId() + "\nNombre: " + starChar.getName() + "\nPrioridad: " + starChar.getPrio_level());
        }

        System.out.println("COLAS DE PRIORIDAD:\n    Cola 0 size: " + this.getSecondStudio().getPrior0_queue().getSize() + "\n    Cola 1 size: " + this.getSecondStudio().getPrior1_queue().getSize() + "\n    Cola 2 size: " + this.getSecondStudio().getPrior2_queue().getSize());

        // INICIALIZAR IMAGENES DE LAS COLAS (PERSONAJES) EN LA GUI
        Principal.getPrincipalInstance().updateQueuesUI();
        // INICIAR VENTANA ARENA
        Principal.getPrincipalInstance().setVisible(true);

        try {
            semaphore.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(Administrator.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Iniciar hilos
        this.start();
        this.getAI().start();
    }

    @Override
    public void run() {
        try {
            while (!this.isDone()) {
                if (this.getAI().getStatus().equals("Waiting")) {
                    getSemaphore().release();
                    Thread.sleep(1000);  // Simula la espera de la IA para procesar el siguiente combate
                }
                if (this.getAI().getStatus().equals("Announcing")) {
                    getSemaphore().acquire();

                    this.cycleCheck++;
                    getAdmin().updateQueues(cycleCheck);

                    Character winner = this.getAI().getLastWinner();
                    if (winner != null) {
                        Studio winnerStudio = (winner.getSeries().equals("Star Wars")) ? firstStudio : secondStudio;
                        winner.setPrio_level(2);
                        winnerStudio.getPrior2_queue().insert(winner);
                        if (!this.getWinners_list().contains(winner)) {
                            this.getWinners_list().addStart(winner);
                        }
                    }

                    this.getAI().setStatus("Waiting");
                    getSemaphore().release();
                }
            }

            System.out.println("Simulación finalizada: tiempo de batalla completado.");
            displayFinalState();
        } catch (InterruptedException ex) {
            Logger.getLogger(Simulator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void displayFinalState() {
        System.out.println("\nEstado final de los personajes:");
        System.out.println(getFirstStudio().getStudioLabel() + " tiene los siguientes personajes:");
        for (int i = 0; i < getFirstStudio().getChr_list().getSize(); i++) {
            System.out.println("- " + getFirstStudio().getChr_list().getValueByIndex(i).getId());
        }

        System.out.println(getSecondStudio().getStudioLabel() + " tiene los siguientes personajes:");
        for (int i = 0; i < getSecondStudio().getChr_list().getSize(); i++) {
            System.out.println("- " + getSecondStudio().getChr_list().getValueByIndex(i).getId());
        }

        System.out.println("\nColas de prioridad:");
        displayQueue(getFirstStudio(), "prior0_queue");
        displayQueue(getFirstStudio(), "prior1_queue");
        displayQueue(getFirstStudio(), "prior2_queue");
        displayQueue(getSecondStudio(), "prior0_queue");
        displayQueue(getSecondStudio(), "prior1_queue");
        displayQueue(getSecondStudio(), "prior2_queue");

        System.out.println("\nColas de refuerzo:");
        displayQueue(getFirstStudio(), "reinforcement_queue");
        displayQueue(getSecondStudio(), "reinforcement_queue");
    }

    private void displayQueue(Studio studio, String queueName) {
        OurQueue<Character> queue = switch (queueName) {
            case "prior0_queue" ->
                studio.getPrior0_queue();
            case "prior1_queue" ->
                studio.getPrior1_queue();
            case "prior2_queue" ->
                studio.getPrior2_queue();
            case "reinforcement_queue" ->
                studio.getReinforcement_queue();
            default ->
                throw new IllegalStateException("Unexpected value: " + queueName);
        };

        System.out.println("Cola de " + studio.getStudioLabel() + ": " + queue);
    }
//
//    @Override
//    public void run() {
//        try {
//            //Tomar semaphore
//            this.getSemaphore().acquire();
//
//            // Iniciar el hilo de AIProcessor
//            getAI().start();
//
//            // Ciclo de control de simulación
//            while (!this.isDone()) {
//
//                if (this.getAI().getStatus().equals("Waiting")) {
//                    // Liberar el semáforo para permitir un ciclo de combate en AIProcessor
//                    getSemaphore().release();
//                }
//                if (this.getAI().getStatus().equals("Announcing")) {
//
//                    getSemaphore().acquire();
//
//                    this.cycleCheck++;
//
//                    //logica de colas
//                    getAdmin().updateQueues(cycleCheck);
//
//                    Character winner = this.getAI().getLastWinner();
//                    if (winner != null) {
//                        Studio winnerStudio = (winner.getSeries().equals("Star Wars")) ? firstStudio : secondStudio;
//                        winner.setPrio_level(2);
//                        winnerStudio.getPrior2_queue().insert(winner);//devolver al personaje a la cola de prio 2
//                        if (!this.getWinners_list().contains(winner)) {
//                            this.getWinners_list().addStart(winner);//Anadirlo en la lista de ganadores si no esta ya
//                        }
//
//                    }
//
//                    //Actualizamos el estado de la inteligencia y devolvemos el semaforo
//                    this.getAI().setStatus("Waiting");
//                    getSemaphore().release();
//                }
//
//            }
//
//            // Mostrar el estado final
//            System.out.println("Simulación finalizada: tiempo de batalla completado.");
//
//            // Mostrar los personajes restantes en cada estudio
//            System.out.println("\nEstado final de los personajes:");
//            System.out.println(getFirstStudio().getStudioLabel() + " tiene los siguientes personajes:");
//            for (int i = 0; i < getFirstStudio().getChr_list().getSize(); i++) {
//                System.out.println("- " + getFirstStudio().getChr_list().getValueByIndex(i).getId());
//            }
//
//            System.out.println(getSecondStudio().getStudioLabel() + " tiene los siguientes personajes:");
//            for (int i = 0; i < getSecondStudio().getChr_list().getSize(); i++) {
//                System.out.println("- " + getSecondStudio().getChr_list().getValueByIndex(i).getId());
//            }
//
//            // Mostrar las colas de prioridad y refuerzo
//            System.out.println("\nColas de prioridad:");
//            System.out.println("Cola de " + getFirstStudio().getStudioLabel() + ": " + getFirstStudio().getPrior0_queue());
//            System.out.println("Cola de " + getFirstStudio().getStudioLabel() + ": " + getFirstStudio().getPrior1_queue());
//            System.out.println("Cola de " + getFirstStudio().getStudioLabel() + ": " + getFirstStudio().getPrior2_queue());
//            System.out.println("Cola de " + getSecondStudio().getStudioLabel() + ": " + getSecondStudio().getPrior0_queue());
//            System.out.println("Cola de " + getSecondStudio().getStudioLabel() + ": " + getSecondStudio().getPrior1_queue());
//            System.out.println("Cola de " + getSecondStudio().getStudioLabel() + ": " + getSecondStudio().getPrior2_queue());
//
//            System.out.println("\nColas de refuerzo:");
//            System.out.println("Cola de refuerzo de " + getFirstStudio().getStudioLabel() + ": " + getFirstStudio().getReinforcement_queue());
//            System.out.println("Cola de refuerzo de " + getSecondStudio().getStudioLabel() + ": " + getSecondStudio().getReinforcement_queue());
//        } catch (InterruptedException ex) {
//            Logger.getLogger(Simulator.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

    private boolean isDone() {
        return false;//cambiar para que revise si alguno de los dos estudios se quedo sin cola
    }

    public Studio getFirstStudio() {
        return firstStudio;
    }

    public void setFirstStudio(Studio firstStudio) {
        this.firstStudio = firstStudio;
    }

    public Studio getSecondStudio() {
        return secondStudio;
    }

    public void setSecondStudio(Studio secondStudio) {
        this.secondStudio = secondStudio;
    }

    public Administrator getAdmin() {
        return admin;
    }

    public void setAdmin(Administrator admin) {
        this.admin = admin;
    }

    public AIProcessor getAI() {
        return AI;
    }

    public void setAI(AIProcessor AI) {
        this.AI = AI;
    }

    public int getCycleCheck() {
        return cycleCheck;
    }

    public void setCycleCheck(int cycleCheck) {
        this.cycleCheck = cycleCheck;
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }

    public void setSemaphore(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    public SimpleList<Character> getWinners_list() {
        return winners_list;
    }

    public void setWinners_list(SimpleList<Character> winners_list) {
        this.winners_list = winners_list;
    }

}
