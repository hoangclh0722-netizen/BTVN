import 'package:flutter/material.dart';

import '../models/app_info.dart';
import '../theme.dart';

class AppLogo extends StatelessWidget {
  final double size;
  final Color background;
  final Color foreground;

  const AppLogo({
    super.key,
    this.size = 24,
    this.background = AppColors.primary,
    this.foreground = Colors.white,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      width: size,
      height: size,
      decoration: BoxDecoration(
        color: background,
        borderRadius: BorderRadius.circular(size * 0.25),
      ),
      child: Icon(
        Icons.checklist_rounded,
        size: size * 0.65,
        color: foreground,
      ),
    );
  }
}

class BrandHeader extends StatelessWidget {
  final AppInfo info;
  final VoidCallback? onSkip;

  const BrandHeader({super.key, required this.info, this.onSkip});

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        const AppLogo(),
        const SizedBox(width: 8),
        Text.rich(
          TextSpan(
            style: const TextStyle(fontSize: 16, fontWeight: FontWeight.w600),
            children: [
              TextSpan(
                text: '${info.brandPrefix} ',
                style: const TextStyle(color: AppColors.text),
              ),
              TextSpan(
                text: info.brandSuffix,
                style: const TextStyle(color: AppColors.primary),
              ),
            ],
          ),
        ),
        const Spacer(),
        if (onSkip != null)
          TextButton(
            onPressed: onSkip,
            child: const Text(
              'Bỏ qua',
              style: TextStyle(color: AppColors.subText),
            ),
          ),
      ],
    );
  }
}
