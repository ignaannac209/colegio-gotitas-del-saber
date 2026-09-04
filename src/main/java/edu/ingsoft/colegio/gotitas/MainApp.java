package main.java.edu.ingsoft.colegio.gotitas;

import javafx.application.Application;
import javafx.stage.Stage;
import main.java.edu.ingsoft.colegio.gotitas.config.DataBaseConnection;
import main.java.edu.ingsoft.colegio.gotitas.util.SceneManager;

/**
 * Clase principal de la aplicación JavaFX. Arranca el flujo de navegación
 * mostrando la vista de Login.
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneManager sceneManager = new SceneManager(primaryStage);
        sceneManager.showLoginView();
        primaryStage.show();
    }

    public static void main(String[] args) {
        try {
            DataBaseConnection.getConnectionDataBase();
            System.out.println("Conectado a la base de datos!");
        } catch (Exception e) {
            System.out.println("No se pudo conectar a la base de datos: " + e.getMessage());
            System.out.println("La aplicación continuará; puedes iniciar sesión con el usuario de prueba admin/123.");
        }
        launch(args);
    }
}
