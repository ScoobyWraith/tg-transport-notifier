package com.softpunk.trasportclient;

import java.util.List;
import java.util.Map;

public interface Client {
    Map<String, List<TransportDto>> getEstimated(Map<String, List<String>> transportsOnStops);
}
