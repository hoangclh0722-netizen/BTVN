import 'package:flutter/material.dart';

import '../theme.dart';
import 'category.dart';

abstract class Illustration {
  const Illustration();

  Widget buildContent();

  Widget build() {
    return Container(
      width: 180,
      height: 210,
      padding: const EdgeInsets.all(12),
      decoration: BoxDecoration(
        color: AppColors.soft,
        borderRadius: BorderRadius.circular(20),
      ),
      child: Center(child: buildContent()),
    );
  }

  Widget card(Widget child) {
    return Container(
      width: double.infinity,
      margin: const EdgeInsets.symmetric(vertical: 4),
      padding: const EdgeInsets.all(12),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(10),
      ),
      child: child,
    );
  }

  Widget line(double width) {
    return Container(
      width: width,
      height: 5,
      decoration: BoxDecoration(
        color: AppColors.line,
        borderRadius: BorderRadius.circular(3),
      ),
    );
  }
}

class ChecklistIllustration extends Illustration {
  final List<bool> checked;

  const ChecklistIllustration(this.checked);

  @override
  Widget buildContent() {
    return Column(
      mainAxisSize: MainAxisSize.min,
      children: [
        for (final c in checked)
          card(
            Row(
              children: [
                Icon(
                  c ? Icons.check_circle : Icons.radio_button_unchecked,
                  size: 16,
                  color: c ? AppColors.primary : AppColors.line,
                ),
                const SizedBox(width: 10),
                Expanded(child: line(double.infinity)),
              ],
            ),
          ),
      ],
    );
  }
}

class CategoryIllustration extends Illustration {
  final List<Category> categories;

  const CategoryIllustration(this.categories);

  @override
  Widget buildContent() {
    return Column(
      mainAxisSize: MainAxisSize.min,
      children: [
        for (final c in categories)
          card(
            Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Container(
                  padding: const EdgeInsets.symmetric(
                    horizontal: 6,
                    vertical: 2,
                  ),
                  decoration: BoxDecoration(
                    color: c.backgroundColor,
                    borderRadius: BorderRadius.circular(4),
                  ),
                  child: Text(
                    c.label,
                    style: TextStyle(fontSize: 9, color: c.textColor),
                  ),
                ),
                const SizedBox(height: 10),
                line(110),
              ],
            ),
          ),
      ],
    );
  }
}

class ReminderIllustration extends Illustration {
  final String message;

  const ReminderIllustration(this.message);

  @override
  Widget buildContent() {
    return Column(
      mainAxisSize: MainAxisSize.min,
      children: [
        Container(
          width: 52,
          height: 52,
          decoration: const BoxDecoration(
            color: Colors.white,
            shape: BoxShape.circle,
          ),
          child: const Icon(
            Icons.notifications_none,
            color: AppColors.primary,
            size: 26,
          ),
        ),
        const SizedBox(height: 24),
        Container(
          padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 6),
          decoration: BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.circular(8),
          ),
          child: Text(message, style: const TextStyle(fontSize: 10)),
        ),
      ],
    );
  }
}
