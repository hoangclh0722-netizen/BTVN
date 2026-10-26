import 'package:flutter/material.dart';

import 'models/app_info.dart';
import 'models/onboarding_page.dart';
import 'models/todo_repository.dart';
import 'screens/home_screen.dart';
import 'screens/onboarding_screen.dart';
import 'screens/splash_screen.dart';
import 'theme.dart';

void main() {
  runApp(UthTodoApp());
}

class UthTodoApp extends StatelessWidget {
  final AppInfo info = AppInfo.uthTodo;
  final OnboardingRepository onboarding = OnboardingRepository();
  final TodoRepository todos = TodoRepository();

  UthTodoApp({super.key});

  Route<dynamic> _onGenerateRoute(RouteSettings settings) {
    final Widget page = switch (settings.name) {
      AppRoutes.onboarding => OnboardingScreen(
        info: info,
        repository: onboarding,
        index: settings.arguments as int? ?? 0,
      ),
      AppRoutes.home => HomeScreen(info: info, repository: todos),
      _ => SplashScreen(info: info),
    };
    return MaterialPageRoute(builder: (_) => page, settings: settings);
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: info.name,
      debugShowCheckedModeBanner: false,
      theme: ThemeData(colorSchemeSeed: AppColors.primary),
      initialRoute: AppRoutes.splash,
      onGenerateRoute: _onGenerateRoute,
    );
  }
}
