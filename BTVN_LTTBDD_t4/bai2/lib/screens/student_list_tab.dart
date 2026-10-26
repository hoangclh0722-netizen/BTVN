import 'package:flutter/material.dart';

import '../models/library.dart';
import '../theme.dart';

class StudentListTab extends StatelessWidget {
  final Library library;

  const StudentListTab({super.key, required this.library});

  @override
  Widget build(BuildContext context) {
    return ListenableBuilder(
      listenable: library,
      builder: (context, _) => ListView(
        padding: const EdgeInsets.all(20),
        children: [
          const Text(
            'Danh sách sinh viên',
            style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
          ),
          const SizedBox(height: 12),
          for (final s in library.students)
            Card(
              child: ListTile(
                leading: CircleAvatar(child: Text(s.name[0])),
                title: Text(s.name),
                subtitle: Text(
                  '${s.id} · ${s.className} · '
                  'đang mượn ${library.booksOf(s).length} cuốn',
                ),
                trailing: const Icon(Icons.chevron_right),
                onTap: () => Navigator.pushNamed(
                  context,
                  AppRoutes.studentDetail,
                  arguments: s,
                ),
              ),
            ),
        ],
      ),
    );
  }
}
