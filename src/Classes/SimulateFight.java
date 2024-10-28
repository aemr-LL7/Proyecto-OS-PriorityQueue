/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import EDD.OurQueue;

/**
 *
 * @author andre
 */
public class SimulateFight extends Thread {
    // Faltaria definir admin y la IA
    private Studio firstStudio;
    private Studio secondStudio;
    private OurQueue winnersQueue;
    private int duration;

    public SimulateFight(String labelStudio1, String labelStudio2, int battleDuration) {
        this.firstStudio = new Studio(labelStudio1);
        this.secondStudio = new Studio(labelStudio2);
        this.winnersQueue = new OurQueue();
        this.duration = battleDuration;
    }

}
