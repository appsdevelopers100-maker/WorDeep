# WorDeep (WorDig)

**Every Word Hides More** — A bilingual English-Arabic word puzzle web application built with React, TypeScript, Vite, and Tailwind CSS.

## Features

- **50 Puzzle Levels**: Discover hidden English words inside each level's main word with Arabic translations and categories.
- **Left-to-Right Selection Rule**: Tap letters strictly from left to right (tap the last selected letter again to undo, or clear the selection).
- **Live Prefix Matching**: Target word slots highlight as your formed prefix matches hidden words.
- **Hints & Scoring**: Earn points based on word length and spend 50 points to reveal a hidden word.
- **Pronunciation**: Uses the browser Web Speech API (`speechSynthesis`) to pronounce main words and discovered words.
- **Educational Exam Checkpoints**: Multiple-choice English/Arabic checkpoints after every 5 levels.
- **Info Screens & Persistence**: Includes How to Play, FAQ, Privacy Policy, and Terms & Conditions screens, with progress saved in `localStorage`.

## Development

```bash
npm install
npm run dev
```

## Build & Typecheck

```bash
npm run lint
npm run build
```
