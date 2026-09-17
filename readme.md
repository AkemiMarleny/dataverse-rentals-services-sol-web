# Dataverse Rentals Services

## launch.json

```json
{
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "DataverserentalsApplication",
            "request": "launch",
            "mainClass": "com.dataverse.dataverserentals.DataverserentalsApplication",
            "projectName": "dataverserentals",
            "env": {
                "DATASOURCE_URL": "jdbc:postgresql://localhost:5432/dataverse_db",
                "DB_USERNAME": "admin",
                "DB_PASSWORD": "admin",
                "JWT_SECRET": "42290b6c825e5e1653023d2c2561b9facbc65b6fec6d1c8ec9a17370923d07be",
                "JWT_EXPIRATION": "86400000",
                "MFA_ENCRYPTOR_PASSWORD": "tbXx956gnMckxPYWYB",
                "MFA_ENCRYPTOR_SALT": "f0d80ee874c946bb",
                "SPRING_PROFILES_ACTIVE": "dev"
            }
        }
    ]
}
```
