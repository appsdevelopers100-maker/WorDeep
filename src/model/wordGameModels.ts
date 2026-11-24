export interface WordItem {
  word: string;
  arabic: string;
  length: number;
  isDiscovered: boolean;
  revealedIndices: number[];
  wasAlreadyRewarded?: boolean;
  earnedPoints?: number;
}

export interface Level {
  id: number;
  mainWord: string;
  arabic: string;
  category?: string;
  extractedWords: WordItem[];
}

export interface ExamQuestion {
  id: number;
  question: string;
  arabic: string;
  options: string[];
  correctIndex: number;
}

export enum GameDifficulty {
  EASY = 'EASY',
  MEDIUM = 'MEDIUM',
  HARD = 'HARD',
}

export interface DifficultyConfig {
  id: GameDifficulty;
  label: string;
  arabic: string;
  multiplier: number;
  multiplierLabel: string;
  secondsPerWord: number;
  description: string;
}

export const DIFFICULTY_CONFIG: Record<GameDifficulty, DifficultyConfig> = {
  [GameDifficulty.EASY]: {
    id: GameDifficulty.EASY,
    label: 'Easy',
    arabic: 'سهل',
    multiplier: 0.75,
    multiplierLabel: '0.75x',
    secondsPerWord: 12,
    description: 'Simpler words · 0.75x pts · 12s/word',
  },
  [GameDifficulty.MEDIUM]: {
    id: GameDifficulty.MEDIUM,
    label: 'Medium',
    arabic: 'متوسط',
    multiplier: 1.0,
    multiplierLabel: '1.0x',
    secondsPerWord: 10,
    description: 'Balanced mix · 1.0x pts · 10s/word',
  },
  [GameDifficulty.HARD]: {
    id: GameDifficulty.HARD,
    label: 'Hard',
    arabic: 'صعب',
    multiplier: 1.25,
    multiplierLabel: '1.25x',
    secondsPerWord: 8,
    description: 'Complex words · 1.25x pts · 8s/word',
  },
};

export enum AppScreen {
  SPLASH = 'SPLASH',
  WELCOME = 'WELCOME',
  LEVEL_MAP = 'LEVEL_MAP',
  GAMEPLAY = 'GAMEPLAY',
  EXAM = 'EXAM',
  FAQ = 'FAQ',
  PRIVACY = 'PRIVACY',
  TERMS = 'TERMS',
  HOW_TO_PLAY = 'HOW_TO_PLAY',
}

export function getSecondsPerWord(difficulty: GameDifficulty): number {
  return DIFFICULTY_CONFIG[difficulty].secondsPerWord;
}

export function getLevelTimeLimitSeconds(
  wordCount: number,
  difficulty: GameDifficulty
): number {
  return Math.max(1, wordCount) * getSecondsPerWord(difficulty);
}

export function calculateSpeedBonus(
  remainingSeconds: number,
  totalSeconds: number,
  multiplier: number
): number {
  if (remainingSeconds <= 0 || totalSeconds <= 0) return 0;
  const ratio = Math.min(1, remainingSeconds / totalSeconds);
  return Math.max(5, Math.round(30 * ratio * multiplier));
}

export type DailyMissionId = 'PASS_LEVEL' | 'EXTRA_WORDS' | 'COMPLETE_EXAM';

export interface DailyMissionDefinition {
  id: DailyMissionId;
  title: string;
  arabic: string;
  target: number;
  rewardPoints: number;
}

export interface DailyMissionsState {
  date: string;
  levelsPassed: number;
  extraWordsFound: number;
  examsCompleted: number;
  claimedIds: DailyMissionId[];
}

export const DAILY_MISSION_REWARD_POINTS = 15;

export const DAILY_MISSIONS: DailyMissionDefinition[] = [
  {
    id: 'PASS_LEVEL',
    title: 'Pass 1 level',
    arabic: 'أكمل مستوى واحداً',
    target: 1,
    rewardPoints: DAILY_MISSION_REWARD_POINTS,
  },
  {
    id: 'EXTRA_WORDS',
    title: 'Find 2 extra words (3+ letters)',
    arabic: 'اعثر على كلمتين إضافيتين (٣+ أحرف)',
    target: 2,
    rewardPoints: DAILY_MISSION_REWARD_POINTS,
  },
  {
    id: 'COMPLETE_EXAM',
    title: 'Complete 1 exam',
    arabic: 'أنهِ اختباراً واحداً',
    target: 1,
    rewardPoints: DAILY_MISSION_REWARD_POINTS,
  },
];

export function getTodayDateKey(now: Date = new Date()): string {
  const year = now.getFullYear();
  const month = String(now.getMonth() + 1).padStart(2, '0');
  const day = String(now.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
}

export function createEmptyDailyMissionsState(today: string = getTodayDateKey()): DailyMissionsState {
  return {
    date: today,
    levelsPassed: 0,
    extraWordsFound: 0,
    examsCompleted: 0,
    claimedIds: [],
  };
}

export function normalizeDailyMissionsState(
  state: Partial<DailyMissionsState> | null | undefined,
  today: string = getTodayDateKey()
): DailyMissionsState {
  if (!state || state.date !== today) {
    return createEmptyDailyMissionsState(today);
  }
  const validIds: DailyMissionId[] = ['PASS_LEVEL', 'EXTRA_WORDS', 'COMPLETE_EXAM'];
  const claimedIds = Array.isArray(state.claimedIds)
    ? Array.from(
        new Set(
          state.claimedIds.filter((id): id is DailyMissionId =>
            validIds.includes(id as DailyMissionId)
          )
        )
      )
    : [];

  return {
    date: today,
    levelsPassed:
      typeof state.levelsPassed === 'number' && state.levelsPassed >= 0
        ? Math.floor(state.levelsPassed)
        : 0,
    extraWordsFound:
      typeof state.extraWordsFound === 'number' && state.extraWordsFound >= 0
        ? Math.floor(state.extraWordsFound)
        : 0,
    examsCompleted:
      typeof state.examsCompleted === 'number' && state.examsCompleted >= 0
        ? Math.floor(state.examsCompleted)
        : 0,
    claimedIds,
  };
}

export function getDailyMissionProgress(
  state: DailyMissionsState,
  missionId: DailyMissionId
): number {
  switch (missionId) {
    case 'PASS_LEVEL':
      return state.levelsPassed;
    case 'EXTRA_WORDS':
      return state.extraWordsFound;
    case 'COMPLETE_EXAM':
      return state.examsCompleted;
  }
}

export function getClaimableDailyMissionsCount(state: DailyMissionsState): number {
  const normalized = normalizeDailyMissionsState(state);
  return DAILY_MISSIONS.filter(
    (mission) =>
      getDailyMissionProgress(normalized, mission.id) >= mission.target &&
      !normalized.claimedIds.includes(mission.id)
  ).length;
}


