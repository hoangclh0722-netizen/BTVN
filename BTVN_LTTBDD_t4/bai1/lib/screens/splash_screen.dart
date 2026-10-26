import 'package:flutter/material.dart';

import '../models/app_info.dart';
import '../theme.dart';
import '../widgets/app_logo.dart';

class SplashScreen extends StatefulWidget {
  final AppInfo info;

  const SplashScreen({super.key, required this.info});

  @override
  State<SplashScreen> createState() => _SplashScreenState();
}

class _SplashScreenState extends State<SplashScreen>
    with SingleTickerProviderStateMixin {
  late final AnimationController _progress = AnimationController(
    vsync: this,
    duration: const Duration(seconds: 2),
  );

  @override
  void initState() {
    super.initState();
    _progress.forward().whenComplete(() {
      if (!mounted) return;
      Navigator.pushReplacementNamed(
        context,
        AppRoutes.onboarding,
        arguments: 0,
      );
    });
  }

  @override
  void dispose() {
    _progress.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final info = widget.info;
    return Scaffold(
      backgroundColor: AppColors.primary,
      body: SafeArea(
        child: SizedBox(
          width: double.infinity,
          child: Column(
            children: [
              const Spacer(),
              const AppLogo(
                size: 96,
                background: Colors.white,
                foreground: AppColors.primary,
              ),
              const SizedBox(height: 20),
              Text(
                info.name,
                style: const TextStyle(
                  color: Colors.white,
                  fontSize: 32,
                  fontWeight: FontWeight.bold,
                ),
              ),
              const SizedBox(height: 6),
              Text(info.tagline, style: const TextStyle(color: Colors.white70)),
              const Spacer(),
              SizedBox(
                width: 100,
                child: AnimatedBuilder(
                  animation: _progress,
                  builder: (_, _) => LinearProgressIndicator(
                    value: _progress.value,
                    minHeight: 3,
                    color: Colors.white,
                    backgroundColor: Colors.white24,
                    borderRadius: BorderRadius.circular(2),
                  ),
                ),
              ),
              const SizedBox(height: 12),
              Text(
                info.versionLabel,
                style: const TextStyle(color: Colors.white70, fontSize: 12),
              ),
              const SizedBox(height: 32),
            ],
          ),
        ),
      ),
    );
  }
}
