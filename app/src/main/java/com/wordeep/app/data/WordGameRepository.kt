package com.wordeep.app.data

import com.wordeep.app.model.ExamQuestion
import com.wordeep.app.model.Level
import com.wordeep.app.model.WordItem

object WordGameRepository {

    val levels: List<Level> = listOf(
        Level(
            id = 1,
            mainWord = "FARM",
            arabic = "مزرعة",
            category = "Agriculture",
            extractedWords = listOf(
                WordItem("FAR", "بعيد", 3),
                WordItem("ARM", "ذراع", 3)
            )
        ),
        Level(
            id = 2,
            mainWord = "CLOUD",
            arabic = "سحابة",
            category = "Nature",
            extractedWords = listOf(
                WordItem("COD", "سمك القد", 3),
                WordItem("LOUD", "صاخب", 4)
            )
        ),
        Level(
            id = 3,
            mainWord = "CHAIR",
            arabic = "كرسي",
            category = "Home",
            extractedWords = listOf(
                WordItem("CAR", "سيارة", 3),
                WordItem("AIR", "هواء", 3),
                WordItem("HAIR", "شعر", 4)
            )
        ),
        Level(
            id = 4,
            mainWord = "CART",
            arabic = "عربة",
            category = "Transport",
            extractedWords = listOf(
                WordItem("CAR", "سيارة", 3),
                WordItem("CAT", "قطة", 3),
                WordItem("ART", "فن", 3)
            )
        ),
        Level(
            id = 5,
            mainWord = "JOURNEY",
            arabic = "رحلة",
            category = "Adventure",
            extractedWords = listOf(
                WordItem("JOY", "فرح", 3),
                WordItem("ONE", "واحد", 3),
                WordItem("OUR", "لنا", 3),
                WordItem("URN", "جرة", 3)
            )
        ),
        Level(
            id = 6,
            mainWord = "THUNDER",
            arabic = "رعد",
            category = "Nature",
            extractedWords = listOf(
                WordItem("THE", "أداة التعريف", 3),
                WordItem("TUN", "برميل كبير", 3),
                WordItem("UNDER", "تحت", 5)
            )
        ),
        Level(
            id = 7,
            mainWord = "PYRAMID",
            arabic = "هرم",
            category = "History",
            extractedWords = listOf(
                WordItem("AID", "مساعدة", 3),
                WordItem("RAM", "كبش", 3),
                WordItem("RID", "تخلص من", 3),
                WordItem("PAID", "مدفوع", 4)
            )
        ),
        Level(
            id = 8,
            mainWord = "DIAMETER",
            arabic = "قطر الدائرة",
            category = "Science",
            extractedWords = listOf(
                WordItem("AT", "في / عند", 2),
                WordItem("ATE", "أكل", 3),
                WordItem("DATE", "تاريخ / تمر", 4),
                WordItem("METER", "متر", 5)
            )
        ),
        Level(
            id = 9,
            mainWord = "UMBRELLA",
            arabic = "مظلة",
            category = "Objects",
            extractedWords = listOf(
                WordItem("BE", "يكون", 2),
                WordItem("ME", "أنا", 2),
                WordItem("ELL", "ذراع قياس", 3),
                WordItem("BELL", "جرس", 4)
            )
        ),
        Level(
            id = 10,
            mainWord = "FLOWER",
            arabic = "زهور",
            category = "Nature",
            extractedWords = listOf(
                WordItem("OR", "أو", 2),
                WordItem("LOW", "منخفض", 3),
                WordItem("FOR", "لـ / لأجل", 3),
                WordItem("FLOW", "تدفق", 4),
                WordItem("LOWER", "أدنى / يخفض", 5)
            )
        ),
        Level(
            id = 11,
            mainWord = "DIAMOND",
            arabic = "الماس",
            category = "Jewelry",
            extractedWords = listOf(
                WordItem("AM", "أكون", 2),
                WordItem("DAM", "سد", 3),
                WordItem("DAD", "أب", 3),
                WordItem("DIM", "خافت", 3),
                WordItem("MOD", "تعديل", 3)
            )
        ),
        Level(
            id = 12,
            mainWord = "STONE",
            arabic = "حجر",
            category = "Nature",
            extractedWords = listOf(
                WordItem("SO", "لذلك / جداً", 2),
                WordItem("ONE", "واحد", 3),
                WordItem("TOE", "إصبع القدم", 3),
                WordItem("SON", "ابن", 3),
                WordItem("TONE", "نغمة", 4)
            )
        ),
        Level(
            id = 13,
            mainWord = "SYMPHONY",
            arabic = "سيمفونية",
            category = "Music",
            extractedWords = listOf(
                WordItem("MY", "ملكي", 2),
                WordItem("SON", "ابن", 3),
                WordItem("SOY", "صويا", 3),
                WordItem("SHY", "خجول", 3),
                WordItem("PHONY", "زائف", 5)
            )
        ),
        Level(
            id = 14,
            mainWord = "HAPPINESS",
            arabic = "سعادة",
            category = "Emotions",
            extractedWords = listOf(
                WordItem("APP", "تطبيق", 3),
                WordItem("PIN", "دبوس", 3),
                WordItem("ASS", "حمار", 3),
                WordItem("APE", "قرد كبير", 3),
                WordItem("PINE", "صنوبر", 4)
            )
        ),
        Level(
            id = 15,
            mainWord = "MIRACLE",
            arabic = "معجزة",
            category = "Wonder",
            extractedWords = listOf(
                WordItem("ICE", "ثلج", 3),
                WordItem("ACE", "الآس", 3),
                WordItem("MILE", "ميل", 4),
                WordItem("MALE", "ذكر", 4),
                WordItem("RACE", "سباق", 4)
            )
        ),
        Level(
            id = 16,
            mainWord = "SEAT",
            arabic = "مقعد",
            category = "School",
            extractedWords = listOf(
                WordItem("AT", "في", 2),
                WordItem("SEA", "بحر", 3),
                WordItem("SET", "مجموعة", 3),
                WordItem("SAT", "جلس", 3),
                WordItem("EAT", "يأكل", 3)
            )
        ),
        Level(
            id = 17,
            mainWord = "HEART",
            arabic = "قلب",
            category = "Body",
            extractedWords = listOf(
                WordItem("HE", "هو", 2),
                WordItem("HAT", "قبعة", 3),
                WordItem("EAT", "يأكل", 3),
                WordItem("ART", "فن", 3),
                WordItem("HEAR", "يسمع", 4),
                WordItem("HEAT", "حرارة", 4)
            )
        ),
        Level(
            id = 18,
            mainWord = "FOREST",
            arabic = "غابة",
            category = "Nature",
            extractedWords = listOf(
                WordItem("OR", "أو", 2),
                WordItem("FOR", "لـ", 3),
                WordItem("FOE", "عدو", 3),
                WordItem("ORE", "خام المعدن", 3),
                WordItem("REST", "راحة", 4),
                WordItem("FORT", "حصن", 4)
            )
        ),
        Level(
            id = 19,
            mainWord = "SPRING",
            arabic = "ربيع / نبع",
            category = "Seasons",
            extractedWords = listOf(
                WordItem("PIN", "دبوس", 3),
                WordItem("SIN", "خطيئة", 3),
                WordItem("RING", "خاتم", 4),
                WordItem("SING", "يغني", 4),
                WordItem("PING", "إشارة صوتية", 4),
                WordItem("SPIN", "يدور", 4)
            )
        ),
        Level(
            id = 20,
            mainWord = "BREAD",
            arabic = "خبز",
            category = "Food",
            extractedWords = listOf(
                WordItem("BE", "يكون", 2),
                WordItem("BED", "سرير", 3),
                WordItem("RED", "أحمر", 3),
                WordItem("BAD", "سيء", 3),
                WordItem("READ", "يقرأ", 4),
                WordItem("BEAD", "خرزة", 4)
            )
        ),
        Level(
            id = 21,
            mainWord = "BILLION",
            arabic = "مليار",
            category = "Numbers",
            extractedWords = listOf(
                WordItem("IN", "في", 2),
                WordItem("ON", "على", 2),
                WordItem("ION", "أيون", 3),
                WordItem("BIO", "سيرة شخصية", 3),
                WordItem("BILL", "فاتورة", 4),
                WordItem("LION", "أسد", 4)
            )
        ),
        Level(
            id = 22,
            mainWord = "PROBLEM",
            arabic = "مشكلة",
            category = "General",
            extractedWords = listOf(
                WordItem("BE", "يكون", 2),
                WordItem("ROB", "يسرق", 3),
                WordItem("PRO", "محترف", 3),
                WordItem("POEM", "قصيدة", 4),
                WordItem("POLE", "قطب", 4),
                WordItem("ROLE", "دور", 4)
            )
        ),
        Level(
            id = 23,
            mainWord = "CREATIVITY",
            arabic = "إبداع",
            category = "Art",
            extractedWords = listOf(
                WordItem("AT", "في", 2),
                WordItem("IT", "هو", 2),
                WordItem("CAT", "قطة", 3),
                WordItem("EAT", "يأكل", 3),
                WordItem("RAT", "فأر", 3),
                WordItem("CRY", "يبكي", 3)
            )
        ),
        Level(
            id = 24,
            mainWord = "LIBRARY",
            arabic = "مكتبة",
            category = "Education",
            extractedWords = listOf(
                WordItem("BY", "بواسطة", 2),
                WordItem("BAR", "شريط", 3),
                WordItem("LAY", "يضع", 3),
                WordItem("RAY", "شعاع", 3),
                WordItem("BAY", "خليج", 3),
                WordItem("LIBRA", "برج الميزان", 5)
            )
        ),
        Level(
            id = 25,
            mainWord = "BEAUTIFUL",
            arabic = "جميل",
            category = "General",
            extractedWords = listOf(
                WordItem("AT", "في", 2),
                WordItem("BAT", "مضرب", 3),
                WordItem("BUT", "لكن", 3),
                WordItem("EAT", "يأكل", 3),
                WordItem("BEAT", "يهزم", 4),
                WordItem("BEAU", "عشيق", 4)
            )
        ),
        Level(
            id = 26,
            mainWord = "PLANET",
            arabic = "كوكب",
            category = "Space",
            extractedWords = listOf(
                WordItem("ANT", "نملة", 3),
                WordItem("PAN", "مقلاة", 3),
                WordItem("PAT", "ربتة", 3),
                WordItem("LAN", "شبكة", 3),
                WordItem("LANE", "ممر", 4),
                WordItem("PLAN", "خطة", 4),
                WordItem("PLANE", "طائرة", 5)
            )
        ),
        Level(
            id = 27,
            mainWord = "CARRIAGE",
            arabic = "عربة كابينة",
            category = "Transport",
            extractedWords = listOf(
                WordItem("ARE", "يكونون", 3),
                WordItem("AGE", "عمر", 3),
                WordItem("CAR", "سيارة", 3),
                WordItem("RAG", "خرقة", 3),
                WordItem("RIG", "منصة", 3),
                WordItem("CAGE", "قفص", 4),
                WordItem("RAGE", "غضب", 4)
            )
        ),
        Level(
            id = 28,
            mainWord = "FOOTBALL",
            arabic = "كرة القدم",
            category = "Sports",
            extractedWords = listOf(
                WordItem("ALL", "الكل", 3),
                WordItem("TALL", "طويل", 4),
                WordItem("FALL", "سقوط", 4),
                WordItem("BALL", "كرة", 4),
                WordItem("FOOL", "أحمق", 4),
                WordItem("FOAL", "مهر", 4),
                WordItem("FOOT", "قدم", 4)
            )
        ),
        Level(
            id = 29,
            mainWord = "ADVENTURE",
            arabic = "مغامرة",
            category = "Adventure",
            extractedWords = listOf(
                WordItem("AD", "إعلان", 2),
                WordItem("DEN", "عرين", 3),
                WordItem("VET", "طبيب بيطري", 3),
                WordItem("DUE", "مستحق", 3),
                WordItem("DENT", "انبعاج", 4),
                WordItem("VENT", "فتحة", 4),
                WordItem("VENTURE", "مغامرة", 7)
            )
        ),
        Level(
            id = 30,
            mainWord = "NOTEBOOK",
            arabic = "دفتر ملاحظات",
            category = "School",
            extractedWords = listOf(
                WordItem("NO", "لا", 2),
                WordItem("NOT", "ليس", 3),
                WordItem("TOO", "أيضاً", 3),
                WordItem("NOOK", "ركن", 4),
                WordItem("NOTE", "ملاحظة", 4),
                WordItem("TOOK", "أخذ", 4),
                WordItem("BOOK", "كتاب", 4)
            )
        ),
        Level(
            id = 31,
            mainWord = "HOLIDAY",
            arabic = "عطلة",
            category = "Time",
            extractedWords = listOf(
                WordItem("HAS", "يمتلك", 3),
                WordItem("DAY", "يوم", 3),
                WordItem("OLD", "قديم", 3),
                WordItem("LAY", "يستلقي", 3),
                WordItem("LID", "غطاء", 3),
                WordItem("HOLD", "يمسك", 4),
                WordItem("HOLY", "مقدس", 4)
            )
        ),
        Level(
            id = 32,
            mainWord = "TREASURE",
            arabic = "كنز",
            category = "Adventure",
            extractedWords = listOf(
                WordItem("TEA", "شاي", 3),
                WordItem("EAR", "أذن", 3),
                WordItem("EASE", "سهولة", 4),
                WordItem("REAR", "خلفي", 4),
                WordItem("RARE", "نادر", 4),
                WordItem("TRUE", "حقيقي", 4),
                WordItem("SURE", "بالتأكيد", 4),
                WordItem("TEASE", "يضايق", 5)
            )
        ),
        Level(
            id = 33,
            mainWord = "MARKET",
            arabic = "سوق",
            category = "Places",
            extractedWords = listOf(
                WordItem("AT", "في", 2),
                WordItem("ART", "فن", 3),
                WordItem("MET", "التقى", 3),
                WordItem("MAT", "سجادة", 3),
                WordItem("MARE", "أنثى الحصان", 4),
                WordItem("MAKE", "يصنع", 4),
                WordItem("MART", "متجر", 4),
                WordItem("MARK", "علامة", 4)
            )
        ),
        Level(
            id = 34,
            mainWord = "FREEDOM",
            arabic = "حرية",
            category = "General",
            extractedWords = listOf(
                WordItem("DO", "يفعل", 2),
                WordItem("FEE", "رسوم", 3),
                WordItem("RED", "أحمر", 3),
                WordItem("FED", "أطعم", 3),
                WordItem("FREE", "حر", 4),
                WordItem("FEED", "يطعم", 4),
                WordItem("REED", "قصب", 4),
                WordItem("FROM", "من", 4)
            )
        ),
        Level(
            id = 35,
            mainWord = "WATERFALL",
            arabic = "شلال",
            category = "Nature",
            extractedWords = listOf(
                WordItem("AT", "في", 2),
                WordItem("ALL", "الكل", 3),
                WordItem("TEA", "شاي", 3),
                WordItem("ATE", "أكل", 3),
                WordItem("FALL", "سقوط", 4),
                WordItem("TALL", "طويل", 4),
                WordItem("WALL", "جدار", 4),
                WordItem("WATER", "ماء", 5)
            )
        ),
        Level(
            id = 36,
            mainWord = "MOUNTAIN",
            arabic = "جبل",
            category = "Nature",
            extractedWords = listOf(
                WordItem("IN", "في", 2),
                WordItem("ON", "على", 2),
                WordItem("AN", "أداة", 2),
                WordItem("TAN", "أسمر", 3),
                WordItem("TIN", "قصدير", 3),
                WordItem("MAIN", "رئيسي", 4),
                WordItem("MOAN", "أنين", 4),
                WordItem("MOUNT", "يركب", 5)
            )
        ),
        Level(
            id = 37,
            mainWord = "BRIGHT",
            arabic = "ساطع",
            category = "Light",
            extractedWords = listOf(
                WordItem("BIT", "قطعة", 3),
                WordItem("BIG", "كبير", 3),
                WordItem("RIG", "معدة", 3),
                WordItem("BIGHT", "خليج", 5),
                WordItem("RIGHT", "صحيح", 5)
            )
        ),
        Level(
            id = 38,
            mainWord = "ATTENTION",
            arabic = "انتباه",
            category = "School",
            extractedWords = listOf(
                WordItem("AT", "في", 2),
                WordItem("IN", "في", 2),
                WordItem("ON", "على", 2),
                WordItem("ATE", "أكل", 3),
                WordItem("TEN", "عشرة", 3),
                WordItem("ION", "أيون", 3),
                WordItem("ANT", "نملة", 3),
                WordItem("TENT", "خيمة", 4)
            )
        ),
        Level(
            id = 39,
            mainWord = "BUTTERFLY",
            arabic = "فراشة",
            category = "Nature",
            extractedWords = listOf(
                WordItem("BUT", "لكن", 3),
                WordItem("BUY", "يشتري", 3),
                WordItem("TRY", "يحاول", 3),
                WordItem("FLY", "يطير", 3),
                WordItem("BUTT", "طرف", 4),
                WordItem("UTTER", "ينطق", 5),
                WordItem("BUTTER", "زبدة", 6),
                WordItem("UTTERLY", "تماما", 7)
            )
        ),
        Level(
            id = 40,
            mainWord = "FANTASTIC",
            arabic = "رائع",
            category = "General",
            extractedWords = listOf(
                WordItem("AS", "كما", 2),
                WordItem("AT", "في", 2),
                WordItem("ANT", "نملة", 3),
                WordItem("FAN", "مروحة", 3),
                WordItem("FAT", "سمين", 3),
                WordItem("TIC", "تشنج", 3),
                WordItem("ATTIC", "علية", 5)
            )
        ),
        Level(
            id = 41,
            mainWord = "WONDERFUL",
            arabic = "رائع",
            category = "General",
            extractedWords = listOf(
                WordItem("OR", "أو", 2),
                WordItem("OF", "من", 2),
                WordItem("ON", "على", 2),
                WordItem("ONE", "واحد", 3),
                WordItem("ODE", "قصيدة", 3),
                WordItem("WON", "فاز", 3),
                WordItem("WOE", "حزن", 3),
                WordItem("WONDER", "عجب", 6),
                WordItem("WOEFUL", "محزن", 6)
            )
        ),
        Level(
            id = 42,
            mainWord = "BUILDING",
            arabic = "مبنى",
            category = "Places",
            extractedWords = listOf(
                WordItem("IN", "في", 2),
                WordItem("DIN", "ضوضاء", 3),
                WordItem("BUD", "برعم", 3),
                WordItem("BUG", "حشرة", 3),
                WordItem("BIG", "كبير", 3),
                WordItem("BIN", "سلة", 3),
                WordItem("DIG", "يحفر", 3),
                WordItem("DING", "رنين", 4),
                WordItem("BUILD", "يبني", 5)
            )
        ),
        Level(
            id = 43,
            mainWord = "CHILDREN",
            arabic = "أطفال",
            category = "People",
            extractedWords = listOf(
                WordItem("IN", "في", 2),
                WordItem("HE", "هو", 2),
                WordItem("HID", "اختبأ", 3),
                WordItem("DEN", "عرين", 3),
                WordItem("HEN", "دجاجة", 3),
                WordItem("HIDE", "يختبئ", 4),
                WordItem("CHIN", "ذقن", 4),
                WordItem("CHIDE", "ينهر", 5),
                WordItem("CHILD", "طفل", 5)
            )
        ),
        Level(
            id = 44,
            mainWord = "SUNSHINE",
            arabic = "أشعة الشمس",
            category = "Nature",
            extractedWords = listOf(
                WordItem("IN", "في", 2),
                WordItem("US", "نحن", 2),
                WordItem("HE", "هو", 2),
                WordItem("SUN", "شمس", 3),
                WordItem("SIN", "خطيئة", 3),
                WordItem("USE", "يستخدم", 3),
                WordItem("SUE", "يقاضي", 3),
                WordItem("SHIN", "عظمة الساق", 4),
                WordItem("SHINE", "يشرق", 5)
            )
        ),
        Level(
            id = 45,
            mainWord = "FRIENDSHIP",
            arabic = "صداقة",
            category = "Social",
            extractedWords = listOf(
                WordItem("IN", "في", 2),
                WordItem("DIP", "غمس", 3),
                WordItem("HIP", "ورك", 3),
                WordItem("RED", "أحمر", 3),
                WordItem("FIN", "زعنفة", 3),
                WordItem("END", "نهاية", 3),
                WordItem("FIND", "يجد", 4),
                WordItem("SHIP", "سفينة", 4),
                WordItem("FRIES", "بطاطس", 5),
                WordItem("FRIED", "مقلي", 5)
            )
        ),
        Level(
            id = 46,
            mainWord = "HOSPITAL",
            arabic = "مستشفى",
            category = "Health",
            extractedWords = listOf(
                WordItem("HIT", "يضرب", 3),
                WordItem("HOT", "حار", 3),
                WordItem("HOP", "يقفز", 3),
                WordItem("OIL", "زيت", 3),
                WordItem("PIT", "حفرة", 3),
                WordItem("SIT", "يجلس", 3),
                WordItem("SPA", "منتجع", 3),
                WordItem("OPT", "يختار", 3),
                WordItem("HOST", "مضيف", 4),
                WordItem("SPIT", "يبصق", 4)
            )
        ),
        Level(
            id = 47,
            mainWord = "ANIMATION",
            arabic = "رسوم متحركة",
            category = "Art",
            extractedWords = listOf(
                WordItem("AT", "في", 2),
                WordItem("IN", "في", 2),
                WordItem("AN", "أداة", 2),
                WordItem("AIM", "هدف", 3),
                WordItem("ANT", "نملة", 3),
                WordItem("ION", "أيون", 3),
                WordItem("MAN", "رجل", 3),
                WordItem("MAT", "سجادة", 3),
                WordItem("MAIN", "رئيسي", 4),
                WordItem("NATION", "أمة", 6)
            )
        ),
        Level(
            id = 48,
            mainWord = "PRESENT",
            arabic = "هدية / حاضر",
            category = "General",
            extractedWords = listOf(
                WordItem("SET", "مجموعة", 3),
                WordItem("PET", "حيوان", 3),
                WordItem("PEN", "قلم", 3),
                WordItem("PEST", "آفة", 4),
                WordItem("REST", "راحة", 4),
                WordItem("RENT", "إيجار", 4),
                WordItem("SENT", "أرسل", 4),
                WordItem("RESET", "إعادة", 5),
                WordItem("PREEN", "ينظف", 5),
                WordItem("PRESET", "إعداد", 6)
            )
        ),
        Level(
            id = 49,
            mainWord = "EDUCATION",
            arabic = "تعليم",
            category = "School",
            extractedWords = listOf(
                WordItem("IN", "في", 2),
                WordItem("ON", "على", 2),
                WordItem("AT", "في", 2),
                WordItem("TO", "إلى", 2),
                WordItem("RAT", "فأر", 3),
                WordItem("CON", "خدعة", 3),
                WordItem("TON", "طن", 3),
                WordItem("CAN", "يستطيع", 3),
                WordItem("CAT", "قطة", 3),
                WordItem("DIN", "ضوضاء", 3),
                WordItem("ION", "أيون", 3),
                WordItem("TIN", "قصدير", 3),
                WordItem("DUCT", "أنبوب", 4)
            )
        ),
        Level(
            id = 50,
            mainWord = "PAINTING",
            arabic = "لوحة / رسم",
            category = "Art",
            extractedWords = listOf(
                WordItem("AN", "أداة", 2),
                WordItem("IN", "في", 2),
                WordItem("IT", "هو", 2),
                WordItem("ANT", "نملة", 3),
                WordItem("PIN", "دبوس", 3),
                WordItem("TIN", "قصدير", 3),
                WordItem("PAN", "مقلاة", 3),
                WordItem("PAT", "ربتة", 3),
                WordItem("ANTI", "مضاد", 4),
                WordItem("PAIN", "ألم", 4),
                WordItem("PANT", "يلهث", 4),
                WordItem("PINT", "باينت", 4),
                WordItem("PING", "إشارة", 4),
                WordItem("TING", "رنين", 4),
                WordItem("PAINT", "طلاء", 5)
            )
        )
    )

    val examQuestions: List<ExamQuestion> = listOf(
        ExamQuestion(
            id = 1,
            question = "Which word can be formed from 'FARM' using left-to-right selection?",
            arabic = "أي كلمة يمكن تكوينها من FARM باختيار الحروف من اليسار لليمين؟",
            options = listOf("ARM", "RAM", "MAR", "MRA"),
            correctIndex = 0
        ),
        ExamQuestion(
            id = 2,
            question = "In WORDIG, can you go backward when selecting letters?",
            arabic = "هل يمكنك الرجوع للخلف عند اختيار الحروف في ووردج؟",
            options = listOf("Yes", "No", "Sometimes", "Only on level 10"),
            correctIndex = 1
        ),
        ExamQuestion(
            id = 3,
            question = "What does 'CLOUD' mean in Arabic?",
            arabic = "ما معنى كلمة CLOUD بالعربية؟",
            options = listOf("شمس", "سحابة", "مطر", "رعد"),
            correctIndex = 1
        ),
        ExamQuestion(
            id = 4,
            question = "Which word is hidden inside 'CHAIR'?",
            arabic = "ما هي الكلمة المخفية داخل CHAIR؟",
            options = listOf("HAIR", "CHIP", "CHAT", "CHIN"),
            correctIndex = 0
        )
    )

    val faqList = listOf(
        Pair("What is WORDIG?", "WORDIG is an engaging word puzzle game where players find smaller words hidden inside a larger main word following specific left-to-right sequencing rules."),
        Pair("Does the game require internet?", "No! WORDIG is built as an offline-friendly game experience."),
        Pair("How are points calculated?", "Points depend on word length (2 letters = 5 pts, up to 7+ letters = 30 pts). Hints cost 50 points."),
        Pair("Can I go backward when selecting letters?", "No. Letters must be selected from left to right.")
    )

    val privacyPolicyText = """
WORDIG – PRIVACY POLICY
Last Updated: October 15, 2026
1. DATA COLLECTION: Offline-first game, no personal data collected.
2. LOCAL STORAGE: Game progress is stored locally on your device.
    """.trimIndent()

    val termsText = """
WORDIG – TERMS AND CONDITIONS
Last Updated: October 15, 2026
1. ACCEPTANCE: By playing WORDIG, you agree to these terms.
2. RULES: Left-to-right selection, no backward selection.
    """.trimIndent()
}
