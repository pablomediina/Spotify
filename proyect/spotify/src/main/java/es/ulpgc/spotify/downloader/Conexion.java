package es.ulpgc.spotify.downloader;

import java.sql.DatabaseMetaData;
import java.sql.DriverManager;

public class Conexion {
    java.sql.Connection conectar = null;

    public java.sql.Connection conectar() {
        try {
            Class.forName("org.sqlite.JDBC");
            conectar = DriverManager.getConnection("jdbc:sqlite:database.db");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return conectar;
    }
}
