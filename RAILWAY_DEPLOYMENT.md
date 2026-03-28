# Railway Deployment Guide

## Prerequisites

- Railway account
- PostgreSQL or MySQL database on Railway
- Git repo connected to Railway

## Step-by-Step Deployment

### 1. Create Environment Variables in Railway

In your Railway project dashboard, add these variables:

```
PORT=8081
# Option A (recommended): define DB_URL directly
DB_URL=jdbc:mysql://your_host:3306/your_database

# Option B: define by parts (fallback supported by app)
DB_HOST=your_railway_mysql_host
DB_PORT=3306
DB_NAME=your_database_name

DB_USERNAME=your_username
DB_PASSWORD=your_secure_password_here
SECURITY_USER=admin
SECURITY_PASSWORD=your_secure_password_here
JWT_SECRET=your_very_secure_jwt_secret_minimum_64_characters
JWT_EXPIRATION=3600000

# Frontend URL(s) allowed by CORS (comma separated)
CORS_ALLOWED_ORIGINS=https://your-frontend.up.railway.app
```

### 2. Generate Secure Credentials

Run these commands locally to generate secure values:

```bash
# Database password (20 chars)
openssl rand -base64 20

# Security password (16 chars)
openssl rand -base64 16

# JWT Secret (64+ chars)
openssl rand -base64 64
```

### 3. Database Setup

For MySQL service variables, this app now supports these Railway-style fallbacks:

- `MYSQLHOST` / `MYSQLPORT` / `MYSQLDATABASE`
- `MYSQLUSER` / `MYSQLPASSWORD`

If you already provide `DB_URL`, it has priority.

The app automatically creates/updates tables with:
```
spring.jpa.hibernate.ddl-auto=update
```

### 4. Connect Repository

1. Go to Railway dashboard
2. Create new project
3. Select "Deploy from Git"
4. Connect your GitHub repository
5. Railway will auto-detect the `Dockerfile`

### 5. Configure Port

Railway assigns a dynamic port via the `PORT` environment variable. The application reads this automatically from `application.properties`:

```properties
server.port=${PORT:8081}
```

### 6. Deploy

Railway will automatically:
- Build using the Dockerfile
- Run the Spring Boot application
- Expose it on a public URL

## Dockerfile Details

The provided Dockerfile uses multi-stage build:

```dockerfile
# Stage 1: Build with Maven
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml ./
COPY src ./src
RUN mvn -DskipTests clean package

# Stage 2: Runtime with JRE only
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar"]
```

This keeps the final image small by excluding Maven from the runtime.

## Local Testing with Docker

```bash
# Build image
docker build -t test-dev .

# Run with local .env
docker run --env-file .env -p 8081:8081 test-dev
```

## Troubleshooting

### Application won't start
- Check logs in Railway dashboard
- Verify all required environment variables are set
- Ensure database host/credentials are correct

### Database connection fails
- Verify `DB_URL` (or `DB_HOST`, `DB_PORT`, `DB_NAME`) and `DB_USERNAME`, `DB_PASSWORD`
- Check that MySQL service is running in Railway
- Test connection locally first

### Frontend gets CORS errors
- Set `CORS_ALLOWED_ORIGINS` with your real frontend URL (or multiple URLs separated by commas)
- Keep protocol included (`https://...`)

### Port issues
- Railway assigns port dynamically via `PORT` env var
- Don't hardcode port 8081 in production
- Application reads from config: ✅ Correct

## Security Reminders

✅ **DO:**
- Use Railway's secret management for all credentials
- Rotate credentials regularly
- Use strong, randomly generated passwords
- Update dependencies regularly

❌ **DON'T:**
- Hardcode credentials in code
- Commit `.env` to git
- Use simple passwords like "123456"
- Store secrets in comments or documentation
