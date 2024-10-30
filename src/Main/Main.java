
import EDD.SimpleList;
import Classes.Character;
import Classes.Simulator;
import GUI.Principal;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
/**
 *
 * @author andre
 */
public class Main {

    public static int battleDuration = 10;

    // Crea la simulación con dos estudios
    public static Simulator simulation = new Simulator("LucasArt", "Pinewood", battleDuration);

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        simulation.start();
//        Principal mainwind = new Principal();
//        mainwind.setVisible(true);
    }

}
