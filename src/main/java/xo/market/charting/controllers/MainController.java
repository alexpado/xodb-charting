package xo.market.charting.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import xo.market.charting.services.ChartService;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/chart")
public class MainController {

    private final ChartService chart;

    public MainController(ChartService chart) {

        this.chart = chart;
    }

    @GetMapping(
            value = "/{sendTime:\\d+}/{item:\\d+}.png",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public byte[] getChart(HttpServletResponse response, @PathVariable int sendTime, @PathVariable int item, @RequestParam(value = "hour", defaultValue = "5") int hour) {

        // Keep hour between 1 & 24 (inclusive)
        int realHour = hour < 1 ? 1 : hour > 24 ? 24 : hour;
        return this.chart.getChart(response, sendTime, item, realHour);
    }

}
