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

import java.util.List;

import es.mdelapenya.uned.master.is.ubicomp.sensors.internal.db.Criterion;

/**
 * @author Manuel de la Peña
 */
public interface CRUDService<T> {

    T add(T t);

    void delete(T t);

    List<T> findBy(Criterion criterion);

    T get(long id);

    List<T> list();

    T update(T t);

}