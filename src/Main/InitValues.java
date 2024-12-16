/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import Classes.AIProcessor;
import Classes.Administrator;
import Classes.Studio;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HP-Probook
 */
public class InitValues {

    public static void initializeParams() {
        App app = App.getInstance();

        Studio starWarsStudios = new Studio("Star Wars");
        Studio starTrekStudios = new Studio("Star Trek");

        app.setIa(new AIProcessor());
        Administrator admin = new Administrator(starWarsStudios, starTrekStudios, app.getSemaphore(), app.getIa());
        app.setAdmin(admin);
        app.getAdmin().getAI().setAdmin(admin);

        // Iniciar simulacion (crear personajes)
        app.getAdmin().initializeSimulation();

        // Esperar a que el hilo Administrator termine
        try {
            app.getAdmin().join(); // Esperar a que `Administrator` finalice
            app.getIa().join(); // Esperar a que `AIProcessor` finalice
        } catch (InterruptedException ex) {
            Logger.getLogger(InitValues.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
