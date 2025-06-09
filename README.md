# 🎮 GamingHub

**A next‑gen social gaming platform** where you and your friends can connect, chat, and play—all from a single, secure, cross‑platform ecosystem!

---

## 🚀 What is GamingHub?

GamingHub unifies your gaming life:
- **Multi‑platform**: Web, Android (Kotlin), iOS (Swift)  
- **One‑click login** via Google OAuth 2.0  
- **End‑to‑end encryption** for chats & group invites  
- **Persistent sessions** with encrypted credentials (Jetpack DataStore on Android)  
- **Lightweight REST API** built with Spring Boot  
- **Library of social games** (“Impostors”, “Secret Santa 2.0”)  

---

## 🗂️ Repo Structure

```
/FrontEnd
  /AndroidApp
    └── com/example/gaminghub
        ├── data/          # remote/data sources + security + repos
        ├── domain/        # use‑cases & business logic
        └── framework/     # UI: pantallaLogin, pantallaHome, pantallaSocial, etc.
/BackEnd
  ├── src/main/java/com/example/gaminghub
  │   ├── controller/    # AuthController, FriendController
  │   ├── entity/        # User, Game, Match, Message…
  │   ├── repository/    # Spring Data JPA interfaces
  │   └── service/       # OAuth, SMS simulation, user management
  └── src/main/resources
      └── application.properties
```

---

## 📦 Features & Data Model

### Core Endpoints
- **Auth**: `/api/auth/google`, `/api/auth/sms` (simulated)  
- **Users**: CRUD + friends list  
- **Games**: list, invite, join  
- **Chat**: send/receive encrypted messages  

### Main Entities

| Entity       | Key Fields                                             |
| ------------ | ------------------------------------------------------ |
| **User**     | `id`, `username`, `email`, `googleId`, `createdAt`    |
| **Game**     | `id`, `name`, `description`, `rules`                  |
| **Match**    | `id`, `hostId`, `gameId`, `status`, `createdAt`       |
| **Friend**   | `userId`, `friendId`, `status`, `requestedAt`         |
| **Message**  | `chatId`, `senderId`, `content`, `sentAt`, `isSystem` |

---

## 🛠️ Tech Stack

- **Backend**: Java 17 + Spring Boot 3, Spring Security OAuth2, JPA (MySQL)  
- **Frontend (Android)**: Kotlin, Jetpack DataStore + AndroidX Security, Hilt DI  
- **(Planned)**: JWT access & refresh tokens, SMS via Twilio/Firebase, real‑time chat with WebSockets  
- **Docs & Tests**: Javadoc, JUnit + Mockito, Markdown in `/docs`

---

## ⚙️ Getting Started

1. **Clone**  
   ```bash
   git clone https://github.com/YourUser/GamingHub.git
   cd GamingHub
   ```
2. **Backend**  
   - Edit `BackEnd/src/main/resources/application.properties` → your MySQL creds  
   - Run with Maven/Gradle:  
     ```bash
     ./mvnw spring-boot:run
     ```
3. **Android Client**  
   - Open `FrontEnd/AndroidApp/GamingHubAndroidApp` in Android Studio  
   - Build & Run on emulator or device  
4. **Explore**  
   - Use Postman or your UI to hit `/api/auth/google` and the other endpoints  
   - Check the Android logcat for DataStore token storage  

---

## 📄 License & Contributing

Feel free to file issues or submit PRs! This project is MIT‑licensed—see [LICENSE](LICENSE) for details.

---

### Let’s make gaming social again! 🕹️✨
