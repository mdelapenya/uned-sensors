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

import android.content.Context;

/**
 * @author Manuel de la Peña
 */
public class ResourceLocator {

    public static final String BASE_PACKAGE = "es.mdelapenya.uned.master.is.ubicomp.sensors";

    public static int getMipmapResourceByName(Context context, String name) {
        return getResourceByName(context, name, "mipmap");
    }

    public static int getStringResourceByName(Context context, String name) {
        return getResourceByName(context, name, "string");
    }

    private static int getResourceByName(Context context, String name, String resourceType) {
        return context.getResources().getIdentifier(name, resourceType, BASE_PACKAGE);
    }

}