package xo.market.charting.services;

import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Formatter;

@Service
public class ChartCacheService {

    private File getCacheFolder(LocalDateTime time) {

        String filePath = "cache/%s/%s/%s/%s/%s".formatted(
                time.getYear(),
                time.getMonthValue(),
                time.getDayOfMonth(),
                time.getHour(),
                time.getMinute()
        );

        File cache = new File(filePath);

        if (!cache.exists() && !cache.mkdirs()) {
            throw new IllegalStateException("Unable to create cache folder.");
        }

        return cache;
    }

    private String computeHash(int item, int interval) throws NoSuchAlgorithmException {

        String signature = "%s@%s".formatted(item, interval);

        MessageDigest digest   = MessageDigest.getInstance("SHA-1");
        byte[]        checksum = digest.digest(signature.getBytes(StandardCharsets.UTF_8));

        Formatter formatter = new Formatter();
        for (byte b : checksum) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    public File getCache(int item, LocalDateTime time, int interval) throws NoSuchAlgorithmException {

        File folder = this.getCacheFolder(time);
        return new File(folder, this.computeHash(item, interval) + ".png");
    }

}
