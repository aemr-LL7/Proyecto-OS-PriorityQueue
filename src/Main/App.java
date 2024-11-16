/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import Classes.Simulator;
import GUI.Principal;

/**
 *
 * @author HP-Probook
 */
public class App {
    
    private static int battleDuration = 10;
    private static App app;
    private Simulator simulation = new Simulator("LucasArt", "Pinewood", battleDuration);

    // Retorna instancia unica de la app
    public static synchronized App getInstance() {
        if (getApp() == null) {
            setApp(new App());
        }
        return getApp();
    }

    public void start() {
        getSimulation().start();
        // Inicia la simulación y pasa el control a la ventana Principal
        Principal mainFrame = Principal.getPrincipalInstance();
        mainFrame.setVisible(true);  // Muestra la ventana Principal
    }

    /**
     * @return the app
     */
    public static App getApp() {
        return app;
    }

    /**
     * @param aApp the app to set
     */
    public static void setApp(App aApp) {
        app = aApp;
    }

    /**
     * @return the battleDuration
     */
    public static int getBattleDuration() {
        return battleDuration;
    }

    /**
     * @param aBattleDuration the battleDuration to set
     */
    public static void setBattleDuration(int aBattleDuration) {
        battleDuration = aBattleDuration;
    }

    /**
     * @return the simulation
     */
    public Simulator getSimulation() {
        return simulation;
    }

    /**
     * @param simulation the simulation to set
     */
    public void setSimulation(Simulator simulation) {
        this.simulation = simulation;
    }
    
}
