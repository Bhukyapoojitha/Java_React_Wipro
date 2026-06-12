# Phase 4 - Questions & Answers Module

## Pages Included

- Questions List Page
- Ask Question Page
- Question Details Page
- Answer Submission Form
- Question Service API

---

## Folder Structure

```text
src
├── services
│   └── questionService.js
├── pages
│   ├── questions
│   │   ├── QuestionsPage.jsx
│   │   ├── AskQuestionPage.jsx
│   │   └── QuestionDetailsPage.jsx
```

---

## questionService.js

```javascript
import axiosInstance from "../api/axiosConfig";

export const getAllQuestions = () =>
  axiosInstance.get("/questions");

export const createQuestion = (data) =>
  axiosInstance.post("/questions", data);

export const getQuestionById = (id) =>
  axiosInstance.get(`/questions/${id}`);

export const createAnswer = (id, data) =>
  axiosInstance.post(`/answers/${id}`, data);
```

---

## QuestionsPage.jsx

```jsx
import { useEffect, useState } from "react";
import { getAllQuestions } from "../../services/questionService";

export default function QuestionsPage() {

  const [questions, setQuestions] = useState([]);

  useEffect(() => {
    loadQuestions();
  }, []);

  const loadQuestions = async () => {
    const response = await getAllQuestions();
    setQuestions(response.data);
  };

  return (
    <div className="p-6">

      <h1 className="text-3xl font-bold mb-6">
        Questions
      </h1>

      <div className="space-y-4">

        {questions.map((q) => (
          <div
            key={q.id}
            className="bg-white shadow rounded-xl p-5"
          >
            <h2 className="font-bold text-xl">
              {q.title}
            </h2>

            <p className="mt-2 text-slate-600">
              {q.content}
            </p>
          </div>
        ))}

      </div>

    </div>
  );
}
```

---

## AskQuestionPage.jsx

```jsx
import { useState } from "react";
import { createQuestion } from "../../services/questionService";

export default function AskQuestionPage() {

  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");

  const submitQuestion = async () => {

    await createQuestion({
      title,
      content
    });

    alert("Question Added");
  };

  return (
    <div className="p-6">

      <h1 className="text-3xl font-bold mb-6">
        Ask Question
      </h1>

      <input
        className="w-full border p-3 rounded mb-4"
        placeholder="Question Title"
        onChange={(e) => setTitle(e.target.value)}
      />

      <textarea
        className="w-full border p-3 rounded mb-4"
        rows="6"
        placeholder="Question Content"
        onChange={(e) => setContent(e.target.value)}
      />

      <button
        onClick={submitQuestion}
        className="bg-indigo-600 text-white px-6 py-3 rounded"
      >
        Submit
      </button>

    </div>
  );
}
```

---

## QuestionDetailsPage.jsx

```jsx
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getQuestionById } from "../../services/questionService";

export default function QuestionDetailsPage() {

  const { id } = useParams();

  const [question, setQuestion] = useState(null);

  useEffect(() => {
    loadQuestion();
  }, []);

  const loadQuestion = async () => {
    const response = await getQuestionById(id);
    setQuestion(response.data);
  };

  if (!question) return <p>Loading...</p>;

  return (
    <div className="p-6">

      <h1 className="text-3xl font-bold">
        {question.title}
      </h1>

      <p className="mt-4">
        {question.content}
      </p>

    </div>
  );
}
```

---

## Answer Form Component

```jsx
import { useState } from "react";

export default function AnswerForm({ onSubmit }) {

  const [answer, setAnswer] = useState("");

  return (
    <div className="mt-6">

      <textarea
        rows="4"
        className="w-full border p-3 rounded"
        onChange={(e) => setAnswer(e.target.value)}
      />

      <button
        className="mt-3 bg-indigo-600 text-white px-4 py-2 rounded"
        onClick={() => onSubmit(answer)}
      >
        Submit Answer
      </button>

    </div>
  );
}
```

---

## Routes

```jsx
<Route path="/questions" element={<QuestionsPage />} />
<Route path="/ask-question" element={<AskQuestionPage />} />
<Route path="/questions/:id" element={<QuestionDetailsPage />} />
```

---

## Next Phase

Chat Module
Notifications Module
WebSocket Integration
