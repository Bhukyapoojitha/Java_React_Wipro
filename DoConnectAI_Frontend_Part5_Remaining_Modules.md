# DoConnect AI Frontend - Part 5 (Chat, Notifications, Analytics, Moderation, AI, Profile)

## src/pages/Chat/ChatPage.jsx

```javascript
import { useState } from "react";

export default function ChatPage() {

  const [message,setMessage] =
    useState("");

  return (

    <div className="p-6">

      <h1 className="text-3xl font-bold mb-6">
        Chat Room
      </h1>

      <input
        className="border p-3 rounded w-full"
        placeholder="Type message"
        onChange={(e)=>setMessage(e.target.value)}
      />

    </div>

  );
}
```

## src/pages/Notifications/NotificationPage.jsx

```javascript
export default function NotificationPage(){

  return (

    <div className="p-6">

      <h1 className="text-3xl font-bold">
        Notifications
      </h1>

    </div>

  );
}
```

## src/pages/Analytics/AnalyticsPage.jsx

```javascript
import { useEffect,useState }
from "react";

export default function AnalyticsPage(){

 const [stats,setStats] =
  useState({});

 return (

  <div className="p-6">

   <h1 className="text-3xl font-bold">
    Analytics Dashboard
   </h1>

   <pre>
    {JSON.stringify(stats,null,2)}
   </pre>

  </div>

 );
}
```

## src/pages/Moderation/ModerationPage.jsx

```javascript
import { useState } from "react";

export default function ModerationPage(){

 const [content,setContent] =
  useState("");

 return (

  <div className="p-6">

   <textarea
    rows="5"
    className="border p-3 rounded w-full"
    onChange={(e)=>setContent(e.target.value)}
   />

  </div>

 );
}
```

## src/pages/AI/AIToolsPage.jsx

```javascript
import { useState } from "react";

export default function AIToolsPage(){

 const [text,setText] =
  useState("");

 return (

  <div className="p-6">

   <h1 className="text-3xl font-bold mb-6">
    AI Tools
   </h1>

   <textarea
    rows="6"
    className="border p-3 rounded w-full"
    onChange={(e)=>setText(e.target.value)}
   />

  </div>

 );
}
```

## src/pages/Sentiment/SentimentPage.jsx

```javascript
export default function SentimentPage(){

 return (
  <div className="p-6">
   Sentiment Analysis
  </div>
 );
}
```

## src/pages/Toxicity/ToxicityPage.jsx

```javascript
export default function ToxicityPage(){

 return (
  <div className="p-6">
   Toxicity Analyzer
  </div>
 );
}
```

## src/pages/Recommendations/RecommendationPage.jsx

```javascript
export default function RecommendationPage(){

 return (
  <div className="p-6">
   Recommendations
  </div>
 );
}
```

## src/pages/Profile/ProfilePage.jsx

```javascript
export default function ProfilePage(){

 return (

  <div className="p-6">

   <div className="bg-white shadow rounded-xl p-6">

    <h2 className="text-2xl font-bold">
      User Profile
    </h2>

   </div>

  </div>

 );
}
```

## src/pages/NotFound/NotFoundPage.jsx

```javascript
export default function NotFoundPage(){

 return (
  <h1>404 Not Found</h1>
 );
}
```

# Remaining small components

Create empty starter files:

- ChatBox.jsx
- ChatHeader.jsx
- ChatInput.jsx
- ChatMessage.jsx
- OnlineUsers.jsx
- NotificationBell.jsx
- NotificationCard.jsx
- DashboardChart.jsx
- TrendingTopics.jsx
- UserActivityChart.jsx
- AnalyticsCards.jsx
- ModerationForm.jsx
- ModerationResult.jsx
- SummaryPanel.jsx
- SentimentResult.jsx
- ToxicityResult.jsx
- RecommendationPanel.jsx
- ProfileCard.jsx
- ProfileStats.jsx

Export a simple React component from each and enhance later.
