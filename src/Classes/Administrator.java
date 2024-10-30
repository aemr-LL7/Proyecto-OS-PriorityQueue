/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import java.util.Random;

/**
 *
 * @author andre
 */
public class Administrator {

    private int cycleForReviewCount;
    private Studio firstStudio; // Star Wars
    private Studio secondStudio; // Star Trek
    private static final double NEW_CHAR_PROB = 0.8;

    public Administrator(Studio studioStarWars, Studio studioStarTrek, int ciclickCheck) {
        this.firstStudio = studioStarWars;
        this.secondStudio = studioStarTrek;
        this.cycleForReviewCount = ciclickCheck;
    }

    //Seleccionador de personaje pa la pelea por estudio
    public Character selectFighter(Studio studio) {
        if (!studio.getPrior1_queue().isEmpty()) {
            return (Character) studio.getPrior1_queue().pop();
        }

        if (!studio.getPrior2_queue().isEmpty()) {
            return (Character) studio.getPrior2_queue().pop();
        }

        if (!studio.getPrior3_queue().isEmpty()) {
            return (Character) studio.getPrior3_queue().pop();
        }

        return null;
    }

    public void updateQueues() {
        // Incrementa el contador de revision
        this.cycleForReviewCount++;

        // Cada dos ciclos de revision (cuatro personajes revisados)
        if (this.cycleForReviewCount % 2 == 0) {
            this.addNewCharToQueues();
        }
    }

    private void addNewCharToQueues() {
        Random random = new Random();
        if (random.nextDouble() < this.NEW_CHAR_PROB) {
            Character newStarWarsChar = this.firstStudio.createCharacter();
            Character newStarTrekChar = this.secondStudio.createCharacter();

            // Agregar personajes a las colas de prioridad segun su nivel inicial
            this.insertCharByPriority(newStarWarsChar, this.firstStudio);
            this.insertCharByPriority(newStarTrekChar, this.secondStudio);

        }
    }

    private void insertCharByPriority(Character character, Studio studio) {
        int priorityLevel = character.getPrio_level();

        switch (priorityLevel) {
            case 1: // Excepcional
                studio.getPrior1_queue().insert(character);
                System.out.println("Personaje " + character.getId() + " insertado en la Cola de Prioridad 1 (Excepcional)");
                break;
            case 2: // Promedio
                studio.getPrior2_queue().insert(character);
                System.out.println("Personaje " + character.getId() + " insertado en la Cola de Prioridad 2 (Promedio)");
                break;
            case 3: // Deficiente
                studio.getPrior3_queue().insert(character);
                System.out.println("Personaje " + character.getId() + " insertado en la Cola de Prioridad 3 (Deficiente)");
                break;
            default:
                System.out.println("No se ha encontrado correctamente la prioridad del Personaje " + character.getId());
        }
    }

}
