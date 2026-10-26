import 'category.dart';

class Todo {
  final String _title;
  final Category _category;
  final DateTime? _remindAt;
  bool _done;

  Todo({
    required this._title,
    required this._category,
    this._remindAt,
    this._done = false,
  });

  String get title => _title;
  Category get category => _category;
  bool get isDone => _done;

  String? get remindLabel {
    final t = _remindAt;
    if (t == null) return null;
    final hh = t.hour.toString().padLeft(2, '0');
    final mm = t.minute.toString().padLeft(2, '0');
    return 'Nhắc lúc $hh:$mm';
  }

  void toggle() => _done = !_done;
}
