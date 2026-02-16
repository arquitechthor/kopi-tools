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
   git clone https://github.com/arquitechthor/kopi-tools.git
   cd kopi-tools
   ```

2. **Build the project**:
   **Windows**:
   ```bash
   .\mvnw.cmd clean package
   ```
   **Linux/Mac**:
   ```bash
   ./mvnw clean package
   ```

3. **Run the application**:
   
   **Option A: Using Java (Recommended)**
   This method is more robust against path issues on Windows.
   ```bash
   java -jar target/kopi-tools-0.0.1-SNAPSHOT.jar
   ```

   **Option B: Using Maven Wrapper**
   **Windows**:
   ```bash
   .\mvnw.cmd spring-boot:run
   ```
   **Linux/Mac**:
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
### AWS Synchronization (Optional)
To enable S3 backup synchronization with Cognito authentication, set the following environment variables:

**AWS S3 Config**:
- `AWS_ACCESS_KEY_ID`: Your AWS Access Key.
- `AWS_SECRET_ACCESS_KEY`: Your AWS Secret Key.
- `AWS_REGION`: AWS Region (default: `eu-west-1`).
- `S3_BUCKET_NAME`: The S3 bucket name for backups.

**Cognito Config**:
- `COGNITO_CLIENT_ID`: App Client ID from User Pool.
- `COGNITO_CLIENT_SECRET`: App Client Secret.
- `COGNITO_ISSUER_URI`: Issuer URI (e.g., `https://cognito-idp.eu-west-1.amazonaws.com/eu-west-1_xxxxxx`).

## 📝 Usage

1. **Home**: Dashboard showing all links grouped by category.
2. **Tools > Links Manager**: Manage your links.
3. **Add Link**: Click "Add Link" to save a new URL.

---
Created by [Héctor Guzmán/Arquitechthor]
