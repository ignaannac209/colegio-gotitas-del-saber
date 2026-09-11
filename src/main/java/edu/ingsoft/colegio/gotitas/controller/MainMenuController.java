package main.java.edu.ingsoft.colegio.gotitas.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import main.java.edu.ingsoft.colegio.gotitas.model.auth.Auth;
import main.java.edu.ingsoft.colegio.gotitas.repository.AlumnoRepository;
import main.java.edu.ingsoft.colegio.gotitas.repository.CursoRepository;
import main.java.edu.ingsoft.colegio.gotitas.repository.DocenteRepository;
import main.java.edu.ingsoft.colegio.gotitas.repository.HorarioRepository;
import main.java.edu.ingsoft.colegio.gotitas.repository.MateriaRepository;
import main.java.edu.ingsoft.colegio.gotitas.repository.PeriodoRepository;
import main.java.edu.ingsoft.colegio.gotitas.repository.SeccionRepository;
import main.java.edu.ingsoft.colegio.gotitas.service.AlumnoService;
import main.java.edu.ingsoft.colegio.gotitas.service.CatalogoService;
import main.java.edu.ingsoft.colegio.gotitas.service.DocenteService;
import main.java.edu.ingsoft.colegio.gotitas.service.HorarioService;
import main.java.edu.ingsoft.colegio.gotitas.util.SceneManager;

/**
 * Controlador de la Vista C (Menú Principal / Dashboard).
 * Muestra el nombre del usuario autenticado, la navegación lateral
 * (incluyendo el panel de administración del director) y gestiona el
 * cierre de sesión.
 */
public class MainMenuController implements Initializable {

    private static final String VIEW_PERSONAS = "/main/resources/view/admin-personas-view.fxml";
    private static final String VIEW_CATALOGOS = "/main/resources/view/admin-catalogos-view.fxml";
    private static final String VIEW_HORARIOS = "/main/resources/view/admin-horarios-view.fxml";
    private static final String VIEW_HORARIO_DOCENTE = "/main/resources/view/admin-horario-docente-view.fxml";
    private static final String VIEW_DOCENTES_ALUMNOS = "/main/resources/view/admin-docentes-alumnos-view.fxml";
    private static final String  DASHBOARD_ESTUDIANTE_VIEW = "/main/resources/view/estudiante-dashboard-view.fxml";
    private static SceneManager sceneManager;
    private static Auth usuarioAutenticado;

    // Servicios del panel de administración, compartidos entre las sub-vistas.
    private  AlumnoService alumnoService;
    private  DocenteService docenteService;
    private  CatalogoService catalogoService;
    private  HorarioService horarioService;

    @FXML
    private Label lblUsuarioActivo;

    @FXML
    private Label lblTituloSeccion;

    @FXML
    private StackPane paneContenido;

    public MainMenuController(){
        
    }
    
    public MainMenuController(SceneManager sceneManager, Auth usuarioAutenticado) {
        this.sceneManager = sceneManager;
        this.usuarioAutenticado = usuarioAutenticado;

        this.alumnoService = new AlumnoService(new AlumnoRepository());
        this.docenteService = new DocenteService(new DocenteRepository());
        this.catalogoService = new CatalogoService(
                new CursoRepository(), new SeccionRepository(), new MateriaRepository(), new PeriodoRepository());
        this.horarioService = new HorarioService(new HorarioRepository(), new AlumnoRepository());
    }

    public static void setSceneManager(SceneManager sceneManager) {
        MainMenuController.sceneManager = sceneManager;
    }

    public static void setUsuarioAutenticado(Auth usuarioAutenticado) {
        MainMenuController.usuarioAutenticado = usuarioAutenticado;
    }

    
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String nombre = usuarioAutenticado != null ? usuarioAutenticado.getNombreCompleto() : "Usuario";
        lblUsuarioActivo.setText(nombre);
        mostrarSeccion("Inicio", "Bienvenido(a) de nuevo, " + nombre + ".");
    }

    @FXML
    private void handleInicio(ActionEvent event) {
        mostrarSeccion("Inicio", "Bienvenido(a) de nuevo, " + lblUsuarioActivo.getText() + ".");
    }

    @FXML
    private void handlePerfil(ActionEvent event) {
        mostrarSeccion("Perfil", "Aquí se mostrará la información del perfil del usuario.");
    }

    @FXML
    private void handleConfiguracion(ActionEvent event) {
        mostrarSeccion("Configuración", "Aquí se mostrarán las opciones de configuración de la cuenta.");
    }

    @FXML
    private void handlePersonas(ActionEvent event) {
        cargarVistaAdmin(VIEW_PERSONAS, "Docentes y Alumnos", clazz -> {
            if (clazz == AdminPersonasController.class) {
                return new AdminPersonasController(docenteService, alumnoService, catalogoService);
            }
            return null;
        });
    }

    @FXML
    private void handleCatalogos(ActionEvent event) {
        cargarVistaAdmin(VIEW_CATALOGOS, "Catálogos: Cursos, Secciones y Materias", clazz -> {
            if (clazz == AdminCatalogosController.class) {
                return new AdminCatalogosController(catalogoService);
            }
            return null;
        });
    }

    @FXML
    private void handleHorarios(ActionEvent event) {
        cargarVistaAdmin(VIEW_HORARIOS, "Horarios y Asignaciones (7:05 am - 12:05 pm)", clazz -> {
            if (clazz == AdminHorariosController.class) {
                return new AdminHorariosController(horarioService, catalogoService, docenteService);
            }
            return null;
        });
    }

    @FXML
    private void handleHorarioDocente(ActionEvent event) {
        cargarVistaAdmin(VIEW_HORARIO_DOCENTE, "Horario Diario de Profesores", clazz -> {
            if (clazz == AdminHorarioDocenteController.class) {
                return new AdminHorarioDocenteController(horarioService, docenteService);
            }
            return null;
        });
    }

    @FXML
    private void handleDocentesAlumnos(ActionEvent event) {
        cargarVistaAdmin(VIEW_DOCENTES_ALUMNOS, "Profesores y sus Alumnos", clazz -> {
            if (clazz == AdminDocentesAlumnosController.class) {
                return new AdminDocentesAlumnosController(docenteService, horarioService);
            }
            return null;
        });
    }

    @FXML
    private void handleCerrarSesion(ActionEvent event) throws Exception {
        paneContenido.getChildren().clear();
        sceneManager.showLoginView();
    }

    
    
   @FXML
private void handledDashBoardEstudianteView(ActionEvent event) {
    cargarVistaAdmin(DASHBOARD_ESTUDIANTE_VIEW, "Horario Diario de Profesores", clazz -> {
            if (clazz == AdminHorarioDocenteController.class) {
                return new AdminHorarioDocenteController(horarioService, docenteService);
            }
            return null;
           });
    }
    

    /** Actualiza el área central con un contenido de ejemplo por sección. */
    private void mostrarSeccion(String titulo, String contenido) {
        lblTituloSeccion.setText(titulo);
        paneContenido.getChildren().clear();
        Label label = new Label(contenido);
        label.getStyleClass().add("contenido-placeholder");
        paneContenido.getChildren().add(label);
    }

    /** Carga una vista FXML del panel de administración dentro del área central. */
    private void cargarVistaAdmin(String rutaFxml, String titulo, javafx.util.Callback<Class<?>, Object> controllerFactory) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFxml));
            loader.setControllerFactory(controllerFactory);
            Parent vista = loader.load();
            
            lblTituloSeccion.setText(titulo);
            paneContenido.getChildren().clear();
            paneContenido.getChildren().add(vista);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al cargar la vista");
            alert.setHeaderText(null);
            alert.setContentText("No fue posible abrir \"" + titulo + "\": " + e.getMessage());
            alert.showAndWait();
        }
    }
}
