import 'person.dart';

abstract class Book {
  final String _id;
  final String _title;
  final String _author;
  final int _year;
  Student? _borrower;

  Book({
    required this._id,
    required this._title,
    required this._author,
    required this._year,
  });

  String get id => _id;
  String get title => _title;
  String get author => _author;
  int get year => _year;
  Student? get borrower => _borrower;
  bool get isAvailable => _borrower == null;

  String get typeLabel;
  int get loanDays;

  String get status =>
      isAvailable ? 'Còn trong kho' : 'Đang mượn bởi ${_borrower!.name}';

  bool lendTo(Student student) {
    if (!isAvailable) return false;
    _borrower = student;
    return true;
  }

  void takeBack() => _borrower = null;
}

class Textbook extends Book {
  final String _subject;

  Textbook({
    required super.id,
    required super.title,
    required super.author,
    required super.year,
    required this._subject,
  });

  String get subject => _subject;

  @override
  String get typeLabel => 'Giáo trình';
  @override
  int get loanDays => 90;
}

class Novel extends Book {
  Novel({
    required super.id,
    required super.title,
    required super.author,
    required super.year,
  });

  @override
  String get typeLabel => 'Tiểu thuyết';
  @override
  int get loanDays => 14;
}

class Magazine extends Book {
  final int _issue;

  Magazine({
    required super.id,
    required super.title,
    required super.author,
    required super.year,
    required this._issue,
  });

  int get issue => _issue;

  @override
  String get typeLabel => 'Tạp chí';
  @override
  int get loanDays => 7;
}
