import { Gift, Timer } from 'lucide-react';
import { AppScreen, DIFFICULTY_CONFIG, GameDifficulty } from '../model/wordGameModels';

interface WelcomeScreenProps {
  difficulty: GameDifficulty;
  timeTrialEnabled: boolean;
  claimableMissionsCount: number;
  onSelectDifficulty: (difficulty: GameDifficulty) => void;
  onToggleTimeTrial: (enabled: boolean) => void;
  onOpenDailyMissions: () => void;
  onNavigate: (screen: AppScreen) => void;
}

const DIFFICULTIES: GameDifficulty[] = [
  GameDifficulty.EASY,
  GameDifficulty.MEDIUM,
  GameDifficulty.HARD,
];

export function WelcomeScreen({
  difficulty,
  timeTrialEnabled,
  claimableMissionsCount,
  onSelectDifficulty,
  onToggleTimeTrial,
  onOpenDailyMissions,
  onNavigate,
}: WelcomeScreenProps) {
  return (
    <div className="relative flex min-h-[100dvh] w-full items-center justify-center bg-gradient-to-b from-[#2FA8D8] via-[#48BFE3] to-[#68C75A]">
      <div className="relative aspect-[1376/3064] h-[100dvh] max-h-[100dvh] w-auto max-w-full overflow-hidden shadow-2xl">
        <img
          src="/launchpage.png"
          alt="WORDIG Welcome Artwork"
          referrerPolicy="no-referrer"
          className="h-full w-full object-contain select-none"
        />

        {/* One Small Compact Daily Missions Button (Top-Right Sky Corner) */}
        <button
          type="button"
          onClick={onOpenDailyMissions}
          aria-label="Open Daily Missions"
          className="absolute top-2 right-2.5 z-10 flex cursor-pointer items-center gap-1 rounded-xl border border-[#D5E1EB] border-b-[3px] border-b-[#B4C5D5] bg-gradient-to-b from-white to-[#EEF4F8] px-2.5 py-1.5 text-[11px] leading-none font-bold text-[#1E3346] shadow-[0_2px_6px_rgba(15,23,42,0.16),inset_0_1px_0_rgba(255,255,255,0.95)] transition-all duration-150 select-none hover:to-[#F7FAFC] active:translate-y-[1px] active:border-b-[2px]"
        >
          <Gift className="h-3.5 w-3.5 shrink-0 text-[#0EA5E9]" />
          <span>Missions</span>
          {claimableMissionsCount > 0 && (
            <span className="flex h-4 min-w-4 items-center justify-center rounded-full bg-[#F59E0B] px-1 text-[9.5px] font-extrabold text-white shadow-xs">
              {claimableMissionsCount}
            </span>
          )}
        </button>

        {/* START PLAYING (center orange puzzle piece) */}
        <button
          type="button"
          aria-label="Start Playing"
          onClick={() => onNavigate(AppScreen.LEVEL_MAP)}
          className="absolute top-[44%] left-[22%] h-[16.5%] w-[56%] cursor-pointer rounded-2xl transition-all duration-150 hover:bg-white/15 focus-visible:outline-2 focus-visible:outline-white active:scale-95"
        />

        {/* HOW TO PLAY (middle-lower left orange puzzle piece) */}
        <button
          type="button"
          aria-label="How to Play"
          onClick={() => onNavigate(AppScreen.HOW_TO_PLAY)}
          className="absolute top-[62.5%] left-[13%] h-[9%] w-[37.5%] cursor-pointer rounded-2xl transition-all duration-150 hover:bg-white/15 focus-visible:outline-2 focus-visible:outline-white active:scale-95"
        />

        {/* FAQ (middle-lower right teal puzzle piece) */}
        <button
          type="button"
          aria-label="FAQ"
          onClick={() => onNavigate(AppScreen.FAQ)}
          className="absolute top-[61.5%] left-[55.5%] h-[10%] w-[32%] cursor-pointer rounded-2xl transition-all duration-150 hover:bg-white/15 focus-visible:outline-2 focus-visible:outline-white active:scale-95"
        />

        {/* Privacy Policy (bottom left white puzzle piece) */}
        <button
          type="button"
          aria-label="Privacy Policy"
          onClick={() => onNavigate(AppScreen.PRIVACY)}
          className="absolute top-[78.5%] left-[9.5%] h-[9.5%] w-[41%] cursor-pointer rounded-2xl transition-all duration-150 hover:bg-sky-500/15 focus-visible:outline-2 focus-visible:outline-sky-700 active:scale-95"
        />

        {/* Terms & Conditions (bottom right white puzzle piece) */}
        <button
          type="button"
          aria-label="Terms & Conditions"
          onClick={() => onNavigate(AppScreen.TERMS)}
          className="absolute top-[78.5%] left-[51%] h-[9.5%] w-[39.5%] cursor-pointer rounded-2xl transition-all duration-150 hover:bg-sky-500/15 focus-visible:outline-2 focus-visible:outline-sky-700 active:scale-95"
        />

        {/* 4 Matching Game-Tile Buttons in Two Clean Rows Below Privacy Policy & Terms & Conditions */}
        <div
          aria-label="Difficulty and Time Challenge Settings"
          className="absolute inset-x-3 bottom-[max(8px,env(safe-area-inset-bottom))] z-10 flex flex-col gap-1.5"
        >
          {/* Row 1: 3 Difficulty Buttons (Easy / Medium / Hard) */}
          <div role="group" aria-label="Difficulty" className="grid w-full grid-cols-3 gap-1.5">
            {DIFFICULTIES.map((item) => {
              const cfg = DIFFICULTY_CONFIG[item];
              const isSelected = difficulty === item;
              return (
                <button
                  key={item}
                  type="button"
                  onClick={() => onSelectDifficulty(item)}
                  aria-pressed={isSelected}
                  title={`${cfg.label} (${cfg.arabic}): ${cfg.description}`}
                  className={`flex min-w-0 cursor-pointer items-center justify-center gap-1 rounded-xl border border-b-[3px] px-1.5 py-1.5 text-[11px] leading-none font-bold tracking-tight transition-all duration-150 select-none active:translate-y-[1px] active:border-b-[2px] ${
                    isSelected
                      ? 'border-[#5AE0E8] border-b-[#0E6972] bg-gradient-to-b from-[#33C4CD] to-[#178E98] font-extrabold text-white shadow-[0_3px_8px_rgba(14,105,114,0.35),inset_0_1px_0_rgba(255,255,255,0.45)]'
                      : 'border-[#D5E1EB] border-b-[#B4C5D5] bg-gradient-to-b from-white to-[#EEF4F8] text-[#1E3346] shadow-[0_2px_6px_rgba(15,23,42,0.16),inset_0_1px_0_rgba(255,255,255,0.95)] hover:to-[#F7FAFC]'
                  }`}
                >
                  <span className="truncate">{cfg.label}</span>
                  <span
                    className={`shrink-0 rounded-md px-1 py-0.5 text-[9px] font-extrabold ${
                      isSelected
                        ? 'bg-white/25 text-white'
                        : 'bg-[#DCE8F2] text-[#2B4358]'
                    }`}
                  >
                    {cfg.multiplierLabel}
                  </span>
                </button>
              );
            })}
          </div>

          {/* Row 2: Time Toggle Button (Matches Terms White Tile when OFF / How-to-Play Gold Tile when ON) */}
          <button
            type="button"
            role="switch"
            aria-checked={timeTrialEnabled}
            onClick={() => onToggleTimeTrial(!timeTrialEnabled)}
            title={
              timeTrialEnabled
                ? `Time-Trial ON: ${DIFFICULTY_CONFIG[difficulty].secondsPerWord}s per listed word & +10 pts Extra Words`
                : 'Time-Trial OFF: No timer & +5 pts Extra Words'
            }
            className={`flex w-full cursor-pointer items-center justify-center gap-1.5 rounded-xl border border-b-[3px] px-3 py-1.5 text-[11px] leading-none font-bold tracking-tight transition-all duration-150 select-none active:translate-y-[1px] active:border-b-[2px] ${
              timeTrialEnabled
                ? 'border-[#FDD875] border-b-[#A85B06] bg-gradient-to-b from-[#F7B234] to-[#DE8112] font-extrabold text-white shadow-[0_3px_8px_rgba(168,91,6,0.35),inset_0_1px_0_rgba(255,255,255,0.45)]'
                : 'border-[#D5E1EB] border-b-[#B4C5D5] bg-gradient-to-b from-white to-[#EEF4F8] text-[#1E3346] shadow-[0_2px_6px_rgba(15,23,42,0.16),inset_0_1px_0_rgba(255,255,255,0.95)] hover:to-[#F7FAFC]'
            }`}
          >
            <Timer
              className={`h-3.5 w-3.5 shrink-0 ${
                timeTrialEnabled ? 'text-white' : 'text-[#178E98]'
              }`}
            />
            <span className="whitespace-nowrap">
              {timeTrialEnabled ? 'Time-Trial: ON' : 'Time-Trial: OFF'}
            </span>
            <span
              className={`rounded-md px-1.5 py-0.5 text-[9.5px] font-extrabold ${
                timeTrialEnabled
                  ? 'bg-white/25 text-white'
                  : 'bg-[#DCE8F2] text-[#2B4358]'
              }`}
            >
              {timeTrialEnabled
                ? `${DIFFICULTY_CONFIG[difficulty].secondsPerWord}s/word · +10`
                : 'Extra +5'}
            </span>
          </button>
        </div>
      </div>
    </div>
  );
}
