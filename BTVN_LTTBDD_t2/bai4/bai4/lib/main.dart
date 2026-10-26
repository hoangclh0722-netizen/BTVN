import 'package:flutter/material.dart';

// 1. Tạo class Student gồm name và score (nullable)
class Student {
  String name;
  double? score;

  Student({
    required this.name,
    this.score,
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
      home: GradeBoardScreen(),
    );
  }
}

class GradeBoardScreen extends StatelessWidget {
  const GradeBoardScreen({super.key});

  // Tái sử dụng hàm xếp loại từ Bài 2 (xử lý thêm trường hợp score == null)
  String getClassification(double? score) {
    if (score == null) {
      return 'Chưa có điểm';
    }
    if (score < 0 || score > 10) {
      return 'Không hợp lệ';
    } else if (score >= 8.0) {
      return 'Giỏi';
    } else if (score >= 6.5) {
      return 'Khá';
    } else if (score >= 5.0) {
      return 'Trung bình';
    } else {
      return 'Yếu';
    }
  }

  // Lấy màu chữ tương ứng cho từng xếp loại
  Color getClassificationColor(String classification) {
    if (classification == 'Giỏi') {
      return const Color(0xFF2E7D32); // Xanh lá
    } else if (classification == 'Khá') {
      return const Color(0xFF1976D2); // Xanh dương
    } else if (classification == 'Trung bình') {
      return const Color(0xFFE65100); // Cam
    } else if (classification == 'Yếu') {
      return const Color(0xFFC62828); // Đỏ
    } else {
      return Colors.grey; // Xám (Chưa có điểm)
    }
  }

  @override
  Widget build(BuildContext context) {
    // Khai báo danh sách sinh viên theo đúng đề bài
    final List<Student> students = [
      Student(name: 'Nguyễn Văn An', score: 9.0),
      Student(name: 'Trần Thị Bình', score: 7.2),
      Student(name: 'Lê Minh Chi', score: null), // Chưa có điểm
      Student(name: 'Phạm Quốc Dũng', score: 4.5),
      Student(name: 'Võ Thanh Em', score: 5.8),
    ];

    // 3. DÙNG FOR + IF ĐỂ TÍNH TOÁN CÁC THÔNG SỐ
    double totalScore = 0;
    int scoredCount = 0;      // Số người đã có điểm
    int gioCount = 0;         // Số sinh viên Giỏi
    int noScoreCount = 0;     // Số sinh viên chưa có điểm
    double maxScore = 0;      // Điểm cao nhất

    for (var s in students) {
      if (s.score == null) {
        noScoreCount++;
      } else {
        totalScore += s.score!;
        scoredCount++;

        if (s.score! >= 8.0) {
          gioCount++;
        }

        if (s.score! > maxScore) {
          maxScore = s.score!;
        }
      }
    }

    // Tính điểm trung bình (làm tròn 2 chữ số thập phân -> 6.63)
    double avgScore = scoredCount > 0 ? (totalScore / scoredCount) : 0;
    String avgScoreText = avgScore.toStringAsFixed(2);

    return Scaffold(
      backgroundColor: const Color(0xFFF3F4F6), // Nền xám nhạt
      appBar: AppBar(
        title: const Text(
          'Bảng điểm lớp',
          style: TextStyle(
            color: Colors.white,
            fontSize: 18,
            fontWeight: FontWeight.bold,
          ),
        ),
        backgroundColor: const Color(0xFF28487D), // Xanh navy UTH
        elevation: 0,
      ),
      body: Container(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // KHỐI THỐNG KÊ MÀU XANH TRÊN CÙNG
            Container(
              width: double.infinity,
              padding: const EdgeInsets.all(18.0),
              decoration: BoxDecoration(
                color: const Color(0xFF28487D),
                borderRadius: BorderRadius.circular(12.0),
              ),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  const Text(
                    'Điểm trung bình lớp',
                    style: TextStyle(
                      color: Colors.white70,
                      fontSize: 13,
                    ),
                  ),
                  const SizedBox(height: 6),
                  Text(
                    avgScoreText,
                    style: const TextStyle(
                      color: Colors.white,
                      fontSize: 34,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  const SizedBox(height: 16),

                  // Đường kẻ mảnh phân cách (dùng Container)
                  Container(
                    height: 1,
                    color: Colors.white24,
                  ),
                  const SizedBox(height: 12),

                  // 3 cột thống kê: Giỏi - Chưa có điểm - Cao nhất
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          const Text('Giỏi', style: TextStyle(color: Colors.white70, fontSize: 11)),
                          const SizedBox(height: 4),
                          Text('$gioCount', style: const TextStyle(color: Colors.white, fontSize: 16, fontWeight: FontWeight.bold)),
                        ],
                      ),
                      Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          const Text('Chưa có điểm', style: TextStyle(color: Colors.white70, fontSize: 11)),
                          const SizedBox(height: 4),
                          Text('$noScoreCount', style: const TextStyle(color: Colors.white, fontSize: 16, fontWeight: FontWeight.bold)),
                        ],
                      ),
                      Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          const Text('Cao nhất', style: TextStyle(color: Colors.white70, fontSize: 11)),
                          const SizedBox(height: 4),
                          Text('$maxScore', style: const TextStyle(color: Colors.white, fontSize: 16, fontWeight: FontWeight.bold)),
                        ],
                      ),
                    ],
                  ),
                ],
              ),
            ),

            const SizedBox(height: 22),

            // TIÊU ĐỀ PHẦN DANH SÁCH
            Text(
              'DANH SÁCH · ${students.length} SINH VIÊN',
              style: const TextStyle(
                fontSize: 11,
                fontWeight: FontWeight.bold,
                color: Colors.grey,
                letterSpacing: 0.5,
              ),
            ),
            const SizedBox(height: 10),

            // DANH SÁCH SINH VIÊN (DÙNG FOR)
            Container(
              width: double.infinity,
              padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 4.0),
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(12.0),
              ),
              child: Column(
                children: [
                  for (int i = 0; i < students.length; i++) ...[
                    Container(
                      padding: const EdgeInsets.symmetric(vertical: 12.0),
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          // Cột trái: Tên & Xếp loại
                          Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(
                                students[i].name,
                                style: const TextStyle(
                                  fontWeight: FontWeight.bold,
                                  fontSize: 14,
                                  color: Colors.black87,
                                ),
                              ),
                              const SizedBox(height: 3),
                              Text(
                                getClassification(students[i].score),
                                style: TextStyle(
                                  fontSize: 12,
                                  fontWeight: FontWeight.w500,
                                  color: getClassificationColor(
                                    getClassification(students[i].score),
                                  ),
                                ),
                              ),
                            ],
                          ),

                          // Cột phải: Điểm số (hoặc dấu — nếu null)
                          Text(
                            students[i].score != null
                                ? students[i].score.toString()
                                : '—',
                            style: TextStyle(
                              fontSize: 16,
                              fontWeight: FontWeight.bold,
                              color: students[i].score != null
                                  ? Colors.black87
                                  : Colors.grey,
                            ),
                          ),
                        ],
                      ),
                    ),

                    // Đường kẻ phân cách giữa các sinh viên
                    if (i < students.length - 1)
                      Container(
                        height: 1,
                        color: const Color(0xFFF1F1F1),
                      ),
                  ],
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}