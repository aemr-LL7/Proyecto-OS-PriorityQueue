/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 *
 * @author andre
 */
public class Administrator {

    private Studio firstStudio; // Star Wars
    private Studio secondStudio; // Star Trek
    private static double NEW_CHAR_PROB = 0.8;

    public Administrator(Studio studioStarWars, Studio studioStarTrek) {
        this.firstStudio = studioStarWars;
        this.secondStudio = studioStarTrek;
    }

    // Proveer peleador a AIProcessor para combate
    public Character provideFighter(String studioName) {
        Studio studioContext = (studioName.equalsIgnoreCase("Star Wars")) ? firstStudio : secondStudio;
        return selectFighter(studioContext);
    }

    // Seleccionador de personaje para la pelea por estudio
    public Character selectFighter(Studio studio) {
        if (!studio.getPrior0_queue().isEmpty()) {
            return (Character) studio.getPrior0_queue().pop();
        }

        if (!studio.getPrior1_queue().isEmpty()) {
            return (Character) studio.getPrior1_queue().pop();
        }

        if (!studio.getPrior2_queue().isEmpty()) {
            return (Character) studio.getPrior2_queue().pop();
        }

        return null;
    }

    // Elimina un personaje de las colas de prioridad de su estudio
    public void removeFighter(Character character) {
        Studio studioContext = (character.getSeries().equals("Star Wars")) ? firstStudio : secondStudio;

        int priorityLevel = character.getPrio_level();

        switch (priorityLevel) {
            case 0:
                studioContext.getPrior0_queue().remove(character);
                break;
            case 1:
                studioContext.getPrior1_queue().remove(character);
                break;
            case 2:
                studioContext.getPrior2_queue().remove(character);
                break;
            default:
                System.out.println("El personaje no tiene un nivel de prioridad valido!");
        }

        System.out.println("-- Personaje " + character.getName() + " ID:" + character.getId() + " fue eliminado de la simulacion --");
    }

    // Reencolar un character en la cola de prioridad especificada
    public void ReEnqueueFighter(Character character, int priorityLevel) {
        Studio studioContext = (character.getSeries().equals("Star Wars")) ? firstStudio : secondStudio;

        character.setPrio_level(priorityLevel);

        switch (priorityLevel) {
            case 0:
                studioContext.getPrior0_queue().insert(character);
                break;
            case 1:
                studioContext.getPrior1_queue().insert(character);
                break;
            case 2:
                studioContext.getPrior2_queue().insert(character);
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

    public void updateQueues(int cycleCounter) {

        // Cada ciclo revisa los personajes en inanición y gestiona los personajes nuevos
        this.handleStarvation(getFirstStudio());
        this.handleStarvation(getSecondStudio());

        // Cada dos ciclos de revisión
        if (cycleCounter % 2 == 0) {
            int isTimeForNewChar = (int) (Math.random() * 101);

            if (isTimeForNewChar < 80) {
                this.addNewCharToQueues();
            }

        }
        
        //refuerzo
        reinforceQueueUpdate(getFirstStudio());
        reinforceQueueUpdate(getSecondStudio());
    }

    private void reinforceQueueUpdate(Studio studio) {

        if (!studio.getReinforcement_queue().isEmpty()) {
            Character reinforcement = studio.getReinforcement_queue().pop();
            
            int willReinforce = (int) (Math.random() * 101);

            if (willReinforce < 40) {
                studio.getPrior0_queue().insert(reinforcement);
                reinforcement.setPrio_level(0);
            } else {
                studio.getReinforcement_queue().insert(reinforcement);
            }
        }
    }

    private void handleStarvation(Studio studio) {
        // Invoca el metodo starvationUpdate de Studio para manejar la inanicion y update de prioridades
        studio.starvationUpdate();
    }

    private void addNewCharToQueues() {
        Random random = new Random();
        if (random.nextDouble() < getNEW_CHAR_PROB()) {
            Character newStarWarsChar = this.getFirstStudio().createAndEnqueueCharacter();
            Character newStarTrekChar = this.getSecondStudio().createAndEnqueueCharacter();

            // Agregar personajes a las colas de prioridad según su nivel inicial
            this.insertCharByPriority(newStarWarsChar, this.getFirstStudio());
            this.insertCharByPriority(newStarTrekChar, this.getSecondStudio());
        }
    }

    private void insertCharByPriority(Character character, Studio studio) {
        int priorityLevel = character.getPrio_level();

        switch (priorityLevel) {
            case 0: // Excepcional
                studio.getPrior0_queue().insert(character);
                System.out.println("Personaje " + character.getId() + " insertado en la Cola de Prioridad 1 (Excepcional)");
                break;
            case 1: // Promedio
                studio.getPrior1_queue().insert(character);
                System.out.println("Personaje " + character.getId() + " insertado en la Cola de Prioridad 2 (Promedio)");
                break;
            case 2: // Deficiente
                studio.getPrior2_queue().insert(character);
                System.out.println("Personaje " + character.getId() + " insertado en la Cola de Prioridad 3 (Deficiente)");
                break;
            default:
                System.out.println("No se ha encontrado correctamente la prioridad del Personaje " + character.getId());
        }
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
     * @return the NEW_CHAR_PROB
     */
    public static double getNEW_CHAR_PROB() {
        return NEW_CHAR_PROB;
    }

    /**
     * @param aNEW_CHAR_PROB the NEW_CHAR_PROB to set
     */
    public static void setNEW_CHAR_PROB(double aNEW_CHAR_PROB) {
        NEW_CHAR_PROB = aNEW_CHAR_PROB;
    }

}
