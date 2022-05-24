package xo.market.charting;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {

    @Value("${chart.source}")
    private String chartSource;

    @Value("${chart.failure}")
    private String chartFailure;

    @Value("${chart.options.min.position.x}")
    private int minPosX;

    @Value("${chart.options.min.position.y}")
    private int minPosY;

    @Value("${chart.options.max.position.x}")
    private int maxPosX;

    @Value("${chart.options.max.position.y}")
    private int maxPosY;

    @Value("${chart.options.area.position.x}")
    private int areaPosX;

    @Value("${chart.options.area.position.y}")
    private int areaPosY;

    @Value("${chart.options.area.size.w}")
    private int areaWidth;

    @Value("${chart.options.area.size.h}")
    private int areaHeight;

    @Value("${chart.options.area.stroke}")
    private int areaStroke;

    @Value("${chart.options.time.position.x}")
    private int timePosX;

    @Value("${chart.options.time.position.y}")
    private int timePosY;

    public String getChartSource() {

        return this.chartSource;
    }

    public String getChartFailure() {

        return this.chartFailure;
    }

    public int getMinPosX() {

        return this.minPosX;
    }

    public int getMinPosY() {

        return this.minPosY;
    }

    public int getMaxPosX() {

        return this.maxPosX;
    }

    public int getMaxPosY() {

        return this.maxPosY;
    }

    public int getAreaPosX() {

        return this.areaPosX;
    }

    public int getAreaPosY() {

        return this.areaPosY;
    }

    public int getAreaWidth() {

        return this.areaWidth;
    }

    public int getAreaHeight() {

        return this.areaHeight;
    }

    public int getAreaStroke() {

        return this.areaStroke;
    }

    public int getTimePosX() {

        return this.timePosX;
    }

    public int getTimePosY() {

        return this.timePosY;
    }

}
