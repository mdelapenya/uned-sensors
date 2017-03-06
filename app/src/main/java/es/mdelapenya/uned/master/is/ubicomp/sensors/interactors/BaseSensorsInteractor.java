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

package es.mdelapenya.uned.master.is.ubicomp.sensors.interactors;

import es.mdelapenya.uned.master.is.ubicomp.sensors.services.SensorsService;
import es.mdelapenya.uned.master.is.ubicomp.sensors.util.AndroidBus;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * @author Manuel de la Peña
 */
public abstract class BaseSensorsInteractor implements SensorsInteractor {

    private static final String BACKEND_ENDPOINT =
        "http://api.mdelapenya-sensors.wedeploy.io";

    @Override
    public void run() {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BACKEND_ENDPOINT)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

            SensorsService sensorsService = retrofit.create(SensorsService.class);

            Call<String> stringCall = getResponse(sensorsService);

            Response<String> response = stringCall.execute();

            Object event = new Error(response.message());

            if (response.isSuccessful()) {
                event = response.body();
            }

            AndroidBus.getInstance().post(event);
        }
        catch (IOException e) {
            AndroidBus.getInstance().post(e);
        }
    }

    protected abstract Call<String> getResponse(SensorsService sensorsService)
        throws IOException;

}
