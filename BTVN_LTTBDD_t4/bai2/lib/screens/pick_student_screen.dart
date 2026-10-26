import 'package:flutter/material.dart';

import '../models/library.dart';

class PickStudentScreen extends StatelessWidget {
  final Library library;

  const PickStudentScreen({super.key, required this.library});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Chọn sinh viên')),
      body: ListView(
        children: [
          for (final s in library.students)
            ListTile(
              leading: CircleAvatar(child: Text(s.name[0])),
              title: Text(s.name),
              subtitle: Text('${s.id} · ${s.className}'),
              onTap: () => Navigator.pop(context, s),
            ),
        ],
      ),
    );
  }
}
