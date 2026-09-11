package main.java.edu.ingsoft.colegio.gotitas.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import main.java.edu.ingsoft.colegio.gotitas.model.docente.Docente;
import main.java.edu.ingsoft.colegio.gotitas.model.cursos.Horario;
import main.java.edu.ingsoft.colegio.gotitas.model.cursos.Materia;
import main.java.edu.ingsoft.colegio.gotitas.model.cursos.Periodo;
import main.java.edu.ingsoft.colegio.gotitas.model.cursos.Seccion;
import main.java.edu.ingsoft.colegio.gotitas.service.CatalogoService;
import main.java.edu.ingsoft.colegio.gotitas.service.DocenteService;
import main.java.edu.ingsoft.colegio.gotitas.service.HorarioService;

/**
 * Controlador de la vista "Horarios": permite al director asignar, para
 * una sección + día + periodo, qué materia imparte qué profesor, y muestra
 * la tabla general resultante (sección, horario, materia y profesor).
 */
public class AdminHorariosController implements Initializable {

    private static final String[] DIAS = {"LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES"};

    private final HorarioService horarioService;
    private final CatalogoService catalogoService;
    private final DocenteService docenteService;

    @FXML private ComboBox<Seccion> comboSeccion;
    @FXML private ComboBox<String> comboDia;
    @FXML private ComboBox<Periodo> comboPeriodo;
    @FXML private ComboBox<Materia> comboMateria;
    @FXML private ComboBox<Docente> comboDocente;
    @FXML private Label lblMensaje;

    @FXML private TableView<Horario> tablaHorarios;
    @FXML private TableColumn<Horario, String> colSeccion;
    @FXML private TableColumn<Horario, String> colDia;
    @FXML private TableColumn<Horario, String> colHora;
    @FXML private TableColumn<Horario, String> colMateria;
    @FXML private TableColumn<Horario, String> colDocente;

    public AdminHorariosController(HorarioService horarioService, CatalogoService catalogoService, DocenteService docenteService) {
        this.horarioService = horarioService;
        this.catalogoService = catalogoService;
        this.docenteService = docenteService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colSeccion.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombreSeccion()));
        colDia.setCellValueFactory(d -> new SimpleStringProperty(capitalizar(d.getValue().getDiaSemana())));
        colHora.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getRangoPeriodo()));
        colMateria.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombreMateria()));
        colDocente.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombreDocente()));

        comboDia.setItems(FXCollections.observableArrayList(DIAS));

        cargarCombos();
        recargarTabla();
    }

    @FXML
    private void handleAsignar(ActionEvent event) {
        try {
            Horario horario = new Horario();
            Seccion seccion = comboSeccion.getValue();
            Periodo periodo = comboPeriodo.getValue();
            Materia materia = comboMateria.getValue();
            Docente docente = comboDocente.getValue();
            String dia = comboDia.getValue();

            horario.setIdSeccion(seccion != null ? seccion.getIdSeccion() : 0);
            horario.setIdPeriodo(periodo != null ? periodo.getIdPeriodo() : 0);
            horario.setIdMateria(materia != null ? materia.getIdMateria() : 0);
            horario.setIdDocente(docente != null ? docente.getIdDocente() : 0);
            horario.setDiaSemana(dia);

            horarioService.asignar(horario);
            mostrarMensaje("Clase asignada correctamente.", false);
            recargarTabla();
        } catch (Exception e) {
            mostrarMensaje(mensajeDe(e), true);
        }
    }

    @FXML
    private void handleEliminar(ActionEvent event) {
        Horario seleccionado = tablaHorarios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarMensaje("Selecciona una fila de la tabla primero.", true);
            return;
        }
        if (!confirmar("¿Eliminar la asignación de " + seleccionado.getNombreMateria() + " con "
                + seleccionado.getNombreDocente() + "?")) {
            return;
        }
        try {
            horarioService.eliminar(seleccionado.getIdHorario());
            recargarTabla();
        } catch (Exception e) {
            mostrarMensaje(mensajeDe(e), true);
        }
    }

    private void cargarCombos() {
        try {
            comboSeccion.setItems(FXCollections.observableArrayList(catalogoService.listarSecciones()));
            comboPeriodo.setItems(FXCollections.observableArrayList(catalogoService.listarPeriodos()));
            comboMateria.setItems(FXCollections.observableArrayList(catalogoService.listarMaterias()));

            List<Docente> docentes = docenteService.listar();
            comboDocente.setItems(FXCollections.observableArrayList(docentes));
        } catch (Exception e) {
            mostrarMensaje(mensajeDe(e), true);
        }
    }

    private void recargarTabla() {
        try {
            List<Horario> horarios = horarioService.listarTodos();
            tablaHorarios.setItems(FXCollections.observableArrayList(horarios));
        } catch (Exception e) {
            mostrarMensaje(mensajeDe(e), true);
        }
    }

    private String capitalizar(String texto) {
        if (texto == null || texto.isEmpty()) {
            return texto;
        }
        return texto.charAt(0) + texto.substring(1).toLowerCase();
    }

    private boolean confirmar(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, mensaje);
        alert.setHeaderText(null);
        alert.setTitle("Confirmar");
        return alert.showAndWait().filter(b -> b.getButtonData().isDefaultButton()).isPresent();
    }

    private void mostrarMensaje(String mensaje, boolean esError) {
        lblMensaje.setText(mensaje);
        lblMensaje.getStyleClass().removeAll("mensaje-error", "mensaje-info");
        lblMensaje.getStyleClass().add(esError ? "mensaje-error" : "mensaje-info");
    }

    private String mensajeDe(Exception e) {
        return e.getMessage() != null ? e.getMessage() : "Ocurrió un error inesperado.";
    }
}
