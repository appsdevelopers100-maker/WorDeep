import { ExamScreen } from './components/ExamScreen';
import { GameplayScreen } from './components/GameplayScreen';
import { FAQScreen, HowToPlayScreen, PrivacyScreen, TermsScreen } from './components/InfoScreens';
import { LevelMapScreen } from './components/LevelMapScreen';
import { SplashScreen } from './components/SplashScreen';
import { WelcomeScreen } from './components/WelcomeScreen';
import { useWordGame } from './hooks/useWordGame';
import { AppScreen } from './model/wordGameModels';

export function App() {
  const game = useWordGame();

  const renderScreen = () => {
    switch (game.currentScreen) {
      case AppScreen.SPLASH:
        return <SplashScreen onComplete={game.navigateTo} />;
      case AppScreen.WELCOME:
        return <WelcomeScreen onNavigate={game.navigateTo} />;
      case AppScreen.LEVEL_MAP:
        return (
          <LevelMapScreen
            levels={game.levels}
            unlockedLevel={game.unlockedLevel}
            score={game.score}
            onBack={() => game.navigateTo(AppScreen.WELCOME)}
            onSelectLevel={game.selectLevel}
            onStartExam={game.startExam}
          />
        );
      case AppScreen.GAMEPLAY:
        return (
          <GameplayScreen
            level={game.currentLevel}
            score={game.score}
            selectedIndices={game.selectedLetterIndices}
            extractedWords={game.currentExtractedWords}
            discoveredExtraWords={game.discoveredExtraWords}
            isLevelComplete={game.isLevelComplete}
            onBack={() => game.navigateTo(AppScreen.LEVEL_MAP)}
            onSpeakWord={game.speakWord}
            onUseHint={game.useHint}
            onLetterClick={game.onLetterClicked}
            onClearSelection={game.clearSelection}
            onNextLevel={game.nextLevel}
          />
        );
      case AppScreen.EXAM:
        return (
          <ExamScreen
            currentExamIndex={game.currentExamIndex}
            examScore={game.examScore}
            examCompleted={game.examCompleted}
            onAnswerExam={game.answerExam}
            onFinishExam={game.finishExam}
          />
        );
      case AppScreen.FAQ:
        return <FAQScreen onBack={() => game.navigateTo(AppScreen.WELCOME)} />;
      case AppScreen.PRIVACY:
        return <PrivacyScreen onBack={() => game.navigateTo(AppScreen.WELCOME)} />;
      case AppScreen.TERMS:
        return <TermsScreen onBack={() => game.navigateTo(AppScreen.WELCOME)} />;
      case AppScreen.HOW_TO_PLAY:
        return <HowToPlayScreen onBack={() => game.navigateTo(AppScreen.WELCOME)} />;
      default:
        return <WelcomeScreen onNavigate={game.navigateTo} />;
    }
  };

  return (
    <div className="flex min-h-[100dvh] w-full justify-center bg-[#0F172A]">
      <div className="w-full max-w-[460px] min-h-[100dvh] shadow-2xl">{renderScreen()}</div>
    </div>
  );
}

export default App;
