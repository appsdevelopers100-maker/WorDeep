import { ArrowLeft, Lightbulb, RotateCcw, Volume2 } from 'lucide-react';
import { Level, WordItem } from '../model/wordGameModels';

interface GameplayScreenProps {
  level: Level;
  score: number;
  selectedIndices: number[];
  extractedWords: WordItem[];
  discoveredExtraWords: string[];
  isLevelComplete: boolean;
  onBack: () => void;
  onSpeakWord: (word: string) => void;
  onUseHint: () => void;
  onLetterClick: (index: number) => void;
  onClearSelection: () => void;
  onNextLevel: () => void;
}

export function GameplayScreen({
  level,
  score,
  selectedIndices,
  extractedWords,
  discoveredExtraWords,
  isLevelComplete,
  onBack,
  onSpeakWord,
  onUseHint,
  onLetterClick,
  onClearSelection,
  onNextLevel,
}: GameplayScreenProps) {
  const formedWord = selectedIndices.map((i) => level.mainWord[i] ?? '').join('');
  const discoveredCount = extractedWords.filter((w) => w.isDiscovered).length;
  const canUseHint = score >= 50 && discoveredCount < extractedWords.length;

  const wordLength = level.mainWord.length;
  const tileSizeClass =
    wordLength <= 6
      ? 'h-11 w-11 text-xl'
      : wordLength <= 8
        ? 'h-9 w-9 text-[17px]'
        : wordLength <= 10
          ? 'h-8 w-8 text-sm'
          : 'h-7 w-7 text-xs';

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
            L{level.id}: {level.mainWord}
          </h1>
        </div>

        <div className="flex shrink-0 items-center gap-1.5">
          <button
            type="button"
            onClick={() => onSpeakWord(level.mainWord)}
            aria-label="Pronounce main word"
            title="Pronounce word"
            className="flex h-9 w-9 shrink-0 cursor-pointer items-center justify-center rounded-full text-[#0369A1] transition-colors hover:bg-black/10 focus-visible:outline-2 focus-visible:outline-slate-900"
          >
            <Volume2 className="h-5 w-5" />
          </button>

          <button
            type="button"
            onClick={onUseHint}
            disabled={!canUseHint}
            title={score < 50 ? 'Need 50 points for a hint' : 'Reveal a word (-50 pts)'}
            className={`flex items-center gap-1 rounded-full px-2.5 py-1.5 text-[11px] font-bold text-white shadow-xs transition-all ${
              canUseHint
                ? 'cursor-pointer bg-[#F59E0B] hover:bg-amber-600 active:scale-95'
                : 'cursor-not-allowed bg-[#F59E0B]/50'
            }`}
          >
            <Lightbulb className="h-3.5 w-3.5" />
            <span>Hint</span>
          </button>

          <div className="rounded-xl bg-[#FEF08A] px-2.5 py-1 text-[11px] font-bold text-[#854D0E] tabular-nums shadow-xs">
            ⭐{score}
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="flex flex-1 flex-col items-center p-4">
        <div className="flex w-full max-w-md flex-1 flex-col items-center">
          {/* Category & Arabic Translation */}
          <p className="text-center text-lg font-bold text-[#0369A1]">
            {level.arabic} ({level.category})
          </p>

          {/* Main Word Interactive Letter Tiles Card */}
          <section className="mt-3 w-full rounded-[20px] bg-white p-4 shadow-md">
            <p className="text-center text-[13px] text-gray-500">
              Tap letters left-to-right (اختر الحروف من اليسار لليمين):
            </p>

            <div className="mt-3 flex w-full items-center justify-center gap-1 overflow-x-auto py-1">
              {level.mainWord.split('').map((char, index) => {
                const isSelected = selectedIndices.includes(index);
                return (
                  <button
                    key={`${level.id}-${index}-${char}`}
                    type="button"
                    onClick={() => onLetterClick(index)}
                    className={`flex shrink-0 cursor-pointer items-center justify-center rounded-[10px] font-bold transition-all duration-150 select-none active:scale-90 ${tileSizeClass} ${
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

            <div className="mt-3 flex items-center justify-between">
              <p className="text-base font-bold text-[#0284C7]">
                Formed: <span className="tracking-wider">{formedWord}</span>
              </p>
              <button
                type="button"
                onClick={onClearSelection}
                aria-label="Clear selection"
                title="Clear selection"
                className="flex h-9 w-9 cursor-pointer items-center justify-center rounded-full text-red-600 transition-colors hover:bg-red-50 active:scale-95"
              >
                <RotateCcw className="h-5 w-5" />
              </button>
            </div>
          </section>

          {/* Extra Discovered Words Section */}
          {discoveredExtraWords.length > 0 && (
            <section className="mt-3 w-full">
              <p className="text-[13px] font-bold text-[#0284C7]">
                Extra Words Discovered (+30 pts each):
              </p>
              <div className="mt-1 flex gap-2 overflow-x-auto pb-1">
                {discoveredExtraWords.map((extraWord) => (
                  <div
                    key={extraWord}
                    className="flex shrink-0 items-center gap-1 rounded-xl bg-[#E0F2FE] px-2 py-1 shadow-xs"
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
                  </div>
                ))}
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
            <p className="mt-2 text-sm font-bold text-[#16A34A]">Score bonus added!</p>

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
              const isFilled = isPrefixMatch && idx < formedWord.length;
              const charToDisplay = isFilled ? wordItem.word[idx] : '_';
              return (
                <div
                  key={idx}
                  className={`flex h-7 w-7 items-center justify-center rounded-md text-sm font-bold ${
                    isFilled ? 'bg-[#38BDF8] text-white' : 'bg-[#E2E8F0] text-gray-500'
                  }`}
                >
                  {charToDisplay}
                </div>
              );
            })}
          </div>
        )}
      </div>

      <span className="ml-2 shrink-0 text-[13px] text-gray-500">
        {wordItem.isDiscovered ? '✅' : `${wordItem.length} letters`}
      </span>
    </div>
  );
}
