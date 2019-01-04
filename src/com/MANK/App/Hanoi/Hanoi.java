package com.MANK.App.Hanoi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static java.lang.System.out;

public class Hanoi extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		Parent root =
				FXMLLoader.load(getClass().getResource("Hanoi.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		out.println("\n\n****Program ends****");
		System.exit(0);
	}

	public static void main(String[] args) {
		out.println("****program started****\n") ;
	    launch(args);
	}

}
