# TwoDew App

## About The Project

**TwoDew** is a relatively simple to-do list application designed to help users keep track of tasks

## Main Technologies Used

- **Kotlin:** Primary programming language for the app.
- **Android Jetpack Compose:** Used for building UI.
- **Room Database:** For local data storage.
- **Dagger-Hilt:** For dependency injection.
- **Coroutines:** For handling async tasks.

## Features

- **Task Management:** Add, edit, delete, and update tasks.
- **Task Prioritization:** Mark tasks as important.
- **Data Persistence:** Tasks are stored locally via Room db to retain data across app restarts.
- **Tests:** There are tests for both the viewmodel functions and database functions

## Potential Improvements

- With more time, I would have taken modularization further. The only separated concern I have is
  the repository at the moment
- I left some commented out Log statements for error handling, ideally this would be more robust and
  only log in debug build etc
- The UI could be improved. The toasts are a basic way to convey success/error, and I should have
  taken more time to apply consistent theming/colors etc
- Navigation could be explored instead of the current single screen with dialog setup
- Functionality to undo a deletion would be a nice touch
- Add snapshot tests and maybe an espresso test
- Add libraries like spotless/detekt for code quality and consistency
- Make viewmodel tests less flaky when run all at once and add Turbine to enhance existing vm tests
- Explore using fakes instead of mocks for testing
- cleanup hardcoded strings
- update to Java 11

## Other Considerations

- Please run on a physical device. It will work on emulator, but there is a bug with the dialog
  composable in emulators: https://issuetracker.google.com/issues/289117017