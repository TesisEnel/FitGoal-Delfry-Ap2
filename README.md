
<div align="center">

# 💪 **FitGoal** 🎯  
¡Tu compañero definitivo para alcanzar tus metas de fitness y bienestar! 💪💧✨

<p align="center">
  <img src="https://github.com/Elpelfry/FitGoal/blob/master/app/src/main/ic_launcher-playstore.png" width="200" height="200" alt="FitGoal Logo">
</p>

</div>

---

<div align="center">

## ✨ **Descripción**

</div>

**FitGoal** es una aplicación móvil diseñada para ayudarte a mantenerte en forma y saludable. Ofrece herramientas que facilitan el seguimiento de tus objetivos de fitness, el consumo de agua y mucho más. Todo esto mientras aprovecha lo último en tecnología para una experiencia de usuario fluida y moderna.

---

<div align="center">

## 🌟 **Características Destacadas**

</div>

- **🏋️ Gestión de Ejercicios:** Planifica y organiza tus rutinas con detalles como repeticiones, series y grupos musculares.
- **💧 Seguimiento de Hidratación:** Recibe recordatorios y lleva un registro de tu consumo diario de agua.
- **🔒 Autenticación Segura:** Utiliza Firebase Authentication para inicios de sesión rápidos y seguros.
- **📅 Planificación:** Herramientas de planificación de actividades personalizadas.
- **📲 Notificaciones Inteligentes:** Recibe recordatorios para mantenerte en el camino correcto.
- **🎨 Diseño Moderno:** Interfaz atractiva construida con Jetpack Compose.
- **🌐 Conectividad Avanzada:** Integración de APIs para sincronización de datos en tiempo real.

---

<div align="center">

## 🛠️ **Tecnologías Utilizadas**

</div>

- 🏗️ **Room:** Gestión de datos locales con base de datos SQLite.
- 🛡️ **Hilt:** Inyección de dependencias para simplificar el desarrollo.
- 🌐 **Retrofit:** Consumo de APIs RESTful para sincronización remota.
- 🎨 **Jetpack Compose:** Diseño moderno de interfaces de usuario.
- 🔥 **Firebase Authentication:** Autenticación de usuarios segura y confiable.
- 🕰️ **WorkManager:** Programación de tareas en segundo plano.
- 📅 **Calendarios Personalizados:** Seguimiento y planificación de objetivos.
- 📊 **Firebase Analytics:** Para entender cómo los usuarios interactúan con la aplicación.

---

<div align="center">

## 📂 **Estructura del Proyecto**

</div>

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

<div align="center">

## 📸 **Capturas de Pantalla**

</div>

---

<div align="center">

### Pantallas de Inicio de Sesión y Registro
<div align="center">
<table>
  <tr>
    <td align="center">
      <img src="https://github.com/stvnyc/Fotos_FitGoal/blob/main/capturas/1.inicio_sesion.png" width="200"><br>
      <b>Pantalla de Inicio de Sesión</b><br>
      Los usuarios pueden iniciar sesión utilizando su correo electrónico y contraseña.
    </td>
    <td align="center">
      <img src="https://github.com/stvnyc/Fotos_FitGoal/blob/main/capturas/2.registro_usuario.png" width="200"><br>
      <b>Pantalla de Registro</b><br>
      Los usuarios nuevos pueden registrarse proporcionando su nombre, apellidos, correo y contraseña.
    </td>
  </tr>
</table>
</div>

---

### Inicio de Sesión con Google
<div align="center">
<table>
  <tr>
    <td align="center">
      <img src="https://github.com/stvnyc/Fotos_FitGoal/blob/main/capturas/3.inicio_sesion_google.png" width="200"><br>
      <b>Inicio de Sesión con Google</b><br>
      Inicio rápido y seguro a través de la autenticación con Google.
    </td>
    <td align="center">
      <img src="https://github.com/stvnyc/Fotos_FitGoal/blob/main/capturas/0.formulario_inicial.png" width="200"><br>
      <b>Registro Inicial</b><br>
      Registro de datos para nuevos usuarios.
    </td>
  </tr>
</table>
</div>

---

### Pantalla Home y Nueva Meta
<div align="center">
<table>
  <tr>
    <td align="center">
      <img src="https://github.com/stvnyc/Fotos_FitGoal/blob/main/capturas/4.home.png" width="200"><br>
      <b>Pantalla Home</b><br>
      Consulta estadísticas y metas de manera general.
    </td>
    <td align="center">
      <img src="https://github.com/stvnyc/Fotos_FitGoal/blob/main/capturas/12.nueva_meta.png" width="200"><br>
      <b>Nueva Meta</b><br>
      Crea metas personalizadas y sigue tu progreso.
    </td>
  </tr>
</table>
</div>

---

### Perfil y edición
<div align="center">
<table>
  <tr>
    <td align="center">
      <img src="https://github.com/stvnyc/Fotos_FitGoal/blob/main/capturas/10.perfil.png" width="200"><br>
      <b>Pantalla perfil</b><br>
      Muestra información del usuario.
    </td>
    <td align="center">
      <img src="https://github.com/stvnyc/Fotos_FitGoal/blob/main/capturas/11.editar_perfil.png" width="200"><br>
      <b>Nueva Meta</b><br>
      Modifica edad y altura del usuario.
    </td>
  </tr>
</table>
</div>

---

### Gestión de Ejercicios
<div align="center">
<table>
  <tr>
    <td align="center">
      <img src="https://github.com/stvnyc/Fotos_FitGoal/blob/main/capturas/5.ejercicios.png" width="200"><br>
      <b>Lista de Ejercicios</b><br>
      Visualiza ejercicios disponibles y selecciona los más adecuados.
    </td>
    <td align="center">
      <img src="https://github.com/stvnyc/Fotos_FitGoal/blob/main/capturas/6.descripcion_ejercicio.png" width="200"><br>
      <b>Descripción de Ejercicios</b><br>
      Detalles paso a paso para realizar cada ejercicio correctamente.
    </td>
  </tr>
</table>
</div>

---

### Gestión de Hidratación
<div align="center">
<table>
  <tr>
    <td align="center">
      <img src="https://github.com/stvnyc/Fotos_FitGoal/blob/main/capturas/7.registro_agua.png" width="200"><br>
      <b>Registro de Hidratación</b><br>
      Lleva un control diario del agua consumida.
    </td>
    <td align="center">
      <img src="https://github.com/stvnyc/Fotos_FitGoal/blob/main/capturas/8.agua_diaria.png" width="200"><br>
      <b>Consumo Diario</b><br>
      Visualiza y ajusta tus metas de hidratación diaria.
    </td>
    <td align="center">
      <img src="https://github.com/stvnyc/Fotos_FitGoal/blob/main/capturas/9.agua_notificacion.png" width="200"><br>
      <b>Notificación de Hidratación</b><br>
      Recibe recordatorios para mantenerte hidratado.
    </td>
  </tr>
</table>
</div>

---

### Rutinas y Ejercicios
<div align="center">
<table>
  <tr>
    <td align="center">
      <img src="https://github.com/stvnyc/Fotos_FitGoal/blob/main/capturas/13.creacion_rutina.png" width="200"><br>
      <b>Creación de Rutinas</b><br>
      Diseña tus propias rutinas personalizadas.
    </td>
    <td align="center">
      <img src="https://github.com/stvnyc/Fotos_FitGoal/blob/main/capturas/14.configurar_ejercicio.png" width="200"><br>
      <b>Configurar Ejercicios</b><br>
      Ajusta series y repeticiones para cada actividad.
    </td>
    <td align="center">
      <img src="https://github.com/stvnyc/Fotos_FitGoal/blob/main/capturas/15.partes_cuerpo.png" width="200"><br>
      <b>Partes del cuerpo</b><br>
      Lista partes del cuerpo a las que se enfoca el ejercicio.
    </td>
  </tr>
</table>
</div>

---

### Cronómetro y Calendario
<div align="center">
<table>
  <tr>
    <td align="center">
      <img src="https://github.com/stvnyc/Fotos_FitGoal/blob/main/capturas/17.cronometro_plantilla.png" width="200"><br>
      <b>Cronómetro</b><br>
      Temporiza tus rutinas y monitorea la duración.
    </td>
    <td align="center">
      <img src="https://github.com/stvnyc/Fotos_FitGoal/blob/main/capturas/18.calendario.png" width="200"><br>
      <b>Calendario</b><br>
      Programa tus actividades diarias y planifica tu progreso.
    </td>
  </tr>
</table>
</div>

---

<div align="center">

## ⚙️ **Desarrollado con Amor por:**
<p align="center" style="font-size: 1.5em;">
  <b>Delfry Paulino</b> 💻 <br>
  <b>Yudelka Guillen</b> 🎨 <br>
  <b>Steven Candelario</b> 🔧 <br>
</p> 

<p>✨ ¡Gracias a nuestro querido profesor Enel Almonte y a todos nuestros compañeros por todas las experiencias compartidas! ✨</p>
<p>❤️ ¡Los llevaremos con nosotros siempre! ❤️</p>

</div>
