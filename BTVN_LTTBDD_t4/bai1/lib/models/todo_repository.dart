import 'category.dart';
import 'todo.dart';

class TodoRepository {
  final List<Todo> _todos = [
    Todo(
      title: 'Làm bài tập Navigation + OOP',
      category: const StudyCategory(),
      done: true,
    ),
    Todo(
      title: 'Ôn bài Toán',
      category: const StudyCategory(),
      remindAt: DateTime(2026, 10, 26, 19, 0),
    ),
    Todo(
      title: 'Đọc chương 4 Lập trình di động',
      category: const StudyCategory(),
    ),
    Todo(title: 'Dọn phòng', category: const LifeCategory()),
    Todo(
      title: 'Đi chợ mua đồ ăn',
      category: const LifeCategory(),
      remindAt: DateTime(2026, 10, 26, 17, 30),
    ),
  ];

  List<Todo> get all => List.unmodifiable(_todos);

  int get doneCount => _todos.where((t) => t.isDone).length;
}
