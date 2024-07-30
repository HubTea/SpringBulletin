package com.bulletin.bulletin.common;

import java.time.LocalDateTime;
import java.util.UUID;

public class Constant {
    public static final LocalDateTime MIN_DATE = LocalDateTime.of(2000, 1, 1, 0, 0);

    public static final UUID MIN_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
}
