import 'package:flutter/material.dart';
import 'dart:async';
import 'package:flutter/services.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({Key? key, required this.title}) : super(key: key);

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  // Создайте канал платформы, используйтя MethodChannel
  // Стороны клиента и хоста канала связаны через имя канала, переданное в конструкторе канала. Все имена каналов, используемые в одном приложении, должны быть уникальными.
  // В нашем примере мы создаем имя канала 'channelname/network'.
  static const platform = MethodChannel('channelname/network');
  // Get network info.
  String _networkInfo = 'Unknown network info.';

  // Вызвать метод на канале платформы
  // Вызвать метод в канале метода, указав конкретный метод для вызова через идентификатор String. В приведенном ниже коде это 'getNetworkInfo'.
  // Вызов может завершиться ошибкой, например, если платформа не поддерживает API платформы (например, при работе в симуляторе), поэтому оберните invokeMethod вызов оператором try-catch.
  Future<void> _getNetworkInfo() async {
    String networkInfo;
    try {
      final String result = await platform.invokeMethod('getNetworkInfo');
      networkInfo = result;
    } on PlatformException catch (e) {
      networkInfo = "Failed to get network info: '${e.message}'.";
    }

    // Используйте возвращенный результат, чтобы обновить состояние пользовательского интерфейса _networkInfo внутри setState.
    setState(() {
      _networkInfo = networkInfo;
    });
  }

  // Наконец, замените build метод из шаблона, чтобы он содержал небольшой пользовательский интерфейс, отображающий состояние батареи в виде строки, и кнопку для обновления значения.
  @override
  Widget build(BuildContext context) {
    return Material(
      child: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: [
            ElevatedButton(
              child: const Text('Get Network Info'),
              onPressed: _getNetworkInfo,
            ),
            Text(_networkInfo),
          ],
        ),
      ),
    );
  }
}
