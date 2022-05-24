package xo.market.charting.lib;

import xo.market.charting.AppConfig;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Chart {

    private final AppConfig      config;
    private final BufferedImage  image;
    private final String         name;
    private final List<ChartSet> sets;

    private int min = Integer.MAX_VALUE;
    private int max = Integer.MIN_VALUE;

    public Chart(AppConfig config, BufferedImage image, String name, ChartSet... sets) {

        this.config = config;
        this.image  = image;
        this.name   = name;
        this.sets   = Arrays.asList(sets);

        for (ChartSet set : this.sets) {
            this.min = Integer.min(set.getMin(), this.min);
            this.max = Integer.max(set.getMax(), this.max);
        }
    }

    public void draw() {

        Graphics2D _2d = this.image.createGraphics();
        _2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.drawGraph(_2d);
        this.drawSets(_2d);
        _2d.dispose();
    }

    private void drawSets(Graphics2D _2d) {

        int minX = this.config.getMinPosX();
        int minY = this.config.getMinPosY();
        int maxX = this.config.getMaxPosX();
        int maxY = this.config.getMaxPosY();
        int txtX = this.config.getTimePosX();
        int txtY = this.config.getTimePosY();

        _2d.setFont(new Font("Roboto", Font.PLAIN, 20));
        _2d.setColor(Color.WHITE);

        _2d.drawString(String.format(Locale.US, "%.2f", this.min / 100.0), minX, minY);
        _2d.drawString(String.format(Locale.US, "%.2f", this.max / 100.0), maxX, maxY);
        _2d.drawString(this.name, txtX, txtY);

    }

    private void drawGraph(Graphics2D _2d) {

        int graphX = this.config.getAreaPosX();
        int graphY = this.config.getAreaPosY();
        int graphH = this.config.getAreaHeight();
        int graphW = this.config.getAreaWidth();
        int stokeW = this.config.getAreaStroke();

        this.sets.forEach(set -> set.clamp(this.min, this.max, graphH));

        // Set the stroke width for the graph.
        _2d.setStroke(new BasicStroke(stokeW));

        for (ChartSet set : this.sets) {
            _2d.setColor(set.getColor());

            List<Integer> values = set.getClampedValues();

            float fX = (float) graphW / (values.size() - 1);

            int prevY = -1;
            int prevX = -1;

            for (int xVal = 0; xVal < values.size(); xVal++) {
                int y  = graphY + graphH - values.get(xVal);
                int xn = Math.round(fX * (xVal));
                int x  = xn + graphX;

                if (prevY != -1) {
                    _2d.drawLine(prevX, prevY, x, y);
                }

                prevX = x;
                prevY = y;
            }
        }
    }

    public BufferedImage getImage() {

        return this.image;
    }

}
