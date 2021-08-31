package cs2103.duke;

import java.util.Scanner;
import java.io.IOException;

/**
 * This class encapsulates a Duke chat-bot.
 */
public class Duke {
    private final String dukeFilePath;
    private final Storage storage;
    private TaskList tasks;
    private Ui ui;

    public Duke(String dukeFilePath) {
        this.dukeFilePath = dukeFilePath;
        storage = new Storage(dukeFilePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (DukeException | IOException e) {
            System.out.println(ui.showLoadingError());
            tasks = new TaskList();
        }
        ui = new Ui(tasks);
    }

    public void run() {
        System.out.println(ui.showWelcome());
        boolean canExit = false;
        // Scanner to read user inputs
        Scanner scanner = new Scanner(System.in);

        while (!canExit) {
            String userInput = scanner.next();
            try {
                if (userInput.equals("bye")) { // user inputs "bye", set canExit to true and Exit
                    canExit = true;
                    // store task list
                    String temp = tasks.listBeautify();
                    storage.overwriteFile(dukeFilePath, temp);
                    System.out.println(ui.showGoodbye());
                } else { // check first input
                    System.out.println(ui.handleInput(scanner, userInput, tasks));
                }
            } catch (DukeException | IOException e) {
                e.printStackTrace(); // print stack trace for e
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String dukeFilePath = "./data/duke.txt";
        Storage s = new Storage(dukeFilePath);
        s.initialize();
        new Duke(dukeFilePath).run();
    }
}