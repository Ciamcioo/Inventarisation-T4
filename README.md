# Inventarisation-T4

## Project Description

**Inventarisation-T4** is a Java-based application designed to manage the inventory process. The project includes both backend and frontend components, providing a comprehensive solution for handling inventory data.

## Key Features

- **Inventory Data Management**: Ability to add, edit, and delete inventory records.
- **User Interface**: Intuitive frontend for easy navigation and interaction with the application.
- **Database**: Integration with a database for persistent data storage.

## Technologies and Skills Used

- **Java**: The main programming language used to implement application logic.
- **Spring Boot**: A framework for building the backend in Java.
- **Maven**: A tool for dependency management and backend build process.
- **JavaScript, HTML, CSS**: Frontend technologies used to develop the user interface.
- **Node.js & npm**: Tools for managing dependencies and running the frontend application.
- **MariaDB**: The database management system used for storing application data.
- **TeX**: Used for creating project documentation.
- **Git Version Control System**: Managing source code and team collaboration.
- **Project Structure**: Organized into modules such as `front-end`, `inventarisation`, `media`, and `sql`, demonstrating structured application development.

## Directory Structure

```
.
├── front-end/           # User interface-related files
├── inventarisation/     # Application logic and inventory functionalities
├── media/               # Multimedia resources used in the application
├── sql/                 # SQL scripts for database management
└── Documentation.pdf    # Project documentation
```

## Installation and Setup

1. **Clone the repository**:
   ```bash
   git clone https://github.com/Ciamcioo/Inventarisation-T4.git
   cd Inventarisation-T4
   ```

2. **Start the backend** (Spring Boot, Maven):
   ```bash
   cd inventarisation
   mvn clean install
   mvn spring-boot:run
   ```

3. **Start the frontend** (Node.js, npm):
   ```bash
   cd front-end
   npm install
   npm start
   ```

4. **Database Configuration** (MariaDB):
    - Ensure MariaDB is installed and running.
    - Create a database and load the schema from the `sql/` folder.
    - Configure the database connection in the backend configuration file (e.g., `application.properties`).

## Documentation

Detailed database documentation is available in the `Dokumentacja_Bazy_Danych.tex` file in the project's root directory.

## Authors

The project was developed by:
- [Ciamcioo](https://github.com/Ciamcioo)
- [kornelu123](https://github.com/kornelu123)
- [husxk](https://github.com/husxk)

## License

This project is licensed under the MIT License. See the LICENSE file for details.
