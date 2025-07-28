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

### Employee Management

#### Get Employees

```
GET /api/employees
```

Retrieves a paginated list of employees with optional filtering.

**Query Parameters:**

| Parameter      | Description                          | Type   | Required | Default |
|----------------|--------------------------------------|--------|----------|---------|
| `page`         | Page number for pagination (0-based) | Number | No       | 0       |
| `departmentId` | Filter employees by department UUID  | UUID   | No       | null    |
| `position`     | Filter employees by position         | String | No       | null    |

**Success Response Fields:**

| Field                | Description                                                         | Type    |
|----------------------|---------------------------------------------------------------------|---------|
| `data`               | Contains paginated employee information                             | Object  |
| `data.content`       | Array of employee objects                                           | Array   |
| `data.totalPages`    | Total number of pages available                                     | Number  |
| `data.totalElements` | Total number of employees matching the filter criteria              | Number  |
| `data.size`          | Number of employees per page                                        | Number  |
| `status`             | HTTP status code (200 for successful requests)                      | Number  |
| `message`            | Human-readable success message ("Employees retrieved successfully") | String  |
| `timestamp`          | ISO-8601 formatted date and time when the response was generated    | String  |
| `success`            | Boolean flag indicating the request was successful (true)           | Boolean |

**Note:** The employee objects in the response will differ based on the user's role:

**Admin Role Response (EmployeeAdminDto):**

| Field        | Description                                    | Type      |
|--------------|------------------------------------------------|-----------|
| `id`         | Employee UUID                                  | UUID      |
| `name`       | Employee name                                  | String    |
| `position`   | Employee position/job title                    | String    |
| `phone`      | Employee phone number                          | String    |
| `email`      | Employee email address                         | String    |
| `department` | Department object with id and name             | Object    |
| `createdAt`  | Timestamp when the employee record was created | Timestamp |
| `updatedAt`  | Timestamp when the employee record was updated | Timestamp |
| `isActive`   | Whether the employee is active                 | Boolean   |

**User Role Response (EmployeeUserDto):**

| Field        | Description                 | Type   |
|--------------|-----------------------------|--------|
| `id`         | Employee UUID               | UUID   |
| `name`       | Employee name               | String |
| `position`   | Employee position/job title | String |
| `phone`      | Employee phone number       | String |
| `email`      | Employee email address      | String |
| `department` | Department name             | String |
| `division`   | Division name               | String |

#### Get Employee by ID

```
GET /api/employees/{id}
```

Retrieves a specific employee by ID.

**Path Parameters:**

| Parameter | Description   | Type | Required |
|-----------|---------------|------|----------|
| `id`      | Employee UUID | UUID | Yes      |

**Success Response Fields:**

Similar to the Get Employees endpoint, the response will contain either an EmployeeAdminDto or EmployeeUserDto based on
the user's role.

#### Create Employee

```
POST /api/employees
```

Creates a new employee.

**Request Body Fields:**

| Field          | Description                                  | Type   | Required |
|----------------|----------------------------------------------|--------|----------|
| `name`         | Employee name                                | String | Yes      |
| `position`     | Employee position/job title                  | String | Yes      |
| `phone`        | Employee phone number                        | String | Yes      |
| `email`        | Employee email address (must be valid email) | String | No       |
| `departmentId` | UUID of the department                       | UUID   | No       |

**Success Response Fields:**

Returns the created employee as an EmployeeAdminDto with a 201 Created status.

#### Update Employee

```
PUT /api/employees/{id}
```

Updates an existing employee.

**Path Parameters:**

| Parameter | Description   | Type | Required |
|-----------|---------------|------|----------|
| `id`      | Employee UUID | UUID | Yes      |

**Request Body Fields:**

| Field          | Description                                  | Type   | Required |
|----------------|----------------------------------------------|--------|----------|
| `name`         | Employee name                                | String | Yes      |
| `position`     | Employee position/job title                  | String | Yes      |
| `phone`        | Employee phone number                        | String | Yes      |
| `email`        | Employee email address (must be valid email) | String | No       |
| `departmentId` | UUID of the department                       | UUID   | No       |

**Success Response Fields:**

Returns the updated employee as an EmployeeAdminDto with a 200 OK status.

#### Delete Employee

```
DELETE /api/employees/{id}
```

Deletes an employee.

**Path Parameters:**

| Parameter | Description   | Type | Required |
|-----------|---------------|------|----------|
| `id`      | Employee UUID | UUID | Yes      |

**Success Response Fields:**

Returns a success message with a 204 No Content status.

### User Role Management

#### Get Roles

```
GET /api/roles
```

Retrieves all available roles.

**Success Response:**

Returns an array of Role objects:

| Field  | Description | Type   |
|--------|-------------|--------|
| `id`   | Role UUID   | UUID   |
| `name` | Role name   | String |

#### Add Role to User

```
POST /api/users/{userId}/roles
```

Adds a role to a user.

**Path Parameters:**

| Parameter | Description | Type | Required |
|-----------|-------------|------|----------|
| `userId`  | User UUID   | UUID | Yes      |

**Request Body Fields:**

| Field    | Description | Type | Required |
|----------|-------------|------|----------|
| `roleId` | Role UUID   | UUID | Yes      |

**Success Response:**

Returns a UserDto object:

| Field      | Description                | Type    |
|------------|----------------------------|---------|
| `id`       | User UUID                  | UUID    |
| `username` | Username                   | String  |
| `isActive` | Whether the user is active | Boolean |
| `roles`    | Array of Role objects      | Array   |

#### Remove Role from User

```
DELETE /api/users/{userId}/roles/{roleId}
```

Removes a role from a user.

**Path Parameters:**

| Parameter | Description | Type | Required |
|-----------|-------------|------|----------|
| `userId`  | User UUID   | UUID | Yes      |
| `roleId`  | Role UUID   | UUID | Yes      |

**Success Response:**

Returns a UserDto object with the updated roles.

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

## Project Maintainers

<table>
    <tbody>
        <tr>
            <td align="center" valign="top">
                <img width="125" height="125" src="https://github.com/bermetMi.png?s=150">
                <br>
                <strong>bermetMi</strong>
                <br>
                <a href="https://github.com/bermetMi">@bermetMi</a>
            </td>
            <td align="center" valign="top">
                <img width="125" height="125" src="https://github.com/camilo960321.png?s=150">
                <br>
                <strong>camilo960321</strong>
                <br>
                <a href="https://github.com/camilo960321">@camilo960321</a>
            </td>
            <td align="center" valign="top">
                <img width="125" height="125" src="https://github.com/Gasiyu.png?s=150">
                <br>
                <strong>Gasiyu</strong>
                <br>
                <a href="https://github.com/Gasiyu">@Gasiyu</a>
            </td>
        </tr>
    </tbody>
</table>

## License

[MIT](https://github.com/Gasiyu/phonebook-app-spring-boot/blob/main/LICENSE)
© [bermetMi](https://github.com/bermetMi), [camilo960321](https://github.com/camilo960321), [Gasiyu](https://ngoding.id)
