# Práctica Sensores

    Alumno: Manuel de la Peña
    D.N.I.: 53.430.012-T
    Asignatura: Computación Ubícua
    Máster de Investigación en Ingeniería del Software y Sistemas Informáticos
    Curso: 2016-2017
    Universidad Nacional de Educación a Distancia (U.N.E.D.)

Para acceder en modo online a este documento, por favor visite [el siguiente enlace](https://github.com/mdelapenya/uned-sensors/blob/master/README.md)

## Enunciado de la práctica

Diseñar e implementar un nuevo sensor (denominado MIVELOCIDAD) “virtual” que devolverá un valor
(enumerado) según la velocidad a la que se desplace el dispositivo que lo utilice.

## Descripción de la práctica

La práctica consiste en el diseño de un nuevo sensor MIVELOCIDAD que devolverá el estado de velocidad
al que se encuentra el dispositivo en el que se encuentra instalado.

El nuevo sensor MIVELOCIDAD define los estados de velocidad de acuerdo a los siguientes criterios:

* Velocidad Mínima 0km/h – Velocidad Máxima 1Km/h: ESTADO PARADO.
* Velocidad Mínima 1km/h – Velocidad Máxima 4Km/h: ESTADO CAMINANDO
* Velocidad Mínima 4km/h – Velocidad Máxima 6Km/h: ESTADO MARCHANDO
* Velocidad Mínima 6km/h – Velocidad Máxima 12Km/h: ESTADO CORRIENDO
* Velocidad Mínima 12km/h – Velocidad Máxima 25Km/h: ESTADO SPRINT
* Velocidad Mínima 25km/h – Velocidad Máxima 170Km/h: ESTADO VEH. MOTOR TERRESTRE
* Velocidades Mayores 170Km/h: ESTADO VEH. MOTOR AÉREO

El sensor dispondrá de una configuración para delimitar tanto el estado inicial (fijando un determinado
intervalo de tiempo para establecer el valor inicial) como las bandas muertas existentes en los límites
entre los cambios de estado (tanto por arriba como por abajo) ya sea por recogida de valores o por
tiempos. Por ejemplo, para cambiar de estado de CORRIENDO a SPRINT puede configurarse un tipo de banda
muerta de 1500 msg. de valores en el estado de CORRIENDO para cambiar a SPRINT y de 500 msg en estado
de SPRINT para cambiar a CORRIENDO.

## Arquitectura de la solución

Existen numerosos enfoques para abordar el desarrollo de una aplicación en un dispositivo móvil. Para
comenzar, es necesario elegir la plataforma de desarrollo. Actualmente existen iOS, Android y Windows
Phone como principales plataformas de desarrollo, aunque existen alguna más con una horquilla del
mercado extremadamente reducida en comparación con las tres mencionadas, como podría ser BlackBerry.

Debido a la facilidad de acceso a un dispositivo *Android*, así como el aprovechamiento del conocimiento
del lenguaje Java por parte del desarrollador, se ha optado por realizar el desarrollo de la práctica
bajo la tecnología **Android**, de modo que para poder probar la aplicación será necesario disponer
de un terminal con este sistema operativo móvil.

La aplicación ha sido desarrollada según los patrones de desarrollo de *Android*, por el cual las vistas
se encapsulan en clases de tipo **Activity**. Estas *activities* serán las responsables de disparar
la lógica de negocio de la aplicación, así como de responder ante los eventos disponibles en el
terminal, como pueden ser los cambios de orientación, cambios de posición, etc.

Para el caso que nos ocupa, la actividad principal responderá ante los cambios de posición, y en cada
uno de estos cambios, leerá del sensor hardware del dispositivo el valor de la velocidad actual.

Además, la aplicación permitirá definir unos rangos de velocidades, de modo que se pueda identificar
el rango de velocidad en el que se encuentra el sensor del dispositivo, comparando la velocidad actual
con los valores límite establecidos para cada uno de los rangos, determinando de este modo el rango
de velocidad en el que se encuentra el dispositivo.

Estos rangos tendrán unos valores límite, valores mínimo y máximo, que determinen el rango de velocidad,
así como un nombre que lo identifique.

## Diseño del sensor

Para el diseño del sensor se han valorado las siguientes aproximaciones, todas relacionadas con el tipo
de sensor a utilizar para obtener los datos.

La primera es utilizar los valores del sensor que mide los cambios en el acelerómetro. Este sensor,
que en los dispositivos Android se identifica por *Sensor.TYPE_ACCELEROMETER*, obtiene los cambios
producidos sobre el sensor Acelerómetro.

La segunda es utilizar los valores del sensor que mide los cambios en la velocidad linear. Este sensor,
que en los dispositivos Android se identifica por *Sensor.TYPE_LINEAR_ACCELERATION*, obtiene los cambios
producidos sobre el sensor Acelerómetro eliminado la componente de la gravedad.

Por último, se podrían utilizar los valores obtenidos del GPS del dispositivo, facilitados por las
librerías de *Google Play Services*. En estas librerías se tiene acceso a los datos de localización
obtenidos del chip GPS del dispositivo, y en función a la posición actual y anterior, calcular la
velocidad instantánea del mismo, que es un valor leído directamente del GPS del dispositivo.

Si optásemos por la lectura desde los sensores del acelerómetro o de la aceleración lineal, nos obligaría
a constantemente leer del hardware para actualizar los datos de representación en la pantalla
del terminal, lo cual consumiría muchos recursos, sobre todo de batería. En cambio, si utilizásemos
los valores obtenidos desde el GPS, a través de los servicios de *Google Play Services*, reduciríamos
la frecuencia de actualización de esas lecturas, ocurriendo ésto en el evento de cambio de ubicación
del GPS. Es importante destacar que este evento de cambio de la posición ocurre con con mucha menor
frecuencia que los eventos asociados a los sensores físicos comentados con anterioridad.

Por esta razón, la aplicación desarrollada utiliza los servicios de *Google Play Services* para obtener
los datos de posición del dispotivo, obteniendo la velocidad instantánea directamente desde este API.

### Limitaciones del sensor

Es importante conocer que el uso de estos servicios de localización tienen la misma limitación que un
GPS convencional, esto es, el **mal funcionamiento en interiores**, de modo que para disfrutar de la
mejor experiencia de uso de la aplicación es conveniente utilizarla en exteriores, con cobertura GPS.

## Desarrollo

### Lenguaje de programación

El proceso de desarrollo viene definido por el desarrollo de aplicaciones Android, por lo que es
necesario la instalación de un SDK de Android, así como tener la versión adecuada de Java respecto al
SDK anterior.

De este modo, el SDK de Android utilizado es la versión 23.0.3, utilizando Java 8 en su versión 1.8.0_45.

### Sistema de Build

Para la construcción del proyecto, tal y como es habitual en los proyectos *Android*, se ha utilizado
**Gradle**, en su versión 3.3.

#### Alternativas en el sistema de build para aplicaciones de la JVM

El ecosistema JVM se encuentra dominado por tres herramientas de build:

* Apache Ant con Ivy como gestor de dependencias
* Maven
* Gradle

##### Apache Ant + Ivy

`Ant` fue la primera herramienta moderna de build. En muchos aspectos es similar a `Make`. Fue lanzada
en el año 2000, y en un periodo corto de tiempo consiguió ser la herramienta de build más popular para
proyectos Java. Tiene una curva de aprendizaje pequeña, lo que permite a cualquiera comenzar a utilizarla
sin ninguna preparación especial, Está basado en la idea de la programación procedural.

Tras su lanzamiento inicial, fue mejorada con el soporte para añadir plugins.

Por otro lado, su mayor incoveniente siempre ha sido el uso de XML como formato para la escritura de
los scripts de construcción. XML, debido a su naturaleza jerárquica, no es un buen candidato para el
enfoque procedural de programación que `Ant` utiliza. Otro problema importante con `Ant` es que el
XML que utiliza tiende a convertirse en inmanejablemente grande, tanto en proyectos grandes como
pequeños.

##### Maven

`Maven` fue lanzado en 2004. Su objetivo era el mejorar los problemas que los desarrolladores tenían
al usar `Ant`. Continúa utilizando XML como el formato de escritura de los scripts de construcción,
sin embargo es diametralmente diferente a `Ant` en su estructura: mientras `Ant` obliga a los
desarrolladores a escribir todos los comandos que llevan a la ejecución satisfactoria de algunas tareas,
`Maven` se apoya en convenciones y proporciona unos `target` (goals, u objetivos) que pueden ser
invocados. Otras mejoras, y problamente las más importantes, son que `Maven` introdujo la habilidad
de descargar dependencias de la red, que luego `Ant` adoptó mediante el proyecto `Apache Ivy`. Este
nuevo enfoque de gestión de dependencias revolucionó la manera de entregar software.

Sin embargo, `Maven` tiene sus propios problemas. La gestión de dependencias que hace no maneja de
manera correcta los conflictos entre diferentes versiones de la misma librería, algo en lo que `Ivy`
es mucho mejor. Además, XML como formato de configuración es muy estricto en su estructura, así como
muy estandarizado. La personalización de los `targets` es compleja, por ello, al estar `Maven` más
enfocado en la gestión de las dependencias, es más dificil escribir complejos scripts de construcción
personalizados en `Maven` que en `Ant`.

El principal beneficio de utilizar `Maven` es su ciclo de vida. Mientras un proyecto se adhiera a unos
estándares, con `Maven` se puede adaptar el ciclo de vida con cierta facilidad. Como contrapartida,
esta adaptación disminuye la flexibilidad.

##### Gradle

Hoy día existe cierto interés creciente en los DSLs (Domain Specific Languages), en los que la idea
es disponer de lenguajes diseñados específicamente para solucionar problemas de cierto dominio. Aplicado
al mundo de los sistema de build, uno de los resultados de aplicar DSL es `Gradle`.

`Gradle` combina las buenas partes de las dos herramientas anteriores y construye sobre ellas utilizando
un DSL, entre otras mejoras. Dispone de la potencia y la flexibilidad de `Ant`, así como la facilidad
a la hora de definir un ciclo de vida de `Maven`. El resultado final es una herramienta que fue
lanzada en 2012, y obtuve mucha atención en muy poco tiempo. Por ejemplo, Google adoptó `Gradle` como
herramienta de build por defecto para el sistema operativo Android.

`Gradle` no utiliza XML. En su lugar, tiene su propio DSL basado en `Groovy`, un lenguaje de la JVM.
Como resultado, los scripts de construcción de `Gradle` tienden a ser mucho más pequeños y limpios
que aquéllos escritos con `Ant` o `Maven`. La cantidad de código repetitivo es mucho menor, puesto
que su DSL está diseñado para resolver un problema en concreto: mover el software a través de su ciclo
de vida, desde la compilación, pasando por el análisis estático de código y el testing, hasta el
empaquetado y el despliegue. En sus inicios, `Gradle` utilizaba `Apache Ivy` para la gestión de
dependencias, pero más adelante pasó a utilizar un motor de resolución propio.

Los esfuerzos de `Gradle` se podrían resumir en la frase “la convención es buena, así como la flexibilidad”.

Gradle proporciona:

* una herramienta de construcción de propóstio general y muy flexible, parecido a Apache Ant.
* proporciona frameworks intercambiables, basados en construcción-por-convención, al estilo de Maven.
* soporte de construcción de proyectos multi-proyecto muy potente.
* gestión de dependencias muy potente, basado en Apache Ivy.
* soporte completo para infraestructuras de repositorios Maven o Ivy existentes.
* soporte para gestión de dependencias transitivas, sin la necesidad de repositorios remotos, o ficheros
`pom.xml` o `ivy.xml`.
* tareas Ant y construcciones tratadas como ciudadanos de primera clase.
* scripts de construcción de `Groovy`.
* un modelo de dominio muy rico para describir el sistema de construcción de cada proyecto.

En el caso concreto de Android, para el proyecto no se utiliza nada fuera del estándar, basando el
sistema de construcción en el *default* que ofrece el plugin de Android. Por ello, las tareas de
construcción utilizadas son: `clean`, `assemble`, `test`.

El diagrama de interacción entre las diferentes tareas de Gradle es el siguiente:

![Diagrama de tareas de Gradle](./static/gradle_diagram.png)

En cuanto a la gestión de dependencias, se utilizan tanto los repositorios de `jcenter` como la
instalación local de Maven, ubicada en `$USER_HOME/.m2`.

### Organización del código

Al utilizar Gradle como sistema de build, el proyecto sigue un `layout` específico determinado por la
convención de nombres y directorios propia de Gradle.

Según esta convención, la aplicación está dentro de un directorio `app`, y dentro de este directorio
existirá un `src`, así como algunos ficheros descriptores, como por ejemplo el `build.gradle`. Dentro
de `src` se sigue una estructura igual a la definida por `Maven`:

* `src/main` para el descriptor principal de la aplicación Android, `AndroidManifest.xml`
* `src/main/java` para el código de la aplicación
* `src/main/res` para los recursos estáticos de la aplicación: layouts de Android, Strings, etc.
* `src/main/test` para los tests unitarios
* `src/main/testIntegration` para los tests de integración
* `src/main/androidTest` para los tests de interfaz de usuario de Android

En la siguiente imagen aparecen los elementos antes mencionados:

![Estructura de proyecto en Gradle](./static/gradle_project_layout.png)

Por otro lado, en el desarrollo de la aplicación se ha utilizado una estructura de paquetes adecuada
para realizar la separación lógica entre los diferentes componentes de la misma.

A continuación se enumeran los paquetes de la aplicación, que como hemos mencionado antes, se ubican
bajo el directorio `app/src/main/java` del proyecto.

#### Actividades

Las clases que aquí se encuentran representan la vista de las aplicaciones Android. Se encuentran bajo
el paquete `es.mdelapenya.uned.master.is.ubicomp.sensors.activities`.

Una actividad es una única cosa que un usuario puede hacer. Casi todas las actividades interaccionan
con el usuario, por tanto la clase Activity de Android se responsabiliza de crear una ventan en la
que ubicar la interfaz de usuario de las aplicaciones Android. Para hacer ésto, se dispone del método
`setContentView(View)`. Del mismo modo que a menudo las actividades son presentadas al usuario como
ventanas a tamaño completo, también pueden ser utilizadas de otras maneras: como ventanas flotantes
(a través de un tema de apariencia), o empotradas dentro de otra actividad, utilizando *ActivityGroup*.
Existen dos métodos que casi todas las actividades implementarán:

* **onCreate(Bundle)**: inicializa la actividad, y lo que es más importante, normalmente invocará el método
`setContentView(int)` con un recurso de tipo *layout* definiendo la interfaz de usuario, y utilizando
el método `findViewById(int)` encontrará los widgets de la UI que se necesitarán a la hora de interactuar
con ellos de manera programática.
* **onPause()**: es donde se gestiona el momento en el que el usuario deja de utilizar la actividad, y lo
que es lo más importante, cualquier cambio realizado por el usuario debería ser comiteado en este
momento, normalmente mediante un `ContentProvider` que mantenga el acceso a los datos.

En el caso concreto de la aplicación, existen 4 actividades: BaseGeoLocatedActivity, MainActivity,
RangeListActivity, RangeDetailActivity, que detallaremos a continuación.

##### BaseGeoLocatedActivity

Esta actividad contiene el código responsable de responder a los eventos de localización, manteniendo
en su estado interno la última localización conocida del dispositivo, y que comparará con la actual
para obtener la velocidad instantánea.

Además, inicializa los servicios de *Google Play Services*, así como gestiona de una manera *lazy*
los permisos que la aplicación necesita parara funcionar, como es el acceso a la localización. Por
*lazy* se entiende que los permisos serán solicitados al usuario no en el momento de la instalación
de la aplicación, donde el usuario posiblemente no tenga el conocimiento suficiente para tomar una
decisión acertada sobre la instalación de la misma, sino en el momento del primer uso del permiso.

##### MainActivity

Esta actividad es la actividad principal de la aplicación. Extenderá la actividad anterior para acceder
a las capacidades de localización, y las extenderá para actualizar la propia UI.

Actualizará la interfaz de usuario en cuanto se haya producido un cambio en la localización y la
velocidad haya cambiado. Para ello, la actividad dispone del siguiente método, que determina si tiene
que actualizar la interfaz en los casos en que el estado anterior sea diferente del estado actual:

```java
    private TextView currentSpeed;
    private TextView currentSpeedText;
    private String oldSpeed = "0.00";
    private String oldSpeedText = "";
    ...
    ...
    private void updateUI() {
        float speedKm = SpeedConverter.convertToKmsh(getSpeed());

        String rangeName = getRangeName(speedKm);
        String speedValue = roundSpeed(speedKm);

        if (!UIManager.syncUIRequired(speedValue, oldSpeed, rangeName, oldSpeedText)) {
            return;
        }

        currentSpeed.setText(speedValue);
        currentSpeedText.setText(rangeName);

        oldSpeed = speedValue;
        oldSpeedText = rangeName;
    }
```

Siendo `currentSpeed` y `currentSpeedText` dos componentes gráficos de Android representando etiquetas
de presentación de datos, y `oldSpeed` y `oldSpeedText` el estado interno de la actividad, en el que
cada vez que se detecte un cambio de posición se almacenerán los valores de la velocidad y el nombre
del rango en el que se encuentra.

El método `getRangeName(float)` determina si la velocidad actual se encuentra en alguno de los rangos
existentes en la aplicación. Para ello, se iterará por los rangos, y se comprobará que la velocidad
se encuentra entre los valores mínimo y máximo del rango. Al existir un único rango para cada valor
mínimo o máximo, una velocidad dada sólo podrá estar dentro de un único rango. El método devolverá
el nombre del rango, primero buscando entre los recursos de tipo String de la aplicación, y si no
existiese, directamente el nombre del rango. Si, por otro lado, la velocidad no se encuentra dentro
de ningún rango, devolverá como valor por defecto el valor de la clave *"PARADO"*.

```java
    private String getRangeName(float currentSpeed) {
        for (Range range : ranges) {
            if (range.isInRange(currentSpeed)) {
                int resourceByName = ResourceLocator.getStringResourceByName(this, range.getName());

                if (resourceByName > 0) {
                    return this.getString(resourceByName);
                }

                return range.getName();
            }
        }

        return this.getString(R.string.status_stopped);
    }
```

##### RangeListActivity

Esta actividad, invocada desde la actividad principal desde la barra de navegación, administrará las
operaciones de creación, modificación y borrado de los rangos disponibles en la aplicación.

Presentará una lista con los rangos en la base de datos, donde se presentará un botón de *Nuevo Rango*,
se podrá editar un rango existente simplement pulsando en la fila del rango, o se podrá borrar un
rango, mediante la acción de `swipe`, tanto a izquierdas como a derechas, sobre la fila que representa
a un rango.

##### RangeDetailActivity

Esta actividad, invocada desde la pulsación de un elemento en la fila de rangos, mostrará un formulario
con todos los datos del rango seleccionado: nombre, valor mínimo y valor máximo del rango. Desde esta
actividad se podrán guardar los datos asociados al rango, tanto para un rango nuevo (creación) como
para uno existente (actualización).

En ambos casos existirá un proceso de validación, que comprobará que no existan rangos con valores
mínimos o máximos duplicados, de modo que sólo pueda existir un rango con un valor mínimo o máximo
dado.

```java
    public boolean isValidationSuccess() {
        if ("".equals(range.getName())) {
            return false;
        }

        RangeService rangeService = new RangeService(getContext());

        List<Range> minRanges = rangeService.findBy(
            new CriterionImpl(RangeDBHelper.Range.COLUMN_NAME_MIN, range.getMin()));

        if (checkDuplicates(minRanges)) {
            txtMin.requestFocus();

            return false;
        }

        List<Range> maxRanges = rangeService.findBy(
            new CriterionImpl(RangeDBHelper.Range.COLUMN_NAME_MAX, range.getMax()));

        if (checkDuplicates(maxRanges)) {
            txtMax.requestFocus();

            return false;
        }

        return true;
    }

    private boolean checkDuplicates(List<Range> ranges) {
        if (ranges.size() == 0) {
            return false;
        }

        if (ranges.get(0).getId() == range.getId()) {
            return false;
        }

        return true;
    }
```

#### Persistencia

Las clases que aquí se encuentran representan la capa de persistencia de la aplicación. Se encuentran
bajo el paquete `es.mdelapenya.uned.master.is.ubicomp.sensors.internal.db`.

Las clases que extiendan a la clase `SQLiteOpenHelper` se corresponden con clases de utilidad para
manejar tanto la creación de una base de datos como el versionado de la misma.

Es posible que estas subclases implementen los métodos `onCreate(SQLiteDatabase)` y
`onUpgrade(SQLiteDatabase, int, int)`, y de manera opcional `onOpen(SQLiteDatabase)`. Estas clases se
responsabilizarán de abrir la base de datos en caso de que exista, de crearla si no existe, y de
actualizarla si fuese necesario. Las transacciones están soportadas.

En la aplicación, dispondremos de la clase `RangeDBHelper`, que es una clase con dos responsabilidades
claramente definidas:

* la creación física de la base de datos, utilizando como sistema gestor de bases de datos **SQLite**,
pequeña base de datos en memoria muy utilizada en desarrollos Android.
* la definición de la estructura de las entidades a almacenar en la base de datos, en este caso el
modelo de Rangos.

##### Operaciones CRUD

Por otro lado, la clase `RangeDAO`, que sigue el patrón DAO (*Data Access Object*), y representa las
operaciones de manipulación de datos al nivel de la base de datos. Estas operaciones serán, además de
las básicas de CRUD (alta, baja, y modificación), la de búsqueda. Esta clase implementa la interfaz
`Closeable`, de modo que existirá un método `close()` que se invocará de manera automática en los
bloques *finally*, de modo que se permitan cerrar los recursos sin tener que repetir el código de
cerrado cada vez que se utilice. En este caso de clase de acceso a datos, se cerrarán las conexiones
realizadas a la base de datos.

```java
    @Override
    public void close() {
        dbHelper.close();
    }
```

##### Búsquedas

Para implementar las búsquedas, se ha definido una interfaz `Criterion`, que siguiendo el patrón de
desarrollo `Strategy`, permite al DAO definir una búsqueda u otra en función del criterio de utilizado.
Este criterio definirá como estado interno un campo de búsqueda y el valor asociado a ese campo. Se
ofrece una implementación por defecto de la interfaz, de modo que se puedan crear criterios y buscar
por ellos.

Esta capa de persistencia nunca deberá ser invocada directamente, puesto que será preferible abstraerla
mediante la capa de servicios, descrita a continuación. De esta manera los consumidores de los datos
no necesitan conocer la implementación de la base de datos, sino que simplemente llaman a un servicio
para obtenerlos.

#### Servicios

Las clases que aquí se encuentran representan la capa de servicios de la aplicación. La interfaz del
servicio se encuentra bajo el paquete `es.mdelapenya.uned.master.is.ubicomp.sensors.services`, y la
implementación por defecto se encuentra en el paquete
`es.mdelapenya.uned.master.is.ubicomp.sensors.internal.services`.

El único servicio que ofrece la aplicación, `CRUDService` es un servicio para encapsular las operaciones
de acceso a datos, y en su interfaz define dichas operaciones.

```java
    public interface CRUDService<T> {

        T add(T t);

        void delete(T t);

        List<T> findBy(Criterion criterion);

        T get(long id);

        List<T> list();

        T update(T t);

    }
```

Se ha desarrollado con *Generics* de Java, lo cual permite reutilizar la interfaz con cualquier tipo
de modelo.

La implementación, `RangeService`, invoca a la capa de persistencia en cada uno de sus métodos, de
modo que abstraiga la implementación de la base de datos, invocando siempre al servicio en su lugar.

#### Modelos

Las clases que aquí se encuentran representan el modelo de la aplicación. Se encuentran bajo el paquete
`es.mdelapenya.uned.master.is.ubicomp.sensors.model`.

En este caso, la aplicación contiene únicamente un solo modelo, definido en la clase `Range`, que no
es más que un POJO (*Plain Old Java Object*) con los campos necesarios para formar un Rango. Estos
campos son:

* rangeId: identificador único del rango, obtenido de persistir el modelo en la base de datos.
* name: identifica en lenguaje natural el rango, pudiendo permitir duplicados.
* min: valor mínimo del rango. Debe ser **único** entre todos los rangos.
* max: valor máximo del rango. Debe ser **único** entre todos los rangos.

#### Comunicación con la plataforma IoT

Para la implementación de la comunicación con la plataforma IoT, se ha utilizado un modelo de programación
asíncrona, de modo que no se bloquea el hilo principal de ejecución de Android. Para ello, el código
se apoya en dos librerías de utilidad.

Por un lado se utiliza [Retrofit](http://square.github.io/retrofit/) se utiliza para el envío de métricas
a la plataforma IoT. Retrofit permite mapear un API HTTP a interfaces Java. Y por otro, para el paso
de mensajes de manera asíncrona, se utiliza [Otto](http://square.github.io/otto/), que es un bus de
eventos para desacoplar diferentes partes de la aplicación permitiendo al mismo tiempo que se comuniquen
entre ellos de manera eficiente.

En cuanto al código Java de implementación, se ha seguido un patrón `Interactor`, que es el responsable
de implementar el patrón MVC (*Model-View-Controller*) en Android. El `Interactor` enviará datos desde
la aplicación al servicio remoto.

El interactor, que en su estado interno dispone de una métrica, utilizará `Retrofit` para invocar el
servicio y realizar una petición `POST`, con los datos de dicha métrica.

```java
    private static final String BACKEND_ENDPOINT = "http://api.mdelapenya-sensors.wedeploy.io";

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
```

Para la comunicación en segundo plano, se ha implementado un Bus de mensajes en la clase `AndroidBus`,
basada en la librería `Otto`.

#### Clases de Utilidad

Con el mero fin de encapsular el código, bajo el paquete `es.mdelapenya.uned.master.is.ubicomp.sensors.util`
se han creado varias clases de utilidad, que definen ciertas operacion de manera aislada.

Estas clases son:

* ResourceLocator, que permite la obtención de recursos de Android, ya sean de tipo String, o imágenes.
* SpeedConverter, que convierte una velocidad en metros por segundo a kilómetros por hora.
* UIManager, que determina si la interfaz de usuario (UI) debe ser actualizada en función de los
parámetros de entrada. Estos parámetros se corresponden con los valores anteriores y actualies de
velocidad.

El motivo principal de esta encapsulación es el de poder probar la funcionalidad definida en estas
clases, de modo que se puedan escribir *tests unitarios* de las mismas.

#### Eventos

Para obtener la velocidad desde el GPS del dispositivo tendremos que suscribirnos a determinados
eventos, de modo que cuando sean disparados, podemos realizar alguna acción.

Para el caso que nos ocupa, la actividad **MainActivity** deberá suscribirse al evento de cambio de
ubicación del GPS, tal y como se observa en el siguiente bloque de código:

```java
    @Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);

        updateUI();
    }
```

Tal y como comentamos al detallar las actividades de la aplicación, la aplicación dispone de una clase
base **BaseGeoLocatedActivity**. Esta clase es la responsable de encapsular el código necesario para
acceder a los servicios de *Google Play Services*, y con ellos acceder a los servicios de localización.
Cada vez que cambie la localización, se ejecutará su método `onLocationChanged(Location)`, que calculará
la velocidad instantánea a partir de un cálculo con las coordenadas de la localización actual y la
anterior conocida, y el tiempo transcurrido entre ambas mediciones.

Una vez se haya actualizado la localización actual, se enviará una métrica a la plataforma IoT.

```java
    @Override
    public void onLocationChanged(Location location) {
        Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(
            googleApiClient);

        double speed = 0;

        if (lastLocation != null) {
            speed = Math.sqrt(
                Math.pow(
                    currentLocation.getLongitude() - lastLocation.getLongitude(), 2) +
                    Math.pow(currentLocation.getLatitude() - lastLocation.getLatitude(), 2)
                ) / (currentLocation.getTime() - lastLocation.getTime());

            if (currentLocation.hasSpeed()) {
                speed = currentLocation.getSpeed();
            }

            this.speed = new Float(speed);

            lastLocation = currentLocation;

            Metric metric = new SensorMetric(
                uniqueDeviceId, "sensors-android", currentLocation.getLatitude(),
                currentLocation.getLongitude(), speed, "speed", "km/h", new Date().getTime());

            SensorsInteractor sensorsInteractor = new SensorsMetricInteractor(metric);

            new Thread(sensorsInteractor).start();
        }
    }
```

Como puede observarse, el envío de la métrica a la plataforma se realiza en otro hilo de ejecución.
Previamente, se ha populado la métrica con los datos de interés:

* el identificador del dispositivo, `uniqueDeviceId`,
* el ID de la aplicación, `sensors-android`,
* las coordenadas en formato latitud y longitud,
* el valor de la métrica, almacenado en la variable `speed`,
* el tipo de métrica, en formato texto, `"speed"`,
* las unidades de la métrica, en formato texto, `"km/h"`, y
* un timestamp de la petición

Además, se ha creado un objeto de tipo `Interactor` con dicha métrica para ser utilizado en segundo
plano.

## Pruebas

Un requisito imprescindible en toda aplicación es la escritura de buenos tests. Se consideran como
buenas prácticas de escritura de tests las siguientes:

* Un buen test tiene una única razón para fallar.
* Un buen test verifica una única cosa del código a probar.
* Un buen test evita la lógica condicional.
* Un buen test tiene un nombre que describe de manera clara el caso de prueba. Por contra, no se puede
confiar en un test cuya implentación no tiene nada que ver con el nombre del método de test.
* Un buen test crea/abre los recursos que necesita antes de su ejecución, y los cierra o borra al final
de su ejecución.
* Un buen test no define de manera explícita sus dependencias (*hardcoded dependencies*).
* Un test que falla de manera constante es inútil.
* Un test que no puede fallar no aporta valor, pues crea una falsa sensación de seguridad.

### Tests unitarios

Por tanto, y siguiendo las prácticas anteriores, para aquellas funcionalidades sin dependencias
externas se han escritos pruebas unitarias. Estos tests unitarios utilizan *jUnit* como framework de
escritura y ejecución de tests.

Para ejecutar los tests unitarios bastará con ejecutar desde el directorio raíz el comando:

```shell
    ./gradlew test
```

Cuyo resultado será el siguiente:

```shell
    es.mdelapenya.uned.master.is.ubicomp.sensors.model.RangeTest > testCompareThisGreaterThanThat PASSED
    es.mdelapenya.uned.master.is.ubicomp.sensors.model.RangeTest > testCompareThisLowerThanThat PASSED
    es.mdelapenya.uned.master.is.ubicomp.sensors.model.RangeTest > testCompareThisEqualsToThat PASSED
    es.mdelapenya.uned.master.is.ubicomp.sensors.model.RangeTest > testIsInRange PASSED
    es.mdelapenya.uned.master.is.ubicomp.sensors.model.RangeTest > testDefaultConstructor PASSED
    es.mdelapenya.uned.master.is.ubicomp.sensors.util.SpeedConverterTest > testConvert2 PASSED
    es.mdelapenya.uned.master.is.ubicomp.sensors.util.SpeedConverterTest > testConvert PASSED
    es.mdelapenya.uned.master.is.ubicomp.sensors.util.UIManagerTest > testSyncUIRequiredWithDifferentIds PASSED
    es.mdelapenya.uned.master.is.ubicomp.sensors.util.UIManagerTest > testSyncUIRequired PASSED
    es.mdelapenya.uned.master.is.ubicomp.sensors.util.UIManagerTest > testSyncUIRequiredWithDifferentSpeed PASSED
```

#### Tests sobre el modelo

La clase `RangeTest` recoge los tests unitarios sobre las operaciones con lógica de negocio de la clase
`Range`. En particular los dos métodos en los que podrían producirse bugs o errores, puesto que los
demás métodos de la clase son `getters` o `setters` de los atributos, siendo éstos populados en los
constructores, evitando así que obtuvieran valores nulos.

Para el método `compareTo`, resultado de implementar la clase `Comparable` de Java, se verifica que
para todos los casos posibles se verifica que un objeto Range es mayor, menor o igual que otro. Este
método `compareTo` es internamente llamado por `Collections.sort(list)` para realizar ordenaciones
sobre una lista, en este caso de objetos `Range`.

Para el método `isInRange`, se verifica que dada una velocidad en kilómetros, ésta está dentro o fuera
(tanto por encima como por debajo) del rango definidido por las velocidades mínima y máxima que lo
delimitan.

Por último, al existir un constructor por defecto, se verifica que éste popula los atributos con los
valores por defecto.

#### Tests sobre las utilidades

La clase `SpeedConverterTest` recoge los tests unitarios sobre las operaciones con lógica de negocio
de la clase `SpeedConverter`. En particular del único método de la clase, `convertToKmsh`, que dada
una velocidad en metros por segundo, obtiene su equivalente en kilómetros por hora. De este modo, los
tests verifican que para una velocidad de 1 metro por segundo la equivalencia es de 3.6 kilómetros
por hora, y que para un valor arbitrario como es 13 metros por segundo, la equivalencia es de 46.8
kilómetros por hora. En ambos casos, al tratarse de valores con decimales, el API de `jUnit` obliga
a definir un delta para determinar el margen de error producido por el cálculo de decimales.

La clase `UIManagerTest` recoge los tests unitarios sobre las operaciones con lógica de negocio
de la clase `UIManager`. En particular del único método de la clase, `syncUIRequired`, que dados unos
valores de entrada, actuales y anteriores, para la velocidad y el nombre del rango, determina si es
necesario actualizar la interfaz de usuario o UI. De este modo, los tests verifican que el método
devuelve `true` si y sólo si alguno de los valores actuales es distinto a su contrapartida en los
valores anteriores.

### Tests de integración

Debido a la simplicidad de la aplicación, y de no poseer dependencias externas con las que integrarse,
no se han escrito tests de ingración.

### Exploratory Testing

Se han realizado pruebas exploratorias (o `Exploratory Testing`) sobre la aplicación, de modo que
todos los controles han sido extensamente probados, utilizando diferentes prácticas: valores límite,
valores incorrectos, valores nulos en los campos, etc.

## Manual de Uso

A continuación se presentan las diferentes pantallas de la aplicación, con la información necesaria
para utilizar la aplicación con fluídez.

### Pantalla Principal

Al abrir la aplicación se mostrará la actividad principal, que directamente mostrará dos etiquetas,
la superior mostrando la velocidad en kilómetros por hora a la que se encuentra el dispositivo, y
otra etiqueta inmediatamente debajo mostrando el nombre del rango de velocidad en el que se encuentra
la velocidad actual.

![MainActivity](./static/screenshot_main.png)

### Pantalla de Administración de Rangos

Pulsando en la barra de navegación de la aplicación, arriba a la izquierda, aparecerá el icono del
menú, representado por tres puntos verticales. Una vez pulsado, desplegará las opciones del menú, que
para la aplicación corresponden con la gestión de los rangos del sensor, así como una opción mostrando
una ayuda de la aplicación.

![Configuración desde MainActivity](./static/screenshot_main_config.png)

### Pantalla de Lista de rangos

Al pulsar en la opción "Rangos del Sensor" del menú, se mostrará la pantalla de gestión de rangos,
formada por una lista con los rangos disponibles en la actualidad, así como una opción para añadir
nuevos rangos. Esta opción de "Nuevo Rango" se encuentra ubicado abajo a la derecha, siguiendo los
patrones de diseño de Google basados en `Material Design`. Pulsando sobre esta opción se accederá a
la pantalla de creación de rango, que detallaremos más adelante.

Pulsando sobre un elemento de la lista se accederá a la pantalla de edición de rango, que detallaremos
más adelante.

Para volver a la pantalla principal, en la barra de navegación aparece una flecha apuntando a la
izquierda, representando la vuelta a la pantalla anterior del flujo.

![Lista de Rangos](./static/screenshot_range_list.png)

### Pantalla de Nuevo Rango

Para crear un nuevo Rango podremos introducir tres valores: nombre del rango, valor mínimo del rango,
en kilómetros por hora, y valor máximo del rango, también en kilómetros por hora.

La aplicación validará que no existe otro rango con los valores mínimo o máximo iguales a los
introducidos, por tanto no permitirá el solapamiento entre rangos, y una velocidad podrá pertenecer
a un único rango.

![Nuevo Rango](./static/screenshot_new_range.png)

### Pantalla de Edición de Rango

Para modificar un Rango podremos modificar alguno de los tres valores: nombre del rango, valor mínimo
del rango, en kilómetros por hora, y valor máximo del rango, también en kilómetros por hora.

Del mismo modo que en la pantalla de creación de rangos, la aplicación validará que no existe otro
rango con los valores mínimo o máximo iguales a los introducidos, a excepción de tratarse del propio
rango, por tanto no permitirá el solapamiento entre rangos, y una velocidad podrá pertenecer a un único
rango.

![Modificación de Rango](./static/screenshot_edit_range.png)

## Instalación de la aplicación

Para instalar la aplicación en un dispositivo Android, al no estar publicada en `Google Play` como app,
será necesario construirla desde los fuentes. Para ello bastará con realizar los siguientes pasos:

1) Bajar el código, en un directorio con permisos de escritura, teniendo `git` instalado:

```shell
    git clone https://github.com/mdelapenya/uned-sensors.git
```

2) Tener instalado Java:

```shell
    java --version
```

3) Ejecutar el proceso de construcción:

```shell
    ./gradlew assemble
```

4) Copiar el empaquetado al terminal, mediante terminal con `adb` o usando un interfaz gráfico.

```shell
    adb push ./app/build/outputs/apk/app-debug.apk /sdcard
```

Donde `/sdcard` representa el almacenamiento interno del dispositivo Android. Para instalarlo será
necesario habilitar la instalación de aplicaciones de fuentes desconocidas.

## Integración con otros sistemas

En el momento que estamos leyendo los datos del sensor GPS del dispositivo, podríamos en ese punto
crear cualquier tipo de comunicación con un sistema de terceros para enviar dichos datos, mediante
invocaciones a un servicio web, por ejemplo. Dicho servicio web podría recoger los datos de velocidad
instantánea, datos de ubicación, identificador del dispositivo, entre otros, para poder clasificar
 y/o agrupar los datos en otras operaciones de explotación de datos.

## Recursos