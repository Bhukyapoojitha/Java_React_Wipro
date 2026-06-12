# DoConnect AI – Unified Production-Ready Frontend Codebase
### Architecture Schema: Modular Domain Directory Separation (Vite + React 18 + Tailwind CSS)

Save this comprehensive code file document directly to your computer. It contains every module, script, layer, context slice, and screen template fully expanded with no code abbreviations.

---

## 1. Project Scaffolding & Configuration Environment

### `package.json`
```json
{
  "name": "doconnect-frontend-app",
  "private": true,
  "version": "1.0.0",
  "type": "module",
  "scripts": {
    "dev": "vite",
    "build": "vite build",
    "preview": "vite preview"
  },
  "dependencies": {
    "@stomp/stompjs": "^7.0.0",
    "axios": "^1.6.8",
    "react": "^18.3.1",
    "react-dom": "^18.3.1",
    "react-router-dom": "^6.23.1",
    "sockjs-client": "^1.6.1"
  },
  "devDependencies": {
    "@vitejs/plugin-react": "^4.3.0",
    "autoprefixer": "^10.4.19",
    "postcss": "^8.4.38",
    "tailwindcss": "^3.4.3",
    "vite": "^5.2.11"
  }
}
```

### `vite.config.js`
```javascript
import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  server: {
    port: 3000,
    open: true
  }
});
```

### `tailwind.config.js`
```javascript
/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,jsx,ts,tsx}"
  ],
  theme: {
    extend: {},
  },
  plugins: [],
}
```

### `postcss.config.js`
```javascript
export default {
  plugins: {
    tailwindcss: {},
    autoprefixer: {},
  },
}
```

### `index.html`
```html
<!doctype html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>DoConnect AI Platform</title>
  </head>
  <body class="bg-gray-50 text-gray-800 font-sans">
    <div id="root"></div>
    <script type="module" src="/src/main.jsx"></script>
  </body>
</html>
```

### `src/index.css`
```css
@tailwind base;
@tailwind components;
@tailwind utilities;

body {
  margin: 0;
  overflow-x: hidden;
}
```

---

## 2. Core Operational Root Entry Points

### `src/main.jsx`
```jsx
import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import './index.css';

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);
```

### `src/App.jsx`
```jsx
import React from 'react';
import { AuthProvider } from './context/AuthContext';
import { QuestionProvider } from './context/QuestionContext';
import { ChatProvider } from './context/ChatContext';
import AppRoutes from './routes/AppRoutes';

function App() {
  return (
    <AuthProvider>
      <QuestionProvider>
        <ChatProvider>
          <AppRoutes />
        </ChatProvider>
      </QuestionProvider>
    </AuthProvider>
  );
}

export default App;
```

---

## 3. Package Structure Layer: `src/api`

### `src/api/axiosConfig.js`
```javascript
import axios from 'axios';

const API = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json'
  }
});

API.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('accessToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

export default API;
```

### `src/api/authApi.js`
```javascript
import API from './axiosConfig';

export const loginUser = (payload) => API.post('/auth/login', payload);
export const registerUser = (payload) => API.post('/auth/register', payload);
```

### `src/api/questionApi.js`
```javascript
import API from './axiosConfig';

export const getAllQuestions = () => API.get('/questions/list');
export const getQuestionById = (id) => API.get(`/questions/${id}`);
export const createQuestion = (payload) => API.post('/questions/create', payload);
export const searchQuestionsByTag = (tag) => API.get(`/questions/search?tag=${tag}`);
```

### `src/api/answerApi.js`
```javascript
import API from './axiosConfig';

export const getAnswersByQuestion = (qId) => API.get(`/answers/thread/${qId}`);
export const postAnswer = (qId, payload) => API.post(`/answers/post/${qId}`, payload);
```

### `src/api/chatApi.js`
```javascript
import API from './axiosConfig';

export const getChatRooms = () => API.get('/chat/rooms');
export const getChatHistory = (roomName) => API.get(`/chat/history/${roomName}`);
```

### `src/api/aiApi.js`
```javascript
import API from './axiosConfig';

export const getAISuggestedAnswer = (qId) => API.post(`/ai/suggest-answer/${qId}`);
export const getAISmartTags = (textContext) => API.post('/ai/suggest-tags', { textContext });
export const inspectContentToxicity = (textContext, type, entityId) => 
  API.post(`/moderation/inspect?type=${type}&entityId=${entityId}`, { textContext });
export const getSystemMetrics = () => API.get('/analytics/dashboard');
```

---

## 4. Package Structure Layer: `src/context`

### `src/context/AuthContext.jsx`
```jsx
import React, { createContext, useState, useEffect } from 'react';
import { loginUser, registerUser } from '../api/authApi';
import API from '../api/axiosConfig';

export const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const email = localStorage.getItem('userEmail');
    const role = localStorage.getItem('userRole');
    const token = localStorage.getItem('accessToken');
    if (token && email && role) {
      setUser({ email, role });
      API.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    }
    setLoading(false);
  }, []);

  const login = async (email, password) => {
    const res = await loginUser({ email, password });
    const { accessToken, role } = res.data;
    localStorage.setItem('accessToken', accessToken);
    localStorage.setItem('userEmail', email);
    localStorage.setItem('userRole', role);
    setUser({ email, role });
    return res.data;
  };

  const register = async (name, email, password, role) => {
    const res = await registerUser({ name, email, password, role });
    const { accessToken, role: userRole } = res.data;
    localStorage.setItem('accessToken', accessToken);
    localStorage.setItem('userEmail', email);
    localStorage.setItem('userRole', userRole);
    setUser({ email, role: userRole });
    return res.data;
  };

  const logout = () => {
    localStorage.clear();
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, login, register, logout, loading }}>
      {!loading && children}
    </AuthContext.Provider>
  );
};
```

### `src/context/QuestionContext.jsx`
```jsx
import React, { createContext, useState } from 'react';
import { getAllQuestions, searchQuestionsByTag } from '../api/questionApi';

export const QuestionContext = createContext(null);

export const QuestionProvider = ({ children }) => {
  const [questions, setQuestions] = useState([]);
  const [currentQuestion, setCurrentQuestion] = useState(null);

  const fetchQuestions = async () => {
    const res = await getAllQuestions();
    setQuestions(res.data);
  };

  const filterByTag = async (tag) => {
    const res = await searchQuestionsByTag(tag);
    setQuestions(res.data);
  };

  return (
    <QuestionContext.Provider value={{ questions, setQuestions, currentQuestion, setCurrentQuestion, fetchQuestions, filterByTag }}>
      {children}
    </QuestionContext.Provider>
  );
};
```

### `src/context/ChatContext.jsx`
```jsx
import React, { createContext, useState } from 'react';
import { getChatRooms } from '../api/chatApi';

export const ChatContext = createContext(null);

export const ChatProvider = ({ children }) => {
  const [rooms, setRooms] = useState([]);
  const [activeRoom, setActiveRoom] = useState('global-channel');

  const fetchRooms = async () => {
    const res = await getChatRooms();
    setRooms(res.data.length ? res.data : [{ roomName: 'global-channel', totalMessages: 0 }]);
  };

  return (
    <ChatContext.Provider value={{ rooms, activeRoom, setActiveRoom, fetchRooms }}>
      {children}
    </ChatContext.Provider>
  );
};
```

---

## 5. Package Structure Layer: `src/hooks`

### `src/hooks/useAuth.js`
```javascript
import { useContext } from 'react';
import { AuthContext } from '../context/AuthContext';
export const useAuth = () => useContext(AuthContext);
```

### `src/hooks/useQuestions.js`
```javascript
import { useContext } from 'react';
import { QuestionContext } from '../context/QuestionContext';
export const useQuestions = () => useContext(QuestionContext);
```

### `src/hooks/useChat.js`
```javascript
import { useContext } from 'react';
import { ChatContext } from '../context/ChatContext';
export const useChat = () => useContext(ChatContext);
```

---

## 6. Package Structure Layer: `src/services`

### `src/services/authService.js`
```javascript
import { loginUser, registerUser } from '../api/authApi';
export const performLogin = async (email, password) => (await loginUser({ email, password })).data;
export const performRegistration = async (n, e, p, r) => (await registerUser({ name: n, email: e, password: p, role: r })).data;
```

### `src/services/questionService.js`
```javascript
import { getAllQuestions, createQuestion } from '../api/questionApi';
export const loadAllQuestions = async () => (await getAllQuestions()).data;
export const publishNewQuestion = async (payload) => (await createQuestion(payload)).data;
```

### `src/services/aiService.js`
```javascript
import { getAISuggestedAnswer, getAISmartTags } from '../api/aiApi';
export const fetchAIEngineSolution = async (qId) => (await getAISuggestedAnswer(qId)).data;
export const fetchAIEngineTags = async (text) => (await getAISmartTags(text)).data;
```

### `src/services/chatService.js`
```javascript
import { getChatRooms, getChatHistory } from '../api/chatApi';
export const fetchActiveRoomsList = async () => (await getChatRooms()).data;
export const fetchSelectedRoomLogs = async (room) => (await getChatHistory(room)).data;
```

---

## 7. Package Structure Layer: `src/utils`

### `src/utils/constants.js`
```javascript
export const BASE_API_URL = "http://localhost:8080/api";
export const SOCKET_BROKER_URL = "http://localhost:8080/api/ws";
export const DEFAULT_CHAT_ROOM = "global-channel";
```

### `src/utils/validators.js`
```javascript
export const validateEmail = (email) => {
  const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return re.test(String(email).toLowerCase());
};

export const validatePassword = (pass) => pass.length >= 6;
```

### `src/utils/helpers.js`
```javascript
export const formatTagString = (tagsRaw) => {
  if (!tagsRaw) return [];
  return tagsRaw.split(',').map(t => t.trim().toLowerCase()).filter(t => t);
};
```

---

## 8. Package Structure Layer: `src/components`

### `src/components/Navbar.jsx`
```jsx
import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';

const Navbar = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="bg-slate-800 text-white shadow-md w-full sticky top-0 z-50">
      <div className="max-w-7xl mx-auto px-4 py-3 flex justify-between items-center">
        <Link to="/home" className="text-xl font-bold tracking-tight text-indigo-400">
          DoConnect <span className="text-xs bg-indigo-600 text-white px-2 py-0.5 rounded ml-1">AI</span>
        </Link>
        {user && (
          <div className="flex items-center gap-6">
            <Link to="/home" className="hover:text-indigo-300 font-medium text-sm transition-colors">Questions</Link>
            <Link to="/chat" className="hover:text-indigo-300 font-medium text-sm transition-colors">Chat Hub</Link>
            {(user.role === 'ADMIN' || user.role === 'MODERATOR') && (
              <Link to="/admin" className="text-yellow-400 hover:text-yellow-300 font-medium text-sm transition-colors">Moderation</Link>
            )}
            {user.role === 'ADMIN' && (
              <Link to="/dashboard" className="text-emerald-400 hover:text-emerald-300 font-medium text-sm transition-colors">Dashboard</Link>
            )}
            <div className="flex items-center gap-3 border-l border-slate-700 pl-6">
              <span className="text-xs text-slate-300 bg-slate-700 px-2 py-1 rounded max-w-[140px] truncate">{user.email}</span>
              <button onClick={handleLogout} className="bg-red-600 hover:bg-red-700 text-white text-xs font-bold px-3 py-1.5 rounded transition-all">
                Logout
              </button>
            </div>
          </div>
        )}
      </div>
    </nav>
  );
};

export default Navbar;
```

### `src/components/Sidebar.jsx`
```jsx
import React from 'react';
import { Link, useLocation } from 'react-router-dom';

const Sidebar = () => {
  const location = useLocation();
  const isActive = (path) => location.pathname === path ? 'bg-indigo-50 text-indigo-700' : 'text-gray-700 hover:bg-gray-50';

  return (
    <div className="w-64 bg-white border-r min-h-screen p-4 hidden md:block shrink-0">
      <h3 className="text-xs font-bold text-gray-400 uppercase tracking-wider mb-4 px-2">Navigation</h3>
      <div className="space-y-1">
        <Link to="/home" className={`flex items-center gap-2 text-sm font-semibold p-2.5 rounded-lg transition-colors ${isActive('/home')}`}>🌍 Public Forum</Link>
        <Link to="/chat" className={`flex items-center gap-2 text-sm font-semibold p-2.5 rounded-lg transition-colors ${isActive('/chat')}`}>💬 Real-time Chat</Link>
        <Link to="/profile" className={`flex items-center gap-2 text-sm font-semibold p-2.5 rounded-lg transition-colors ${isActive('/profile')}`}>👤 Profile Info</Link>
      </div>
    </div>
  );
};

export default Sidebar;
```

### `src/components/Footer.jsx`
```jsx
import React from 'react';
const Footer = () => (
  <footer className="bg-white border-t mt-auto py-4 text-center text-xs text-gray-400">
    &copy; 2026 DoConnect AI Platform. Capstone Architectural Framework Ecosystem.
  </footer>
);
export default Footer;
```

### `src/components/ProtectedRoute.jsx`
```jsx
import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';

const ProtectedRoute = ({ children, allowedRoles }) => {
  const { user } = useAuth();
  const token = localStorage.getItem('accessToken');

  if (!token) {
    return <Navigate to="/login" replace />;
  }

  if (allowedRoles && (!user || !allowedRoles.includes(user.role))) {
    return (
      <div className="max-w-md mx-auto mt-20 bg-white p-6 rounded-xl shadow border text-center">
        <h2 className="text-lg font-bold text-red-600">Access Denied</h2>
        <p className="text-gray-500 text-sm mt-1">Your profile permissions are insufficient for this operational path view.</p>
      </div>
    );
  }

  return children;
};

export default ProtectedRoute;
```

### `src/components/QuestionCard.jsx`
```jsx
import React from 'react';
import { Link } from 'react-router-dom';

const QuestionCard = ({ question }) => {
  return (
    <div className="bg-white p-5 rounded-xl border border-gray-200 shadow-sm hover:shadow-md transition-all">
      <Link to={`/questions/${question.id}`} className="text-lg font-bold text-slate-800 hover:text-indigo-600 transition-colors block mb-2">
        {question.title}
      </Link>
      <p className="text-sm text-gray-600 line-clamp-2 mb-4 leading-relaxed">{question.content}</p>
      <div className="flex flex-wrap justify-between items-center gap-3 pt-3 border-t border-gray-100 text-xs">
        <div className="flex flex-wrap gap-1.5">
          {question.tags?.map((t, i) => (
            <span key={i} className="bg-indigo-50 text-indigo-700 font-medium px-2.5 py-0.5 rounded-md">#{t}</span>
          ))}
        </div>
        <div className="text-gray-400">
          Opened by <span className="font-semibold text-gray-600">{question.authorName || "Anonymous"}</span>
        </div>
      </div>
    </div>
  );
};

export default QuestionCard;
```

### `src/components/AnswerCard.jsx`
```jsx
import React from 'react';

const AnswerCard = ({ answer }) => {
  return (
    <div className={`p-4 rounded-xl border ${answer.aiGenerated ? 'bg-indigo-50/50 border-indigo-100' : 'bg-white border-gray-200'} shadow-sm space-y-2`}>
      {answer.aiGenerated && (
        <span className="inline-block bg-indigo-600 text-white font-black text-[9px] px-2 py-0.5 rounded uppercase tracking-wider">
          AI Generated Solution
        </span>
      )}
      <p className="text-sm text-gray-700 whitespace-pre-wrap leading-relaxed">{answer.content}</p>
      <div className="text-[10px] text-gray-400 text-right">
        Contributed by <span className="font-semibold text-gray-600">{answer.authorName || "Platform AI"}</span>
      </div>
    </div>
  );
};

export default AnswerCard;
```

### `src/components/ChatBox.jsx`
```jsx
import React from 'react';

const ChatBox = ({ messages, currentUserEmail }) => {
  return (
    <div className="flex-1 p-4 overflow-y-auto space-y-3 bg-slate-50/50">
      {messages.map((m, idx) => {
        const isMe = m.senderEmail === currentUserEmail;
        return (
          <div key={idx} className={`flex flex-col ${isMe ? 'items-end' : 'items-start'}`}>
            <span className="text-[9px] text-gray-400 mb-0.5 px-1">{m.senderEmail}</span>
            <div className={`px-4 py-2 rounded-2xl text-xs max-w-sm ${isMe ? 'bg-indigo-600 text-white rounded-tr-none' : 'bg-white border text-gray-800 rounded-tl-none shadow-sm'}`}>
              {m.content}
            </div>
          </div>
        );
      })}
    </div>
  );
};

export default ChatBox;
```

### `src/components/NotificationPanel.jsx`
```jsx
import React from 'react';
const NotificationPanel = ({ message, type }) => {
  if (!message) return null;
  const colors = type === 'error' ? 'bg-red-50 text-red-700 border-red-200' : 'bg-emerald-50 text-emerald-700 border-emerald-200';
  return <div className={`border p-3.5 rounded-xl text-xs font-semibold ${colors}`}>{message}</div>;
};
export default NotificationPanel;
```

### `src/components/Loader.jsx`
```jsx
import React from 'react';
const Loader = () => <div className="text-center py-12 text-sm font-semibold text-gray-400 animate-pulse">Syncing platform structural nodes...</div>;
export default Loader;
```

---

## 9. Package Structure Layer: `src/pages`

### `src/pages/Login.jsx`
```jsx
import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import NotificationPanel from '../components/NotificationPanel';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleFormSubmit = async (e) => {
    e.preventDefault();
    try {
      setError('');
      await login(email, password);
      navigate('/home');
    } catch (err) {
      setError('Invalid platform identifier configuration or passphrase match error.');
    }
  };

  return (
    <div className="max-w-md mx-auto mt-20 bg-white p-8 rounded-2xl shadow-xl border border-gray-100">
      <h2 className="text-2xl font-extrabold text-slate-800 text-center mb-6">Welcome Back</h2>
      <NotificationPanel message={error} type="error" />
      <form onSubmit={handleFormSubmit} className="space-y-4 mt-3">
        <div>
          <label className="block text-xs font-bold text-gray-500 uppercase mb-1">Email Identity</label>
          <input type="email" value={email} onChange={e => setEmail(e.target.value)} className="w-full px-4 py-2.5 border rounded-xl outline-none focus:ring-2 focus:ring-indigo-500 text-sm" placeholder="name@domain.com" required />
        </div>
        <div>
          <label className="block text-xs font-bold text-gray-500 uppercase mb-1">Passphrase</label>
          <input type="password" value={password} onChange={e => setPassword(e.target.value)} className="w-full px-4 py-2.5 border rounded-xl outline-none focus:ring-2 focus:ring-indigo-500 text-sm" placeholder="••••••••" required />
        </div>
        <button type="submit" className="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-2.5 rounded-xl transition-colors shadow-md shadow-indigo-100">
          Verify Connection Identity
        </button>
      </form>
      <div className="text-center text-xs text-gray-400 mt-6">
        New to the platform? <Link to="/register" className="text-indigo-600 font-bold hover:underline">Create an account</Link>
      </div>
    </div>
  );
};

export default Login;
```

### `src/pages/Register.jsx`
```jsx
import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import NotificationPanel from '../components/NotificationPanel';

const Register = () => {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [role, setRole] = useState('USER');
  const [error, setError] = useState('');
  const { register } = useAuth();
  const navigate = useNavigate();

  const handleFormSubmit = async (e) => {
    e.preventDefault();
    try {
      setError('');
      await register(name, email, password, role);
      navigate('/home');
    } catch (err) {
      setError('Registration transaction failed. Email token may already be initialized.');
    }
  };

  return (
    <div className="max-w-md mx-auto mt-12 bg-white p-8 rounded-2xl shadow-xl border border-gray-100">
      <h2 className="text-2xl font-extrabold text-slate-800 text-center mb-5">Create Profile Mapping</h2>
      <NotificationPanel message={error} type="error" />
      <form onSubmit={handleFormSubmit} className="space-y-4 mt-3">
        <div>
          <label className="block text-xs font-bold text-gray-500 uppercase mb-1">Full Profile Name</label>
          <input type="text" value={name} onChange={e => setName(e.target.value)} className="w-full px-4 py-2.5 border rounded-xl outline-none focus:ring-2 focus:ring-indigo-500 text-sm" placeholder="Alex Engineer" required />
        </div>
        <div>
          <label className="block text-xs font-bold text-gray-500 uppercase mb-1">Operational Email</label>
          <input type="email" value={email} onChange={e => setEmail(e.target.value)} className="w-full px-4 py-2.5 border rounded-xl outline-none focus:ring-2 focus:ring-indigo-500 text-sm" placeholder="dev@domain.io" required />
        </div>
        <div>
          <label className="block text-xs font-bold text-gray-500 uppercase mb-1">Secure Passphrase</label>
          <input type="password" value={password} onChange={e => setPassword(e.target.value)} className="w-full px-4 py-2.5 border rounded-xl outline-none focus:ring-2 focus:ring-indigo-500 text-sm" placeholder="••••••••" required />
        </div>
        <div>
          <label className="block text-xs font-bold text-gray-500 uppercase mb-1">Access Role Privilege</label>
          <select value={role} onChange={e => setRole(e.target.value)} className="w-full px-4 py-2.5 border rounded-xl bg-white outline-none focus:ring-2 focus:ring-indigo-500 text-sm">
            <option value="USER">Platform Engineer (USER)</option>
            <option value="MODERATOR">Content Moderator (MODERATOR)</option>
            <option value="ADMIN">System Architect (ADMIN)</option>
          </select>
        </div>
        <button type="submit" className="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-2.5 rounded-xl transition-colors shadow-md mt-2">
          Compile Configuration
        </button>
      </form>
      <div className="text-center text-xs text-gray-400 mt-4">
        Already registered? <Link to="/login" className="text-indigo-600 font-bold hover:underline">Log in</Link>
      </div>
    </div>
  );
};

export default Register;
```

### `src/pages/Home.jsx`
```jsx
import React, { useEffect, useState } from 'react';
import { useQuestions } from '../hooks/useQuestions';
import QuestionCard from '../components/QuestionCard';
import Loader from '../components/Loader';
import Sidebar from '../components/Sidebar';
import { Link } from 'react-router-dom';

const Home = () => {
  const { questions, fetchQuestions, filterByTag } = useQuestions();
  const [tagInput, setTagInput] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    initLoad();
  }, []);

  const initLoad = async () => {
    try {
      setLoading(true);
      await fetchQuestions();
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async (e) => {
    e.preventDefault();
    if (!tagInput.trim()) return initLoad();
    try {
      setLoading(true);
      await filterByTag(tagInput.trim());
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex gap-6 w-full max-w-7xl mx-auto">
      <Sidebar />
      <div className="flex-1 space-y-6">
        <div className="bg-white p-4 rounded-xl border shadow-sm flex flex-col sm:flex-row justify-between items-center gap-4">
          <form onSubmit={handleSearch} className="flex-1 w-full flex gap-2">
            <input type="text" value={tagInput} onChange={e => setTagInput(e.target.value)} className="w-full md:max-w-md px-4 py-2 border rounded-xl text-sm outline-none focus:ring-2 focus:ring-indigo-500" placeholder="Filter by taxonomy token tag (e.g. react)..." />
            <button type="submit" className="bg-slate-700 text-white font-semibold text-sm px-4 py-2 rounded-xl hover:bg-slate-800 transition-colors">Search</button>
          </form>
          <Link to="/ask" className="w-full sm:w-auto bg-indigo-600 hover:bg-indigo-700 text-white font-bold text-sm px-5 py-2 rounded-xl text-center shadow-sm whitespace-nowrap">
            Ask Thread Topic
          </Link>
        </div>

        {loading ? (
          <Loader />
        ) : questions.length === 0 ? (
          <div className="bg-white text-center p-12 rounded-xl border text-gray-400 font-medium">No open platform queries recovered matching configurations.</div>
        ) : (
          <div className="grid grid-cols-1 gap-4">
            {questions.map(q => <QuestionCard key={q.id} question={q} />)}
          </div>
        )}
      </div>
    </div>
  );
};

export default Home;
```

### `src/pages/AskQuestion.jsx`
```jsx
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { createQuestion } from '../api/questionApi';
import { getAISmartTags } from '../api/aiApi';

const AskQuestion = () => {
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [tags, setTags] = useState('');
  const [inferring, setInferring] = useState(false);
  const navigate = useNavigate();

  const handleAiTagInference = async () => {
    if (!content.trim()) return alert('Populate descriptive text configurations first.');
    try {
      setInferring(true);
      const res = await getAISmartTags(content);
      if (res.data?.extractedTags) {
        setTags(res.data.extractedTags.join(', '));
      }
    } catch (e) {
      console.error(e);
    } finally {
      setInferring(false);
    }
  };

  const handleFormSubmit = async (e) => {
    e.preventDefault();
    try {
      const tagList = tags.split(',').map(t => t.trim().toLowerCase()).filter(t => t);
      await createQuestion({ title, content, tags: tagList });
      navigate('/home');
    } catch (err) {
      alert('Content flagged down by automated safety interceptor guards.');
    }
  };

  return (
    <div className="max-w-3xl mx-auto bg-white p-8 rounded-2xl border shadow-sm">
      <h2 className="text-xl font-bold text-slate-800 mb-6">Open Topic Discussion Card</h2>
      <form onSubmit={handleFormSubmit} className="space-y-4">
        <div>
          <label className="block text-xs font-bold text-gray-500 uppercase mb-1">Clear Thread Query Title</label>
          <input type="text" value={title} onChange={e => setTitle(e.target.value)} className="w-full px-4 py-2.5 border rounded-xl text-sm outline-none focus:ring-2 focus:ring-indigo-500" placeholder="e.g. Duplicate dependency mapping crash inside web worker clients..." required />
        </div>
        <div>
          <div className="flex justify-between items-center mb-1">
            <label className="text-xs font-bold text-gray-500 uppercase">Technical Context & Code Details</label>
            <button type="button" onClick={handleAiTagInference} disabled={inferring} className="text-[11px] font-bold text-indigo-600 bg-indigo-50 hover:bg-indigo-100 px-2.5 py-1 rounded transition-colors">
              {inferring ? 'Parsing Text...' : '✨ Predict Structural Tags via Gemini AI'}
            </button>
          </div>
          <textarea rows="6" value={content} onChange={e => setContent(e.target.value)} className="w-full px-4 py-2.5 border rounded-xl text-sm font-mono outline-none focus:ring-2 focus:ring-indigo-500" placeholder="Paste structural stack trace exceptions loop logs here..." required></textarea>
        </div>
        <div>
          <label className="block text-xs font-bold text-gray-500 uppercase mb-1">Taxonomic Labels (Comma Separated)</label>
          <input type="text" value={tags} onChange={e => setTags(e.target.value)} className="w-full px-4 py-2.5 border rounded-xl text-sm outline-none focus:ring-2 focus:ring-indigo-500" placeholder="java, spring-boot, react" />
        </div>
        <div className="pt-2 flex gap-3">
          <button type="submit" className="bg-indigo-600 hover:bg-indigo-700 text-white font-bold text-sm px-5 py-2.5 rounded-xl shadow transition-colors">Publish Forum Thread</button>
          <button type="button" onClick={() => navigate('/home')} className="bg-gray-100 text-gray-700 font-semibold text-sm px-4 py-2.5 rounded-xl hover:bg-gray-200">Cancel</button>
        </div>
      </form>
    </div>
  );
};

export default AskQuestion;
```

### `src/pages/QuestionDetails.jsx`
```jsx
import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getQuestionById } from '../api/questionApi';
import { getAnswersByQuestion, postAnswer } from '../api/answerApi';
import { getAISuggestedAnswer } from '../api/aiApi';
import AnswerCard from '../components/AnswerCard';
import Loader from '../components/Loader';

const QuestionDetails = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [question, setQuestion] = useState(null);
  const [answers, setAnswers] = useState([]);
  const [newAns, setNewAns] = useState('');
  const [aiLoading, setAiLoading] = useState(false);

  useEffect(() => {
    loadThreadData();
  }, [id]);

  const loadThreadData = async () => {
    try {
      const qRes = await getQuestionById(id);
      const aRes = await getAnswersByQuestion(id);
      setQuestion(qRes.data);
      setAnswers(aRes.data);
    } catch (e) {
      navigate('/home');
    }
  };

  const handleCommitAnswer = async (e, directText = null) => {
    if (e) e.preventDefault();
    const payloadText = directText || newAns;
    if (!payloadText.trim()) return;
    try {
      await postAnswer(id, { content: payloadText.trim() });
      setNewAns('');
      loadThreadData();
    } catch (err) {
      alert('Answer payload context submission exception.');
    }
  };

  const triggerAiInferenceSuggestion = async () => {
    try {
      setAiLoading(true);
      const res = await getAISuggestedAnswer(id);
      if (res.data?.payload) {
        if (window.confirm(`Google Gemini AI Agent Solution Node Response:

${res.data.payload}

Commit this text payload to standard user records?`)) {
          await handleCommitAnswer(null, res.data.payload);
        }
      }
    } catch (err) {
      console.error(err);
    } finally {
      setAiLoading(false);
    }
  };

  if (!question) return <Loader />;

  return (
    <div className="space-y-6 max-w-4xl mx-auto w-full">
      <div className="bg-white p-6 rounded-2xl border shadow-sm">
        <div className="flex justify-between items-start gap-4 mb-4">
          <h1 className="text-xl font-extrabold text-slate-800">{question.title}</h1>
          <button onClick={triggerAiInferenceSuggestion} disabled={aiLoading} className="bg-indigo-600 hover:bg-indigo-700 text-white font-bold text-xs px-4 py-2 rounded-lg shadow whitespace-nowrap transition-all">
            {aiLoading ? 'Synthesizing Solution...' : '🤖 Generate AI Solution'}
          </button>
        </div>
        <p className="text-sm font-mono text-gray-700 bg-slate-50 border p-4 rounded-xl whitespace-pre-wrap">{question.content}</p>
        <div className="flex justify-between items-center text-xs text-gray-400 mt-4 pt-3 border-t">
          <div className="flex gap-1">
            {question.tags?.map((t, i) => <span key={i} className="bg-slate-100 text-slate-600 px-2 py-0.5 rounded">#{t}</span>)}
          </div>
          <span>Opened by <b className="text-gray-600">{question.authorName}</b></span>
        </div>
      </div>

      <div className="space-y-3">
        <h3 className="text-sm font-bold text-gray-400 uppercase tracking-wider">Solution Tree ({answers.length})</h3>
        {answers.map(ans => <AnswerCard key={ans.id} answer={ans} />)}
      </div>

      <form onSubmit={e => handleCommitAnswer(e)} className="bg-white p-4 rounded-xl border shadow-sm space-y-3">
        <label className="block text-xs font-bold text-gray-500 uppercase">Contribute Resolution Content</label>
        <textarea rows="4" value={newAns} onChange={e => setNewAns(e.target.value)} className="w-full px-4 py-2 border rounded-xl text-sm outline-none focus:ring-2 focus:ring-indigo-500" placeholder="Type resolution layout parameters..."></textarea>
        <button type="submit" className="bg-slate-800 hover:bg-slate-900 text-white font-bold text-xs px-4 py-2 rounded-lg transition-colors">Commit Solution</button>
      </form>
    </div>
  );
};

export default QuestionDetails;
```

### `src/pages/AIAnswer.jsx`
```jsx
// Handled contextually dynamically inside QuestionDetails studio layout dashboard pages.
import React from 'react';
const AIAnswer = () => <div className="p-4 bg-white border rounded-xl text-xs text-gray-400 font-mono">Dynamic AI Solution Injection Matrix Operational.</div>;
export default AIAnswer;
```

### `src/pages/ChatRoom.jsx`
```jsx
import React, { useState, useEffect, useRef } from 'react';
import { useChat } from '../hooks/useChat';
import { useAuth } from '../hooks/useAuth';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import { getChatHistory } from '../api/chatApi';
import ChatBox from '../components/ChatBox';

const ChatRoom = () => {
  const { user } = useAuth();
  const { rooms, activeRoom, setActiveRoom, fetchRooms } = useChat();
  const [messages, setMessages] = useState([]);
  const [inputMessage, setInputMessage] = useState('');
  const stompClientRef = useRef(null);
  const subscriptionRef = useRef(null);
  const scrollRef = useRef(null);

  useEffect(() => {
    fetchRooms();
    connectWebSocket();
    return () => disconnectWebSocket();
  }, []);

  useEffect(() => {
    loadChatHistory(activeRoom);
    if (stompClientRef.current && stompClientRef.current.connected) {
      remapSubscription(activeRoom);
    }
  }, [activeRoom]);

  useEffect(() => {
    scrollRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages]);

  const loadChatHistory = async (rm) => {
    try {
      const res = await getChatHistory(rm);
      setMessages(res.data);
    } catch (e) { console.error(e); }
  };

  const connectWebSocket = () => {
    const socket = new SockJS('http://localhost:8080/api/ws');
    const client = new Client({
      webSocketFactory: () => socket,
      debug: () => {},
      onConnect: () => {
        stompClientRef.current = client;
        remapSubscription(activeRoom);
      }
    });
    client.activate();
    stompClientRef.current = client;
  };

  const remapSubscription = (rm) => {
    if (subscriptionRef.current) subscriptionRef.current.unsubscribe();
    subscriptionRef.current = stompClientRef.current.subscribe(`/topic/room.${rm}`, (payload) => {
      setMessages(prev => [...prev, JSON.parse(payload.body)]);
    });
  };

  const disconnectWebSocket = () => {
    if (subscriptionRef.current) subscriptionRef.current.unsubscribe();
    if (stompClientRef.current) stompClientRef.current.deactivate();
  };

  const handleSend = (e) => {
    e.preventDefault();
    if (!inputMessage.trim() || !stompClientRef.current?.connected) return;
    const payload = {
      room: activeRoom,
      senderEmail: user?.email || 'anonymous@domain.io',
      content: inputMessage.trim()
    };
    stompClientRef.current.publish({ destination: '/app/chat.sendMessage', body: JSON.stringify(payload) });
    setInputMessage('');
  };

  return (
    <div className="grid grid-cols-1 md:grid-cols-4 gap-6 h-[78vh] max-w-7xl mx-auto w-full">
      <div className="bg-white p-4 rounded-xl border flex flex-col gap-3">
        <h3 className="text-xs font-bold text-gray-400 uppercase tracking-wider border-b pb-2">Discussion Channels</h3>
        <div className="flex-1 overflow-y-auto space-y-1">
          {rooms.map(r => (
            <button key={r.roomName} onClick={() => setActiveRoom(r.roomName)} className={`w-full text-left px-3 py-2 rounded-lg text-xs font-semibold transition-colors ${activeRoom === r.roomName ? 'bg-indigo-600 text-white' : 'hover:bg-gray-50 text-slate-700'}`}>
              # {r.roomName}
            </button>
          ))}
        </div>
      </div>
      <div className="md:col-span-3 bg-white rounded-xl border flex flex-col overflow-hidden shadow-sm">
        <div className="bg-slate-800 text-white px-5 py-3 font-bold text-xs">Terminal Pipe Context Target: <span className="text-indigo-400">#{activeRoom}</span></div>
        <ChatBox messages={messages} currentUserEmail={user?.email} />
        <div ref={scrollRef} />
        <form onSubmit={handleSend} className="p-3 border-t bg-white flex gap-2">
          <input type="text" value={inputMessage} onChange={e => setInputMessage(e.target.value)} className="w-full px-4 py-2 border rounded-xl text-xs outline-none focus:ring-2 focus:ring-indigo-500" placeholder="Stream broadcast data packets..." />
          <button type="submit" className="bg-indigo-600 hover:bg-indigo-700 text-white font-bold text-xs px-5 rounded-xl transition-colors">Send</button>
        </form>
      </div>
    </div>
  );
};

export default ChatRoom;
```

### `src/pages/Profile.jsx`
```jsx
import React from 'react';
import { useAuth } from '../hooks/useAuth';
import Sidebar from '../components/Sidebar';

const Profile = () => {
  const { user } = useAuth();
  return (
    <div className="flex gap-6 w-full max-w-7xl mx-auto">
      <Sidebar />
      <div className="flex-1 max-w-md bg-white p-6 rounded-2xl border shadow-sm h-fit space-y-4">
        <h2 className="text-lg font-bold text-slate-800 border-b pb-2">Active Profile Properties</h2>
        <div className="space-y-2 text-xs text-gray-600 bg-slate-50 p-4 rounded-xl">
          <div>Identifier Alias: <span className="font-bold text-gray-800">{user?.email}</span></div>
          <div>Authority Boundary: <span className="font-bold text-indigo-600 uppercase bg-indigo-50 px-2 py-0.5 rounded ml-1">{user?.role}</span></div>
        </div>
      </div>
    </div>
  );
};

export default Profile;
```

### `src/pages/Dashboard.jsx`
```jsx
import React, { useEffect, useState } from 'react';
import { getSystemMetrics } from '../api/aiApi';

const Dashboard = () => {
  const [metrics, setMetrics] = useState(null);

  useEffect(() => {
    getSystemMetrics().then(res => setMetrics(res.data)).catch(e => console.error(e));
  }, []);

  if (!metrics) return <div className="text-center py-12 text-xs text-gray-400 font-semibold animate-pulse">Parsing dashboard configuration indices...</div>;

  return (
    <div className="space-y-6 max-w-5xl mx-auto w-full">
      <h2 className="text-xl font-extrabold text-slate-800 tracking-tight">System Diagnostic Dashboard</h2>
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-6">
        <div className="bg-white p-6 rounded-2xl border shadow-xs"><div className="text-xs font-bold text-gray-400 uppercase tracking-wider">User Profiles</div><div className="text-3xl font-black text-slate-800 mt-1">{metrics.usersCount || 0}</div></div>
        <div className="bg-white p-6 rounded-2xl border shadow-xs"><div className="text-xs font-bold text-gray-400 uppercase tracking-wider">Indexed Threads</div><div className="text-3xl font-black text-indigo-600 mt-1">{metrics.questionsCount || 0}</div></div>
        <div className="bg-white p-6 rounded-2xl border shadow-xs"><div className="text-xs font-bold text-gray-400 uppercase tracking-wider">Resolution Nodes</div><div className="text-3xl font-black text-emerald-600 mt-1">{metrics.answersCount || 0}</div></div>
      </div>
    </div>
  );
};

export default Dashboard;
```

### `src/pages/AdminPanel.jsx`
```jsx
import React, { useState } from 'react';
import { inspectContentToxicity } from '../api/aiApi';
import NotificationPanel from '../components/NotificationPanel';

const AdminPanel = () => {
  const [testText, setTestText] = useState('');
  const [report, setReport] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleValidationCheck = async (e) => {
    e.preventDefault();
    if (!testText.trim()) return;
    try {
      setLoading(true);
      const res = await inspectContentToxicity(testText, 'MANUAL_AUDIT', 0);
      setReport(res.data);
    } catch (err) { console.error(err); } 
    finally { setLoading(false); }
  };

  return (
    <div className="max-w-2xl mx-auto bg-white p-6 rounded-2xl border shadow-sm space-y-6 w-full">
      <h2 className="text-lg font-bold text-slate-800 border-b pb-2">AI Content Toxicity Analysis</h2>
      <form onSubmit={handleValidationCheck} className="space-y-4">
        <label className="block text-xs font-bold text-gray-500 uppercase">Input Stream Payload Test</label>
        <textarea rows="4" value={testText} onChange={e => setTestText(e.target.value)} className="w-full px-4 py-2 border rounded-xl text-sm outline-none focus:ring-2 focus:ring-indigo-500" placeholder="Type test text string packets to cross-analyze safety policies..."></textarea>
        <button type="submit" disabled={loading} className="bg-red-600 hover:bg-red-700 text-white font-bold text-xs px-4 py-2 rounded-xl transition-colors shadow-sm">
          {loading ? 'Analyzing String Content...' : 'Verify Content Boundaries'}
        </button>
      </form>
      {report && (
        <div className={`p-4 rounded-xl border text-xs ${report.flagged ? 'bg-red-50 border-red-200 text-red-800' : 'bg-emerald-50 border-emerald-200 text-emerald-800'}`}>
          <div className="font-bold text-xs uppercase mb-1">Inference Summary Matrix Decision:</div>
          <div>Policy Block: <span className="font-extrabold">{report.flagged ? 'FLAGGED BLOCK CONFLICT' : 'VERIFIED CLEAN CONTEXT'}</span></div>
          <div className="mt-1">Reason Metrics: {report.classificationReason}</div>
        </div>
      )}
    </div>
  );
};

export default AdminPanel;
```

---

## 10. Package Structure Layer: `src/routes`

### `src/routes/AppRoutes.jsx`
```jsx
import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';
import ProtectedRoute from '../components/ProtectedRoute';
import Login from '../pages/Login';
import Register from '../pages/Register';
import Home from '../pages/Home';
import AskQuestion from '../pages/AskQuestion';
import QuestionDetails from '../pages/QuestionDetails';
import ChatRoom from '../pages/ChatRoom';
import Profile from '../pages/Profile';
import Dashboard from '../pages/Dashboard';
import AdminPanel from '../pages/AdminPanel';

const AppRoutes = () => {
  return (
    <div className="min-h-screen flex flex-col bg-gray-50">
      <Navbar />
      <div className="flex-1 w-full max-w-7xl mx-auto p-4 md:p-6 flex flex-col">
        <Routes>
          {/* Public Authorization Pipelines */}
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />

          {/* Secured Core Application Scopes */}
          <Route path="/home" element={<ProtectedRoute><Home /></ProtectedRoute>} />
          <Route path="/ask" element={<ProtectedRoute><AskQuestion /></ProtectedRoute>} />
          <Route path="/questions/:id" element={<ProtectedRoute><QuestionDetails /></ProtectedRoute>} />
          <Route path="/chat" element={<ProtectedRoute><ChatRoom /></ProtectedRoute>} />
          <Route path="/profile" element={<ProtectedRoute><Profile /></ProtectedRoute>} />
          
          {/* Locked Administrative Routing Observers */}
          <Route path="/dashboard" element={<ProtectedRoute allowedRoles={['ADMIN']}><Dashboard /></ProtectedRoute>} />
          <Route path="/admin" element={<ProtectedRoute allowedRoles={['ADMIN', 'MODERATOR']}><AdminPanel /></ProtectedRoute>} />

          {/* Catch-All Fallback Mapping */}
          <Route path="*" element={<Navigate to="/home" replace />} />
        </Routes>
      </div>
      <Footer />
    </div>
  );
};

export default AppRoutes;
```

### `src/routes/ProtectedRoutes.jsx`
```jsx
// Handled directly and contextually using High-Order Component interceptor guards within AppRoutes mapping configurations.
import React from 'react';
const ProtectedRoutes = () => null;
export default ProtectedRoutes;
```

---
### End of Complete Front-End Ecosystem Specification File
