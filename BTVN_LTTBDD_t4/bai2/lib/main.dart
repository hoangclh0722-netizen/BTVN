import 'package:flutter/material.dart';

import 'models/book.dart';
import 'models/library.dart';
import 'models/person.dart';
import 'screens/book_detail_screen.dart';
import 'screens/home_screen.dart';
import 'screens/pick_books_screen.dart';
import 'screens/pick_student_screen.dart';
import 'screens/student_detail_screen.dart';
import 'theme.dart';

void main() {
  runApp(LibraryApp(library: Library.sample()));
}

class LibraryApp extends StatelessWidget {
  final Library library;

  const LibraryApp({super.key, required this.library});

  Route<dynamic> _onGenerateRoute(RouteSettings settings) {
    final args = settings.arguments;
    final Widget page = switch (settings.name) {
      AppRoutes.pickStudent => PickStudentScreen(library: library),
      AppRoutes.pickBooks => PickBooksScreen(library: library),
      AppRoutes.bookDetail => BookDetailScreen(book: args as Book),
      AppRoutes.studentDetail => StudentDetailScreen(
        library: library,
        student: args as Student,
      ),
      _ => HomeScreen(library: library),
    };
    return MaterialPageRoute(builder: (_) => page, settings: settings);
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Quản lý Thư viện',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(colorSchemeSeed: AppColors.primary),
      initialRoute: AppRoutes.home,
      onGenerateRoute: _onGenerateRoute,
    );
  }
}
