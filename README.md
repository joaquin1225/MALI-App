## ▶️ Para ingresar como empleado

### 1. 🔐 Inicia sesión
- Ingresa tu nombre de usuario y contraseña asignados al rol `empleado` en PostgreSQL.
- Presiona el botón **"Iniciar sesión"**.

### 2. 🏠 Pantalla principal
- Se abrirá una ventana donde puedes:
  - Ingresar el **tipo** y **número** de identificación del visitante.
  - Consultar las tablas de:
    - `visitante`
    - `boleto`
    - `identificacion`
  - Cerrar sesión para volver al login.

### 3. 🔍 Buscar visitante
- Ingresa el tipo de documento (`DNI`, `Pasaporte`, `Carnet`) y su número.
- El sistema buscará si ya existe una identificación con esos datos:
  - Si el visitante **ya existe**, se abrirá la ventana para **registrar un boleto**.
  - Si el visitante **no existe**, se abrirá el formulario para **registrar sus datos**.

### 4. 🧾 Registrar nuevo visitante
- Llena el formulario con:
  - Nombre
  - Apellido
  - Género
  - País
  - Teléfono
- Al registrar:
  - Se guarda automáticamente la **identificación** ingresada previamente.
  - Luego se abre la ventana para **registrar su boleto**.

### 5. 🎟 Registrar boletos
- Aparecerán los datos del visitante.
- Selecciona:
  - Tipo de boleto (`General`, `Estudiante`, `VIP`)
  - Fecha de visita
- Puedes:
  - Registrar **varios boletos seguidos** para el mismo visitante.
  - Finalizar y regresar a la pantalla inicial.
