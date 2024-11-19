/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Main;

import Classes.Studio;
import Classes.Character;
import java.awt.GridLayout;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

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
//        Studio starWars = new Studio("Star Wars");
//        Studio starTrek = new Studio("Star Trek");
//
//        JFrame frame = new JFrame("Character Images");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(500, 500);
//
//        JScrollPane scrollPane = new JScrollPane();
//
//        // Crear personajes
//        Character myChar = starWars.createCharacter();
//        Character myChar2 = starTrek.createCharacter();
//
//        // Redimensionar imágenes
//        ImageIcon originalIcon1 = myChar.getCharacterImage();
//        ImageIcon originalIcon2 = myChar2.getCharacterImage();
//
//        Image resizedImage1 = originalIcon1.getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH);
//        Image resizedImage2 = originalIcon2.getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH);
//
//        ImageIcon resizedIcon1 = new ImageIcon(resizedImage1);
//        ImageIcon resizedIcon2 = new ImageIcon(resizedImage2);
//
//        // Crear labels con imágenes redimensionadas y nombres
//        JLabel label1 = new JLabel(resizedIcon1);
//        JLabel label3 = new JLabel(myChar.getName());
//        JLabel label2 = new JLabel(resizedIcon2);
//        JLabel label4 = new JLabel(myChar2.getName());
//
//        // Crear panel y establecer su layout
//        JPanel panel = new JPanel();
//        panel.setLayout(new GridLayout(2, 2)); // 2 filas y 2 columnas
//
//        // Añadir componentes al panel en el orden deseado
//        panel.add(label1);
//        panel.add(label2);
//        panel.add(label3);
//        panel.add(label4);
//
//        // Ajustar alineación de texto
//        label3.setHorizontalAlignment(SwingConstants.CENTER);
//        label4.setHorizontalAlignment(SwingConstants.CENTER);
//
//        // Agregar panel al scrollPane
//        scrollPane.setViewportView(panel);
//
//        // Agregar scrollPane al frame
//        frame.add(scrollPane);
//
//        // Mostrar frame
//        frame.setVisible(true);

        App myApp = new App();
        myApp.startProgram();
    }

}
