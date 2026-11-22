## ALBARRÁN JARA CARLOS FERNANDO - (TRABAJO AUTOMATIZACION APIS) 

Proyecto de automatizacion de pruebas para la API de productos.
Usando Cucumber y  el patron Screenplay.

API USADA: `https://demo.totalshiftleft.ai/api/v1/`

PAG WEB DE LA API: https://demo.totalshiftleft.ai/ui#rest

Swagger: https://demo.totalshiftleft.ai/docs/static/index.html

## Que necesitamos?

- Java 17
- Maven
- IntelliJ (de preferencia)

## Como correr las pruebas

Desde la terminal, en la carpeta del proyecto:

```
mvn clean verify
```
O en el mismo IntelliJ, en el icono de maven ejecutamos
```
mvn serenity::aggregate
```
Tambien se puede correr el `ProductRunnerTest` desde IntelliJ.

Para correr solo un escenario, se usa su tag:

```
mvn clean verify -Dcucumber.filter.tags="@CP01_Product"
```
O tambien en el "ProductRunnerTest"
```
En el tags = "@CP05_Product"
```
Colacamos en proceso que deseamos ejecutar a eleccion

Al terminar, podremos encontrar el reporte en la carpeta: `target/site/serenity/index.html`.

## Que Probamos - Nuestros Casos

| Tag | Escenario |
|---|---|
| @CP01_Product | Crear un producto (POST) |
| @CP02_Product | Listar productos con paginacion (GET) |
| @CP03_Product | Consultar un producto por id (GET) |
| @CP04_Product | Actualizar precio y stock (PATCH) |
| @CP05_Product | Reemplazar un producto (PUT) |
| @CP06_Product | Eliminar un producto (DELETE) |
| @CP07_Product | Eliminar un producto que no existe (404) |
| @CP08_Product | Crear un producto con datos invalidos (400) |

Los Escenarios negativos tienen el tag `@Negativo`.

## TENER EN CUENTA

Para el PATCH, PUT, DELETE y consulta por id la primera ejecucion de todos los test dará con normalidad, luego puede marcar error al eliminar, ya que se eliminó el producto con el id que estaba y se necesite modificar
en producto ya que esta por defecto un id que se reestablece cada 30 min de inacctividad "cccccccc-cccc-cccc-cccc-cccccccccccc" , en caso marque error consultar los productos existentes y poner el id de un producto existente

Para el PATCH, PUT, DELETE y consulta por id, el id del `.feature` tiene que ser de un ID del producto que exista. Si no, hay que crear uno y cambiar el id 
O consultar en la pag web con un ID ya existente y probrar los diferentes casos.

