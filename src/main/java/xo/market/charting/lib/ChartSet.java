package xo.market.charting.lib;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ChartSet {

    private final String        name;
    private final List<Integer> values;
    private final Color         color;
    private final List<Integer> clampedValues;

    private int min = Integer.MAX_VALUE;
    private int max = Integer.MIN_VALUE;

    public ChartSet(String name, Color color, List<Integer> values) {

        this.name          = name;
        this.color         = color;
        this.values        = values;
        this.clampedValues = new ArrayList<>();

        for (Integer value : this.values) {
            this.min = Integer.min(this.min, value);
            this.max = Integer.max(this.max, value);
        }
    }

    public void clamp(int min, int max, int height) {

        this.values.forEach(value -> {
            double diff = max - min;
            this.clampedValues.add((int) (Math.round((height / diff) * (value - min))));
        });
    }

    public String getName() {

        return this.name;
    }

    public Color getColor() {

        return this.color;
    }

    public int getMin() {

        return this.min;
    }

    public int getMax() {

        return this.max;
    }

    public List<Integer> getClampedValues() {

        return this.clampedValues;
    }

}
