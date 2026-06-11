# DoConnect AI – Complete Frontend React Source Code Document
### Framework Architecture: React 18.x with Tailwind CSS, Axios, Context API & React Router v6

This file contains the complete standalone code setup for your Capstone project frontend. Every file structure has been fully written out to target your backend API routes (`http://localhost:8080/api`) securely using custom Axios routing instances, Context interceptors, routing protections, and WebSockets.

---

## 1. Environment and Base Layout Configuration

### `package.json`
```json
{
  "name": "doconnect-frontend",
  "private": true,
  "version": "1.0.0",
  "type": "module",
  "scripts": {
    "dev": "vite",
    "build": "vite build",
    "lint": "eslint . --ext js,jsx --report-unused-disable-directives --max-warnings 0",
    "preview": "vite preview"
  },
  "dependencies": {
    "axios": "^1.6.8",
    "react": "^18.3.1",
    "react-dom": "^18.3.1",
    "react-router-dom": "^6.23.1",
    "sockjs-client": "^1.6.1",
    "stompjs": "^2.3.3"
  },
  "devDependencies": {
    "@types/react": "^18.3.3",
    "@types/react-dom": "^18.3.0",
    "@vitejs/plugin-react": "^4.3.0",
    "autoprefixer": "^10.4.19",
    "postcss": "^8.4.38",
    "tailwindcss": "^3.4.3",
    "vite": "^5.2.11"
  }
}
```

### `src/index.css`
```css
@tailwind base;
@tailwind components;
@tailwind utilities;

body {
  margin: 0;
  background-color: #f3f4f6;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;
}
```

### `src/App.jsx`
```jsx
import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import Navbar from './components/Navbar';
import Login from './pages/Login';
import Register from './pages/Register';
import QuestionList from './pages/QuestionList';
import AskQuestion from './pages/AskQuestion';
import QuestionDetails from './pages/QuestionDetails';
import ChatRoom from './pages/ChatRoom';
import AnalyticsDashboard from './pages/AnalyticsDashboard';
import AdminModeration from './pages/AdminModeration';

function App() {
  return (
    <Router>
      <AuthProvider>
        <div className="min-h-screen bg-gray-100 flex flex-col">
          <Navbar />
          <main className="flex-1 container mx-auto px-4 py-6">
            <Routes>
              {/* Public Routing Routes */}
              <option />
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />

              {/* Protected Workspace Layout Paths */}
              <Route path="/questions" element={<ProtectedRoute><QuestionList /></ProtectedRoute>} />
              <Route path="/ask" element={<ProtectedRoute><AskQuestion /></ProtectedRoute>} />
              <Route path="/questions/:id" element={<ProtectedRoute><QuestionDetails /></ProtectedRoute>} />
              <Route path="/chat" element={<ProtectedRoute><ChatRoom /></ProtectedRoute>} />
              <Route path="/analytics" element={<ProtectedRoute allowedRoles={['ADMIN']}><AnalyticsDashboard /></ProtectedRoute>} />
              <Route path="/moderation" element={<ProtectedRoute allowedRoles={['ADMIN', 'MODERATOR']}><AdminModeration /></ProtectedRoute>} />

              {/* Fallback Restructure Mapping */}
              <Route path="*" element={<Navigate to="/questions" replace />} />
            </Routes>
          </main>
        </div>
      </AuthProvider>
    </Router>
  );
}

export default App;
```

---

## 2. Infrastructure Layer Mappings & Context Providers

### `src/context/AuthContext.jsx`
```jsx
import React, { createContext, useState, useEffect } from 'react';
import axios from 'axios';

export const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(localStorage.getItem('accessToken'));
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (token) {
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
      // Initialize basic user metadata parsing from saved state parameters
      const savedEmail = localStorage.getItem('userEmail');
      const savedRole = localStorage.getItem('userRole');
      if (savedEmail && savedRole) {
        setUser({ email: savedEmail, role: savedRole });
      }
    } else {
      delete axios.defaults.headers.common['Authorization'];
      setUser(null);
    }
    setLoading(false);
  }, [token]);

  const login = async (email, password) => {
    const res = await axios.post('http://localhost:8080/api/auth/login', { email, password });
    const { accessToken, role } = res.data;
    localStorage.setItem('accessToken', accessToken);
    localStorage.setItem('userEmail', email);
    localStorage.setItem('userRole', role);
    setToken(accessToken);
    setUser({ email, role });
    return res.data;
  };

  const register = async (name, email, password, role) => {
    const res = await axios.post('http://localhost:8080/api/auth/register', { name, email, password, role });
    const { accessToken, role: userRole } = res.data;
    localStorage.setItem('accessToken', accessToken);
    localStorage.setItem('userEmail', email);
    localStorage.setItem('userRole', userRole);
    setToken(accessToken);
    setUser({ email, role: userRole });
    return res.data;
  };

  const logout = () => {
    localStorage.clear();
    setToken(null);
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, token, login, register, logout, loading }}>
      {!loading && children}
    </AuthContext.Provider>
  );
};
```

---

## 3. High-Order Guarded Middleware UI Layout Components

### `src/components/ProtectedRoute.jsx`
```jsx
import React, { useContext } from 'react';
import { Navigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';

const ProtectedRoute = ({ children, allowedRoles }) => {
  const { user, token } = useContext(AuthContext);

  if (!token) {
    return <Navigate to="/login" replace />;
  }

  if (allowedRoles && (!user || !allowedRoles.includes(user.role))) {
    return (
      <div className="bg-white p-6 rounded-lg shadow text-center max-w-md mx-auto mt-12">
        <h2 className="text-xl font-bold text-red-600 mb-2">Access Denied</h2>
        <p className="text-gray-600">Your profile credentials do not possess matching administrative privileges for this panel view.</p>
      </div>
    );
  }

  return children;
};

export default ProtectedRoute;
```

### `src/components/Navbar.jsx`
```jsx
import React, { useContext } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';

const Navbar = () => {
  const { user, logout } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="bg-slate-800 text-white shadow-md">
      <div className="container mx-auto px-4 py-3 flex justify-between items-center">
        <Link to="/" className="text-xl font-bold tracking-wide flex items-center gap-2 text-indigo-400">
          <span>DoConnect <span className="text-white text-sm bg-indigo-600 px-2 py-0.5 rounded">AI</span></span>
        </Link>

        {user && (
          <div className="flex items-center gap-6">
            <Link to="/questions" className="hover:text-indigo-300 transition-colors">Questions</Link>
            <Link to="/chat" className="hover:text-indigo-300 transition-colors">Chat Hub</Link>
            
            {(user.role === 'ADMIN' || user.role === 'MODERATOR') && (
              <Link to="/moderation" className="hover:text-red-400 text-yellow-400 font-medium transition-colors">Moderation</Link>
            )}
            {user.role === 'ADMIN' && (
              <Link to="/analytics" className="hover:text-emerald-400 text-emerald-300 font-medium transition-colors">Dashboard</Link>
            )}

            <div className="flex items-center gap-3 border-l border-slate-700 pl-6">
              <span className="text-sm text-slate-300 max-w-[150px] truncate">{user.email}</span>
              <button onClick={handleLogout} className="bg-red-600 hover:bg-red-700 text-xs font-semibold px-3 py-1.5 rounded transition-all">
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

---

## 4. Operational Screen Layout Pages

### `src/pages/Login.jsx`
```jsx
import React, { useState, useContext } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const { login } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!email || !password) return setError('Please input all baseline identity requirements.');
    try {
      setError('');
      await login(email, password);
      navigate('/questions');
    } catch (err) {
      setError(err.response?.data?.message || 'Authentication configuration validation error identity mismatch.');
    }
  };

  return (
    <div className="max-w-md mx-auto mt-16 bg-white p-8 rounded-xl shadow-lg border border-gray-100">
      <h2 className="text-2xl font-extrabold text-gray-800 text-center mb-6">Welcome Back</h2>
      {error && <div className="bg-red-50 text-red-700 p-3 rounded-lg text-sm mb-4 border border-red-200">{error}</div>}
      
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-semibold text-gray-700 mb-1">Email Identity</label>
          <input type="email" value={email} onChange={e => setEmail(e.target.value)} className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none" placeholder="name@domain.com" />
        </div>
        <div>
          <label className="block text-sm font-semibold text-gray-700 mb-1">Passphrase Credential</label>
          <input type="password" value={password} onChange={e => setPassword(e.target.value)} className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none" placeholder="••••••••" />
        </div>
        <button type="submit" className="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-2 rounded-lg transition-colors shadow-md shadow-indigo-100 mt-2">
          Verify Connection Identity
        </button>
      </form>
      <p className="text-center text-sm text-gray-500 mt-6">
        New engineer to our platform? <Link to="/register" className="text-indigo-600 font-semibold hover:underline">Create a baseline account</Link>
      </p>
    </div>
  );
};

export default Login;
```

### `src/pages/Register.jsx`
```jsx
import React, { useState, useContext } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';

const Register = () => {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [role, setRole] = useState('USER');
  const [error, setError] = useState('');
  const { register } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!name || !email || !password) return setError('All explicit metadata constraints are required.');
    try {
      setError('');
      await register(name, email, password, role);
      navigate('/questions');
    } catch (err) {
      setError(err.response?.data?.message || 'An operational registration error has tripped.');
    }
  };

  return (
    <div className="max-w-md mx-auto mt-10 bg-white p-8 rounded-xl shadow-lg border border-gray-100">
      <h2 className="text-2xl font-extrabold text-gray-800 text-center mb-4">Join Platform Context</h2>
      {error && <div className="bg-red-50 text-red-700 p-3 rounded-lg text-sm mb-4 border border-red-200">{error}</div>}
      
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-semibold text-gray-700 mb-1">Full Display Name</label>
          <input type="text" value={name} onChange={e => setName(e.target.value)} className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none" placeholder="Alex Dev" />
        </div>
        <div>
          <label className="block text-sm font-semibold text-gray-700 mb-1">Operational Email</label>
          <input type="email" value={email} onChange={e => setEmail(e.target.value)} className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none" placeholder="dev@domain.io" />
        </div>
        <div>
          <label className="block text-sm font-semibold text-gray-700 mb-1">Password Target</label>
          <input type="password" value={password} onChange={e => setPassword(e.target.value)} className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none" placeholder="••••••••" />
        </div>
        <div>
          <label className="block text-sm font-semibold text-gray-700 mb-1">Access Role Context</label>
          <select value={role} onChange={e => setRole(e.target.value)} className="w-full px-4 py-2 border border-gray-300 rounded-lg bg-white outline-none focus:ring-2 focus:ring-indigo-500">
            <option value="USER">Platform Engineer (USER)</option>
            <option value="MODERATOR">Content Moderator (MODERATOR)</option>
            <option value="ADMIN">System Architect (ADMIN)</option>
          </select>
        </div>
        <button type="submit" className="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-2 rounded-lg transition-colors shadow-md mt-2">
          Compile Profile Mapping
        </button>
      </form>
      <p className="text-center text-sm text-gray-500 mt-4">
        Already registered? <Link to="/login" className="text-indigo-600 font-semibold hover:underline">Log in context</Link>
      </p>
    </div>
  );
};

export default Register;
export default Register;
```

### `src/pages/QuestionList.jsx`
```jsx
import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';

const QuestionList = () => {
  const [questions, setQuestions] = useState([]);
  const [searchTag, setSearchTag] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchQuestions();
  }, []);

  const fetchQuestions = async () => {
    try {
      setLoading(true);
      const res = await axios.get('http://localhost:8080/api/questions/list');
      setQuestions(res.data);
    } catch (err) {
      console.error('Failed to resolve database threads index.', err);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async (e) => {
    e.preventDefault();
    if (!searchTag.trim()) return fetchQuestions();
    try {
      setLoading(true);
      const res = await axios.get(`http://localhost:8080/api/questions/search?tag=${searchTag}`);
      setQuestions(res.data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex flex-col md:flex-row justify-between items-start md:items-center gap-4 bg-white p-4 rounded-xl shadow-sm border border-gray-100">
        <form onSubmit={handleSearch} className="flex-1 w-full flex gap-2">
          <input type="text" value={searchTag} onChange={e => setSearchTag(e.target.value)} className="w-full md:max-w-md px-4 py-2 border border-gray-300 rounded-lg outline-none focus:ring-2 focus:ring-indigo-500" placeholder="Search threads by technical tag (e.g., react)..." />
          <button type="submit" className="bg-slate-700 text-white px-5 py-2 rounded-lg font-semibold hover:bg-slate-800 transition-colors">Search</button>
        </form>
        <Link to="/ask" className="w-full md:w-auto bg-indigo-600 text-white text-center px-5 py-2 rounded-lg font-bold hover:bg-indigo-700 transition-colors shadow-sm">
          Ask Thread Topic
        </Link>
      </div>

      {loading ? (
        <div className="text-center text-gray-500 font-medium py-12">Querying workspace infrastructure indices...</div>
      ) : questions.length === 0 ? (
        <div className="bg-white p-12 text-center rounded-xl shadow-sm border text-gray-500">No active discussion threads located matching parameters.</div>
      ) : (
        <div className="grid gap-4">
          {questions.map((q) => (
            <div key={q.id} className="bg-white p-6 rounded-xl shadow-sm border border-gray-100 hover:shadow-md transition-shadow flex flex-col justify-between gap-4">
              <div>
                <Link to={`/questions/${q.id}`} className="text-xl font-bold text-slate-800 hover:text-indigo-600 transition-colors block mb-2">{q.title}</Link>
                <p className="text-gray-600 line-clamp-2 text-sm mb-4">{q.content}</p>
              </div>
              <div className="flex flex-wrap justify-between items-center gap-2 pt-2 border-t border-gray-50 text-xs">
                <div className="flex gap-1.5">
                  {q.tags?.map((t, idx) => (
                    <span key={idx} className="bg-indigo-50 text-indigo-700 px-2.5 py-1 rounded-md font-medium">#{t}</span>
                  ))}
                </div>
                <div className="text-gray-500">
                  Opened by <span className="font-semibold text-gray-700">{q.authorName}</span>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default QuestionList;
```

### `src/pages/AskQuestion.jsx`
```jsx
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const AskQuestion = () => {
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [tags, setTags] = useState('');
  const [predicting, setPredicting] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const navigate = useNavigate();

  const handlePredictTags = async () => {
    if (!content.trim()) return alert('Provide explicit background technical info blocks first.');
    try {
      setPredicting(true);
      const res = await axios.post('http://localhost:8080/api/ai/suggest-tags', { textContext: content });
      if (res.data?.extractedTags) {
        setTags(res.data.extractedTags.join(', '));
      }
    } catch (err) {
      console.error('AI tags inference mechanism anomaly: ', err);
    } finally {
      setPredicting(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!title || !content) return alert('Title and core structural explanation targets are mandatory.');
    try {
      setSubmitting(true);
      const tagArray = tags.split(',').map(t => t.trim().toLowerCase()).filter(t => t);
      await axios.post('http://localhost:8080/api/questions/create', { title, content, tags: tagArray });
      navigate('/questions');
    } catch (err) {
      alert('Content flagged or system execution exception tripped.');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="max-w-3xl mx-auto bg-white p-8 rounded-xl shadow-sm border">
      <h2 className="text-2xl font-bold text-slate-800 mb-6">Open Knowledge Discussion Thread</h2>
      
      <form onSubmit={handleSubmit} className="space-y-5">
        <div>
          <label className="block text-sm font-bold text-gray-700 mb-1">Clear Query Concept Heading</label>
          <input type="text" value={title} onChange={e => setTitle(e.target.value)} className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none" placeholder="e.g., Connection dropped exceptions mapping over sockjs clients..." />
        </div>
        <div>
          <div className="flex justify-between items-center mb-1">
            <label className="text-sm font-bold text-gray-700">Detailed Technical Code Execution & Context Block</label>
            <button type="button" onClick={handlePredictTags} disabled={predicting} className="text-xs bg-indigo-50 hover:bg-indigo-100 text-indigo-700 px-3 py-1 rounded font-semibold transition-colors">
              {predicting ? 'Inference Pending...' : '✨ Auto-Predict Structural Tags via AI'}
            </button>
          </div>
          <textarea rows="8" value={content} onChange={e => setContent(e.target.value)} className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none font-mono text-sm" placeholder="Paste structural stacktrace metadata loops or core logical framework configurations here..."></textarea>
        </div>
        <div>
          <label className="block text-sm font-bold text-gray-700 mb-1">Technical Taxonomy Tokens (Comma-Separated)</label>
          <input type="text" value={tags} onChange={e => setTags(e.target.value)} className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none" placeholder="java, spring-boot, microservices, runtime" />
        </div>
        <div className="pt-2 flex gap-3">
          <button type="submit" disabled={submitting} className="bg-indigo-600 hover:bg-indigo-700 text-white font-bold px-6 py-2 rounded-lg shadow-md transition-colors">
            {submitting ? 'Publishing Context...' : 'Commit Query to Index'}
          </button>
          <button type="button" onClick={() => navigate('/questions')} className="bg-gray-100 text-gray-700 px-4 py-2 rounded-lg font-semibold hover:bg-gray-200 transition-colors">Cancel</button>
        </div>
      </form>
    </div>
  );
};

export default AskQuestion;
```

### `src/pages/QuestionDetails.jsx`
```jsx
import React, { useState, useEffect, useContext } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import axios from 'axios';

const QuestionDetails = () => {
  const { id } = useParams();
  const { user } = useContext(AuthContext);
  const [question, setQuestion] = useState(null);
  const [answers, setAnswers] = useState([]);
  const [newAnswer, setNewAnswer] = useState('');
  const [aiGenerating, setAiGenerating] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    loadThreadContext();
  }, [id]);

  const loadThreadContext = async () => {
    try {
      const qRes = await axios.get(`http://localhost:8080/api/questions/${id}`);
      const aRes = await axios.get(`http://localhost:8080/api/answers/thread/${id}`);
      setQuestion(qRes.data);
      setAnswers(aRes.data);
    } catch (err) {
      console.error(err);
      navigate('/questions');
    }
  };

  const handlePostAnswer = async (e, directText = null) => {
    if (e) e.preventDefault();
    const cleanContent = directText || newAnswer;
    if (!cleanContent.trim()) return;
    try {
      await axios.post(`http://localhost:8080/api/answers/post/${id}`, { content: cleanContent });
      setNewAnswer('');
      loadThreadContext();
    } catch (err) {
      alert('Answer payload context failed validation checks.');
    }
  };

  const handleAiSynthesize = async () => {
    try {
      setAiGenerating(true);
      const res = await axios.post(`http://localhost:8080/api/ai/suggest-answer/${id}`);
      if (res.data?.payload) {
        if(confirm(`AI Agent Synthesized Solution:

${res.data.payload}

Commit this directly to user discussion indexing pipeline?`)) {
          await handlePostAnswer(null, res.data.payload);
        }
      }
    } catch (err) {
      console.error(err);
    } finally {
      setAiGenerating(false);
    }
  };

  if (!question) return <div className="text-center py-12 text-gray-500">Parsing thread metrics...</div>;

  return (
    <div className="space-y-6 max-w-4xl mx-auto">
      <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
        <div className="flex justify-between items-start gap-4 mb-4">
          <h1 className="text-2xl font-extrabold text-slate-800">{question.title}</h1>
          <button onClick={handleAiSynthesize} disabled={aiGenerating} className="bg-indigo-600 hover:bg-indigo-700 text-white text-xs font-bold px-4 py-2 rounded-lg shadow transition-all whitespace-nowrap">
            {aiGenerating ? 'AI Synthesizing...' : '🤖 Generate AI Solution'}
          </button>
        </div>
        <p className="text-gray-700 whitespace-pre-wrap font-mono text-sm bg-slate-50 p-4 rounded-lg border border-slate-100 mb-4">{question.content}</p>
        <div className="flex justify-between items-center text-xs text-gray-500 pt-2 border-t">
          <div className="flex gap-1">
            {question.tags?.map((t, i) => <span key={i} className="bg-indigo-50 text-indigo-700 px-2 py-0.5 rounded">#{t}</span>)}
          </div>
          <span>Opened by <b className="text-slate-700">{question.authorName}</b></span>
        </div>
      </div>

      <div className="space-y-4">
        <h3 className="text-lg font-bold text-slate-800 border-b pb-2">Compiled Solution Responses ({answers.length})</h3>
        {answers.map((a) => (
          <div key={a.id} className={`p-5 rounded-xl shadow-sm border ${a.aiGenerated ? 'bg-indigo-50/60 border-indigo-100' : 'bg-white border-gray-100'}`}>
            {a.aiGenerated && <span className="inline-block bg-indigo-600 text-white font-bold text-[10px] uppercase tracking-wider px-2 py-0.5 rounded mb-2">AI Synthesized</span>}
            <p className="text-gray-700 text-sm whitespace-pre-wrap mb-3">{a.content}</p>
            <div className="text-[11px] text-gray-400 text-right">Contributed by <span className="font-semibold text-gray-600">{a.authorName}</span></div>
          </div>
        ))}
      </div>

      <form onSubmit={e => handlePostAnswer(e)} className="bg-white p-5 rounded-xl shadow-sm border space-y-3">
        <label className="block text-sm font-bold text-gray-800">Contribute Solution Context</label>
        <textarea rows="4" value={newAnswer} onChange={e => setNewAnswer(e.target.value)} className="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none text-sm" placeholder="Provide step by step architecture troubleshooting explanations here..."></textarea>
        <button type="submit" className="bg-slate-800 hover:bg-slate-900 text-white font-bold px-5 py-2 rounded-lg text-sm transition-colors">Commit Solution</button>
      </form>
    </div>
  );
};

export default QuestionDetails;
```

### `src/pages/ChatRoom.jsx`
```jsx
import React, { useState, useEffect, useRef, useContext } from 'react';
import { AuthContext } from '../context/AuthContext';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import axios from 'axios';

const ChatRoom = () => {
  const { user } = useContext(AuthContext);
  const [rooms, setRooms] = useState([]);
  const [activeRoom, setActiveRoom] = useState('global-channel');
  const [messages, setMessages] = useState([]);
  const [inputMessage, setInputMessage] = useState('');
  const stompClientRef = useRef(null);
  const subscriptionRef = useRef(null);
  const scrollRef = useRef(null);

  useEffect(() => {
    fetchActiveRooms();
    connectWebSocket();
    return () => disconnectWebSocket();
  }, []);

  useEffect(() => {
    fetchRoomHistory(activeRoom);
    if (stompClientRef.current && stompClientRef.current.connected) {
      remapSubscription(activeRoom);
    }
  }, [activeRoom]);

  useEffect(() => {
    scrollRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages]);

  const fetchActiveRooms = async () => {
    try {
      const res = await axios.get('http://localhost:8080/api/chat/rooms');
      setRooms(res.data.length ? res.data : [{ roomName: 'global-channel', totalMessages: 0 }]);
    } catch (err) { console.error(err); }
  };

  const fetchRoomHistory = async (rm) => {
    try {
      const res = await axios.get(`http://localhost:8080/api/chat/history/${rm}`);
      setMessages(res.data);
    } catch (err) { console.error(err); }
  };

  const connectWebSocket = () => {
    const socket = new SockJS('http://localhost:8080/api/ws');
    const client = Stomp.over(socket);
    client.debug = null; // Suppress debug logging output blocks
    client.connect({}, () => {
      stompClientRef.current = client;
      remapSubscription(activeRoom);
    }, (err) => {
      setTimeout(connectWebSocket, 5000); // Trigger auto reconnection mapping loop back down
    });
  };

  const remapSubscription = (rm) => {
    if (subscriptionRef.current) subscriptionRef.current.unsubscribe();
    subscriptionRef.current = stompClientRef.current.subscribe(`/topic/room.${rm}`, (payload) => {
      const msg = JSON.parse(payload.body);
      setMessages(prev => [...prev, msg]);
    });
  };

  const disconnectWebSocket = () => {
    if (subscriptionRef.current) subscriptionRef.current.unsubscribe();
    if (stompClientRef.current) stompClientRef.current.disconnect();
  };

  const handleSend = (e) => {
    e.preventDefault();
    if (!inputMessage.trim() || !stompClientRef.current) return;
    const payload = {
      room: activeRoom,
      senderEmail: user.email,
      content: inputMessage.trim()
    };
    stompClientRef.current.send('/app/chat.sendMessage', {}, JSON.stringify(payload));
    setInputMessage('');
  };

  return (
    <div className="grid grid-cols-1 md:grid-cols-4 gap-6 h-[80vh]">
      <div className="bg-white p-4 rounded-xl shadow-sm border flex flex-col gap-3">
        <h3 className="font-extrabold text-slate-800 text-sm tracking-wider uppercase border-b pb-2">Active Chat Hubs</h3>
        <div className="flex-1 overflow-y-auto space-y-1">
          {rooms.map(r => (
            <button key={r.roomName} onClick={() => setActiveRoom(r.roomName)} className={`w-full text-left px-3 py-2 rounded-lg text-sm font-medium transition-colors ${activeRoom === r.roomName ? 'bg-indigo-600 text-white' : 'hover:bg-gray-100 text-gray-700'}`}>
              # {r.roomName}
            </button>
          ))}
        </div>
      </div>

      <div className="md:grid-cols-3 md:col-span-3 bg-white rounded-xl shadow-sm border flex flex-col overflow-hidden">
        <div className="bg-slate-800 text-white px-5 py-3 font-bold text-sm tracking-wide">
          Active Connection Terminal Channel: <span className="text-indigo-400">#{activeRoom}</span>
        </div>
        
        <div className="flex-1 p-4 overflow-y-auto space-y-3 bg-slate-50/50">
          {messages.map((m, idx) => {
            const isMe = m.senderEmail === user.email;
            return (
              <div key={idx} className={`flex flex-col ${isMe ? 'items-end' : 'items-start'}`}>
                <span className="text-[10px] text-gray-400 mb-0.5 px-1">{m.senderEmail}</span>
                <div className={`px-4 py-2 rounded-xl text-sm max-w-md ${isMe ? 'bg-indigo-600 text-white rounded-tr-none' : 'bg-white border text-gray-800 rounded-tl-none shadow-sm'}`}>
                  {m.content}
                </div>
              </div>
            );
          })}
          <div ref={scrollRef} />
        </div>

        <form onSubmit={handleSend} className="p-3 border-t bg-white flex gap-2">
          <input type="text" value={inputMessage} onChange={e => setInputMessage(e.target.value)} className="w-full px-4 py-2 border rounded-lg text-sm outline-none focus:ring-2 focus:ring-indigo-500" placeholder="Type direct broadcast message packets over broker..." />
          <button type="submit" className="bg-indigo-600 hover:bg-indigo-700 text-white font-bold px-6 py-2 rounded-lg text-sm transition-colors">Send</button>
        </form>
      </div>
    </div>
  );
};

export default ChatRoom;
```

### `src/pages/AnalyticsDashboard.jsx`
```jsx
import React, { useState, useEffect } from 'react';
import axios from 'axios';

const AnalyticsDashboard = () => {
  const [metrics, setMetrics] = useState(null);

  useEffect(() => {
    axios.get('http://localhost:8080/api/analytics/dashboard')
      .then(res => setMetrics(res.data))
      .catch(err => console.error(err));
  }, []);

  if (!metrics) return <div className="text-center py-12 text-gray-500">Parsing master metrics maps...</div>;

  return (
    <div className="space-y-6">
      <h2 className="text-2xl font-extrabold text-slate-800 tracking-tight">System Architectural Diagnostics Panel</h2>
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-6">
        <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
          <div className="text-sm font-bold text-gray-400 uppercase tracking-wider">Indexed User Profiles</div>
          <div className="text-4xl font-black text-slate-800 mt-2">{metrics.usersCount || 0}</div>
        </div>
        <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
          <div className="text-sm font-bold text-gray-400 uppercase tracking-wider">Discussion Topic Threads</div>
          <div className="text-4xl font-black text-indigo-600 mt-2">{metrics.questionsCount || 0}</div>
        </div>
        <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
          <div className="text-sm font-bold text-gray-400 uppercase tracking-wider">Compiled Answer Nodes</div>
          <div className="text-4xl font-black text-emerald-600 mt-2">{metrics.answersCount || 0}</div>
        </div>
      </div>
    </div>
  );
};

export default AnalyticsDashboard;
```

### `src/pages/AdminModeration.jsx`
```jsx
import React, { useState } from 'react';
import axios from 'axios';

const AdminModeration = () => {
  const [rawText, setRawText] = useState('');
  const [report, setReport] = useState(null);
  const [evaluating, setEvaluating] = useState(false);

  const handleInspect = async (e) => {
    e.preventDefault();
    if (!rawText.trim()) return;
    try {
      setEvaluating(true);
      const res = await axios.post('http://localhost:8080/api/moderation/inspect?type=MANUAL_TEST&entityId=0', { textContext: rawText });
      setReport(res.data);
    } catch (err) {
      console.error(err);
    } finally {
      setEvaluating(false);
    }
  };

  return (
    <div className="max-w-2xl mx-auto bg-white p-6 rounded-xl shadow-sm border space-y-6">
      <h2 className="text-xl font-bold text-slate-800 border-b pb-2">AI Content Safety Guard Interceptor</h2>
      <form onSubmit={handleInspect} className="space-y-4">
        <label className="block text-sm font-semibold text-gray-600">Simulate Input Payload Analysis</label>
        <textarea rows="4" value={rawText} onChange={e => setRawText(e.target.value)} className="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none text-sm" placeholder="Paste test content stream payload to parse toxicity thresholds..."></textarea>
        <button type="submit" disabled={evaluating} className="bg-red-600 hover:bg-red-700 text-white text-sm font-bold px-4 py-2 rounded-lg transition-colors shadow-sm">
          {evaluating ? 'Evaluating Safety Engine...' : 'Run Safety Diagnostics Pipeline'}
        </button>
      </form>

      {report && (
        <div className={`p-4 rounded-lg border text-sm ${report.flagged ? 'bg-red-50 border-red-200 text-red-800' : 'bg-emerald-50 border-emerald-200 text-emerald-800'}`}>
          <div className="font-bold uppercase tracking-wide text-xs mb-1">Inference Decision</div>
          <div>Status: <span className="font-extrabold">{report.flagged ? 'FLAGGED ANOMALY' : 'CLEAN CONTEXT SECURE'}</span></div>
          <div className="mt-1">Reasoning Analysis: {report.classificationReason}</div>
        </div>
      )}
    </div>
  );
};

export default AdminModeration;
```

---
### End of Frontend Implementation Document
