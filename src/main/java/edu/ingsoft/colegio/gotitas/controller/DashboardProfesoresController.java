package main.java.edu.ingsoft.colegio.gotitas.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class DashboardProfesoresController implements Initializable {

    @FXML
    private Button btnCursos;

    @FXML
    private Button btnAsistencia;

    @FXML
    private Button btnNotas;

    @FXML
    private Button btnCerrarSesion;

    @FXML
    private TableView<?> tableAlumnos;

    @FXML
    private TableColumn<?, ?> colCarnet;

    @FXML
    private TableColumn<?, ?> colNombre;

    @FXML
    private TableColumn<?, ?> colGrado;

    @FXML
    private TableColumn<?, ?> colAccion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }
}