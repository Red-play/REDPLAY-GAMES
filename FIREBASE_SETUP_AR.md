# إعداد تسجيل الدخول الحقيقي لـ REDPLAY

## 1) إنشاء مشروع Firebase
1. افتح https://console.firebase.google.com/ وأنشئ مشروعًا باسم REDPLAY GAMES.
2. أضف تطبيق Android بالمعرّف الدقيق: `com.redplay.games`.
3. نزّل ملف `google-services.json`.
4. ضعه داخل مجلد `app/` بجانب `build.gradle.kts` الخاص بالتطبيق. لا تضعه داخل `src/`.

## 2) تفعيل وسائل الدخول
1. من Firebase Console افتح Authentication ثم Sign-in method.
2. فعّل Email/Password.
3. فعّل Google واحفظ التغييرات.
4. في إعدادات تطبيق Android داخل Firebase أضف SHA-1 وSHA-256 لشهادة التوقيع المستخدمة. من Android Studio يمكن تشغيل `./gradlew signingReport`، أو استخراجها من إعدادات Gradle المناسبة.
5. عند استخدام Google Sign-In يجب تمرير Web client ID (OAuth client من نوع Web) إلى واجهة Google Sign-In، ثم إرسال ID token إلى `signInWithGoogleIdToken`.

## 3) قاعدة البيانات
1. افتح Firestore Database وأنشئ قاعدة بيانات.
2. من تبويب Rules الصق محتوى `FirestoreRules.txt` ثم انشر القواعد.
3. بنية البيانات:
   - `users/{uid}` لملف اللاعب.
   - `users/{uid}/favorites/{gameId}` للمفضلة.
4. القواعد تقصر القراءة والكتابة على صاحب الحساب فقط.

## 4) ملفات المشروع
- `RedplayAuthRepository.kt` يحتوي عمليات إنشاء الحساب، تسجيل الدخول بالبريد، إكمال تسجيل Google بعد الحصول على ID token، الملف الشخصي، المفضلة، إعادة تعيين كلمة المرور، والخروج.
- تمت إضافة Firebase Auth وFirestore واعتماد Google Sign-In إلى Gradle.

## تنبيه قبل البناء
لا ترفع `google-services.json` إلى مستودع عام إذا كان مشروعك يتطلب إبقاء إعداداته خاصة. ملف Firebase Android config ليس مفتاح Admin، لكن احمِ إعدادات المشروع وفعّل قواعد Firestore الصحيحة وقيود مفاتيح API.
هذا التحديث يضيف طبقة الربط الخلفية وعملياتها؛ شاشة الدخول المرئية وتدفق Google OAuth يحتاجان استكمال الربط في واجهة التطبيق باستخدام Web client ID، ثم اختبارًا فعليًا بعد إضافة إعداد Firebase. لا تعتبر تسجيل الدخول جاهزًا للإنتاج قبل هذا الاختبار.
