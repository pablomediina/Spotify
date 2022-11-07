package es.ulpgc.spotify.downloader;

import com.google.gson.*;

import java.util.*;


public class Json {
    static SpotifyAccessor accessor;

    static {
        try {
            accessor = new SpotifyAccessor();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void uploadArtists(String artistId) throws Exception {
        String response = accessor.get("/artists/" + artistId, Map.of());
        JsonObject jsonObject = new Gson().fromJson(response, JsonObject.class);
        String name = jsonObject.get("name").getAsString();
        int popularity = jsonObject.get("popularity").getAsInt();
        JsonArray genres = jsonObject.get("genres").getAsJsonArray();
        int followers = jsonObject.get("followers").getAsJsonObject().get("total").getAsInt();
        Sqlite.postArtists(name, popularity, genres.toString(), followers);
    }

    public static void uploadAlbums(String artistId) throws Exception {
        String response = accessor.get("/artists/" + artistId + "/albums?include_groups=album", Map.of());
        JsonObject jsonObject = new Gson().fromJson(response, JsonObject.class);
        JsonArray items = jsonObject.get("items").getAsJsonArray();
        for (JsonElement item : items) {
            String nameAlbum = item.getAsJsonObject().get("name").getAsString();
            String releaseDate = item.getAsJsonObject().get("release_date").getAsString();
            int totalTracks = item.getAsJsonObject().get("total_tracks").getAsInt();
            JsonArray artists = item.getAsJsonObject().get("artists").getAsJsonArray();
            JsonArray artistsNames = new JsonArray();
            for (JsonElement artist : artists) artistsNames.add(artist.getAsJsonObject().get("name").getAsString());
            Sqlite.postAlbums(nameAlbum, artistsNames.toString(), releaseDate, totalTracks);
        }
    }

    public static void uploadSongs(String artistId) throws Exception {
        JsonArray songsIds = getSongsId(artistId);
        for (JsonElement songIdAsJsonElement : songsIds) {
            String songId = songIdAsJsonElement.getAsString();
            String response = accessor.get("/tracks/" + songId, Map.of());
            JsonObject jsonObject = new Gson().fromJson(response, JsonObject.class);
            String name = jsonObject.getAsJsonObject().get("name").getAsString();
            String releaseDate = jsonObject.get("album").getAsJsonObject().get("release_date").getAsString();
            int duration = jsonObject.getAsJsonObject().get("duration_ms").getAsInt();
            boolean explicit = jsonObject.getAsJsonObject().get("explicit").getAsBoolean();
            int popularity = jsonObject.getAsJsonObject().get("popularity").getAsInt();
            String album = jsonObject.get("album").getAsJsonObject().get("name").getAsString();
            JsonArray artists = jsonObject.get("artists").getAsJsonArray();
            JsonArray artistsNames = new JsonArray();
            for (JsonElement artist : artists) artistsNames.add(artist.getAsJsonObject().get("name").getAsString());
            Sqlite.postSongs(name, artistsNames.toString(), releaseDate, duration, explicit, popularity, album);
        }
    }

    public static JsonArray getAlbumsId(String artistId) throws Exception {
        String response = accessor.get("/artists/" + artistId + "/albums?include_groups=album", Map.of());
        JsonObject jsonObject = new Gson().fromJson(response, JsonObject.class);
        JsonArray items = jsonObject.get("items").getAsJsonArray();
        JsonArray albumsIds = new JsonArray();
        for (JsonElement item : items) albumsIds.add(item.getAsJsonObject().get("id").getAsString());
        return albumsIds;
    }

    public static JsonArray getSongsId(String artistId) throws Exception {
        JsonArray albumsIds = getAlbumsId(artistId);
        JsonArray songsIds = new JsonArray();
        for (JsonElement JsonElementalbumsId : albumsIds) {
            String albumsId = JsonElementalbumsId.getAsString();
            String response = accessor.get("/albums/" + albumsId + "/tracks", Map.of());
            JsonObject jsonObject = new Gson().fromJson(response, JsonObject.class);
            JsonArray items = jsonObject.get("items").getAsJsonArray();
            for (JsonElement item : items) songsIds.add(item.getAsJsonObject().get("id").getAsString());
        }
        return songsIds;
    }
}