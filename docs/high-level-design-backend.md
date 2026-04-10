# High Level Design (HLD) - Backend DOSW-Library


## 1. Contexto del sistema
El backend expone una API REST para:
- Gestion de usuarios
- Gestion de libros
- Gestion de prestamos
- Login y autorizacion con JWT

La aplicacion esta construida en Java 21 + Spring Boot 4, y permite ejecutar diferentes estrategias de persistencia mediante perfiles de Spring.

## 2. Vista de arquitectura
Se usa una arquitectura por capas con separacion clara de responsabilidades:

1. Capa API (Controllers + DTOs + Mappers)
- Expone endpoints HTTP
- Valida payloads de entrada
- Mapea DTO <-> modelo de dominio

2. Capa de Aplicacion/Dominio (Services + Models + Validators)
- Contiene reglas de negocio
- Orquesta casos de uso
- Valida invariantes de negocio

3. Capa de Persistencia desacoplada (Core Repository interfaces + adapters)
- Define contratos de repositorio en el dominio
- Implementa adaptadores para MongoDB y para JPA/PostgreSQL
- Seleccion de implementacion por perfil activo

4. Capa de Seguridad
- Filtro JWT por request
- Configuracion de reglas de autorizacion
- Manejo de errores 401/403

## 3. Componentes principales

### 3.1 API Layer
- AuthController
- UserController
- BookController
- LoanController
- DTOs de request/response
- GlobalExceptionHandler para respuestas de error consistentes

### 3.2 Domain/Application Layer
- UserService
- BookService
- LoanService
- AuthService
- Validadores (BookValidator, LoanValidator, etc.)

Reglas de negocio clave:
- Maximo 3 prestamos activos por usuario
- Un prestamo solo se crea si hay disponibilidad
- Devolucion cambia estado del prestamo e incrementa stock
- Titulos y usuarios se validan para evitar duplicados

### 3.3 Persistence Layer
- Interfaces de dominio en core/repository
- Implementaciones:
  - Relational adapters (JPA)
  - Non-relational adapters (Mongo)

Estrategia:
- Perfil `mongo`: usa repositorios/documentos Mongo
- Perfil `relational`: usa entidades/repositorios JPA

### 3.4 Security Layer
- JwtUtil para emision y validacion de tokens
- JwtAuthenticationFilter para autenticar cada request
- SecurityConfig con endpoints publicos y protegidos
- Integracion OpenAPI/Swagger con Bearer token

## 4. Flujos de alto nivel

### 4.1 Registro/Login
1. Cliente envia credenciales
2. AuthService valida usuario y password
3. JwtUtil genera token
4. Se retorna token al cliente

### 4.2 Crear prestamo
1. LoanController recibe solicitud
2. LoanService valida usuario, libro y fecha
3. Verifica limite de prestamos activos
4. Reduce disponibilidad del libro
5. Persiste prestamo via LoanRepository

### 4.3 Devolver libro
1. LoanController recibe usuario + titulo libro
2. LoanService busca prestamo activo
3. Actualiza estado a RETURNED
4. Incrementa disponibilidad del libro

## 5. Modelo de datos (alto nivel)
Entidades de dominio principales:
- User: id, name, username, passwordHash, role
- Book: id, title, author, totalStock, availableStock
- Loan: id, user, book, loanDate, returnDate, status, returnedAt

Persistencia:
- Relacional: tablas normalizadas para user/book/loan
- No relacional: documentos, con prestamos embebidos/relacionados segun el modelo de Mongo

## 6. Configuracion y perfiles
Configuracion por perfil de Spring:
- `mongo`: activa adaptadores Mongo y desactiva autoconfiguracion JPA
- `relational`: activa adaptadores JPA y desactiva autoconfiguracion Mongo
- `azure` (recomendado para despliegue): usa variables de entorno, puerto esperado por App Service y SSL embebido desactivado

## 7. Estrategia de despliegue
Objetivo de despliegue:
- Azure App Service
- CI en Pull Request (build/test/analysis)
- CD en push a main para deploy

Pipeline recomendado:
1. Build del jar con Maven
2. Test y analisis estatico
3. Deploy con GitHub Actions a App Service
4. Configuracion de variables de entorno en Azure (ej. SPRING_DATA_MONGODB_URI)

## 8. Calidad y observabilidad
- Pruebas unitarias por servicio y componentes de persistencia
- Cobertura con JaCoCo
- Analisis estatico (Checkstyle/PMD/Sonar opcional)
- Logs centralizados via App Service Log Stream en ambiente Azure

## 9. Riesgos y mitigaciones
1. Riesgo: errores por configuracion de puerto en App Service
- Mitigacion: usar perfil de despliegue con puerto esperado y sin SSL embebido

2. Riesgo: dependencia a credenciales hardcodeadas
- Mitigacion: mover secretos a variables de entorno y GitHub Secrets

3. Riesgo: diferencias entre Mongo y Relational
- Mitigacion: mantener contratos de repositorio en dominio y pruebas por adaptador

## 10. Decisiones de arquitectura
- Se adopta arquitectura por capas para claridad y mantenibilidad
- Se desacopla persistencia con interfaces en dominio para intercambiar tecnologias
- Se estandariza seguridad con JWT stateless
- Se prioriza configuracion por perfiles para ambientes local, pruebas y cloud

## 11. Criterios de exito
El backend cumple el HLD si:
- Expone API REST funcional para usuarios/libros/prestamos
- Aplica reglas de negocio en capa service
- Opera con Mongo o Relational sin cambiar logica de negocio
- Protege endpoints con JWT
- Se despliega y opera en Azure App Service con configuracion externa
