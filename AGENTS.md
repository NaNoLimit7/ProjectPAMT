# AGENTS Guide for ProjectPAMT

## Baseline context
- Existing AI instruction files were searched via glob and none were found (`README.md`, `AGENT*.md`, `.github/copilot-instructions.md`, Cursor/Windsurf/Cline rules).
- This is a single-module Android app (`:app`) using Kotlin + Jetpack Compose + Navigation + Supabase Auth (`settings.gradle.kts`, `app/build.gradle.kts`).

## Architecture you need first
- UI flow is Compose-only: `MainActivity` sets `AppNavigation()` as the root (`app/src/main/java/com/example/projectpamt/MainActivity.kt`).
- Auth gate happens before route graph rendering: `AppNavigation` waits on `authCheckState` (`Checking`/`Authenticated`/`NotAuthenticated`) and picks start destination (`navigation/AppNavigation.kt`).
- Main layers are: `ui/*Screen.kt` -> `viewmodel/auth/AuthViewModel.kt` -> `data/repository/AuthRepository.kt` -> `data/SupabaseClientProvider.kt`.
- ViewModel exposes form fields as `StateFlow` (`email`, `password`, `fullname`) and a separate async state (`AuthUiState`), then screens consume with `collectAsStateWithLifecycle` in `MainNavHost`.
- Success navigation is centralized in `LaunchedEffect(uiState)` inside `MainNavHost`; it redirects to dashboard and calls `authViewModel.resetState()`.

## Integration boundaries and external services
- Supabase is configured in a singleton provider with `Auth` plugin installed (`data/SupabaseClientProvider.kt`).
- Auth lifecycle depends on Supabase `sessionStatus` flow and `awaitInitialization()` to avoid false logout on cold start (`data/repository/AuthRepository.kt`).
- `register()` sends `full_name` in signup metadata JSON; keep that field key stable unless backend schema changes (`AuthRepository.register`).
- INTERNET permission is required and already declared (`app/src/main/AndroidManifest.xml`).

## Project-specific coding patterns
- Routing uses a sealed class `Screen` with string routes (`navigation/Screen.kt`); add new destinations there first.
- UI text/comments are mixed Indonesian/English; error defaults are Indonesian (e.g., `"Login gagal"`, `"Register gagal"` in `AuthViewModel`). Keep language style consistent in the touched feature.
- Compose screens are mostly stateless and callback-driven (`LoginScreen`, `RegisterScreen`, `DashboardScreen`); keep business logic in ViewModel/repository.
- Current model classes under `data/model/` are plain transport/domain structures; only some are `@Serializable` yet, so check serialization needs per model before using Supabase/PostgREST.

## Build/test workflows (verified constraints)
- Wrapper script is present but not executable in this workspace (`./gradlew` returns permission denied).
- Java runtime is missing in current environment (`sh ./gradlew ...` fails with "Unable to locate a Java Runtime").
- Typical commands once JDK is available:

```bash
chmod +x ./gradlew
./gradlew :app:assembleDebug
./gradlew :app:testDebugUnitTest
./gradlew :app:connectedDebugAndroidTest
```

## Safe change strategy for this repo
- When adjusting auth behavior, update both `AuthCheckState` gating and nav transitions to avoid loops between `Login` and `Dashboard`.
- Prefer extending `AuthRepository` for Supabase calls rather than calling Supabase directly from UI or navigation code.
- Keep `SupabaseClientProvider` as the single source for client config; do not create ad-hoc clients in screens/viewmodels.

