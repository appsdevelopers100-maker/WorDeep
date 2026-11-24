package com.wordeep.app.data

import com.wordeep.app.model.ExamQuestion
import com.wordeep.app.model.FaqItem
import com.wordeep.app.model.GameDifficulty
import com.wordeep.app.model.Level
import com.wordeep.app.model.WordItem

object WordGameRepository {

    private fun w(word: String, arabic: String, length: Int): WordItem = WordItem(
        word = word,
        arabic = arabic,
        length = length,
        isDiscovered = false,
        revealedIndices = emptyList()
    )

    val levels: List<Level> = listOf(
        Level(1, "FARM", "مزرعة", "Agriculture", listOf(w("FAR", "بعيد", 3), w("ARM", "ذراع", 3))),
        Level(2, "CLOUD", "سحابة", "Nature", listOf(w("COD", "سمك القد", 3), w("LOUD", "صاخب", 4))),
        Level(3, "CHAIR", "كرسي", "Home", listOf(w("CAR", "سيارة", 3), w("AIR", "هواء", 3), w("HAIR", "شعر", 4))),
        Level(4, "CART", "عربة", "Transport", listOf(w("CAR", "سيارة", 3), w("CAT", "قطة", 3), w("ART", "فن", 3))),
        Level(5, "JOURNEY", "رحلة", "Adventure", listOf(w("JOY", "فرح", 3), w("ONE", "واحد", 3), w("OUR", "لنا", 3), w("URN", "جرة", 3))),
        Level(6, "THUNDER", "رعد", "Nature", listOf(w("THE", "أداة التعريف", 3), w("TUN", "برميل كبير", 3), w("UNDER", "تحت", 5))),
        Level(7, "PYRAMID", "هرم", "History", listOf(w("AID", "مساعدة", 3), w("RAM", "كبش", 3), w("RID", "تخلص من", 3), w("PAID", "مدفوع", 4))),
        Level(8, "DIAMETER", "قطر الدائرة", null, listOf(w("ATE", "أكل", 3), w("DATE", "تاريخ / تمر", 4), w("METER", "متر", 5))),
        Level(9, "UMBRELLA", "مظلة", "Objects", listOf(w("ELL", "ذراع قياس", 3), w("BELL", "جرس", 4))),
        Level(10, "FLOWER", "زهور", "Nature", listOf(w("LOW", "منخفض", 3), w("FOR", "لـ / لأجل", 3), w("FLOW", "تدفق", 4), w("LOWER", "أدنى / يخفض", 5))),
        Level(11, "DIAMOND", "الماس", "Jewelry", listOf(w("DAM", "سد", 3), w("DAD", "أب", 3), w("DIM", "خافت", 3), w("MOD", "تعديل", 3))),
        Level(12, "STONE", "حجر", "Nature", listOf(w("ONE", "واحد", 3), w("TOE", "إصبع القدم", 3), w("SON", "ابن", 3), w("TONE", "نغمة", 4))),
        Level(13, "SYMPHONY", "سيمفونية", "Music", listOf(w("SON", "ابن", 3), w("SOY", "صويا", 3), w("SHY", "خجول", 3), w("PHONY", "زائف", 5))),
        Level(14, "HAPPINESS", "سعادة", "Emotions", listOf(w("APP", "تطبيق", 3), w("PIN", "دبوس", 3), w("APE", "قرد كبير", 3), w("PINE", "صنوبر", 4))),
        Level(15, "MIRACLE", "معجزة", "Wonder", listOf(w("ICE", "ثلج", 3), w("ACE", "الآس", 3), w("MILE", "ميل", 4), w("MALE", "ذكر", 4), w("RACE", "سباق", 4))),
        Level(16, "SEAT", "مقعد", "School", listOf(w("SEA", "بحر", 3), w("SET", "مجموعة", 3), w("SAT", "جلس", 3), w("EAT", "يأكل", 3))),
        Level(17, "HEART", "قلب", "Body", listOf(w("HAT", "قبعة", 3), w("EAT", "يأكل", 3), w("ART", "فن", 3), w("HEAR", "يسمع", 4), w("HEAT", "حرارة", 4))),
        Level(18, "FOREST", "غابة", "Nature", listOf(w("FOR", "لـ", 3), w("FOE", "عدو", 3), w("ORE", "خام المعدن", 3), w("REST", "راحة", 4), w("FORT", "حصن", 4))),
        Level(19, "SPRING", "ربيع / نبع", "Seasons", listOf(w("PIN", "دبوس", 3), w("SIN", "خطيئة", 3), w("RING", "خاتم", 4), w("SING", "يغني", 4), w("PING", "إشارة صوتية", 4), w("SPIN", "يدور", 4))),
        Level(20, "BREAD", "خبز", "Food", listOf(w("BED", "سرير", 3), w("RED", "أحمر", 3), w("BAD", "سيء", 3), w("READ", "يقرأ", 4), w("BEAD", "خرزة", 4))),
        Level(21, "BILLION", "مليار", "Numbers", listOf(w("ION", "أيون", 3), w("BIO", "سيرة شخصية", 3), w("BILL", "فاتورة", 4), w("LION", "أسد", 4))),
        Level(22, "PROBLEM", "مشكلة", "General", listOf(w("ROB", "يسرق", 3), w("PRO", "محترف", 3), w("POEM", "قصيدة", 4), w("POLE", "قطب", 4), w("ROLE", "دور", 4))),
        Level(23, "CREATIVITY", "إبداع", "Art", listOf(w("CAT", "قطة", 3), w("EAT", "يأكل", 3), w("RAT", "فأر", 3), w("CRY", "يبكي", 3))),
        Level(24, "LIBRARY", "مكتبة", "Education", listOf(w("BAR", "شريط", 3), w("LAY", "يضع", 3), w("RAY", "شعاع", 3), w("BAY", "خليج", 3), w("LIBRA", "برج الميزان", 5))),
        Level(25, "BEAUTIFUL", "جميل", "General", listOf(w("BAT", "مضرب", 3), w("BUT", "لكن", 3), w("EAT", "يأكل", 3), w("BEAT", "يهزم", 4), w("BEAU", "عشيق", 4))),
        Level(26, "PLANET", "كوكب", "Space", listOf(w("ANT", "نملة", 3), w("PAN", "مقلاة", 3), w("PAT", "ربتة", 3), w("LAN", "شبكة", 3), w("LANE", "ممر", 4), w("PLAN", "خطة", 4), w("PLANE", "طائرة", 5))),
        Level(27, "CARRIAGE", "عربة كابينة", "Transport", listOf(w("ARE", "يكونون", 3), w("AGE", "عمر", 3), w("CAR", "سيارة", 3), w("RAG", "خرقة", 3), w("RIG", "منصة", 3), w("CAGE", "قفص", 4), w("RAGE", "غضب", 4))),
        Level(28, "FOOTBALL", "كرة القدم", "Sports", listOf(w("ALL", "الكل", 3), w("TALL", "طويل", 4), w("FALL", "سقوط", 4), w("BALL", "كرة", 4), w("FOOL", "أحمق", 4), w("FOAL", "مهر", 4), w("FOOT", "قدم", 4))),
        Level(29, "ADVENTURE", "مغامرة", "Adventure", listOf(w("DEN", "عرين", 3), w("VET", "طبيب بيطري", 3), w("DUE", "مستحق", 3), w("DENT", "انبعاج", 4), w("VENT", "فتحة", 4), w("VENTURE", "مغامرة", 7))),
        Level(30, "NOTEBOOK", "دفتر ملاحظات", "School", listOf(w("NOT", "ليس", 3), w("TOO", "أيضاً", 3), w("NOOK", "ركن", 4), w("NOTE", "ملاحظة", 4), w("TOOK", "أخذ", 4), w("BOOK", "كتاب", 4))),
        Level(31, "HOLIDAY", "عطلة", "Time", listOf(w("HAY", "قش", 3), w("DAY", "يوم", 3), w("OLD", "قديم", 3), w("LAY", "يستلقي", 3), w("LID", "غطاء", 3), w("HOLD", "يمسك", 4), w("HOLY", "مقدس", 4))),
        Level(32, "TREASURE", "كنز", "Adventure", listOf(w("TEA", "شاي", 3), w("EAR", "أذن", 3), w("EASE", "سهولة", 4), w("REAR", "خلفي", 4), w("RARE", "نادر", 4), w("TRUE", "حقيقي", 4), w("SURE", "بالتأكيد", 4), w("TEASE", "يضايق", 5))),
        Level(33, "MARKET", "سوق", "Places", listOf(w("ART", "فن", 3), w("MET", "التقى", 3), w("MAT", "سجادة", 3), w("MARE", "أنثى الحصان", 4), w("MAKE", "يصنع", 4), w("MART", "متجر", 4), w("MARK", "علامة", 4))),
        Level(34, "FREEDOM", "حرية", "General", listOf(w("FEE", "رسوم", 3), w("RED", "أحمر", 3), w("FED", "أطعم", 3), w("FREE", "حر", 4), w("FEED", "يطعم", 4), w("REED", "قصب", 4), w("FROM", "من", 4))),
        Level(35, "WATERFALL", "شلال", "Nature", listOf(w("ALL", "الكل", 3), w("TEA", "شاي", 3), w("ATE", "أكل", 3), w("FALL", "سقوط", 4), w("TALL", "طويل", 4), w("WALL", "جدار", 4), w("WATER", "ماء", 5))),
        Level(36, "MOUNTAIN", "جبل", "Nature", listOf(w("TAN", "أسمر", 3), w("TIN", "قصدير", 3), w("MAIN", "رئيسي", 4), w("MOAN", "أنين", 4), w("MOUNT", "يركب", 5))),
        Level(37, "BRIGHT", "ساطع", "Light", listOf(w("BIT", "قطعة", 3), w("BIG", "كبير", 3), w("RIG", "معدة", 3), w("BIGHT", "خليج", 5), w("RIGHT", "صحيح", 5))),
        Level(38, "ATTENTION", "انتباه", "School", listOf(w("ATE", "أكل", 3), w("TEN", "عشرة", 3), w("ION", "أيون", 3), w("ANT", "نملة", 3), w("TENT", "خيمة", 4))),
        Level(39, "BUTTERFLY", "فراشة", "Nature", listOf(w("BUT", "لكن", 3), w("BUY", "يشتري", 3), w("TRY", "يحاول", 3), w("FLY", "يطير", 3), w("BUTT", "طرف", 4), w("UTTER", "ينطق", 5), w("BUTTER", "زبدة", 6), w("UTTERLY", "تماما", 7))),
        Level(40, "FANTASTIC", "رائع", "General", listOf(w("ANT", "نملة", 3), w("FAN", "مروحة", 3), w("FAT", "سمين", 3), w("TIC", "تشنج", 3), w("ATTIC", "علية", 5))),
        Level(41, "WONDERFUL", "رائع", "General", listOf(w("ONE", "واحد", 3), w("ODE", "قصيدة", 3), w("WON", "فاز", 3), w("WOE", "حزن", 3), w("WONDER", "عجب", 6), w("WOEFUL", "محزن", 6))),
        Level(42, "BUILDING", "مبنى", "Places", listOf(w("DIN", "ضوضاء", 3), w("BUD", "برعم", 3), w("BUG", "حشرة", 3), w("BIG", "كبير", 3), w("BIN", "سلة", 3), w("DIG", "يحفر", 3), w("DING", "رنين", 4), w("BUILD", "يبني", 5))),
        Level(43, "CHILDREN", "أطفال", "People", listOf(w("HID", "اختبأ", 3), w("DEN", "عرين", 3), w("HEN", "دجاجة", 3), w("HIDE", "يختبئ", 4), w("CHIN", "ذقن", 4), w("CHIDE", "ينهر", 5), w("CHILD", "طفل", 5))),
        Level(44, "SUNSHINE", "أشعة الشمس", "Nature", listOf(w("SUN", "شمس", 3), w("SIN", "خطيئة", 3), w("USE", "يستخدم", 3), w("SUE", "يقاضي", 3), w("SHIN", "عظمة الساق", 4), w("SHINE", "يشرق", 5))),
        Level(45, "FRIENDSHIP", "صداقة", "Social", listOf(w("DIP", "غمس", 3), w("HIP", "ورك", 3), w("RED", "أحمر", 3), w("FIN", "زعنفة", 3), w("END", "نهاية", 3), w("FIND", "يجد", 4), w("SHIP", "سفينة", 4), w("FRIES", "بطاطس", 5), w("FRIED", "مقلي", 5))),
        Level(46, "HOSPITAL", "مستشفى", "Health", listOf(w("HIT", "يضرب", 3), w("HOT", "حار", 3), w("HOP", "يقفز", 3), w("OIL", "زيت", 3), w("PIT", "حفرة", 3), w("SIT", "يجلس", 3), w("SPA", "منتجع", 3), w("OPT", "يختار", 3), w("HOST", "مضيف", 4), w("SPIT", "يبصق", 4))),
        Level(47, "ANIMATION", "رسوم متحركة", "Art", listOf(w("AIM", "هدف", 3), w("ANT", "نملة", 3), w("ION", "أيون", 3), w("MAN", "رجل", 3), w("MAT", "سجادة", 3), w("MAIN", "رئيسي", 4), w("NATION", "أمة", 6))),
        Level(48, "PRESENT", "هدية / حاضر", "General", listOf(w("SET", "مجموعة", 3), w("PET", "حيوان", 3), w("PEN", "قلم", 3), w("PEST", "آفة", 4), w("REST", "راحة", 4), w("RENT", "إيجار", 4), w("SENT", "أرسل", 4), w("RESET", "إعادة", 5), w("PREEN", "ينظف", 5), w("PRESET", "إعداد", 6))),
        Level(49, "EDUCATION", "تعليم", "School", listOf(w("EAT", "يأكل", 3), w("CON", "خدعة", 3), w("TON", "طن", 3), w("CAN", "يستطيع", 3), w("CAT", "قطة", 3), w("DIN", "ضوضاء", 3), w("ION", "أيون", 3), w("TIN", "قصدير", 3), w("DUCT", "أنبوب", 4))),
        Level(50, "PAINTING", "لوحة / رسم", "Art", listOf(w("ANT", "نملة", 3), w("PIN", "دبوس", 3), w("TIN", "قصدير", 3), w("PAN", "مقلاة", 3), w("PAT", "ربتة", 3), w("ANTI", "مضاد", 4), w("PAIN", "ألم", 4), w("PANT", "يلهث", 4), w("PINT", "باينت", 4), w("PING", "إشارة", 4), w("TING", "رنين", 4), w("PAINT", "طلاء", 5)))
    )

    private val hardBonusWords: Map<Int, List<WordItem>> = mapOf(
        23 to listOf(w("CITY", "مدينة", 4), w("CAVITY", "تجويف", 6))
    )

    fun getLevelsForDifficulty(difficulty: GameDifficulty): List<Level> {
        return levels.map { level ->
            val baseWords = level.extractedWords.filter { it.length >= 3 }
            val adjustedWords = when (difficulty) {
                GameDifficulty.EASY -> {
                    val sortedSimple = baseWords.sortedBy { it.length }
                    val targetCount = sortedSimple.size.coerceAtMost(3).coerceAtLeast(2.coerceAtMost(sortedSimple.size))
                    sortedSimple.take(targetCount)
                }

                GameDifficulty.HARD -> {
                    val bonus = hardBonusWords[level.id].orEmpty()
                    val combined = baseWords.toMutableList()
                    bonus.forEach { b ->
                        if (combined.none { it.word == b.word }) {
                            combined.add(b)
                        }
                    }
                    val complexOnly = combined.filter { it.length >= 4 }
                    val pool = if (complexOnly.size >= 2) complexOnly else combined
                    pool.sortedByDescending { it.length }
                }

                GameDifficulty.MEDIUM -> {
                    baseWords.take(baseWords.size.coerceAtMost(6))
                }
            }
            level.copy(extractedWords = adjustedWords)
        }
    }

    val examsByNumber: Map<Int, List<ExamQuestion>> = mapOf(
        1 to listOf(
            ExamQuestion(101, "Which word can be formed from 'FARM' using left-to-right selection?", "أي كلمة يمكن تكوينها من FARM باختيار الحروف من اليسار لليمين؟", listOf("ARM", "RAM", "MAR", "MRA"), 0),
            ExamQuestion(102, "What does 'CLOUD' mean in Arabic?", "ما معنى كلمة CLOUD بالعربية؟", listOf("شمس", "سحابة", "مطر", "رعد"), 1),
            ExamQuestion(103, "Which 4-letter word is hidden inside 'CHAIR'?", "ما هي الكلمة المكونة من ٤ حروف والمخفية داخل CHAIR؟", listOf("CHIP", "CHAT", "HAIR", "CHIN"), 2),
            ExamQuestion(104, "Complete the missing letters for 'عربة': C _ R _", "أكمل الحروف الناقصة لكلمة عربة:", listOf("E, D", "O, N", "U, P", "A, T"), 3),
            ExamQuestion(105, "Which word can be formed left-to-right from 'JOURNEY'?", "أي كلمة يمكن تكوينها من اليسار لليمين من JOURNEY؟", listOf("RUN", "JOY", "YOU", "EAR"), 1)
        ),
        2 to listOf(
            ExamQuestion(201, "What does 'THUNDER' mean in Arabic?", "ما معنى كلمة THUNDER بالعربية؟", listOf("برق", "رعد", "سحابة", "ريح"), 1),
            ExamQuestion(202, "Which 4-letter word can be formed left-to-right from 'PYRAMID'?", "أي كلمة يمكن تكوينها من اليسار لليمين من PYRAMID؟", listOf("PAID", "DAMP", "MIND", "PLAY"), 0),
            ExamQuestion(203, "Fill in the missing letters for 'قطر الدائرة': D I A M _ T _ R", "أكمل الحروف الناقصة لكلمة قطر الدائرة:", listOf("A, E", "E, A", "E, E", "O, E"), 2),
            ExamQuestion(204, "Which English word means 'مظلة'?", "أي كلمة إنجليزية تعني مظلة؟", listOf("FLOWER", "THUNDER", "DIAMETER", "UMBRELLA"), 3),
            ExamQuestion(205, "Which 5-letter word is hidden left-to-right inside 'FLOWER'?", "ما الكلمة المكونة من ٥ حروف داخل FLOWER من اليسار لليمين؟", listOf("WORLD", "LOWER", "FLOOR", "POWER"), 1)
        ),
        3 to listOf(
            ExamQuestion(301, "What does 'DIAMOND' mean in Arabic?", "ما معنى كلمة DIAMOND بالعربية؟", listOf("ذهب", "فضة", "الماس", "حجر"), 2),
            ExamQuestion(302, "Which 4-letter word is hidden left-to-right inside 'STONE'?", "أي كلمة من ٤ حروف مخفية داخل STONE من اليسار لليمين؟", listOf("TONE", "NOTE", "NEST", "SENT"), 0),
            ExamQuestion(303, "Complete the spelling of 'سيمفونية': S Y M _ H O _ Y", "أكمل تهجئة كلمة سيمفونية:", listOf("B, M", "F, N", "P, M", "P, N"), 3),
            ExamQuestion(304, "Which English word means 'سعادة'?", "أي كلمة إنجليزية تعني سعادة؟", listOf("STONE", "HAPPINESS", "MIRACLE", "SYMPHONY"), 1),
            ExamQuestion(305, "Which 4-letter word can be formed left-to-right from 'MIRACLE'?", "أي كلمة يمكن تكوينها من اليسار لليمين من MIRACLE؟", listOf("CREAM", "CLEAR", "RACE", "CARE"), 2)
        ),
        4 to listOf(
            ExamQuestion(401, "Which word can be formed left-to-right from 'SEAT'?", "أي كلمة يمكن تكوينها من اليسار لليمين من SEAT؟", listOf("EAST", "TEA", "SEA", "ATE"), 2),
            ExamQuestion(402, "What does 'HEART' mean in Arabic?", "ما معنى كلمة HEART بالعربية؟", listOf("عين", "قلب", "يد", "عقل"), 1),
            ExamQuestion(403, "Complete the missing letters for 'غابة': F _ R _ S T", "أكمل الحروف الناقصة لكلمة غابة:", listOf("O, E", "A, E", "O, A", "U, E"), 0),
            ExamQuestion(404, "Which 4-letter word is hidden left-to-right inside 'SPRING'?", "أي كلمة من ٤ حروف مخفية داخل SPRING من اليسار لليمين؟", listOf("GRIN", "KING", "SONG", "RING"), 3),
            ExamQuestion(405, "Which English word means 'خبز'?", "أي كلمة إنجليزية تعني خبز؟", listOf("BREAD", "FOREST", "SPRING", "SEAT"), 0)
        ),
        5 to listOf(
            ExamQuestion(501, "What does 'BILLION' mean in Arabic?", "ما معنى كلمة BILLION بالعربية؟", listOf("مليون", "ألف", "مليار", "مئة"), 2),
            ExamQuestion(502, "Which 4-letter word can be formed left-to-right from 'PROBLEM'?", "أي كلمة من ٤ حروف مخفية داخل PROBLEM من اليسار لليمين؟", listOf("MORE", "POEM", "ROPE", "BORE"), 1),
            ExamQuestion(503, "Fill in the missing letters for 'مكتبة': L I B _ A _ Y", "أكمل الحروف الناقصة لكلمة مكتبة:", listOf("R, R", "L, R", "R, N", "B, R"), 0),
            ExamQuestion(504, "Which English word means 'إبداع'?", "أي كلمة إنجليزية تعني إبداع؟", listOf("PROBLEM", "LIBRARY", "BEAUTIFUL", "CREATIVITY"), 3),
            ExamQuestion(505, "Which 4-letter word can be formed left-to-right from 'BEAUTIFUL'?", "أي كلمة يمكن تكوينها من اليسار لليمين من BEAUTIFUL؟", listOf("TUBE", "BEAT", "LATE", "TAIL"), 1)
        ),
        6 to listOf(
            ExamQuestion(601, "What does 'PLANET' mean in Arabic?", "ما معنى كلمة PLANET بالعربية؟", listOf("نجم", "كوكب", "قمر", "شمس"), 1),
            ExamQuestion(602, "Which 4-letter word can be formed left-to-right from 'CARRIAGE'?", "أي كلمة يمكن تكوينها من اليسار لليمين من CARRIAGE؟", listOf("GEAR", "DEAR", "RAGE", "BEAR"), 2),
            ExamQuestion(603, "Complete the missing letters for 'كرة القدم': F O O T _ A _ L", "أكمل الحروف الناقصة لكلمة كرة القدم:", listOf("B, L", "P, L", "B, R", "D, L"), 0),
            ExamQuestion(604, "Which English word means 'مغامرة'?", "أي كلمة إنجليزية تعني مغامرة؟", listOf("NOTEBOOK", "PLANET", "CARRIAGE", "ADVENTURE"), 3),
            ExamQuestion(605, "Which 4-letter word is hidden left-to-right inside 'NOTEBOOK'?", "أي كلمة من ٤ حروف مخفية داخل NOTEBOOK من اليسار لليمين؟", listOf("BONE", "BOOK", "BOAT", "BOOT"), 1)
        ),
        7 to listOf(
            ExamQuestion(701, "What does 'HOLIDAY' mean in Arabic?", "ما معنى كلمة HOLIDAY بالعربية؟", listOf("عمل", "مدرسة", "عطلة", "سفر"), 2),
            ExamQuestion(702, "Which 4-letter word is hidden left-to-right inside 'TREASURE'?", "أي كلمة من ٤ حروف مخفية داخل TREASURE من اليسار لليمين؟", listOf("TRUE", "STAR", "RATE", "SEAT"), 0),
            ExamQuestion(703, "Complete the spelling of 'سوق': M _ R K _ T", "أكمل تهجئة كلمة سوق:", listOf("E, A", "O, E", "A, I", "A, E"), 3),
            ExamQuestion(704, "Which English word means 'حرية'?", "أي كلمة إنجليزية تعني حرية؟", listOf("MARKET", "FREEDOM", "TREASURE", "WATERFALL"), 1),
            ExamQuestion(705, "Which 5-letter word can be formed left-to-right from 'WATERFALL'?", "أي كلمة من ٥ حروف مخفية داخل WATERFALL من اليسار لليمين؟", listOf("LATER", "AFTER", "WATER", "FLAME"), 2)
        ),
        8 to listOf(
            ExamQuestion(801, "What does 'MOUNTAIN' mean in Arabic?", "ما معنى كلمة MOUNTAIN بالعربية؟", listOf("بحر", "جبل", "نهر", "وادي"), 1),
            ExamQuestion(802, "Which 5-letter word is hidden left-to-right inside 'BRIGHT'?", "أي كلمة من ٥ حروف مخفية داخل BRIGHT من اليسار لليمين؟", listOf("RIGHT", "LIGHT", "NIGHT", "TIGHT"), 0),
            ExamQuestion(803, "Fill in the missing letters for 'انتباه': A T T E _ T _ O N", "أكمل الحروف الناقصة لكلمة انتباه:", listOf("M, I", "N, E", "N, I", "R, I"), 2),
            ExamQuestion(804, "Which English word means 'فراشة'?", "أي كلمة إنجليزية تعني فراشة؟", listOf("FANTASTIC", "BRIGHT", "MOUNTAIN", "BUTTERFLY"), 3),
            ExamQuestion(805, "Which 5-letter word can be formed left-to-right from 'FANTASTIC'?", "أي كلمة من ٥ حروف مخفية داخل FANTASTIC من اليسار لليمين؟", listOf("STACK", "ATTIC", "TASTE", "SCENT"), 1)
        ),
        9 to listOf(
            ExamQuestion(901, "What does 'WONDERFUL' mean in Arabic?", "ما معنى كلمة WONDERFUL بالعربية؟", listOf("رائع", "صعب", "سريع", "قديم"), 0),
            ExamQuestion(902, "Which 5-letter word is hidden left-to-right inside 'BUILDING'?", "أي كلمة من ٥ حروف مخفية داخل BUILDING من اليسار لليمين؟", listOf("BLIND", "GUILD", "BUILD", "BINGO"), 2),
            ExamQuestion(903, "Complete the spelling of 'أطفال': C H _ L D _ E N", "أكمل تهجئة كلمة أطفال:", listOf("E, R", "I, R", "I, L", "A, R"), 1),
            ExamQuestion(904, "Which English word means 'أشعة الشمس'?", "أي كلمة إنجليزية تعني أشعة الشمس؟", listOf("BUILDING", "WONDERFUL", "FRIENDSHIP", "SUNSHINE"), 3),
            ExamQuestion(905, "Which 4-letter word can be formed left-to-right from 'FRIENDSHIP'?", "أي كلمة من ٤ حروف مخفية داخل FRIENDSHIP من اليسار لليمين؟", listOf("SHIP", "PINE", "HIDE", "RIDE"), 0)
        ),
        10 to listOf(
            ExamQuestion(1001, "What does 'HOSPITAL' mean in Arabic?", "ما معنى كلمة HOSPITAL بالعربية؟", listOf("مدرسة", "مكتبة", "مستشفى", "سوق"), 2),
            ExamQuestion(1002, "Which 6-letter word is hidden left-to-right inside 'ANIMATION'?", "أي كلمة من ٦ حروف مخفية داخل ANIMATION من اليسار لليمين؟", listOf("MOTION", "NATION", "ACTION", "STATION"), 1),
            ExamQuestion(1003, "Fill in the missing letters for 'هدية / حاضر': P R _ S _ N T", "أكمل الحروف الناقصة لكلمة هدية / حاضر:", listOf("E, E", "A, E", "E, A", "I, E"), 0),
            ExamQuestion(1004, "Which English word means 'تعليم'?", "أي كلمة إنجليزية تعني تعليم؟", listOf("PAINTING", "ANIMATION", "HOSPITAL", "EDUCATION"), 3),
            ExamQuestion(1005, "Which 5-letter word can be formed left-to-right from 'PAINTING'?", "أي كلمة من ٥ حروف مخفية داخل PAINTING من اليسار لليمين؟", listOf("GIANT", "PLANT", "PAINT", "TRAIN"), 2)
        )
    )

    fun getQuestionsForExam(examNumber: Int): List<ExamQuestion> {
        return examsByNumber[examNumber] ?: examsByNumber.getValue(1)
    }

    val faqList: List<FaqItem> = listOf(
        FaqItem(
            question = "What is WORDIG?",
            answer = "WORDIG is an engaging word puzzle game where players find smaller words (at least 3 letters) hidden inside a larger main word following specific left-to-right sequencing rules."
        ),
        FaqItem(
            question = "Does the game require internet?",
            answer = "No! WORDIG is built as an offline-friendly game experience."
        ),
        FaqItem(
            question = "How are points calculated?",
            answer = "Points depend on word length (3 letters = 8 pts, 4 letters = 12 pts, 5 letters = 15 pts, 6+ letters = 20 pts). Extra valid English words (3+ letters) award +5 pts each. Hints cost 50 points and reveal 1 letter. Each Exam Checkpoint is worth up to 10 points and can be retaken anytime to improve your best score."
        ),
        FaqItem(
            question = "Can I go backward when selecting letters?",
            answer = "No. Letters must be selected from left to right."
        )
    )

    const val privacyPolicyText: String = """WORDIG – PRIVACY POLICY
Last Updated: October 15, 2026

1. DATA COLLECTION: Offline-first game, no personal data collected.
2. LOCAL STORAGE: Game progress is stored locally on your device."""

    const val termsText: String = """WORDIG – TERMS AND CONDITIONS
Last Updated: October 15, 2026

1. ACCEPTANCE: By playing WORDIG, you agree to these terms.
2. RULES: Left-to-right selection, minimum 3-letter words, no backward selection."""

    const val howToPlayText: String = """EVERY WORD HIDES MORE.

WORDIG is a word puzzle game where one main word hides many smaller words. Your challenge is to discover them.

THE MAIN RULE:
Letters must always be selected from left to right (minimum 3 letters per word). You can skip letters, but you cannot go backward or return to an earlier letter after selecting a later one.

FIND THE HIDDEN WORDS:
Below the main word, you will see word boxes containing empty squares. The number of squares shows the number of letters in a hidden word (e.g. [ ] [ ] [ ] means a 3-letter word).

SCORE MORE POINTS & EXTRA WORDS:
Longer words are worth more points. Valid 3+ letter English words outside the target list count as Extra Words and award +5 points each. Hints cost 50 points and reveal 1 letter of a hidden word. Every 5 levels unlocks a 10-point Exam Checkpoint that you can retake anytime to improve your best score."""
}
