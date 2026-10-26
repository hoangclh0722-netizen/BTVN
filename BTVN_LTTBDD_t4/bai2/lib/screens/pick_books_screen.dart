import 'package:flutter/material.dart';

import '../models/book.dart';
import '../models/library.dart';
import '../theme.dart';

class PickBooksScreen extends StatefulWidget {
  final Library library;

  const PickBooksScreen({super.key, required this.library});

  @override
  State<PickBooksScreen> createState() => _PickBooksScreenState();
}

class _PickBooksScreenState extends State<PickBooksScreen> {
  final _selected = <Book>{};

  @override
  Widget build(BuildContext context) {
    final books = widget.library.availableBooks;
    return Scaffold(
      appBar: AppBar(title: const Text('Chọn sách để mượn')),
      body: books.isEmpty
          ? const Center(child: Text('Thư viện đã hết sách để mượn'))
          : ListView(
              children: [
                for (final b in books)
                  CheckboxListTile(
                    value: _selected.contains(b),
                    activeColor: AppColors.check,
                    controlAffinity: ListTileControlAffinity.leading,
                    title: Text(b.title),
                    subtitle: Text('${b.typeLabel} · ${b.author}'),
                    onChanged: (v) => setState(
                      () => v! ? _selected.add(b) : _selected.remove(b),
                    ),
                  ),
              ],
            ),
      bottomNavigationBar: SafeArea(
        child: Padding(
          padding: const EdgeInsets.all(16),
          child: FilledButton(
            style: FilledButton.styleFrom(
              backgroundColor: AppColors.primary,
              minimumSize: const Size.fromHeight(48),
            ),
            onPressed: _selected.isEmpty
                ? null
                : () => Navigator.pop(context, _selected.toList()),
            child: Text('Mượn ${_selected.length} cuốn'),
          ),
        ),
      ),
    );
  }
}
