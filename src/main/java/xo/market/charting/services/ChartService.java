package xo.market.charting.services;

import fr.alexpado.xodb4j.XoDB;
import fr.alexpado.xodb4j.interfaces.IItem;
import fr.alexpado.xodb4j.interfaces.IMarket;
import io.sentry.Sentry;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import xo.market.charting.AppConfig;
import xo.market.charting.lib.Chart;
import xo.market.charting.lib.ChartSet;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class ChartService {

    private final XoDB              xoDB;
    private final ChartCacheService cacheService;
    private final AppConfig         config;
    private final byte[]            source;
    private final byte[]            failed;

    public ChartService(XoDB xoDB, ChartCacheService cacheService, AppConfig config) throws IOException {

        this.xoDB         = xoDB;
        this.cacheService = cacheService;
        this.config       = config;
        this.source       = new ClassPathResource(config.getChartSource()).getInputStream().readAllBytes();
        this.failed       = new ClassPathResource(config.getChartFailure()).getInputStream().readAllBytes();
    }

    public byte[] getChart(HttpServletResponse response, int sendTime, int itemId, int hour) {

        try {
            LocalDateTime endTime = LocalDateTime.ofEpochSecond(sendTime, 0, ZoneOffset.UTC);
            File          file    = this.cacheService.getCache(itemId, endTime, hour);

            if (file.exists()) {
                response.addHeader("X-FROM-DISK", "Yes");
                return Files.readAllBytes(file.toPath());
            }
            response.addHeader("X-FROM-DISK", "No");

            LocalDateTime startTime = endTime.minusHours(hour);

            IItem         item      = this.xoDB.items().findById(itemId).complete();
            List<IMarket> priceData = this.xoDB.market(item).findAllBetween(startTime, endTime).complete();

            List<Integer> marketSellData = priceData.stream()
                    .map(IMarket::getMarketSell)
                    .map(price -> (int) Math.round(price * 100))
                    .toList();

            List<Integer> marketBuyData = priceData.stream()
                    .map(IMarket::getMarketBuy)
                    .map(price -> (int) Math.round(price * 100))
                    .toList();

            ChartSet sellSet = new ChartSet("Buy it for", Color.RED, marketSellData);
            ChartSet buySet  = new ChartSet("Sell it for", Color.GREEN, marketBuyData);

            BufferedImage image = ImageIO.read(new ByteArrayInputStream(this.source));
            Chart         chart = new Chart(this.config, image, "Last %s hours".formatted(hour), sellSet, buySet);

            chart.draw();

            ByteArrayOutputStream output           = new ByteArrayOutputStream();
            FileOutputStream      fileOutputStream = new FileOutputStream(file);

            ImageIO.write(image, "png", output);
            ImageIO.write(image, "png", fileOutputStream);
            fileOutputStream.close();
            output.close();
            return output.toByteArray();
        } catch (Exception e) {
            Sentry.captureException(e);
            return this.failed;
        }
    }

}
