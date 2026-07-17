# DailyRounds MCQ Quiz App

An Android quiz app built with Jetpack Compose for the DailyRounds assignment.

---

## Features

- **10-question MCQ quiz** loaded from a local JSON asset
- **Instant answer feedback** — tap an option to reveal correct (green) and wrong (red) answers
- **Auto-advance** — moves to next question 2 seconds after answering
- **Skip** — skip any unanswered question instantly (button or swipe left)
- **Swipe gesture** — swipe left anywhere on the screen to skip
- **Streak system** — 4 flame icons light up as your streak grows; badge appears at 3+ consecutive correct answers
- **Results screen** — shows correct/total score, highest streak, skipped count, and accuracy percentage
- **Restart** — reset all state and return to question 1

---

## Architecture

Clean separation of data, state, and UI layers:

```
app/src/main/java/com/binayshaw7777/dailyroundsassignment/
├── data/
│   ├── model/Question.kt           — domain data class
│   └── repository/QuizRepository.kt — loads JSON from assets via Gson
├── ui/
│   ├── quiz/
│   │   ├── QuizUiState.kt          — immutable state (streak, score, index, etc.)
│   │   └── QuizViewModel.kt        — AndroidViewModel; StateFlow + Channel effects
│   ├── navigation/
│   │   └── AppNavigation.kt        — NavHost wiring Splash → Quiz → Results
│   ├── screens/
│   │   ├── SplashScreen.kt         — fade-in, 1.5s delay
│   │   ├── QuizScreen.kt           — state holder + pure QuizContent composable
│   │   └── ResultsScreen.kt        — state holder + pure ResultsContent composable
│   ├── components/
│   │   └── QuizComponents.kt       — OptionButton, StreakFlames
│   └── theme/
│       ├── Color.kt                — dark palette
│       ├── Type.kt                 — typography scale
│       └── Theme.kt                — dark-only MaterialTheme
└── MainActivity.kt
```

**Patterns used:**
- **State holder / UI split** — every screen has a ViewModel-wired composable + a pure UI composable that takes plain state and callbacks; the pure composable is fully previewable
- **StateFlow + `update {}`** — all state mutations use `MutableStateFlow.update { }` for atomic, race-safe updates
- **Channel(BUFFERED).receiveAsFlow()** for one-shot navigation effects (navigate to results) to avoid event loss
- **No business logic in composables** — ViewModels own all quiz logic

---

## Quiz Flow

```
Splash (1.5s) → Question Screen → [answer / skip] → ... → Results Screen
                      ↑                                         |
                      └──────────── Restart Quiz ───────────────┘
```

**Streak logic:**
- Correct answer → streak + 1; update longestStreak
- Wrong answer → streak reset to 0
- At streak ≥ 3 → badge animates in; flame icons light up proportionally
- Streak resets on wrong answer only (skips do not reset streak)

---

## UX & Animations

| Interaction | Animation |
|---|---|
| Tap option | `animateColorAsState` 400ms color transition |
| Question advance | `AnimatedContent` slide-in from right |
| Streak badge | `AnimatedVisibility` fade in/out |
| Streak flames | `animateColorAsState` per flame icon |
| Splash | `Animatable` alpha fade-in |

---

## Edge Cases Handled

- **Double-tap guard** — `selectOption` and `skipQuestion` are no-ops if `isAnswered == true`
- **Swipe during answered state** — swipe gesture disabled while auto-advance delay is active
- **Empty JSON** — shows "No questions available" message
- **Malformed JSON** — `runCatching` in repository; loading state clears gracefully

---

## Tech Stack

| Library | Version | Purpose |
|---|---|---|
| Jetpack Compose BOM | 2026.02.01 | UI toolkit |
| Material 3 | BOM | Components and theming |
| Navigation Compose | 2.9.0 | Screen navigation |
| Lifecycle ViewModel Compose | 2.9.0 | ViewModel integration |
| Lifecycle Runtime Compose | 2.9.0 | `collectAsStateWithLifecycle` |
| Gson | 2.10.1 | JSON parsing |
| Kotlin Coroutines | 1.10.2 | Async operations |

**Min SDK:** 24 | **Compile SDK:** 37 | **Language:** Kotlin

---

## Building

```bash
./gradlew assembleDebug
```

APK output: `app/build/outputs/apk/debug/app-debug.apk`
