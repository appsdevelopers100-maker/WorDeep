// Lightweight Web Audio API wrapper for subtle game sound effects.
// Lazily initializes and resumes AudioContext on user gesture, with exponential gain envelopes to prevent clicks.

interface ToneStep {
  frequency: number;
  delaySeconds: number;
  durationSeconds: number;
  type: OscillatorType;
  peakGain: number;
}

class SoundEffectsManager {
  private ctx: AudioContext | null = null;
  private enabled = true;

  public setEnabled(enabled: boolean): void {
    this.enabled = enabled;
  }

  public isEnabled(): boolean {
    return this.enabled;
  }

  private getContext(): AudioContext | null {
    if (typeof window === 'undefined') return null;

    if (!this.ctx) {
      const AudioCtx =
        window.AudioContext ||
        (window as unknown as { webkitAudioContext?: typeof AudioContext }).webkitAudioContext;
      if (!AudioCtx) return null;
      try {
        this.ctx = new AudioCtx();
      } catch {
        return null;
      }
    }

    if (this.ctx.state === 'suspended') {
      void this.ctx.resume().catch(() => {
        // Ignore resume failure if not triggered inside user gesture
      });
    }

    return this.ctx;
  }

  private scheduleTone(ctx: AudioContext, step: ToneStep): void {
    try {
      const startTime = ctx.currentTime + step.delaySeconds;
      const osc = ctx.createOscillator();
      const gain = ctx.createGain();

      osc.type = step.type;
      osc.frequency.setValueAtTime(step.frequency, startTime);

      gain.gain.setValueAtTime(0.0001, startTime);
      gain.gain.exponentialRampToValueAtTime(step.peakGain, startTime + 0.008);
      gain.gain.exponentialRampToValueAtTime(0.0001, startTime + step.durationSeconds);

      osc.connect(gain);
      gain.connect(ctx.destination);

      osc.start(startTime);
      osc.stop(startTime + step.durationSeconds + 0.02);
    } catch {
      // Ignore Web Audio errors in restricted environments
    }
  }

  private playSequence(steps: ToneStep[]): void {
    if (!this.enabled) return;
    const ctx = this.getContext();
    if (!ctx) return;

    for (const step of steps) {
      this.scheduleTone(ctx, step);
    }
  }

  /** Subtle tactile tap when clicking/selecting a letter tile */
  public playTileClick(selectedCount = 1, isDeselect = false): void {
    if (isDeselect) {
      this.playSequence([
        {
          frequency: 310,
          delaySeconds: 0,
          durationSeconds: 0.055,
          type: 'sine',
          peakGain: 0.045,
        },
      ]);
      return;
    }

    // Gently rise in pitch along a pentatonic curve as letters are chained
    const scaleMultipliers = [1, 1.122, 1.26, 1.498, 1.682, 2, 2.245, 2.52];
    const mult = scaleMultipliers[Math.min(Math.max(selectedCount - 1, 0), scaleMultipliers.length - 1)];

    this.playSequence([
      {
        frequency: 440 * mult,
        delaySeconds: 0,
        durationSeconds: 0.06,
        type: 'triangle',
        peakGain: 0.05,
      },
    ]);
  }

  /** Warm ascending chime when discovering a target hidden word */
  public playWordMatch(): void {
    this.playSequence([
      {
        frequency: 523.25, // C5
        delaySeconds: 0,
        durationSeconds: 0.11,
        type: 'sine',
        peakGain: 0.065,
      },
      {
        frequency: 659.25, // E5
        delaySeconds: 0.065,
        durationSeconds: 0.13,
        type: 'sine',
        peakGain: 0.07,
      },
      {
        frequency: 783.99, // G5
        delaySeconds: 0.13,
        durationSeconds: 0.2,
        type: 'triangle',
        peakGain: 0.06,
      },
    ]);
  }

  /** Light sparkle sound when discovering a valid Extra Word (+5 pts) */
  public playExtraWord(isDuplicate = false): void {
    if (isDuplicate) {
      this.playSequence([
        {
          frequency: 360,
          delaySeconds: 0,
          durationSeconds: 0.06,
          type: 'sine',
          peakGain: 0.04,
        },
        {
          frequency: 330,
          delaySeconds: 0.07,
          durationSeconds: 0.07,
          type: 'sine',
          peakGain: 0.035,
        },
      ]);
      return;
    }

    this.playSequence([
      {
        frequency: 587.33, // D5
        delaySeconds: 0,
        durationSeconds: 0.09,
        type: 'sine',
        peakGain: 0.06,
      },
      {
        frequency: 739.99, // F#5
        delaySeconds: 0.055,
        durationSeconds: 0.1,
        type: 'sine',
        peakGain: 0.06,
      },
      {
        frequency: 880.0, // A5
        delaySeconds: 0.11,
        durationSeconds: 0.12,
        type: 'sine',
        peakGain: 0.065,
      },
      {
        frequency: 1174.66, // D6
        delaySeconds: 0.165,
        durationSeconds: 0.22,
        type: 'triangle',
        peakGain: 0.05,
      },
    ]);
  }
}

export const soundEffects = new SoundEffectsManager();
