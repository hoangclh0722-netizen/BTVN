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
      title: 'Thực hành 03',
      theme: ThemeData(
        scaffoldBackgroundColor: Colors.white,
        useMaterial3: true,
      ),
      home: const CalculatorScreen(),
    );
  }
}

class CalculatorScreen extends StatefulWidget {
  const CalculatorScreen({super.key});

  @override
  State<CalculatorScreen> createState() => _CalculatorScreenState();
}

class _CalculatorScreenState extends State<CalculatorScreen> {
  // Bộ điều khiển lấy dữ liệu từ hai ô nhập
  final TextEditingController _num1Controller = TextEditingController();
  final TextEditingController _num2Controller = TextEditingController();

  String _result = '';              // Biến lưu kết quả hiển thị
  String? _selectedOperation;       // Biến lưu phép tính đang chọn (+, -, *, /)

  @override
  void dispose() {
    _num1Controller.dispose();
    _num2Controller.dispose();
    super.dispose();
  }

  // Hàm tính toán và xử lý lỗi
  void _calculate(String op) {
    setState(() {
      _selectedOperation = op; // Đánh dấu phép tính được chọn để làm nổi bật nút

      String text1 = _num1Controller.text.trim();
      String text2 = _num2Controller.text.trim();

      // 1. Kiểm tra ô nhập bị trống
      if (text1.isEmpty || text2.isEmpty) {
        _result = 'Vui lòng nhập đủ 2 số';
        return;
      }

      // 2. Chuyển chuỗi thành số thực
      double? num1 = double.tryParse(text1);
      double? num2 = double.tryParse(text2);

      // 3. Kiểm tra dữ liệu không phải là số hợp lệ
      if (num1 == null || num2 == null) {
        _result = 'Dữ liệu không hợp lệ';
        return;
      }

      // 4. Xử lý lỗi chia cho 0
      if (op == '/' && num2 == 0) {
        _result = 'Không thể chia cho 0';
        return;
      }

      // 5. Thực hiện 4 phép toán
      double res = 0;
      switch (op) {
        case '+':
          res = num1 + num2;
          break;
        case '-':
          res = num1 - num2;
          break;
        case '*':
          res = num1 * num2;
          break;
        case '/':
          res = num1 / num2;
          break;
      }

      // 6. Định dạng kết quả (số nguyên hiển thị như 8, số thập phân làm tròn 2 chữ số)
      if (res == res.roundToDouble()) {
        _result = res.toInt().toString();
      } else {
        _result = res.toStringAsFixed(2);
      }
    });
  }

  // Widget tạo từng nút bấm phép toán (có viền nổi bật khi được chọn)
  Widget _buildOperatorButton(String label, Color color) {
    final bool isSelected = _selectedOperation == label;

    return InkWell(
      onTap: () => _calculate(label),
      borderRadius: BorderRadius.circular(10),
      child: Container(
        width: 60,
        height: 48,
        decoration: BoxDecoration(
          color: color,
          borderRadius: BorderRadius.circular(10),
          // Làm nổi bật viền đen khi nút này đang được chọn giống ảnh mẫu
          border: isSelected
              ? Border.all(color: Colors.black, width: 2.5)
              : null,
        ),
        alignment: Alignment.center,
        child: Text(
          label,
          style: const TextStyle(
            color: Colors.white,
            fontSize: 22,
            fontWeight: FontWeight.bold,
          ),
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      body: SafeArea(
        child: SingleChildScrollView(
          padding: const EdgeInsets.symmetric(horizontal: 28.0, vertical: 40.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              // 1. Tiêu đề
              const Text(
                'Thực hành 03',
                style: TextStyle(
                  fontSize: 20,
                  fontWeight: FontWeight.bold,
                  color: Colors.black87,
                ),
              ),
              const SizedBox(height: 35),

              // 2. Ô nhập số thứ nhất
              TextField(
                controller: _num1Controller,
                keyboardType: const TextInputType.numberWithOptions(decimal: true, signed: true),
                decoration: InputDecoration(
                  contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 14),
                  border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(12),
                  ),
                  enabledBorder: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(12),
                    borderSide: BorderSide(color: Colors.grey.shade400),
                  ),
                ),
              ),
              const SizedBox(height: 20),

              // 3. Hàng gồm 4 nút phép tính
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  _buildOperatorButton('+', const Color(0xFFE55D50)), // Đỏ cam
                  _buildOperatorButton('-', const Color(0xFFE5B048)), // Vàng cam
                  _buildOperatorButton('*', const Color(0xFF5D5FEF)), // Tím xanh
                  _buildOperatorButton('/', const Color(0xFF262626)), // Đen
                ],
              ),
              const SizedBox(height: 20),

              // 4. Ô nhập số thứ hai
              TextField(
                controller: _num2Controller,
                keyboardType: const TextInputType.numberWithOptions(decimal: true, signed: true),
                decoration: InputDecoration(
                  contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 14),
                  border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(12),
                  ),
                  enabledBorder: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(12),
                    borderSide: BorderSide(color: Colors.grey.shade400),
                  ),
                ),
              ),
              const SizedBox(height: 25),

              // 5. Khu vực hiển thị kết quả
              Align(
                alignment: Alignment.centerLeft,
                child: Text(
                  _result.isEmpty ? 'Kết quả:' : 'Kết quả: $_result',
                  style: const TextStyle(
                    fontSize: 16,
                    fontWeight: FontWeight.bold,
                    color: Colors.black87,
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}