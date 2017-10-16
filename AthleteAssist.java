
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.geometry.Insets;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
public class AthleteAssist extends Application {

    //declare global variables which need to be used inside certain methods	
    int loginAttempts = 2;
    int loginLimit = 0;
    int groupSize;
    int groupRemaining;
    int teamSize;
    int teamRemaining;
    
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
     * @Override Override overrides the start() method.
     * @param primaryStage creates the stage object which can be shown.
     */

    @Override
    public void start(Stage primaryStage) {
	
	//Introductory alert    
        Alert intro = new Alert(Alert.AlertType.INFORMATION);
        intro.setTitle("Introduction to the AtleteAssist Program");
        intro.setHeaderText("Hello! I am AthleteAssist");
        intro.setContentText("I can help you with your athletic program organization.\n" +
                "After you log in we can get to work!");
        intro.showAndWait();
	
	//declarations
        String correctUsername = "csc200";
        String correctPassword = "password";
        Button login;
        Button cancel;
        TextField username;
        TextField password;
	
	//instantiating a BorderPane object
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setTop(new Label("Please enter your username and password."));

	//instantiating a GridPane object
        GridPane center = new GridPane();
        center.setVgap(5);
        center.setHgap(5);

	//instantiating text field objects
        username = new TextField();
        username.setPrefWidth(150);
        password = new TextField();
        password.setPrefWidth(150);

	//adding labels and text fields to the GridPane object
        center.add(new Label("Enter Username:"), 0, 0);
        center.add(new Label("Enter Password:"), 0, 1);
        center.add(username, 1, 0);
        center.add(password, 1, 1);

	//instantiating and adding button objects to the GridPane object
        login = new Button("Login");
        center.add(login, 2, 2);
        cancel = new Button("cancel");
        center.add(cancel, 3, 2);
	//setting the GridPane in the BorderPane center area
        root.setCenter(center);

	//instantiating a scene object and setting the BorderPane in the scene and then setting the scene in the stage
        Scene scene = new Scene(root, 450, 150);
        primaryStage.setTitle("Login Window");
        primaryStage.setScene(scene);
	//showing the stage object
        primaryStage.show();

        Alert invalid = new Alert(Alert.AlertType.INFORMATION);
        invalid.setTitle("Invalid Login");
        invalid.setHeaderText("Your password is incorrect");

        Alert failed = new Alert(Alert.AlertType.INFORMATION);
	failed.setTitle("Failed login");
        failed.setHeaderText("You are out of login attempts, please contact your system administrator");

	//Set actions to follow the event of the cancel button being clicked
        cancel.setOnAction(new EventHandler<ActionEvent>() {
	    
	    /**
	     * The handle() method is utilized to provide instructions following an event, such as a button being clicked. 
	     * This method deals with the cancel button being clicked.
	     * @Override overrides the handle method?
	     * @param event the event object?
	     */
            @Override
            public void handle(ActionEvent event) {

                Platform.exit();
            }
        });


	//set an action handler for the login button
        login.setOnAction(new EventHandler<ActionEvent>() {

	    /**
            * The handle() method outlines the subsequent instructions upon the event of the login button being clicked.
	    * @Override override overrides the handle() method?
	    * @param event the event object?
	    */
            @Override
            public void handle(ActionEvent event) {

		//does the input match the correct username and password?    
                if ((username.getText().equalsIgnoreCase(correctUsername)) && (password.getText().equals(correctPassword))) {	

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
                    } else {
                        groupSize = peopleCountNum / 2;
                        groupRemaining = peopleCountNum % 2;
                        Alert highGroup = new Alert(Alert.AlertType.INFORMATION);
                        highGroup.setContentText("your group size is:  " + groupSize + "\n" +
                                "with " + groupRemaining + " people(s) unassigned");
                        highGroup.showAndWait();
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
                    } else {
                        Alert dummy = new Alert(Alert.AlertType.INFORMATION);
                        dummy.setContentText("hey dummy, you entered some number bigger or smaller than the\n" +
                                "amount of people, try again");
                        dummy.showAndWait();
                    }

		//set control flow for incorrect username and password using a loose form of a limited loop with decrement    
                } else if ((loginAttempts > 0) && (!username.getText().equalsIgnoreCase(correctUsername))) {
                    Alert invalidUser = new Alert(Alert.AlertType.INFORMATION);
                    invalidUser.setContentText("invalid username\n" +
                            "you have " + loginAttempts + " login attempts left");
                    invalidUser.showAndWait();
		    //decrement the loginAttempts variable
                    loginAttempts--;
                } else if (loginAttempts > 0) {
                    invalid.setContentText("you have " + loginAttempts + " login attempts left");
                    invalid.showAndWait();
                    loginAttempts--;
                } else {
                    primaryStage.close();
                    failed.showAndWait();
                    Platform.exit();
                }
            }
        });
    }
}
