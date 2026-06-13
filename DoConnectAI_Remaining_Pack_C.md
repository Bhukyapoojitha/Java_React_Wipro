# DoConnect AI Remaining Files Pack C

## src/api/aiApi.js

```javascript
import axiosInstance from "./axiosConfig";

export const summarizeText = (data) =>
  axiosInstance.post("/ai/summarize", data);

export const suggestAnswer = (questionId) =>
  axiosInstance.get(`/ai/suggest-answer/${questionId}`);
```

## src/api/sentimentApi.js

```javascript
import axiosInstance from "./axiosConfig";

export const analyzeSentiment = (data) =>
  axiosInstance.post("/sentiment/analyze", data);
```

## src/api/recommendationApi.js

```javascript
import axiosInstance from "./axiosConfig";

export const getRecommendations = (userId) =>
  axiosInstance.get(`/recommendations/${userId}`);
```

## src/services/aiService.js

```javascript
import * as api from "../api/aiApi";

export const summarizeText = async (data) =>
 (await api.summarizeText(data)).data;

export const suggestAnswer = async (id) =>
 (await api.suggestAnswer(id)).data;
```

## src/services/sentimentService.js

```javascript
import * as api from "../api/sentimentApi";

export const analyzeSentiment = async(data)=>
 (await api.analyzeSentiment(data)).data;
```

## src/services/recommendationService.js

```javascript
import * as api from "../api/recommendationApi";

export const getRecommendations = async(id)=>
 (await api.getRecommendations(id)).data;
```

## src/components/ai/SummaryPanel.jsx

```javascript
export default function SummaryPanel({ summary }) {
  return (
    <div className="bg-white rounded-xl shadow p-4">
      {summary}
    </div>
  );
}
```

## src/components/ai/SentimentResult.jsx

```javascript
export default function SentimentResult({ result }) {
  return (
    <div className="bg-white rounded-xl shadow p-4">
      Sentiment: {result}
    </div>
  );
}
```

## src/components/ai/ToxicityResult.jsx

```javascript
export default function ToxicityResult({ result }) {
  return (
    <div className="bg-white rounded-xl shadow p-4">
      Toxicity Score: {result}
    </div>
  );
}
```

## src/components/ai/RecommendationPanel.jsx

```javascript
export default function RecommendationPanel({ recommendations }) {
  return (
    <ul>
      {recommendations?.map((item,index)=>(
        <li key={index}>{item}</li>
      ))}
    </ul>
  );
}
```

## src/components/common/SearchBar.jsx

```javascript
export default function SearchBar({ value,onChange }) {
  return (
    <input
      className="border p-3 rounded w-full"
      placeholder="Search..."
      value={value}
      onChange={onChange}
    />
  );
}
```

## src/components/common/Pagination.jsx

```javascript
export default function Pagination({
 currentPage,
 totalPages,
 onPageChange
}){
 return (
  <div className="flex gap-2">
   <button
    onClick={()=>onPageChange(currentPage-1)}
   >
    Prev
   </button>

   <span>
    {currentPage}/{totalPages}
   </span>

   <button
    onClick={()=>onPageChange(currentPage+1)}
   >
    Next
   </button>
  </div>
 );
}
```

## src/components/dashboard/RecentActivity.jsx

```javascript
export default function RecentActivity({ activities }) {
  return (
    <div className="bg-white shadow rounded-xl p-5">
      {activities?.map((a,index)=>(
        <div key={index}>{a}</div>
      ))}
    </div>
  );
}
```

## src/components/dashboard/QuickActions.jsx

```javascript
export default function QuickActions() {
  return (
    <div className="flex gap-3">
      <button className="bg-indigo-600 text-white px-4 py-2 rounded">
        Ask Question
      </button>
    </div>
  );
}
```

## Remaining tiny helper files

```javascript
export default function ComponentName(){
 return <div>ComponentName</div>;
}
```

Use the above template for any leftover placeholder component.
