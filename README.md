# Calculator - Android Mobile Programming (Session 2 & Session 3 Complete)

Dự án ứng dụng **Máy tính (Calculator)** hoàn thiện toàn bộ yêu cầu của **Session 2 (User interface design with Views)** và **Session 3 (Components, navigation and lists)**.

- **Sinh viên thực hiện:** Trần Đức Anh
- **Mã số sinh viên:** 24110155
- **Package name:** `vn.edu.course.calculator`
- **Ngôn ngữ:** Java Native (minSdk 26, targetSdk 36)

---

## 1. Tính năng Session 2 (Giao diện & Bộ máy tính toán)
- [x] **Giao diện phẳng với `ConstraintLayout` (`fragment_calculator.xml`):**
  - `Guideline` ngang ở mức **35%** phân chia màn hình: 35% trên cho hiển thị, 65% dưới cho bàn phím.
  - Đầy đủ 18 phím: các phím số `0` – `9`, dấu chấm `.`, phép toán `+`, `−`, `×`, `÷`, dấu bằng `=`, xóa hết `C` (`weight="2"`), xóa lùi `Delete` (icon vector `ic_backspace` + `contentDescription`).
- [x] **Độ ưu tiên toán tử (`CalculatorEvaluator.java`):**
  - Nhân/chia tính trước, cộng/trừ tính sau: `2 + 3 × 4 = 14`.
  - Xử lý chia cho 0 (`÷ 0`) và biểu thức lỗi an toàn, hiển thị thông báo lỗi thân thiện, không bao giờ crash.
- [x] **Accessibility & Dark Theme:**
  - Kích thước vùng chạm tối thiểu mỗi nút $\ge 48\text{dp} \times 48\text{dp}$.
  - Hỗ trợ song ngữ Tiếng Anh (`values/strings.xml`) và Tiếng Việt (`values-vi/strings.xml`).
  - Hỗ trợ Dark Theme tự động qua Material 3 (`values-night/themes.xml`).

---

## 2. Tính năng nâng cấp Session 3 (Navigation, RecyclerView & Intents)
- [x] **Kiến trúc Single-Activity + Multiple-Fragments & Navigation Safe Args (`nav_graph.xml`):**
  - `MainActivity` đóng vai trò Host với `MaterialToolbar`, `NavHostFragment`, và `BottomNavigationView` (2 tab: *Calculator* & *History*).
  - Truyền `entryId` (`long`) kiểu an toàn (type-safe) sang `HistoryDetailFragment` bằng plugin **Safe Args** (`HistoryFragmentDirections` & `HistoryDetailFragmentArgs`).
  - Tuân thủ nghiêm ngặt vòng đời View Binding trong Fragment (`binding = null` trong `onDestroyView()`).
- [x] **Danh sách lịch sử với `RecyclerView` + `ListAdapter` + `DiffUtil` (`HistoryFragment` & `HistoryAdapter`):**
  - Hiển thị mỗi phép tính trên một `MaterialCardView` (`item_history.xml`) gồm biểu thức, kết quả và thời gian tương đối (`DateUtils.getRelativeTimeSpanString`).
  - Tự động chia cột theo màn hình (`GridLayoutManager` với `R.integer.history_columns`: 1 cột ở màn hình dọc, 2 cột ở màn hình ngang/tablet).
  - Nút **Clear History**: Xóa lịch sử kèm **`Snackbar` có nút "Undo" (Hoàn tác)** để khôi phục lại danh sách vừa xóa.
  - Nút **Export (`ExtendedFloatingActionButton`)**: Sử dụng **Activity Result API** (`ActivityResultContracts.CreateDocument("text/plain")`) để xuất toàn bộ lịch sử ra file `.txt`.
- [x] **Màn hình Chi tiết & Giao tiếp Intents (`HistoryDetailFragment` & `AboutActivity`):**
  - **Share**: Chia sẻ phép tính qua Implicit Intent `Intent.ACTION_SEND` kèm `Intent.createChooser()`.
  - **Copy**: Sao chép kết quả vào `ClipboardManager`.
  - **Use in Calculator**: Nạp lại phép tính từ lịch sử về màn hình Máy tính.
  - **About Menu**: Mở `AboutActivity` bằng Explicit Intent kèm `EXTRA_COUNT` (tổng số phép tính trong lịch sử).
  - **Help Menu**: Mở trang trợ giúp qua Implicit Intent `Intent.ACTION_VIEW` có bắt ngoại lệ `ActivityNotFoundException`.

---

## 3. Hướng dẫn mở và chạy trong Android Studio
1. Mở **Android Studio** $\to$ **Open** $\to$ chọn thư mục `c:\Users\DUCANHZZ\Downloads\LTDĐ\Calculator`.
2. Chọn máy ảo `Medium_Phone_API_37.0` $\to$ nhấn **Run (▶️)** (`Shift + F10`).

## 4. Chạy Unit Test
```bash
./gradlew test
```
