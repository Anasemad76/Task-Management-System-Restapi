# Task Management System REST API

## Overview
This is a **Task Management System** backend project built using **Spring 5**. It provides a REST API to manage tasks, user accounts, and admin functionalities. The API allows the creation, modification, and deletion of tasks, assigning tasks to users, and more.

The system is designed to manage tasks effectively and is intended to be integrated with a frontend (Swing UI or web application) for full-stack interaction.

## Features
- **User Authentication**: Users can register, log in, and access their tasks.
- **Task Management**: Worker can view, filter,update tasks.
- **Admin Features**: Admin can manage tasks, view users, and perform administrative actions.
- **Task Priority Management**: Tasks can be categorized by priority (e.g., High, Medium, Low).
- **User Assignment**: Admin can assign tasks to different users.

## Technology Stack
- **Backend**: Spring Core, Spring MVC
- **Database**: Microsoft SQL Server (JDBC)
- **ORM**: Hibernate (JPA)
- **Serialization/Deserialization**: Jackson (for JSON handling)
- **Authentication**: Custom user authentication via username and password.

## Some Endpoints Examples
### 1. **Login User**
- **URL**: `/api/user/login`
- **Method**: `POST`
- **Request Body**: `username`, `password`
- **Response**: `200 OK` with the user details if credentials are correct.

### 2. **Register User**
- **URL**: `/api/user/register`
- **Method**: `POST`
- **Request Body**: `username`, `password`, `isAdmin`, etc.
- **Response**: `201 Created` if user is successfully registered.

### 3. **Create Task**
- **URL**: `/api/task/addTask?username=worker1`
- **Method**: `POST`
- **Request Body**:
  ```json
  {
       "taskTitle": "Payment Fraud",
        "taskDescription": "AI driven project",
        "isCompleted": false,
        "priority": 3,
        "dueDate": "2025-06-06",
        "isApproved": false
  }
