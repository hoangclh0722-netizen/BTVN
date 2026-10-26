import 'package:flutter/material.dart';

import '../models/book.dart';

class BookDetailScreen extends StatelessWidget {
  final Book book;

  const BookDetailScreen({super.key, required this.book});

  @override
  Widget build(BuildContext context) {
    final b = book;
    final rows = <(String, String)>[
      ('Mã sách', b.id),
      ('Tên sách', b.title),
      ('Tác giả', b.author),
      ('Năm xuất bản', '${b.year}'),
      ('Loại', b.typeLabel),
      if (b is Textbook) ('Môn học', b.subject),
      if (b is Magazine) ('Số', '${b.issue}'),
      ('Thời hạn mượn', '${b.loanDays} ngày'),
      ('Trạng thái', b.status),
    ];
    return Scaffold(
      appBar: AppBar(title: const Text('Thông tin sách')),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          for (final (label, value) in rows)
            ListTile(title: Text(label), subtitle: Text(value), dense: true),
        ],
      ),
    );
  }
}
