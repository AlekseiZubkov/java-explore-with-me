package ru.practicum.ewm;

import ru.practicum.ewm.dto.EndpointHit;
import ru.practicum.ewm.dto.ViewStats;
import ru.practicum.ewm.exception.ClientException;

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

public class StatsClient {

    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;
    private final Gson gson = new GsonBuilder().create();

    public StatsClient(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public EndpointHit saveHit(EndpointHit endpointHitDto) {
        URI uri = URI.create(serverUrl + "/hit");
        HttpRequest request = buildPostRequest(uri, endpointHitDto.toString());
        return executeRequest(request, EndpointHit.class);
    }

    public List<ViewStats> getAllStats(LocalDateTime start, LocalDateTime end,
                                       List<String> uris, Boolean unique) {
        URI uri = buildStatsURI(start, end, uris, unique);
        HttpRequest request = buildGetRequest(uri);
        return executeRequest(request, new TypeToken<List<ViewStats>>() {}.getType());
    }

    private HttpRequest buildPostRequest(URI uri, String requestBody) {
        return HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
    }

    private URI buildStatsURI(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        return URI.create(serverUrl + "/stats?start={start}&end={end}&uris={uris}&unique={unique}");
    }

    private HttpRequest buildGetRequest(URI uri) {
        return HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
    }

    private <T> T executeRequest(HttpRequest request, Type responseType) {
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return gson.fromJson(response.body(), responseType);
        } catch (IOException | InterruptedException e) {
            throw new ClientException("Ошибка в клиенте статистики при выполнении запроса: " + request);
        }
    }
}