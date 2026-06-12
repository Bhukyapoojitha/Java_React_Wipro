# Phase 1 - DoConnect AI Frontend Setup & Routing

## Tech Stack

- Create React App
- Tailwind CSS
- React Router DOM
- Axios
- Context API

## Create Project

```bash
npx create-react-app doconnect-ai-frontend
cd doconnect-ai-frontend
```

## Install Dependencies

```bash
npm install react-router-dom axios
npm install -D tailwindcss postcss autoprefixer
npx tailwindcss init -p
```

## Folder Structure

```text
src
├── api
│   └── axiosConfig.js
├── context
│   └── AuthContext.js
├── routes
│   └── ProtectedRoute.js
├── pages
│   ├── landing
│   ├── auth
│   └── dashboard
├── layouts
├── services
├── App.js
└── index.js
```

## Tailwind Theme

Primary: #4F46E5
Secondary: #7C3AED
Accent: #06B6D4

## Axios Configuration

File: src/api/axiosConfig.js

```javascript
import axios from "axios";

const axiosInstance = axios.create({
  baseURL: "http://localhost:8080"
});

axiosInstance.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

export default axiosInstance;
```

## Auth Context

File: src/context/AuthContext.js

```javascript
import { createContext, useState } from "react";

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] =
    useState(!!localStorage.getItem("token"));

  const login = (token) => {
    localStorage.setItem("token", token);
    setIsAuthenticated(true);
  };

  const logout = () => {
    localStorage.removeItem("token");
    setIsAuthenticated(false);
  };

  return (
    <AuthContext.Provider
      value={{ isAuthenticated, login, logout }}
    >
      {children}
    </AuthContext.Provider>
  );
};
```

## Protected Route

File: src/routes/ProtectedRoute.js

```javascript
import { Navigate } from "react-router-dom";

const ProtectedRoute = ({ children }) => {
  const token = localStorage.getItem("token");

  return token ? children : <Navigate to="/login" />;
};

export default ProtectedRoute;
```

## Routes

Public:
- /
- /login
- /register

Protected:
- /dashboard

## Next Phase

Landing Page + Login + Register UI
