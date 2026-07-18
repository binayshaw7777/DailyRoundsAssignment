# Compose Previews Export

This document lists all screens and components with Compose previews in the project.

## Screens

### 1. SplashScreen
- **File**: `ui/splash/SplashScreen.kt`
- **Preview**: `SplashScreenPreview()`
- **Description**: Initial loading screen with animated logo

### 2. OnboardingScreen
- **File**: `ui/onboarding/OnboardingScreen.kt`
- **Preview**: `OnboardingScreenPreview()`
- **Description**: First-time user onboarding flow

### 3. UserDetailsScreen
- **File**: `ui/userdetails/UserDetailsScreen.kt`
- **Preview**: `UserDetailsScreenPreview()`
- **Description**: User name input screen

### 4. QuizStartScreen
- **File**: `ui/quizstart/QuizStartScreen.kt`
- **Preview**: 
  - `QuizStartScreenPreview()` - Regular user with stats
  - `QuizStartScreenFirstTimePreview()` - First-time user
- **Description**: Quiz start screen with statistics

### 5. HomeScreen
- **File**: `ui/home/HomeScreen.kt`
- **Preview**: `HomeScreenPreview()`
- **Description**: Main home screen with tabs (Quiz, Leaderboard, Settings)

### 6. QuizScreen
- **File**: `ui/quiz/QuizScreen.kt`
- **Preview**: 
  - `QuizScreenLoadingPreview()` - Loading state
  - `QuizScreenContentPreview()` - Active quiz with question
- **Description**: Quiz gameplay screen

### 7. ResultsScreen
- **File**: `ui/results/ResultsScreen.kt`
- **Preview**: `ResultsScreenPreview()`
- **Description**: Quiz results with confetti celebration

### 8. LeaderboardScreen
- **File**: `ui/leaderboard/LeaderboardScreen.kt`
- **Preview**: `LeaderboardScreenPreview()`
- **Description**: Leaderboard with quiz results

### 9. SettingsScreen
- **File**: `ui/settings/SettingsScreen.kt`
- **Preview**: `SettingsScreenPreview()`
- **Description**: App settings screen

## Components

### OptionButton
- **File**: `ui/components/QuizComponents.kt`
- **Previews**: 
  - `OptionButtonAllStatesPreview()` - Shows all button states (default, correct, wrong, code)
- **Description**: Quiz option button with animation

### StreakFlames
- **File**: `ui/components/QuizComponents.kt`
- **Preview**: `StreakFlamesAllStatesPreview()`
- **Description**: Flame streak indicator (0, 2, 4+ flames)

## Export Instructions

### Using Android Studio
1. Open any file with `@Preview` annotation
2. Click "Run" on the preview panel
3. Screenshots will be rendered in the Preview panel

### Using Gradle Task
First, install the app and test APKs on your device/emulator:
```bash
./gradlew :app:installDebug :app:installDebugAndroidTest
```

Then run the export:
```bash
./gradlew :app:exportPreviews
```

### Manual Export
1. Connect device/emulator
2. Install the test APK:
```bash
./gradlew :app:installDebugAndroidTest
```
3. Run the instrumented test:
```bash
adb shell am instrument -w \
  -e class com.binayshaw7777.dailyroundsassignment.PreviewExportTest#exportAllPreviews \
  com.binayshaw7777.dailyroundsassignment.test/androidx.test.runner.AndroidJUnitRunner
```
4. Pull screenshots:
```bash
./export-previews.sh
```

## Output Location
Screenshots are saved to: `previews/` folder in project root
