/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author andre
 */
public class SimulateFight extends Thread {

    private Studio firstStudio;
    private Studio secondStudio;
    private Administrator admin;
    private AIProcessor AI;
    private int duration;
    
    private int ciclickCheck;

    public SimulateFight(String labelStudio1, String labelStudio2, int battleDuration) {
        this.firstStudio = new Studio(labelStudio1);
        this.secondStudio = new Studio(labelStudio2);
        this.admin = new Administrator(firstStudio, secondStudio, this.ciclickCheck);
        this.AI = new AIProcessor(admin, firstStudio, secondStudio);
        this.duration = battleDuration;
    }

    @Override
    public void run() {
        // Creación de los personajes iniciales
        System.out.println("Iniciando la creación de personajes...");
        for (int i = 0; i < 21; i++) {
            Character character1 = firstStudio.createCharacter();
            Character character2 = secondStudio.createCharacter();
            System.out.println("Personaje creado para " + firstStudio.getStudioLabel() + ": " + character1.getId());
            System.out.println("Personaje creado para " + secondStudio.getStudioLabel() + ": " + character2.getId());
        }

        long startTime = System.currentTimeMillis();
        long endTime = startTime + duration * 1000; // Convertir duración a milisegundos

        // Ciclo de simulación
        while (System.currentTimeMillis() < endTime) {
            System.out.println("\n--- Inicio de un nuevo ciclo de combate ---");

            // Operación del administrador para actualizar colas y elegir personajes
            System.out.println("El administrador está actualizando las colas y seleccionando personajes...");
            admin.updateQueues();

            // Validación de selección de personajes para combate
            Character fighter1 = admin.selectFighter(firstStudio);
            Character fighter2 = admin.selectFighter(secondStudio);
            if (fighter1 == null || fighter2 == null) {
                System.out.println("No hay suficientes personajes listos para combatir en esta ronda.");
                continue;
            }

            System.out.println("Personaje seleccionado de " + firstStudio.getStudioLabel() + ": " + fighter1.getId());
            System.out.println("Personaje seleccionado de " + secondStudio.getStudioLabel() + ": " + fighter2.getId());

            // La AI selecciona personajes y lleva a cabo la simulación de la pelea
            System.out.println("La IA está procesando el combate...");
            try {
                AI.executeCombat(fighter1, fighter2);

            } catch (InterruptedException ex) {
                Logger.getLogger(SimulateFight.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Control pasa de nuevo al administrador para gestionar el siguiente ciclo
            System.out.println("--- Fin del ciclo de combate ---");

        }

        System.out.println("Simulación finalizada: tiempo de batalla completado.");
    }

}
