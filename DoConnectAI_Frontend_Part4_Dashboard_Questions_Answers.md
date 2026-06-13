# DoConnect AI Frontend - Part 4 (Dashboard + Questions + Answers)

## src/components/dashboard/StatsCard.jsx

```javascript
export default function StatsCard({ title, value }) {
  return (
    <div className="bg-white rounded-xl shadow p-5">
      <h3>{title}</h3>
      <h2 className="text-3xl font-bold">{value}</h2>
    </div>
  );
}
```

## src/components/dashboard/DashboardCards.jsx

```javascript
import StatsCard from "./StatsCard";

export default function DashboardCards({ stats }) {
  return (
    <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
      <StatsCard title="Users" value={stats.users || 0} />
      <StatsCard title="Questions" value={stats.questions || 0} />
      <StatsCard title="Answers" value={stats.answers || 0} />
      <StatsCard title="Notifications" value={stats.notifications || 0} />
    </div>
  );
}
```

## src/pages/Dashboard/DashboardPage.jsx

```javascript
import { useEffect, useState } from "react";
import DashboardLayout from "../../layouts/DashboardLayout";
import DashboardCards from "../../components/dashboard/DashboardCards";

export default function DashboardPage() {

  const [stats] = useState({
    users: 0,
    questions: 0,
    answers: 0,
    notifications: 0
  });

  return (
    <DashboardLayout>
      <DashboardCards stats={stats} />
    </DashboardLayout>
  );
}
```

## src/components/questions/QuestionCard.jsx

```javascript
export default function QuestionCard({ question }) {

  return (
    <div className="bg-white shadow rounded-xl p-5">

      <h2 className="font-bold text-xl">
        {question.title}
      </h2>

      <p className="mt-2">
        {question.content}
      </p>

    </div>
  );
}
```

## src/components/questions/QuestionList.jsx

```javascript
import QuestionCard from "./QuestionCard";

export default function QuestionList({ questions }) {

  return (
    <div className="space-y-4">

      {questions.map((question) => (
        <QuestionCard
          key={question.id}
          question={question}
        />
      ))}

    </div>
  );
}
```

## src/pages/Questions/QuestionListPage.jsx

```javascript
import { useEffect, useState } from "react";
import { getQuestions } from "../../services/questionService";
import QuestionList from "../../components/questions/QuestionList";

export default function QuestionListPage() {

  const [questions, setQuestions] = useState([]);

  useEffect(() => {
    loadQuestions();
  }, []);

  const loadQuestions = async () => {

    const data =
      await getQuestions();

    setQuestions(data);
  };

  return (

    <div className="p-6">

      <h1 className="text-3xl font-bold mb-6">
        Questions
      </h1>

      <QuestionList questions={questions} />

    </div>

  );
}
```

## src/components/questions/QuestionForm.jsx

```javascript
import { useState } from "react";

export default function QuestionForm({ onSubmit }) {

  const [title,setTitle] = useState("");
  const [content,setContent] = useState("");

  return (

    <div>

      <input
        className="w-full border p-3 rounded mb-4"
        placeholder="Title"
        onChange={(e)=>setTitle(e.target.value)}
      />

      <textarea
        rows="5"
        className="w-full border p-3 rounded mb-4"
        placeholder="Content"
        onChange={(e)=>setContent(e.target.value)}
      />

      <button
        onClick={()=>onSubmit({
          title,
          content
        })}
        className="bg-indigo-600 text-white px-4 py-2 rounded"
      >
        Save
      </button>

    </div>

  );
}
```

## src/pages/Questions/CreateQuestionPage.jsx

```javascript
import { createQuestion }
from "../../services/questionService";

import QuestionForm
from "../../components/questions/QuestionForm";

export default function CreateQuestionPage(){

  const saveQuestion = async(data)=>{

    await createQuestion(data);

    alert("Question Created");
  };

  return (
    <QuestionForm
      onSubmit={saveQuestion}
    />
  );
}
```

## src/components/answers/AnswerForm.jsx

```javascript
import { useState } from "react";

export default function AnswerForm({ onSubmit }) {

  const [content,setContent] =
    useState("");

  return (

    <div>

      <textarea
        rows="4"
        className="w-full border p-3 rounded"
        onChange={(e)=>
          setContent(e.target.value)
        }
      />

      <button
        onClick={()=>
          onSubmit(content)
        }
        className="mt-3 bg-indigo-600 text-white px-4 py-2 rounded"
      >
        Submit Answer
      </button>

    </div>
  );
}
```

## src/pages/Answers/AnswerPage.jsx

```javascript
import AnswerForm
from "../../components/answers/AnswerForm";

export default function AnswerPage(){

  const submitAnswer = async(answer)=>{

    console.log(answer);
  };

  return (

    <div className="p-6">

      <AnswerForm
        onSubmit={submitAnswer}
      />

    </div>
  );
}
```
