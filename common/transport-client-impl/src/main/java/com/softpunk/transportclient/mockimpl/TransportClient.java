package com.softpunk.transportclient.mockimpl;

import com.softpunk.transportclient.Client;
import com.softpunk.transportclient.TransportDto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TransportClient implements Client {
    private final int rndInitSeed = new Random().nextInt();

    @Override
    public Map<String, List<TransportDto>> getEstimated(Map<String, List<String>> transportsOnStops) {
        int minIntervalMs = 3 * 60 * 1000;
        int maxIntervalMs = 7 * 60 * 1000;
        Instant now = Instant.now();
        Map<String, List<TransportDto>> result = new HashMap<>();

        for (String stopId : transportsOnStops.keySet()) {
            List<String> transports = transportsOnStops.get(stopId);
            List<TransportDto> resultTransports = new ArrayList<>();

            for (String transportId : transports) {
                Random rnd = new Random((long) rndInitSeed * transportId.hashCode());

                int periodMs = (int) (minIntervalMs + (maxIntervalMs - minIntervalMs) * rnd.nextDouble());
                long periods = now.toEpochMilli() / periodMs;
                Instant estimated = Instant.ofEpochMilli((periods + 1) * periodMs);
                resultTransports.add(new TransportDto(transportId, estimated));
            }

            result.put(stopId, resultTransports);
        }

        return result;
    }
}
