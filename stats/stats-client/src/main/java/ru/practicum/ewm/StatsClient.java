package ru.practicum.ewm;

import ru.practicum.ewm.dto.EndpointHit;
import ru.practicum.ewm.dto.ViewStats;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.exception.ClientException;

@Component
public class StatsClient {

    private final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .build();

    private final String serverUrl;
    private final Gson gson = getGson();

    public StatsClient(@Value("${stats-server.url}") String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public EndpointHit saveHit(EndpointHit endpointHitDto) {
        URI uri = URI.create(serverUrl + "/hit");
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(gson.toJson(endpointHitDto));
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .uri(uri)
                .POST(body)
                .build();
        try {
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return gson.fromJson(response.body(), EndpointHit.class);
        } catch (NullPointerException | IOException | InterruptedException e) {
            throw new ClientException("Ошибка в клиенте статистики при выполнении запроса: " + request);
        }
    }

    public List<ViewStats> getAllStats(String start, String end,
                                       List<String> uris, Boolean unique) {
        URI uri = UriComponentsBuilder.fromUriString(serverUrl)
                .path("/stats")
                .queryParam("start", start)
                .queryParam("end", end)
                .queryParam("uris", uris)
                .queryParam("unique", unique)
                .encode()
                .build()
                .toUri();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        try {
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Type userType = new TypeToken<List<ViewStats>>() {
            }.getType();
            return gson.fromJson(response.body(), userType);
        } catch (NullPointerException | IOException | InterruptedException e) {
            throw new ClientException("Ошибка в клиенте статистики при выполнении запроса: " + request);
        }
    }

    private static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        return gsonBuilder.create();
    }

}