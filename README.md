# Kopi Tools

Kopi Tools is a Multi-tool application built with Java 21 and Spring Boot. It includes a Link Management System and allows extending with more tools.

## 🚀 Features

- **Link Manager**: Store, categorize, and manage web links.
- **Categorization**: Links are organized by categories with collapsible sections.
- **Quick Actions**:
  - 🔗 Open link in a new tab.
  - 👁️ View link details.
  - ✏️ Edit link information.
  - 🗑️ Delete link.
- **Responsive UI**: Built with Thymeleaf and Bootstrap.

## 🛠️ Technology Stack

- **Java**: 21 (LTS)
- **Framework**: Spring Boot 3.3.0
- **Template Engine**: Thymeleaf
- **Database**: H2 (File-based storage)
- **Frontend**: Bootstrap 5, FontAwesome
- **Build Tool**: Maven

## ⚙️ Setup & Installation

### Prerequisites
- JDK 25 installed.
- Maven installed (or use the provided wrapper).

### Running the Application

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd kopi-links
   ```

2. **Build the project**:
   ```bash
   ./mvnw clean install
   ```

3. **Run the application**:
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Access the application**:
    Open your browser and navigate to `http://localhost:8080`.

## 📂 Project Structure

- `src/main/java`: Backend source code (MVC architecture).
- `src/main/resources/templates`: Thymeleaf HTML templates.
- `src/main/resources/application.properties`: Configuration.

## 🔒 Configuration

The application uses an H2 file-based database stored in `./data`. 
**Note**: The database files are excluded from version control for security.

## 📝 Usage

1. **Home**: Dashboard showing all links grouped by category.
2. **Tools > Links Manager**: Manage your links.
3. **Add Link**: Click "Add Link" to save a new URL.

---
Created by [Your Name/Organization]
