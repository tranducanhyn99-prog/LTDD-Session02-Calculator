# Calculator - Android Mobile Programming (Session 2 Assignment)

Dự án ứng dụng **Máy tính (Calculator)** được xây dựng hoàn thiện theo toàn bộ yêu cầu trong tài liệu **Session 2: User interface design with Views** (Slide 28 – 38).

- **Sinh viên thực hiện:** Trần Đức Anh
- **Mã số sinh viên:** 24110155
- **Package name:** `vn.edu.course.calculator`
- **Ngôn ngữ:** Java Native (minSdk 26, targetSdk 36)

---

## 1. Tính năng & Nghiệp vụ đã hoàn thiện
- [x] **Giao diện phẳng với `ConstraintLayout`:**
  - `Guideline` ngang ở mức **35%** phân chia màn hình: 35% trên cho hiển thị, 65% dưới cho bàn phím.
  - Màn hình hiển thị gồm `MaterialToolbar`, dòng biểu thức (`expressionText`) và dòng kết quả (`resultText`).
  - Hàng phím trên cùng: Nút `C` (chiếm gấp đôi chiều rộng `weight="2"`), nút xóa lùi `delete` (icon vector `ic_backspace` và `contentDescription`), nút chia `÷`.
  - Đầy đủ phím: các phím số từ `0` đến `9`, dấu chấm `.`, các phép toán `+`, `−`, `×`, `÷`, dấu bằng `=`.
- [x] **Độ ưu tiên toán tử (Operator Precedence):**
  - Nhân/chia tính trước, cộng/trừ tính sau. Ví dụ: `2 + 3 × 4 = 14`.
- [x] **Bắt lỗi không crash ứng dụng (Error Handling):**
  - Xử lý chia cho 0 (`÷ 0`): Hiển thị thông báo lỗi thân thiện *"Cannot divide by zero"* / *"Không thể chia cho 0"*.
  - Biểu thức không hợp lệ: Hiển thị *"Invalid expression"* / *"Biểu thức không hợp lệ"*, không bao giờ crash app.
- [x] **Khả năng tiếp cận & Trải nghiệm (Accessibility & UX):**
  - Kích thước chạm tối thiểu mỗi nút đạt chuẩn $\ge 48\text{dp}$.
  - Toàn bộ các nút icon và toán tử đều có thuộc tính `android:contentDescription`.
  - Nhấn giữ nút Delete để xóa sạch toàn bộ biểu thức (`onClear`).
- [x] **Kiến trúc sạch (Clean Code Checklist):**
  - Bật **View Binding** (`ActivityMainBinding`), tuyệt đối **không dùng `findViewById`**.
  - Gom toàn bộ logic cập nhật giao diện vào một hàm duy nhất `render()`.
  - Tách style riêng trong `res/values/styles.xml` (`Widget.Calculator.Button`, `Button.Function`, `Button.Operator`, `Button.Number`).
- [x] **Lưu trạng thái khi xoay màn hình (Orientation Change):**
  - Cả biểu thức và kết quả được lưu vào `Bundle` qua `onSaveInstanceState()` và khôi phục trong `onCreate()`.
- [x] **Đa ngôn ngữ (i18n):**
  - Hỗ trợ tiếng Anh mặc định trong `res/values/strings.xml`.
  - Hỗ trợ tiếng Việt đầy đủ dấu trong `res/values-vi/strings.xml`.
- [x] **Tương thích Dark Theme:**
  - Cấu hình màu sắc Material 3 tự động chuyển màu tối ưu khi thiết bị bật chế độ ban đêm (`res/values-night/themes.xml`).
- [x] **Tính năng mở rộng (Stretch Task):**
  - Menu lịch sử tính toán (`History`) trên Toolbar, cho phép xem lại các phép tính trước đó và chạm để chọn lại.

---

## 2. Hướng dẫn mở và chạy trong Android Studio
1. Mở **Android Studio**.
2. Chọn **Open** $\to$ Trỏ tới thư mục:
   `c:\Users\DUCANHZZ\Downloads\LTDĐ\Calculator`
3. Chờ Gradle sync trong vài giây.
4. Chọn máy ảo (`Medium_Phone_API_37.0`) hoặc thiết bị thật.
5. Bấm **Run** (`Shift + F10`) để chạy ứng dụng.

---

## 3. Chạy Unit Test kiểm thử
Mở terminal trong thư mục `Calculator` và chạy:
```bash
./gradlew test
```
Toàn bộ unit test kiểm tra thứ tự ưu tiên toán tử (`2 + 3 × 4 = 14`), chia cho 0, số thập phân đều tự động pass.
