package com.cloudator;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Util {

    public static String getAbsolutePathFromResource(String fullFileName) throws URISyntaxException {
        URL url = Util.class.getClassLoader().getResource(fullFileName);
        assert url != null;
        File file = Paths.get(url.toURI()).toFile();
        return file.getAbsolutePath();
    }

    public static long getTimestampInSeconds(LocalDateTime fromLocalDateTime) {
        return fromLocalDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000;
    }

}
