# README - Aplicación TodoList Mejorada

Aplicación TodoList con base de datos Firebase y autenticación vía Google.

## Descripción del Proyecto

La aplicación TodoList es una herramienta de gestión de tareas que permite a los usuarios crear, editar y completar dichas tareas. Esta versión incluye una pantalla para la recuperación de la contraseña.

## Efecto de hint y borrado de tarea

Se ha agregado un efecto de difuminado a la hora de la eliminación de la tarea, así como el hint en las cajas de entrada de texto.

![Borrado](/imgReadme/borrado.gif)
![Hint](/imgReadme/hint.gif)

### Cambio de Icono de la Aplicación
Se ha reemplazado el icono de la aplicación por uno personalizado.

![Icono de la Aplicación](/imgReadme/appicon.png)

### Pantalla Inicial de Splash con Firebase Authentication
Ahora, la aplicación cuenta con una pantalla inicial de Splash que nos dirige hacia el proceso de login utilizando Firebase Authentication.

![Icono de la Aplicación](/imgReadme/splash.gif)

### Autenticación mediante Google
La aplicación también cuenta con la opción de registrarse mediante una cuenta de Google, simplemente clickando sobre el logo nos llevará a la plataforma de autenticación Google.

![Google](/imgReadme/google.gif)

### Iconos del menú
Se han cambiado los iconos del menú, así como se ha añadido un icono al boton de done, mejorando la usabilidad y estética de éste. 

![Botones menu](/imgReadme/botonesmenu.png)
![Botones donde](/imgReadme/iconoboton.png)

### Avisos Personalizados con Toast
Mediante una librería de Android (StyleableToast) se han implementado avisos de tipo Toast personalizados para informar a los usuarios sobre eventos importantes, como el registro exitoso y la creación de tareas.

![Toast](/imgReadme/toastpersonalizado.png)
![Toast](/imgReadme/toastpersonalizado2.png)


### Funcionalidad de Edición de Tareas
Cada tarea se modifica clickando sobre el propio texto, lo que permite a los usuarios modificar el texto de la tarea original a través de un cuadro de diálogo, manteniendo actualizada la información en la base de datos y en la interfaz de usuario.

![Actualizar](/imgReadme/actualizar.gif)


## Verificación de formularios
Todos los campos de entrada (SignUp, EmailRecovery y Creación de Tareas) se han verificado para no permitir campos en blanco, formatos de email incorrectos o contraseñas de menos de 6 caracteres. 
![Recovery](/imgReadme/prueba_mail_recovery.gif)
![SignUp](/imgReadme/signup.gif)
![PruebaTarea](/imgReadme/prueba_tarea_vacia.gif)




