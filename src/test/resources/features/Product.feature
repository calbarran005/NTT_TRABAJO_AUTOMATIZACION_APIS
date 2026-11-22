@Product
Feature: Apis de Productos
  Como usuario de la tienda
  Quiero gestionar los productos
  para poder crear, consultar, actualizar y eliminar registros

  Background:
    Given el actor establece el endpoint de productos

  #Crear Producto
  @CP01_Product @Product
  Scenario: Crear un producto exitosamente
    When el actor crea un producto con "PC GAMER" "8700" "PC AMD - DE ALTO RENDIMIENTO" "12" "electronics"
    Then el codigo de respuesta debe ser 201
    And la respuesta debe ser exitosa
    And el producto debe tener un id generado
    And el producto debe tener "PC GAMER" "8700" "PC AMD - DE ALTO RENDIMIENTO" "12" "electronics"

  #Listar todos los productos
  @CP02_Product @Product
  Scenario: Listar todos los productos exitosamente
    When el actor lista los productos con page "1" y limit "100"
    Then el codigo de respuesta debe ser 200
    And la respuesta debe ser exitosa
    And la respuesta debe contener una lista de productos con paginacion

  #Obtener un producto por ID
  @CP03_Product @Product
  Scenario: Obtener un producto por id exitosamente
    When el actor consulta el producto con id "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"
    Then el codigo de respuesta debe ser 200
    And la respuesta debe ser exitosa
    And el id del producto debe ser "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"
    And el producto debe tener todos sus datos

  #PATCH
  @CP04_Product @Product
  Scenario: Actualizar parcialmente un producto exitosamente
    When el actor actualiza el producto con id "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb" con precio "39.99" y stock "60"
    Then el codigo de respuesta debe ser 200
    And la respuesta debe ser exitosa
    And el id del producto debe ser "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"
    And el producto debe tener precio "39.99" y stock "60"

  #PUT
  @CP05_Product @Product
  Scenario: Reemplazar un producto exitosamente
    When el actor reemplaza el producto con id "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb" con "Teclado Mecánico con Switches rojos" "49.90" "RGB hot-swappable mechanical keyboard" "65" "electronics"
    Then el codigo de respuesta debe ser 200
    And la respuesta debe ser exitosa
    And el id del producto debe ser "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"
    And el producto debe tener "Teclado Mecánico con Switches rojos" "49.90" "RGB hot-swappable mechanical keyboard" "65" "electronics"

  #DELETE
  @CP06_Product @Product
  Scenario: Eliminar un producto exitosamente
    When el actor elimina el producto con id "cccccccc-cccc-cccc-cccc-cccccccccccc"
    Then el codigo de respuesta debe ser 204
    And la respuesta no debe tener contenido
    And el producto con id "cccccccc-cccc-cccc-cccc-cccccccccccc" ya no debe existir

  #ESCENARIOS NEGATIVOS
  #DELETE de un producto que no exista
  @CP07_Product @Product @Negativo
  Scenario: Eliminar un producto que no existe
    When el actor elimina el producto con id "00000000-0000-0000-0000-000000000000"
    Then el codigo de respuesta debe ser 404
    And la respuesta debe ser fallida con el error "NOT_FOUND" y el mensaje "product not found"

  #CREAR PRODUCTO con datos invalidos
  @CP08_Product @Product @Negativo
  Scenario Outline: Crear un producto con datos invalidos
    When el actor crea un producto con "<name>" "<price>" "<description>" "<stock>" "<category>"
    Then el codigo de respuesta debe ser 400
    And la respuesta debe ser fallida con el error "VALIDATION_ERROR" y el mensaje "<message>"
    And el detalle del error debe indicar el campo "<field>" con el mensaje "<detail>"

    Examples:
      | name       | price  | description     | stock | category    | field | detail                                | message                                                       |
      |            | 999.99 | High-end laptop | 50    | electronics | name  | must NOT have fewer than 1 characters | Validation failed: name must NOT have fewer than 1 characters |
      | Laptop Pro | -5     | High-end laptop | 50    | electronics | price | must be > 0                           | Validation failed: price must be > 0                          |
      | Laptop Pro | 999.99 | High-end laptop | -1    | electronics | stock | must be >= 0                          | Validation failed: stock must be >= 0                         |
