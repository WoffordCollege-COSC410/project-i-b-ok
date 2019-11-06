package edu.wofford.wocoin.gui;

import edu.wofford.wocoin.ConsoleController;
import io.bretty.console.view.ActionView;
import io.bretty.console.view.Validator;
import io.bretty.console.view.ViewConfig;

public class AdminUI extends ActionView {
    private ConsoleController cc;

    public AdminUI(ConsoleController cc) {
        super("Enter the administrator password: ", "administrator");
        this.cc = cc;
    }

    public AdminUI(ConsoleController cc, ViewConfig viewConfig) {
        super("Enter the administrator password: ", "administrator", viewConfig);
        this.cc = cc;
        this.keyboard = MainMenu.keyboard;
    }


    @Override
    public void executeCustomAction() {
        String password = this.prompt("", String.class);

        if (cc.adminLogin(password)) {
            new AdminRootMenu(cc, this.viewConfig).display();
        }
        else {
            this.println("Incorrect administrator password.");
            this.goBack();
        }
    }


    private class AdminRootMenu extends CustomMenuView {
        private ConsoleController cc;

        public AdminRootMenu(ConsoleController cc, ViewConfig viewConfig) {
            super("Welcome, Administrator", "", viewConfig);
            this.keyboard = MainMenu.keyboard;

            ActionView addUserAction = new ActionView("Add User", "add user", viewConfig) {
                @Override
                public void executeCustomAction() {
                    Validator<String> customValidator = s -> s.split(" ").length == 2;
                    String usernameAndPassword = this.prompt("Enter a username and password separated by a space for the user to add.", String.class, customValidator);
                    String username = usernameAndPassword.split(" ")[0];
                    String password = usernameAndPassword.split(" ")[1];

                    this.println(cc.addUser(username, password));
                    this.println("Invalid input");
                }

                @Override
                public void display() {
                    this.println();
                    this.println(this.runningTitle);
                    this.executeCustomAction();
                    this.goBack();
                }
            };

            ActionView removeUserAction = new ActionView("Remove User", "remove user", viewConfig) {
                @Override
                public void executeCustomAction() {
                    String username = this.prompt("Please enter the username of the account to be removed: ", String.class);
                    this.println(cc.removeUser(username));
                }

                @Override
                public void display() {
                    this.println();
                    this.println(this.runningTitle);
                    this.executeCustomAction();
                    this.goBack();
                }
            };

            this.addMenuItem(addUserAction);
            this.addMenuItem(removeUserAction);
            this.setParentView(new MainMenu(cc));
        }

    }

    @Override
    protected void onBack() {
        cc.doLogout();
        super.onBack();
    }
}
