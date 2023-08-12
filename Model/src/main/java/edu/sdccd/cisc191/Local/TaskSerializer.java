package edu.sdccd.cisc191.Local;

import java.io.*;
import java.util.ArrayList;

/**
 * The TaskSerializer class provides methods to save and load objects using serialization.
 * It allows you to serialize an ArrayList of Task objects and save them to a file,
 * as well as deserialize the saved data and retrieve the ArrayList of Task objects.
 */
public class TaskSerializer {
    /**
     * Saves an ArrayList of objects to a file using serialization.
     *
     * @param objects  The ArrayList of objects to be saved.
     * @param filePath The path to the file where the objects will be saved.
     * @param <T>      The type of objects in the ArrayList.
     */
    public <T> void saveObjects(ArrayList<T> objects, String filePath) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(objects);
            objectOutputStream.close();
            fileOutputStream.close();
            System.out.println("Objects saved to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads an ArrayList of objects from a file using deserialization.
     *
     * @param filePath The path to the file from which the objects will be loaded.
     * @param <T>      The type of objects in the ArrayList.
     * @return The ArrayList of objects loaded from the file.
     *         If an exception occurs during deserialization, an empty ArrayList is returned.
     */
    public <T> ArrayList<T> load(String filePath) {
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            ArrayList<T> loadedObjects = (ArrayList<T>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
            System.out.println("Objects loaded from " + filePath);
            return loadedObjects;

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
