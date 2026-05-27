# Cambios al Backend Spring Boot para Angular

## Resumen
El backend pasa de Thymeleaf (MVC) a una REST API con JWT.
Angular consume los endpoints JSON directamente.

## Archivos a MODIFICAR o CREAR

### 1. SecurityConfig.java — REEMPLAZAR COMPLETAMENTE
### 2. Todos los Controllers — REEMPLAZAR COMPLETAMENTE  
### 3. Agregar CorsConfig.java — NUEVO ARCHIVO
### 4. Agregar JwtUtil.java — NUEVO ARCHIVO
### 5. Agregar AuthController.java — NUEVO ARCHIVO
### 6. pom.xml — ya tiene jjwt, solo agregar hibernate-validator

Ver cada archivo .java en esta carpeta.
