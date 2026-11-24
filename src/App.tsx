import { DailyMissionsModal } from './components/DailyMissionsModal';
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
        return (
          <WelcomeScreen
            difficulty={game.difficulty}
            timeTrialEnabled={game.timeTrialEnabled}
            claimableMissionsCount={game.claimableDailyMissionsCount}
            onSelectDifficulty={game.setDifficulty}
            onToggleTimeTrial={game.setTimeTrialEnabled}
            onOpenDailyMissions={game.openDailyMissions}
            onNavigate={game.navigateTo}
          />
        );
      case AppScreen.LEVEL_MAP:
        return (
          <LevelMapScreen
            levels={game.levels}
            unlockedLevel={game.unlockedLevel}
            score={game.score}
            examBestScores={game.examBestScores}
            claimableMissionsCount={game.claimableDailyMissionsCount}
            onBack={() => game.navigateTo(AppScreen.WELCOME)}
            onSelectLevel={game.selectLevel}
            onStartExam={(examNumber) => game.startExam(examNumber, true)}
            onOpenDailyMissions={game.openDailyMissions}
          />
        );
      case AppScreen.GAMEPLAY:
        return (
          <GameplayScreen
            level={game.currentLevel}
            difficultyConfig={game.difficultyConfig}
            timeTrialEnabled={game.timeTrialEnabled}
            levelTimeLimitSeconds={game.levelTimeLimitSeconds}
            timeRemainingSeconds={game.timeRemainingSeconds}
            isTimeUp={game.isTimeUp}
            lastSpeedBonus={game.lastSpeedBonus}
            extraWordPoints={game.extraWordPoints}
            score={game.score}
            selectedIndices={game.selectedLetterIndices}
            extractedWords={game.currentExtractedWords}
            discoveredExtraWords={game.discoveredExtraWords}
            extraWordFeedback={game.extraWordFeedback}
            canClaimFormedExtraWord={game.canClaimFormedExtraWord}
            isFormedExtraAlreadyClaimed={game.isFormedExtraAlreadyClaimed}
            isLevelComplete={game.isLevelComplete}
            soundEnabled={game.soundEnabled}
            onBack={() => game.navigateTo(AppScreen.LEVEL_MAP)}
            onToggleSound={game.toggleSound}
            onSpeakWord={game.speakWord}
            onUseHint={game.useHint}
            onLetterClick={game.onLetterClicked}
            onClaimExtraWord={game.claimFormedExtraWord}
            onClearSelection={game.clearSelection}
            onRetryLevel={game.retryCurrentLevel}
            onNextLevel={game.nextLevel}
          />
        );
      case AppScreen.EXAM:
        return (
          <ExamScreen
            examNumber={game.activeExamNumber}
            questions={game.activeExamQuestions}
            currentExamIndex={game.currentExamIndex}
            examScore={game.examScore}
            bestScore={game.activeExamBestScore}
            earnedDelta={game.examLastEarnedDelta}
            examCompleted={game.examCompleted}
            enteredFromMap={game.examEnteredFromMap}
            onBackToMap={() => game.navigateTo(AppScreen.LEVEL_MAP)}
            onAnswerExam={game.answerExam}
            onRetakeExam={game.retakeExam}
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
        return (
          <WelcomeScreen
            difficulty={game.difficulty}
            timeTrialEnabled={game.timeTrialEnabled}
            claimableMissionsCount={game.claimableDailyMissionsCount}
            onSelectDifficulty={game.setDifficulty}
            onToggleTimeTrial={game.setTimeTrialEnabled}
            onOpenDailyMissions={game.openDailyMissions}
            onNavigate={game.navigateTo}
          />
        );
    }
  };

  return (
    <div className="flex min-h-[100dvh] w-full justify-center bg-[#0F172A]">
      <div className="w-full max-w-[460px] min-h-[100dvh] shadow-2xl">
        {renderScreen()}
        <DailyMissionsModal
          isOpen={game.isDailyMissionsOpen}
          missionsState={game.dailyMissions}
          onClaimMission={game.claimDailyMission}
          onClose={game.closeDailyMissions}
        />
      </div>
    </div>
  );
}

export default App;
