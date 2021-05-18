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


import java.io.*;
import java.net.*;

public class ResponsiveUI extends Application {
Label statusLbl = new Label("Downloaded 0 bytes");
Button startBtn = new Button("Start download");
Button exitBtn = new Button("Exit");


public static void main(String[] args) {
       Application.launch(args);
}


@Override
public void start(Stage stage) {
// Add event handlers to the buttons
startBtn.setOnAction(e -> startTask());
exitBtn.setOnAction(e -> stage.close());
HBox buttonBox = new HBox(5, startBtn, exitBtn);
VBox root = new VBox(10, statusLbl, buttonBox);
Scene scene = new Scene(root);
stage.setScene(scene);
stage.setTitle("Responsive UI");
stage.setWidth(250);
stage.setHeight(250);
stage.show();
}




public void startTask() {



// Create a Runnable
Runnable task = () -> runTask();
// Run the task in a background thread
Thread backgroundThread = new Thread(task);

// Terminate the running thread if the application exits
backgroundThread.setDaemon(true);


// Start the thread
backgroundThread.start();

}

public void runTask() {


	  long totalBytes = 0L;

	  try {
		  //5.3GB
          String targetUrl = "http://ftp.halifax.rwth-aachen.de/debian-cd/current/amd64/iso-bd/debian-edu-10.9.0-amd64-BD-1.iso";
		  BufferedInputStream in = new BufferedInputStream(new URL(targetUrl).openStream());
	      byte dataBuffer[] = new byte[1024];
	      int bytesRead;
	      while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
	          totalBytes = totalBytes + bytesRead;
	          String labelText = "Done " + totalBytes + " bytes";
	          Platform.runLater(() -> statusLbl.setText(labelText));
	      }
	  } catch (IOException e) {
	      // handle exception
	      Platform.runLater(() -> statusLbl.setText("Error downloading resource"));
      }
}



}