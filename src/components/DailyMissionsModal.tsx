import { Check, Gift, GraduationCap, Sparkles, Trophy, X } from 'lucide-react';
import {
  DAILY_MISSIONS,
  DailyMissionId,
  DailyMissionsState,
  getDailyMissionProgress,
} from '../model/wordGameModels';

interface DailyMissionsModalProps {
  isOpen: boolean;
  missionsState: DailyMissionsState;
  onClaimMission: (missionId: DailyMissionId) => void;
  onClose: () => void;
}

export function DailyMissionsModal({
  isOpen,
  missionsState,
  onClaimMission,
  onClose,
}: DailyMissionsModalProps) {
  if (!isOpen) return null;

  const claimedCount = missionsState.claimedIds.length;

  return (
    <div
      role="dialog"
      aria-modal="true"
      aria-labelledby="daily-missions-title"
      className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 p-4 backdrop-blur-[2px]"
      onClick={onClose}
    >
      <div
        className="w-full max-w-sm overflow-hidden rounded-3xl bg-white shadow-2xl"
        onClick={(e) => e.stopPropagation()}
      >
        {/* Header */}
        <div className="flex items-center justify-between bg-gradient-to-r from-[#38BDF8] to-[#0EA5E9] px-5 py-4 text-white">
          <div className="flex items-center gap-2.5">
            <div className="flex h-10 w-10 items-center justify-center rounded-2xl bg-white/20">
              <Gift className="h-5 w-5 text-white" />
            </div>
            <div>
              <h2 id="daily-missions-title" className="text-base font-extrabold leading-tight">
                Daily Missions
              </h2>
              <p className="text-[11px] font-semibold text-sky-50">
                {missionsState.date} · {claimedCount}/{DAILY_MISSIONS.length} claimed
              </p>
            </div>
          </div>
          <button
            type="button"
            onClick={onClose}
            aria-label="Close Daily Missions"
            className="flex h-8 w-8 cursor-pointer items-center justify-center rounded-full bg-white/20 text-white transition-colors hover:bg-white/30"
          >
            <X className="h-4 w-4" />
          </button>
        </div>

        {/* Mission List */}
        <div className="flex flex-col gap-3 p-4">
          {DAILY_MISSIONS.map((mission) => {
            const rawProgress = getDailyMissionProgress(missionsState, mission.id);
            const cappedProgress = Math.min(rawProgress, mission.target);
            const isCompleted = rawProgress >= mission.target;
            const isClaimed = missionsState.claimedIds.includes(mission.id);
            const percent = Math.round((cappedProgress / mission.target) * 100);

            return (
              <div
                key={mission.id}
                className={`rounded-2xl border p-3.5 transition-colors ${
                  isClaimed
                    ? 'border-emerald-200 bg-emerald-50/70'
                    : isCompleted
                      ? 'border-amber-300 bg-amber-50/80'
                      : 'border-slate-200 bg-slate-50/70'
                }`}
              >
                <div className="flex items-center justify-between gap-2.5">
                  <div className="flex min-w-0 items-center gap-2.5">
                    <div
                      className={`flex h-9 w-9 shrink-0 items-center justify-center rounded-xl ${
                        isClaimed
                          ? 'bg-[#22C55E] text-white'
                          : isCompleted
                            ? 'bg-[#F59E0B] text-white'
                            : 'bg-[#E0F2FE] text-[#0284C7]'
                      }`}
                    >
                      {mission.id === 'PASS_LEVEL' ? (
                        <Trophy className="h-4 w-4" />
                      ) : mission.id === 'EXTRA_WORDS' ? (
                        <Sparkles className="h-4 w-4" />
                      ) : (
                        <GraduationCap className="h-4 w-4" />
                      )}
                    </div>
                    <div className="min-w-0">
                      <p className="text-sm font-bold text-[#0F172A] leading-snug">
                        {mission.title}
                      </p>
                      <p dir="rtl" className="text-[11px] font-medium text-slate-500">
                        {mission.arabic}
                      </p>
                    </div>
                  </div>

                  {isClaimed ? (
                    <span className="flex shrink-0 items-center gap-1 rounded-xl bg-[#DCFCE7] px-2.5 py-1.5 text-xs font-extrabold text-[#15803D]">
                      <Check className="h-3.5 w-3.5" />
                      Claimed
                    </span>
                  ) : isCompleted ? (
                    <button
                      type="button"
                      onClick={() => onClaimMission(mission.id)}
                      className="shrink-0 cursor-pointer rounded-xl border border-b-[3px] border-[#FCD34D] border-b-[#B45309] bg-gradient-to-b from-[#F59E0B] to-[#D97706] px-3 py-1.5 text-xs font-extrabold text-white shadow-sm transition-transform active:translate-y-[1px] active:scale-95"
                    >
                      Claim +{mission.rewardPoints}
                    </button>
                  ) : (
                    <span className="shrink-0 rounded-xl bg-slate-200/80 px-2.5 py-1.5 text-xs font-bold text-slate-600 tabular-nums">
                      {cappedProgress}/{mission.target} · +{mission.rewardPoints}
                    </span>
                  )}
                </div>

                {/* Progress Bar */}
                <div className="mt-2.5 flex items-center gap-2">
                  <div className="h-1.5 flex-1 overflow-hidden rounded-full bg-slate-200">
                    <div
                      className={`h-full rounded-full transition-all duration-300 ${
                        isClaimed
                          ? 'bg-[#22C55E]'
                          : isCompleted
                            ? 'bg-[#F59E0B]'
                            : 'bg-[#0EA5E9]'
                      }`}
                      style={{ width: `${percent}%` }}
                    />
                  </div>
                  <span className="text-[10px] font-bold text-slate-500 tabular-nums">
                    {cappedProgress}/{mission.target}
                  </span>
                </div>
              </div>
            );
          })}

          <p className="text-center text-[11px] font-medium text-slate-500">
            Resets automatically each new day · +{DAILY_MISSIONS[0].rewardPoints} pts per mission
          </p>

          <button
            type="button"
            onClick={onClose}
            className="w-full cursor-pointer rounded-2xl bg-slate-100 py-2.5 text-sm font-bold text-slate-700 transition-colors hover:bg-slate-200"
          >
            Close
          </button>
        </div>
      </div>
    </div>
  );
}
