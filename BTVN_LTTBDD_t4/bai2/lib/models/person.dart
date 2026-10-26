abstract class Person {
  final String _id;
  final String _name;

  Person({required this._id, required this._name});

  String get id => _id;
  String get name => _name;

  String get role;

  String describe() => '$role: $_name ($_id)';
}

class Student extends Person {
  final String _className;

  Student({required super.id, required super.name, required this._className});

  String get className => _className;

  @override
  String get role => 'Sinh viên';

  @override
  String describe() => '${super.describe()} - Lớp $_className';
}
