# DoConnect AI Remaining Files Pack B

## src/api/analyticsApi.js

```javascript
import axiosInstance from "./axiosConfig";

export const getAnalytics = () =>
  axiosInstance.get("/analytics/dashboard");

export const getTrendingTopics = () =>
  axiosInstance.get("/analytics/trending-topics");

export const getUserActivity = () =>
  axiosInstance.get("/analytics/user-activity");
```

## src/services/analyticsService.js

```javascript
import * as api from "../api/analyticsApi";

export const getAnalytics = async () =>
 (await api.getAnalytics()).data;

export const getTrendingTopics = async () =>
 (await api.getTrendingTopics()).data;

export const getUserActivity = async () =>
 (await api.getUserActivity()).data;
```

## src/components/notifications/NotificationBell.jsx

```javascript
export default function NotificationBell({ count }) {
  return (
    <div className="relative">
      🔔
      <span className="absolute -top-2 -right-2 bg-red-500 text-white rounded-full px-2">
        {count}
      </span>
    </div>
  );
}
```

## src/components/notifications/NotificationCard.jsx

```javascript
export default function NotificationCard({ notification }) {
  return (
    <div className="bg-white shadow rounded-xl p-4">
      <h4>{notification.title}</h4>
      <p>{notification.message}</p>
    </div>
  );
}
```

## src/components/analytics/AnalyticsCards.jsx

```javascript
export default function AnalyticsCards({ title, value }) {
  return (
    <div className="bg-white rounded-xl shadow p-5">
      <h3>{title}</h3>
      <h2>{value}</h2>
    </div>
  );
}
```

## src/components/analytics/TrendingTopics.jsx

```javascript
export default function TrendingTopics({ topics }) {
  return (
    <ul>
      {topics?.map((topic, index) => (
        <li key={index}>{topic}</li>
      ))}
    </ul>
  );
}
```

## src/components/analytics/UserActivityChart.jsx

```javascript
export default function UserActivityChart() {
  return (
    <div className="bg-white rounded-xl shadow p-5">
      User Activity Chart
    </div>
  );
}
```

## src/components/profile/ProfileCard.jsx

```javascript
export default function ProfileCard({ user }) {
  return (
    <div className="bg-white rounded-xl shadow p-5">
      <h2>{user?.name}</h2>
      <p>{user?.email}</p>
    </div>
  );
}
```

## src/components/profile/ProfileStats.jsx

```javascript
export default function ProfileStats({ stats }) {
  return (
    <div className="grid grid-cols-3 gap-4">
      <div>{stats?.questions}</div>
      <div>{stats?.answers}</div>
      <div>{stats?.reputation}</div>
    </div>
  );
}
```

## src/components/moderation/ModerationForm.jsx

```javascript
import { useState } from "react";

export default function ModerationForm({ onSubmit }) {
  const [content, setContent] = useState("");

  return (
    <div>
      <textarea
        className="w-full border p-3 rounded"
        rows="5"
        onChange={(e) => setContent(e.target.value)}
      />

      <button
        onClick={() => onSubmit(content)}
        className="bg-indigo-600 text-white px-4 py-2 rounded mt-3"
      >
        Check
      </button>
    </div>
  );
}
```

## src/components/moderation/ModerationResult.jsx

```javascript
export default function ModerationResult({ result }) {
  return (
    <div className="bg-white p-4 rounded shadow mt-4">
      {result}
    </div>
  );
}
```
