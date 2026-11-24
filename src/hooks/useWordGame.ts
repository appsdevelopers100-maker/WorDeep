import { useCallback, useEffect, useMemo, useRef, useState } from 'react';
import { getWordArabic, isValidEnglishWord, warmUpDictionary } from '../data/englishWords';
import { getLevelsForDifficulty, getQuestionsForExam, LEVELS } from '../data/wordGameRepository';
import {
  AppScreen,
  calculateSpeedBonus,
  createEmptyDailyMissionsState,
  DAILY_MISSIONS,
  DailyMissionId,
  DailyMissionsState,
  DIFFICULTY_CONFIG,
  GameDifficulty,
  getClaimableDailyMissionsCount,
  getDailyMissionProgress,
  getLevelTimeLimitSeconds,
  getTodayDateKey,
  Level,
  normalizeDailyMissionsState,
  WordItem,
} from '../model/wordGameModels';
import { hapticFeedback } from '../utils/hapticFeedback';
import { soundEffects } from '../utils/soundEffects';
import { speakText } from '../utils/textToSpeech';

const STORAGE_KEY = 'wordig_game_prefs';

interface StoredProgress {
  score: number;
  unlockedLevel: number;
  currentLevelIndex: number;
  examBestScores: Record<number, number>;
  difficulty: GameDifficulty;
  timeTrialEnabled: boolean;
  rewardedTargetsByLevel: Record<number, string[]>;
  rewardedExtrasByLevel: Record<number, string[]>;
  rewardedTimeTrialLevels: number[];
  dailyMissions: DailyMissionsState;
}

export interface ExtraWordFeedback {
  id: number;
  word: string;
  arabic?: string | null;
  points: number;
  isDuplicate: boolean;
  kind?: 'extra' | 'target';
}

function sanitizeWordsByLevel(raw: unknown): Record<number, string[]> {
  const result: Record<number, string[]> = {};
  if (!raw || typeof raw !== 'object') return result;
  for (const [k, v] of Object.entries(raw as Record<string, unknown>)) {
    const levelId = Number(k);
    if (!Number.isInteger(levelId) || levelId <= 0 || !Array.isArray(v)) continue;
    const unique = Array.from(
      new Set(
        v
          .filter((item): item is string => typeof item === 'string')
          .map((item) => item.trim().toUpperCase())
          .filter((item) => item.length >= 3)
      )
    );
    if (unique.length > 0) {
      result[levelId] = unique;
    }
  }
  return result;
}

function sanitizeLevelIdList(raw: unknown): number[] {
  if (!Array.isArray(raw)) return [];
  return Array.from(
    new Set(raw.filter((item): item is number => typeof item === 'number' && Number.isInteger(item) && item > 0))
  );
}

function loadStoredProgress(): StoredProgress {
  try {
    const raw = window.localStorage.getItem(STORAGE_KEY);
    if (!raw) {
      return {
        score: 100,
        unlockedLevel: 1,
        currentLevelIndex: 0,
        examBestScores: {},
        difficulty: GameDifficulty.MEDIUM,
        timeTrialEnabled: false,
        rewardedTargetsByLevel: {},
        rewardedExtrasByLevel: {},
        rewardedTimeTrialLevels: [],
        dailyMissions: createEmptyDailyMissionsState(),
      };
    }
    const parsed = JSON.parse(raw) as Partial<StoredProgress>;
    const examBestScores: Record<number, number> = {};
    if (parsed.examBestScores && typeof parsed.examBestScores === 'object') {
      for (const [k, v] of Object.entries(parsed.examBestScores)) {
        const numKey = Number(k);
        if (Number.isInteger(numKey) && typeof v === 'number' && v >= 0 && v <= 10) {
          examBestScores[numKey] = v;
        }
      }
    }
    const savedDifficulty =
      parsed.difficulty === GameDifficulty.EASY ||
      parsed.difficulty === GameDifficulty.MEDIUM ||
      parsed.difficulty === GameDifficulty.HARD
        ? parsed.difficulty
        : GameDifficulty.MEDIUM;

    return {
      score: typeof parsed.score === 'number' ? parsed.score : 100,
      unlockedLevel: typeof parsed.unlockedLevel === 'number' ? parsed.unlockedLevel : 1,
      currentLevelIndex:
        typeof parsed.currentLevelIndex === 'number' &&
        parsed.currentLevelIndex >= 0 &&
        parsed.currentLevelIndex < LEVELS.length
          ? parsed.currentLevelIndex
          : 0,
      examBestScores,
      difficulty: savedDifficulty,
      timeTrialEnabled: typeof parsed.timeTrialEnabled === 'boolean' ? parsed.timeTrialEnabled : false,
      rewardedTargetsByLevel: sanitizeWordsByLevel(parsed.rewardedTargetsByLevel),
      rewardedExtrasByLevel: sanitizeWordsByLevel(parsed.rewardedExtrasByLevel),
      rewardedTimeTrialLevels: sanitizeLevelIdList(parsed.rewardedTimeTrialLevels),
      dailyMissions: normalizeDailyMissionsState(parsed.dailyMissions),
    };
  } catch {
    return {
      score: 100,
      unlockedLevel: 1,
      currentLevelIndex: 0,
      examBestScores: {},
      difficulty: GameDifficulty.MEDIUM,
      timeTrialEnabled: false,
      rewardedTargetsByLevel: {},
      rewardedExtrasByLevel: {},
      rewardedTimeTrialLevels: [],
      dailyMissions: createEmptyDailyMissionsState(),
    };
  }
}

function saveStoredProgress(progress: StoredProgress): void {
  try {
    window.localStorage.setItem(STORAGE_KEY, JSON.stringify(progress));
  } catch {
    // Ignore storage errors
  }
}

function getBasePointsForLength(length: number): number {
  switch (length) {
    case 3:
      return 8;
    case 4:
      return 12;
    case 5:
      return 15;
    default:
      return length >= 6 ? 20 : 0;
  }
}

function getPointsForLength(length: number, multiplier: number): number {
  return Math.round(getBasePointsForLength(length) * multiplier);
}

function cloneExtractedWords(level: Level): WordItem[] {
  return level.extractedWords
    .filter((item) => item.length >= 3)
    .map((item) => ({
      ...item,
      isDiscovered: false,
      revealedIndices: [],
      wasAlreadyRewarded: false,
      earnedPoints: 0,
    }));
}

export function useWordGame() {
  const [initial] = useState<StoredProgress>(() => loadStoredProgress());

  const [currentScreen, setCurrentScreen] = useState<AppScreen>(AppScreen.SPLASH);
  const [difficulty, setDifficultyState] = useState<GameDifficulty>(initial.difficulty);
  const [timeTrialEnabled, setTimeTrialEnabledState] = useState<boolean>(initial.timeTrialEnabled);
  const [score, setScore] = useState<number>(initial.score);
  const [unlockedLevel, setUnlockedLevel] = useState<number>(initial.unlockedLevel);
  const [currentLevelIndex, setCurrentLevelIndex] = useState<number>(initial.currentLevelIndex);
  const [examBestScores, setExamBestScores] = useState<Record<number, number>>(
    initial.examBestScores
  );
  const [rewardedTargetsByLevel, setRewardedTargetsByLevel] = useState<Record<number, string[]>>(
    initial.rewardedTargetsByLevel
  );
  const [rewardedExtrasByLevel, setRewardedExtrasByLevel] = useState<Record<number, string[]>>(
    initial.rewardedExtrasByLevel
  );
  const [rewardedTimeTrialLevels, setRewardedTimeTrialLevels] = useState<number[]>(
    initial.rewardedTimeTrialLevels
  );
  const [dailyMissions, setDailyMissions] = useState<DailyMissionsState>(() =>
    normalizeDailyMissionsState(initial.dailyMissions)
  );
  const [isDailyMissionsOpen, setIsDailyMissionsOpen] = useState<boolean>(false);

  const difficultyConfig = DIFFICULTY_CONFIG[difficulty];
  const extraWordPoints = timeTrialEnabled ? 10 : 5;
  const levels = useMemo(() => getLevelsForDifficulty(difficulty), [difficulty]);
  const currentLevel = levels[currentLevelIndex] ?? levels[0];
  const currentTargetWordCount = useMemo(
    () => currentLevel.extractedWords.filter((item) => item.length >= 3).length,
    [currentLevel.extractedWords]
  );
  const levelTimeLimitSeconds = useMemo(
    () => getLevelTimeLimitSeconds(currentTargetWordCount, difficulty),
    [currentTargetWordCount, difficulty]
  );

  const [selectedLetterIndices, setSelectedLetterIndices] = useState<number[]>([]);
  const [currentExtractedWords, setCurrentExtractedWords] = useState<WordItem[]>(() =>
    cloneExtractedWords(getLevelsForDifficulty(initial.difficulty)[initial.currentLevelIndex] ?? levels[0])
  );
  const [discoveredExtraWords, setDiscoveredExtraWords] = useState<string[]>(() => {
    const startLvl = getLevelsForDifficulty(initial.difficulty)[initial.currentLevelIndex] ?? levels[0];
    return initial.rewardedExtrasByLevel[startLvl.id] ?? [];
  });
  const [extraWordFeedback, setExtraWordFeedback] = useState<ExtraWordFeedback | null>(null);
  const [isLevelComplete, setIsLevelComplete] = useState<boolean>(false);
  const [timeRemainingSeconds, setTimeRemainingSeconds] = useState<number>(() =>
    getLevelTimeLimitSeconds(
      (getLevelsForDifficulty(initial.difficulty)[initial.currentLevelIndex] ?? levels[0]).extractedWords.filter(
        (item) => item.length >= 3
      ).length,
      initial.difficulty
    )
  );
  const [isTimeUp, setIsTimeUp] = useState<boolean>(false);
  const [lastSpeedBonus, setLastSpeedBonus] = useState<number>(0);
  const [soundEnabled, setSoundEnabled] = useState<boolean>(true);

  // Exam state
  const [activeExamNumber, setActiveExamNumber] = useState<number>(1);
  const [currentExamIndex, setCurrentExamIndex] = useState<number>(0);
  const [examCorrectCount, setExamCorrectCount] = useState<number>(0);
  const [examScore, setExamScore] = useState<number>(0);
  const [examLastEarnedDelta, setExamLastEarnedDelta] = useState<number>(0);
  const [examCompleted, setExamCompleted] = useState<boolean>(false);
  const [examEnteredFromMap, setExamEnteredFromMap] = useState<boolean>(false);

  const feedbackTimerRef = useRef<number | null>(null);

  useEffect(() => {
    const warmTimer = window.setTimeout(() => {
      warmUpDictionary();
      if (typeof window !== 'undefined' && 'speechSynthesis' in window) {
        try {
          window.speechSynthesis.getVoices();
        } catch {
          // ignore
        }
      }
    }, 60);
    return () => window.clearTimeout(warmTimer);
  }, []);

  useEffect(() => {
    saveStoredProgress({
      score,
      unlockedLevel,
      currentLevelIndex,
      examBestScores,
      difficulty,
      timeTrialEnabled,
      rewardedTargetsByLevel,
      rewardedExtrasByLevel,
      rewardedTimeTrialLevels,
      dailyMissions,
    });
  }, [
    score,
    unlockedLevel,
    currentLevelIndex,
    examBestScores,
    difficulty,
    timeTrialEnabled,
    rewardedTargetsByLevel,
    rewardedExtrasByLevel,
    rewardedTimeTrialLevels,
    dailyMissions,
  ]);

  useEffect(() => {
    const syncDailyDate = () => {
      const today = getTodayDateKey();
      setDailyMissions((prev) =>
        prev.date === today ? prev : createEmptyDailyMissionsState(today)
      );
    };
    const handleVisibility = () => {
      if (document.visibilityState === 'visible') {
        syncDailyDate();
      }
    };
    window.addEventListener('focus', syncDailyDate);
    document.addEventListener('visibilitychange', handleVisibility);
    return () => {
      window.removeEventListener('focus', syncDailyDate);
      document.removeEventListener('visibilitychange', handleVisibility);
    };
  }, []);

  useEffect(() => {
    if (!timeTrialEnabled || currentScreen !== AppScreen.GAMEPLAY || isLevelComplete || isTimeUp) {
      return;
    }
    const intervalId = window.setInterval(() => {
      setTimeRemainingSeconds((prev) => {
        if (prev <= 1) {
          setIsTimeUp(true);
          return 0;
        }
        return prev - 1;
      });
    }, 1000);
    return () => window.clearInterval(intervalId);
  }, [timeTrialEnabled, currentScreen, isLevelComplete, isTimeUp]);

  useEffect(() => {
    return () => {
      if (feedbackTimerRef.current !== null) {
        window.clearTimeout(feedbackTimerRef.current);
      }
    };
  }, []);

  const showWordFeedback = useCallback(
    (
      word: string,
      isDuplicate: boolean,
      pointsAwarded: number,
      kind: 'extra' | 'target' = 'extra'
    ) => {
      if (feedbackTimerRef.current !== null) {
        window.clearTimeout(feedbackTimerRef.current);
      }
      setExtraWordFeedback({
        id: Date.now(),
        word,
        arabic: getWordArabic(word),
        points: isDuplicate ? 0 : pointsAwarded,
        isDuplicate,
        kind,
      });
      feedbackTimerRef.current = window.setTimeout(() => {
        setExtraWordFeedback(null);
      }, 2200);
    },
    []
  );

  const setDifficulty = useCallback(
    (nextDifficulty: GameDifficulty) => {
      setDifficultyState(nextDifficulty);
      const nextLevels = getLevelsForDifficulty(nextDifficulty);
      const targetLevel = nextLevels[currentLevelIndex] ?? nextLevels[0];
      setCurrentExtractedWords(cloneExtractedWords(targetLevel));
      setDiscoveredExtraWords(rewardedExtrasByLevel[targetLevel.id] ?? []);
      setExtraWordFeedback(null);
      setSelectedLetterIndices([]);
      setIsLevelComplete(false);
      setIsTimeUp(false);
      setLastSpeedBonus(0);
      setTimeRemainingSeconds(
        getLevelTimeLimitSeconds(
          targetLevel.extractedWords.filter((item) => item.length >= 3).length,
          nextDifficulty
        )
      );
    },
    [currentLevelIndex, rewardedExtrasByLevel]
  );

  const setTimeTrialEnabled = useCallback(
    (enabled: boolean) => {
      setTimeTrialEnabledState(enabled);
      setIsTimeUp(false);
      setLastSpeedBonus(0);
      setTimeRemainingSeconds(getLevelTimeLimitSeconds(currentTargetWordCount, difficulty));
    },
    [currentTargetWordCount, difficulty]
  );

  const navigateTo = useCallback((screen: AppScreen) => {
    const today = getTodayDateKey();
    setDailyMissions((prev) =>
      prev.date === today ? prev : createEmptyDailyMissionsState(today)
    );
    setCurrentScreen(screen);
  }, []);

  const openDailyMissions = useCallback(() => {
    const today = getTodayDateKey();
    setDailyMissions((prev) =>
      prev.date === today ? prev : createEmptyDailyMissionsState(today)
    );
    setIsDailyMissionsOpen(true);
  }, []);

  const closeDailyMissions = useCallback(() => {
    setIsDailyMissionsOpen(false);
  }, []);

  const claimDailyMission = useCallback(
    (missionId: DailyMissionId) => {
      const mission = DAILY_MISSIONS.find((m) => m.id === missionId);
      if (!mission) return;
      const today = getTodayDateKey();
      const normalized = normalizeDailyMissionsState(dailyMissions, today);
      const progress = getDailyMissionProgress(normalized, missionId);
      if (progress < mission.target || normalized.claimedIds.includes(missionId)) {
        if (normalized.date !== dailyMissions.date) {
          setDailyMissions(normalized);
        }
        return;
      }

      setDailyMissions({
        ...normalized,
        claimedIds: [...normalized.claimedIds, missionId],
      });
      setScore((prev) => prev + mission.rewardPoints);
      soundEffects.playWordMatch();
      hapticFeedback.vibrateWordMatch();
    },
    [dailyMissions]
  );

  useEffect(() => {
    soundEffects.setEnabled(soundEnabled);
    hapticFeedback.setEnabled(soundEnabled);
  }, [soundEnabled]);

  const toggleSound = useCallback(() => {
    setSoundEnabled((prev) => {
      const next = !prev;
      soundEffects.setEnabled(next);
      hapticFeedback.setEnabled(next);
      return next;
    });
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
      setDiscoveredExtraWords(rewardedExtrasByLevel[lvl.id] ?? []);
      setExtraWordFeedback(null);
      setSelectedLetterIndices([]);
      setIsLevelComplete(false);
      setIsTimeUp(false);
      setLastSpeedBonus(0);
      setTimeRemainingSeconds(
        getLevelTimeLimitSeconds(
          lvl.extractedWords.filter((item) => item.length >= 3).length,
          difficulty
        )
      );
    },
    [difficulty, levels, rewardedExtrasByLevel]
  );

  const retryCurrentLevel = useCallback(() => {
    loadLevel(currentLevelIndex);
  }, [currentLevelIndex, loadLevel]);

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

  const awardExtraWord = useCallback(
    (upperWord: string) => {
      if (upperWord.length < 3) return;
      const levelId = currentLevel.id;
      const persistedForLevel = rewardedExtrasByLevel[levelId] ?? [];
      const alreadyRewarded =
        persistedForLevel.includes(upperWord) || discoveredExtraWords.includes(upperWord);
      setSelectedLetterIndices([]);

      if (alreadyRewarded) {
        if (!discoveredExtraWords.includes(upperWord)) {
          setDiscoveredExtraWords((prev) => [...prev, upperWord]);
        }
        soundEffects.playExtraWord(true);
        hapticFeedback.vibrateExtraWord(true);
        showWordFeedback(upperWord, true, 0, 'extra');
        return;
      }

      setDiscoveredExtraWords((prev) => (prev.includes(upperWord) ? prev : [...prev, upperWord]));
      setRewardedExtrasByLevel((prev) => {
        const existing = prev[levelId] ?? [];
        if (existing.includes(upperWord)) return prev;
        return { ...prev, [levelId]: [...existing, upperWord] };
      });
      setScore((prev) => prev + extraWordPoints);
      setDailyMissions((prev) => {
        const normalized = normalizeDailyMissionsState(prev);
        return {
          ...normalized,
          extraWordsFound: normalized.extraWordsFound + 1,
        };
      });
      soundEffects.playExtraWord(false);
      hapticFeedback.vibrateExtraWord(false);
      speakWord(upperWord);
      showWordFeedback(upperWord, false, extraWordPoints, 'extra');
    },
    [
      currentLevel.id,
      discoveredExtraWords,
      extraWordPoints,
      rewardedExtrasByLevel,
      showWordFeedback,
      speakWord,
    ]
  );

  const onLetterClicked = useCallback(
    (index: number) => {
      if (isLevelComplete || isTimeUp) return;

      if (selectedLetterIndices.length > 0) {
        const lastIndex = selectedLetterIndices[selectedLetterIndices.length - 1];
        if (index <= lastIndex) {
          if (index === lastIndex) {
            const trimmed = selectedLetterIndices.slice(0, -1);
            soundEffects.playTileClick(trimmed.length, true);
            hapticFeedback.vibrateTileClick(true);
            setSelectedLetterIndices(trimmed);
          }
          return;
        }
      }

      const nextSelected = [...selectedLetterIndices, index];
      const formedString = nextSelected.map((i) => currentLevel.mainWord[i]).join('');
      const formedUpper = formedString.toUpperCase();

      if (formedUpper.length < 3) {
        soundEffects.playTileClick(nextSelected.length, false);
        hapticFeedback.vibrateTileClick(false);
        setSelectedLetterIndices(nextSelected);
        return;
      }

      const matchIdx = currentExtractedWords.findIndex(
        (wordItem) =>
          !wordItem.isDiscovered && wordItem.word.toUpperCase() === formedUpper
      );

      if (matchIdx !== -1) {
        const levelId = currentLevel.id;
        const matched = currentExtractedWords[matchIdx];
        const matchedUpper = matched.word.toUpperCase();
        const persistedTargets = rewardedTargetsByLevel[levelId] ?? [];
        const wasAlreadyRewarded = persistedTargets.includes(matchedUpper);
        const pts = wasAlreadyRewarded
          ? 0
          : getPointsForLength(matched.length, difficultyConfig.multiplier);

        const allIndices = Array.from({ length: matched.length }, (_, i) => i);
        const updatedWords = currentExtractedWords.map((item, idx) =>
          idx === matchIdx
            ? {
                ...item,
                isDiscovered: true,
                revealedIndices: allIndices,
                wasAlreadyRewarded,
                earnedPoints: pts,
              }
            : item
        );

        const completedAll = updatedWords.every((item) => item.isDiscovered);
        const canAwardSpeedBonus =
          completedAll && timeTrialEnabled && !rewardedTimeTrialLevels.includes(levelId);
        const speedBonus = canAwardSpeedBonus
          ? calculateSpeedBonus(
              timeRemainingSeconds,
              levelTimeLimitSeconds,
              difficultyConfig.multiplier
            )
          : 0;

        if (!wasAlreadyRewarded) {
          setRewardedTargetsByLevel((prev) => {
            const existing = prev[levelId] ?? [];
            if (existing.includes(matchedUpper)) return prev;
            return { ...prev, [levelId]: [...existing, matchedUpper] };
          });
        } else {
          showWordFeedback(matched.word, true, 0, 'target');
        }

        if (canAwardSpeedBonus && speedBonus > 0) {
          setRewardedTimeTrialLevels((prev) =>
            prev.includes(levelId) ? prev : [...prev, levelId]
          );
        }

        if (pts + speedBonus > 0) {
          setScore((prev) => prev + pts + speedBonus);
        }

        setCurrentExtractedWords(updatedWords);
        setSelectedLetterIndices([]);
        soundEffects.playWordMatch();
        hapticFeedback.vibrateWordMatch();
        speakWord(matched.word);

        if (completedAll) {
          setLastSpeedBonus(speedBonus);
          setIsLevelComplete(true);
          setDailyMissions((prev) => {
            const normalized = normalizeDailyMissionsState(prev);
            return {
              ...normalized,
              levelsPassed: normalized.levelsPassed + 1,
            };
          });
          if (currentLevelIndex + 1 >= unlockedLevel && unlockedLevel < levels.length) {
            setUnlockedLevel(currentLevelIndex + 2);
          }
        }
        return;
      }

      const isTargetWord = currentExtractedWords.some(
        (item) => item.word.toUpperCase() === formedUpper
      );
      const isMainWord = formedUpper === currentLevel.mainWord.toUpperCase();

      if (!isTargetWord && !isMainWord && isValidEnglishWord(formedUpper)) {
        const isPrefixOfUndiscoveredTarget = currentExtractedWords.some(
          (item) =>
            !item.isDiscovered &&
            item.word.toUpperCase().startsWith(formedUpper) &&
            item.word.length > formedUpper.length
        );

        if (!isPrefixOfUndiscoveredTarget) {
          awardExtraWord(formedUpper);
          return;
        }
      }

      soundEffects.playTileClick(nextSelected.length, false);
      hapticFeedback.vibrateTileClick(false);
      setSelectedLetterIndices(nextSelected);
    },
    [
      awardExtraWord,
      currentExtractedWords,
      currentLevel.id,
      currentLevel.mainWord,
      currentLevelIndex,
      difficultyConfig.multiplier,
      isLevelComplete,
      isTimeUp,
      levelTimeLimitSeconds,
      levels.length,
      rewardedTargetsByLevel,
      rewardedTimeTrialLevels,
      selectedLetterIndices,
      showWordFeedback,
      speakWord,
      timeRemainingSeconds,
      timeTrialEnabled,
      unlockedLevel,
    ]
  );

  const formedWord = selectedLetterIndices
    .map((i) => currentLevel.mainWord[i] ?? '')
    .join('')
    .toUpperCase();

  const canClaimFormedExtraWord =
    formedWord.length >= 3 &&
    formedWord !== currentLevel.mainWord.toUpperCase() &&
    !currentExtractedWords.some((item) => item.word.toUpperCase() === formedWord) &&
    isValidEnglishWord(formedWord);

  const isFormedExtraAlreadyClaimed =
    canClaimFormedExtraWord &&
    ((rewardedExtrasByLevel[currentLevel.id] ?? []).includes(formedWord) ||
      discoveredExtraWords.includes(formedWord));

  const claimFormedExtraWord = useCallback(() => {
    if (!canClaimFormedExtraWord) return;
    awardExtraWord(formedWord);
  }, [awardExtraWord, canClaimFormedExtraWord, formedWord]);

  const clearSelection = useCallback(() => {
    setSelectedLetterIndices([]);
  }, []);

  const useHint = useCallback(() => {
    if (score < 50 || levels.length === 0 || isLevelComplete || isTimeUp) return;

    const targetIdx = currentExtractedWords.findIndex(
      (item) => !item.isDiscovered && item.revealedIndices.length < item.length
    );
    if (targetIdx === -1) return;

    const target = currentExtractedWords[targetIdx];
    let nextRevealCharIndex = -1;
    for (let i = 0; i < target.length; i++) {
      if (!target.revealedIndices.includes(i)) {
        nextRevealCharIndex = i;
        break;
      }
    }
    if (nextRevealCharIndex === -1) return;

    const nextRevealedIndices = [...target.revealedIndices, nextRevealCharIndex];
    const completedWordByHint = nextRevealedIndices.length >= target.length;
    const levelId = currentLevel.id;
    const wasAlreadyRewarded =
      completedWordByHint &&
      (rewardedTargetsByLevel[levelId] ?? []).includes(target.word.toUpperCase());

    const updatedWords = currentExtractedWords.map((item, idx) =>
      idx === targetIdx
        ? {
            ...item,
            revealedIndices: nextRevealedIndices,
            isDiscovered: completedWordByHint,
            wasAlreadyRewarded,
            earnedPoints: 0,
          }
        : item
    );

    const completedAll = completedWordByHint && updatedWords.every((item) => item.isDiscovered);
    const canAwardSpeedBonus =
      completedAll && timeTrialEnabled && !rewardedTimeTrialLevels.includes(levelId);
    const speedBonus = canAwardSpeedBonus
      ? calculateSpeedBonus(
          timeRemainingSeconds,
          levelTimeLimitSeconds,
          difficultyConfig.multiplier
        )
      : 0;

    if (canAwardSpeedBonus && speedBonus > 0) {
      setRewardedTimeTrialLevels((prev) =>
        prev.includes(levelId) ? prev : [...prev, levelId]
      );
    }

    setScore((prev) => prev - 50 + speedBonus);
    setCurrentExtractedWords(updatedWords);

    if (completedWordByHint) {
      setSelectedLetterIndices([]);
      speakWord(target.word);
      if (completedAll) {
        setLastSpeedBonus(speedBonus);
        setIsLevelComplete(true);
        setDailyMissions((prev) => {
          const normalized = normalizeDailyMissionsState(prev);
          return {
            ...normalized,
            levelsPassed: normalized.levelsPassed + 1,
          };
        });
        if (currentLevelIndex + 1 >= unlockedLevel && unlockedLevel < levels.length) {
          setUnlockedLevel(currentLevelIndex + 2);
        }
      }
    }
  }, [
    currentExtractedWords,
    currentLevel.id,
    currentLevelIndex,
    difficultyConfig.multiplier,
    isLevelComplete,
    isTimeUp,
    levelTimeLimitSeconds,
    levels.length,
    rewardedTargetsByLevel,
    rewardedTimeTrialLevels,
    score,
    speakWord,
    timeRemainingSeconds,
    timeTrialEnabled,
    unlockedLevel,
  ]);

  const startExam = useCallback((examNumber = 1, fromMap = false) => {
    const safeExamNumber = Math.min(Math.max(examNumber, 1), 10);
    setActiveExamNumber(safeExamNumber);
    setCurrentExamIndex(0);
    setExamCorrectCount(0);
    setExamScore(0);
    setExamLastEarnedDelta(0);
    setExamCompleted(false);
    setExamEnteredFromMap(fromMap);
    setCurrentScreen(AppScreen.EXAM);
  }, []);

  const retakeExam = useCallback(() => {
    setCurrentExamIndex(0);
    setExamCorrectCount(0);
    setExamScore(0);
    setExamLastEarnedDelta(0);
    setExamCompleted(false);
  }, []);

  const nextLevel = useCallback(() => {
    if (levels.length === 0) return;
    const completedLevelNumber = currentLevelIndex + 1;
    if (completedLevelNumber % 5 === 0) {
      startExam(completedLevelNumber / 5, false);
    } else if (currentLevelIndex + 1 < levels.length) {
      const nextIdx = currentLevelIndex + 1;
      setCurrentLevelIndex(nextIdx);
      loadLevel(nextIdx);
      setCurrentScreen(AppScreen.GAMEPLAY);
    } else {
      setCurrentScreen(AppScreen.LEVEL_MAP);
    }
  }, [currentLevelIndex, levels.length, loadLevel, startExam]);

  const answerExam = useCallback(
    (selectedOptionIndex: number) => {
      const questions = getQuestionsForExam(activeExamNumber);
      if (currentExamIndex >= questions.length) return;

      const isCorrect = questions[currentExamIndex].correctIndex === selectedOptionIndex;
      const nextCorrectCount = examCorrectCount + (isCorrect ? 1 : 0);
      const nextExamScore = Math.round((nextCorrectCount * 10) / questions.length);

      setExamCorrectCount(nextCorrectCount);
      setExamScore(nextExamScore);

      if (currentExamIndex + 1 < questions.length) {
        setCurrentExamIndex((prev) => prev + 1);
      } else {
        const previousBest = examBestScores[activeExamNumber] ?? 0;
        const newBest = Math.max(previousBest, nextExamScore);
        const delta = newBest - previousBest;

        if (delta > 0) {
          setScore((prev) => prev + delta);
        }
        setExamLastEarnedDelta(delta);
        setExamBestScores((prev) => ({
          ...prev,
          [activeExamNumber]: Math.max(prev[activeExamNumber] ?? 0, newBest),
        }));
        setDailyMissions((prev) => {
          const normalized = normalizeDailyMissionsState(prev);
          return {
            ...normalized,
            examsCompleted: normalized.examsCompleted + 1,
          };
        });
        setExamCompleted(true);
      }
    },
    [activeExamNumber, currentExamIndex, examBestScores, examCorrectCount]
  );

  const finishExam = useCallback(() => {
    if (examEnteredFromMap) {
      setCurrentScreen(AppScreen.LEVEL_MAP);
      return;
    }

    const nextIdx = activeExamNumber * 5;
    if (nextIdx < levels.length) {
      setCurrentLevelIndex(nextIdx);
      loadLevel(nextIdx);
      setCurrentScreen(AppScreen.GAMEPLAY);
    } else {
      setCurrentScreen(AppScreen.LEVEL_MAP);
    }
  }, [activeExamNumber, examEnteredFromMap, levels.length, loadLevel]);

  const activeExamQuestions = getQuestionsForExam(activeExamNumber);
  const activeExamBestScore = examBestScores[activeExamNumber] ?? 0;
  const claimableDailyMissionsCount = useMemo(
    () => getClaimableDailyMissionsCount(dailyMissions),
    [dailyMissions]
  );

  return {
    currentScreen,
    difficulty,
    difficultyConfig,
    timeTrialEnabled,
    levelTimeLimitSeconds,
    timeRemainingSeconds,
    isTimeUp,
    lastSpeedBonus,
    extraWordPoints,
    score,
    unlockedLevel,
    currentLevelIndex,
    levels,
    currentLevel,
    selectedLetterIndices,
    currentExtractedWords,
    discoveredExtraWords,
    extraWordFeedback,
    canClaimFormedExtraWord,
    isFormedExtraAlreadyClaimed,
    isLevelComplete,
    soundEnabled,
    dailyMissions,
    claimableDailyMissionsCount,
    isDailyMissionsOpen,
    activeExamNumber,
    activeExamQuestions,
    activeExamBestScore,
    examBestScores,
    currentExamIndex,
    examScore,
    examLastEarnedDelta,
    examCompleted,
    examEnteredFromMap,
    navigateTo,
    openDailyMissions,
    closeDailyMissions,
    claimDailyMission,
    setDifficulty,
    setTimeTrialEnabled,
    toggleSound,
    speakWord,
    selectLevel,
    retryCurrentLevel,
    onLetterClicked,
    claimFormedExtraWord,
    clearSelection,
    useHint,
    nextLevel,
    startExam,
    retakeExam,
    answerExam,
    finishExam,
  };
}

export type WordGameController = ReturnType<typeof useWordGame>;
