/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import Classes.AIProcessor;
import Classes.Administrator;
import Classes.Simulator;
import GUI.Principal;
import java.util.concurrent.Semaphore;

/**
 *
 * @author HP-Probook
 */
public class App {

    private static App app;

    private static Semaphore semaphore = new Semaphore(1);
    private static int battleDuration = 10;
    private static Administrator admin;
    private static AIProcessor ia;

    // Retorna instancia unica de la app
    public static synchronized App getInstance() {
        if (getApp() == null) {
            setApp(new App());
        }
        return getApp();
    }

    public void startProgram() {
        // COMENZAR LA SIMULACION DEL PROGRAMA
        InitValues.initializeParams();
    }

    public static Administrator getAdmin() {
        return admin;
    }

    public static void setAdmin(Administrator admin) {
        App.admin = admin;
    }

    public static App getApp() {
        return app;
    }

    public static void setApp(App app) {
        App.app = app;
    }

    public static Semaphore getSemaphore() {
        return semaphore;
    }

    public static void setSemaphore(Semaphore semaphore) {
        App.semaphore = semaphore;
    }

    public static int getBattleDuration() {
        return battleDuration;
    }

    public static void setBattleDuration(int battleDuration) {
        App.battleDuration = battleDuration;
    }

    public static AIProcessor getIa() {
        return ia;
    }

    public static void setIa(AIProcessor ia) {
        App.ia = ia;
    }

}
