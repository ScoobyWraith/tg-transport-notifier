package com.softpunk.trasportclient;

import java.time.Instant;

public record TransportDto(String transportId, Instant estimated) {}
