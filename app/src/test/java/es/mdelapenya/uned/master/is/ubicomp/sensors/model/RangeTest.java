package es.mdelapenya.uned.master.is.ubicomp.sensors.model;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Manuel de la Peña
 */
public class RangeTest {

    @Test
    public void testCompareThisEqualsToThat() {
        Range r1 = new Range(1, 10, 1, "uno");
        Range r2 = new Range(2, 10, 1, "dos");

        Assert.assertEquals(0, r1.compareTo(r2));
    }

    @Test
    public void testCompareThisGreaterThanThat() {
        Range r1 = new Range(1, 100, 10, "uno");
        Range r2 = new Range(2, 10, 1, "dos");

        Assert.assertEquals(1, r1.compareTo(r2));

        r1 = new Range(1, 100, 1, "uno");
        r2 = new Range(2, 10, 1, "dos");

        Assert.assertEquals(1, r1.compareTo(r2));
    }

    @Test
    public void testCompareThisLowerThanThat() {
        Range r1 = new Range(1, 10, 1, "uno");
        Range r2 = new Range(2, 100, 10, "dos");

        Assert.assertEquals(-1, r1.compareTo(r2));

        r1 = new Range(1, 10, 1, "uno");
        r2 = new Range(2, 100, 1, "dos");

        Assert.assertEquals(-1, r1.compareTo(r2));
    }

    @Test
    public void testIsInRange() {
        Range r = new Range(1, 10, 1, "uno");

        Assert.assertFalse(r.isInRange(0));
        Assert.assertTrue(r.isInRange(1));
        Assert.assertTrue(r.isInRange(4));
        Assert.assertTrue(r.isInRange(10));
        Assert.assertFalse(r.isInRange(100));
    }

}
