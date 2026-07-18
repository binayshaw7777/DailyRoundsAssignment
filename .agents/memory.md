# Memory Log

Record of all completed actions in the development of the DailyRounds Assignment project.

## [2026-07-17] Initial Setup and Rules
- Git Init, rules.md, checklist.md, assets dir created.

## [2026-07-17] Figma Design Extraction
- Used local Figma MCP bridge (http://localhost:3001) to export 5 screens as PNG.
- Screens: Default question, Correct+streak, Correct+5streak, Wrong+reveal, Results.
- Dark theme: bg=#0D0D11, options=dark navy pills, correct=green, wrong=dark red, streak flames emoji.
- Results: two stat cards (Correct Answers, Highest Streak), Restart Quiz light pill button.

## [2026-07-17] Full App Implementation (Phase 2-4)

### Architecture
- Data layer: `data/model/Question.kt`, `data/repository/QuizRepository.kt` (Gson + assets)
- State: `ui/quiz/QuizUiState.kt` (computed props: currentQuestion, progress, streakActive)
- ViewModel: `ui/quiz/QuizViewModel.kt` (AndroidViewModel, StateFlow + Channel effects)
- Navigation: `ui/navigation/AppNavigation.kt` (NavHost, Splash→Quiz→Results)

### Gradle changes
- compileSdk upgraded to 37 (needed by core-ktx 1.19.0, lifecycle 2.9.0)
- Added: gson 2.10.1, lifecycle-viewmodel-compose 2.9.0, lifecycle-runtime-compose 2.9.0, navigation-compose 2.9.0, kotlinx-coroutines-android 1.10.2, material-icons-core

### UI Files
- `ui/theme/Color.kt` — custom dark palette
- `ui/theme/Type.kt` — quiz typography
- `ui/theme/Theme.kt` — dark-only MaterialTheme
- `ui/screens/SplashScreen.kt` — fade-in, 1.5s, navigates out
- `ui/screens/QuizScreen.kt` — state holder + pure QuizContent
- `ui/screens/ResultsScreen.kt` — state holder + pure ResultsContent + StatCard
- `ui/components/QuizComponents.kt` — OptionButton (animateColorAsState) + StreakFlames

- BUILD SUCCESSFUL after fixing: animateColorAsState wrong package, ResultsScreen weight import conflict.

## [2026-07-17] Fix Preview Export Errors
- Split `PreviewExportTest` into 15 individual `@Test` methods to prevent `IllegalStateException` from multiple `setContent` calls.
- Resolved Gradle Configuration Cache serialization issue in `exportPreviews` task by extracting `rootProject.projectDir.absolutePath` to a task configuration-phase variable `rootDirPath` and running all tests under the test class.

