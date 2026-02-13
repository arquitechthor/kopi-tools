# S3 Synchronization with Cognito Walkthrough

I have integrated AWS Cognito to support multi-user synchronization. Each user now has their own isolated backup file in S3.

## Key Changes

### Security & Authentication
- **Integrated** `spring-security-oauth2-client` for Cognito login.
- **Added** `SecurityConfig` to secure endpoints.
- **Improved Loop Protection**: Removed custom `loginPage` redirection to prevent infinite loops on authentication failure.
- **Optimized Scopes**: Reduced to only `openid` to resolve `invalid_scope` errors with standard Cognito configurations.

### Synchronization Logic
- **Updated** `SyncManager` to use `userId` in backup filenames.
- **Updated** `SyncController` to pass the authenticated user's ID.
- **Added** "Restore Backup" button in `sync.html`.

## Configuration

Required Environment Variables:
- `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, `AWS_REGION`, `S3_BUCKET_NAME`
- `COGNITO_CLIENT_ID`, `COGNITO_CLIENT_SECRET`, `COGNITO_ISSUER_URI`

## Unit Test Fixes

The unit tests were updated to support Spring Security:
- Created `TestSecurityConfig.java` to mock `ClientRegistrationRepository`.
- Added `spring-security-test` to `pom.xml`.
- Updated `LinkControllerTest.java` with `@WithMockUser` and CSRF protection.

## Troubleshooting Redirect Loop (`ERR_TOO_MANY_REDIRECTS`)

Si encuentras un bucle de redirección:

1.  **Limpieza de Bucle**: He eliminado la redirección automática en `SecurityConfig.java`. Ahora, si hay un error, el sistema no volverá a intentar el login automáticamente.
2.  **Scope Erróneo**: He reducido los scopes a solo `openid` en `application.properties`. Si esto funciona, puedes intentar añadir `profile` y `email` de nuevo.
3.  **Configuración de Cognito**: Asegúrate de que en la consola de AWS Cognito, tu **App Client** tenga habilitado el scope "openid".
4.  **Cookies**: Limpia las cookies de `localhost`.
5.  **Logs**: He activado `DEBUG` para seguridad. Si ves `invalid_scope` en los logs, es que el App Client no tiene permisos ni para `openid`.

Para verificar todo el proceso:
```powershell
.\mvnw.cmd clean package
java -jar target/kopi-tools-0.0.1-SNAPSHOT.jar
```
