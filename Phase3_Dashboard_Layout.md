# Phase 3 - Dashboard Layout (CRM Style)

## Objective

Create a professional dashboard inspired by modern CRM applications.

## Folder Structure

```text
src
├── layouts
│   └── DashboardLayout.jsx
├── components
│   ├── Sidebar.jsx
│   ├── TopNavbar.jsx
│   └── StatCard.jsx
└── pages
    └── dashboard
        └── DashboardPage.jsx
```

---

## Sidebar.jsx

```jsx
import { Link } from "react-router-dom";

export default function Sidebar() {
  return (
    <div className="w-64 h-screen bg-indigo-700 text-white p-5">

      <h1 className="text-2xl font-bold mb-8">
        DoConnect AI
      </h1>

      <div className="flex flex-col gap-4">

        <Link to="/dashboard">Dashboard</Link>

        <Link to="/questions">Questions</Link>

        <Link to="/ask-question">Ask Question</Link>

        <Link to="/notifications">Notifications</Link>

        <Link to="/chat">Chat</Link>

        <Link to="/analytics">Analytics</Link>

        <Link to="/moderation">Moderation</Link>

        <Link to="/ai-tools">AI Tools</Link>

        <Link to="/profile">Profile</Link>

      </div>
    </div>
  );
}
```

---

## TopNavbar.jsx

```jsx
export default function TopNavbar() {

  return (
    <div className="bg-white shadow p-4 flex justify-between">

      <h2 className="font-semibold text-xl">
        Dashboard
      </h2>

      <div>
        Welcome User
      </div>

    </div>
  );
}
```

---

## StatCard.jsx

```jsx
export default function StatCard({ title, value }) {

  return (

    <div className="bg-white rounded-xl shadow p-5">

      <h3 className="text-slate-500">
        {title}
      </h3>

      <h2 className="text-3xl font-bold mt-2">
        {value}
      </h2>

    </div>

  );
}
```
---

## DashboardLayout.jsx

```jsx
import Sidebar from "../components/Sidebar";
import TopNavbar from "../components/TopNavbar";

export default function DashboardLayout({ children }) {

  return (

    <div className="flex">

      <Sidebar />

      <div className="flex-1 bg-slate-100 min-h-screen">

        <TopNavbar />

        <div className="p-6">

          {children}

        </div>

      </div>

    </div>
  );
}
```
---

## DashboardPage.jsx

```jsx
import DashboardLayout from "../../layouts/DashboardLayout";
import StatCard from "../../components/StatCard";

export default function DashboardPage() {

  return (

    <DashboardLayout>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">

        <StatCard
          title="Questions"
          value="10"
        />

        <StatCard
          title="Answers"
          value="2"
        />

        <StatCard
          title="Users"
          value="3"
        />

        <StatCard
          title="Notifications"
          value="5"
        />

      </div>

      <div className="mt-8 bg-white p-6 rounded-xl shadow">

        <h2 className="text-xl font-semibold mb-4">
          Recent Activity
        </h2>

        <ul className="space-y-3">

          <li>New Question Added</li>

          <li>Answer Submitted</li>

          <li>Notification Sent</li>

        </ul>

      </div>

    </DashboardLayout>

  );
}
```
---

## Route Configuration

Add in App.js

```jsx
<Route
  path="/dashboard"
  element={
    <ProtectedRoute>
      <DashboardPage />
    </ProtectedRoute>
  }
/>
```

---

## Dashboard Features

- Responsive Sidebar
- CRM Style Layout
- Statistics Cards
- Recent Activity Section
- Protected Route

---

## Next Phase

Questions Module
Answers Module
Question Details
Ask Question Form
