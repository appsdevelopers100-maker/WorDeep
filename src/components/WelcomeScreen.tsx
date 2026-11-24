import { AppScreen } from '../model/wordGameModels';

interface WelcomeScreenProps {
  onNavigate: (screen: AppScreen) => void;
}

export function WelcomeScreen({ onNavigate }: WelcomeScreenProps) {
  return (
    <div className="relative flex min-h-[100dvh] w-full items-center justify-center bg-[#0F172A]">
      <div className="relative aspect-[9/20] max-h-[100dvh] w-full max-w-[440px] overflow-hidden shadow-2xl">
        <img
          src="/launchpage.png"
          alt="WorDig Welcome Artwork"
          referrerPolicy="no-referrer"
          className="h-full w-full object-fill select-none"
        />

        {/* START PLAYING (center orange puzzle piece) */}
        <button
          type="button"
          aria-label="Start Playing"
          onClick={() => onNavigate(AppScreen.LEVEL_MAP)}
          className="absolute top-[44%] left-[22%] h-[16.5%] w-[56%] cursor-pointer rounded-2xl transition-transform duration-150 hover:bg-white/10 focus-visible:outline-2 focus-visible:outline-white active:scale-95"
        />

        {/* HOW TO PLAY (middle-lower left orange puzzle piece) */}
        <button
          type="button"
          aria-label="How to Play"
          onClick={() => onNavigate(AppScreen.HOW_TO_PLAY)}
          className="absolute top-[62.5%] left-[13%] h-[9%] w-[37.5%] cursor-pointer rounded-2xl transition-transform duration-150 hover:bg-white/10 focus-visible:outline-2 focus-visible:outline-white active:scale-95"
        />

        {/* FAQ (middle-lower right teal puzzle piece) */}
        <button
          type="button"
          aria-label="FAQ"
          onClick={() => onNavigate(AppScreen.FAQ)}
          className="absolute top-[61.5%] left-[55.5%] h-[10%] w-[32%] cursor-pointer rounded-2xl transition-transform duration-150 hover:bg-white/10 focus-visible:outline-2 focus-visible:outline-white active:scale-95"
        />

        {/* Privacy Policy (bottom left white puzzle piece) */}
        <button
          type="button"
          aria-label="Privacy Policy"
          onClick={() => onNavigate(AppScreen.PRIVACY)}
          className="absolute top-[78.5%] left-[9.5%] h-[9.5%] w-[41%] cursor-pointer rounded-2xl transition-transform duration-150 hover:bg-sky-500/10 focus-visible:outline-2 focus-visible:outline-sky-700 active:scale-95"
        />

        {/* Terms & Conditions (bottom right white puzzle piece) */}
        <button
          type="button"
          aria-label="Terms & Conditions"
          onClick={() => onNavigate(AppScreen.TERMS)}
          className="absolute top-[78.5%] left-[51%] h-[9.5%] w-[39.5%] cursor-pointer rounded-2xl transition-transform duration-150 hover:bg-sky-500/10 focus-visible:outline-2 focus-visible:outline-sky-700 active:scale-95"
        />
      </div>
    </div>
  );
}
