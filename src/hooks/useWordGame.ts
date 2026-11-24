import { useCallback, useEffect, useState } from 'react';
import { EXAM_QUESTIONS, LEVELS } from '../data/wordGameRepository';
import { AppScreen, Level, WordItem } from '../model/wordGameModels';
import { speakText } from '../utils/textToSpeech';

const STORAGE_KEY = 'wordig_game_prefs';

interface StoredProgress {
  score: number;
  unlockedLevel: number;
  currentLevelIndex: number;
}

function loadStoredProgress(): StoredProgress {
  try {
    const raw = window.localStorage.getItem(STORAGE_KEY);
    if (!raw) {
      return { score: 100, unlockedLevel: 1, currentLevelIndex: 0 };
    }
    const parsed = JSON.parse(raw) as Partial<StoredProgress>;
    return {
      score: typeof parsed.score === 'number' ? parsed.score : 100,
      unlockedLevel: typeof parsed.unlockedLevel === 'number' ? parsed.unlockedLevel : 1,
      currentLevelIndex:
        typeof parsed.currentLevelIndex === 'number' &&
        parsed.currentLevelIndex >= 0 &&
        parsed.currentLevelIndex < LEVELS.length
          ? parsed.currentLevelIndex
          : 0,
    };
  } catch {
    return { score: 100, unlockedLevel: 1, currentLevelIndex: 0 };
  }
}

function saveStoredProgress(progress: StoredProgress): void {
  try {
    window.localStorage.setItem(STORAGE_KEY, JSON.stringify(progress));
  } catch {
    // Ignore storage errors
  }
}

function getPointsForLength(length: number): number {
  switch (length) {
    case 2:
      return 5;
    case 3:
      return 8;
    case 4:
      return 12;
    case 5:
      return 15;
    default:
      return 20;
  }
}

function cloneExtractedWords(level: Level): WordItem[] {
  return level.extractedWords.map((item) => ({ ...item, isDiscovered: false }));
}

export function useWordGame() {
  const [initial] = useState<StoredProgress>(() => loadStoredProgress());

  const [currentScreen, setCurrentScreen] = useState<AppScreen>(AppScreen.SPLASH);
  const [score, setScore] = useState<number>(initial.score);
  const [unlockedLevel, setUnlockedLevel] = useState<number>(initial.unlockedLevel);
  const [currentLevelIndex, setCurrentLevelIndex] = useState<number>(initial.currentLevelIndex);

  const levels = LEVELS;
  const currentLevel = levels[currentLevelIndex] ?? levels[0];

  const [selectedLetterIndices, setSelectedLetterIndices] = useState<number[]>([]);
  const [currentExtractedWords, setCurrentExtractedWords] = useState<WordItem[]>(() =>
    cloneExtractedWords(levels[initial.currentLevelIndex] ?? levels[0])
  );
  const [discoveredExtraWords, setDiscoveredExtraWords] = useState<string[]>([]);
  const [isLevelComplete, setIsLevelComplete] = useState<boolean>(false);
  const [soundEnabled, setSoundEnabled] = useState<boolean>(true);

  const [currentExamIndex, setCurrentExamIndex] = useState<number>(0);
  const [examScore, setExamScore] = useState<number>(0);
  const [examCompleted, setExamCompleted] = useState<boolean>(false);

  useEffect(() => {
    saveStoredProgress({ score, unlockedLevel, currentLevelIndex });
  }, [score, unlockedLevel, currentLevelIndex]);

  const navigateTo = useCallback((screen: AppScreen) => {
    setCurrentScreen(screen);
  }, []);

  const toggleSound = useCallback(() => {
    setSoundEnabled((prev) => !prev);
  }, []);

  const speakWord = useCallback(
    (word: string) => {
      if (soundEnabled) {
        speakText(word);
      }
    },
    [soundEnabled]
  );

  const loadLevel = useCallback(
    (index: number) => {
      const lvl = levels[index] ?? levels[0];
      setCurrentExtractedWords(cloneExtractedWords(lvl));
      setDiscoveredExtraWords([]);
      setSelectedLetterIndices([]);
      setIsLevelComplete(false);
    },
    [levels]
  );

  const selectLevel = useCallback(
    (index: number) => {
      if (index >= 0 && index < levels.length) {
        setCurrentLevelIndex(index);
        loadLevel(index);
        setCurrentScreen(AppScreen.GAMEPLAY);
      }
    },
    [levels.length, loadLevel]
  );

  const onLetterClicked = useCallback(
    (index: number) => {
      if (isLevelComplete) return;

      if (selectedLetterIndices.length > 0) {
        const lastIndex = selectedLetterIndices[selectedLetterIndices.length - 1];
        if (index <= lastIndex) {
          if (index === lastIndex) {
            setSelectedLetterIndices(selectedLetterIndices.slice(0, -1));
          }
          return;
        }
      }

      const nextSelected = [...selectedLetterIndices, index];
      const formedString = nextSelected.map((i) => currentLevel.mainWord[i]).join('');

      const matchIdx = currentExtractedWords.findIndex(
        (wordItem) =>
          !wordItem.isDiscovered && wordItem.word.toUpperCase() === formedString.toUpperCase()
      );

      if (matchIdx !== -1) {
        const matched = currentExtractedWords[matchIdx];
        const updatedWords = currentExtractedWords.map((item, idx) =>
          idx === matchIdx ? { ...item, isDiscovered: true } : item
        );
        const pts = getPointsForLength(matched.length);

        setScore((prev) => prev + pts);
        setCurrentExtractedWords(updatedWords);
        setSelectedLetterIndices([]);
        speakWord(matched.word);

        if (updatedWords.every((item) => item.isDiscovered)) {
          setIsLevelComplete(true);
          if (currentLevelIndex + 1 >= unlockedLevel && unlockedLevel < levels.length) {
            setUnlockedLevel(currentLevelIndex + 2);
          }
        }
      } else {
        setSelectedLetterIndices(nextSelected);
      }
    },
    [
      currentExtractedWords,
      currentLevel.mainWord,
      currentLevelIndex,
      isLevelComplete,
      levels.length,
      selectedLetterIndices,
      speakWord,
      unlockedLevel,
    ]
  );

  const clearSelection = useCallback(() => {
    setSelectedLetterIndices([]);
  }, []);

  const useHint = useCallback(() => {
    if (score < 50 || levels.length === 0) return;
    const undiscoveredIdx = currentExtractedWords.findIndex((item) => !item.isDiscovered);
    if (undiscoveredIdx === -1) return;

    const undiscovered = currentExtractedWords[undiscoveredIdx];
    const updatedWords = currentExtractedWords.map((item, idx) =>
      idx === undiscoveredIdx ? { ...item, isDiscovered: true } : item
    );

    setScore((prev) => prev - 50 + getPointsForLength(undiscovered.length));
    setCurrentExtractedWords(updatedWords);
    setSelectedLetterIndices([]);
    speakWord(undiscovered.word);

    if (updatedWords.every((item) => item.isDiscovered)) {
      setIsLevelComplete(true);
      if (currentLevelIndex + 1 >= unlockedLevel && unlockedLevel < levels.length) {
        setUnlockedLevel(currentLevelIndex + 2);
      }
    }
  }, [currentExtractedWords, currentLevelIndex, levels.length, score, speakWord, unlockedLevel]);

  const startExam = useCallback(() => {
    setCurrentExamIndex(0);
    setExamScore(0);
    setExamCompleted(false);
    setCurrentScreen(AppScreen.EXAM);
  }, []);

  const nextLevel = useCallback(() => {
    if (currentLevelIndex + 1 < levels.length) {
      if ((currentLevelIndex + 1) % 5 === 0) {
        startExam();
      } else {
        const nextIdx = currentLevelIndex + 1;
        setCurrentLevelIndex(nextIdx);
        loadLevel(nextIdx);
        setCurrentScreen(AppScreen.GAMEPLAY);
      }
    } else {
      setCurrentScreen(AppScreen.LEVEL_MAP);
    }
  }, [currentLevelIndex, levels.length, loadLevel, startExam]);

  const answerExam = useCallback(
    (selectedOptionIndex: number) => {
      if (currentExamIndex < EXAM_QUESTIONS.length) {
        if (EXAM_QUESTIONS[currentExamIndex].correctIndex === selectedOptionIndex) {
          setExamScore((prev) => prev + 25);
          setScore((prev) => prev + 20);
        }
        if (currentExamIndex + 1 < EXAM_QUESTIONS.length) {
          setCurrentExamIndex((prev) => prev + 1);
        } else {
          setExamCompleted(true);
        }
      }
    },
    [currentExamIndex]
  );

  const finishExam = useCallback(() => {
    const nextIdx = currentLevelIndex + 1;
    setCurrentLevelIndex(nextIdx);
    if (nextIdx < levels.length) {
      loadLevel(nextIdx);
      setCurrentScreen(AppScreen.GAMEPLAY);
    } else {
      setCurrentScreen(AppScreen.LEVEL_MAP);
    }
  }, [currentLevelIndex, levels.length, loadLevel]);

  return {
    currentScreen,
    score,
    unlockedLevel,
    currentLevelIndex,
    levels,
    currentLevel,
    selectedLetterIndices,
    currentExtractedWords,
    discoveredExtraWords,
    isLevelComplete,
    soundEnabled,
    currentExamIndex,
    examScore,
    examCompleted,
    navigateTo,
    toggleSound,
    speakWord,
    selectLevel,
    onLetterClicked,
    clearSelection,
    useHint,
    nextLevel,
    startExam,
    answerExam,
    finishExam,
  };
}

export type WordGameController = ReturnType<typeof useWordGame>;
