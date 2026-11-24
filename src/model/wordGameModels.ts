export interface WordItem {
  word: string;
  arabic: string;
  length: number;
  isDiscovered: boolean;
}

export interface Level {
  id: number;
  mainWord: string;
  arabic: string;
  category: string;
  extractedWords: WordItem[];
}

export interface ExamQuestion {
  id: number;
  question: string;
  arabic: string;
  options: string[];
  correctIndex: number;
}

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
