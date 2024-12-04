# 💪 **FitGoal** 🎯  
¡Tu compañero definitivo para alcanzar tus metas de fitness y bienestar! 💪💧✨

<p align="center">
  <img src="https://github.com/Elpelfry/FitGoal/blob/master/app/src/main/ic_launcher-playstore.png" width="200" height="200" alt="FitGoal Logo">
</p>

---

## ✨ **Descripción**
**FitGoal** es una aplicación móvil diseñada para ayudarte a mantenerte en forma y saludable. Ofrece herramientas que facilitan el seguimiento de tus objetivos de fitness, el consumo de agua y mucho más. Todo esto mientras aprovecha lo último en tecnología para una experiencia de usuario fluida y moderna.

---

## 🌟 **Características Destacadas**
- **🏋️ Gestión de Ejercicios:** Planifica y organiza tus rutinas con detalles como repeticiones, series y grupos musculares.
- **💧 Seguimiento de Hidratación:** Recibe recordatorios y lleva un registro de tu consumo diario de agua.
- **🔒 Autenticación Segura:** Utiliza Firebase Authentication para inicios de sesión rápidos y seguros.
- **📅 Planificación:** Herramientas de planificación de actividades personalizadas.
- **📲 Notificaciones Inteligentes:** Recibe recordatorios para mantenerte en el camino correcto.
- **🎨 Diseño Moderno:** Interfaz atractiva construida con Jetpack Compose.
- **🌐 Conectividad Avanzada:** Integración de APIs para sincronización de datos en tiempo real.

---

## 🛠️ **Tecnologías Utilizadas**

- 🏗️ **Room:** Gestión de datos locales con base de datos SQLite.
- 🛡️ **Hilt:** Inyección de dependencias para simplificar el desarrollo.
- 🌐 **Retrofit:** Consumo de APIs RESTful para sincronización remota.
- 🎨 **Jetpack Compose:** Diseño moderno de interfaces de usuario.
- 🔥 **Firebase Authentication:** Autenticación de usuarios segura y confiable.
- 🕰️ **WorkManager:** Programación de tareas en segundo plano.
- 📅 **Calendarios Personalizados:** Seguimiento y planificación de objetivos.

---

## 📂 **Estructura del Proyecto**
```plaintext
├── app
│   ├── src
│   │   ├── main
│   │   │   ├── java/edu/ucne/fitgoal
│   │   │   │   ├── data          # Manejo de datos locales y remotos
│   │   │   │   ├── presentation  # UI y navegación
│   │   │   │   ├── util          # Funciones de utilidad compartidas
│   │   │   │   ├── FitGoalApp.kt # Configuración inicial de la app
│   │   │   │   └── MainActivity.kt # Pantalla principal
│   │   │   ├── res               # Recursos como imágenes, colores y cadenas
│   │   │   └── AndroidManifest.xml # Declaraciones de permisos y configuración
│   ├── build.gradle.kts
├── build.gradle.kts
└── settings.gradle.kts
```

---

## 📸 **Capturas de Pantalla**

### Pantallas de Inicio de Sesión y Registro
<table>
  <tr>
    <td align="center">
      <img src="https://github.com/stvnyc/Fotos_FitGoal/blob/main/capturas/1.inicio_sesion.png" width="200"><br>
      <b>Pantalla de Inicio de Sesión</b><br>
      Los usuarios pueden iniciar sesión utilizando su correo electrónico y contraseña, o iniciar sesión con Google.
    </td>
    <td align="center">
      <img src="https://github.com/stvnyc/Fotos_FitGoal/blob/main/capturas/2.registro_usuario.png" width="200"><br>
      <b>Pantalla de Registro de Usuario</b><br>
      Los usuarios nuevos pueden registrarse proporcionando su nombre, apellidos, correo y contraseña.
    </td>
    <td align="center">
      <img src="https://github.com/stvnyc/Fotos_FitGoal/blob/main/capturas/3.inicio_sesion_google.png" width="200"><br>
      <b>Inicio de Sesión con Google</b><br>
      Inicio rápido y seguro a través de la autenticación con Google.
    </td>
  </tr>
</table>

---

### Gestión de Ejercicios
<table>
  <tr>
    <td align="center">
      <img src="https://github.com/stvnyc/Fotos_FitGoal/blob/main/capturas/5.ejercicios.png" width="200"><br>
      <b>Gestión de Ejercicios</b><br>
      Accede a una lista de ejercicios con ilustraciones y descripciones.
    </td>
    <td align="center">
      <img src="https://github.com/stvnyc/Fotos_FitGoal/blob/main/capturas/6.descripcion_ejercicio.png" width="200"><br>
      <b>Descripción de un Ejercicio</b><br>
      Cada ejercicio incluye una guía detallada para realizarlo correctamente.
    </td>
  </tr>
</table>

---

### Gestión de Hidratación
<table>
  <tr>
    <td align="center">
      <img src="https://github.com/stvnyc/Fotos_FitGoal/blob/main/capturas/7.registro_agua.png" width="200"><br>
      <b>Registro de Hidratación</b><br>
      Registra la cantidad de agua consumida y establece recordatorios.
    </td>
    <td align="center">
      <img src="https://github.com/stvnyc/Fotos_FitGoal/blob/main/capturas/8.agua_diaria.png" width="200"><br>
      <b>Resumen de Consumo Diario</b><br>
      Visualiza tus metas diarias de hidratación y ajusta según tus necesidades.
    </td>
    <td align="center">
      <img src="https://github.com/stvnyc/Fotos_FitGoal/blob/main/capturas/9.agua_notificacion.png" width="200"><br>
      <b>Notificación de Hidratación</b><br>
      Recibe notificaciones oportunas para mantenerte hidratado durante el día.
    </td>
  </tr>
</table>
