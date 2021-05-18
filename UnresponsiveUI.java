/*******
 **
 ** Simple example of an UI that becomes unresponsive once a time consuming
 ** task has started.
 **
 ** This program downloads a linux distribution (~5.3GB) representing a time consuming task and attempts to update
 ** some UI elements related to the progress.
 ** The application does NOT use threads.
 **
 ** @v0.1/tzagara/rd1105211853
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

import javafx.event.ActionEvent;
import javafx.event.EventHandler;



import java.io.*;
import java.net.*;

import java.util.*;

import java.lang.*;
import java.util.concurrent.TimeUnit;





public class UnresponsiveUI extends Application {

        /**
         ** UI elements
         **/

		Label statusLbl = new Label(" "); //status label. Any status change is displayed here
		Button startBtn = new Button("Start download"); //button starting the download
		Button exitBtn = new Button("Terminate download & Exit"); //button quitting the app (and terminating the download if in progress)



/**
 ** Story starts here
 **/

public static void main(String[] args) {
       Application.launch(args);
}





@Override
public void start(Stage stage) {


		/**
		 ** Add event handlers to  buttons
		 **/

		// When start button is pressed, download begins
		startBtn.setOnAction(e -> runTask());
		// When exit button is pressed, application quits
		exitBtn.setOnAction(e -> stage.close());

		//Making buttons/boxes look nicely
		HBox buttonBox = new HBox(5, startBtn, exitBtn);
		buttonBox.setStyle("-fx-padding: 10;-fx-border-style: solid outside;"
		                 + "-fx-border-width: 2;" + "-fx-border-insets: 3;"
                         + "-fx-border-radius: 5;" + "-fx-border-color: red;");

		VBox root = new VBox(10, statusLbl, buttonBox);
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Unesponsive UI");
		stage.setWidth(350);
		stage.setHeight(250);

		//ok, show the UI to the user
		stage.show();
}


/**
 ** Start the actual download
 **
 **/

public void runTask() {

      System.out.println("\n\n\n");
	  long totalBytes = 0L;

	  //Update status label
	  // NOTE: this will not have ANY impact
      statusLbl.setText("Started download...");

	  try {

          //signal that we started
		  long startTime = System.currentTimeMillis();

		  //5.3GB file
          String targetUrl = "http://ftp.halifax.rwth-aachen.de/debian-cd/current/amd64/iso-bd/debian-edu-10.9.0-amd64-BD-1.iso";
          Long targetFileSize = 5690831666L;
		  BufferedInputStream in = new BufferedInputStream(new URL(targetUrl).openStream());
	      byte dataBuffer[] = new byte[1024];
	      int bytesRead;

	      while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
	          totalBytes = totalBytes + bytesRead;

	          // This has no effect...
              statusLbl.setText("Done " + totalBytes + " bytes");


	          //Update progress on the console (this is possible)
	          printProgress(startTime,  targetFileSize, totalBytes);

	      }
	      //System.out.println(".");
	  } catch (IOException e) {
	      // Inform about exception
	      statusLbl.setText("Error downloading resource");
      }
}

/**
 ** Prints progress, ETA ON THE CONSOLE in a fancy way
 **
 ** Disclaimer:
 ** Not my code. Got this from: https://stackoverflow.com/questions/1001290/console-based-progress-in-java
 **/
private  void printProgress(long startTime, long total, long current) {
    long eta = current == 0 ? 0 :
        (total - current) * (System.currentTimeMillis() - startTime) / current;

    String etaHms = current == 0 ? "N/A" :
            String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(eta),
                    TimeUnit.MILLISECONDS.toMinutes(eta) % TimeUnit.HOURS.toMinutes(1),
                    TimeUnit.MILLISECONDS.toSeconds(eta) % TimeUnit.MINUTES.toSeconds(1));

    StringBuilder string = new StringBuilder(140);
    int percent = (int) (current * 100 / total);
    string
        .append('\r')
        .append(String.join("", Collections.nCopies(percent == 0 ? 2 : 2 - (int) (Math.log10(percent)), " ")))
        .append(String.format(" %d%% [", percent))
        .append(String.join("", Collections.nCopies(percent, "=")))
        .append('>')
        .append(String.join("", Collections.nCopies(100 - percent, " ")))
        .append(']')
        .append(String.join("", Collections.nCopies(current == 0 ? (int) (Math.log10(total)) : (int) (Math.log10(total)) - (int) (Math.log10(current)), " ")))
        .append(String.format(" %d/%d, ETA: %s", current, total, etaHms));

    System.out.print(string);
}





}


