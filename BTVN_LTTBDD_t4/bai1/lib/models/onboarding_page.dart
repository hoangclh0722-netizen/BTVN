import 'category.dart';
import 'illustration.dart';

class OnboardingPage {
  final String title;
  final String description;
  final Illustration illustration;
  final String buttonLabel;

  const OnboardingPage({
    required this.title,
    required this.description,
    required this.illustration,
    this.buttonLabel = 'Tiếp tục',
  });
}

class OnboardingRepository {
  final List<OnboardingPage> _pages = const [
    OnboardingPage(
      title: 'Mọi việc, gọn trong một chỗ',
      description:
          'Ghi lại bài tập, việc nhà, lịch hẹn. Không còn quên việc nào.',
      illustration: ChecklistIllustration([true, false, false]),
    ),
    OnboardingPage(
      title: 'Chia theo Học tập và Cuộc sống',
      description: 'Mỗi việc có nhóm riêng, nhìn là biết hôm nay cần lo gì.',
      illustration: CategoryIllustration([StudyCategory(), LifeCategory()]),
    ),
    OnboardingPage(
      title: 'Nhắc đúng lúc',
      description: 'Bật thông báo để UTH Todo nhắc bạn trước hạn chót.',
      illustration: ReminderIllustration('Ôn bài Toán lúc 19:00'),
      buttonLabel: 'Bắt đầu ngay',
    ),
  ];

  int get length => _pages.length;

  OnboardingPage pageAt(int index) => _pages[index];

  bool isLast(int index) => index == _pages.length - 1;
}
