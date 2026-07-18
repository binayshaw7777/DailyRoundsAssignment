# Project Rules

This document outlines the rules for AI assistance and development workflows on this project.

## 1. Branching & Commit Guidelines
* **Branches:** Work must be done on the appropriate branch: `feature`, `fixed`, or `ph`. Do not work on a single main branch.
* **No Auto-Commits:** Always ask the user before committing or pushing any code. Never push directly without explicit approval.

## 2. Verification & Building
* **Big Milestones:** Build the project using Gradle after completing any major milestone to ensure compilation succeeds.
* **Command:** `./gradlew assembleDebug` (or appropriate build variant command).

## 3. AI Agent Tasks & Documentation
* **Memory Tracking:** Maintain [memory.md](file:///Users/binay/AndroidStudioProjects/DailyRoundsAssignment/memory.md) containing a historical record of all completed tasks.
* **Checklist Management:** Maintain [checklist.md](file:///Users/binay/AndroidStudioProjects/DailyRoundsAssignment/checklist.md) containing the active checklist of current and next steps.
* **Token Resilience:** Frequently mark progress in `memory.md` and `checklist.md` so work can be resumed if the AI session or token expires.
* **Sub-agents:** Utilize two dedicated sub-agents to maximize parallelization and separation of concerns (e.g., Code Implementation Sub-agent and Review/Testing Sub-agent).

## 4. Quality & Edge Cases
* **Edge & Corner Cases:** Actively think of, list, and verify edge cases:
  * Rapid double-taps on quiz answers.
  * Screen rotation during the 2-second auto-advance.
  * Empty or malformed JSON payloads.
  * Out-of-bounds index access during question transit or skip.
* **Skills Usage:** Reference and strictly apply all Jetpack Compose, Kotlin, and Android development guidelines under the `.agents/skills` folder.
