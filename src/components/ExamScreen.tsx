import { GraduationCap } from 'lucide-react';
import { EXAM_QUESTIONS } from '../data/wordGameRepository';

interface ExamScreenProps {
  currentExamIndex: number;
  examScore: number;
  examCompleted: boolean;
  onAnswerExam: (optionIndex: number) => void;
  onFinishExam: () => void;
}

export function ExamScreen({
  currentExamIndex,
  examScore,
  examCompleted,
  onAnswerExam,
  onFinishExam,
}: ExamScreenProps) {
  const question = EXAM_QUESTIONS[currentExamIndex] ?? EXAM_QUESTIONS[0];

  return (
    <div className="flex min-h-[100dvh] w-full flex-col bg-gradient-to-b from-[#FEF9C3] via-[#E0F2FE] to-[#DCFCE7]">
      {/* TopAppBar */}
      <header className="sticky top-0 z-10 flex h-16 shrink-0 items-center bg-[#F59E0B] px-4 shadow-sm">
        <h1 className="truncate text-lg font-bold text-slate-900">
          ⭐ Educational Exam Checkpoint ⭐
        </h1>
      </header>

      <main className="flex flex-1 items-center justify-center p-6">
        <div className="w-full max-w-md">
          {!examCompleted ? (
            <div className="flex flex-col items-center rounded-3xl bg-white p-6 text-center shadow-lg">
              <GraduationCap className="h-12 w-12 text-[#D97706]" />

              <p className="mt-3 text-sm font-bold text-gray-500 tabular-nums">
                Question {currentExamIndex + 1} of {EXAM_QUESTIONS.length}
              </p>

              <h2 className="mt-2 text-lg font-bold text-[#0F172A]">{question.question}</h2>
              <p className="mt-1.5 text-base text-[#475569]">{question.arabic}</p>

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
              <h2 className="text-[22px] font-bold text-[#16A34A]">🎓 EXAM PASSED! 🎓</h2>
              <p className="mt-1 text-lg font-bold text-[#16A34A]">(اجتاز الاختبار!)</p>

              <p className="mt-3 text-lg font-bold text-slate-900 tabular-nums">
                Exam Score: {examScore} Points
              </p>

              <button
                type="button"
                onClick={onFinishExam}
                className="mt-6 flex h-16 w-full cursor-pointer flex-col items-center justify-center rounded-[20px] bg-[#22C55E] px-4 text-white shadow-sm transition-colors hover:bg-green-600 active:scale-[0.98]"
              >
                <span className="text-[15px] font-bold">Continue Adventure</span>
                <span className="text-sm font-bold">(تابع المغامرة)</span>
              </button>
            </div>
          )}
        </div>
      </main>
    </div>
  );
}
