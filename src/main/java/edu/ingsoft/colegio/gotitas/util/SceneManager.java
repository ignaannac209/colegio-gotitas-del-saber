package main.java.edu.ingsoft.colegio.gotitas.util;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.java.edu.ingsoft.colegio.gotitas.MainApp;

import main.java.edu.ingsoft.colegio.gotitas.controller.LoginController;
import main.java.edu.ingsoft.colegio.gotitas.controller.MainMenuController;
import main.java.edu.ingsoft.colegio.gotitas.controller.RegistroController;
import main.java.edu.ingsoft.colegio.gotitas.model.auth.Auth;
import main.java.edu.ingsoft.colegio.gotitas.repository.AuthRepository;
import main.java.edu.ingsoft.colegio.gotitas.service.AuthService;

public class SceneManager {

    private static final String VIEW_LOGIN
            = "/main/resources/view/login-view.fxml";

    private static final String VIEW_REGISTRO
            = "/main/resources/view/registro-view.fxml";

    private static final String VIEW_MAIN_MENU
            = "/main/resources/view/main-menu-view.fxml";

    private static final String DASHBOARD_ESTUDIANTE_VIEW
            = "/main/resources/view/estudiante-dashboard-view.fxml";

    private final Stage primaryStage;
    private final AuthService authService;

    public SceneManager(Stage primaryStage) {
        this.primaryStage = primaryStage;

        AuthRepository authRepository = new AuthRepository();
        this.authService = new AuthService(authRepository);
    }

    public void showLoginView() throws IOException {
        FXMLLoader loader
                = new FXMLLoader();
        URL url = MainApp.class.getResource(VIEW_LOGIN);
        loader.setLocation(url);
        LoginController.setAuthService(authService);
        LoginController.setSceneManager(this);

        renderScene(
                loader,
                "Colegio Gotitas del Saber - Iniciar Sesión",
                480,
                560
        );
    }

    public void showRegistroView() throws IOException {
        FXMLLoader loader
                = new FXMLLoader(getClass().getResource(VIEW_REGISTRO));

        loader.setController(new RegistroController(authService, this));

        renderScene(
                loader,
                "Colegio Gotitas del Saber - Crear Cuenta",
                480,
                620
        );
    }

    public void showMainMenuView(Auth usuarioAutenticado)
            throws IOException {

        FXMLLoader loader
                = new FXMLLoader();
        URL url = MainApp.class.getResource(VIEW_MAIN_MENU);
        loader.setLocation(url);        
        MainMenuController.setSceneManager(this);
        MainMenuController.setUsuarioAutenticado(usuarioAutenticado);

        renderScene(
                loader,
                "Colegio Gotitas del Saber - Menú Principal",
                900,
                600
        );
    }
     
    /**
     * Muestra la Vista d: Vista estudiante (Dashboard)*
     
     */
      

    //Aljendro Marroqin realizo metodo del  Dashboard 
    public void showDashBoardEstudiante(Auth usuarioAutenticado)
            throws IOException {

        FXMLLoader loader
                = new FXMLLoader(getClass().getResource(DASHBOARD_ESTUDIANTE_VIEW));

        

        renderScene(
                loader,
                "Colegio Gotitas del Saber - Menu principal",
                900,
                600
        );

    }


    private void renderScene(
            FXMLLoader loader,
            String title,
            double width,
            double height
    ) throws IOException {


        Scene scene = new Scene(loader.load(), width, height);

        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

}
