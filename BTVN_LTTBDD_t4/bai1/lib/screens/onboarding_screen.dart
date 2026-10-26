import 'package:flutter/material.dart';

import '../models/app_info.dart';
import '../models/onboarding_page.dart';
import '../theme.dart';
import '../widgets/app_logo.dart';

class OnboardingScreen extends StatelessWidget {
  final AppInfo info;
  final OnboardingRepository repository;
  final int index;

  const OnboardingScreen({
    super.key,
    required this.info,
    required this.repository,
    required this.index,
  });

  void _goHome(BuildContext context) {
    Navigator.pushNamedAndRemoveUntil(context, AppRoutes.home, (_) => false);
  }

  void _next(BuildContext context) {
    if (repository.isLast(index)) {
      _goHome(context);
    } else {
      Navigator.pushNamed(context, AppRoutes.onboarding, arguments: index + 1);
    }
  }

  @override
  Widget build(BuildContext context) {
    final page = repository.pageAt(index);
    return Scaffold(
      backgroundColor: Colors.white,
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.fromLTRB(24, 8, 24, 24),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              BrandHeader(info: info, onSkip: () => _goHome(context)),
              Expanded(child: Center(child: page.illustration.build())),
              Text(
                page.title,
                style: const TextStyle(
                  fontSize: 22,
                  fontWeight: FontWeight.bold,
                  color: AppColors.text,
                ),
              ),
              const SizedBox(height: 10),
              Text(
                page.description,
                style: const TextStyle(color: AppColors.subText, height: 1.4),
              ),
              const SizedBox(height: 28),
              _Dots(count: repository.length, active: index),
              const SizedBox(height: 20),
              SizedBox(
                width: double.infinity,
                height: 48,
                child: FilledButton(
                  style: FilledButton.styleFrom(
                    backgroundColor: AppColors.primary,
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(10),
                    ),
                  ),
                  onPressed: () => _next(context),
                  child: Text(page.buttonLabel),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class _Dots extends StatelessWidget {
  final int count;
  final int active;

  const _Dots({required this.count, required this.active});

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        for (var i = 0; i < count; i++)
          AnimatedContainer(
            duration: const Duration(milliseconds: 200),
            margin: const EdgeInsets.only(right: 6),
            width: i == active ? 18 : 6,
            height: 6,
            decoration: BoxDecoration(
              color: i == active ? AppColors.primary : AppColors.line,
              borderRadius: BorderRadius.circular(3),
            ),
          ),
      ],
    );
  }
}
