/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import EDD.OurQueue;
import EDD.SimpleList;

/**
 *
 * @author andre
 */
public class Studio{

    //Studio info section
    private String studioLabel;
    private int idCounter;

    //Queue section
    private SimpleList<Character> chr_list;
    private OurQueue<Character> prior1_queue;
    private OurQueue<Character> prior2_queue;
    private OurQueue<Character> prior3_queue;
    private OurQueue<Character> reinforcement_queue;

    public Studio(String studioLabel) {
        this.studioLabel = studioLabel;
        this.prior1_queue = new OurQueue();
        this.prior2_queue = new OurQueue();
        this.prior3_queue = new OurQueue();
        this.reinforcement_queue = new OurQueue();
        this.chr_list = new SimpleList(); // Inicializa la lista de personajes
        this.idCounter = 0;
    }

    public Character createCharacter() {
        String characterId = this.getStudioLabel() + "_" + this.idCounter++; // Crea un ID unico para el personaje
        Character newCharacter = new Character(characterId,"PLACEHOLDER", this.getStudioLabel(), 0);
        this.getChr_list().addAtTheEnd(newCharacter); // Agrega el personaje a la lista de personajes disponibles
        return newCharacter;
    }

    public void removeCharacter(Character characterToRemove) {
        // Intentar eliminar de la lista de personajes
        if (!chr_list.isEmpty()) {
            chr_list.delete(characterToRemove);
            System.out.println("Removed character from character list: " + characterToRemove);
        } else {
            System.out.println("Character not found in character list: " + characterToRemove);
        }

        // Intentar eliminar de cada cola
        boolean foundInQueue = false;

        foundInQueue = prior1_queue.remove(characterToRemove);
        if (foundInQueue) {
            System.out.println("Removed character from priority 1 queue: " + characterToRemove);
        }

        foundInQueue = prior2_queue.remove(characterToRemove);
        if (foundInQueue) {
            System.out.println("Removed character from priority 2 queue: " + characterToRemove);
        }

        foundInQueue = prior3_queue.remove(characterToRemove);
        if (foundInQueue) {
            System.out.println("Removed character from priority 3 queue: " + characterToRemove);
        }

        if (!foundInQueue) {
            System.out.println("Character not found in any queue: " + characterToRemove);
        }
    }

    public void starvationUpdate() {
        for (int i = 0; i < chr_list.getSize(); i++) {
            Character character = chr_list.getValueByIndex(i); // Obtiene el personaje en la pos i
            character.incrementStarvationCounter(); // Incrementa el contador de inanicion

            // Verifica si el contador ha llegado a 8
            if (character.getStarvation_counter() == 8) {
                // Reinicia el contador
                character.resetStarvationCounter();

                // Cambia la prioridad del personaje
                if (character.getPrio_level() >0) { // Si no es de prioridad 1
                    character.setPrio_level(character.getPrio_level() - 1); // Incrementa la prioridad
                }
            }
        }
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
     * @return the idCounter
     */
    public int getIdCounter() {
        return idCounter;
    }

    /**
     * @param idCounter the idCounter to set
     */
    public void setIdCounter(int idCounter) {
        this.idCounter = idCounter;
    }

    /**
     * @return the chr_list
     */
    public SimpleList<Character> getChr_list() {
        return chr_list;
    }

    /**
     * @param chr_list the chr_list to set
     */
    public void setChr_list(SimpleList<Character> chr_list) {
        this.chr_list = chr_list;
    }

    /**
     * @return the prior1_queue
     */
    public OurQueue getPrior1_queue() {
        return prior1_queue;
    }

    /**
     * @param prior1_queue the prior1_queue to set
     */
    public void setPrior1_queue(OurQueue prior1_queue) {
        this.prior1_queue = prior1_queue;
    }

    /**
     * @return the prior2_queue
     */
    public OurQueue getPrior2_queue() {
        return prior2_queue;
    }

    /**
     * @param prior2_queue the prior2_queue to set
     */
    public void setPrior2_queue(OurQueue prior2_queue) {
        this.prior2_queue = prior2_queue;
    }

    /**
     * @return the prior3_queue
     */
    public OurQueue getPrior3_queue() {
        return prior3_queue;
    }

    /**
     * @param prior3_queue the prior3_queue to set
     */
    public void setPrior3_queue(OurQueue prior3_queue) {
        this.prior3_queue = prior3_queue;
    }

    /**
     * @return the reinforcement_queue
     */
    public OurQueue getReinforcement_queue() {
        return reinforcement_queue;
    }

    /**
     * @param reinforcement_queue the reinforcement_queue to set
     */
    public void setReinforcement_queue(OurQueue reinforcement_queue) {
        this.reinforcement_queue = reinforcement_queue;
    }

}
