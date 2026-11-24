// Lightweight Vibration API wrapper for tactile feedback on mobile browsers.
// Safely checks window.navigator.vibrate and mirrors the sound effects manager API.

class HapticFeedbackManager {
  private enabled = true;

  public setEnabled(enabled: boolean): void {
    this.enabled = enabled;
  }

  public isEnabled(): boolean {
    return this.enabled;
  }

  private trigger(pattern: number | number[]): void {
    if (!this.enabled) return;
    if (
      typeof window === 'undefined' ||
      typeof window.navigator === 'undefined' ||
      typeof window.navigator.vibrate !== 'function'
    ) {
      return;
    }

    try {
      window.navigator.vibrate(pattern);
    } catch {
      // Ignore errors in browsers or iframes that restrict vibration
    }
  }

  /** Crisp short tap when selecting or deselecting a letter tile */
  public vibrateTileClick(isDeselect = false): void {
    this.trigger(isDeselect ? 8 : 12);
  }

  /** Distinct double-pulse confirmation when matching a hidden target word */
  public vibrateWordMatch(): void {
    this.trigger([20, 35, 35]);
  }

  /** Sparkling triple-pulse for discovering a new Extra Word (+5 pts) */
  public vibrateExtraWord(isDuplicate = false): void {
    if (isDuplicate) {
      this.trigger(10);
      return;
    }
    this.trigger([15, 30, 15, 30, 28]);
  }
}

export const hapticFeedback = new HapticFeedbackManager();
