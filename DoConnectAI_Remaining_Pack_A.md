# DoConnect AI Remaining Files Pack - Part A

## Controllers Found In Backend ZIP

- DoConnectAI-main/doconnect_backend/src/main/java/com/doconnect/controller/AiController.java
- DoConnectAI-main/doconnect_backend/src/main/java/com/doconnect/controller/AnalyticsController.java
- DoConnectAI-main/doconnect_backend/src/main/java/com/doconnect/controller/AnswerController.java
- DoConnectAI-main/doconnect_backend/src/main/java/com/doconnect/controller/AuthController.java
- DoConnectAI-main/doconnect_backend/src/main/java/com/doconnect/controller/ChatController.java
- DoConnectAI-main/doconnect_backend/src/main/java/com/doconnect/controller/ModerationController.java
- DoConnectAI-main/doconnect_backend/src/main/java/com/doconnect/controller/NotificationController.java
- DoConnectAI-main/doconnect_backend/src/main/java/com/doconnect/controller/QuestionController.java
- DoConnectAI-main/doconnect_backend/src/main/java/com/doconnect/controller/UserController.java


## src/api/chatApi.js

```javascript
import axiosInstance from "./axiosConfig";

export const getMessages = () => axiosInstance.get("/chat/messages");
export const sendMessage = (data) => axiosInstance.post("/chat/send", data);
```

## src/api/notificationApi.js

```javascript
import axiosInstance from "./axiosConfig";

export const getNotifications = () =>
 axiosInstance.get("/notifications");
```
## src/services/chatService.js

```javascript
import * as api from "../api/chatApi";

export const getMessages = async()=> (await api.getMessages()).data;
export const sendMessage = async(data)=> (await api.sendMessage(data)).data;
```
## src/components/chat/ChatBox.jsx

```javascript
export default function ChatBox({children}){
 return <div className="bg-white rounded-xl p-4 shadow">{children}</div>;
}
```
