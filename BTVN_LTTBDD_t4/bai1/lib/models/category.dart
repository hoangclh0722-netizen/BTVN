import 'package:flutter/material.dart';

abstract class Category {
  const Category();

  String get label;
  Color get textColor;
  Color get backgroundColor;
}

class StudyCategory extends Category {
  const StudyCategory();

  @override
  String get label => 'Học tập';
  @override
  Color get textColor => const Color(0xFF3B5BDB);
  @override
  Color get backgroundColor => const Color(0xFFE7ECFF);
}

class LifeCategory extends Category {
  const LifeCategory();

  @override
  String get label => 'Cuộc sống';
  @override
  Color get textColor => const Color(0xFFD9822B);
  @override
  Color get backgroundColor => const Color(0xFFFFF1E0);
}
