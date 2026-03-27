# Security Information

## ⚠️ Important Notice

This is a public repository used for portfolio/demonstration purposes. **Do not use any credentials found in the git history for production environments**.

## Credentials Management

### Environment Variables Required for Deployment

All sensitive credentials must be provided via environment variables. **NEVER hardcode secrets in the application**.

#### Required Variables:

```
DB_HOST=your_database_host
DB_PORT=3306
DB_NAME=your_database_name
DB_USERNAME=your_db_username
DB_PASSWORD=your_secure_db_password (minimum 20 characters)
SECURITY_USER=admin
SECURITY_PASSWORD=your_secure_password (minimum 16 characters)
JWT_SECRET=your_secure_jwt_secret (minimum 64 characters)
JWT_EXPIRATION=3600000
```

### Local Development

1. Copy `.env.example` to `.env`:
   ```bash
   cp .env.example .env
   ```

2. Update `.env` with **your own secure credentials**:
   ```bash
   # Generate secure passwords:
   openssl rand -base64 20  # Database password
   openssl rand -base64 16  # Security password
   openssl rand -base64 64  # JWT Secret
   ```

3. **NEVER commit `.env` to git** (it's in `.gitignore`)

### Railway Deployment

Railway provides secure secret management:

1. Set environment variables in Railway dashboard
2. Use variable names from the `.env.example` file
3. Generate new, strong credentials for production

## Past Security Note

Early commits contained hardcoded credentials for development purposes. These have been removed, but may still be visible in git history. Always:

- ✅ Use new credentials for any actual deployment
- ✅ Rotate all credentials regularly
- ✅ Use environment variables for all secrets
- ✅ Never hardcode sensitive data

## Best Practices

- All credentials are loaded from environment variables via `application.properties`
- Spring Security is configured with JWT authentication
- CORS is restricted to specific origins
- SQL Injection prevention through JPA
- Password encoding with BCrypt

## Questions

For security concerns, ensure credentials are managed through your deployment platform's secret management system.
