# BlindDate (Android)

Mystery-date dinner picker for couples who can't decide. You set dislikes, allergies, price, distance, rating floor—and BlindDate picks a spot and only reveals it when you arrive. 

## Tech stack
- Kotlin, Jetpack Compose, Material 3
- Hilt (DI), Coroutines/Flows
- Navigation Compose
- Room / DataStore
- Retrofit/OkHttp (or Ktor) for APIs
- Maps/Location (Play Services), Permissions
- CI: GitHub Actions (tests, lint, release APK artifacts)

## Architecture
- Modularized: `app`, `core`, `feature-*`
- Clean-ish layers (ui / domain / data), unidirectional data flow (UDF)
- ViewModel + StateFlow; repository interfaces; use cases where it helps

## Key features
- Onboarding: dislikes + allergies (filters)
- Night/date theme UI
- Home “question flow”: cuisine type, include fast food?, price/person, distance, rating floor
- Surprise reveal: restaurant name unlocks on arrival (geofence/GPS proximity)

## Screenshots/GIFs
<!-- TODO -->

License: Proprietary – All rights reserved. Contact for Permission
