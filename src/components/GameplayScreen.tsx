import { ArrowLeft, Lightbulb, RotateCcw, Sparkles, Timer, Volume2, VolumeX, Zap } from 'lucide-react';
import { getWordArabic } from '../data/englishWords';
import { ExtraWordFeedback } from '../hooks/useWordGame';
import { calculateSpeedBonus, DifficultyConfig, GameDifficulty, Level, WordItem } from '../model/wordGameModels';

interface GameplayScreenProps {
  level: Level;
  difficultyConfig: DifficultyConfig;
  timeTrialEnabled: boolean;
  levelTimeLimitSeconds: number;
  timeRemainingSeconds: number;
  isTimeUp: boolean;
  lastSpeedBonus: number;
  extraWordPoints: number;
  score: number;
  selectedIndices: number[];
  extractedWords: WordItem[];
  discoveredExtraWords: string[];
  extraWordFeedback: ExtraWordFeedback | null;
  canClaimFormedExtraWord: boolean;
  isFormedExtraAlreadyClaimed: boolean;
  isLevelComplete: boolean;
  soundEnabled: boolean;
  onBack: () => void;
  onToggleSound: () => void;
  onSpeakWord: (word: string) => void;
  onUseHint: () => void;
  onLetterClick: (index: number) => void;
  onClaimExtraWord: () => void;
  onClearSelection: () => void;
  onRetryLevel: () => void;
  onNextLevel: () => void;
}

function formatTime(seconds: number): string {
  const safe = Math.max(0, seconds);
  const mins = Math.floor(safe / 60);
  const secs = safe % 60;
  return `${String(mins).padStart(2, '0')}:${String(secs).padStart(2, '0')}`;
}

export function GameplayScreen({
  level,
  difficultyConfig,
  timeTrialEnabled,
  levelTimeLimitSeconds,
  timeRemainingSeconds,
  isTimeUp,
  lastSpeedBonus,
  extraWordPoints,
  score,
  selectedIndices,
  extractedWords,
  discoveredExtraWords,
  extraWordFeedback,
  canClaimFormedExtraWord,
  isFormedExtraAlreadyClaimed,
  isLevelComplete,
  soundEnabled,
  onBack,
  onToggleSound,
  onSpeakWord,
  onUseHint,
  onLetterClick,
  onClaimExtraWord,
  onClearSelection,
  onRetryLevel,
  onNextLevel,
}: GameplayScreenProps) {
  const formedWord = selectedIndices.map((i) => level.mainWord[i] ?? '').join('');
  const discoveredCount = extractedWords.filter((w) => w.isDiscovered).length;
  const hasUnrevealedHintLetter = extractedWords.some(
    (w) => !w.isDiscovered && w.revealedIndices.length < w.length
  );
  const canUseHint = score >= 50 && !isLevelComplete && !isTimeUp && hasUnrevealedHintLetter;
  const timeProgressPercent =
    levelTimeLimitSeconds > 0
      ? Math.max(0, Math.min(100, (timeRemainingSeconds / levelTimeLimitSeconds) * 100))
      : 0;
  const projectedSpeedBonus = timeTrialEnabled
    ? calculateSpeedBonus(timeRemainingSeconds, levelTimeLimitSeconds, difficultyConfig.multiplier)
    : 0;
  const isUrgent = timeTrialEnabled && timeRemainingSeconds <= 10;
  const isWarning = timeTrialEnabled && timeRemainingSeconds > 10 && timeRemainingSeconds <= 20;

  const wordLength = level.mainWord.length;
  const tileTextSizeClass =
    wordLength <= 5
      ? 'text-xl'
      : wordLength <= 7
        ? 'text-lg'
        : wordLength <= 9
          ? 'text-base'
          : 'text-sm';

  return (
    <div className="relative flex min-h-[100dvh] w-full flex-col bg-gradient-to-b from-[#E0F2FE] via-[#FEF9C3] to-[#DCFCE7]">
      {/* TopAppBar */}
      <header className="sticky top-0 z-10 flex h-16 shrink-0 items-center justify-between gap-2 bg-[#38BDF8] px-2.5 shadow-sm">
        <div className="flex min-w-0 items-center gap-1.5">
          <button
            type="button"
            onClick={onBack}
            aria-label="Back to Level Map"
            className="flex h-10 w-10 shrink-0 cursor-pointer items-center justify-center rounded-full text-slate-900 transition-colors hover:bg-black/10 focus-visible:outline-2 focus-visible:outline-slate-900"
          >
            <ArrowLeft className="h-6 w-6" />
          </button>
          <h1 className="truncate text-base font-bold text-slate-900 whitespace-nowrap">
            Level {level.id}
          </h1>
          <span
            className={`shrink-0 rounded-lg px-1.5 py-0.5 text-[10px] font-extrabold text-white ${
              difficultyConfig.id === GameDifficulty.EASY
                ? 'bg-[#16A34A]'
                : difficultyConfig.id === GameDifficulty.MEDIUM
                  ? 'bg-[#0284C7]'
                  : 'bg-[#D97706]'
            }`}
          >
            {difficultyConfig.label} {difficultyConfig.multiplierLabel}
          </span>
        </div>

        <div className="flex shrink-0 items-center gap-1.5">
          <button
            type="button"
            onClick={onToggleSound}
            aria-label={soundEnabled ? 'Mute sound effects' : 'Enable sound effects'}
            title={soundEnabled ? 'Sound On' : 'Sound Off'}
            className="flex h-9 w-9 shrink-0 cursor-pointer items-center justify-center rounded-full text-slate-900 transition-colors hover:bg-black/10 focus-visible:outline-2 focus-visible:outline-slate-900"
          >
            {soundEnabled ? <Volume2 className="h-5 w-5" /> : <VolumeX className="h-5 w-5" />}
          </button>

          <button
            type="button"
            onClick={onUseHint}
            disabled={!canUseHint}
            title={
              score < 50
                ? 'Need 50 points for a hint'
                : 'Reveal 1 letter of a hidden word (-50 pts)'
            }
            className={`flex items-center gap-1 rounded-full px-2.5 py-1.5 text-[11px] font-bold text-white shadow-xs transition-all ${
              canUseHint
                ? 'cursor-pointer bg-[#F59E0B] hover:bg-amber-600 active:scale-95'
                : 'cursor-not-allowed bg-[#F59E0B]/50'
            }`}
          >
            <Lightbulb className="h-3.5 w-3.5" />
            <span>Hint · 50</span>
          </button>

          <div className="rounded-xl bg-[#FEF08A] px-2.5 py-1 text-[11px] font-bold text-[#854D0E] tabular-nums shadow-xs">
            ⭐{score}
          </div>
        </div>
      </header>

      {/* Non-intrusive Extra Word Celebration Toast */}
      {extraWordFeedback && (
        <div
          key={extraWordFeedback.id}
          role="status"
          aria-live="polite"
          className="pointer-events-none fixed top-20 left-1/2 z-30 -translate-x-1/2 animate-bounce"
        >
          <div
            className={`flex items-center gap-2 rounded-full px-4 py-2 text-xs font-bold shadow-lg ring-2 ${
              extraWordFeedback.isDuplicate
                ? 'bg-slate-800 text-white ring-slate-600'
                : 'bg-[#22C55E] text-white ring-white'
            }`}
          >
            <Sparkles className="h-4 w-4 shrink-0" />
            <span className="whitespace-nowrap">
              {extraWordFeedback.isDuplicate
                ? `Already claimed: ${extraWordFeedback.word}${extraWordFeedback.arabic ? ` (${extraWordFeedback.arabic})` : ''} (+0 pts)`
                : `Extra Word: ${extraWordFeedback.word}${extraWordFeedback.arabic ? ` (${extraWordFeedback.arabic})` : ''} (+${extraWordFeedback.points} pts)`}
            </span>
          </div>
        </div>
      )}

      {/* Main Content */}
      <main className="flex flex-1 flex-col items-center p-4">
        <div className="flex w-full max-w-md flex-1 flex-col items-center">
          {/* Time-Trial Countdown Bar */}
          {timeTrialEnabled && (
            <div
              className={`mb-2.5 w-full rounded-2xl border px-3 py-2 shadow-xs transition-colors ${
                isUrgent
                  ? 'border-red-300 bg-red-50'
                  : isWarning
                    ? 'border-amber-300 bg-amber-50'
                    : 'border-sky-200 bg-white/90'
              }`}
            >
              <div className="flex items-center justify-between text-xs font-bold">
                <div className="flex items-center gap-1.5">
                  <Timer
                    className={`h-4 w-4 ${
                      isUrgent
                        ? 'animate-pulse text-red-600'
                        : isWarning
                          ? 'text-amber-600'
                          : 'text-[#0284C7]'
                    }`}
                  />
                  <span
                    className={`tabular-nums text-sm font-extrabold ${
                      isUrgent
                        ? 'text-red-700'
                        : isWarning
                          ? 'text-amber-700'
                          : 'text-[#0F172A]'
                    }`}
                  >
                    {formatTime(timeRemainingSeconds)}
                  </span>
                  <span className="text-[10.5px] font-semibold text-slate-500 tabular-nums">
                    ({extractedWords.length}×{difficultyConfig.secondsPerWord}s)
                  </span>
                </div>
                <div className="flex items-center gap-1.5">
                  <span className="rounded-md bg-[#22C55E]/15 px-1.5 py-0.5 text-[10px] font-extrabold text-[#15803D]">
                    Extra +{extraWordPoints} pts
                  </span>
                  <span className="flex items-center gap-1 text-[11px] font-bold text-[#15803D]">
                    <Zap className="h-3.5 w-3.5 text-[#F59E0B]" />
                    Speed: +{projectedSpeedBonus}
                  </span>
                </div>
              </div>
              <div className="mt-1.5 h-1.5 w-full overflow-hidden rounded-full bg-slate-200">
                <div
                  className={`h-full rounded-full transition-all duration-500 ${
                    isUrgent ? 'bg-red-500' : isWarning ? 'bg-amber-500' : 'bg-[#0284C7]'
                  }`}
                  style={{ width: `${timeProgressPercent}%` }}
                />
              </div>
            </div>
          )}

          {/* Main Word & Arabic Translation Side-by-Side (No Category Label) */}
          <div className="flex w-full flex-wrap items-center justify-center gap-x-2.5 gap-y-1 px-2 text-center">
            <span className="text-xl font-extrabold tracking-wide text-[#0F172A]">
              {level.mainWord}
            </span>
            <span aria-hidden="true" className="text-base font-bold text-slate-400">
              ·
            </span>
            <span dir="rtl" className="text-lg font-bold text-[#0369A1]">
              {level.arabic}
            </span>
            <button
              type="button"
              onClick={() => onSpeakWord(level.mainWord)}
              aria-label="Pronounce main word"
              title="Pronounce word"
              className="flex h-8 w-8 shrink-0 cursor-pointer items-center justify-center rounded-full text-[#0369A1] transition-colors hover:bg-sky-200/60"
            >
              <Volume2 className="h-4 w-4" />
            </button>
          </div>

          {/* Main Word Interactive Letter Tiles Card */}
          <section className="mt-3 w-full rounded-[20px] bg-white p-4 shadow-md">
            <p className="text-center text-[13px] text-gray-500">
              Tap letters left-to-right (اختر الحروف من اليسار لليمين):
            </p>

            {/* Responsive Dynamic Row — Never Overflows or Clips Long Words */}
            <div className="mt-3 flex w-full items-center justify-center gap-1 sm:gap-1.5 py-1">
              {level.mainWord.split('').map((char, index) => {
                const isSelected = selectedIndices.includes(index);
                return (
                  <button
                    key={`${level.id}-${index}-${char}`}
                    type="button"
                    onClick={() => onLetterClick(index)}
                    className={`flex aspect-square min-w-0 max-w-11 flex-1 cursor-pointer items-center justify-center rounded-[10px] font-bold transition-all duration-150 select-none active:scale-90 ${tileTextSizeClass} ${
                      isSelected
                        ? 'bg-[#38BDF8] text-white shadow-md'
                        : 'bg-[#F1F5F9] text-[#0F172A] shadow-xs hover:bg-slate-200'
                    }`}
                  >
                    {char}
                  </button>
                );
              })}
            </div>

            <div className="mt-3 flex items-center justify-between gap-2">
              <div className="flex min-w-0 items-center gap-2">
                <p className="truncate text-base font-bold text-[#0284C7]">
                  Formed: <span className="tracking-wider">{formedWord}</span>
                </p>
                {canClaimFormedExtraWord && (
                  <button
                    type="button"
                    onClick={onClaimExtraWord}
                    className={`flex shrink-0 cursor-pointer items-center gap-1 rounded-full px-2.5 py-1 text-xs font-bold shadow-xs transition-transform active:scale-95 ${
                      isFormedExtraAlreadyClaimed
                        ? 'bg-slate-200 text-slate-700 hover:bg-slate-300'
                        : 'bg-[#22C55E] text-white hover:bg-green-600'
                    }`}
                  >
                    <Sparkles className="h-3 w-3" />
                    <span>
                      {isFormedExtraAlreadyClaimed
                        ? 'Already claimed · +0'
                        : `Extra +${extraWordPoints}`}
                    </span>
                  </button>
                )}
              </div>

              <button
                type="button"
                onClick={onClearSelection}
                aria-label="Clear selection"
                title="Clear selection"
                className="flex h-9 w-9 shrink-0 cursor-pointer items-center justify-center rounded-full text-red-600 transition-colors hover:bg-red-50 active:scale-95"
              >
                <RotateCcw className="h-5 w-5" />
              </button>
            </div>
          </section>

          {/* Extra Discovered Words Section */}
          {discoveredExtraWords.length > 0 && (
            <section className="mt-3 w-full">
              <p className="text-[13px] font-bold text-[#0284C7]">
                Extra Words Discovered (+{extraWordPoints} pts each):
              </p>
              <div className="mt-1 flex gap-2 overflow-x-auto pb-1">
                {discoveredExtraWords.map((extraWord) => {
                  const arabic = getWordArabic(extraWord);
                  return (
                    <div
                      key={extraWord}
                      className="flex shrink-0 items-center gap-1.5 rounded-xl bg-[#E0F2FE] px-2.5 py-1 shadow-xs"
                    >
                      <button
                        type="button"
                        onClick={() => onSpeakWord(extraWord)}
                        aria-label={`Pronounce ${extraWord}`}
                        className="flex h-6 w-6 cursor-pointer items-center justify-center rounded-full text-[#0369A1] hover:bg-sky-200"
                      >
                        <Volume2 className="h-4 w-4" />
                      </button>
                      <span className="text-[13px] font-bold text-[#0369A1]">{extraWord}</span>
                      {arabic && (
                        <span dir="rtl" className="text-[13px] font-semibold text-[#0284C7]">
                          ({arabic})
                        </span>
                      )}
                    </div>
                  );
                })}
              </div>
            </section>
          )}

          {/* Extracted Words Target Slots */}
          <h2 className="mt-3 text-center text-[15px] font-bold text-[#166534]">
            Hidden Words to Discover ({discoveredCount}/{extractedWords.length}):
          </h2>

          <div className="mt-2 flex w-full flex-1 flex-col gap-2 pb-4">
            {extractedWords.map((wordItem) => (
              <ExtractedWordRow
                key={wordItem.word}
                wordItem={wordItem}
                formedWord={formedWord}
                onPronounce={() => onSpeakWord(wordItem.word)}
              />
            ))}
          </div>
        </div>
      </main>

      {/* Level Complete Modal Overlay */}
      {isLevelComplete && (
        <div
          role="dialog"
          aria-modal="true"
          aria-labelledby="level-complete-title"
          className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 p-4"
        >
          <div className="w-full max-w-sm rounded-3xl bg-white p-6 text-center shadow-2xl">
            <h2 id="level-complete-title" className="text-xl font-bold text-slate-900">
              🎉 LEVEL COMPLETED! (أحسنت!) 🎉
            </h2>
            <p className="mt-3 text-sm text-slate-600">
              You discovered all hidden words for {level.mainWord}!
            </p>
            {(() => {
              const levelWordsEarned = extractedWords.reduce(
                (sum, w) => sum + (w.earnedPoints ?? 0),
                0
              );
              const allReplay =
                extractedWords.length > 0 && extractedWords.every((w) => w.wasAlreadyRewarded);
              return (
                <>
                  {timeTrialEnabled && lastSpeedBonus > 0 && (
                    <div className="mt-3 inline-flex items-center gap-1.5 rounded-xl bg-amber-50 px-3.5 py-2 text-xs font-extrabold text-amber-800 ring-1 ring-amber-300">
                      <Zap className="h-4 w-4 text-amber-500" />
                      <span>
                        Speed Bonus: +{lastSpeedBonus} pts ({timeRemainingSeconds}s left!)
                      </span>
                    </div>
                  )}
                  {allReplay ? (
                    <p className="mt-2 text-xs font-semibold text-slate-500">
                      Replay complete · Previously claimed words (+0 pts)
                    </p>
                  ) : levelWordsEarned > 0 ? (
                    <p className="mt-2 text-sm font-bold text-[#16A34A]">
                      +{levelWordsEarned} pts earned ({difficultyConfig.label}{' '}
                      {difficultyConfig.multiplierLabel})
                    </p>
                  ) : null}
                </>
              );
            })()}

            <button
              type="button"
              onClick={onNextLevel}
              className="mt-5 w-full cursor-pointer rounded-2xl bg-[#22C55E] px-5 py-3 text-sm font-bold text-white shadow-sm transition-colors hover:bg-green-600 active:scale-[0.98]"
            >
              Next Level (المستوى التالي)
            </button>
          </div>
        </div>
      )}

      {/* Time's Up Modal Overlay */}
      {timeTrialEnabled && isTimeUp && !isLevelComplete && (
        <div
          role="dialog"
          aria-modal="true"
          aria-labelledby="time-up-title"
          className="fixed inset-0 z-50 flex items-center justify-center bg-black/55 p-4"
        >
          <div className="w-full max-w-sm rounded-3xl bg-white p-6 text-center shadow-2xl">
            <div className="mx-auto flex h-12 w-12 items-center justify-center rounded-full bg-red-100 text-red-600">
              <Timer className="h-6 w-6" />
            </div>
            <h2 id="time-up-title" className="mt-3 text-xl font-bold text-slate-900">
              Time&apos;s Up! (انتهى الوقت!)
            </h2>
            <p className="mt-2 text-sm text-slate-600">
              You found {discoveredCount} of {extractedWords.length} hidden words for{' '}
              <span className="font-bold text-slate-900">{level.mainWord}</span>.
            </p>
            <div className="mt-5 flex flex-col gap-2.5">
              <button
                type="button"
                onClick={onRetryLevel}
                className="w-full cursor-pointer rounded-2xl bg-[#0284C7] px-5 py-3 text-sm font-bold text-white shadow-sm transition-colors hover:bg-sky-700 active:scale-[0.98]"
              >
                Try Again (إعادة المحاولة)
              </button>
              <button
                type="button"
                onClick={onBack}
                className="w-full cursor-pointer rounded-2xl bg-slate-100 px-5 py-2.5 text-sm font-bold text-slate-700 transition-colors hover:bg-slate-200"
              >
                Back to World Map
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

interface ExtractedWordRowProps {
  wordItem: WordItem;
  formedWord: string;
  onPronounce: () => void;
}

function ExtractedWordRow({ wordItem, formedWord, onPronounce }: ExtractedWordRowProps) {
  const isPrefixMatch = formedWord.length > 0 && wordItem.word.startsWith(formedWord);

  return (
    <div
      className={`flex w-full items-center justify-between rounded-2xl p-3 shadow-xs ${
        wordItem.isDiscovered ? 'bg-[#DCFCE7]' : 'bg-white'
      }`}
    >
      <div className="flex min-w-0 items-center">
        {wordItem.isDiscovered ? (
          <>
            <button
              type="button"
              onClick={onPronounce}
              aria-label={`Pronounce ${wordItem.word}`}
              className="flex h-8 w-8 shrink-0 cursor-pointer items-center justify-center rounded-full text-[#166534] transition-colors hover:bg-green-200"
            >
              <Volume2 className="h-5 w-5" />
            </button>
            <span className="ml-2 text-lg font-bold text-[#166534]">{wordItem.word}</span>
            <span className="ml-3 truncate text-sm text-[#475569]">({wordItem.arabic})</span>
          </>
        ) : (
          <div className="flex flex-wrap items-center gap-1">
            {Array.from({ length: wordItem.length }).map((_, idx) => {
              const isMatchedByPrefix = isPrefixMatch && idx < formedWord.length;
              const isHintRevealed = wordItem.revealedIndices.includes(idx);

              let slotStyle = 'bg-[#E2E8F0] text-gray-500';
              let charToDisplay = '_';

              if (isMatchedByPrefix) {
                slotStyle = 'bg-[#38BDF8] text-white';
                charToDisplay = wordItem.word[idx];
              } else if (isHintRevealed) {
                slotStyle = 'bg-[#FEF3C7] text-[#B45309] border border-[#F59E0B]';
                charToDisplay = wordItem.word[idx];
              }

              return (
                <div
                  key={idx}
                  className={`flex h-7 w-7 items-center justify-center rounded-md text-sm font-bold ${slotStyle}`}
                >
                  {charToDisplay}
                </div>
              );
            })}
          </div>
        )}
      </div>

      <span className="ml-2 shrink-0 text-[12px] font-semibold text-gray-500">
        {wordItem.isDiscovered ? (
          wordItem.wasAlreadyRewarded ? (
            <span className="rounded-lg bg-slate-200/80 px-2 py-0.5 text-[11px] font-bold text-slate-600">
              Already claimed · +0 pts
            </span>
          ) : typeof wordItem.earnedPoints === 'number' && wordItem.earnedPoints > 0 ? (
            <span className="font-bold text-[#15803D]">+{wordItem.earnedPoints} pts ✅</span>
          ) : (
            '✅'
          )
        ) : (
          `${wordItem.length} letters`
        )}
      </span>
    </div>
  );
}
