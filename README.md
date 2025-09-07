# OppexAi

Full-stack project with **Quarkus backend** and **React frontend**.

---

##  Tech Stack
- **Backend**: Quarkus (Java), PostgreSQL  
- **Frontend**: React (with .env configs)  

---

##  Project Setup  

### Clone the repo
```bash
git clone https://github.com/rishisni/OppexAI.git
cd oppexai
```

---

## Backend (Quarkus)

### Requirements
- Java 17+
- Maven
- PostgreSQL running locally

### Database Setup (PostgreSQL)
1. Install PostgreSQL (on Ubuntu):
   ```bash
   sudo apt update
   sudo apt install postgresql postgresql-contrib
   ```

2. Start PostgreSQL service:
   ```bash
   sudo service postgresql start
   ```

3. Create a database and user:
   ```bash
   sudo -u postgres psql
   CREATE DATABASE oppexai_db;
   CREATE USER oppexai_user WITH ENCRYPTED PASSWORD 'oppexai_pass';
   GRANT ALL PRIVILEGES ON DATABASE oppexai_db TO oppexai_user;
   \q
   ```

### Configure Quarkus
Edit `backend-quarkus/src/main/resources/application.properties`:

```properties
quarkus.http.port=8080

# PostgreSQL config
quarkus.datasource.db-kind=postgresql
quarkus.datasource.username=oppexai_user
quarkus.datasource.password=oppexai_pass
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/oppexai_db

# Hibernate ORM
quarkus.hibernate-orm.database.generation=update
```

### Run locally
```bash
cd backend-quarkus
./mvnw quarkus:dev
```
Backend will be available at:  
 `http://localhost:8080`

### Build for production
```bash
./mvnw package
java -jar target/quarkus-app/quarkus-run.jar
```

---

##  Frontend (React)

### Requirements
- Node.js 18+
- npm or yarn

### Environment Variables
Create a `.env` file inside `frontend-react/`:

```env
# API (local)
REACT_APP_API_BASE_URL=http://localhost:8080/api

# Auth
REACT_APP_COOKIE_NAME=session

# App
REACT_APP_APP_NAME=OppexAi
```

### Run locally
```bash
cd frontend-react
npm install
npm start
```
Frontend will be available at:  
`http://localhost:3000`

### Build for production
```bash
npm run build
```
This generates optimized static files in the `build/` folder.

---

## API Test (Login Example)

```bash
curl -v http://localhost:8080/api/login -H "Content-Type: application/json" -d '{"email":"test@example.com","password":"password123"}'
```

---

##  Notes
- Make sure PostgreSQL is running before starting Quarkus backend.  
- Update `.env` and `application.properties` if using different DB credentials.  
- Run frontend (`npm start`) and backend (`./mvnw quarkus:dev`) **in parallel** during development.  

---
