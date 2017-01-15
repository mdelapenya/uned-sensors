package es.mdelapenya.uned.master.is.ubicomp.sensors.db.model;

import java.io.Serializable;

/**
 * @author Manuel de la Peña
 */
public class Range implements Serializable {

    private long id;
    private int max;
    private int min;
    private String name;

    public Range(long id, int max, int min, String name) {
        this.id = id;
        this.max = max;
        this.min = min;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        String strMax = String.valueOf(max);

        if (max == Integer.MAX_VALUE) {
            strMax = "MAX";
        }

        return "(" + id + ") " + name + ": " + min + " > " + strMax;
    }

}
