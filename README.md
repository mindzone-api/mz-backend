# Mindzone Backend
Welcome to the MindZone backend repository! This project is a MVC Spring Boot mental health management platform designed to streamline the interactions between patients, psychologists, and psychiatrists. The backend is responsible for handling business logic, database management, authentication, and integration with third-party services such as Stripe for payments and RabbitMQ for asynchronous email notifications.

### Technologies
The backend of MindZone is built with the following technologies:

- MongoDB
- OAuth2 with Google
- RabbitMQ (manages asynchronous communication for email notifications)
- Redis
- Stripe (Payment gateway for managing subscriptions) [WIP]
- JUnit & Mockito [WIP]

### Main Features
- User Registration & Authentication: OAuth2 integration with Google for secure login.
- Professional Subscriptions: Integrated with Stripe for managing subscription-based access to the platform.
- Session Scheduling & History: Allows professionals and patients to manage therapy sessions and review historical data.
- Chat communication between patients, psychologists, and psychiatrists.
- Task & Prescription Management: Features for handling therapy tasks and medical prescriptions.
- Email Notifications: Asynchronous notifications using RabbitMQ.

### Artifacts
[here](https://drive.google.com/drive/folders/1ixHqzBN6pkNcFI-PvrLeWtpIbmv76q8s?usp=sharing) are the mais artifacts (in brazilian portuguese) used for the code development

### License
This project is licensed under the MIT License. See the LICENSE file for details.