# Phase 5 - Chat & Notifications Module

## Modules Included

- Chat Page
- Notification Page
- Chat Service
- Notification Service
- WebSocket Structure
- Responsive UI

---

## Folder Structure

```text
src
├── services
│   ├── chatService.js
│   └── notificationService.js
│
├── pages
│   ├── chat
│   │   └── ChatPage.jsx
│   │
│   └── notifications
│       └── NotificationsPage.jsx
│
├── components
│   ├── ChatMessage.jsx
│   └── NotificationCard.jsx
```

---

# Chat Service

## chatService.js

```javascript
import axiosInstance from "../api/axiosConfig";

export const getMessages = () => {
  return axiosInstance.get("/chat/messages");
};

export const sendMessage = (message) => {
  return axiosInstance.post("/chat/send", message);
};
```

---

# Notification Service

## notificationService.js

```javascript
import axiosInstance from "../api/axiosConfig";

export const getNotifications = () => {
  return axiosInstance.get("/notifications");
};
```

---

# ChatMessage Component

## ChatMessage.jsx

```jsx
export default function ChatMessage({ message }) {

  return (
    <div className="bg-white rounded-xl shadow p-3 mb-3">

      <h4 className="font-semibold">
        {message.sender}
      </h4>

      <p>
        {message.content}
      </p>

    </div>
  );
}
```

---

# Chat Page

## ChatPage.jsx

```jsx
import { useEffect, useState } from "react";
import { getMessages } from "../../services/chatService";
import ChatMessage from "../../components/ChatMessage";

export default function ChatPage() {

  const [messages, setMessages] = useState([]);

  useEffect(() => {
    loadMessages();
  }, []);

  const loadMessages = async () => {

    const response = await getMessages();

    setMessages(response.data);
  };

  return (

    <div className="p-6">

      <h1 className="text-3xl font-bold mb-6">
        Chat Room
      </h1>

      <div className="bg-slate-100 rounded-xl p-4">

        {messages.map((message) => (
          <ChatMessage
            key={message.id}
            message={message}
          />
        ))}

      </div>

    </div>
  );
}
```

---

# Notification Card

## NotificationCard.jsx

```jsx
export default function NotificationCard({
  notification
}) {

  return (

    <div className="bg-white rounded-xl shadow p-4">

      <h3 className="font-semibold">
        {notification.title}
      </h3>

      <p>
        {notification.message}
      </p>

    </div>

  );
}
```

---

# Notifications Page

## NotificationsPage.jsx

```jsx
import { useEffect, useState } from "react";
import { getNotifications }
from "../../services/notificationService";

import NotificationCard
from "../../components/NotificationCard";

export default function NotificationsPage() {

  const [notifications, setNotifications] =
    useState([]);

  useEffect(() => {
    loadNotifications();
  }, []);

  const loadNotifications = async () => {

    const response =
      await getNotifications();

    setNotifications(response.data);
  };

  return (

    <div className="p-6">

      <h1 className="text-3xl font-bold mb-6">
        Notifications
      </h1>

      <div className="space-y-4">

        {notifications.map((notification) => (
          <NotificationCard
            key={notification.id}
            notification={notification}
          />
        ))}

      </div>

    </div>
  );
}
```

---

# WebSocket Integration Structure

Install:

```bash
npm install sockjs-client
npm install stompjs
```

Create:

```text
src/services/websocketService.js
```

```javascript
import SockJS from "sockjs-client";
import Stomp from "stompjs";

let stompClient = null;

export const connectSocket = () => {

  const socket =
    new SockJS("http://localhost:8080/ws");

  stompClient =
    Stomp.over(socket);

  stompClient.connect({}, () => {
    console.log("Connected");
  });
};
```

---

# Routes

Add to App.js

```jsx
<Route
  path="/chat"
  element={<ChatPage />}
/>

<Route
  path="/notifications"
  element={<NotificationsPage />}
/>
```

---

# Features Completed

✅ Chat UI

✅ Notification Center

✅ Notification Cards

✅ Chat Message Component

✅ WebSocket Structure

✅ Responsive Design

---

# Next Phase

Analytics Dashboard

Moderation Module

AI Tools Pages

Profile Page
