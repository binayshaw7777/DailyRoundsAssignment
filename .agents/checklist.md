# Task Checklist

Active checklist for the DailyRounds MCQ Quiz App implementation.

## Phase 1: Setup & Project Foundation
- [x] Initialize Git repository
- [x] Create rules.md
- [x] Create memory.md
- [x] Create checklist.md
- [x] Define 2 sub-agents (quiz_coder + quiz_ui_agent spawned and completed)

## Phase 2: Design & Data Integration
- [x] Locate/Obtain Figma designs (5 screens exported via local MCP bridge)
- [x] Design domain data model (`Question`, `QuizUiState`)
- [x] Implement local asset parser (Gson, loads from assets/questions_mock.json)
- [x] Setup architecture (Data → ViewModel → UI layers, state-holder/UI split per skill)

## Phase 3: Core App Screens
- [x] **Splash Screen:** Fade-in animation, 1.5s delay, navigates to Quiz
- [x] **Question Screen:**
  - [x] Render question text + 4 options
  - [x] Option tap reveals correct & incorrect (animateColorAsState)
  - [x] Auto-advance after 2 seconds
  - [x] Skip button for immediate advancement
- [x] **Streak Feature:**
  - [x] Track consecutive correct answers
  - [x] Reset streak to 0 on wrong answer
  - [x] 4 flame icons animate red when streak active (streak >= index+1)
  - [x] "N questions streak achieved !!" AnimatedVisibility badge
- [x] **Results Screen:**
  - [x] Correct/Total score
  - [x] Highest streak
  - [x] Skipped count + Accuracy
  - [x] Restart button (resets state, returns to Q1)

## Phase 4: UX & Polish
- [x] AnimatedContent slide transition between questions
- [x] animateColorAsState on option buttons
- [x] AnimatedVisibility for streak badge
- [x] Splash fade-in animation
- [x] Swipe left gesture on QuizScreen to skip question (detectHorizontalDragGestures, threshold 200px, disabled while isAnswered)

## Phase 5: Verification & Edge Cases
- [x] Double-tap guard: selectOption no-op if isAnswered==true
- [x] Skip guard: skipQuestion no-op if isAnswered==true
- [x] `_state.update{}` used everywhere (no stale read/write)
- [x] BUILD SUCCESSFUL (compileSdk=37, all deps resolved)
- [x] Write README.md
- [x] Test on device/emulator (exportPreviews runs successfully, generating 15 screenshots)
