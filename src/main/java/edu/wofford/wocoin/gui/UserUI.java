package edu.wofford.wocoin.gui;

import edu.wofford.wocoin.ConsoleController;
import edu.wofford.wocoin.WalletUtilities;
import io.bretty.console.view.ActionView;
import io.bretty.console.view.Validator;
import io.bretty.console.view.ViewConfig;

import java.util.Scanner;

public class UserUI extends ActionView {
    private ConsoleController cc;

    public UserUI(ConsoleController cc, ViewConfig viewConfig) {
        super("Please enter your username and password separated by a space.", "user", viewConfig);
        this.cc = cc;
        this.keyboard = MainMenu.keyboard;
    }

    @Override
    public void executeCustomAction() {
        Validator<String> customValidator = s -> s.split(" ").length == 2;
        String usernameAndPassword = this.prompt("", String.class, customValidator);

        String username = usernameAndPassword.split(" ")[0];
        String password = usernameAndPassword.split(" ")[1];

        if (!cc.userLogin(username, password)){
            this.println("No such user.");
            this.goBack();
        }
        else {
            new UserRootMenu(username, viewConfig, this.keyboard).display();
        }
    }

    private class UserRootMenu extends CustomMenuView {
        public UserRootMenu(String user, ViewConfig viewConfig, Scanner keyboard) {
            this(user, viewConfig);
            this.keyboard = MainMenu.keyboard;
        }

        public UserRootMenu(String user, ViewConfig viewConfig) {
            super("Welcome, " + user, "", viewConfig);

            ActionView createWalletAction = new ActionView("Create a Wallet", "create wallet") {
                @Override
                public void executeCustomAction() {
                    boolean userStillCreatingWallet = true;

                    if (cc.userHasWallet()) {
                        this.println("Your account has an associated wallet.");
                        userStillCreatingWallet = this.confirmDialog("Would you like to delete this wallet and create a new wallet?");

                        if (userStillCreatingWallet) {
                            this.println("Wallet deleted.");
                        }
                    }

                    if (userStillCreatingWallet) {
                        String path = this.prompt("Enter the file path for the wallet: ", String.class);

                        WalletUtilities.CreateWalletResult result = cc.addWalletToUser(path);

                        this.println(result == WalletUtilities.CreateWalletResult.SUCCESS ? "Wallet added." : "Action Canceled");

                    }
                }

                @Override
                public void display() {
                    this.println();
                    this.println(this.runningTitle);
                    this.executeCustomAction();
                    this.goBack();
                }
            };



            this.addMenuItem(createWalletAction);
            this.setParentView(new MainMenu(cc));
        }



        @Override
        protected void onBack() {
            cc.doLogout();
            super.onBack();
        }
    }
}
