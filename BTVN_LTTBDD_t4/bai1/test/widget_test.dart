import 'package:flutter_test/flutter_test.dart';

import 'package:bai1/main.dart';

void main() {
  testWidgets('Splash chuyển sang onboarding rồi tới home', (tester) async {
    await tester.pumpWidget(UthTodoApp());
    expect(find.text('Phiên bản 2.1.0'), findsOneWidget);

    await tester.pumpAndSettle(const Duration(seconds: 3));
    expect(find.text('Mọi việc, gọn trong một chỗ'), findsOneWidget);

    await tester.tap(find.text('Tiếp tục'));
    await tester.pumpAndSettle();
    expect(find.text('Chia theo Học tập và Cuộc sống'), findsOneWidget);

    await tester.tap(find.text('Bỏ qua'));
    await tester.pumpAndSettle();
    expect(find.text('Hôm nay'), findsOneWidget);
  });
}
