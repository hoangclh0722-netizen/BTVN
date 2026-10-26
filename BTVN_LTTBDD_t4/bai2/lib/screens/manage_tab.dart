import 'package:flutter/material.dart';

import '../models/book.dart';
import '../models/library.dart';
import '../models/person.dart';
import '../theme.dart';

class ManageTab extends StatefulWidget {
  final Library library;

  const ManageTab({super.key, required this.library});

  @override
  State<ManageTab> createState() => _ManageTabState();
}

class _ManageTabState extends State<ManageTab> {
  late Student _student = widget.library.students.first;

  Future<void> _changeStudent() async {
    final picked =
        await Navigator.pushNamed(context, AppRoutes.pickStudent) as Student?;
    if (picked != null) setState(() => _student = picked);
  }

  Future<void> _addBooks() async {
    final picked =
        await Navigator.pushNamed(context, AppRoutes.pickBooks) as List<Book>?;
    if (picked != null && picked.isNotEmpty) {
      widget.library.borrow(_student, picked);
    }
  }

  @override
  Widget build(BuildContext context) {
    final library = widget.library;
    return Padding(
      padding: const EdgeInsets.fromLTRB(20, 24, 20, 16),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Center(
            child: Text(
              'Hệ thống\nQuản lý Thư viện',
              textAlign: TextAlign.center,
              style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
            ),
          ),
          const SizedBox(height: 32),
          const _Label('Sinh viên'),
          Row(
            children: [
              Expanded(
                child: Container(
                  height: 48,
                  alignment: Alignment.centerLeft,
                  padding: const EdgeInsets.symmetric(horizontal: 14),
                  decoration: BoxDecoration(
                    border: Border.all(color: AppColors.muted),
                    borderRadius: BorderRadius.circular(8),
                  ),
                  child: Text(
                    _student.name,
                    style: const TextStyle(
                      fontSize: 16,
                      fontWeight: FontWeight.w600,
                    ),
                  ),
                ),
              ),
              const SizedBox(width: 10),
              SizedBox(
                height: 48,
                child: FilledButton(
                  style: _buttonStyle,
                  onPressed: _changeStudent,
                  child: const Text('Thay đổi'),
                ),
              ),
            ],
          ),
          const SizedBox(height: 24),
          const _Label('Danh sách sách'),
          Expanded(
            child: Container(
              width: double.infinity,
              padding: const EdgeInsets.all(12),
              decoration: BoxDecoration(
                color: AppColors.panel,
                borderRadius: BorderRadius.circular(12),
              ),
              child: ListenableBuilder(
                listenable: library,
                builder: (context, _) {
                  final books = library.booksOf(_student);
                  if (books.isEmpty) {
                    return const Center(
                      child: Text(
                        'Bạn chưa mượn quyển sách nào\nNhấn "Thêm" để bắt đầu hành trình đọc sách!',
                        textAlign: TextAlign.center,
                        style: TextStyle(color: AppColors.muted),
                      ),
                    );
                  }
                  return ListView(
                    children: [
                      for (final b in books)
                        _BorrowedTile(
                          book: b,
                          onReturn: () => library.giveBack(b),
                        ),
                    ],
                  );
                },
              ),
            ),
          ),
          const SizedBox(height: 16),
          Center(
            child: SizedBox(
              width: 180,
              height: 46,
              child: FilledButton(
                style: _buttonStyle,
                onPressed: _addBooks,
                child: const Text('Thêm', style: TextStyle(fontSize: 16)),
              ),
            ),
          ),
        ],
      ),
    );
  }
}

final _buttonStyle = FilledButton.styleFrom(
  backgroundColor: AppColors.primary,
  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8)),
);

class _Label extends StatelessWidget {
  final String text;

  const _Label(this.text);

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 10),
      child: Text(
        text,
        style: const TextStyle(fontSize: 16, fontWeight: FontWeight.w600),
      ),
    );
  }
}

class _BorrowedTile extends StatelessWidget {
  final Book book;
  final VoidCallback onReturn;

  const _BorrowedTile({required this.book, required this.onReturn});

  @override
  Widget build(BuildContext context) {
    return Card(
      elevation: 1,
      color: Colors.white,
      margin: const EdgeInsets.only(bottom: 10),
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(14)),
      child: ListTile(
        // bỏ tick = trả sách
        leading: Checkbox(
          value: true,
          activeColor: AppColors.check,
          onChanged: (_) => onReturn(),
        ),
        title: Text(book.title),
        subtitle: Text('${book.typeLabel} · hạn ${book.loanDays} ngày'),
        onTap: () =>
            Navigator.pushNamed(context, AppRoutes.bookDetail, arguments: book),
      ),
    );
  }
}
