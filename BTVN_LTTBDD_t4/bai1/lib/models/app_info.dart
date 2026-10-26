class AppInfo {
  final String _name;
  final String _tagline;
  final String _version;

  const AppInfo({
    required this._name,
    required this._tagline,
    required this._version,
  });

  String get name => _name;
  String get tagline => _tagline;
  String get versionLabel => 'Phiên bản $_version';

  // "UTH Todo" -> "UTH" + "Todo" để tô màu riêng trên header
  String get brandPrefix => _name.split(' ').first;
  String get brandSuffix => _name.split(' ').skip(1).join(' ');

  static const uthTodo = AppInfo(
    name: 'UTH Todo',
    tagline: 'Việc học, việc nhà — gọn gàng mỗi ngày',
    version: '2.1.0',
  );
}
