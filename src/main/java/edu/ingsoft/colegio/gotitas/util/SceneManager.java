package main.java.edu.ingsoft.colegio.gotitas.util;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import main.java.edu.ingsoft.colegio.gotitas.controller.DashboardController;
import main.java.edu.ingsoft.colegio.gotitas.controller.LoginController;
import main.java.edu.ingsoft.colegio.gotitas.controller.MainMenuController;
import main.java.edu.ingsoft.colegio.gotitas.controller.RegistroController;
import main.java.edu.ingsoft.colegio.gotitas.model.auth.Auth;
import main.java.edu.ingsoft.colegio.gotitas.repository.AuthRepository;
import main.java.edu.ingsoft.colegio.gotitas.service.AuthService;

public class SceneManager {

    private static final String VIEW_LOGIN =
            "/main/resources/view/login-view.fxml";

    private static final String VIEW_REGISTRO =
            "/main/resources/view/registro-view.fxml";

    private static final String VIEW_MAIN_MENU =
            "/main/resources/view/main-menu-view.fxml";

    private static final String VIEW_DASHBOARD_DOCENTES =
            "/main/resources/view/dashboard-view-docentes.fxml";

    private final Stage primaryStage;
    private final AuthService authService;

    public SceneManager(Stage primaryStage) {
        this.primaryStage = primaryStage;

        AuthRepository authRepository = new AuthRepository();
        this.authService = new AuthService(authRepository);
    }

    public void showLoginView() throws IOException {
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource(VIEW_LOGIN));

        loader.setControllerFactory(clazz -> {
            if (clazz == LoginController.class) {
                return new LoginController(authService, this);
            }

            return instantiate(clazz);
        });

        renderScene(
                loader,
                "Colegio Gotitas del Saber - Iniciar Sesión",
                480,
                560
        );
    }

    public void showRegistroView() throws IOException {
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource(VIEW_REGISTRO));

        loader.setControllerFactory(clazz -> {
            if (clazz == RegistroController.class) {
                return new RegistroController(authService, this);
            }

            return instantiate(clazz);
        });

        renderScene(
                loader,
                "Colegio Gotitas del Saber - Crear Cuenta",
                480,
                620
        );
    }

    public void showMainMenuView(Auth usuarioAutenticado)
            throws IOException {

        FXMLLoader loader =
                new FXMLLoader(getClass().getResource(VIEW_MAIN_MENU));

        loader.setControllerFactory(clazz -> {
            if (clazz == MainMenuController.class) {
                return new MainMenuController(
                        this,
                        usuarioAutenticado
                );
            }

            return instantiate(clazz);
        });

        renderScene(
                loader,
                "Colegio Gotitas del Saber - Menú Principal",
                900,
                600
        );
    }

    /**
     * Carga el dashboard exclusivo para el rol Docente.
     * Antes de este método, no existía ninguna ruta hacia
     * dashboard-view-docentes.fxml, por eso un docente terminaba siempre
     * viendo el Menú Principal de administrador.
     */
    public void showDashboardDocenteView(Auth usuarioAutenticado) throws IOException {
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource(VIEW_DASHBOARD_DOCENTES));

        loader.setControllerFactory(clazz -> {
            if (clazz == DashboardController.class) {
                return new DashboardController(this, usuarioAutenticado);
            }

            return instantiate(clazz);
        });

        renderScene(
                loader,
                "Colegio Gotitas del Saber - Panel Docente",
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

        Parent root = loader.load();

        Scene scene = new Scene(root, width, height);

        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    private Object instantiate(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(
                    "No se pudo crear el controlador "
                    + clazz.getName(),
                    e
            );
        }
    }
}