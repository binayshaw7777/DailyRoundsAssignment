# RO - Mcq Quiz

## Assignment: Build a Quiz App Using Provided JSON

### Overview

You're given a gist of 10 questions in API: **Question Json List**

---

### Build an Android app that:

#### 1. Launch & Load
*   On launch, parse the JSON into a `List<Question>`.
*   Show a brief splash or loading indicator while you prepare data.

#### 2. Quiz Flow
*   **Question Screen**
    *   Display the current question (text + four options).
    *   Tapping an option reveals the right answer and user-selected answer.
    *   Then after 2 seconds, it immediately advances to the next question.
    *   Tapping **"Skip"** advances immediately to the next question.
*   **Streak Logic**
    *   Track consecutive correct answers.
    *   When the streak count hits 3, the streak badge lights up.
    *   Think of a creative way to engage the user.
    *   Any wrong answer resets the streak counter to 0.

#### 3. End of Quiz
*   After the 10th question, transition to a **Results Screen** that shows:
    *   Results as per design elements.
    *   Correct/Total score.
    *   *Optional:* Skipped questions.
    *   Your longest streak achieved.
*   **Options on Results Screen:**
    *   **Restart Quiz:** Reset all counters and go back to Question 1.

---

### Design Inspiration

The below are the inspirations. It's not important to follow the below design; any creative functional solution is acceptable.

> *The shared design is just a reference: Not a constraint! Feel free to reimagine the solution and go with what you believe delivers the best user experience.*

---

### Deliverables

*   **Git repo**
    *   Full Android Studio running functional project.
    *   `Readme.md`: List your implementation details clearly.
*   **Non-Functional Requirements**
    *   Beautify the user experience by adding **Animations**, **Gestures** (e.g., swipe right to navigate questions during test), etc.
    *   Implement your solution with clear separation of UI, state, and data layers (Architecture).
    *   Consistent design (colors, typography, spacing).
    *   Use material components and follow accessibility best practices.

---

### What do we look for?

*   **Functionality:** API Handling, question navigation, streak logic, results screen.
*   **Code Quality:** Architecture, readability, and clean code principles.
*   **UI/UX:** Intuitive review flow, responsive layouts, and a delightful user experience.
*   **Attention to Detail:** Engaging but unobtrusive micro-interactions, complete end-to-end experience.

---

**Good luck—looking forward to the full journey from first question to final score!**

If there are any doubts, feel free to share them with **harish@dailyrounds.org**

*Remember: Focus on delivering a polished and functional experience.*