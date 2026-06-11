# PF_GRUPO04- Sistema de Gestión para Tienda Online E-Commerce

Este proyecto final ha sido desarrollado para el curso **Algoritmos y Estructuras de Datos** en la **Universidad Tecnológica del Perú (UTP)**. Consiste en una solución informática integral orientada a optimizar la gestión operativa, logística y de atención al cliente de una tienda online, utilizando programación orientada a objetos (POO) e implementando estructuras de datos avanzadas desde cero.

---

## ─── CARACTERÍSTICAS PRINCIPALES ───

* **Sin Librerías Predefinidas:** Toda la lógica de las estructuras de datos (inserción, eliminación, ordenamiento y búsqueda) ha sido desarrollada de forma manual sin utilizar utilitarios comerciales de Java como `ArrayList`, `LinkedList`, o `Collections.sort()`.
* **Interfaz Gráfica Nativa (GUI):** Construido enteramente utilizando la librería nativa **Java Swing** (`JFrame`, `JTabbedPane`, `JTable`, etc.) para ofrecer una navegación intuitiva organizada por pestañas operativas.
* **Arquitectura Limpia y Modular:** Código fuente completamente desacoplado y estructurado mediante el uso de paquetes organizados según sus responsabilidades (`modelo`, `estructuras`, `gui`).

---

## ─── FLUJO DEL NEGOCIO Y ESTRUCTURAS UTILIZADAS ───

El sistema simula los procesos críticos de un comercio electrónico real a través de 5 módulos específicos accesibles desde el formulario principal:

### 1. Catálogo Global de Productos (Arreglo de Objetos)
* **Contexto:** Panel administrativo para dar de alta y gestionar los productos comerciales que la tienda ofrece al mercado.
* **Estructura:** Arreglo estático de objetos (`Producto[]`).
* **Operaciones:** Inserción de nuevos productos, eliminación lógica con reordenamiento de índices, actualización de datos y listado completo.

### 2. Carrito de Compras Dinámico (Lista Enlazada Simple)
* **Contexto:** Espacio del cliente donde añade de forma dinámica los productos y cantidades que planea adquirir antes de proceder al pago.
* **Estructura:** Lista enlazada simple lineal basada en punteros a nodos (`NodoLista`).
* **Operaciones:** Inserción al final, eliminación de un producto específico, modificación de cantidades y cálculo del **Importe Total** acumulado de la compra (cumpliendo con el requerimiento matemático del proyecto).

### 3. Despacho y Envíos (Cola con Prioridades)
* **Contexto:** Cola de salida logística donde los pedidos pagados esperan ser empaquetados y distribuidos.
* **Estructura:** Cola con prioridad basada en nodos (`NodoCola`).
* **Operaciones:** `encolar()` insertando los pedidos de forma ordenada según el tipo de envío (por ejemplo, *Envío Express* toma prioridad frente a *Envío Regular*) y `desencolar()` para procesar la salida del próximo paquete listo.

### 4. Gestión de Devoluciones (Pila LIFO)
* **Contexto:** Módulo de logística inversa encargado de recibir los productos devueltos por fallas o insatisfacción para su posterior revisión técnica.
* **Estructura:** Pila dinámica lineal (`NodoPila`).
* **Operaciones:** `apilar() (push)` para registrar el ingreso de la devolución al almacén y `desapilar() (pop)` para procesar y evaluar el último artículo retornado que ha ingresado (Principio Last In, First Out).

### 5. Indexación y Búsqueda Alfabética (Árbol Binario de Búsqueda - ABB)
* **Contexto:** Motor de búsqueda rápida para que los clientes localicen productos en el catálogo mediante filtros escritos.
* **Estructura:** Árbol Binario de Búsqueda (`NodoArbol`).
* **Operaciones:** Inserción indexada y métodos de búsqueda secuencial/indexada aplicados estrictamente mediante **recursividad**, realizando las comparaciones lógicas de los nodos a través de cadenas de texto (`String.compareTo()`).

---

## ─── ESTRUCTURA DEL PROYECTO ───

El espacio de trabajo en el repositorio mantiene una estructura de paquetes limpia para cumplir con los estándares de evaluación de la universidad:

```text
src/
├── modelo/       # Clases de entidad (Producto.java) y los modelos de Nodos contenedores.
├── estructuras/  # Clases lógicas encargadas del comportamiento y algoritmos de cada estructura.
└── gui/          # Clases visuales y formularios diseñados con componentes de Java Swing.