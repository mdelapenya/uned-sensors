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

package es.mdelapenya.uned.master.is.ubicomp.sensors.util;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

import java.util.ArrayList;

/**
 * @author Manuel de la Peña
 */
public class AndroidBus {

    private static final Bus BUS = new MainThreadBus();

    public static Bus getInstance() {
        return BUS;
    }

    public static class MainThreadBus extends Bus {
        private final Handler handler = new Handler(Looper.getMainLooper());

        private ArrayList registeredObjects = new ArrayList<>();

        @Override
        public void post(final Object event) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                super.post(event);
            }
            else {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        MainThreadBus.super.post(event);
                    }

                });
            }
        }

        @Override
        public void register(Object object) {
            if (!registeredObjects.contains(object)) {
                registeredObjects.add(object);

                super.register(object);
            }
        }

        @Override
        public void unregister(Object object) {
            if (registeredObjects.contains(object)) {
                registeredObjects.remove(object);

                super.unregister(object);
            }
        }

    }

    private AndroidBus() {
    }

}
