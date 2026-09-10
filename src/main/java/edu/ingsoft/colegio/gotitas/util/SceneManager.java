package main.java.edu.ingsoft.colegio.gotitas.util;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.java.edu.ingsoft.colegio.gotitas.controller.LoginController;
import main.java.edu.ingsoft.colegio.gotitas.controller.MainMenuController;
import main.java.edu.ingsoft.colegio.gotitas.controller.RegistroController;
import main.java.edu.ingsoft.colegio.gotitas.model.Auth;
import main.java.edu.ingsoft.colegio.gotitas.repository.AuthRepository;
import main.java.edu.ingsoft.colegio.gotitas.service.AuthService;

/**
  Administra toda la navegación entre pantallas de la aplicación.
 */
public class SceneManager {

    private static final String VIEW_LOGIN = "/main/resources/view/login-view.fxml";
    private static final String VIEW_REGISTRO = "/main/resources/view/registro-view.fxml";
    private static final String VIEW_MAIN_MENU = "/main/resources/view/main-menu-view.fxml";

    private final Stage primaryStage;
    private final AuthService authService;

    public SceneManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
        AuthRepository authRepository = new AuthRepository();
        this.authService = new AuthService(authRepository);
    }

    /** Muestra la Vista A: Login. */
    public void showLoginView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(VIEW_LOGIN));
        loader.setControllerFactory(clazz -> {
            if (clazz == LoginController.class) {
                return new LoginController(authService, this);
            }
            return instantiate(clazz);
        });

        renderScene(loader, "Colegio Gotitas del Saber - Iniciar Sesión", 480, 560);
    }

    /** Muestra la Vista B: Registro. */
    public void showRegistroView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(VIEW_REGISTRO));
        loader.setControllerFactory(clazz -> {
            if (clazz == RegistroController.class) {
                return new RegistroController(authService, this);
            }
            return instantiate(clazz);
        });

        renderScene(loader, "Colegio Gotitas del Saber - Crear Cuenta", 480, 620);
    }

    /**
     * Muestra la Vista C: Menú Principal (Dashboard).
     *
     * @param usuarioAutenticado datos de la sesión iniciada, mostrados en el
     *                            encabezado del menú.
     */
    public void showMainMenuView(Auth usuarioAutenticado) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(VIEW_MAIN_MENU));
        loader.setControllerFactory(clazz -> {
            if (clazz == MainMenuController.class) {
                return new MainMenuController(this, usuarioAutenticado);
            }
            return instantiate(clazz);
        });

        renderScene(loader, "Colegio Gotitas del Saber - Menú Principal", 900, 600);
    }

   
    private void renderScene(FXMLLoader loader, String title, double width, double height) throws IOException {
        Parent root = loader.load();
        Scene scene = new Scene(root, width, height);

        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    /** Fallback para controladores sin dependencias: usa su constructor vacío. */
    private Object instantiate(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("No se pudo crear el controlador " + clazz.getName(), e);
        }
    }
}
