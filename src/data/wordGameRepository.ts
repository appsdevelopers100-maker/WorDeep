import { ExamQuestion, GameDifficulty, Level, WordItem } from '../model/wordGameModels';

const w = (word: string, arabic: string, length: number): WordItem => ({
  word,
  arabic,
  length,
  isDiscovered: false,
  revealedIndices: [],
});

export const LEVELS: Level[] = [
  {
    id: 1,
    mainWord: 'FARM',
    arabic: 'مزرعة',
    category: 'Agriculture',
    extractedWords: [w('FAR', 'بعيد', 3), w('ARM', 'ذراع', 3)],
  },
  {
    id: 2,
    mainWord: 'CLOUD',
    arabic: 'سحابة',
    category: 'Nature',
    extractedWords: [w('COD', 'سمك القد', 3), w('LOUD', 'صاخب', 4)],
  },
  {
    id: 3,
    mainWord: 'CHAIR',
    arabic: 'كرسي',
    category: 'Home',
    extractedWords: [w('CAR', 'سيارة', 3), w('AIR', 'هواء', 3), w('HAIR', 'شعر', 4)],
  },
  {
    id: 4,
    mainWord: 'CART',
    arabic: 'عربة',
    category: 'Transport',
    extractedWords: [w('CAR', 'سيارة', 3), w('CAT', 'قطة', 3), w('ART', 'فن', 3)],
  },
  {
    id: 5,
    mainWord: 'JOURNEY',
    arabic: 'رحلة',
    category: 'Adventure',
    extractedWords: [
      w('JOY', 'فرح', 3),
      w('ONE', 'واحد', 3),
      w('OUR', 'لنا', 3),
      w('URN', 'جرة', 3),
    ],
  },
  {
    id: 6,
    mainWord: 'THUNDER',
    arabic: 'رعد',
    category: 'Nature',
    extractedWords: [
      w('THE', 'أداة التعريف', 3),
      w('TUN', 'برميل كبير', 3),
      w('UNDER', 'تحت', 5),
    ],
  },
  {
    id: 7,
    mainWord: 'PYRAMID',
    arabic: 'هرم',
    category: 'History',
    extractedWords: [
      w('AID', 'مساعدة', 3),
      w('RAM', 'كبش', 3),
      w('RID', 'تخلص من', 3),
      w('PAID', 'مدفوع', 4),
    ],
  },
  {
    id: 8,
    mainWord: 'DIAMETER',
    arabic: 'قطر الدائرة',
    extractedWords: [
      w('ATE', 'أكل', 3),
      w('DATE', 'تاريخ / تمر', 4),
      w('METER', 'متر', 5),
    ],
  },
  {
    id: 9,
    mainWord: 'UMBRELLA',
    arabic: 'مظلة',
    category: 'Objects',
    extractedWords: [w('ELL', 'ذراع قياس', 3), w('BELL', 'جرس', 4)],
  },
  {
    id: 10,
    mainWord: 'FLOWER',
    arabic: 'زهور',
    category: 'Nature',
    extractedWords: [
      w('LOW', 'منخفض', 3),
      w('FOR', 'لـ / لأجل', 3),
      w('FLOW', 'تدفق', 4),
      w('LOWER', 'أدنى / يخفض', 5),
    ],
  },
  {
    id: 11,
    mainWord: 'DIAMOND',
    arabic: 'الماس',
    category: 'Jewelry',
    extractedWords: [
      w('DAM', 'سد', 3),
      w('DAD', 'أب', 3),
      w('DIM', 'خافت', 3),
      w('MOD', 'تعديل', 3),
    ],
  },
  {
    id: 12,
    mainWord: 'STONE',
    arabic: 'حجر',
    category: 'Nature',
    extractedWords: [
      w('ONE', 'واحد', 3),
      w('TOE', 'إصبع القدم', 3),
      w('SON', 'ابن', 3),
      w('TONE', 'نغمة', 4),
    ],
  },
  {
    id: 13,
    mainWord: 'SYMPHONY',
    arabic: 'سيمفونية',
    category: 'Music',
    extractedWords: [
      w('SON', 'ابن', 3),
      w('SOY', 'صويا', 3),
      w('SHY', 'خجول', 3),
      w('PHONY', 'زائف', 5),
    ],
  },
  {
    id: 14,
    mainWord: 'HAPPINESS',
    arabic: 'سعادة',
    category: 'Emotions',
    extractedWords: [
      w('APP', 'تطبيق', 3),
      w('PIN', 'دبوس', 3),
      w('APE', 'قرد كبير', 3),
      w('PINE', 'صنوبر', 4),
    ],
  },
  {
    id: 15,
    mainWord: 'MIRACLE',
    arabic: 'معجزة',
    category: 'Wonder',
    extractedWords: [
      w('ICE', 'ثلج', 3),
      w('ACE', 'الآس', 3),
      w('MILE', 'ميل', 4),
      w('MALE', 'ذكر', 4),
      w('RACE', 'سباق', 4),
    ],
  },
  {
    id: 16,
    mainWord: 'SEAT',
    arabic: 'مقعد',
    category: 'School',
    extractedWords: [
      w('SEA', 'بحر', 3),
      w('SET', 'مجموعة', 3),
      w('SAT', 'جلس', 3),
      w('EAT', 'يأكل', 3),
    ],
  },
  {
    id: 17,
    mainWord: 'HEART',
    arabic: 'قلب',
    category: 'Body',
    extractedWords: [
      w('HAT', 'قبعة', 3),
      w('EAT', 'يأكل', 3),
      w('ART', 'فن', 3),
      w('HEAR', 'يسمع', 4),
      w('HEAT', 'حرارة', 4),
    ],
  },
  {
    id: 18,
    mainWord: 'FOREST',
    arabic: 'غابة',
    category: 'Nature',
    extractedWords: [
      w('FOR', 'لـ', 3),
      w('FOE', 'عدو', 3),
      w('ORE', 'خام المعدن', 3),
      w('REST', 'راحة', 4),
      w('FORT', 'حصن', 4),
    ],
  },
  {
    id: 19,
    mainWord: 'SPRING',
    arabic: 'ربيع / نبع',
    category: 'Seasons',
    extractedWords: [
      w('PIN', 'دبوس', 3),
      w('SIN', 'خطيئة', 3),
      w('RING', 'خاتم', 4),
      w('SING', 'يغني', 4),
      w('PING', 'إشارة صوتية', 4),
      w('SPIN', 'يدور', 4),
    ],
  },
  {
    id: 20,
    mainWord: 'BREAD',
    arabic: 'خبز',
    category: 'Food',
    extractedWords: [
      w('BED', 'سرير', 3),
      w('RED', 'أحمر', 3),
      w('BAD', 'سيء', 3),
      w('READ', 'يقرأ', 4),
      w('BEAD', 'خرزة', 4),
    ],
  },
  {
    id: 21,
    mainWord: 'BILLION',
    arabic: 'مليار',
    category: 'Numbers',
    extractedWords: [
      w('ION', 'أيون', 3),
      w('BIO', 'سيرة شخصية', 3),
      w('BILL', 'فاتورة', 4),
      w('LION', 'أسد', 4),
    ],
  },
  {
    id: 22,
    mainWord: 'PROBLEM',
    arabic: 'مشكلة',
    category: 'General',
    extractedWords: [
      w('ROB', 'يسرق', 3),
      w('PRO', 'محترف', 3),
      w('POEM', 'قصيدة', 4),
      w('POLE', 'قطب', 4),
      w('ROLE', 'دور', 4),
    ],
  },
  {
    id: 23,
    mainWord: 'CREATIVITY',
    arabic: 'إبداع',
    category: 'Art',
    extractedWords: [
      w('CAT', 'قطة', 3),
      w('EAT', 'يأكل', 3),
      w('RAT', 'فأر', 3),
      w('CRY', 'يبكي', 3),
    ],
  },
  {
    id: 24,
    mainWord: 'LIBRARY',
    arabic: 'مكتبة',
    category: 'Education',
    extractedWords: [
      w('BAR', 'شريط', 3),
      w('LAY', 'يضع', 3),
      w('RAY', 'شعاع', 3),
      w('BAY', 'خليج', 3),
      w('LIBRA', 'برج الميزان', 5),
    ],
  },
  {
    id: 25,
    mainWord: 'BEAUTIFUL',
    arabic: 'جميل',
    category: 'General',
    extractedWords: [
      w('BAT', 'مضرب', 3),
      w('BUT', 'لكن', 3),
      w('EAT', 'يأكل', 3),
      w('BEAT', 'يهزم', 4),
      w('BEAU', 'عشيق', 4),
    ],
  },
  {
    id: 26,
    mainWord: 'PLANET',
    arabic: 'كوكب',
    category: 'Space',
    extractedWords: [
      w('ANT', 'نملة', 3),
      w('PAN', 'مقلاة', 3),
      w('PAT', 'ربتة', 3),
      w('LAN', 'شبكة', 3),
      w('LANE', 'ممر', 4),
      w('PLAN', 'خطة', 4),
      w('PLANE', 'طائرة', 5),
    ],
  },
  {
    id: 27,
    mainWord: 'CARRIAGE',
    arabic: 'عربة كابينة',
    category: 'Transport',
    extractedWords: [
      w('ARE', 'يكونون', 3),
      w('AGE', 'عمر', 3),
      w('CAR', 'سيارة', 3),
      w('RAG', 'خرقة', 3),
      w('RIG', 'منصة', 3),
      w('CAGE', 'قفص', 4),
      w('RAGE', 'غضب', 4),
    ],
  },
  {
    id: 28,
    mainWord: 'FOOTBALL',
    arabic: 'كرة القدم',
    category: 'Sports',
    extractedWords: [
      w('ALL', 'الكل', 3),
      w('TALL', 'طويل', 4),
      w('FALL', 'سقوط', 4),
      w('BALL', 'كرة', 4),
      w('FOOL', 'أحمق', 4),
      w('FOAL', 'مهر', 4),
      w('FOOT', 'قدم', 4),
    ],
  },
  {
    id: 29,
    mainWord: 'ADVENTURE',
    arabic: 'مغامرة',
    category: 'Adventure',
    extractedWords: [
      w('DEN', 'عرين', 3),
      w('VET', 'طبيب بيطري', 3),
      w('DUE', 'مستحق', 3),
      w('DENT', 'انبعاج', 4),
      w('VENT', 'فتحة', 4),
      w('VENTURE', 'مغامرة', 7),
    ],
  },
  {
    id: 30,
    mainWord: 'NOTEBOOK',
    arabic: 'دفتر ملاحظات',
    category: 'School',
    extractedWords: [
      w('NOT', 'ليس', 3),
      w('TOO', 'أيضاً', 3),
      w('NOOK', 'ركن', 4),
      w('NOTE', 'ملاحظة', 4),
      w('TOOK', 'أخذ', 4),
      w('BOOK', 'كتاب', 4),
    ],
  },
  {
    id: 31,
    mainWord: 'HOLIDAY',
    arabic: 'عطلة',
    category: 'Time',
    extractedWords: [
      w('HAY', 'قش', 3),
      w('DAY', 'يوم', 3),
      w('OLD', 'قديم', 3),
      w('LAY', 'يستلقي', 3),
      w('LID', 'غطاء', 3),
      w('HOLD', 'يمسك', 4),
      w('HOLY', 'مقدس', 4),
    ],
  },
  {
    id: 32,
    mainWord: 'TREASURE',
    arabic: 'كنز',
    category: 'Adventure',
    extractedWords: [
      w('TEA', 'شاي', 3),
      w('EAR', 'أذن', 3),
      w('EASE', 'سهولة', 4),
      w('REAR', 'خلفي', 4),
      w('RARE', 'نادر', 4),
      w('TRUE', 'حقيقي', 4),
      w('SURE', 'بالتأكيد', 4),
      w('TEASE', 'يضايق', 5),
    ],
  },
  {
    id: 33,
    mainWord: 'MARKET',
    arabic: 'سوق',
    category: 'Places',
    extractedWords: [
      w('ART', 'فن', 3),
      w('MET', 'التقى', 3),
      w('MAT', 'سجادة', 3),
      w('MARE', 'أنثى الحصان', 4),
      w('MAKE', 'يصنع', 4),
      w('MART', 'متجر', 4),
      w('MARK', 'علامة', 4),
    ],
  },
  {
    id: 34,
    mainWord: 'FREEDOM',
    arabic: 'حرية',
    category: 'General',
    extractedWords: [
      w('FEE', 'رسوم', 3),
      w('RED', 'أحمر', 3),
      w('FED', 'أطعم', 3),
      w('FREE', 'حر', 4),
      w('FEED', 'يطعم', 4),
      w('REED', 'قصب', 4),
      w('FROM', 'من', 4),
    ],
  },
  {
    id: 35,
    mainWord: 'WATERFALL',
    arabic: 'شلال',
    category: 'Nature',
    extractedWords: [
      w('ALL', 'الكل', 3),
      w('TEA', 'شاي', 3),
      w('ATE', 'أكل', 3),
      w('FALL', 'سقوط', 4),
      w('TALL', 'طويل', 4),
      w('WALL', 'جدار', 4),
      w('WATER', 'ماء', 5),
    ],
  },
  {
    id: 36,
    mainWord: 'MOUNTAIN',
    arabic: 'جبل',
    category: 'Nature',
    extractedWords: [
      w('TAN', 'أسمر', 3),
      w('TIN', 'قصدير', 3),
      w('MAIN', 'رئيسي', 4),
      w('MOAN', 'أنين', 4),
      w('MOUNT', 'يركب', 5),
    ],
  },
  {
    id: 37,
    mainWord: 'BRIGHT',
    arabic: 'ساطع',
    category: 'Light',
    extractedWords: [
      w('BIT', 'قطعة', 3),
      w('BIG', 'كبير', 3),
      w('RIG', 'معدة', 3),
      w('BIGHT', 'خليج', 5),
      w('RIGHT', 'صحيح', 5),
    ],
  },
  {
    id: 38,
    mainWord: 'ATTENTION',
    arabic: 'انتباه',
    category: 'School',
    extractedWords: [
      w('ATE', 'أكل', 3),
      w('TEN', 'عشرة', 3),
      w('ION', 'أيون', 3),
      w('ANT', 'نملة', 3),
      w('TENT', 'خيمة', 4),
    ],
  },
  {
    id: 39,
    mainWord: 'BUTTERFLY',
    arabic: 'فراشة',
    category: 'Nature',
    extractedWords: [
      w('BUT', 'لكن', 3),
      w('BUY', 'يشتري', 3),
      w('TRY', 'يحاول', 3),
      w('FLY', 'يطير', 3),
      w('BUTT', 'طرف', 4),
      w('UTTER', 'ينطق', 5),
      w('BUTTER', 'زبدة', 6),
      w('UTTERLY', 'تماما', 7),
    ],
  },
  {
    id: 40,
    mainWord: 'FANTASTIC',
    arabic: 'رائع',
    category: 'General',
    extractedWords: [
      w('ANT', 'نملة', 3),
      w('FAN', 'مروحة', 3),
      w('FAT', 'سمين', 3),
      w('TIC', 'تشنج', 3),
      w('ATTIC', 'علية', 5),
    ],
  },
  {
    id: 41,
    mainWord: 'WONDERFUL',
    arabic: 'رائع',
    category: 'General',
    extractedWords: [
      w('ONE', 'واحد', 3),
      w('ODE', 'قصيدة', 3),
      w('WON', 'فاز', 3),
      w('WOE', 'حزن', 3),
      w('WONDER', 'عجب', 6),
      w('WOEFUL', 'محزن', 6),
    ],
  },
  {
    id: 42,
    mainWord: 'BUILDING',
    arabic: 'مبنى',
    category: 'Places',
    extractedWords: [
      w('DIN', 'ضوضاء', 3),
      w('BUD', 'برعم', 3),
      w('BUG', 'حشرة', 3),
      w('BIG', 'كبير', 3),
      w('BIN', 'سلة', 3),
      w('DIG', 'يحفر', 3),
      w('DING', 'رنين', 4),
      w('BUILD', 'يبني', 5),
    ],
  },
  {
    id: 43,
    mainWord: 'CHILDREN',
    arabic: 'أطفال',
    category: 'People',
    extractedWords: [
      w('HID', 'اختبأ', 3),
      w('DEN', 'عرين', 3),
      w('HEN', 'دجاجة', 3),
      w('HIDE', 'يختبئ', 4),
      w('CHIN', 'ذقن', 4),
      w('CHIDE', 'ينهر', 5),
      w('CHILD', 'طفل', 5),
    ],
  },
  {
    id: 44,
    mainWord: 'SUNSHINE',
    arabic: 'أشعة الشمس',
    category: 'Nature',
    extractedWords: [
      w('SUN', 'شمس', 3),
      w('SIN', 'خطيئة', 3),
      w('USE', 'يستخدم', 3),
      w('SUE', 'يقاضي', 3),
      w('SHIN', 'عظمة الساق', 4),
      w('SHINE', 'يشرق', 5),
    ],
  },
  {
    id: 45,
    mainWord: 'FRIENDSHIP',
    arabic: 'صداقة',
    category: 'Social',
    extractedWords: [
      w('DIP', 'غمس', 3),
      w('HIP', 'ورك', 3),
      w('RED', 'أحمر', 3),
      w('FIN', 'زعنفة', 3),
      w('END', 'نهاية', 3),
      w('FIND', 'يجد', 4),
      w('SHIP', 'سفينة', 4),
      w('FRIES', 'بطاطس', 5),
      w('FRIED', 'مقلي', 5),
    ],
  },
  {
    id: 46,
    mainWord: 'HOSPITAL',
    arabic: 'مستشفى',
    category: 'Health',
    extractedWords: [
      w('HIT', 'يضرب', 3),
      w('HOT', 'حار', 3),
      w('HOP', 'يقفز', 3),
      w('OIL', 'زيت', 3),
      w('PIT', 'حفرة', 3),
      w('SIT', 'يجلس', 3),
      w('SPA', 'منتجع', 3),
      w('OPT', 'يختار', 3),
      w('HOST', 'مضيف', 4),
      w('SPIT', 'يبصق', 4),
    ],
  },
  {
    id: 47,
    mainWord: 'ANIMATION',
    arabic: 'رسوم متحركة',
    category: 'Art',
    extractedWords: [
      w('AIM', 'هدف', 3),
      w('ANT', 'نملة', 3),
      w('ION', 'أيون', 3),
      w('MAN', 'رجل', 3),
      w('MAT', 'سجادة', 3),
      w('MAIN', 'رئيسي', 4),
      w('NATION', 'أمة', 6),
    ],
  },
  {
    id: 48,
    mainWord: 'PRESENT',
    arabic: 'هدية / حاضر',
    category: 'General',
    extractedWords: [
      w('SET', 'مجموعة', 3),
      w('PET', 'حيوان', 3),
      w('PEN', 'قلم', 3),
      w('PEST', 'آفة', 4),
      w('REST', 'راحة', 4),
      w('RENT', 'إيجار', 4),
      w('SENT', 'أرسل', 4),
      w('RESET', 'إعادة', 5),
      w('PREEN', 'ينظف', 5),
      w('PRESET', 'إعداد', 6),
    ],
  },
  {
    id: 49,
    mainWord: 'EDUCATION',
    arabic: 'تعليم',
    category: 'School',
    extractedWords: [
      w('EAT', 'يأكل', 3),
      w('CON', 'خدعة', 3),
      w('TON', 'طن', 3),
      w('CAN', 'يستطيع', 3),
      w('CAT', 'قطة', 3),
      w('DIN', 'ضوضاء', 3),
      w('ION', 'أيون', 3),
      w('TIN', 'قصدير', 3),
      w('DUCT', 'أنبوب', 4),
    ],
  },
  {
    id: 50,
    mainWord: 'PAINTING',
    arabic: 'لوحة / رسم',
    category: 'Art',
    extractedWords: [
      w('ANT', 'نملة', 3),
      w('PIN', 'دبوس', 3),
      w('TIN', 'قصدير', 3),
      w('PAN', 'مقلاة', 3),
      w('PAT', 'ربتة', 3),
      w('ANTI', 'مضاد', 4),
      w('PAIN', 'ألم', 4),
      w('PANT', 'يلهث', 4),
      w('PINT', 'باينت', 4),
      w('PING', 'إشارة', 4),
      w('TING', 'رنين', 4),
      w('PAINT', 'طلاء', 5),
    ],
  },
];

const HARD_BONUS_WORDS: Record<number, WordItem[]> = {
  23: [w('CITY', 'مدينة', 4), w('CAVITY', 'تجويف', 6)],
};

export function getLevelsForDifficulty(difficulty: GameDifficulty): Level[] {
  return LEVELS.map((level) => {
    const baseWords = level.extractedWords.filter((item) => item.length >= 3);

    if (difficulty === GameDifficulty.EASY) {
      // Easy uses simpler, shorter words (sorted shortest-first, up to 3 words per level)
      const sortedSimple = [...baseWords].sort((a, b) => a.length - b.length);
      const maxCount = Math.min(3, sortedSimple.length);
      return {
        ...level,
        extractedWords: sortedSimple.slice(0, Math.max(2, maxCount)),
      };
    }

    if (difficulty === GameDifficulty.HARD) {
      // Hard prioritizes complex 4+ letter words and includes extra complex target words
      const bonus = HARD_BONUS_WORDS[level.id] ?? [];
      const combined = [...baseWords];
      for (const b of bonus) {
        if (!combined.some((existing) => existing.word === b.word)) {
          combined.push(b);
        }
      }
      const complexOnly = combined.filter((item) => item.length >= 4);
      const pool = complexOnly.length >= 2 ? complexOnly : combined;
      const sortedHard = [...pool].sort((a, b) => b.length - a.length);
      return {
        ...level,
        extractedWords: sortedHard,
      };
    }

    // Medium keeps a balanced mix (up to 6 words)
    return {
      ...level,
      extractedWords: baseWords.slice(0, Math.max(2, Math.min(6, baseWords.length))),
    };
  });
}

// Distinct, varied questions for Exams 1 to 10 (5 questions per exam, 10 points max per exam)
export const EXAMS_BY_NUMBER: Record<number, ExamQuestion[]> = {
  1: [
    {
      id: 101,
      question: "Which word can be formed from 'FARM' using left-to-right selection?",
      arabic: 'أي كلمة يمكن تكوينها من FARM باختيار الحروف من اليسار لليمين؟',
      options: ['ARM', 'RAM', 'MAR', 'MRA'],
      correctIndex: 0,
    },
    {
      id: 102,
      question: "What does 'CLOUD' mean in Arabic?",
      arabic: 'ما معنى كلمة CLOUD بالعربية؟',
      options: ['شمس', 'سحابة', 'مطر', 'رعد'],
      correctIndex: 1,
    },
    {
      id: 103,
      question: "Which 4-letter word is hidden inside 'CHAIR'?",
      arabic: 'ما هي الكلمة المكونة من ٤ حروف والمخفية داخل CHAIR؟',
      options: ['CHIP', 'CHAT', 'HAIR', 'CHIN'],
      correctIndex: 2,
    },
    {
      id: 104,
      question: "Complete the missing letters for 'عربة': C _ R _",
      arabic: 'أكمل الحروف الناقصة لكلمة عربة:',
      options: ['E, D', 'O, N', 'U, P', 'A, T'],
      correctIndex: 3,
    },
    {
      id: 105,
      question: "Which word can be formed left-to-right from 'JOURNEY'?",
      arabic: 'أي كلمة يمكن تكوينها من اليسار لليمين من JOURNEY؟',
      options: ['RUN', 'JOY', 'YOU', 'EAR'],
      correctIndex: 1,
    },
  ],
  2: [
    {
      id: 201,
      question: "What does 'THUNDER' mean in Arabic?",
      arabic: 'ما معنى كلمة THUNDER بالعربية؟',
      options: ['برق', 'رعد', 'سحابة', 'ريح'],
      correctIndex: 1,
    },
    {
      id: 202,
      question: "Which 4-letter word can be formed left-to-right from 'PYRAMID'?",
      arabic: 'أي كلمة يمكن تكوينها من اليسار لليمين من PYRAMID؟',
      options: ['PAID', 'DAMP', 'MIND', 'PLAY'],
      correctIndex: 0,
    },
    {
      id: 203,
      question: "Fill in the missing letters for 'قطر الدائرة': D I A M _ T _ R",
      arabic: 'أكمل الحروف الناقصة لكلمة قطر الدائرة:',
      options: ['A, E', 'E, A', 'E, E', 'O, E'],
      correctIndex: 2,
    },
    {
      id: 204,
      question: "Which English word means 'مظلة'?",
      arabic: 'أي كلمة إنجليزية تعني مظلة؟',
      options: ['FLOWER', 'THUNDER', 'DIAMETER', 'UMBRELLA'],
      correctIndex: 3,
    },
    {
      id: 205,
      question: "Which 5-letter word is hidden left-to-right inside 'FLOWER'?",
      arabic: 'ما الكلمة المكونة من ٥ حروف داخل FLOWER من اليسار لليمين؟',
      options: ['WORLD', 'LOWER', 'FLOOR', 'POWER'],
      correctIndex: 1,
    },
  ],
  3: [
    {
      id: 301,
      question: "What does 'DIAMOND' mean in Arabic?",
      arabic: 'ما معنى كلمة DIAMOND بالعربية؟',
      options: ['ذهب', 'فضة', 'الماس', 'حجر'],
      correctIndex: 2,
    },
    {
      id: 302,
      question: "Which 4-letter word is hidden left-to-right inside 'STONE'?",
      arabic: 'أي كلمة من ٤ حروف مخفية داخل STONE من اليسار لليمين؟',
      options: ['TONE', 'NOTE', 'NEST', 'SENT'],
      correctIndex: 0,
    },
    {
      id: 303,
      question: "Complete the spelling of 'سيمفونية': S Y M _ H O _ Y",
      arabic: 'أكمل تهجئة كلمة سيمفونية:',
      options: ['B, M', 'F, N', 'P, M', 'P, N'],
      correctIndex: 3,
    },
    {
      id: 304,
      question: "Which English word means 'سعادة'?",
      arabic: 'أي كلمة إنجليزية تعني سعادة؟',
      options: ['STONE', 'HAPPINESS', 'MIRACLE', 'SYMPHONY'],
      correctIndex: 1,
    },
    {
      id: 305,
      question: "Which 4-letter word can be formed left-to-right from 'MIRACLE'?",
      arabic: 'أي كلمة يمكن تكوينها من اليسار لليمين من MIRACLE؟',
      options: ['CREAM', 'CLEAR', 'RACE', 'CARE'],
      correctIndex: 2,
    },
  ],
  4: [
    {
      id: 401,
      question: "Which word can be formed left-to-right from 'SEAT'?",
      arabic: 'أي كلمة يمكن تكوينها من اليسار لليمين من SEAT؟',
      options: ['EAST', 'TEA', 'SEA', 'ATE'],
      correctIndex: 2,
    },
    {
      id: 402,
      question: "What does 'HEART' mean in Arabic?",
      arabic: 'ما معنى كلمة HEART بالعربية؟',
      options: ['عين', 'قلب', 'يد', 'عقل'],
      correctIndex: 1,
    },
    {
      id: 403,
      question: "Complete the missing letters for 'غابة': F _ R _ S T",
      arabic: 'أكمل الحروف الناقصة لكلمة غابة:',
      options: ['O, E', 'A, E', 'O, A', 'U, E'],
      correctIndex: 0,
    },
    {
      id: 404,
      question: "Which 4-letter word is hidden left-to-right inside 'SPRING'?",
      arabic: 'أي كلمة من ٤ حروف مخفية داخل SPRING من اليسار لليمين؟',
      options: ['GRIN', 'KING', 'SONG', 'RING'],
      correctIndex: 3,
    },
    {
      id: 405,
      question: "Which English word means 'خبز'?",
      arabic: 'أي كلمة إنجليزية تعني خبز؟',
      options: ['BREAD', 'FOREST', 'SPRING', 'SEAT'],
      correctIndex: 0,
    },
  ],
  5: [
    {
      id: 501,
      question: "What does 'BILLION' mean in Arabic?",
      arabic: 'ما معنى كلمة BILLION بالعربية؟',
      options: ['مليون', 'ألف', 'مليار', 'مئة'],
      correctIndex: 2,
    },
    {
      id: 502,
      question: "Which 4-letter word can be formed left-to-right from 'PROBLEM'?",
      arabic: 'أي كلمة من ٤ حروف مخفية داخل PROBLEM من اليسار لليمين؟',
      options: ['MORE', 'POEM', 'ROPE', 'BORE'],
      correctIndex: 1,
    },
    {
      id: 503,
      question: "Fill in the missing letters for 'مكتبة': L I B _ A _ Y",
      arabic: 'أكمل الحروف الناقصة لكلمة مكتبة:',
      options: ['R, R', 'L, R', 'R, N', 'B, R'],
      correctIndex: 0,
    },
    {
      id: 504,
      question: "Which English word means 'إبداع'?",
      arabic: 'أي كلمة إنجليزية تعني إبداع؟',
      options: ['PROBLEM', 'LIBRARY', 'BEAUTIFUL', 'CREATIVITY'],
      correctIndex: 3,
    },
    {
      id: 505,
      question: "Which 4-letter word can be formed left-to-right from 'BEAUTIFUL'?",
      arabic: 'أي كلمة يمكن تكوينها من اليسار لليمين من BEAUTIFUL؟',
      options: ['TUBE', 'BEAT', 'LATE', 'TAIL'],
      correctIndex: 1,
    },
  ],
  6: [
    {
      id: 601,
      question: "What does 'PLANET' mean in Arabic?",
      arabic: 'ما معنى كلمة PLANET بالعربية؟',
      options: ['نجم', 'كوكب', 'قمر', 'شمس'],
      correctIndex: 1,
    },
    {
      id: 602,
      question: "Which 4-letter word can be formed left-to-right from 'CARRIAGE'?",
      arabic: 'أي كلمة يمكن تكوينها من اليسار لليمين من CARRIAGE؟',
      options: ['GEAR', 'DEAR', 'RAGE', 'BEAR'],
      correctIndex: 2,
    },
    {
      id: 603,
      question: "Complete the missing letters for 'كرة القدم': F O O T _ A _ L",
      arabic: 'أكمل الحروف الناقصة لكلمة كرة القدم:',
      options: ['B, L', 'P, L', 'B, R', 'D, L'],
      correctIndex: 0,
    },
    {
      id: 604,
      question: "Which English word means 'مغامرة'?",
      arabic: 'أي كلمة إنجليزية تعني مغامرة؟',
      options: ['NOTEBOOK', 'PLANET', 'CARRIAGE', 'ADVENTURE'],
      correctIndex: 3,
    },
    {
      id: 605,
      question: "Which 4-letter word is hidden left-to-right inside 'NOTEBOOK'?",
      arabic: 'أي كلمة من ٤ حروف مخفية داخل NOTEBOOK من اليسار لليمين؟',
      options: ['BONE', 'BOOK', 'BOAT', 'BOOT'],
      correctIndex: 1,
    },
  ],
  7: [
    {
      id: 701,
      question: "What does 'HOLIDAY' mean in Arabic?",
      arabic: 'ما معنى كلمة HOLIDAY بالعربية؟',
      options: ['عمل', 'مدرسة', 'عطلة', 'سفر'],
      correctIndex: 2,
    },
    {
      id: 702,
      question: "Which 4-letter word is hidden left-to-right inside 'TREASURE'?",
      arabic: 'أي كلمة من ٤ حروف مخفية داخل TREASURE من اليسار لليمين؟',
      options: ['TRUE', 'STAR', 'RATE', 'SEAT'],
      correctIndex: 0,
    },
    {
      id: 703,
      question: "Complete the spelling of 'سوق': M _ R K _ T",
      arabic: 'أكمل تهجئة كلمة سوق:',
      options: ['E, A', 'O, E', 'A, I', 'A, E'],
      correctIndex: 3,
    },
    {
      id: 704,
      question: "Which English word means 'حرية'?",
      arabic: 'أي كلمة إنجليزية تعني حرية؟',
      options: ['MARKET', 'FREEDOM', 'TREASURE', 'WATERFALL'],
      correctIndex: 1,
    },
    {
      id: 705,
      question: "Which 5-letter word can be formed left-to-right from 'WATERFALL'?",
      arabic: 'أي كلمة من ٥ حروف مخفية داخل WATERFALL من اليسار لليمين؟',
      options: ['LATER', 'AFTER', 'WATER', 'FLAME'],
      correctIndex: 2,
    },
  ],
  8: [
    {
      id: 801,
      question: "What does 'MOUNTAIN' mean in Arabic?",
      arabic: 'ما معنى كلمة MOUNTAIN بالعربية؟',
      options: ['بحر', 'جبل', 'نهر', 'وادي'],
      correctIndex: 1,
    },
    {
      id: 802,
      question: "Which 5-letter word is hidden left-to-right inside 'BRIGHT'?",
      arabic: 'أي كلمة من ٥ حروف مخفية داخل BRIGHT من اليسار لليمين؟',
      options: ['RIGHT', 'LIGHT', 'NIGHT', 'TIGHT'],
      correctIndex: 0,
    },
    {
      id: 803,
      question: "Fill in the missing letters for 'انتباه': A T T E _ T _ O N",
      arabic: 'أكمل الحروف الناقصة لكلمة انتباه:',
      options: ['M, I', 'N, E', 'N, I', 'R, I'],
      correctIndex: 2,
    },
    {
      id: 804,
      question: "Which English word means 'فراشة'?",
      arabic: 'أي كلمة إنجليزية تعني فراشة؟',
      options: ['FANTASTIC', 'BRIGHT', 'MOUNTAIN', 'BUTTERFLY'],
      correctIndex: 3,
    },
    {
      id: 805,
      question: "Which 5-letter word can be formed left-to-right from 'FANTASTIC'?",
      arabic: 'أي كلمة من ٥ حروف مخفية داخل FANTASTIC من اليسار لليمين؟',
      options: ['STACK', 'ATTIC', 'TASTE', 'SCENT'],
      correctIndex: 1,
    },
  ],
  9: [
    {
      id: 901,
      question: "What does 'WONDERFUL' mean in Arabic?",
      arabic: 'ما معنى كلمة WONDERFUL بالعربية؟',
      options: ['رائع', 'صعب', 'سريع', 'قديم'],
      correctIndex: 0,
    },
    {
      id: 902,
      question: "Which 5-letter word is hidden left-to-right inside 'BUILDING'?",
      arabic: 'أي كلمة من ٥ حروف مخفية داخل BUILDING من اليسار لليمين؟',
      options: ['BLIND', 'GUILD', 'BUILD', 'BINGO'],
      correctIndex: 2,
    },
    {
      id: 903,
      question: "Complete the spelling of 'أطفال': C H _ L D _ E N",
      arabic: 'أكمل تهجئة كلمة أطفال:',
      options: ['E, R', 'I, R', 'I, L', 'A, R'],
      correctIndex: 1,
    },
    {
      id: 904,
      question: "Which English word means 'أشعة الشمس'?",
      arabic: 'أي كلمة إنجليزية تعني أشعة الشمس؟',
      options: ['BUILDING', 'WONDERFUL', 'FRIENDSHIP', 'SUNSHINE'],
      correctIndex: 3,
    },
    {
      id: 905,
      question: "Which 4-letter word can be formed left-to-right from 'FRIENDSHIP'?",
      arabic: 'أي كلمة من ٤ حروف مخفية داخل FRIENDSHIP من اليسار لليمين؟',
      options: ['SHIP', 'PINE', 'HIDE', 'RIDE'],
      correctIndex: 0,
    },
  ],
  10: [
    {
      id: 1001,
      question: "What does 'HOSPITAL' mean in Arabic?",
      arabic: 'ما معنى كلمة HOSPITAL بالعربية؟',
      options: ['مدرسة', 'مكتبة', 'مستشفى', 'سوق'],
      correctIndex: 2,
    },
    {
      id: 1002,
      question: "Which 6-letter word is hidden left-to-right inside 'ANIMATION'?",
      arabic: 'أي كلمة من ٦ حروف مخفية داخل ANIMATION من اليسار لليمين؟',
      options: ['MOTION', 'NATION', 'ACTION', 'STATION'],
      correctIndex: 1,
    },
    {
      id: 1003,
      question: "Fill in the missing letters for 'هدية / حاضر': P R _ S _ N T",
      arabic: 'أكمل الحروف الناقصة لكلمة هدية / حاضر:',
      options: ['E, E', 'A, E', 'E, A', 'I, E'],
      correctIndex: 0,
    },
    {
      id: 1004,
      question: "Which English word means 'تعليم'?",
      arabic: 'أي كلمة إنجليزية تعني تعليم؟',
      options: ['PAINTING', 'ANIMATION', 'HOSPITAL', 'EDUCATION'],
      correctIndex: 3,
    },
    {
      id: 1005,
      question: "Which 5-letter word can be formed left-to-right from 'PAINTING'?",
      arabic: 'أي كلمة من ٥ حروف مخفية داخل PAINTING من اليسار لليمين؟',
      options: ['GIANT', 'PLANT', 'PAINT', 'TRAIN'],
      correctIndex: 2,
    },
  ],
};

export function getQuestionsForExam(examNumber: number): ExamQuestion[] {
  return EXAMS_BY_NUMBER[examNumber] ?? EXAMS_BY_NUMBER[1];
}

export interface FaqItem {
  question: string;
  answer: string;
}

export const FAQ_LIST: FaqItem[] = [
  {
    question: 'What is WORDIG?',
    answer:
      'WORDIG is an engaging word puzzle game where players find smaller words (at least 3 letters) hidden inside a larger main word following specific left-to-right sequencing rules.',
  },
  {
    question: 'Does the game require internet?',
    answer: 'No! WORDIG is built as an offline-friendly game experience.',
  },
  {
    question: 'How are points calculated?',
    answer:
      'Points depend on word length (3 letters = 8 pts, 4 letters = 12 pts, 5 letters = 15 pts, 6+ letters = 20 pts). Extra valid English words (3+ letters) award +5 pts each. Hints cost 50 points and reveal 1 letter. Each Exam Checkpoint is worth up to 10 points and can be retaken anytime to improve your best score.',
  },
  {
    question: 'Can I go backward when selecting letters?',
    answer: 'No. Letters must be selected from left to right.',
  },
];

export const PRIVACY_POLICY_TEXT = `WORDIG – PRIVACY POLICY
Last Updated: October 15, 2026
1. DATA COLLECTION: Offline-first game, no personal data collected.
2. LOCAL STORAGE: Game progress is stored locally on your device.`;

export const TERMS_TEXT = `WORDIG – TERMS AND CONDITIONS
Last Updated: October 15, 2026
1. ACCEPTANCE: By playing WORDIG, you agree to these terms.
2. RULES: Left-to-right selection, minimum 3-letter words, no backward selection.`;

export const HOW_TO_PLAY_TEXT = `EVERY WORD HIDES MORE.

WORDIG is a word puzzle game where one main word hides many smaller words. Your challenge is to discover them.

THE MAIN RULE:
Letters must always be selected from left to right (minimum 3 letters per word). You can skip letters, but you cannot go backward or return to an earlier letter after selecting a later one.

FIND THE HIDDEN WORDS:
Below the main word, you will see word boxes containing empty squares. The number of squares shows the number of letters in a hidden word (e.g. [ ] [ ] [ ] means a 3-letter word).

SCORE MORE POINTS & EXTRA WORDS:
Longer words are worth more points. Valid 3+ letter English words outside the target list count as Extra Words and award +5 points each. Hints cost 50 points and reveal 1 letter of a hidden word. Every 5 levels unlocks a 10-point Exam Checkpoint that you can retake anytime to improve your best score.`;
