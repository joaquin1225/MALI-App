# 🖼️ MALIApp - Manual de Inicio

Este proyecto cuenta con **3 roles de usuario** conectados a la base de datos PostgreSQL.  
Cada rol tiene una interfaz y funcionalidades específicas dentro de la aplicación.

---

## ▶️ Para ingresar como **empleado**

### 1. 🔐 Inicia sesión
- Ingresa tu nombre de usuario y contraseña asignados al rol `empleado` en PostgreSQL.
- Presiona el botón **"Iniciar sesión"**.

### 2. 🏠 Pantalla principal
Se abrirá una ventana donde puedes:
- Ingresar el tipo y número de identificación del visitante.
- Consultar las tablas de:
  - `visitante`
  - `boleto`
  - `identificacion`
- Cerrar sesión para volver al login.

### 3. 🔍 Buscar visitante
- Ingresa el tipo de documento (**DNI**, **Pasaporte**, **Carnet**) y su número.
- El sistema buscará si ya existe una identificación con esos datos:
  - Si el visitante **ya existe**, se abrirá la ventana para registrar un boleto.
  - Si el visitante **no existe**, se abrirá el formulario para registrar sus datos.

### 4. 🧾 Registrar nuevo visitante
- Llena el formulario con:
  - Nombre
  - Apellido
  - Género
  - País
  - Teléfono
- Al registrar:
  - Se guarda automáticamente la identificación ingresada previamente.
  - Luego se abre la ventana para registrar su boleto.

### 5. 🎟 Registrar boletos
- Aparecerán los datos del visitante.
- Selecciona:
  - Tipo de boleto
  - Fecha de visita
- Puedes:
  - Registrar varios boletos seguidos para el mismo visitante.
  - Finalizar y regresar a la pantalla inicial.

---

## 🧑‍💼 Para ingresar como **jefe de empleados**

### 1. 🔐 Inicia sesión
- Ingresa tu nombre de usuario y contraseña asignados al rol `jefe_empleado` en PostgreSQL.
- Presiona el botón **"Iniciar sesión"**.

### 2. 🏠 Pantalla principal
Podrás acceder a las siguientes funciones:
- Visualizar la información de los **trabajos registrados**.
- Consultar las tablas de:
  - `sala`
  - `obra_de_arte`
- Iniciar el flujo para agregar nuevos trabajos a:
  - Obras de arte
  - Salas
- Cerrar sesión para volver al login.

### 3. 🛠 Agregar trabajos
- Ingresa:
  - Descripción del trabajo
  - Fecha de inicio y fin
- Elige si deseas asignarlo a una o más:
  - Obras de arte
  - Salas
- Selecciona múltiples elementos desde listas y confirma la asignación.

---

## 🧑‍💻 Para ingresar como **administrador**

### 1. 🔐 Inicia sesión
- Ingresa tu nombre de usuario y contraseña asignados al rol `administrador` en PostgreSQL.
- Presiona el botón **"Iniciar sesión"**.

### 2. 🏠 Pantalla principal
Accederás a una interfaz que permite administrar los datos del arte.

Funciones disponibles:
- Buscar obras de arte por diferentes criterios.
- Añadir nuevas obras.
- Editar o eliminar obras existentes.

### 3. 🖼 Añadir nueva obra de arte
- Elige o crea un artista asociado.
- Puedes seleccionar una colección existente o dejarla sin colección.
- Ingresa los detalles de la obra (nombre, técnica, fecha, etc.).
- La operación se divide en ventanas secuenciales con navegación entre ellas.

### 4. ♻️ Editar o eliminar obra
- Selecciona una obra desde la tabla principal.
- Puedes:
  - Editar sus datos (nombre, colección, técnica, etc.).
  - Eliminarla de forma permanente.
- Luego de la acción, se actualiza automáticamente la vista.

---

🔐 Asegúrate de tener los permisos correctos en la base de datos y que el servidor esté activo para poder conectarte correctamente.
