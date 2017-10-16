
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.geometry.Insets;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Pair;
import java.util.Optional;

/**
 * The AthleteAssist class calculates group and team sizes based upon a number of people and a number of players entered by the user,
 * but only after they enter the correct login information.
 * This class prompts the user to enter a username and password and then compares those strings to the correct strings stored in variables.
 * This class extends the Application class and inherits and implements the abstract method start().
 *
 * @author Sapper
 * @version 14OCT2017
 * email: email
 */

//inherit from the Application class
public class AthleteAssistCorrected extends Application {

    //declare global variables which need to be used inside certain methods
    int loginAttempts = 2;
    int loginLimit = 0;
    int groupSize;
    int groupRemaining;
    int teamSize;
    int teamRemaining;
    String correctUsername = "csc200";
    String correctPassword = "password";
    String userName = "";
    String passWord = "";
    Alert error = new Alert(Alert.AlertType.ERROR, "You did not enter an integer");

    /**
     * The main() method issues an internal call to launch the start() method.
     * @param args arguments passed from the command line.
     */

    public static void main(String[] args) {

        launch(args);
    }

    /**
     * The start() method is the entry point of the javafx application and is an abstract method so it must be overridden.
     * The start() method crates a stage for graphical windows to be displayed in.  Windows, (nodes etc, don't really understand all of it..), are built
     * utilizing GUI classes such as BorderPane and GridPane to set objects in a pane which is set in a scene which is set in the stage and then shown to the user.
     * Initially there is an introductory Alert box, followed by a custom Pane populated by text boxes which retrieve a user entered username and password which
     * get compared to the correct strings.
     * A series of if-else statements are implemented in order to count the number of attempts and either block entry after three or move on to the rest of the program.
     *
     * @param primaryStage creates the stage object which can be shown.
     */

    @Override
    public void start(Stage primaryStage) {

        //Introductory Alert dialog
        Alert intro = new Alert(Alert.AlertType.INFORMATION);
        intro.setTitle("Introduction to the AtleteAssist Program");
        intro.setHeaderText("Hello! I am AthleteAssist");
        intro.setContentText("I can help you with your athletic program organization.\n" +
                "After you log in we can get to work!");
        intro.showAndWait();



        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Login Dialog");
        dialog.setHeaderText("Enter your username and password");

        // Set the button types.
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, cancelButtonType);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);

        // Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> username.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(username.getText(), password.getText());
            } else if (dialogButton == cancelButtonType) {
                System.exit(0);
            }
            return null;
        });

        //Begin login attempts loop in order to count down incorrect logins.
        do {
            //dialog results
            Optional<Pair<String, String>> result = dialog.showAndWait();

            //parse out the result
            result.ifPresent(usernamePassword -> {
                userName = usernamePassword.getKey();
                passWord = usernamePassword.getValue();
            });

            //contingency for incorrect username, password, or no more login attempts
            if ((loginAttempts > 0) && (!userName.equalsIgnoreCase(correctUsername))) {
                Alert invalidUser = new Alert(Alert.AlertType.INFORMATION);
                invalidUser.setContentText("Invalid Username\n" +
                        "you have " + loginAttempts + " login attempts left");
                invalidUser.showAndWait();
                //decrement
                loginAttempts--;

            } else if ((loginAttempts > 0) && (!passWord.equals(correctPassword))) {
                Alert invalid = new Alert(Alert.AlertType.INFORMATION);
                invalid.setTitle("Invalid Password");
                invalid.setHeaderText("Your password is incorrect");
                invalid.setContentText("you have " + loginAttempts + " login attempts left");
                invalid.showAndWait();
                loginAttempts--;

            } else if (loginAttempts == loginLimit) {
                Alert failed = new Alert(Alert.AlertType.INFORMATION);
                ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                ButtonType okButton = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                failed.getButtonTypes().setAll(okButton, cancelButton);
                failed.setTitle("Failed login");
                failed.setHeaderText("You are out of login attempts, please contact your system administrator");
                Optional<ButtonType> choice = failed.showAndWait();
                if (choice.get() == cancelButton) {
                    System.exit(0);
                }
            }
        } while ((!userName.equalsIgnoreCase(correctUsername)) && (!passWord.equals(correctPassword))
                && (loginAttempts >= loginLimit));

        //instantiate a text input dialog to obtain number of people
        TextInputDialog numPeople = new TextInputDialog();
        numPeople.setTitle("Number of people");
        numPeople.setContentText("Enter the number of people");
        //I don't understand how Optional works but this stores the user input string
        Optional<String> peopleCount = numPeople.showAndWait();
        String peopleCountStr = peopleCount.get();
        //parse the input from string to integer
        int peopleCountNum = Integer.parseInt(peopleCountStr);

        //set conditional control structures for values entered
        if (peopleCountNum < 3) {
            Alert lowGroup = new Alert(Alert.AlertType.INFORMATION);
            lowGroup.setTitle("Invalid entry");
            lowGroup.setContentText("Number of people must be 3 or greater");
            lowGroup.showAndWait();

        } else if ((peopleCountNum >= 3) && (peopleCountNum <= 10)) {
            groupSize = peopleCountNum / 3;
            groupRemaining = peopleCountNum % 3;
            Alert midGroup = new Alert(Alert.AlertType.INFORMATION);
            midGroup.setContentText("your group size is:  " + groupSize + "\n" +
                    "with " + groupRemaining + " people(s) unassigned");
            midGroup.showAndWait();
        } else if (peopleCountNum > 10){
            groupSize = peopleCountNum / 2;
            groupRemaining = peopleCountNum % 2;
            Alert highGroup = new Alert(Alert.AlertType.INFORMATION);
            highGroup.setContentText("your group size is:  " + groupSize + "\n" +
                    "with " + groupRemaining + " people(s) unassigned");
            highGroup.showAndWait();
        } else {
            error.showAndWait();
        }

        //instantiate a text input dialog to read number of players
        TextInputDialog team = new TextInputDialog();
        team.setContentText("enter number of players");
        Optional<String> playerCount = team.showAndWait();
        String playerCountStr = playerCount.get();
        int playerCountNum = Integer.parseInt(playerCountStr);

        if ((playerCountNum >= 11) && (playerCountNum <= 55) && (playerCountNum <= peopleCountNum)) {
            teamSize = playerCountNum / 11;
            teamRemaining = playerCountNum % 11;
            Alert teamBuild = new Alert(Alert.AlertType.INFORMATION);
            teamBuild.setContentText("number of players is:  " + playerCountNum + "\n" +
                    "number of teams is: " + teamSize + " with " + teamRemaining + " players remaining");
            teamBuild.showAndWait();
            Platform.exit();
        } else if ((playerCountNum < 11) || (playerCountNum > 55) || (playerCountNum < 3) || (playerCountNum > peopleCountNum)) {
            Alert dummy = new Alert(Alert.AlertType.INFORMATION);
            dummy.setContentText("hey dummy, you entered some number bigger or smaller than the\n" +
                    "amount of people, try again");
            dummy.showAndWait();
        } else {
            error.showAndWait();
        }

    }

}
