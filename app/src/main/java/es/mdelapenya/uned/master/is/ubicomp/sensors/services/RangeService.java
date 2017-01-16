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

package es.mdelapenya.uned.master.is.ubicomp.sensors.services;

import android.content.Context;

import java.util.List;

import es.mdelapenya.uned.master.is.ubicomp.sensors.db.RangeDAO;
import es.mdelapenya.uned.master.is.ubicomp.sensors.model.Range;

/**
 * @author Manuel de la Peña
 */
public class RangeService implements CRUDService<Range> {

    public RangeService(Context context) {
        this.context = context;
    }

    @Override
    public Range add(Range range) {
        try (RangeDAO rangeDAO = new RangeDAO((context))) {
            return rangeDAO.createRange(range.getMax(), range.getMin(), range.getName());
        }
    }

    @Override
    public void delete(Range range) {
        try (RangeDAO rangeDAO = new RangeDAO((context))) {
            rangeDAO.deleteRange(range);
        }
    }

    @Override
    public Range get(long id) {
        try (RangeDAO rangeDAO = new RangeDAO((context))) {
            return rangeDAO.getRange(id);
        }
    }

    @Override
    public List<Range> list() {
        try (RangeDAO rangeDAO = new RangeDAO((context))) {
            return rangeDAO.getAllRanges();
        }
    }

    @Override
    public Range update(Range range) {
        try (RangeDAO rangeDAO = new RangeDAO((context))) {
            return rangeDAO.updateRange(
                range.getId(), range.getMax(), range.getMin(), range.getName());
        }
    }

    private Context context;

}