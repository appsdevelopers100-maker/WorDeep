import { useEffect } from 'react';
import { Puzzle, Star } from 'lucide-react';
import { AppScreen } from '../model/wordGameModels';

interface SplashScreenProps {
  onComplete: (screen: AppScreen) => void;
}

export function SplashScreen({ onComplete }: SplashScreenProps) {
  useEffect(() => {
    const timer = window.setTimeout(() => {
      onComplete(AppScreen.WELCOME);
    }, 2400);
    return () => window.clearTimeout(timer);
  }, [onComplete]);

  return (
    <div
      onClick={() => onComplete(AppScreen.WELCOME)}
      role="button"
      tabIndex={0}
      aria-label="Enter WORDIG"
      onKeyDown={(e) => {
        if (e.key === 'Enter' || e.key === ' ') {
          onComplete(AppScreen.WELCOME);
        }
      }}
      className="relative flex h-full min-h-[100dvh] w-full cursor-pointer flex-col items-center justify-center overflow-hidden bg-gradient-to-b from-[#2FA8D8] via-[#48BFE3] to-[#68C75A] px-6 text-center select-none"
    >
      {/* Decorative floating letter tiles matching Welcome artwork */}
      <div
        aria-hidden="true"
        className="pointer-events-none absolute top-14 left-8 flex h-12 w-12 -rotate-12 items-center justify-center rounded-xl bg-[#F59E0B] text-xl font-extrabold text-white shadow-lg"
      >
        W
      </div>
      <div
        aria-hidden="true"
        className="pointer-events-none absolute top-24 right-8 flex h-11 w-11 rotate-12 items-center justify-center rounded-xl bg-[#22C55E] text-lg font-extrabold text-white shadow-lg"
      >
        a
      </div>
      <div
        aria-hidden="true"
        className="pointer-events-none absolute bottom-24 left-10 flex h-11 w-11 rotate-6 items-center justify-center rounded-xl bg-[#0284C7] text-lg font-extrabold text-white shadow-lg"
      >
        e
      </div>
      <div
        aria-hidden="true"
        className="pointer-events-none absolute right-10 bottom-20 flex h-12 w-12 -rotate-6 items-center justify-center rounded-xl bg-white text-xl font-extrabold text-[#0369A1] shadow-lg"
      >
        n
      </div>

      {/* Main WORDIG Puzzle Crest */}
      <div className="relative z-10 flex flex-col items-center">
        <div className="relative flex items-center justify-center rounded-3xl bg-white/95 p-4 shadow-2xl ring-4 ring-white/50">
          <div className="grid grid-cols-2 gap-1.5">
            <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-[#38BDF8] text-white">
              <Puzzle className="h-6 w-6" />
            </div>
            <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-[#22C55E] text-white">
              <Puzzle className="h-6 w-6 rotate-90" />
            </div>
            <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-[#F59E0B] text-white">
              <Puzzle className="h-6 w-6 -rotate-90" />
            </div>
            <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-[#0284C7] text-white">
              <Puzzle className="h-6 w-6 rotate-180" />
            </div>
          </div>

          {/* Center golden star badge */}
          <div className="absolute flex h-14 w-14 items-center justify-center rounded-full bg-gradient-to-b from-[#FDE047] to-[#F59E0B] shadow-lg ring-4 ring-white">
            <Star className="h-8 w-8 fill-white text-white" />
          </div>
        </div>

        {/* Brand Wordmark */}
        <h1 className="mt-5 rounded-2xl bg-white px-7 py-2 text-4xl font-extrabold tracking-wide text-[#0369A1] shadow-xl">
          WORDIG
        </h1>

        {/* Bilingual Arabic/English Banner */}
        <div className="mt-4 rounded-2xl bg-white/95 px-6 py-2.5 shadow-lg">
          <p className="text-xl font-extrabold text-[#0369A1]">كل كلمة تخفي المزيد</p>
          <p className="mt-0.5 text-sm font-bold text-[#475569]">Every Word Hides More</p>
        </div>

        <p className="mt-8 text-xs font-semibold text-white/90">Tap anywhere to start</p>
      </div>
    </div>
  );
}
