import 'package:flutter/material.dart';

import '../models/library.dart';
import '../models/person.dart';
import '../theme.dart';

class StudentDetailScreen extends StatelessWidget {
  final Library library;
  final Student student;

  const StudentDetailScreen({
    super.key,
    required this.library,
    required this.student,
  });

  @override
  Widget build(BuildContext context) {
    final books = library.booksOf(student);
    return Scaffold(
      appBar: AppBar(title: Text(student.name)),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          Text(student.describe(), style: const TextStyle(fontSize: 16)),
          const SizedBox(height: 20),
          Text(
            'Sách đang mượn (${books.length})',
            style: const TextStyle(fontWeight: FontWeight.w600),
          ),
          for (final b in books)
            ListTile(
              leading: const Icon(Icons.menu_book, color: AppColors.primary),
              title: Text(b.title),
              subtitle: Text('${b.typeLabel} · hạn ${b.loanDays} ngày'),
              onTap: () => Navigator.pushNamed(
                context,
                AppRoutes.bookDetail,
                arguments: b,
              ),
            ),
        ],
      ),
    );
  }
}
