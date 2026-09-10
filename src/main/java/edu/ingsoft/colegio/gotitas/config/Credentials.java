package main.java.edu.ingsoft.colegio.gotitas.config;

/**
 * Contiene las credenciales de acceso a la base de datos.
 * En un entorno de producción real estos valores deberían leerse desde
 * variables de entorno (System.getenv) y nunca quedar escritos en el
 * código fuente.
 */
public class Credentials {

    public static final String URL_DB = "jdbc:mysql://localhost:3306/colegio_gotitas_del_saber_in4bm";
    public static final String USER_DB = "root";
    public static final String PASS_DB = "$DmynM4A";

    private Credentials() {
    }
}
