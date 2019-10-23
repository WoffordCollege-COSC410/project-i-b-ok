package edu.wofford.wocoin;

import java.util.Scanner;

public class ConsoleMain {

    private SQLController sqlController;

    public enum UIState {EXIT, LOGIN, ADMINISTRATOR}

    private UIState currentState;

    public ConsoleMain() {
        this.currentState = UIState.LOGIN;
        this.sqlController = new SQLController();
    }

    public ConsoleMain(String dbname) {
        this.currentState = UIState.LOGIN;
        this.sqlController = new SQLController(dbname);
    }

    /**
     * @return returns the String representation of the current screen.
     */
    private String getCurrentUIString() {
        switch (currentState) {
            case EXIT:
                return "";
            case LOGIN:
                return "1: exit\n2: administrator";
            case ADMINISTRATOR:
                return "1: back\n2: add user\n3: remove user";
        }
        return null;
    }

    /**
     * This function updates the state of the display to Administrator if the input password is correct.
     * returns true if the UIState is already administrator, or if the password is correct
     *
     * @param password this is the password the user uses to attempt to login as administrator
     * @return true if the UIState is already administrator or if the password is correct, and returns false otherwise
     */
    private boolean adminLogin(String password) {
        if (currentState == UIState.ADMINISTRATOR || password.equals("adminpwd")) {
            currentState = UIState.ADMINISTRATOR;
            return true;
        }
        else {
            return false;
        }

    }

    /**
     * Sets the current state of the UI to login
     */
    private void doLogout() {
        currentState = UIState.LOGIN;
    }

    /**
     * Sets the current state of the UI to exit
     */
    private void exit() {
        currentState = UIState.EXIT;
    }


    /**
     * This function takes a username and password and attempts to add it to the current database.
     * @param username The username of the user to be added
     * @param password The password of the user to be added
     * @return A string representing the action that occurred when the user was added.
     * If the user was added, returns a String in the form "username was added."
     * If the user was a duplicate, returns a String in the form "username already exists."
     * If an exception occurs, returns a String in the form "username was not added."
     */

    private String addUser (String username, String password) {
        SQLController.AddUserResult result = sqlController.insertUser(username, password);

        switch (result) {
            case ADDED:
                return username + " was added.";
            case DUPLICATE:
                return username + " already exists.";
            case NOTADDED:
                return username + " was not added.";
        }
        
        return null;
    }


    /**
     * This function takes a username and attempts to remove it from the current database.
     * @param username The username of the user to be added
     * @return A string representing the action that occurred when the user was added.
     * If the user was removed, returns a String in the form "username was removed."
     * If the user does not exist, returns a String in the form "username does not exist."
     * If an exception occurs, returns a String in the form "username was not removed."
     */

    private String removeUser (String username) {
        SQLController.RemoveUserResult result = sqlController.removeUser(username);

        switch (result) {
            case REMOVED:
                return username + " was removed.";
            case NORECORD:
                return username + " does not exist.";
            case NOTREMOVED:
                return username + " was not removed.";
        }

        return null;
    }

    /**
     * This function can be used to determine what display type is currently
     * @return returns the state of the UI
     */
    public UIState getCurrentState() {
        return currentState;
    }

    public static void main(String[] args) {
        ConsoleMain cm = null;
        if (args.length == 1) {
            cm = new ConsoleMain(args[0]);
        }
        else {
            cm = new ConsoleMain();
        }

        Scanner scanner = new Scanner(System.in);
        int option = 0;

        while (cm.getCurrentState() != UIState.EXIT) {
            System.out.println(cm.getCurrentUIString());
            switch (cm.getCurrentState()) {
                case EXIT:
                    break;
                case LOGIN:
                    option = scanner.nextInt();
                    if (option == 1) {
                        cm.exit();
                    }
                    if (option == 2){
                        String password = scanner.next();
                        if (!cm.adminLogin(password))
                            System.out.println("Incorrect administrator password.");
                    }
                    break;
                case ADMINISTRATOR:
                    option = scanner.nextInt();
                    if (option == 1) {
                        cm.doLogout();
                    } else if (option == 2) {
                        String username = scanner.next();
                        String password = scanner.next();
                        System.out.println(cm.addUser(username, password));
                    } else if (option == 3) {
                        String username = scanner.next();
                        System.out.println(cm.removeUser(username));
                    }
                    break;
            }
        }
    }
}
