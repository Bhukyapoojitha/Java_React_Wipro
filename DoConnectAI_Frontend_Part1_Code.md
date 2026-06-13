# DoConnect AI Frontend - Part 1 (API + Services)

## src/api/axiosConfig.js

```javascript
import axios from "axios";

const axiosInstance = axios.create({
  baseURL: "http://localhost:8080",
  headers: {
    "Content-Type": "application/json"
  }
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

## src/api/authApi.js

```javascript
import axiosInstance from "./axiosConfig";

export const loginApi = (data) =>
  axiosInstance.post("/auth/login", data);

export const registerApi = (data) =>
  axiosInstance.post("/auth/register", data);
```

## src/api/questionApi.js

```javascript
import axiosInstance from "./axiosConfig";

export const getQuestionsApi = () =>
  axiosInstance.get("/questions");

export const createQuestionApi = (data) =>
  axiosInstance.post("/questions", data);

export const getQuestionApi = (id) =>
  axiosInstance.get(`/questions/${id}`);
```

## src/api/answerApi.js

```javascript
import axiosInstance from "./axiosConfig";

export const createAnswerApi = (id,data) =>
  axiosInstance.post(`/answers/${id}`, data);
```

## src/services/authService.js

```javascript
import { loginApi, registerApi } from "../api/authApi";

export const login = async(data)=>{
  const res = await loginApi(data);
  return res.data;
};

export const register = async(data)=>{
  const res = await registerApi(data);
  return res.data;
};
```

## src/services/questionService.js

```javascript
import {
  getQuestionsApi,
  createQuestionApi,
  getQuestionApi
} from "../api/questionApi";

export const getQuestions = async()=>
 (await getQuestionsApi()).data;

export const createQuestion = async(data)=>
 (await createQuestionApi(data)).data;

export const getQuestion = async(id)=>
 (await getQuestionApi(id)).data;
```

## src/services/answerService.js

```javascript
import { createAnswerApi } from "../api/answerApi";

export const createAnswer = async(id,data)=>
 (await createAnswerApi(id,data)).data;
```
