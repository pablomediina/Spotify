package es.ulpgc.spotify.downloader;

import java.util.Map;

import static es.ulpgc.spotify.downloader.Json.*;
import static es.ulpgc.spotify.downloader.Sqlite.*;


public class Main {

    public static Map<String, String> artist = Map.of(
            "Mora", "0Q8NcsJwoCbZOHHW63su5S",
            "Anuel AA", "2R21vXR83lH98kGeO99Y66",
            "Cruz Cafuné", "0jeYkqwckGJoHQhhXwgzk3",
            "Lunay", "47MpMsUfWtgyIIBEFOr4FE",
            "Bad Bunny", "4q3ewBCX7sLwd24euuV69X"
    );

    public static void main(String[] args) throws Exception {
        createTableArtist();
        createTableAlbums();
        createTableSongs();

        for (String artistIds : artist.values()) {
            uploadArtists(artistIds);
            uploadAlbums(artistIds);
            uploadSongs(artistIds);
        }
    }
}