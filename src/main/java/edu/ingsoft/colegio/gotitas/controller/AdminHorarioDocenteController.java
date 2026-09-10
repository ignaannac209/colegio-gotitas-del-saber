package main.java.edu.ingsoft.colegio.gotitas.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import main.java.edu.ingsoft.colegio.gotitas.model.docente.Docente;
import main.java.edu.ingsoft.colegio.gotitas.model.Horario;
import main.java.edu.ingsoft.colegio.gotitas.service.DocenteService;
import main.java.edu.ingsoft.colegio.gotitas.service.HorarioService;

/**
 * Controlador de la vista "Horario de Profesores": muestra, para el
 * profesor seleccionado, todas sus clases de la semana con materia y hora.
 */
public class AdminHorarioDocenteController implements Initializable {

    private final HorarioService horarioService;
    private final DocenteService docenteService;

    @FXML private ComboBox<Docente> comboDocente;
    @FXML private Label lblTituloHorario;
    @FXML private TableView<Horario> tablaHorario;
    @FXML private TableColumn<Horario, String> colDia;
    @FXML private TableColumn<Horario, String> colHora;
    @FXML private TableColumn<Horario, String> colMateria;
    @FXML private TableColumn<Horario, String> colSeccion;

    public AdminHorarioDocenteController(HorarioService horarioService, DocenteService docenteService) {
        this.horarioService = horarioService;
        this.docenteService = docenteService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colDia.setCellValueFactory(d -> new SimpleStringProperty(capitalizar(d.getValue().getDiaSemana())));
        colHora.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getRangoPeriodo()));
        colMateria.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombreMateria()));
        colSeccion.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombreSeccion()));

        comboDocente.valueProperty().addListener((obs, viejo, nuevo) -> mostrarHorarioDe(nuevo));
        cargarDocentes();
    }

    private void cargarDocentes() {
        try {
            List<Docente> docentes = docenteService.listar();
            comboDocente.setItems(FXCollections.observableArrayList(docentes));
        } catch (Exception e) {
            lblTituloHorario.setText(mensajeDe(e));
        }
    }

    private void mostrarHorarioDe(Docente docente) {
        if (docente == null) {
            tablaHorario.setItems(FXCollections.observableArrayList());
            lblTituloHorario.setText("");
            return;
        }
        try {
            List<Horario> horario = horarioService.listarPorDocente(docente.getIdDocente());
            tablaHorario.setItems(FXCollections.observableArrayList(horario));
            lblTituloHorario.setText("Horario semanal de " + docente.getNombreCompleto()
                    + " (" + horario.size() + " clase" + (horario.size() == 1 ? "" : "s") + " asignada"
                    + (horario.size() == 1 ? "" : "s") + ")");
        } catch (Exception e) {
            lblTituloHorario.setText(mensajeDe(e));
        }
    }

    private String capitalizar(String texto) {
        if (texto == null || texto.isEmpty()) {
            return texto;
        }
        return texto.charAt(0) + texto.substring(1).toLowerCase();
    }

    private String mensajeDe(Exception e) {
        return e.getMessage() != null ? e.getMessage() : "Ocurrió un error inesperado.";
    }
}
