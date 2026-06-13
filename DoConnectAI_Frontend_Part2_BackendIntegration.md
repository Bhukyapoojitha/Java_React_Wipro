# DoConnect AI Frontend Part 2 - Backend Integration API Layer


## AiController.java

- PostMapping "/suggest-answer/{qId}"
- PostMapping "/summarize"

## AnalyticsController.java

- GetMapping "/dashboard"

## AnswerController.java

- PostMapping "/post/{qId}"
- GetMapping "/thread/{qId}"
- GetMapping "/{id}"
- PutMapping "/update/{id}"
- DeleteMapping "/delete/{id}"

## AuthController.java

- PostMapping "/register"
- PostMapping "/login"

## ChatController.java

- GetMapping "/history/{room}"
- GetMapping "/rooms"
- PostMapping "/message"

## ModerationController.java

- PostMapping "/inspect"

## NotificationController.java

- GetMapping "/unread"
- PutMapping "/read/{id}"

## QuestionController.java

- PostMapping "/create"
- GetMapping "/list"
- GetMapping "/{id}"
- PutMapping "/update/{id}"
- DeleteMapping "/delete/{id}"

## UserController.java

- GetMapping "/all"
- GetMapping "/profile"