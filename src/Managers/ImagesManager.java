/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Managers;

import EDD.SimpleList;
import java.util.Random;
import javax.swing.ImageIcon;
import Classes.Character;
import java.io.File;

/**
 *
 * @author HP-Probook
 */
public class ImagesManager {

    private SimpleList<ImageIcon> usedStarWarsImages;
    private SimpleList<ImageIcon> usedStarTrekImages;
    private SimpleList<String> usedStarWarsNames;
    private SimpleList<String> usedStarTrekNames;
    private Random random;

    public ImagesManager() {
        this.usedStarWarsImages = new SimpleList<>();
        this.usedStarTrekImages = new SimpleList<>();
        this.usedStarWarsNames = new SimpleList<>();
        this.usedStarTrekNames = new SimpleList<>();
        this.random = new Random();
    }

    // Método para asignar una imagen única a un personaje
    public void assignUniqueImage(Character character, String folderPath, boolean isStarWars) {
        ImageIcon selectedImage = getUniqueImage(folderPath, isStarWars);
        character.setCharacterImage(selectedImage);
        character.setName(getImageName(selectedImage, isStarWars));
    }

    // Método para cargar imágenes únicas según la carpeta proporcionada
    private ImageIcon getUniqueImage(String folderPath, boolean isStarWars) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        if (files == null || files.length == 0) {
            System.out.println("La carpeta está vacía o no se pudo acceder a ella.");
            return null;
        }

        ImageIcon selectedImage;
        String selectedFileName;
        boolean isUnique;

        do {
            int index = random.nextInt(files.length);
            selectedFileName = files[index].getName();
            selectedImage = new ImageIcon(files[index].getPath());
            isUnique = isImageUnique(selectedFileName, isStarWars);
        } while (!isUnique);

        if (isStarWars) {
            this.usedStarWarsImages.addAtTheEnd(selectedImage);
            this.usedStarWarsNames.addAtTheEnd(selectedFileName);
        } else {
            this.usedStarTrekImages.addAtTheEnd(selectedImage);
            this.usedStarTrekNames.addAtTheEnd(selectedFileName);
        }

        return selectedImage;
    }

    // Verifica si la imagen es única según el nombre del archivo
    private boolean isImageUnique(String fileName, boolean isStarWars) {
        if (isStarWars) {
            return !this.usedStarWarsNames.contains(fileName);
        } else {
            return !this.usedStarTrekNames.contains(fileName);
        }
    }

    // Método para obtener el nombre del archivo de la imagen utilizada sin extensión
    private String getImageName(ImageIcon image, boolean isStarWars) {
        SimpleList<String> nameList = isStarWars ? this.usedStarWarsNames : this.usedStarTrekNames;
        SimpleList<ImageIcon> imageList = isStarWars ? this.usedStarWarsImages : this.usedStarTrekImages;

        int index = imageList.indexOf(image);
        if (index != -1) {
            String fileName = nameList.getValueByIndex(index);
            // Remueve la extensión del archivo
            return fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf('.')) : fileName;
        }

        return null;
    }

}
