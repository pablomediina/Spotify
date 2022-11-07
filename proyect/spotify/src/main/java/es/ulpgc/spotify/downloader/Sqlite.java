package es.ulpgc.spotify.downloader;

import java.sql.*;


public class Sqlite {
    static Conexion cc = new Conexion();
    static Connection cn = cc.conectar();
    static Statement statement;

    static {
        try {
            statement = cn.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createTableArtist() throws SQLException {
        statement.execute("CREATE TABLE IF NOT EXISTS artists (" +
                "name TEXT, " +
                "popularity NUMBER, " +
                "genres TEXT, " +
                "followers NUMBER);"
        );
    }

    public static void createTableAlbums() throws SQLException {
        statement.execute("CREATE TABLE IF NOT EXISTS albums (" +
                "name TEXT, " +
                "author TEXT, " +
                "releaseDate TEXT, " +
                "totalTracks NUMBER);"
        );
    }

    public static void createTableSongs() throws SQLException {
        statement.execute("CREATE TABLE IF NOT EXISTS songs (" +
                "title TEXT, " +
                "author TEXT, " +
                "releaseDate TEXT, " +
                "duration NUMBER, " +
                "explicit BOOLEAN, " +
                "popularity NUMBER, " +
                "album TEXT);"
        );
    }

    public static void postArtists(String name, int popularity, String genres, int followers) throws SQLException {
        PreparedStatement ps = cn.prepareStatement("INSERT INTO artists VALUES(?, ?, ?, ?);");
        ps.setString(1, name);
        ps.setInt(2, popularity);
        ps.setString(3, genres);
        ps.setInt(4, followers);
        ps.executeUpdate();
    }

    public static void postAlbums(String name, String author, String releaseDate, int totalTracks) throws SQLException {
        PreparedStatement ps = cn.prepareStatement("INSERT INTO albums VALUES(?, ?, ?, ?);");
        ps.setString(1, name);
        ps.setString(2, author);
        ps.setString(3, releaseDate);
        ps.setInt(4, totalTracks);
        ps.executeUpdate();
    }

    public static void postSongs(String title, String author, String releaseDate, int duration,
                                 boolean explicit, int popularity, String album) throws SQLException {
        PreparedStatement ps = cn.prepareStatement("INSERT INTO songs VALUES(?, ?, ?, ?, ?, ?, ?);");
        ps.setString(1, title);
        ps.setString(2, author);
        ps.setString(3, releaseDate);
        ps.setInt(4, duration);
        ps.setBoolean(5, explicit);
        ps.setInt(6, popularity);
        ps.setString(7, album);
        ps.executeUpdate();
    }
}