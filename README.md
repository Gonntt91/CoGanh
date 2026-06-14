# COGANHQUANGNAM (Cờ Gánh Quảng Nam)

Trò chơi **Cờ Gánh Quảng Nam** được phát triển trên nền tảng Java sử dụng framework **libGDX**, hỗ trợ đa nền tảng bao gồm Desktop (Windows, macOS, Linux) và Mobile (Android, iOS).

---

## 📋 Yêu cầu hệ thống (Prerequisites)

Trước khi bắt đầu, hãy đảm bảo máy tính của bạn đã cài đặt các công cụ sau:

1. **Java Development Kit (JDK 17+)**: Dự án sử dụng Gradle 8.5 và Android Gradle Plugin (AGP) 8.2.2, yêu cầu JDK tối thiểu phiên bản 17.
   - Kiểm tra phiên bản Java bằng lệnh: `java -version`
2. **Android SDK**: Cần thiết để phát triển và build ứng dụng Android.
   - Hãy chắc chắn đường dẫn Android SDK đã được cấu hình chính xác trong file [local.properties](file:///Users/gonton/Tech/Game/CoGanh/local.properties).
3. **IDE đề xuất & Công cụ hỗ trợ**:
   - **Android Studio** hoặc **IntelliJ IDEA** (đã cài đặt plugin Gradle).
   - Khuyên dùng trợ lý AI **Antigravity** (tải về tại [antigravity.google](https://antigravity.google)) để hỗ trợ quá trình lập trình, tối ưu hóa code và sửa lỗi dự án tự động.

---

## 🛠️ Cài đặt dự án (Setup)

1. Tải mã nguồn về máy:
   ```bash
   git clone <repository-url>
   cd CoGanh
   ```
2. Mở IDE (Android Studio hoặc IntelliJ IDEA).
3. Chọn **Open** và trỏ tới thư mục chứa dự án. IDE sẽ tự động đồng bộ hóa cấu hình Gradle.

> [!NOTE]
> **Về framework libGDX:** Bạn **không cần cài đặt thủ công** libGDX vào hệ điều hành. Hệ thống build tự động của Gradle sẽ tự động tải xuống tất cả thư viện của libGDX và các dependency cần thiết khác từ Maven Central khi bạn mở dự án lần đầu tiên hoặc chạy các lệnh build/run.

---

## 💻 Hướng dẫn cho Desktop (Máy tính)

Mô-đun desktop sử dụng thư viện **LWJGL3** để chạy game trực tiếp trên hệ điều hành máy tính.

### 1. Chạy game trực tiếp
Chạy lệnh sau để khởi động game trên máy tính của bạn:
```bash
./gradlew desktop:run
```

### 2. Đóng gói game (Build JAR)
Đóng gói trò chơi thành một file JAR thực thi độc lập (đã bao gồm tất cả assets và thư viện đi kèm):
```bash
./gradlew desktop:dist
```
* **Kết quả:** File JAR sẽ được tạo ra tại thư mục: `desktop/build/libs/desktop-1.0.jar`.
* **Cách chạy file JAR:**
  ```bash
  java -jar desktop/build/libs/desktop-1.0.jar
  ```

---

## 📱 Hướng dẫn cho Mobile (Android & iOS)

### A. Hệ điều hành Android

Mô-đun android được cấu hình mặc định trong dự án để build file APK hoặc App Bundle.

#### 1. Chạy trên thiết bị thật hoặc Emulator
Kết nối thiết bị Android của bạn (đã bật *Chế độ nhà phát triển* và *USB Debugging*) hoặc mở máy ảo Android, sau đó chạy lệnh:
```bash
./gradlew android:installDebug
```

#### 2. Build file APK Debug
Dùng để cài đặt thử nghiệm nhanh:
```bash
./gradlew android:assembleDebug
```
* **Kết quả:** File APK sẽ nằm tại `android/build/outputs/apk/debug/android-debug.apk`.

#### 3. Build file APK Release / App Bundle
Dùng để đóng gói bản phát hành hoặc tải lên Google Play Store:
* **Build APK Release:**
  ```bash
  ./gradlew android:assembleRelease
  ```
  * *Kết quả:* `android/build/outputs/apk/release/android-release-unsigned.apk` (cần được ký bằng Keystore trước khi cài đặt).
* **Build Android App Bundle (AAB):**
  ```bash
  ./gradlew android:bundleRelease
  ```
  * *Kết quả:* `android/build/outputs/bundle/release/android-release.aab`.

---

### B. Hệ điều hành iOS (RoboVM)

Mô-đun iOS sử dụng framework **RoboVM** để biên dịch mã nguồn Java trực tiếp sang mã máy của iOS.

> [!NOTE]
> Để phát triển và build ứng dụng iOS, bạn bắt buộc phải sử dụng hệ điều hành **macOS** và đã cài đặt **Xcode**.

#### 1. Kích hoạt mô-đun iOS
Mặc định mô-đun `:ios` chưa được khai báo trong cấu hình chạy chính. Để kích hoạt, bạn mở file [settings.gradle](file:///Users/gonton/Tech/Game/CoGanh/settings.gradle) và chỉnh sửa thành:
```groovy
include 'desktop', 'android', 'core', 'ios'
```

#### 2. Chạy trên iOS Simulator
```bash
# Chạy trên máy ảo iPhone
./gradlew ios:launchIPhoneSimulator

# Chạy trên máy ảo iPad
./gradlew ios:launchIPadSimulator
```

#### 3. Build file cài đặt IPA
Tạo bản cài đặt thử nghiệm hoặc đưa lên App Store:
```bash
./gradlew ios:createIPA
```
* **Kết quả:** File `.ipa` sẽ được xuất ra trong thư mục `ios/build/robovm/`.

---

## ⚡ Các lệnh Gradle hữu ích khác

* **Dọn dẹp các bản build cũ:**
  ```bash
  ./gradlew clean
  ```
* **Làm mới dependency cache (nếu gặp lỗi tải thư viện):**
  ```bash
  ./gradlew --refresh-dependencies
  ```
