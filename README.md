# DocTrack — Personal Document Vault

**DocTrack** is a secure personal document management system designed to help users store, organize, monitor, and manage important documents while receiving timely reminders before documents expire.

The application addresses a common problem: important personal documents such as passports, national identification documents, driver's licenses, visas, insurance documents, and certificates often have expiration dates that are easy to forget.

DocTrack provides a centralized digital vault where users can securely upload their documents, view their document information, monitor expiration dates, and receive email notifications when renewal is approaching.

---

## 📌 Project Overview

Managing multiple personal documents manually can make it difficult to keep track of expiration and renewal dates. Missing the renewal of an important document can lead to inconvenience, additional costs, or inability to access certain services.

DocTrack was developed to provide a centralized solution for:

* Securely storing personal documents
* Organizing documents by type
* Tracking document expiration dates
* Monitoring upcoming renewals
* Sending automated email reminders
* Managing user accounts and authentication
* Supporting documents with front and back images
* Providing a RESTful backend API for application integration

The system is built using **Java and Spring Boot**, with **PostgreSQL** used for persistent data storage.

---

## ✨ Key Features

### 👤 User Authentication

DocTrack provides user account management functionality, including:

* User registration
* User login
* Password management
* Password reset functionality
* Token-based authentication
* Secure password storage

---

### Document Management

Users can manage their personal documents through the application.

Supported document types include:

* National ID
* NIN
* Passport
* Driver's License
* Visa
* Insurance
* Certificate
* Other/custom document types

Users can:

* Add new documents
* View saved documents
* Update document information
* Delete documents
* Upload document images/files
* Store front and back document images
* Specify document issue dates
* Specify document expiration dates
* Add custom document types where necessary

---

### ⏰ Expiration & Renewal Tracking

One of DocTrack's core features is document expiration monitoring.

The system stores expiration information and automatically checks documents for upcoming expiration dates.

This allows users to identify documents that:

* Are still valid
* Are approaching expiration
* Require renewal

---

### 📧 Automated Email Reminders

DocTrack includes an automated reminder system that uses email notifications to inform users when their documents are approaching their expiration dates.

The backend includes a scheduled reminder process that periodically checks stored documents and triggers email notifications when appropriate.

This reduces the need for users to manually monitor every document.

---

### 🖼️ Document Image Storage

The system supports storing document images, including:

* Front images
* Back images
* File/image type information

This allows documents to be maintained digitally alongside their associated metadata.

---

### 🔐 Security

Security is an important part of the application.

The backend is designed around:

* Authentication
* Authorization
* Secure password hashing
* Token-based security
* Environment-based secret configuration
* Protected API endpoints
* Database-backed user accounts

Sensitive configuration values such as database credentials and email passwords are **not hard-coded into the application**.

---

## 🏗️ System Architecture

DocTrack follows a layered Spring Boot architecture.

```text
Client / Frontend
       │
       ▼
 REST API Controllers
       │
       ▼
    Services
       │
       ▼
Repositories / JPA
       │
       ▼
 PostgreSQL Database
       │
       ├───────────────┐
       ▼               ▼
Document Storage   Email Service
                       │
                       ▼
                Scheduled Reminders
```

### Architecture Layers

#### Controller Layer

Responsible for:

* Receiving HTTP requests
* Validating request data
* Returning HTTP responses
* Exposing REST API endpoints

#### Service Layer

Contains the application's business logic.

Examples include:

* User management
* Authentication
* Document management
* Email processing
* Reminder processing

#### Repository Layer

Handles communication between the application and PostgreSQL using Spring Data JPA.

#### Entity Layer

Defines the application's database models, including users and documents.

---

## 🛠️ Technology Stack

| Technology                     | Purpose                                 |
| ------------------------------ | --------------------------------------- |
| **Java**                       | Primary programming language            |
| **Spring Boot**                | Backend application framework           |
| **Spring Data JPA**            | Database access and ORM                 |
| **Spring Security**            | Authentication and application security |
| **Hibernate**                  | Object-relational mapping               |
| **PostgreSQL**                 | Relational database                     |
| **Maven**                      | Dependency and project management       |
| **Jakarta Mail / Spring Mail** | Email delivery                          |
| **JWT**                        | Token-based authentication              |
| **REST API**                   | Client-server communication             |

---

## 📂 Project Structure

The backend follows a standard Spring Boot project structure.

```text
doctrack/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── wura/
│   │   │       └── example/
│   │   │           └── doctrack/
│   │   │               │
│   │   │               ├── controller/
│   │   │               │
│   │   │               ├── service/
│   │   │               │
│   │   │               ├── repository/
│   │   │               │
│   │   │               ├── entity/
│   │   │               │
│   │   │               ├── dto/
│   │   │               │
│   │   │               ├── security/
│   │   │               │
│   │   │               └── ...
│   │   │
│   │   └── resources/
│   │       ├── application.properties
│   │       └── ...
│   │
│   └── test/
│
├── .gitignore
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

> The exact package/folder structure may vary slightly depending on the current implementation.

---

# 🚀 Getting Started

## Prerequisites

Before running DocTrack locally, make sure the following are installed:

* Java JDK
* Maven or Maven Wrapper
* PostgreSQL
* Git

You can verify Java with:

```bash
java -version
```

Verify Git with:

```bash
git --version
```

---

## 1. Clone the Repository

```bash
git clone <YOUR_GITHUB_REPOSITORY_URL>
```

Navigate into the project:

```bash
cd doctrack
```

---

## 2. Create the PostgreSQL Database

Create a PostgreSQL database named:

```text
doctrack
```

For example, using PostgreSQL:

```sql
CREATE DATABASE doctrack;
```

Make sure your PostgreSQL server is running before starting the application.

---

## 3. Configure Environment Variables

DocTrack uses environment variables for sensitive configuration.

This prevents credentials such as database passwords and email authentication credentials from being committed to GitHub.

The application expects variables such as:

```text
DB_PASSWORD
MAIL_USERNAME
MAIL_PASSWORD
APP_BASE_URL
```

Example:

```bash
export DB_PASSWORD='your_database_password'
export MAIL_USERNAME='your_email@gmail.com'
export MAIL_PASSWORD='your_google_app_password'
export APP_BASE_URL='http://localhost:8080'
```

### Important

Do **not** commit real passwords, API keys, authentication tokens, or other secrets to the repository.

A safe `.env.example` file can be used to document the required variables:

```env
DB_PASSWORD=
MAIL_USERNAME=
MAIL_PASSWORD=
APP_BASE_URL=
```

---

## 4. Configure PostgreSQL

The application connects to PostgreSQL using the configuration defined in:

```text
src/main/resources/application.properties
```

A typical configuration looks like:

```properties
spring.application.name=doctrack

spring.datasource.url=jdbc:postgresql://localhost:5432/doctrack
spring.datasource.username=postgres
spring.datasource.password=${DB_PASSWORD}

server.servlet.context-path=/api/v1.0

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

---

## 5. Configure Email

DocTrack uses Gmail SMTP for sending reminder emails.

Example configuration:

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=465

spring.mail.properties.mail.smtp.socketFactory.class=javax.net.ssl.SSLSocketFactory
spring.mail.properties.mail.smtp.socketFactory.port=465

spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}

spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.ssl.trust=smtp.gmail.com
```

For Gmail, use a **Google App Password** rather than your normal Gmail account password.

---

# ▶️ Running the Application

The project includes the Maven Wrapper, so Maven does not need to be installed globally.

On macOS/Linux:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

Once the application starts successfully, the backend will be available at:

```text
http://localhost:8080
```

Because the application uses the context path:

```text
/api/v1.0
```

API requests will generally use:

```text
http://localhost:8080/api/v1.0/...
```

---

# 🔌 API Overview

DocTrack exposes RESTful endpoints for authentication and document management.

## Authentication

Typical authentication operations include:

```text
POST /api/v1.0/register
POST /api/v1.0/login
```

Additional account-management functionality includes:

```text
GET /api/v1.0/activate
POST /api/v1.0/forgot-password
POST /api/v1.0/reset-password
POST /api/v1.0/resend-activation
```

> Exact endpoint paths may vary depending on the current controller implementation.

---

## Document Management

The document API is responsible for creating, retrieving, updating, and deleting personal documents.

Typical operations include:

```text
POST   /api/v1.0/...
GET    /api/v1.0/...
PUT    /api/v1.0/...
PATCH  /api/v1.0/...
DELETE /api/v1.0/...
```

Document requests may contain information such as:

```text
Document type
Document name
Issue date
Expiry date
Custom document type
Front document image
Back document image
```

---

# 📧 Reminder System

The reminder system is implemented using a scheduled backend process.

Conceptually, the process works as follows:

```text
Scheduled Task
      │
      ▼
Check Stored Documents
      │
      ▼
Compare Expiry Dates
      │
      ├── Not approaching expiry
      │          │
      │          ▼
      │        No action
      │
      └── Approaching expiry
                 │
                 ▼
           Email Service
                 │
                 ▼
          User Notification
```

The scheduled process allows the application to automatically monitor documents without requiring the user to manually check expiration dates.

---

# 🗄️ Database

DocTrack uses **PostgreSQL** as its primary relational database.

The database stores information associated with:

* Users
* Authentication/account status
* Documents
* Document types
* Expiration information
* Document images/files
* Reminder-related information

Spring Data JPA and Hibernate are used to map Java entities to relational database tables.

---

# 🔒 Security & Configuration

Security-sensitive configuration is externalized using environment variables.

The repository should **never contain**:

```text
Database passwords
Email passwords
Google App Passwords
JWT secrets
API keys
Private credentials
Production secrets
```

The `.gitignore` file excludes local environment files and sensitive configuration files.

For example:

```gitignore
.env
.env.*
!.env.example
application-local.properties
application-secret.properties
```

This allows developers to maintain their own local configuration without exposing credentials in source control.

---

# 🧪 Testing

The project can be tested using Spring Boot's testing infrastructure.

Run:

```bash
./mvnw test
```

For a complete Maven build:

```bash
./mvnw clean package
```

If the build completes successfully, the generated application JAR will be available under:

```text
target/
```

---

# 🐛 Troubleshooting

## Port 8080 Already in Use

If you receive:

```text
Web server failed to start. Port 8080 was already in use.
```

Find the process using port 8080:

```bash
lsof -i :8080
```

Then stop the process:

```bash
kill <PID>
```

If necessary:

```bash
kill -9 <PID>
```

Alternatively, change the application port:

```properties
server.port=${PORT:8081}
```

---

## Database Connection Error

If the application cannot connect to PostgreSQL, verify:

1. PostgreSQL is running.
2. The `doctrack` database exists.
3. The PostgreSQL username is correct.
4. `DB_PASSWORD` is correctly configured.
5. PostgreSQL is listening on port `5432`.

---

## Email Sending Error

If email notifications fail, verify:

1. Gmail SMTP configuration is correct.
2. 2-Step Verification is enabled on the Google account.
3. A valid Google App Password is being used.
4. `MAIL_USERNAME` is correct.
5. `MAIL_PASSWORD` is correctly configured.
6. The application can connect to Gmail's SMTP server.

Never use your normal Gmail password as the SMTP password when using Google's App Password authentication.

---

# 🔄 Development Workflow

A typical development workflow is:

```text
1. Clone repository
       ↓
2. Configure PostgreSQL
       ↓
3. Configure environment variables
       ↓
4. Start Spring Boot application
       ↓
5. Test API endpoints
       ↓
6. Implement changes
       ↓
7. Run automated tests
       ↓
8. Build application
       ↓
9. Commit changes
       ↓
10. Push to GitHub
```

---

# 📈 Future Improvements

Potential future enhancements include:


* [ ] Advanced document search and filtering
* [ ] Configurable reminder periods
* [ ] Multiple reminder notifications
* [ ] Push notifications
* [ ] SMS reminders
* [ ] Cloud file storage
* [ ] Audit logs
* [ ] Improved role-based authorization
* [ ] API documentation with Swagger/OpenAPI
* [ ] Docker containerization
* [ ] CI/CD pipeline
* [ ] Automated integration testing
* [ ] Production deployment

---

# 🎯 Project Objectives

The main objectives of DocTrack are to:

1. Provide users with a centralized platform for managing personal documents.
2. Reduce the risk of missing important document expiration dates.
3. Automate document expiration monitoring.
4. Provide timely renewal reminders through email.
5. Implement secure authentication and account management.
6. Provide a reliable REST API for frontend applications.
7. Demonstrate practical backend development using Java and Spring Boot.

---

# 💡 What This Project Demonstrates

DocTrack demonstrates practical experience in backend software development, including:

* Java application development
* Spring Boot development
* RESTful API design
* Spring Data JPA
* Hibernate ORM
* PostgreSQL database design
* Authentication and authorization
* JWT-based security
* Password management
* File/image handling
* Scheduled background processes
* Email integration
* Environment-based configuration
* Exception and error handling
* Maven project management
* Git/GitHub version control

The project was designed not only as a document-management application but also as a practical demonstration of building and integrating multiple backend components into a complete software system.

---

# 📸 Screenshots

Screenshots of the application can be added here to demonstrate the user interface and key functionality.

Recommended screenshots include:

* Login page
* Registration page
* Dashboard
* Add document page
* Document details
* Document list
* Expiration/reminder notification
* Profile/settings page

Example:

```text
## Screenshots

### Login

[Add screenshot here]

### Dashboard

[Add screenshot here]

### Document Management

[Add screenshot here]
```

---

# 🤝 Contributing

Contributions and suggestions are welcome.

To contribute:

1. Fork the repository.
2. Create a feature branch.

```bash
git checkout -b feature/your-feature
```

3. Make your changes.
4. Run the test suite.

```bash
./mvnw test
```

5. Commit your changes.

```bash
git commit -m "Add your feature"
```

6. Push your branch.

```bash
git push origin feature/your-feature
```

7. Open a Pull Request.

---

# 📄 License

This project is currently intended for educational, portfolio, and demonstration purposes.

If this project is released under a specific open-source license, replace this section with the appropriate license information.

---

# 👩🏽‍💻 Author

**Wuraola Hikimat Oyemade**

Computer Science Graduate | Backend Developer

### Technical Interests

* Java
* Spring Boot
* REST APIs
* PostgreSQL
* Backend Development
* Software Engineering

---

## ⭐ Support

If you find this project useful or interesting, consider giving the repository a ⭐ on GitHub.

---

**DocTrack — Keep your documents organized. Never miss a renewal.**
