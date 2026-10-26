import 'package:flutter/material.dart';

import '../models/app_info.dart';
import '../models/todo.dart';
import '../models/todo_repository.dart';
import '../theme.dart';
import '../widgets/app_logo.dart';

class HomeScreen extends StatefulWidget {
  final AppInfo info;
  final TodoRepository repository;

  const HomeScreen({super.key, required this.info, required this.repository});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  @override
  Widget build(BuildContext context) {
    final repo = widget.repository;
    final todos = repo.all;
    return Scaffold(
      backgroundColor: AppColors.soft,
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.fromLTRB(20, 16, 20, 0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              BrandHeader(info: widget.info),
              const SizedBox(height: 24),
              const Text(
                'Hôm nay',
                style: TextStyle(
                  fontSize: 24,
                  fontWeight: FontWeight.bold,
                  color: AppColors.text,
                ),
              ),
              Text(
                'Đã xong ${repo.doneCount}/${todos.length} việc',
                style: const TextStyle(color: AppColors.subText),
              ),
              const SizedBox(height: 16),
              Expanded(
                child: ListView.builder(
                  itemCount: todos.length,
                  itemBuilder: (_, i) => _TodoTile(
                    todo: todos[i],
                    onTap: () => setState(todos[i].toggle),
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class _TodoTile extends StatelessWidget {
  final Todo todo;
  final VoidCallback onTap;

  const _TodoTile({required this.todo, required this.onTap});

  @override
  Widget build(BuildContext context) {
    final category = todo.category;
    final remind = todo.remindLabel;
    return Card(
      elevation: 0,
      color: Colors.white,
      margin: const EdgeInsets.only(bottom: 10),
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
      child: ListTile(
        onTap: onTap,
        leading: Icon(
          todo.isDone ? Icons.check_circle : Icons.radio_button_unchecked,
          color: todo.isDone ? AppColors.primary : AppColors.line,
        ),
        title: Text(
          todo.title,
          style: TextStyle(
            decoration: todo.isDone ? TextDecoration.lineThrough : null,
            color: todo.isDone ? AppColors.subText : AppColors.text,
          ),
        ),
        subtitle: Padding(
          padding: const EdgeInsets.only(top: 6),
          child: Row(
            children: [
              Container(
                padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 2),
                decoration: BoxDecoration(
                  color: category.backgroundColor,
                  borderRadius: BorderRadius.circular(6),
                ),
                child: Text(
                  category.label,
                  style: TextStyle(fontSize: 11, color: category.textColor),
                ),
              ),
              if (remind != null) ...[
                const SizedBox(width: 8),
                const Icon(
                  Icons.notifications_none,
                  size: 14,
                  color: AppColors.subText,
                ),
                const SizedBox(width: 2),
                Text(remind, style: const TextStyle(fontSize: 11)),
              ],
            ],
          ),
        ),
      ),
    );
  }
}
