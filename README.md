# Bus Reservation System 🚌

A comprehensive web-based bus ticket booking system developed during my 2nd year, 2nd semester project. This system provides a complete solution for bus ticket reservations with user authentication, booking management, and administrative features.

## 🌐 Live Demo

� **[Try it Live!](https://project.anirudh.engineer/EasyBus)** - Experience the Bus Reservation System in action

📢 **Note:** This application is self-hosted and may not always be available online. 

🔗 **Need Help or Demo Unavailable?** Connect with me on [LinkedIn](https://www.linkedin.com/in/k-m-g-anirudh/) and I'll be happy to give you a personalized live demonstration of the system!

## 🚀 Features

### User Features
- **User Registration & Login** - Secure user authentication system
- **Bus Search & Booking** - Search available buses and book tickets
- **Booking History** - View past and current bookings
- **Password Reset** - Secure password recovery system

### Admin Features
- **Admin Dashboard** - Comprehensive administrative panel
- **Ticket Management** - View and manage all ticket bookings
- **User Management** - Monitor user activities and registrations

### General Features
- **Responsive Design** - Works seamlessly on desktop and mobile devices

## 🛠️ Technologies Used

### Frontend
- **HTML5** - Structure and content
- **CSS3** - Styling and responsive design
- **JavaScript** - Interactive user interface and client-side validation

### Backend
- **Java Servlets** - Server-side logic and request handling
- **JSP (JavaServer Pages)** - Dynamic web page generation

### Database
- **MySQL** - Relational database for data storage
- **JDBC** - Database connectivity

### Build Tools
- **Maven** - Project management and dependency resolution
- **Apache Tomcat** - Web server and servlet container

## 📁 Project Structure

```
Bus_Reservation_System/
├── src/
│   └── main/
│       ├── java/              # Java Servlets
│       │   ├── BookingServlet.java
│       │   ├── LoginVerification.java
│       │   ├── SignupServlet.java
│       │   ├── HistoryRetrieval.java
│       │   └── ...
│       └── webapp/            # Web Resources
│           ├── *.html         # HTML pages
│           ├── *.css          # Stylesheets
│           ├── images/        # Project images
│           └── WEB-INF/       # Configuration files
├── target/                    # Compiled classes
├── Jenkinsfile               # CI/CD pipeline configuration
├── pom.xml                   # Maven configuration
└── README.md                 # Project documentation
```

## 🗄️ Database Schema

The system uses MySQL database with the following main entities:
- **Users** - User authentication and profile information
- **Buses** - Bus details and route information
- **Bookings** - Ticket booking records
- **Routes** - Bus route and schedule data

## 🚦 Getting Started

### Prerequisites
- Java JDK 8 or higher
- Apache Tomcat 9.0+
- MySQL 8.0+
- Maven 3.6+
- IDE (Eclipse/IntelliJ IDEA)

### Installation Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/anirudh645/Bus_Reservation_System.git
   cd Bus_Reservation_System
   ```

2. **Set up MySQL Database**
   ```sql
   CREATE DATABASE bus_reservation;
   -- Import your database schema here
   ```

3. **Configure Database Connection**
   - Update database credentials in your servlet configuration
   - Ensure MySQL connector is included in dependencies

4. **Build the Project**
   ```bash
   mvn clean compile
   ```

5. **Deploy to Tomcat**
   ```bash
   mvn package
   # Deploy the generated WAR file to Tomcat webapps directory
   ```

6. **Access the Application**
   - Open your browser and navigate to: `http://localhost:8080/Bus_Reservation_System`


### Home Page
- Clean and intuitive interface for bus search
- Featured routes and promotional content

### Booking Interface
- Easy-to-use booking form

### User Dashboard
- Personal booking history
- Quick rebooking options

## 🔧 Configuration

### Database Configuration
Update the database connection parameters in your servlet files:
```java
String url = "jdbc:mysql://localhost:3306/bus_reservation";
String username = "your_username";
String password = "your_password";
```

### Server Configuration
Ensure Tomcat is configured with proper memory settings for optimal performance.

## 🤝 Contributing

This project was developed as an academic assignment. While it's not actively maintained, you're welcome to:
- Fork the repository
- Submit bug reports
- Suggest improvements
- Use it as a reference for your own projects

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

## 👨‍💻 Author

**Anirudh**
- GitHub: [@anirudh645](https://github.com/anirudh645)
- Project: [Bus Reservation System](https://github.com/anirudh645/Bus_Reservation_System)

## 📚 Academic Context

This project was developed during the 2nd year, 2nd semester as part of the curriculum to demonstrate:
- Full-stack web development skills
- Database design and integration
- Server-side programming with Java
- User interface design and implementation
- Project management and version control


---

⭐ **If you find this project helpful, please consider giving it a star!**

*Developed with ❤️ as part of academic curriculum*
