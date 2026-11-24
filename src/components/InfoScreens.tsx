import { ArrowLeft } from 'lucide-react';
import {
  FAQ_LIST,
  FaqItem,
  HOW_TO_PLAY_TEXT,
  PRIVACY_POLICY_TEXT,
  TERMS_TEXT,
} from '../data/wordGameRepository';

interface InfoScreenProps {
  title: string;
  content?: string;
  faqList?: FaqItem[];
  onBack: () => void;
}

function InfoScreen({ title, content = '', faqList, onBack }: InfoScreenProps) {
  return (
    <div className="flex min-h-[100dvh] w-full flex-col bg-gradient-to-b from-[#E0F2FE] via-[#FEF9C3] to-[#DCFCE7]">
      <header className="sticky top-0 z-10 flex h-16 shrink-0 items-center gap-2 bg-[#38BDF8] px-3 shadow-sm">
        <button
          type="button"
          onClick={onBack}
          aria-label="Back"
          className="flex h-10 w-10 shrink-0 cursor-pointer items-center justify-center rounded-full text-slate-900 transition-colors hover:bg-black/10 focus-visible:outline-2 focus-visible:outline-slate-900"
        >
          <ArrowLeft className="h-6 w-6" />
        </button>
        <h1 className="truncate text-lg font-bold text-slate-900">{title}</h1>
      </header>

      <main className="flex-1 overflow-y-auto p-4">
        <div className="mx-auto flex max-w-md flex-col gap-3">
          {faqList ? (
            faqList.map((faq) => (
              <div key={faq.question} className="rounded-2xl bg-white p-4 shadow-xs">
                <h2 className="text-base font-bold text-[#0369A1]">{faq.question}</h2>
                <p className="mt-1 text-sm leading-relaxed text-[#334155]">{faq.answer}</p>
              </div>
            ))
          ) : (
            <div className="rounded-2xl bg-white p-4 shadow-xs">
              <p className="whitespace-pre-line text-[15px] leading-relaxed text-[#334155]">
                {content}
              </p>
            </div>
          )}
        </div>
      </main>
    </div>
  );
}

interface ScreenProps {
  onBack: () => void;
}

export function FAQScreen({ onBack }: ScreenProps) {
  return <InfoScreen title="FAQ (الأسئلة الشائعة)" faqList={FAQ_LIST} onBack={onBack} />;
}

export function PrivacyScreen({ onBack }: ScreenProps) {
  return (
    <InfoScreen
      title="Privacy Policy (سياسة الخصوصية)"
      content={PRIVACY_POLICY_TEXT}
      onBack={onBack}
    />
  );
}

export function TermsScreen({ onBack }: ScreenProps) {
  return (
    <InfoScreen
      title="Terms & Conditions (الشروط والأحكام)"
      content={TERMS_TEXT}
      onBack={onBack}
    />
  );
}

export function HowToPlayScreen({ onBack }: ScreenProps) {
  return (
    <InfoScreen
      title="How to Play (طريقة اللعب)"
      content={HOW_TO_PLAY_TEXT}
      onBack={onBack}
    />
  );
}
