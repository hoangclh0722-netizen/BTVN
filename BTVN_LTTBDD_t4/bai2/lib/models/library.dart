import 'package:flutter/foundation.dart';

import 'book.dart';
import 'person.dart';

class Library extends ChangeNotifier {
  final List<Book> _books;
  final List<Student> _students;

  Library({required this._books, required this._students});

  List<Book> get books => List.unmodifiable(_books);
  List<Student> get students => List.unmodifiable(_students);

  List<Book> get availableBooks => _books.where((b) => b.isAvailable).toList();

  List<Book> booksOf(Student student) =>
      _books.where((b) => b.borrower == student).toList();

  void borrow(Student student, Iterable<Book> books) {
    for (final b in books) {
      b.lendTo(student);
    }
    notifyListeners();
  }

  void giveBack(Book book) {
    book.takeBack();
    notifyListeners();
  }

  factory Library.sample() {
    final students = [
      Student(id: 'SV001', name: 'Nguyen Van A', className: 'CN23A'),
      Student(id: 'SV002', name: 'Tran Thi B', className: 'CN23B'),
      Student(id: 'SV003', name: 'Le Van C', className: 'KT23A'),
    ];
    final books = <Book>[
      Textbook(
        id: 'S01',
        title: 'Lập trình Flutter cơ bản',
        author: 'Nguyễn Văn Hùng',
        year: 2023,
        subject: 'Lập trình di động',
      ),
      Textbook(
        id: 'S02',
        title: 'Cấu trúc dữ liệu và giải thuật',
        author: 'Lê Minh Hoàng',
        year: 2020,
        subject: 'Khoa học máy tính',
      ),
      Novel(
        id: 'S03',
        title: 'Dế Mèn phiêu lưu ký',
        author: 'Tô Hoài',
        year: 1941,
      ),
      Novel(id: 'S04', title: 'Số đỏ', author: 'Vũ Trọng Phụng', year: 1936),
      Magazine(
        id: 'S05',
        title: 'Tạp chí Tin học & Đời sống',
        author: 'NXB Thông tin',
        year: 2026,
        issue: 10,
      ),
    ];
    final library = Library(books: books, students: students);
    books[0].lendTo(students[0]);
    books[2].lendTo(students[0]);
    books[3].lendTo(students[1]);
    return library;
  }
}
