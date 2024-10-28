
import EDD.SimpleList;
import Classes.Character;
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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        SimpleList myList = new SimpleList();
        myList.printList();
        System.out.println("");

        Character obj1 = new Character("SW01", "Star Wars");
        obj1.printCHRAttribs();

        Principal mainwind = new Principal();
        mainwind.setVisible(true);
    }

}
