package edu.sdccd.cisc191.template;

/**
 * Java Reflection and Invoking Methods
 * https://www.oracle.com/technical-resources/articles/java/javareflection.html
 * https://docs.oracle.com/javase/tutorial/reflect/member/methodInvocation.html
 * Threads (Platform.runLater())
 * https://math.hws.edu/javanotes/c12/s2.html
 * Test///
 */
import java.lang.reflect.Method; // Interactive Console & GUI
import javafx.application.Platform; // GUI
import javafx.embed.swing.JFXPanel; // GUI
import javafx.stage.Stage; // GUI
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import java.util.HashMap; // OOP
import java.io.ByteArrayInputStream; // IO
import java.io.InputStream; // IO
import java.util.Scanner; // IO
import java.util.ArrayList; // G&C

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ServerTest {
    @Test // Interactive Console Module
    public void testGetValueAtIndex() throws Exception {
        Platform.runLater(() -> {
            try {
                // String input = "1\n0\n0\n"; // @input {1,0,0}
                // InputStream in = new ByteArrayInputStream(input.getBytes());
                // System.setIn(in);
                Server server = new Server();
                server.start(new Stage());
                // Access private method getValueAtIndex using reflection.
                Method getValueAtIndexMethod = Server.class.getDeclaredMethod("getValueAtIndex");
                getValueAtIndexMethod.setAccessible(true);
                getValueAtIndexMethod.invoke(server);
                // Assert expected result.
                assertEquals("VALUE AT INDEX (0, 0): 1", "EXPECTED_RESULT"); // This will always fail.
            } catch (Exception e) {
                // Handle exceptions.
                e.printStackTrace();
            }
        });
    }

    @BeforeAll // JavaFX GUI of Interactive Console Module
    public static void setup() { // must be static
        // Initialize JavaFX toolkit.
        new JFXPanel();
    }

    @Test // JavaFX GUI of Interactive Console Module
    public void testGetValueAtIndexGUI() throws Exception {
        /*
         * Simulate clicking of button to test functionality.
         * Access private method getValueAtIndexGUI() using reflection.
         */
        Platform.runLater(() -> {
            try {
                Server server = new Server();
                server.start(new Stage());
                Method getValueAtIndexGUIMethod = Server.class.getDeclaredMethod("getValueAtIndexGUI");
                getValueAtIndexGUIMethod.setAccessible(true);
                getValueAtIndexGUIMethod.invoke(server);
                // Assert expected result.
                assertEquals("VALUE AT INDEX (0, 0): 5", "EXPECTED_RESULT"); // This will always fail.
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        // Wait for JavaFx operations to complete.
        Thread.sleep(1000);
    }

    @Test // OOP
    public void testUserGetName() {
        User user = new User("TIMMY", null);
        assertEquals("TIMMY", user.getName());
    }

    @Test // IO
    public void testScannerInput() {
        String input = "1";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);
        int value = scanner.nextInt();
        scanner.close();
        assertEquals(1, value);
    }

    @Test // G&C
    public void testHashMapPutAndGet() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("A", 1);
        map.put("B", 2);
        assertEquals(Integer.valueOf(1), map.get("A"));
        assertEquals(Integer.valueOf(2), map.get("B"));
    }

    @Test // G&C (Good to know: Major Food Allergens)
    public void testArrayListSize() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Milk");
        list.add("Peanuts");
        list.add("Shellfish");
        list.add("Wheat");
        list.add("Soy");
        assertEquals(5, list.size());
    }
}