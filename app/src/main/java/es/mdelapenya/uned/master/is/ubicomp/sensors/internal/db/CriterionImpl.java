package es.mdelapenya.uned.master.is.ubicomp.sensors.internal.db;

/**
 * @author Manuel de la Peña
 */
public class CriterionImpl implements Criterion {

    public CriterionImpl(String field, Object value) {
        this.field = field;
        this.value = value;
    }

    @Override
    public String getField() {
        return field;
    }

    @Override
    public Object getValue() {
        return value;
    }

    private String field;
    private Object value;

}