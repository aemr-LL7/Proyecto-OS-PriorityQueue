/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import GUI.Principal;
import Main.App;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author andre
 */
public class Administrator extends Thread {

    private Studio firstStudio; // Star Wars
    private Studio secondStudio; // Star Trek
    private Semaphore semaphore;
    private AIProcessor AI;
    private int numRound = 0;

    public Administrator(Studio studioStarWars, Studio studioStarTrek, Semaphore mutex, AIProcessor ai) {
        this.firstStudio = studioStarWars;
        this.secondStudio = studioStarTrek;
        this.semaphore = mutex;
        this.AI = ai;
    }

    public void initializeSimulation() {
        // Creación de los personajes iniciales y asignacion de colas correspondientes (STUDIO)
        System.out.println("Iniciando la creación de personajes...");
        for (int i = 0; i < 20; i++) {
            getFirstStudio().createAndEnqueueCharacter();
            getSecondStudio().createAndEnqueueCharacter();
        }

        // INICIALIZAR IMAGENES DE LAS COLAS (PERSONAJES) EN LA GUI
        Principal.getPrincipalInstance().updateQueuesUI();
        // INICIAR VENTANA ARENA
        Principal.getPrincipalInstance().setVisible(true);

        try {
            this.getSemaphore().acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(Administrator.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Iniciar hilos
        this.start();
        this.getAI().start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("DURACION BATALLAS: " + this.getAI().getDuration());

                this.firstStudio.printQueueStatus();
                this.secondStudio.printQueueStatus();

                // Actualizar colas de refuerzo
                this.firstStudio.updateReinforcementQueue();
                this.secondStudio.updateReinforcementQueue();

                // Chequear dos ciclos de revision para crear nuevos personajes
                if (this.getNumRound() == 2) {
                    this.addNewCharToQueues();
                    this.setNumRound(0);
                }

                // Pasar los personajes seleccionados a la IA
                Character newSTARWARSFighter = getFirstStudio().getNextCharacterForBattle();
                Character newSTARTREKFighter = getSecondStudio().getNextCharacterForBattle();

                this.getAI().setStarWarsPlayer(newSTARWARSFighter);
                this.getAI().setStarTrekPlayer(newSTARTREKFighter);

                // Actualizar las colas en la UI
                //=> UI
                Principal.getPrincipalInstance().updateQueuesUI();
                this.getSemaphore().release();
                Thread.sleep(100);
                this.getSemaphore().acquire();

                this.setNumRound(this.getNumRound() + 1);

                // Subir las prioridades de los personajes en colas mas bajas
                this.getFirstStudio().updateStarvationCounters();
                this.getSecondStudio().updateStarvationCounters();

                // ACtualizar nuevamente la UI de colas
                //=> UI
                Principal.getPrincipalInstance().updateQueuesUI();

            } catch (InterruptedException ex) {
                Logger.getLogger(Administrator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void addNewCharToQueues() {
        double randomNum = Math.random();
        if (randomNum <= 0.8) {
            // Agregar personajes a las colas de prioridad según su nivel inicial
            this.getFirstStudio().createAndEnqueueCharacter();
            this.getSecondStudio().createAndEnqueueCharacter();
        }
    }

    // Reencolar un character en la cola de prioridad especificada
    public void ReEnqueueFighter(Character character, int priorityLevel) {
        Studio studioContext = (character.getSeries().equals("Star Wars")) ? firstStudio : secondStudio;

        character.setPriorityLevel(priorityLevel);

        switch (priorityLevel) {
            case 1:
                studioContext.getPrior1_queue().insert(character);
                break;
            case 2:
                studioContext.getPrior2_queue().insert(character);
                break;
            case 3:
                studioContext.getPrior3_queue().insert(character);
                break;
            default:
                System.out.println("El nivel de prioridad especificado no es valido!");
        }

        System.out.println("Personaje " + character.getName() + " ID:" + character.getId() + " reencolado en la Cola de Prioridad " + priorityLevel);
    }

    // Envía un personaje a la cola de refuerzo
    public void reinforceFighter(Character character) {
        Studio studioContext = (character.getSeries().equals("Star Wars")) ? firstStudio : secondStudio;
        studioContext.getReinforcement_queue().insert(character);
        character.resetStarvationCounter();
        character.setIsReinforced(true);

        System.out.println("Personaje " + character.getName() + " ID:" + character.getId() + " enviado a la Cola de Refuerzo.");
    }

//
//    // Proveer peleador a AIProcessor para combate
//    public Character provideFighter(String studioName) {
//        Studio studioContext = (studioName.equalsIgnoreCase("Star Wars")) ? firstStudio : secondStudio;
//        return selectFighter(studioContext);
//    }
//
//    // Seleccionador de personaje para la pelea por estudio
//    public Character selectFighter(Studio studio) {
//        if (!studio.getPrior0_queue().isEmpty()) {
//            return (Character) studio.getPrior0_queue().pop();
//        }
//
//        if (!studio.getPrior1_queue().isEmpty()) {
//            return (Character) studio.getPrior1_queue().pop();
//        }
//
//        if (!studio.getPrior2_queue().isEmpty()) {
//            return (Character) studio.getPrior2_queue().pop();
//        }
//
//        return null;
//    }
//
//    // Elimina un personaje de las colas de prioridad de su estudio
//    public void removeFighter(Character character) {
//        Studio studioContext = (character.getSeries().equals("Star Wars")) ? firstStudio : secondStudio;
//
//        int priorityLevel = character.getPrio_level();
//
//        switch (priorityLevel) {
//            case 0:
//                studioContext.getPrior0_queue().remove(character);
//                break;
//            case 1:
//                studioContext.getPrior1_queue().remove(character);
//                break;
//            case 2:
//                studioContext.getPrior2_queue().remove(character);
//                break;
//            default:
//                System.out.println("El personaje no tiene un nivel de prioridad valido!");
//        }
//
//        System.out.println("-- Personaje " + character.getName() + " ID:" + character.getId() + " fue eliminado de la simulacion --");
//    }
//
//
//    public void updateQueues(int cycleCounter) {
//
//        // Cada ciclo revisa los personajes en inanición y gestiona los personajes nuevos
//        this.handleStarvation(getFirstStudio());
//        this.handleStarvation(getSecondStudio());
//
//        // Cada dos ciclos de revisión
//        if (cycleCounter % 2 == 0) {
//            int isTimeForNewChar = (int) (Math.random() * 101);
//
//            if (isTimeForNewChar < 80) {
//                this.addNewCharToQueues();
//            }
//
//        }
//
//        //refuerzo
//        reinforceQueueUpdate(getFirstStudio());
//        reinforceQueueUpdate(getSecondStudio());
//    }
//
//    private void updateReinforcementQueue(Studio studio) {
//
//        if (!studio.getReinforcement_queue().isEmpty()) {
//            Character reinforcement = studio.getReinforcement_queue().pop();
//
//            int willReinforce = (int) (Math.random() * 101);
//
//            if (willReinforce <= 40) {
//                studio.getPrior0_queue().insert(reinforcement);
//                reinforcement.setPrio_level(0);
//            } else {
//                studio.getReinforcement_queue().insert(reinforcement);
//            }
//        }
//    }
//
//    private void handleStarvation(Studio studio) {
//        // Invoca el metodo starvationUpdate de Studio para manejar la inanicion y update de prioridades
//        studio.starvationUpdate();
//    }
//    private void insertCharByPriority(Character character, Studio studio) {
//        int priorityLevel = character.getPrio_level();
//
//        switch (priorityLevel) {
//            case 0: // Excepcional
//                studio.getPrior0_queue().insert(character);
//                System.out.println("Personaje " + character.getId() + " insertado en la Cola de Prioridad 1 (Excepcional)");
//                break;
//            case 1: // Promedio
//                studio.getPrior1_queue().insert(character);
//                System.out.println("Personaje " + character.getId() + " insertado en la Cola de Prioridad 2 (Promedio)");
//                break;
//            case 2: // Deficiente
//                studio.getPrior2_queue().insert(character);
//                System.out.println("Personaje " + character.getId() + " insertado en la Cola de Prioridad 3 (Deficiente)");
//                break;
//            default:
//                System.out.println("No se ha encontrado correctamente la prioridad del Personaje " + character.getId());
//        }
//    }
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
     * @return the numRound
     */
    public int getNumRound() {
        return numRound;
    }

    /**
     * @param numRound the numRound to set
     */
    public void setNumRound(int numRound) {
        this.numRound = numRound;
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
