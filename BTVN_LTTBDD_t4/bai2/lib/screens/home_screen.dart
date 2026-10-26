import 'package:flutter/material.dart';

import '../models/library.dart';
import '../theme.dart';
import 'book_list_tab.dart';
import 'manage_tab.dart';
import 'student_list_tab.dart';

class HomeScreen extends StatefulWidget {
  final Library library;

  const HomeScreen({super.key, required this.library});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  int _tab = 0;

  @override
  Widget build(BuildContext context) {
    final library = widget.library;
    return Scaffold(
      backgroundColor: Colors.white,
      body: SafeArea(
        child: IndexedStack(
          index: _tab,
          children: [
            ManageTab(library: library),
            BookListTab(library: library),
            StudentListTab(library: library),
          ],
        ),
      ),
      bottomNavigationBar: BottomNavigationBar(
        currentIndex: _tab,
        onTap: (i) => setState(() => _tab = i),
        selectedItemColor: AppColors.primary,
        unselectedItemColor: AppColors.muted,
        items: const [
          BottomNavigationBarItem(icon: Icon(Icons.home), label: 'Quản lý'),
          BottomNavigationBarItem(
            icon: Icon(Icons.library_books),
            label: 'DS Sách',
          ),
          BottomNavigationBarItem(icon: Icon(Icons.person), label: 'Sinh viên'),
        ],
      ),
    );
  }
}
