# Phase 6 - Analytics, Moderation, AI Tools & Profile

## Modules Included

- Analytics Dashboard
- Moderation Page
- AI Tools Page
- Profile Page
- Service Layer
- Routes

---

# Folder Structure

```text
src
├── services
│   ├── analyticsService.js
│   ├── moderationService.js
│   ├── aiService.js
│   └── profileService.js
│
├── pages
│   ├── analytics
│   │   └── AnalyticsPage.jsx
│   ├── moderation
│   │   └── ModerationPage.jsx
│   ├── ai
│   │   └── AIToolsPage.jsx
│   └── profile
│       └── ProfilePage.jsx
```

---

# Analytics Service

## analyticsService.js

```javascript
import axiosInstance from "../api/axiosConfig";

export const getDashboardStats = () => {
  return axiosInstance.get("/analytics/dashboard");
};
```

---

# Analytics Page

## AnalyticsPage.jsx

```jsx
import { useEffect, useState } from "react";
import { getDashboardStats }
from "../../services/analyticsService";

export default function AnalyticsPage() {

  const [stats, setStats] = useState({});

  useEffect(() => {
    loadAnalytics();
  }, []);

  const loadAnalytics = async () => {

    const response =
      await getDashboardStats();

    setStats(response.data);
  };

  return (

    <div className="p-6">

      <h1 className="text-3xl font-bold mb-6">
        Analytics Dashboard
      </h1>

      <div className="grid md:grid-cols-3 gap-6">

        <div className="bg-white p-5 rounded-xl shadow">
          <h3>Total Users</h3>
          <h2 className="text-3xl font-bold">
            {stats.totalUsers}
          </h2>
        </div>

        <div className="bg-white p-5 rounded-xl shadow">
          <h3>Total Questions</h3>
          <h2 className="text-3xl font-bold">
            {stats.totalQuestions}
          </h2>
        </div>

        <div className="bg-white p-5 rounded-xl shadow">
          <h3>Total Answers</h3>
          <h2 className="text-3xl font-bold">
            {stats.totalAnswers}
          </h2>
        </div>

      </div>

    </div>
  );
}
```

---

# Moderation Service

## moderationService.js

```javascript
import axiosInstance from "../api/axiosConfig";

export const moderateContent = (data) => {
  return axiosInstance.post(
    "/moderation/check",
    data
  );
};
```

---

# Moderation Page

## ModerationPage.jsx

```jsx
import { useState } from "react";
import { moderateContent }
from "../../services/moderationService";

export default function ModerationPage() {

  const [text, setText] = useState("");
  const [result, setResult] = useState("");

  const analyze = async () => {

    const response =
      await moderateContent({
        textContext: text
      });

    setResult(response.data.message);
  };

  return (

    <div className="p-6">

      <h1 className="text-3xl font-bold mb-6">
        Content Moderation
      </h1>

      <textarea
        rows="6"
        className="w-full border p-4 rounded"
        onChange={(e) =>
          setText(e.target.value)
        }
      />

      <button
        onClick={analyze}
        className="mt-4 bg-indigo-600
        text-white px-6 py-3 rounded"
      >
        Analyze
      </button>

      <p className="mt-4 font-semibold">
        {result}
      </p>

    </div>
  );
}
```

---

# AI Service

## aiService.js

```javascript
import axiosInstance from "../api/axiosConfig";

export const summarizeText = (data) => {
  return axiosInstance.post(
    "/ai/summarize",
    data
  );
};

export const suggestAnswer = (id) => {
  return axiosInstance.get(
    `/ai/suggest-answer/${id}`
  );
};
```

---

# AI Tools Page

## AIToolsPage.jsx

```jsx
import { useState } from "react";
import { summarizeText }
from "../../services/aiService";

export default function AIToolsPage() {

  const [text, setText] = useState("");
  const [summary, setSummary] =
    useState("");

  const generateSummary = async () => {

    const response =
      await summarizeText({
        textContext: text
      });

    setSummary(response.data.payload);
  };

  return (

    <div className="p-6">

      <h1 className="text-3xl font-bold mb-6">
        AI Tools
      </h1>

      <textarea
        rows="6"
        className="w-full border p-4 rounded"
        onChange={(e) =>
          setText(e.target.value)
        }
      />

      <button
        onClick={generateSummary}
        className="mt-4 bg-indigo-600
        text-white px-6 py-3 rounded"
      >
        Generate Summary
      </button>

      <div className="mt-6 bg-white p-4 rounded shadow">

        {summary}

      </div>

    </div>
  );
}
```

---

# Profile Service

## profileService.js

```javascript
import axiosInstance from "../api/axiosConfig";

export const getProfile = () => {
  return axiosInstance.get("/users/profile");
};
```

---

# Profile Page

## ProfilePage.jsx

```jsx
import { useEffect, useState }
from "react";

import { getProfile }
from "../../services/profileService";

export default function ProfilePage() {

  const [profile, setProfile] =
    useState({});

  useEffect(() => {
    loadProfile();
  }, []);

  const loadProfile = async () => {

    const response =
      await getProfile();

    setProfile(response.data);
  };

  return (

    <div className="p-6">

      <div className="bg-white p-6 rounded-xl shadow">

        <h2 className="text-2xl font-bold">
          {profile.name}
        </h2>

        <p>{profile.email}</p>

        <p>{profile.role}</p>

      </div>

    </div>
  );
}
```

---

# Routes

```jsx
<Route path="/analytics"
element={<AnalyticsPage />} />

<Route path="/moderation"
element={<ModerationPage />} />

<Route path="/ai-tools"
element={<AIToolsPage />} />

<Route path="/profile"
element={<ProfilePage />} />
```

---

# Frontend Completion

✅ Landing Page

✅ Login

✅ Register

✅ Dashboard

✅ Questions

✅ Answers

✅ Chat

✅ Notifications

✅ Analytics

✅ Moderation

✅ AI Tools

✅ Profile

---

# Next

Backend API endpoint alignment
Testing
Deployment
