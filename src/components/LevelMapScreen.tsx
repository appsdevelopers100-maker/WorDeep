import { ArrowLeft, Gift, GraduationCap, Lock, Sparkles, Star } from 'lucide-react';
import { AppScreen, Level } from '../model/wordGameModels';

interface LevelMapScreenProps {
  levels: Level[];
  unlockedLevel: number;
  score: number;
  examBestScores: Record<number, number>;
  claimableMissionsCount: number;
  onBack: () => void;
  onSelectLevel: (index: number) => void;
  onStartExam: (examNumber: number) => void;
  onOpenDailyMissions: () => void;
}

type MapItem =
  | { type: 'level'; level: Level; levelNumber: number }
  | { type: 'exam'; examNumber: number };

export function LevelMapScreen({
  levels,
  unlockedLevel,
  score,
  examBestScores,
  claimableMissionsCount,
  onBack,
  onSelectLevel,
  onStartExam,
  onOpenDailyMissions,
}: LevelMapScreenProps) {
  const mapItems: MapItem[] = [];
  levels.forEach((level, index) => {
    const levelNumber = index + 1;
    mapItems.push({ type: 'level', level, levelNumber });
    if (levelNumber % 5 === 0) {
      mapItems.push({ type: 'exam', examNumber: levelNumber / 5 });
    }
  });

  return (
    <div className="flex min-h-[100dvh] w-full flex-col bg-gradient-to-b from-[#E0F2FE] via-[#FEF9C3] to-[#DCFCE7]">
      {/* TopAppBar */}
      <header className="sticky top-0 z-10 flex h-16 shrink-0 items-center justify-between gap-2 bg-[#38BDF8] px-3 shadow-sm">
        <div className="flex min-w-0 items-center gap-2">
          <button
            type="button"
            onClick={onBack}
            aria-label="Back"
            className="flex h-10 w-10 shrink-0 cursor-pointer items-center justify-center rounded-full text-slate-900 transition-colors hover:bg-black/10 focus-visible:outline-2 focus-visible:outline-slate-900"
          >
            <ArrowLeft className="h-6 w-6" />
          </button>
          <h1 className="truncate text-base font-bold text-slate-900 whitespace-nowrap">
            World Map (خريطة المستويات)
          </h1>
        </div>

        <div className="flex shrink-0 items-center gap-1.5">
          <button
            type="button"
            onClick={onOpenDailyMissions}
            aria-label="Daily Missions"
            title="Daily Missions"
            className="relative flex h-9 w-9 cursor-pointer items-center justify-center rounded-full bg-white/80 text-[#0369A1] shadow-xs transition-colors hover:bg-white active:scale-95"
          >
            <Gift className="h-4.5 w-4.5" />
            {claimableMissionsCount > 0 && (
              <span className="absolute -top-0.5 -right-0.5 flex h-4 min-w-4 items-center justify-center rounded-full bg-[#F59E0B] px-1 text-[9px] font-extrabold text-white ring-2 ring-[#38BDF8]">
                {claimableMissionsCount}
              </span>
            )}
          </button>

          <div className="shrink-0 rounded-2xl bg-[#FEF08A] px-3 py-1.5 text-xs font-bold text-[#854D0E] tabular-nums shadow-xs">
            ⭐ Score: {score}
          </div>
        </div>
      </header>

      {/* Scrollable Level List */}
      <main className="flex-1 overflow-y-auto p-4">
        <div className="mx-auto flex max-w-md flex-col items-center gap-4 pb-6">
          <p className="text-center text-base font-bold text-[#0369A1]">
            Select Your Puzzle Level! (اختر مستواك)
          </p>

          {mapItems.map((item) => {
            if (item.type === 'level') {
              // Passed levels (< unlockedLevel) and the single next active level (=== unlockedLevel)
              const isUnlocked = item.levelNumber <= unlockedLevel;
              const isPassed = item.levelNumber < unlockedLevel;
              const subtitle = item.level.category
                ? `${item.level.arabic} • ${item.level.category}`
                : item.level.arabic;

              return (
                <button
                  key={`level-${item.level.id}`}
                  type="button"
                  disabled={!isUnlocked}
                  onClick={() => {
                    if (isUnlocked) {
                      onSelectLevel(item.levelNumber - 1);
                    }
                  }}
                  className={`flex h-[90px] w-full items-center justify-between rounded-3xl px-5 text-left transition-transform duration-150 ${
                    isUnlocked
                      ? 'cursor-pointer bg-white shadow-md hover:scale-[1.01] active:scale-[0.99]'
                      : 'cursor-not-allowed bg-[#E2E8F0] shadow-xs'
                  }`}
                >
                  <div className="flex min-w-0 items-center gap-4">
                    <div
                      className={`flex h-[50px] w-[50px] shrink-0 items-center justify-center rounded-full ${
                        isUnlocked ? 'bg-[#0284C7]' : 'bg-gray-500'
                      }`}
                    >
                      {isUnlocked ? (
                        <span className="text-lg font-bold text-white tabular-nums">
                          {item.levelNumber}
                        </span>
                      ) : (
                        <Lock className="h-5 w-5 text-white" />
                      )}
                    </div>

                    <div className="min-w-0">
                      {isUnlocked ? (
                        <>
                          <p className="truncate text-base font-bold text-[#0F172A]">
                            Level.{item.levelNumber}: {item.level.mainWord}
                          </p>
                          <p className="truncate text-[13px] text-[#475569]">{subtitle}</p>
                        </>
                      ) : (
                        <>
                          <p className="truncate text-base font-bold text-gray-500">
                            Level.{item.levelNumber}
                          </p>
                          <p className="truncate text-xs text-gray-500">Locked</p>
                        </>
                      )}
                    </div>
                  </div>

                  {isUnlocked && (
                    <div className="flex shrink-0 items-center gap-0.5">
                      {[0, 1, 2].map((starIdx) => (
                        <Star
                          key={starIdx}
                          className={`h-5 w-5 ${
                            isPassed
                              ? 'fill-[#EAB308] text-[#EAB308]'
                              : 'fill-[#EAB308]/35 text-[#EAB308]'
                          }`}
                        />
                      ))}
                    </div>
                  )}
                </button>
              );
            }

            const isExamUnlocked = item.examNumber * 5 <= unlockedLevel;
            const bestScore = examBestScores[item.examNumber];
            const hasTakenExam = typeof bestScore === 'number';

            return (
              <button
                key={`exam-${item.examNumber}`}
                type="button"
                disabled={!isExamUnlocked}
                onClick={() => {
                  if (isExamUnlocked) {
                    onStartExam(item.examNumber);
                  }
                }}
                className={`flex min-h-[84px] w-full items-center justify-between rounded-3xl px-5 py-3 text-left transition-transform duration-150 ${
                  isExamUnlocked
                    ? 'cursor-pointer bg-[#FDE047] shadow-md hover:scale-[1.01] active:scale-[0.99]'
                    : 'cursor-not-allowed bg-[#E2E8F0] shadow-xs'
                }`}
              >
                <div className="flex min-w-0 items-center gap-4">
                  <div
                    className={`flex h-[44px] w-[44px] shrink-0 items-center justify-center rounded-full ${
                      isExamUnlocked ? 'bg-[#D97706]' : 'bg-gray-500'
                    }`}
                  >
                    {isExamUnlocked ? (
                      <GraduationCap className="h-5 w-5 text-white" />
                    ) : (
                      <Lock className="h-5 w-5 text-white" />
                    )}
                  </div>

                  <div className="min-w-0">
                    <p
                      className={`truncate text-[15px] font-bold ${
                        isExamUnlocked ? 'text-[#78350F]' : 'text-gray-500'
                      }`}
                    >
                      ⭐ EXAM CHECKPOINT {item.examNumber} ⭐
                    </p>
                    <p
                      className={`truncate text-xs ${
                        isExamUnlocked ? 'text-[#92400E]' : 'text-gray-600'
                      }`}
                    >
                      {isExamUnlocked
                        ? hasTakenExam
                          ? `Best: ${bestScore}/10 · Tap to retake`
                          : 'Test your vocabulary (10 pts max)'
                        : 'Locked'}
                    </p>
                  </div>
                </div>

                {isExamUnlocked && hasTakenExam && (
                  <span className="ml-2 shrink-0 rounded-xl bg-white/80 px-2.5 py-1 text-xs font-bold text-[#78350F] tabular-nums">
                    {bestScore}/10
                  </span>
                )}
              </button>
            );
          })}

          {/* Coming Soon Banner After Level 50 & Exam Checkpoint 10 */}
          <div className="mt-2 flex w-full flex-col items-center rounded-3xl border border-[#38BDF8]/40 bg-white/95 p-5 text-center shadow-md">
            <div className="flex h-11 w-11 items-center justify-center rounded-full bg-[#E0F2FE] text-[#0284C7]">
              <Sparkles className="h-6 w-6" />
            </div>
            <p className="mt-3 text-sm font-bold leading-relaxed text-[#0F172A]">
              More challenging words and exams are coming soon. Keep up the good work
            </p>
          </div>
        </div>
      </main>
    </div>
  );
}

export type { AppScreen };
