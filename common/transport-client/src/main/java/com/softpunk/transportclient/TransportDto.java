package com.softpunk.transportclient;

import java.time.Instant;

public record TransportDto(String transportId, Instant estimated) {}
