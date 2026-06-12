# Phase 2 - Landing Page + Login + Register

## Pages Included

- Landing Page
- Login Page
- Register Page
- Responsive Navbar
- Footer
- Tailwind Theme

## Landing Page Design

### Features Section

- AI Assisted Discussions
- Real-Time Chat
- Knowledge Sharing
- Analytics Dashboard
- Moderation System

### Navbar

```jsx
import { Link } from "react-router-dom";

export default function Navbar() {
  return (
    <nav className="flex justify-between items-center p-6 bg-white shadow">
      <h1 className="text-2xl font-bold text-indigo-600">
        DoConnect AI
      </h1>

      <div className="space-x-4">
        <Link to="/login">Login</Link>
        <Link
          to="/register"
          className="bg-indigo-600 text-white px-4 py-2 rounded"
        >
          Register
        </Link>
      </div>
    </nav>
  );
}
```

## LandingPage.jsx

Create:

```text
src/pages/landing/LandingPage.jsx
```

Hero Section:

```jsx
import { Link } from "react-router-dom";

export default function LandingPage() {
  return (
    <div className="min-h-screen bg-slate-50">
      <section className="text-center py-24">
        <h1 className="text-6xl font-bold text-slate-900">
          Ask. Learn. Connect.
        </h1>

        <p className="mt-6 text-xl text-slate-600">
          DoConnect AI is a modern discussion platform
          for students and professionals.
        </p>

        <div className="mt-8">
          <Link
            to="/register"
            className="bg-indigo-600 text-white px-8 py-4 rounded-xl"
          >
            Get Started
          </Link>
        </div>
      </section>
    </div>
  );
}
```

## Login Page

File:

```text
src/pages/auth/LoginPage.jsx
```

```jsx
import { useState } from "react";

export default function LoginPage() {

  const [email,setEmail] = useState("");
  const [password,setPassword] = useState("");

  return (
    <div className="min-h-screen flex items-center justify-center bg-slate-100">

      <div className="bg-white p-10 rounded-2xl shadow-lg w-full max-w-md">

        <h2 className="text-3xl font-bold mb-6">
          Welcome Back
        </h2>

        <input
          type="email"
          placeholder="Email"
          className="w-full border p-3 rounded mb-4"
        />

        <input
          type="password"
          placeholder="Password"
          className="w-full border p-3 rounded mb-4"
        />

        <button
          className="w-full bg-indigo-600 text-white p-3 rounded"
        >
          Login
        </button>

      </div>
    </div>
  );
}
```

## Register Page

File:

```text
src/pages/auth/RegisterPage.jsx
```

```jsx
export default function RegisterPage() {

  return (
    <div className="min-h-screen flex items-center justify-center bg-slate-100">

      <div className="bg-white p-10 rounded-2xl shadow-lg w-full max-w-lg">

        <h2 className="text-3xl font-bold mb-6">
          Create Account
        </h2>

        <input
          type="text"
          placeholder="Full Name"
          className="w-full border p-3 rounded mb-4"
        />

        <input
          type="email"
          placeholder="Email"
          className="w-full border p-3 rounded mb-4"
        />

        <input
          type="password"
          placeholder="Password"
          className="w-full border p-3 rounded mb-4"
        />

        <button
          className="w-full bg-indigo-600 text-white p-3 rounded"
        >
          Register
        </button>

      </div>
    </div>
  );
}
```

## App.js Routes

Add:

```jsx
<Route path="/" element={<LandingPage />} />
<Route path="/login" element={<LoginPage />} />
<Route path="/register" element={<RegisterPage />} />
```

## Phase 3

Dashboard Layout
Sidebar
Navbar
CRM Style Dashboard
