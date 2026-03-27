# 1. Abre una terminal en la raíz del proyecto y ejecuta:
keytool -genkeypair -alias dosw-library -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore keystore.p12 -validity 3650

# 2. Cuando te pida contraseña, usa una segura (ejemplo: password123)
# 3. Cuando te pida datos, puedes poner valores de prueba.
# 4. Mueve el archivo keystore.p12 a src/main/resources/
# 5. Agrega esto a tu application.yaml:

server:
  port: 8443
  ssl:
    enabled: true
    key-store: classpath:keystore.p12
    key-store-password: password123
    key-store-type: PKCS12
    key-alias: dosw-library

# 6. Reinicia tu aplicación y accede por https://localhost:8443
