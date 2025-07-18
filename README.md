# Phonebook APP

A RESTful API for managing phonebook systems. Built with Spring Boot and Kotlin, this backend service provides employee directory management with authentication and authorization features.

## API Documentation

### Authentication

#### Login

```
POST /api/login
```

Authenticates a user and returns a JWT token.

**Request Body Fields:**

| Field      | Description                 | Type   | Required |
|------------|-----------------------------|--------|----------|
| `username` | Username for authentication | String | Yes      |
| `password` | Password for authentication | String | Yes      |

**Success Response Fields:**

| Field                   | Description                                                        | Type    |
|-------------------------|--------------------------------------------------------------------|---------|
| `data`                  | Contains authentication information                                | Object  |
| `data.accessToken`      | JWT token used for authenticating subsequent requests              | String  |
| `data.expiresInSeconds` | Token validity duration in seconds (typically 3600 seconds/1 hour) | Number  |
| `data.expiresAt`        | Unix timestamp (in milliseconds) when the token will expire        | Number  |
| `status`                | HTTP status code (200 for successful requests)                     | Number  |
| `message`               | Human-readable success message ("Login successful")                | String  |
| `timestamp`             | ISO-8601 formatted date and time when the response was generated   | String  |
| `success`               | Boolean flag indicating the request was successful (true)          | Boolean |

**Error Response Fields:**

| Field         | Description                                                                                                                     | Type    |
|---------------|---------------------------------------------------------------------------------------------------------------------------------|---------|
| `description` | Detailed explanation of the error (e.g., "Invalid username or password provided" or "Password is required and cannot be blank") | String  |
| `status`      | HTTP status code (401 for authentication failures, 400 for invalid requests)                                                    | Number  |
| `message`     | Standard HTTP status message (e.g., "Unauthorized" or "Bad Request")                                                            | String  |
| `timestamp`   | ISO-8601 formatted date and time when the response was generated                                                                | String  |
| `success`     | Boolean flag indicating the request failed (false)                                                                              | Boolean |

## Sample Credentials

### ROLE ADMIN

```
username: admin
password: admin
```

### ROLE MANAGER

```
username: manager
password: manager
```

### ROLE USER

```
username: user
password: user
```
