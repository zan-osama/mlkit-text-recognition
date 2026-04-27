# Plan: Add Dependencies to Project

## Task Scope

- Add these dependencies into project
```
dependencies {
  // To recognize Latin script
  implementation 'com.google.mlkit:text-recognition:16.0.1'

  // To recognize Chinese script
  implementation 'com.google.mlkit:text-recognition-chinese:16.0.1'

  // To recognize Devanagari script
  implementation 'com.google.mlkit:text-recognition-devanagari:16.0.1'

  // To recognize Japanese script
  implementation 'com.google.mlkit:text-recognition-japanese:16.0.1'

  // To recognize Korean script
  implementation 'com.google.mlkit:text-recognition-korean:16.0.1'
}
```

## Todo & Progress

- [x] Add ML Kit dependencies to libs.versions.toml
- [x] Add ML Kit dependencies to build.gradle.kts
- [x] Build and verify changes

## Notes

- Updated version to 16.0.1
- All 5 ML Kit text recognition dependencies added
