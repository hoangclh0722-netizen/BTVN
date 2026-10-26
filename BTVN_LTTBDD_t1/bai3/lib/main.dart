import 'package:flutter/material.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Bài tập tuần 1',
      theme: ThemeData(
        scaffoldBackgroundColor: Colors.white,
        useMaterial3: true,
      ),
      home: const ProfileScreen(),
    );
  }
}

class ProfileScreen extends StatelessWidget {
  const ProfileScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      body: SafeArea(
        child: Center(
          // ConstrainedBox giúp giao diện hiển thị chuẩn kích thước điện thoại trên Chrome (Web)
          // và tự động co giãn 100% chiều ngang trên thiết bị Android
          child: ConstrainedBox(
            constraints: const BoxConstraints(maxWidth: 480),
            child: Padding(
              padding: const EdgeInsets.symmetric(horizontal: 24.0, vertical: 16.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  // --- THANH ĐIỀU HƯỚNG TRÊN CÙNG: 2 NÚT VUÔNG BO GÓC ---
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      // ========================================================
                      // [VỊ TRÍ 1: NÚT VÀ ICON BÊN TRÁI]
                      // Nút hình vuông bo góc, viền xám nhạt, hiện đang để trống.
                      // Để thêm icon (ví dụ mũi tên quay lại), hãy thay "child: null" bằng:
                      // child: const Icon(Icons.arrow_back, size: 20, color: Colors.black87),
                      // ========================================================
                      Container(
                        width: 44,
                        height: 44,
                        decoration: BoxDecoration(
                          color: Colors.white,
                          borderRadius: BorderRadius.circular(12),
                          border: Border.all(
                            color: const Color(0xFFE0E0E0), // Viền xám nhạt
                            width: 1.5,
                          ),
                        ),
                        child: const Icon(Icons.arrow_back, size: 20, color: Colors.black87), // <-- CHÈN ICON TRÁI TẠI ĐÂY
                      ),

                      // ========================================================
                      // [VỊ TRÍ 2: NÚT VÀ ICON BÊN PHẢI]
                      // Nút hình vuông bo góc, viền xám nhạt, hiện đang để trống.
                      // Để thêm icon (ví dụ icon chỉnh sửa), hãy thay "child: null" bằng:
                      // child: const Icon(Icons.edit_outlined, size: 20, color: Colors.black87),
                      // ========================================================
                      Container(
                        width: 44,
                        height: 44,
                        decoration: BoxDecoration(
                          color: Colors.white,
                          borderRadius: BorderRadius.circular(12),
                          border: Border.all(
                            color: const Color(0xFFE0E0E0), // Viền xám nhạt
                            width: 1.5,
                          ),
                        ),
                        child: const Icon(Icons.edit_outlined, size: 20, color: Colors.black87), // <-- CHÈN ICON PHẢI TẠI ĐÂY
                      ),
                    ],
                  ),

                  // Khoảng cách từ thanh trên cùng xuống ảnh avatar (tương đồng ảnh mẫu)
                  const SizedBox(height: 90),

                  // ========================================================
                  // [VỊ TRÍ 3: VÒNG TRÒN AVATAR]
                  // Vòng tròn placeholder màu xanh nhạt.
                  // Khi muốn thay ảnh avatar:
                  // Cách 1 (Ảnh trên mạng):
                  //   backgroundImage: const NetworkImage('https://link_anh.jpg'),
                  // Cách 2 (Ảnh trong thư mục assets):
                  //   backgroundImage: const AssetImage('assets/avatar.png'),
                  // ========================================================
                  const CircleAvatar(
                  radius: 65,
                  backgroundColor: Color(0xFFD6E8FA),
                  backgroundImage: AssetImage('assets/avt.jpg'),
                ),

                  // Khoảng cách giữa avatar và Tên
                  const SizedBox(height: 24),
                  const Text(
                    'Cao Lê Huy Hoàng', // <-- THAY TÊN CỦA BẠN TẠI ĐÂY
                    style: TextStyle(
                      fontSize: 22,
                      fontWeight: FontWeight.bold,
                      color: Colors.black87,
                    ),
                    textAlign: TextAlign.center,
                  ),

                  // Khoảng cách giữa Tên và MSSV
                  const SizedBox(height: 8),

                  const Text(
                    '077206010722',
                    style: TextStyle(
                      fontSize: 15,
                      color: Colors.grey,
                      fontWeight: FontWeight.w400,
                    ),
                    textAlign: TextAlign.center,
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}
