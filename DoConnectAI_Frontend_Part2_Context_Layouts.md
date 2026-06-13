# DoConnect AI Frontend - Part 2 (Context, Hooks, Layouts, Routing)

## src/context/AuthContext.jsx

```javascript
import { createContext, useState } from "react";

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);

  const login = (token, userData) => {
    localStorage.setItem("token", token);
    localStorage.setItem("user", JSON.stringify(userData));
    setUser(userData);
  };

  const logout = () => {
    localStorage.clear();
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
```

## src/context/ThemeContext.jsx

```javascript
import { createContext } from "react";

export const ThemeContext = createContext();

export const ThemeProvider = ({ children }) => {
  return (
    <ThemeContext.Provider value={{}}>
      {children}
    </ThemeContext.Provider>
  );
};
```

## src/hooks/useAuth.js

```javascript
import { useContext } from "react";
import { AuthContext } from "../context/AuthContext";

export const useAuth = () => {
  return useContext(AuthContext);
};
```

## src/routes/ProtectedRoute.jsx

```javascript
import { Navigate } from "react-router-dom";

export default function ProtectedRoute({ children }) {
  const token = localStorage.getItem("token");
  return token ? children : <Navigate to="/login" />;
}
```

## src/layouts/MainLayout.jsx

```javascript
export default function MainLayout({ children }) {
  return <>{children}</>;
}
```

## src/layouts/AuthLayout.jsx

```javascript
export default function AuthLayout({ children }) {
  return (
    <div className="min-h-screen bg-slate-100">
      {children}
    </div>
  );
}
```

## src/layouts/DashboardLayout.jsx

```javascript
import Navbar from "../components/common/Navbar";
import Sidebar from "../components/common/Sidebar";

export default function DashboardLayout({ children }) {
  return (
    <div className="flex">
      <Sidebar />
      <div className="flex-1">
        <Navbar />
        <div className="p-6">{children}</div>
      </div>
    </div>
  );
}
```
## src/App.js

```javascript
import { BrowserRouter, Routes, Route } from "react-router-dom";

import LandingPage from "./pages/Landing/LandingPage";
import LoginPage from "./pages/Auth/LoginPage";
import RegisterPage from "./pages/Auth/RegisterPage";
import DashboardPage from "./pages/Dashboard/DashboardPage";

import ProtectedRoute from "./routes/ProtectedRoute";

function App() {
  return (
    <BrowserRouter>
      <Routes>

        <Route path="/" element={<LandingPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />

        <Route
          path="/dashboard"
          element={
            <ProtectedRoute>
              <DashboardPage />
            </ProtectedRoute>
          }
        />

      </Routes>
    </BrowserRouter>
  );
}

export default App;
```

## src/index.js

```javascript
import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";

import App from "./App";

import { AuthProvider } from "./context/AuthContext";

const root = ReactDOM.createRoot(
 document.getElementById("root")
);

root.render(
 <AuthProvider>
   <App />
 </AuthProvider>
);
```
