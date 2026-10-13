import 'package:flutter/material.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return const MaterialApp(
      debugShowCheckedModeBanner: false,
      home: ScoreClassificationScreen(),
    );
  }
}

class ScoreClassificationScreen extends StatelessWidget {
  const ScoreClassificationScreen({super.key});

  // 2. Viết hàm nhận vào điểm và trả về xếp loại theo if/else
  String getClassification(double score) {
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

  // 3. Màu chữ của xếp loại được chọn bằng if
  Color getTextColor(String classification) {
    if (classification == 'Giỏi') {
      return const Color(0xFF2E7D32); // Xanh lá đậm
    } else if (classification == 'Khá') {
      return const Color(0xFF1976D2); // Xanh dương
    } else if (classification == 'Trung bình') {
      return const Color(0xFFE65100); // Cam đậm
    } else if (classification == 'Yếu') {
      return const Color(0xFFC62828); // Đỏ đậm
    } else {
      return const Color(0xFF616161); // Xám
    }
  }

  // Màu nền nhãn xếp loại được chọn bằng if
  Color getBgColor(String classification) {
    if (classification == 'Giỏi') {
      return const Color(0xFFE8F5E9); // Xanh lá nhạt
    } else if (classification == 'Khá') {
      return const Color(0xFFE3F2FD); // Xanh dương nhạt
    } else if (classification == 'Trung bình') {
      return const Color(0xFFFFF3E0); // Cam nhạt
    } else if (classification == 'Yếu') {
      return const Color(0xFFFFEBEE); // Đỏ nhạt
    } else {
      return const Color(0xFFEEEEEE); // Xám nhạt
    }
  }

  // Widget hiển thị 1 dòng trong phần kiểm tra thêm
  Widget _buildCheckRow(double testScore) {
    final String classification = getClassification(testScore);
    // Định dạng số: nếu là số nguyên (ví dụ 9.0) thì hiện là 9, số lẻ thì hiện 5.5
    final String scoreText = (testScore == testScore.roundToDouble())
        ? testScore.toInt().toString()
        : testScore.toString();

    return Container(
      padding: const EdgeInsets.symmetric(vertical: 9.0),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Text(
            'Điểm $scoreText',
            style: const TextStyle(
              fontSize: 14,
              color: Colors.black87,
              fontWeight: FontWeight.w500,
            ),
          ),
          Container(
            padding: const EdgeInsets.symmetric(horizontal: 14.0, vertical: 5.0),
            decoration: BoxDecoration(
              color: getBgColor(classification),
              borderRadius: BorderRadius.circular(6.0),
            ),
            child: Text(
              classification,
              style: TextStyle(
                fontSize: 12,
                fontWeight: FontWeight.bold,
                color: getTextColor(classification),
              ),
            ),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    // 1. Khai báo biến điểm có giá trị 8.5
    const double mainScore = 8.5;
    final String mainClassification = getClassification(mainScore);

    // 4. Danh sách các điểm cần kiểm tra thêm: 9, 7, 5.5, 3 và 11
    final List<double> extraScores = [9, 7, 5.5, 3, 11];

    return Scaffold(
      backgroundColor: const Color(0xFFF3F4F6), // Nền xám nhạt
      appBar: AppBar(
        title: const Text(
          'Xếp loại học lực',
          style: TextStyle(
            color: Colors.white,
            fontSize: 18,
            fontWeight: FontWeight.bold,
          ),
        ),
        backgroundColor: const Color(0xFF28487D), // Màu xanh UTH
        elevation: 0,
      ),
      body: Container(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // KHỐI 1: HIỂN THỊ ĐIỂM TRUNG BÌNH 8.5 VÀ XẾP LOẠI
            Container(
              width: double.infinity,
              padding: const EdgeInsets.symmetric(vertical: 20.0, horizontal: 16.0),
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(12.0),
              ),
              child: Column(
                children: [
                  const Text(
                    'Điểm trung bình',
                    style: TextStyle(
                      fontSize: 12,
                      color: Colors.grey,
                    ),
                  ),
                  const SizedBox(height: 6),
                  Text(
                    '$mainScore',
                    style: const TextStyle(
                      fontSize: 34,
                      fontWeight: FontWeight.bold,
                      color: Colors.black87,
                    ),
                  ),
                  const SizedBox(height: 14),
                  Container(
                    width: double.infinity,
                    padding: const EdgeInsets.symmetric(vertical: 10.0),
                    decoration: BoxDecoration(
                      color: getBgColor(mainClassification),
                      borderRadius: BorderRadius.circular(8.0),
                    ),
                    alignment: Alignment.center,
                    child: Text(
                      mainClassification,
                      style: TextStyle(
                        fontSize: 16,
                        fontWeight: FontWeight.bold,
                        color: getTextColor(mainClassification),
                      ),
                    ),
                  ),
                ],
              ),
            ),

            const SizedBox(height: 24),

            // TIÊU ĐỀ: KIỂM TRA THÊM
            const Text(
              'KIỂM TRA THÊM',
              style: TextStyle(
                fontSize: 12,
                fontWeight: FontWeight.bold,
                color: Colors.grey,
                letterSpacing: 0.5,
              ),
            ),
            const SizedBox(height: 10),

            // KHỐI 2: DANH SÁCH CÁC ĐIỂM KIỂM TRA THÊM (9, 7, 5.5, 3, 11)
            Container(
              width: double.infinity,
              padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 6.0),
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(12.0),
              ),
              child: Column(
                children: [
                  for (int i = 0; i < extraScores.length; i++) ...[
                    _buildCheckRow(extraScores[i]),
                    // Đường kẻ phân cách giữa các dòng (chỉ dùng Container)
                    if (i < extraScores.length - 1)
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