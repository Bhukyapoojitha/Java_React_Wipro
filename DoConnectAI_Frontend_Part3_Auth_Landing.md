# DoConnect AI Frontend - Part 3 (Landing, Auth & Common Components)

## src/components/common/Navbar.jsx

```javascript
import { Link } from "react-router-dom";

export default function Navbar() {
  return (
    <nav className="flex justify-between items-center px-8 py-4 bg-white shadow">
      <h1 className="text-2xl font-bold text-indigo-600">
        DoConnect AI
      </h1>

      <div className="space-x-4">
        <Link to="/login">Login</Link>
        <Link to="/register">Register</Link>
      </div>
    </nav>
  );
}
```

## src/components/common/Footer.jsx

```javascript
export default function Footer() {
  return (
    <footer className="bg-slate-900 text-white text-center p-4">
      © 2026 DoConnect AI
    </footer>
  );
}
```

## src/components/common/Sidebar.jsx

```javascript
import { Link } from "react-router-dom";

export default function Sidebar() {
  return (
    <div className="w-64 min-h-screen bg-indigo-700 text-white p-6">

      <h2 className="text-2xl font-bold mb-8">
        DoConnect AI
      </h2>

      <div className="flex flex-col gap-4">

        <Link to="/dashboard">Dashboard</Link>
        <Link to="/questions">Questions</Link>
        <Link to="/chat">Chat</Link>
        <Link to="/notifications">Notifications</Link>
        <Link to="/analytics">Analytics</Link>
        <Link to="/profile">Profile</Link>

      </div>

    </div>
  );
}
```

## src/components/common/Loader.jsx

```javascript
export default function Loader() {
  return (
    <div className="text-center p-10">
      Loading...
    </div>
  );
}
```

## src/pages/Landing/LandingPage.jsx

```javascript
import Navbar from "../../components/common/Navbar";
import Footer from "../../components/common/Footer";

export default function LandingPage() {

  return (
    <>

      <Navbar />

      <section className="min-h-screen bg-slate-50 flex flex-col justify-center items-center text-center px-6">

        <h1 className="text-6xl font-bold text-slate-900">
          Ask. Learn. Connect.
        </h1>

        <p className="mt-6 text-xl text-slate-600 max-w-3xl">

          DoConnect AI is a collaborative discussion platform
          where students and professionals can ask questions,
          share answers and learn together.

        </p>

      </section>

      <Footer />

    </>
  );
}
```

## src/pages/Auth/LoginPage.jsx

```javascript
import { useState } from "react";
import { login } from "../../services/authService";

export default function LoginPage() {

  const [email,setEmail] = useState("");
  const [password,setPassword] = useState("");

  const handleLogin = async () => {

    try{

      const response = await login({
        email,
        password
      });

      localStorage.setItem(
        "token",
        response.token
      );

      window.location.href="/dashboard";

    }catch(error){
      alert("Login Failed");
    }
  };

  return (

    <div className="min-h-screen flex items-center justify-center bg-slate-100">

      <div className="bg-white p-10 rounded-xl shadow w-full max-w-md">

        <h2 className="text-3xl font-bold mb-6">
          Welcome Back
        </h2>

        <input
          className="w-full border p-3 rounded mb-4"
          placeholder="Email"
          onChange={(e)=>setEmail(e.target.value)}
        />

        <input
          type="password"
          className="w-full border p-3 rounded mb-4"
          placeholder="Password"
          onChange={(e)=>setPassword(e.target.value)}
        />

        <button
          onClick={handleLogin}
          className="w-full bg-indigo-600 text-white p-3 rounded"
        >
          Login
        </button>

      </div>

    </div>
  );
}
```

## src/pages/Auth/RegisterPage.jsx

```javascript
import { useState } from "react";
import { register } from "../../services/authService";

export default function RegisterPage() {

  const [name,setName] = useState("");
  const [email,setEmail] = useState("");
  const [password,setPassword] = useState("");

  const handleRegister = async()=>{

    await register({
      name,
      email,
      password
    });

    alert("Registration Successful");
  };

  return (

    <div className="min-h-screen flex items-center justify-center bg-slate-100">

      <div className="bg-white p-10 rounded-xl shadow w-full max-w-lg">

        <h2 className="text-3xl font-bold mb-6">
          Create Account
        </h2>

        <input
          className="w-full border p-3 rounded mb-4"
          placeholder="Name"
          onChange={(e)=>setName(e.target.value)}
        />

        <input
          className="w-full border p-3 rounded mb-4"
          placeholder="Email"
          onChange={(e)=>setEmail(e.target.value)}
        />

        <input
          type="password"
          className="w-full border p-3 rounded mb-4"
          placeholder="Password"
          onChange={(e)=>setPassword(e.target.value)}
        />

        <button
          onClick={handleRegister}
          className="w-full bg-indigo-600 text-white p-3 rounded"
        >
          Register
        </button>

      </div>

    </div>
  );
}
```
