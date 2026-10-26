import 'package:flutter/material.dart';

// 1. Tạo class Student theo đúng yêu cầu
class Student {
  String name;
  String? email; // Kiểu nullable (có thể null)
  String? phone; // Kiểu nullable (có thể null)

  Student({
    required this.name,
    this.email,
    this.phone,
  });
}

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return const MaterialApp(
      debugShowCheckedModeBanner: false,
      home: StudentListScreen(),
    );
  }
}

class StudentListScreen extends StatelessWidget {
  const StudentListScreen({super.key});

  @override
  Widget build(BuildContext context) {
    // 2. Tạo list 5 sinh viên khớp chính xác với dữ liệu trong ảnh đề bài
    final List<Student> students = [
      Student(
        name: 'Nguyễn Văn An',
        email: 'an@sv.edu.vn',
        phone: '0901 234 567',
      ),
      Student(
        name: 'Trần Thị Bình',
        email: null, // Thiếu email
        phone: '0912 888 999',
      ),
      Student(
        name: 'Lê Minh Chi',
        email: 'chi@sv.edu.vn',
        phone: null, // Thiếu SĐT
      ),
      Student(
        name: 'Phạm Quốc Dũng',
        email: null, // Thiếu cả hai
        phone: null,
      ),
      Student(
        name: 'Võ Thanh Em',
        email: 'em@sv.edu.vn',
        phone: '0933 111 222',
      ),
    ];

    // 5. Dùng vòng lặp for để đếm số sinh viên thiếu email
    int missingEmailCount = 0;
    for (var s in students) {
      if (s.email == null) {
        missingEmailCount++;
      }
    }

    return Scaffold(
      backgroundColor: const Color(0xFFF3F4F6), // Màu nền xám nhạt như ảnh mẫu
      appBar: AppBar(
        title: const Text(
          'Danh sách sinh viên',
          style: TextStyle(
            color: Colors.white,
            fontSize: 18,
            fontWeight: FontWeight.bold,
          ),
        ),
        backgroundColor: const Color(0xFF28487D), // Màu xanh tím than chuẩn UTH
        elevation: 0,
      ),
      body: Container(
        padding: const EdgeInsets.all(14.0),
        child: Column(
          children: [
            // 3. Dùng vòng lặp for để tạo các thẻ sinh viên trong danh sách
            for (var student in students) ...[
              Container(
                width: double.infinity,
                padding: const EdgeInsets.symmetric(horizontal: 14.0, vertical: 10.0),
                decoration: BoxDecoration(
                  color: Colors.white,
                  borderRadius: BorderRadius.circular(10.0),
                ),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    // Tên sinh viên (in đậm)
                    Text(
                      student.name,
                      style: const TextStyle(
                        fontWeight: FontWeight.bold,
                        fontSize: 15,
                        color: Colors.black87,
                      ),
                    ),
                    const SizedBox(height: 4),

                    // Dòng Email - 4. Dùng toán tử ?? để hiển thị "Chưa cập nhật"
                    Row(
                      children: [
                        const Text(
                          'Email: ',
                          style: TextStyle(color: Colors.grey, fontSize: 13),
                        ),
                        Text(
                          student.email ?? 'Chưa cập nhật',
                          style: TextStyle(
                            fontSize: 13,
                            color: student.email == null
                                ? const Color(0xFFE55D50) // Màu đỏ khi null
                                : Colors.grey.shade700,
                            fontStyle: student.email == null
                                ? FontStyle.italic
                                : FontStyle.normal,
                          ),
                        ),
                      ],
                    ),
                    const SizedBox(height: 2),

                    // Dòng SĐT - 4. Dùng toán tử ?? để hiển thị "Chưa cập nhật"
                    Row(
                      children: [
                        const Text(
                          'SĐT: ',
                          style: TextStyle(color: Colors.grey, fontSize: 13),
                        ),
                        Text(
                          student.phone ?? 'Chưa cập nhật',
                          style: TextStyle(
                            fontSize: 13,
                            color: student.phone == null
                                ? const Color(0xFFE55D50) // Màu đỏ khi null
                                : Colors.grey.shade700,
                            fontStyle: student.phone == null
                                ? FontStyle.italic
                                : FontStyle.normal,
                          ),
                        ),
                      ],
                    ),
                  ],
                ),
              ),
              const SizedBox(height: 10), // Khoảng cách giữa các thẻ
            ],

            const SizedBox(height: 6),

            // Thanh thống kê tổng kết ở cuối danh sách
            Container(
              padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 12.0),
              decoration: BoxDecoration(
                color: const Color(0xFFE8EEF5), // Nền xanh nhạt
                borderRadius: BorderRadius.circular(8.0),
              ),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  // Tổng số sinh viên
                  Text(
                    'Tổng: ${students.length} sinh viên',
                    style: const TextStyle(
                      color: Color(0xFF28487D),
                      fontWeight: FontWeight.bold,
                      fontSize: 13,
                    ),
                  ),

                  // Số sinh viên thiếu email
                  Row(
                    children: [
                      const Text(
                        'Thiếu email: ',
                        style: TextStyle(
                          fontSize: 13,
                          color: Colors.black87,
                        ),
                      ),
                      Text(
                        '$missingEmailCount',
                        style: const TextStyle(
                          color: Color(0xFFE55D50),
                          fontWeight: FontWeight.bold,
                          fontSize: 13,
                        ),
                      ),
                    ],
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}