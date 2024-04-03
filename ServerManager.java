import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.net.URI;

public class ServerManager extends Application {
    private ServeurWeb server;
    private Label statusLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Label titleLabel = new Label("Gestionnaire de Serveur");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        statusLabel = new Label("Statut du serveur: Arrêté");
        statusLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button startButton = new Button("Start Server");
        startButton.setStyle("-fx-font-size: 14px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        startButton.setOnAction(e -> startServer());

        Button stopButton = new Button("Stop Server");
        stopButton.setStyle("-fx-font-size: 14px; -fx-background-color: #f44336; -fx-text-fill: white;");
        stopButton.setOnAction(e -> stopServer());

        Button openBrowserButton = new Button("Ouvrir dans le navigateur");
        openBrowserButton.setStyle("-fx-font-size: 14px;");
        openBrowserButton.setOnAction(e -> openInBrowser());

        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.getChildren().addAll(titleLabel, statusLabel, startButton, stopButton, openBrowserButton);
        root.setAlignment(javafx.geometry.Pos.CENTER);
        root.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        Scene scene = new Scene(root, 350, 250);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Gestionnaire de Serveur");
        primaryStage.setOnCloseRequest(e -> {
            stopServer();
            Platform.exit();
        });
        primaryStage.show();
    }

    private void startServer() {
        if (server == null) {
            server = new ServeurWeb(80); // Spécifiez le port ici
        }

        // Mettre à jour le statut avant de démarrer le serveur
        statusLabel.setText("Statut du serveur: En cours");

        new Thread(() -> {
            try {
                server.startServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void stopServer() {
        if (server != null) {
            // Mettre à jour le statut avant d'arrêter le serveur
            statusLabel.setText("Statut du serveur: Arrêté");

            new Thread(() -> {
                try {
                    server.stopServer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private void openInBrowser() {
        // Vérifier si le serveur est démarré
        if (server != null) {
            // URL locale par défaut
            String url = "http://localhost:80"; // Modifier le port si nécessaire
    
            try {
                // Ouvrir l'URL dans le navigateur par défaut
                Desktop.getDesktop().browse(new URI(url));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            // Afficher un message d'avertissement si le serveur n'est pas démarré
            statusLabel.setText("Le serveur doit être démarré pour ouvrir dans le navigateur.");
        }
    }
    
}
