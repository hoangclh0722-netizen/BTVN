import 'package:flutter_test/flutter_test.dart';

import 'package:bai2/main.dart';
import 'package:bai2/models/library.dart';

void main() {
  testWidgets('Đổi sinh viên, mượn thêm sách rồi trả', (tester) async {
    final library = Library.sample();
    await tester.pumpWidget(LibraryApp(library: library));
    expect(find.text('Nguyen Van A'), findsOneWidget);
    expect(find.text('Lập trình Flutter cơ bản'), findsOneWidget);

    await tester.tap(find.text('Thay đổi'));
    await tester.pumpAndSettle();
    await tester.tap(find.text('Le Van C'));
    await tester.pumpAndSettle();
    expect(find.textContaining('Bạn chưa mượn'), findsOneWidget);

    await tester.tap(find.text('Thêm'));
    await tester.pumpAndSettle();
    expect(find.text('Số đỏ'), findsNothing); // Tran Thi B đang mượn
    await tester.tap(find.text('Tạp chí Tin học & Đời sống'));
    await tester.pump();
    await tester.tap(find.text('Mượn 1 cuốn'));
    await tester.pumpAndSettle();

    final c = library.students.last;
    expect(library.booksOf(c).single.title, 'Tạp chí Tin học & Đời sống');
  });
}
