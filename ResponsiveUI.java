/*******
 **
 ** Simple example of an UI that is responsive once a time consuming
 ** task has started.
 **
 ** This program downloads a linux distribution (~5.3GB) representing a time consuming task and attempts to update
 ** some UI elements related to the progress.
 ** The application does  use threads which allows the application to properly update status messages and respond to
 ** mouse events.
 **
 ** @v0.1/tzagara/rd1105211919
 **
 **
 **
 *******/


import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


import java.io.*;
import java.net.*;

public class ResponsiveUI extends Application {
Label statusLbl = new Label("Download progress");
Button startBtn = new Button("Start download");
Button exitBtn = new Button("Terminate download & Exit");


public static void main(String[] args) {
       Application.launch(args);
}


@Override
public void start(Stage stage) {

		// Add event handlers to the buttons
		startBtn.setOnAction(e -> startTask());
		exitBtn.setOnAction(e -> stage.close());
		HBox buttonBox = new HBox(5, startBtn, exitBtn);

		//Making buttons/boxes look nicely
		buttonBox.setStyle("-fx-padding: 10;-fx-border-style: solid outside;"
								 + "-fx-border-width: 2;" + "-fx-border-insets: 3;"
								 + "-fx-border-radius: 5;" + "-fx-border-color: green;");

		VBox root = new VBox(10, statusLbl, buttonBox);
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Responsive UI");
		stage.setWidth(350);
		stage.setHeight(250);

		//ok, show the UI to the user
		stage.show();
}



/**
 ** Setting up the task to be executed as a separate thread.
 ** Uses lambda expressions to setup the method to be executed
 ** inside the thread.
 **
 **/

public void startTask() {




		// Create a Runnable
		// NOTE: runTask() does the actual download
		Runnable task = () -> runTask();


		// Create a thread so that the task can be executed
		// concurrently with the Main thread
		Thread backgroundThread = new Thread(task);

		// Signal to terminate the running background thread if the application
		// or rather the Main thread exits
		backgroundThread.setDaemon(true);


		// Everything done. Start the thread now
		// This will have as a result the execution of method runTask()
		backgroundThread.start();

		// Everything seems fine. Disable the start button once a download is executed.
        // We do this to avoid starting more than one downloads of the same file - which is possible yet
        // does not make any sense.
		startBtn.setDisable(true);

}



/**
 ** Actual download of file
 ** This method will be executed in a separate thread and
 ** concurrently with the Main thread.
 **
 **/

public void runTask() {

      /**
       ** Change the visual appearance of the label to indicate that
       ** thread has started.
       **/
      statusLbl.setTextFill(Color.web("green"));
      statusLbl.setStyle( "-fx-font: 18px Helvetica; -fx-fill: linear-gradient(from 0% 0% to 100% 200%, repeat, aqua 0%, red 50%);"
                          + "-fx-stroke: black;"
                          + "-fx-stroke-width: 1;");



	  long totalBytes = 0L;
	  try {
		  //URL to 5.3GB file of linux distro to download
          String targetUrl = "http://ftp.halifax.rwth-aachen.de/debian-cd/current/amd64/iso-bd/debian-edu-10.9.0-amd64-BD-1.iso";
		  BufferedInputStream in = new BufferedInputStream(new URL(targetUrl).openStream());
	      byte dataBuffer[] = new byte[1024];
	      int bytesRead;
	      //Read 1024 bytes each time
	      while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {

	          totalBytes = totalBytes + bytesRead;


	          //Update UI status label.
	          final String labelText = "Downloaded " + totalBytes + " Bytes";
	          Platform.runLater(() -> statusLbl.setText(labelText));


              //Change label color just to show that this is also possible now.
	          if ( totalBytes > 20000000)
	               statusLbl.setTextFill(Color.web("red"));
	          else  if ( totalBytes > 5242880)
	                     statusLbl.setTextFill(Color.web("orange"));

	      } //while



	  } catch (IOException e) {
	      // handle exception by notifying the UI via the status label
	      Platform.runLater(() -> statusLbl.setText("Error downloading resource"));
      }

      // If download finished for any reason, tell the UI thread to enable download again.
      Platform.runLater(() -> startBtn.setDisable(false));

} //runTask



} //ResponsiveUI