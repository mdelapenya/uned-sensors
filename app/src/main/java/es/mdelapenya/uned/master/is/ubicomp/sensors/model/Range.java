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
        return toString("km");
    }

    public String toString(String units) {
        String strMax = String.valueOf(max);

        if (max == Integer.MAX_VALUE) {
            strMax = "MAX";
        }

        return min + " " + units + " > " + strMax + " " + units;
    }

}
