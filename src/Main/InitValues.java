/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import Classes.AIProcessor;
import Classes.Administrator;
import Classes.Simulator;
import Classes.Studio;

/**
 *
 * @author HP-Probook
 */
public class InitValues {
    public static void initializeParams(){
        App app = App.getInstance();
        app.setIa(new AIProcessor());
        
        Studio starWarsStudios = new Studio("Star Wars");
        Studio starTrekStudios = new Studio("Star Trek");
        
        Administrator admin = new Administrator(starWarsStudios,starTrekStudios);
        Simulator simulation = new Simulator(app.getIa(), app.getAdmin(), app.getSemaphore());
        
        app.setSimulation(simulation);
        app.getSimulation().getAI().setAdmin(admin);
        app.getSimulation().setFirstStudio(starWarsStudios);
        app.getSimulation().setSecondStudio(starTrekStudios);
        
        // Iniciar simulacion (crear personajes)
        app.getSimulation().initializeSimulation();
        
    }
}
