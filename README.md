# HayiDev++

<div align="center">

![Android](https://img.shields.io/badge/Android-26%2B-brightgreen?style=flat&logo=android)
![Kotlin](https://img.shields.io/badge/Kotlin-2.0-blue?style=flat&logo=kotlin)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-BOM%202024-purple?style=flat)
![Firebase](https://img.shields.io/badge/Firebase-Latest-orange?style=flat&logo=firebase)
![License](https://img.shields.io/badge/License-MIT-blue?style=flat)

**Sosyal tanışma, sohbet ve canlı yayın uygulaması**

[Özellikler](#özellikler) • [Ekran Görüntüleri](#ekran-görüntüleri) • [Kurulum](#kurulum) • [Yapı](#yapı) • [Teknolojiler](#teknolojiler)

</div>

---

## 📱 Özellikler

### 🔍 Keşfet
- Tinder benzeri swipe ile yeni insanlarla tanışın
- Beğen, geç ve süper beğen
- Günlük beğeni limiti
- Premium ile sınırsız beğeni

### 💬 Sohbet
- Eşleştiğiniz kişilerle anlık mesajlaşma
- Metin, sesli mesaj ve fotoğraf paylaşımı
- Okundu bilgisi
- Sohbet sabitleme ve silme

### 📺 Canlı Yayın
- Sesli canlı yayınlar açın
- Oda oluşturma (herkese açık, takipçiler, özel)
- İzleyici ve beğeni sayıları
- Misafir davet etme

### 📹 Video Görüşme
- Eşleştiğiniz kişilerle HD video görüşmesi
- Mikrofon ve kamera kontrolü
- Hoparlör modu

### 🎁 Hediye Sistemi
- Sanal hediyeler gönderin (kalp, gül, elmas, lüks arab vb.)
- Coin ve elmas para birimleri
- Hediyeleri mağazadan satın alın
- Premium hediyeler

### 👤 Profil
- Fotoğraf ve video ekleme
- İlgi alanlarını belirtme
- Coin ve premium bakiyesi
- Profil istatistikleri (beğeni, eşleşme)

### ⚙️ Ayarlar
- Dil ve tema seçimi
- Bildirim tercihleri
- Gizlilik ayarları
- Hesap güvenliği

### 🌍 Bölge Seçimi
- 7 kıta, 50+ ülke
- Ülke bazlı içerik
- Konum gizleme seçeneği

### 🔒 Güvenlik
- Kullanıcı raporlama
- Engelleme
- Doğrulanmış hesap rozeti

---

## 🛠️ Kurulum

### Gereksinimler
- Android Studio Hedgehog veya üzeri
- JDK 17
- Firebase hesabı
- Android SDK 35

### Adımlar

1. **Repository'yi klonlayın**
```bash
git clone https://github.com/username/HayiDev-.git
cd HayiDev-
```

2. **Firebase projesi oluşturun**
   - [Firebase Console](https://console.firebase.google.com/) adresine gidin
   - Yeni proje oluşturun
   - Android uygulaması ekleyin (package name: `com.hayidev.app`)
   - `google-services.json` dosyasını `app/` klasörüne koyun

3. **Firebase Servislerini Etkinleştirin**
   - Authentication (Email/Password, Google, Facebook)
   - Cloud Firestore
   - Storage
   - Cloud Messaging
   - Crashlytics

4. **Google Sign-In Ayarları**
   - Firebase Console > Authentication > Sign-in method > Google'ı etkinleştirin
   - SHA-1 fingerprint'i ekleyin: `./gradlew signingReport`

5. **Facebook Login Ayarları**
   - [Facebook Developers](https://developers.facebook.com/) adresinde uygulama oluşturun
   - Firebase Console > Authentication > Sign-in method > Facebook'u etkinleştirin
   - App ID ve Client Token'ı girin

6. **Projeyi açın ve çalıştırın**
```bash
./gradlew assembleDebug
```

---

## 📁 Yapı

```
HayiDev++/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/hayidev/app/
│   │   │   │   ├── data/
│   │   │   │   │   ├── model/          # Veri modelleri
│   │   │   │   │   ├── repository/     # Veri erişim katmanı
│   │   │   │   │   └── service/        # Firebase servisleri
│   │   │   │   ├── di/                 # Bağımlılık enjeksiyonu (Hilt)
│   │   │   │   ├── ui/
│   │   │   │   │   ├── components/     # Yeniden kullanılabilir bileşenler
│   │   │   │   │   ├── navigation/     # Navigasyon
│   │   │   │   │   ├── screens/
│   │   │   │   │   │   ├── call/       # Çağrı bildirimleri
│   │   │   │   │   │   ├── chat/       # Sohbet ekranları
│   │   │   │   │   │   ├── discover/   # Keşif ekranı
│   │   │   │   │   │   ├── gift/       # Hediye mağazası
│   │   │   │   │   │   ├── live/       # Canlı yayın ekranları
│   │   │   │   │   │   ├── login/      # Giriş ekranı
│   │   │   │   │   │   ├── profile/    # Profil ekranları
│   │   │   │   │   │   ├── region/     # Bölge seçimi
│   │   │   │   │   │   ├── report/     # Raporlama
│   │   │   │   │   │   ├── settings/   # Ayarlar
│   │   │   │   │   │   └── video/      # Video görüşme
│   │   │   │   │   └── theme/          # Tema dosyaları
│   │   │   │   └── HayiDevApplication.kt
│   │   │   ├── res/                    # Kaynak dosyaları
│   │   │   └── AndroidManifest.xml
│   │   └── androidTest/
│   └── build.gradle.kts
├── gradle/
├── .gitignore
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

---

## 🧪 Teknolojiler

| Kategori | Teknoloji |
|----------|-----------|
| **Dil** | Kotlin 2.0 |
| **UI** | Jetpack Compose (Material 3) |
| **Mimari** | MVVM + Clean Architecture |
| **Bağımlılık Enjeksiyonu** | Hilt |
| **Backend** | Firebase |
| **Veritabanı** | Cloud Firestore |
| **Dosya Saklama** | Firebase Storage |
| **Kimlik Doğrulama** | Firebase Auth (Google, Facebook, Email) |
| **Push Bildirim** | Firebase Cloud Messaging |
| **Görüntü Yükleme** | Coil |
| **HTTP** | Retrofit + OkHttp |
| **Mesajlaşma** | RongCloud |
| **Video** | WebRTC |
| **Ses Kaydı** | MediaRecorder |
| **Animasyon** | Lottie |

---

## 📦 Bağımlılıklar

```kotlin
// Core
implementation("androidx.core:core-ktx:1.15.0")
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
implementation("androidx.activity:activity-compose:1.9.3")

// Compose
implementation(platform("androidx.compose:compose-bom:2024.11.00"))
implementation("androidx.compose.material3:material3")
implementation("androidx.compose.material:material-icons-extended")

// Firebase
implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
implementation("com.google.firebase:firebase-auth-ktx")
implementation("com.google.firebase:firebase-firestore-ktx")
implementation("com.google.firebase:firebase-storage-ktx")
implementation("com.google.firebase:firebase-messaging-ktx")

// Navigation
implementation("androidx.navigation:navigation-compose:2.8.4")
implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
```

---

## � Katkıda Bulunma

1. Forklayın
2. Branch oluşturun (`git checkout -b feature/amazing-feature`)
3. Değişikliklerinizi commit edin (`git commit -m 'Add amazing feature'`)
4. Push edin (`git push origin feature/amazing-feature`)
5. Pull Request oluşturun

---

## 📄 Lisans

Bu proje MIT Lisansı altında dağıtılıyor. Detaylar için [LICENSE](LICENSE) dosyasına bakın.

---

## 📧 İletişim

- GitHub: [@username](https://github.com/username)
- Email: your.email@example.com

---

<div align="center">

**⭐ Bu projeyi beğenmeyi unutmayın!**

</div>
