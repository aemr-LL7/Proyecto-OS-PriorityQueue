/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Managers;

import java.util.Random;
import javax.swing.ImageIcon;
import Classes.Character;
import EDD.OurQueue;
import EDD.SimpleList;
import EDD.SimpleNode;
import java.awt.Image;
import java.io.File;

public class ImagesManager {

    private Random random;

    public ImagesManager() {
        this.random = new Random();
    }

    // Método para asignar una imagen a un personaje según el estudio (permitiendo repeticiones)
    public void assignImage(Character character, String folderPath) {
        ImageIcon selectedImage = getRandomImage(folderPath);
        if (selectedImage != null) {
            character.setCharacterImage(selectedImage);
            character.setName(getImageName(selectedImage));
        } else {
            System.err.println("No se pudo asignar una imagen al personaje.");
        }
    }

    // Método para cargar imágenes aleatoriamente según la carpeta proporcionada y el estudio
    private ImageIcon getRandomImage(String folderPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        if (files == null || files.length == 0) {
            System.out.println("La carpeta está vacía o no se pudo acceder a ella.");
            return null;
        }

        // Selecciona una imagen aleatoria basada en el estudio
        int index = random.nextInt(files.length);
        return new ImageIcon(files[index].getPath());
    }

    // Método para obtener el nombre del archivo de la imagen utilizada sin extensión
    private String getImageName(ImageIcon image) {
        String filePath = image.getDescription();
        String fileName = new File(filePath).getName();
        return fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf('.')) : fileName;
    }

    public SimpleList<ImageIcon> getImagesFromQueue(OurQueue<Character> queue) {
        SimpleList<ImageIcon> imageList = new SimpleList<>();
        SimpleNode<Character> currentNode = queue.getpFirst();

        while (currentNode != null) {
            Character character = currentNode.getData();
            ImageIcon originalImage = character.getCharacterImage();

            if (originalImage != null) {
                // Redimensionar la imagen
                Image resizedImage = originalImage.getImage().getScaledInstance(30, 45, Image.SCALE_SMOOTH);
                ImageIcon resizedIcon = new ImageIcon(resizedImage);
                imageList.addAtTheEnd(resizedIcon); // Añadimos la imagen a la lista
            } else {
                System.err.println("El Character no tiene una imagen valida.");
            }

            currentNode = currentNode.getpNext();
        }

        return imageList;
    }

    public ImageIcon reScaleImage(ImageIcon originalImage, int width, int height) {
        if (originalImage != null) {
            // Redimensionar la imagen
            Image resizedImage = originalImage.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(resizedImage);
            return resizedIcon;
        } else {
            System.err.println("El Character no tiene una imagen valida.");
        }
        return originalImage;
    }
}
