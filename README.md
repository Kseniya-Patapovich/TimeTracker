# TimeTracker
Test task Java trainee Krainet

## Descriptions
An application for recording the time spent by the user at work.

## Technologies
- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Postgres
- Flyway
- Spring Security
- Docker Compose

## Authentication
To authenticate an existing user, you need to follow the path <http://localhost:8080/auth> and enter the authentication data into the body, then you will receive a response with a JWT token.
After authentication, the user can have three roles: USER, ADMIN, SUPER_ADMIN.

## USER capabilities
The user has the ability to create new reports and make changes to them.

## ADMIN capabilities
The administrator has the ability to create, edit and delete users. Administrators can also create projects, add users to projects, change and delete projects. And the admin can view all information about users, projects and records.

## SUPER_ADMIN capabilities
A superadmin has more power than an admin.
Superadmin can create, modify and delete users and admins. And can view all information about users. Also superadmin can create, delete, update projects and view all information about projects.
Superadmin Authentication: 
```json
{
"login":"superadmin",
"password":"superadmin"
}
```

## Swagger
You can use swagger <http://localhost:8080/swagger-api>

## How to run the application using docker:
1. ```git clone https://github.com/Kseniya-Patapovich/TimeTracker.git```
2. ```docker-compose build```
3. ```docker-compose up```
