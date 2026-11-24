import { ArrowLeft, GraduationCap, RotateCcw } from 'lucide-react';
import { ExamQuestion } from '../model/wordGameModels';

interface ExamScreenProps {
  examNumber: number;
  questions: ExamQuestion[];
  currentExamIndex: number;
  examScore: number;
  bestScore: number;
  earnedDelta: number;
  examCompleted: boolean;
  enteredFromMap: boolean;
  onBackToMap: () => void;
  onAnswerExam: (optionIndex: number) => void;
  onRetakeExam: () => void;
  onFinishExam: () => void;
}

export function ExamScreen({
  examNumber,
  questions,
  currentExamIndex,
  examScore,
  bestScore,
  earnedDelta,
  examCompleted,
  enteredFromMap,
  onBackToMap,
  onAnswerExam,
  onRetakeExam,
  onFinishExam,
}: ExamScreenProps) {
  const question = questions[currentExamIndex] ?? questions[0];
  const pointsPerQuestion = questions.length > 0 ? 10 / questions.length : 2;
  const isFinalExam = examNumber >= 10;

  const colonIdx = question.question.lastIndexOf(':');
  const afterColon = colonIdx !== -1 ? question.question.slice(colonIdx + 1).trim() : '';
  const hasPattern = colonIdx !== -1 && afterColon.includes('_');
  const promptLabel = hasPattern ? question.question.slice(0, colonIdx + 1).trim() : question.question;
  const missingPattern = hasPattern ? afterColon : null;

  return (
    <div className="flex min-h-[100dvh] w-full flex-col bg-gradient-to-b from-[#FEF9C3] via-[#E0F2FE] to-[#DCFCE7]">
      {/* TopAppBar */}
      <header className="sticky top-0 z-10 flex h-16 shrink-0 items-center justify-between gap-2 bg-[#F59E0B] px-3 shadow-sm">
        <div className="flex min-w-0 items-center gap-1.5">
          <button
            type="button"
            onClick={onBackToMap}
            aria-label="Back to World Map"
            className="flex h-10 w-10 shrink-0 cursor-pointer items-center justify-center rounded-full text-slate-900 transition-colors hover:bg-black/10 focus-visible:outline-2 focus-visible:outline-slate-900"
          >
            <ArrowLeft className="h-6 w-6" />
          </button>
          <h1 className="truncate text-base font-bold text-slate-900">
            ⭐ Exam Checkpoint {examNumber} ⭐
          </h1>
        </div>

        <span className="shrink-0 rounded-xl bg-white/85 px-2.5 py-1 text-xs font-bold text-[#78350F] tabular-nums">
          Best: {bestScore}/10
        </span>
      </header>

      <main className="flex flex-1 items-center justify-center p-6">
        <div className="w-full max-w-md">
          {!examCompleted ? (
            <div className="flex flex-col items-center rounded-3xl bg-white p-6 text-center shadow-lg">
              <GraduationCap className="h-12 w-12 text-[#D97706]" />

              <p className="mt-3 text-sm font-bold text-gray-500 tabular-nums">
                Question {currentExamIndex + 1} of {questions.length} · {pointsPerQuestion} pts each
              </p>

              <h2 className="mt-2 text-lg font-bold text-[#0F172A]">{promptLabel}</h2>

              {missingPattern && (
                <div className="mt-2.5 w-full rounded-2xl border border-[#38BDF8]/40 bg-[#E0F2FE] px-3 py-2.5">
                  <p className="whitespace-nowrap font-mono text-[clamp(0.875rem,4.5vw,1.25rem)] font-extrabold tracking-wide text-[#0369A1]">
                    {missingPattern.replace(/ /g, '\u00A0')}
                  </p>
                </div>
              )}

              <p dir="rtl" className="mt-2.5 w-full text-center text-base text-[#475569]">
                {question.arabic}
              </p>

              <div className="mt-6 flex w-full flex-col gap-3">
                {question.options.map((option, optIndex) => (
                  <button
                    key={option}
                    type="button"
                    onClick={() => onAnswerExam(optIndex)}
                    className="h-[50px] w-full cursor-pointer rounded-2xl bg-[#38BDF8] px-4 text-base font-bold text-white shadow-xs transition-all hover:bg-sky-500 active:scale-[0.98]"
                  >
                    {option}
                  </button>
                ))}
              </div>
            </div>
          ) : (
            <div className="flex flex-col items-center rounded-3xl bg-white p-7 text-center shadow-lg">
              <h2 className="text-[22px] font-bold text-[#16A34A]">🎓 EXAM COMPLETED! 🎓</h2>
              <p className="mt-1 text-lg font-bold text-[#16A34A]">(أنهيت الاختبار!)</p>

              <p className="mt-4 text-xl font-extrabold text-slate-900 tabular-nums">
                Exam Score: {examScore} / 10 Points
              </p>
              <p className="mt-1 text-sm font-bold text-[#0369A1] tabular-nums">
                Best Score on Exam {examNumber}: {bestScore} / 10
              </p>

              <p
                className={`mt-2 text-xs font-bold ${
                  earnedDelta > 0 ? 'text-[#16A34A]' : 'text-slate-500'
                }`}
              >
                {earnedDelta > 0
                  ? `+${earnedDelta} new points added to your total score!`
                  : 'Retake anytime to beat your best score without losing progress.'}
              </p>

              {isFinalExam && (
                <div className="mt-4 w-full rounded-2xl border border-[#38BDF8]/40 bg-[#E0F2FE]/60 px-4 py-3 text-xs font-bold leading-relaxed text-[#0369A1]">
                  More challenging words and exams are coming soon. Keep up the good work
                </div>
              )}

              <div className="mt-6 flex w-full flex-col gap-3">
                <button
                  type="button"
                  onClick={onRetakeExam}
                  className="flex h-12 w-full cursor-pointer items-center justify-center gap-2 rounded-2xl bg-[#F59E0B] px-4 text-sm font-bold text-white shadow-sm transition-colors hover:bg-amber-600 active:scale-[0.98]"
                >
                  <RotateCcw className="h-4 w-4" />
                  <span>Retake Exam (إعادة الاختبار)</span>
                </button>

                <button
                  type="button"
                  onClick={onFinishExam}
                  className="flex h-16 w-full cursor-pointer flex-col items-center justify-center rounded-[20px] bg-[#22C55E] px-4 text-white shadow-sm transition-colors hover:bg-green-600 active:scale-[0.98]"
                >
                  <span className="text-[15px] font-bold">
                    {enteredFromMap || isFinalExam ? 'Back to World Map' : 'Continue Adventure'}
                  </span>
                  <span className="text-sm font-bold">
                    {enteredFromMap || isFinalExam ? '(العودة إلى الخريطة)' : '(تابع المغامرة)'}
                  </span>
                </button>
              </div>
            </div>
          )}
        </div>
      </main>
    </div>
  );
}
