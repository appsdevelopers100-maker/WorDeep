import { useEffect } from 'react';
import { AppScreen } from '../model/wordGameModels';

interface SplashScreenProps {
  onComplete: (screen: AppScreen) => void;
}

export function SplashScreen({ onComplete }: SplashScreenProps) {
  useEffect(() => {
    const timer = window.setTimeout(() => {
      onComplete(AppScreen.WELCOME);
    }, 3500);
    return () => window.clearTimeout(timer);
  }, [onComplete]);

  return (
    <div
      onClick={() => onComplete(AppScreen.WELCOME)}
      role="button"
      tabIndex={0}
      onKeyDown={(e) => {
        if (e.key === 'Enter' || e.key === ' ') {
          onComplete(AppScreen.WELCOME);
        }
      }}
      className="flex h-full min-h-[100dvh] w-full cursor-pointer flex-col items-center justify-center bg-[#0F172A] px-6 text-center select-none"
    >
      <div className="flex flex-col items-center justify-center gap-2">
        <h1 className="text-5xl font-bold tracking-tight text-white">WorDig</h1>
        <p className="text-lg font-medium text-[#38BDF8]">Learn &amp; Play</p>
      </div>
    </div>
  );
}
