package edu.sdccd.cisc191.template;

import java.io.Serializable;
import java.util.Arrays;

/*
 * Includes the implementation of a JavaFX GUI menu using buttons and dialog
 * boxes for user input and displaying messages.
 * Methods getValueAtIndexGUI(), setValueAtIndexGUI(), findIndexOfValueGUI(),
 * printAllValuesGUI(), deleteValueAtIndexGUI(), expandArrayGUI(), and
 * shrinkArrayGUI()
 * used to handle the corresponding actions in the GUI interface.
 * Networking functionality is not implemented in this code.
 * Implementing networking would involve creating a separate class for the
 * server and client,
 * establishing a network connection, and exchanging data between them using
 * sockets and input/output streams.
 */
class User implements Serializable {
    private final String name;
    private int[][] twoDimArray;

    public User(String name, int[][] twoDimArray) {
        this.name = name;
        this.twoDimArray = twoDimArray;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }
}
