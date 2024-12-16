/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import EDD.OurQueue;
import EDD.SimpleList;
import GUI.Principal;
import Managers.ImagesManager;

/**
 *
 * @author andre
 */
public class Studio {

    //Studio info section
    private String studioLabel;
    private int idCounter;

    //Queue section
    private SimpleList<Character> losers_list;
    private OurQueue<Character> prior1_queue;
    private OurQueue<Character> prior2_queue;
    private OurQueue<Character> prior3_queue;
    private OurQueue<Character> reinforcement_queue;

    public Studio(String studioLabel) {
        this.studioLabel = studioLabel;
        this.prior1_queue = new OurQueue<Character>();
        this.prior2_queue = new OurQueue<Character>();
        this.prior3_queue = new OurQueue<Character>();
        this.reinforcement_queue = new OurQueue<Character>();
        this.losers_list = new SimpleList<Character>(); // Inicializa la lista de personajes
        this.idCounter = 0;
    }

    // Estado actual de las colas
    public void printQueueStatus() {
        System.out.println("Estado de las colas para el estudio: " + this.getStudioLabel());
        System.out.println("Prioridad 1: " + this.getPrior1_queue().getSize() + " personajes.");
        System.out.println("Prioridad 2: " + this.getPrior2_queue().getSize() + " personajes.");
        System.out.println("Prioridad 3: " + this.getPrior3_queue().getSize() + " personajes.");
        System.out.println("Refuerzo: " + this.getReinforcement_queue().getSize() + " personajes.");
    }

    public boolean areAllQueuesEmpty() {
        return this.prior1_queue.isEmpty() && this.prior2_queue.isEmpty() && this.prior3_queue.isEmpty()
                && reinforcement_queue.isEmpty();
    }

    public void createAndEnqueueCharacter() {
        String characterId = this.getStudioLabel().replace(" ", "") + "-" + this.idCounter++; // Crea un ID unico para el personaje
        Character newCharacter = new Character(characterId, this.getStudioLabel());

        // Asigna una imagen única del Star Wars o Star Trek y a su vez el nombre del personaje
        Principal.getPrincipalInstance().getImagesManager().assignImage(newCharacter, this.getStudioLabel().equalsIgnoreCase("Star Wars") ? "./src/GUI/Assets/StarWars" : "./src/GUI/Assets/StarTrek");

        int charPrio = newCharacter.getPriorityLevel();

//        newCharacter.printCHRAttribs();
        switch (charPrio) {
            case 1:
                this.getPrior1_queue().insert(newCharacter);
                break;
            case 2:
                this.getPrior2_queue().insert(newCharacter);
                break;
            case 3:
                this.getPrior3_queue().insert(newCharacter);
                break;
            default:
                System.out.println("Nivel de prioridad no valido para: " + newCharacter.getName());
        }
    }

    //Implementar borrar personaje en algun momento de la lista, el personaje esta fuera de la simulacion al estar fuera de las colas.
    public void removeCharacter(Character characterToRemove) {
        // Intentar eliminar de la lista de personajes
        if (!losers_list.isEmpty()) {
            getLosers_list().delete(characterToRemove);
            System.out.println("Un personaje ha sido eliminado de la simulacion: " + characterToRemove);
        } else {
            System.out.println("Personaje no enocntrado en la lista de personajes: " + characterToRemove);
        }
    }

    public void updateReinforcementQueue() {
        if (!this.reinforcement_queue.isEmpty()) {
            Character actualCharacter = getReinforcement_queue().pop();
            double probability = Math.random();

            if (probability <= 0.4) {
                // Mover a la cola de prioridad 1
                this.getPrior1_queue().insert(actualCharacter);
                actualCharacter.setPriorityLevel(1);
                actualCharacter.setIsReinforced(false);
                System.out.println(actualCharacter.getName() + " ha sido promovido a prioridad 1.");
            } else {
                // Mover al final de la cola de refuerzo
                actualCharacter.setIsReinforced(true);
                this.getReinforcement_queue().insert(actualCharacter);
                System.out.println(actualCharacter.getName() + " permanece en la cola de refuerzo.");
            }
        }
    }

    //Actualizar las prioridades de los personajes según las reglas de inanición.
    public void updateStarvationCounters() {
        updateQueueStarvation(this.getPrior2_queue(), 2);
        updateQueueStarvation(this.getPrior3_queue(), 3);
    }

    private void updateQueueStarvation(OurQueue<Character> queue, int priorityLevel) {
        int size = queue.getSize();
        for (int i = 0; i < size; i++) {
            Character actualCharacter = queue.pop();

            actualCharacter.incrementStarvationCounter();
            if (actualCharacter.getStarvation_counter() >= 8) {
                actualCharacter.resetStarvationCounter();
                actualCharacter.setPriorityLevel(priorityLevel - 1);

                if (priorityLevel == 3) {
                    this.getPrior2_queue().insert(actualCharacter);
                    System.out.println(actualCharacter.getName() + " ha sido promovido a prioridad 2 debido a inanicion");
                } else if (priorityLevel == 2) {
                    this.getPrior1_queue().insert(actualCharacter);
                    System.out.println(actualCharacter.getName() + " ha sido promovido a prioridad 1 debido a inanición.");
                }
            } else {
                // Volver a añadir al personaje en su cola correspondiente en la ultima posicion
                queue.insert(actualCharacter);
            }
        }
    }

    public Character getNextCharacterForBattle() {
        if (!this.prior1_queue.isEmpty()) {
            return this.prior1_queue.pop();
        } else if (!this.prior2_queue.isEmpty()) {
            return this.prior2_queue.pop();
        } else if (!this.prior3_queue.isEmpty()) {
            return this.prior3_queue.pop();
        } else {
            System.out.println("No hay personajes disponibles en las colas de prioridad para el estudio " + this.studioLabel);
            return null;
        }
    }

    // Registrar un perdedor en la lista de perdedores.
    public void registerLoser(Character loserCharacter) {
        this.getLosers_list().addAtTheEnd(loserCharacter);
        System.out.println(loserCharacter.getName() + " ha sido enviado a la lista de Perdedores");
    }

    /**
     * @return the studioLabel
     */
    public String getStudioLabel() {
        return studioLabel;
    }

    /**
     * @param studioLabel the studioLabel to set
     */
    public void setStudioLabel(String studioLabel) {
        this.studioLabel = studioLabel;
    }

    /**
     * @return the losers_list
     */
    public SimpleList<Character> getLosers_list() {
        return losers_list;
    }

    /**
     * @return the prior1_queue
     */
    public OurQueue<Character> getPrior1_queue() {
        return prior1_queue;
    }

    /**
     * @return the prior2_queue
     */
    public OurQueue<Character> getPrior2_queue() {
        return prior2_queue;
    }

    /**
     * @return the prior3_queue
     */
    public OurQueue<Character> getPrior3_queue() {
        return prior3_queue;
    }

    /**
     * @return the reinforcement_queue
     */
    public OurQueue<Character> getReinforcement_queue() {
        return reinforcement_queue;
    }

}
