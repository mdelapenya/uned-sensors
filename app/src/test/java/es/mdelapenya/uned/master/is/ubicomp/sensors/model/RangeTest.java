/**
 *    Copyright 2017-today Manuel de la Peña
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    public void testDefaultConstructor() {
        Range range = new Range();

        Assert.assertEquals(0, range.getId());
        Assert.assertEquals("", range.getName());
        Assert.assertEquals(0, range.getMin());
        Assert.assertEquals(0, range.getMax());
    }

    @Test
    public void testIsInRange() {
        Range r = new Range(1, 10, 1, "uno");

        // speed smaller than min
        Assert.assertFalse(r.isInRange(0));

        // speed is equals to min
        Assert.assertTrue(r.isInRange(1));

        // speed is between max and min
        Assert.assertTrue(r.isInRange(4));

        // speed is equals to max
        Assert.assertTrue(r.isInRange(10));

        // speed is greater than max
        Assert.assertFalse(r.isInRange(100));
    }

}
