import 'package:flutter/material.dart';

import '../models/library.dart';
import '../theme.dart';

class BookListTab extends StatelessWidget {
  final Library library;

  const BookListTab({super.key, required this.library});

  @override
  Widget build(BuildContext context) {
    return ListenableBuilder(
      listenable: library,
      builder: (context, _) => ListView(
        padding: const EdgeInsets.all(20),
        children: [
          const Text(
            'Danh sách sách',
            style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
          ),
          const SizedBox(height: 12),
          for (final b in library.books)
            Card(
              child: ListTile(
                leading: Icon(
                  Icons.menu_book,
                  color: b.isAvailable ? AppColors.primary : AppColors.muted,
                ),
                title: Text(b.title),
                subtitle: Text('${b.typeLabel} · ${b.status}'),
                trailing: const Icon(Icons.chevron_right),
                onTap: () => Navigator.pushNamed(
                  context,
                  AppRoutes.bookDetail,
                  arguments: b,
                ),
              ),
            ),
        ],
      ),
    );
  }
}
