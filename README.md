# ErgoHabit API

Backend REST desarrollado con **Ktor + Kotlin** para la aplicación móvil ErgoHabit — una plataforma de seguimiento de hábitos saludables para estudiantes universitarios.

El proyecto integra monitoreo postural inercial con el seguimiento de hábitos diarios (hidratación, sueño, ejercicio, nutrición y tareas de enfoque) con el objetivo de reducir las posturas de riesgo y mejorar la autogestión del bienestar.

---

## Stack tecnológico

| Tecnología | Uso |
|---|---|
| Kotlin 2.x + JDK 21 | Lenguaje principal |
| Ktor 2.3.x (Netty) | Framework HTTP |
| Exposed (ORM) | Acceso a base de datos |
| MySQL 8 | Base de datos relacional |
| HikariCP | Pool de conexiones |
| Koin 3.5 | Inyección de dependencias |
| JWT (auth-jwt) | Autenticación stateless |
| BCrypt | Hash de contraseñas |
| Cloudinary | Almacenamiento de fotos de perfil |
| Docker | Contenedorización |
| Render | Despliegue en la nube |

---

## Arquitectura

El proyecto organiza el código en módulos independientes, cada uno con tres capas internas:

```
src/main/kotlin/
│
├── Application.kt                          # Punto de entrada, configuración Koin + routing
│
├── common/
│   └── infrastructure/
│       ├── DatabaseFactory.kt              # Configuración HikariCP + Exposed
│       └── security/
│           ├── JwtConfig.kt               # Generación y validación de tokens JWT
│           └── AuthConfig.kt              # Configuración de autenticación Ktor
│
└── modules/
    │
    ├── autentificacion/                    # Login, registro, restablecer contraseña
    │   ├── domain/
    │   │   ├── model/Registro.kt
    │   │   └── repository/AutentificacionRepository.kt
    │   ├── application/usecase/
    │   │   ├── LoginUseCase.kt
    │   │   ├── RegistrarUseCase.kt
    │   │   └── RestablecerPasswordUseCase.kt
    │   └── infrastructure/
    │       ├── persistence/AutentificacionRepositoryImpl.kt
    │       └── rest/
    │           ├── AutentificacionController.kt
    │           ├── AutentificacionRouting.kt
    │           └── dto/LoginRequest.kt
    │
    ├── usuarios/                           # CRUD perfil + foto de perfil (Cloudinary)
    │   ├── domain/
    │   ├── application/usecase/
    │   └── infrastructure/
    │       ├── persistence/
    │       └── rest/dto/
    │
    ├── catalogosRol/                       # Catálogo de roles (id_rol 1=admin, 2=usuarioFinal)
    │   └── infrastructure/persistence/RolTable.kt
    │
    ├── administrador/
    │   └── reportes/                       # Panel admin — reporte postura comunidad
    │       ├── domain/
    │       │   ├── model/ResumenComunidad.kt
    │       │   └── repository/AdminReportesRepository.kt
    │       ├── application/usecase/ObtenerReportePosturaUseCase.kt
    │       └── infrastructure/
    │           ├── persistence/MysqlAdminReportesRepository.kt
    │           └── rest/
    │               ├── AdminReportesController.kt
    │               ├── AdminReportesRouter.kt
    │               └── dto/AdminReportesResponse.kt
    │
    └── usuarioFinal/
        │
        ├── ConfiguracionHabitos/           # Tabla compartida: meta agua, meta ejercicio, horarios
        │   └── infrastructure/persistence/ConfiguracionHabitos.kt
        │
        ├── agua/                           # Dashboard + tomas + meta + progreso semanal
        │   ├── domain/
        │   ├── application/usecase/
        │   └── infrastructure/
        │       ├── persistence/
        │       │   ├── ProgresoAguaTable.kt
        │       │   ├── AguaDetalleTable.kt
        │       │   └── MysqlAguaRepository.kt
        │       └── rest/
        │
        ├── sueno/                          # Dashboard + horario + despertar + progreso semanal
        ├── ejercicio/                      # Dashboard + km + meta + progreso semanal
        ├── nutricion/                      # Dashboard + horarios + marcar comidas
        │
        ├── ergonomia/                      # Sincronizar alertas sensor + historial + progreso semanal
        │   ├── domain/
        │   ├── application/usecase/
        │   └── infrestructure/             # (nota: carpeta con ese nombre en el proyecto)
        │       ├── persistence/
        │       └── rest/
        │
        ├── tarea/                          # CRUD tareas + cronómetro Pomodoro (8 use cases)
        │   ├── domain/
        │   ├── application/usecase/        # Crear, Listar, Iniciar, Pausar, Completar,
        │   │                               # Extender, Eliminar, ObtenerDetalle
        │   └── infrastructure/
        │       ├── persistence/
        │       │   ├── TareaEnfoqueTable.kt
        │       │   ├── EstadoTareaTable.kt
        │       │   └── MysqlTareaRepository.kt
        │       └── rest/
        │
        ├── frases/                         # Frases motivacionales por categoría
        ├── racha/                          # Tabla racha_usuario (calculada en progresoDiario)
        └── progresoDiario/                 # Dashboard consolidado de todos los hábitos + racha
            ├── domain/
            ├── application/usecase/VerProgresoDiarioUseCase.kt
            └── infrastructure/
                ├── persistence/MysqlProgresoDiarioRepo.kt  ← usa LEFT JOIN de 6 tablas
                └── rest/
```

Cada módulo sigue el patrón:
- `domain/` — modelos e interfaces (sin dependencias externas)
- `application/usecase/` — lógica de negocio, validaciones de rol
- `infrastructure/persistence/` — implementación con Exposed + MySQL
- `infrastructure/rest/` — Controller (manejo HTTP) + Router (rutas) + dto/

---

## Variables de entorno

Crea un archivo `.env` en la raíz del proyecto:

```env
# Base de datos
DB_URL=jdbc:mysql://localhost:3306/ergohabit?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
DB_USER=tu_usuario
DB_PASSWORD=tu_password

# JWT
JWT_SECRET=tu_secreto_seguro

# Cloudinary (fotos de perfil)
CLOUDINARY_CLOUD_NAME=tu_cloud_name
CLOUDINARY_API_KEY=tu_api_key
CLOUDINARY_API_SECRET=tu_api_secret
```

En **Render**, configura estas mismas variables en *Environment → Environment Variables*.

---

## Instalación y ejecución local

**Requisitos:** JDK 21, Gradle 8, MySQL 8

```bash
# Clonar el repositorio
git clone https://github.com/tu-usuario/ktor-ErgoHabit.git
cd ktor-ErgoHabit

# Configurar variables de entorno
cp .env.example .env
# Edita .env con tus credenciales

# Ejecutar el servidor
./gradlew run
```

El servidor arranca en `http://0.0.0.0:8080`.

---

## Docker

```bash
# Construir imagen
./gradlew buildFatJar
docker build -t ergohabit-api .

# Ejecutar contenedor
docker run -p 8080:8080 --env-file .env ergohabit-api
```


## Endpoints

### Autenticación
| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/v1/auth/login` | Iniciar sesión → devuelve JWT |
| POST | `/api/v1/auth/register` | Registrar nuevo usuario |
| POST | `/api/v1/auth/password` | Restablecer contraseña |

### Usuarios
| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/v1/usuarios/{id}` | Ver perfil |
| PUT | `/api/v1/usuarios/{id}` | Actualizar perfil |
| POST | `/api/v1/usuarios/{id}/foto` | Subir foto de perfil |

### Agua
| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/v1/habitos/agua/dashboard` | Dashboard de hidratación |
| POST | `/api/v1/habitos/agua/toma` | Registrar toma de agua |
| PUT | `/api/v1/habitos/agua/meta` | Configurar meta diaria |
| GET | `/api/v1/habitos/agua/progreso-semanal` | Gráfica semanal |

### Sueño
| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/v1/habitos/sueno/dashboard` | Dashboard de sueño |
| PUT | `/api/v1/habitos/sueno/horario` | Configurar horario de sueño |
| POST | `/api/v1/habitos/sueno/despertar` | Registrar despertar |
| GET | `/api/v1/habitos/sueno/progreso-semanal` | Gráfica semanal |

### Ejercicio
| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/v1/habitos/ejercicio/dashboard` | Dashboard de ejercicio |
| POST | `/api/v1/habitos/ejercicio/recorrido` | Registrar kilómetros |
| PUT | `/api/v1/habitos/ejercicio/meta` | Configurar meta diaria |
| GET | `/api/v1/habitos/ejercicio/progreso-semanal` | Gráfica semanal |

### Nutrición
| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/v1/habitos/nutricion/dashboard` | Dashboard de nutrición |
| PUT | `/api/v1/habitos/nutricion/horarios` | Configurar horarios de comidas |
| POST | `/api/v1/habitos/nutricion/marcar` | Marcar comida completada |

### Ergonomía (Postura)
| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/v1/ergonomia/sincronizar` | Sincronizar alertas del sensor |
| GET | `/api/v1/ergonomia/historial` | Historial de posturas |
| GET | `/api/v1/ergonomia/progreso-semanal` | Gráfica semanal |

### Tareas de Enfoque
| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/v1/tareas` | Listar tareas del día |
| POST | `/api/v1/tareas` | Crear tarea |
| PATCH | `/api/v1/tareas/{id}/iniciar` | Iniciar cronómetro |
| PATCH | `/api/v1/tareas/{id}/pausar` | Pausar cronómetro |
| PATCH | `/api/v1/tareas/{id}/completar` | Marcar completada |
| PATCH | `/api/v1/tareas/{id}/extender` | Agregar tiempo extra |
| GET | `/api/v1/tareas/{id}/cronometro` | Detalle del cronómetro |
| DELETE | `/api/v1/tareas/{id}` | Eliminar tarea |

### Progreso Diario
| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/v1/progresodiario/{idUsuario}` | Dashboard consolidado + racha |

### Frases Motivacionales
| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/v1/frases/aleatoria/{categoria}` | Frase aleatoria por categoría |

### Panel Administrador *(requiere rol admin)*
| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/v1/admin/reportes/postura` | KPI global + detalle semanal postura |

