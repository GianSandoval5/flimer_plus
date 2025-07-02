# Flimer Plus 📸

[![pub package](https://img.shields.io/pub/v/flimer.svg)](https://pub.dev/packages/flimer_plus)
[![Licencia](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Plataforma](https://img.shields.io/badge/platform-flutter-blue.svg)](https://flutter.dev)

**Flimer Plus** significa **Fl**utter **Im**age Pick**er** Plus - Un selector de imágenes multiplataforma unificado y mejorado para Flutter que funciona perfectamente en plataformas Móviles, de Escritorio y Web.

## ✨ Características

- 🎯 **Multiplataforma**: Funciona en Android, iOS, Web, macOS, Windows y Linux
- 📱 **Optimizado para móviles**: Acceso a cámara y galería en dispositivos móviles
- 🖥️ **Amigable con escritorio**: Diálogos de archivos nativos en plataformas de escritorio
- 🌐 **Compatible con web**: Integración de selector de archivos para aplicaciones web
- 🔧 **Fácil de usar**: API simple y consistente en todas las plataformas
- 🎨 **Personalizable**: Soporte para calidad de imagen, restricciones de tamaño y selección de cámara

## 🏗️ Arquitectura

Flimer Plus combina inteligentemente lo mejor de ambos mundos:

| Plataforma | Paquete Subyacente |
|----------|-------------------|
| **Android e iOS** | [image_picker](https://pub.dev/packages/image_picker) |
| **Web** | [image_picker](https://pub.dev/packages/image_picker) |
| **macOS, Windows y Linux** | [file_selector](https://pub.dev/packages/file_selector) |

## 📦 Instalación

Agrega flimer plus a tu `pubspec.yaml`:

```yaml
dependencies:
  flimer_plus: ^1.0.0
```

Luego ejecuta:

```bash
flutter pub get
```

## 🚀 Inicio Rápido

Importa el paquete:

```dart
import 'package:flimer_plus/flimer_plus.dart';
```

### Seleccionar una Imagen

```dart
// Seleccionar de la galería (por defecto)
final XFile? file = await flimerPlus.pickImage();

// Seleccionar de la cámara
final XFile? file = await flimerPlus.pickImage(source: ImageSource.camera);

// Con opciones personalizadas
final XFile? file = await flimerPlus.pickImage(
  source: ImageSource.gallery,
  maxWidth: 1024,
  maxHeight: 1024,
  imageQuality: 85,
);

if (file != null) {
  final String fileName = file.name;
  final String filePath = file.path;
  // Usa tu imagen aquí
}
```

### Seleccionar Múltiples Imágenes

```dart
final List<XFile>? files = await flimerPlus.pickImages();

if (files != null && files.isNotEmpty) {
  print("Seleccionadas ${files.length} imágenes");
  for (final file in files) {
    print("Imagen: ${file.name}");
  }
}
```

### Uso Avanzado

```dart
// Seleccionar imagen con restricciones de calidad y tamaño
final XFile? file = await flimerPlus.pickImage(
  source: ImageSource.camera,
  maxWidth: 800,
  maxHeight: 600,
  imageQuality: 70,
  preferredCameraDevice: CameraDevice.front,
);

// Seleccionar múltiples imágenes con restricciones
final List<XFile>? files = await flimerPlus.pickImages(
  maxWidth: 1200,
  maxHeight: 1200,
  imageQuality: 80,
);
```

## 📱 Comportamiento Específico por Plataforma

### Móviles (Android e iOS)
- **Galería**: Abre la galería de imágenes del dispositivo
- **Cámara**: Abre la aplicación de cámara para tomar nuevas fotos
- Soporta selección de imagen simple y múltiple
- Respeta los parámetros de calidad y tamaño de imagen

### Escritorio (macOS, Windows, Linux)
- Abre diálogos nativos de selector de archivos
- Soporta formatos de imagen comunes: JPG, JPEG, PNG, WebP, BMP
- Tanto `ImageSource.camera` como `ImageSource.gallery` abren diálogos de archivos
- Selección múltiple de imágenes soportada

### Web
- Abre el selector de archivos del navegador
- Soporta filtrado de formatos de imagen
- Las URLs de archivos se proporcionan como URLs blob
- Respeta las restricciones de tamaño y calidad

## 🎨 Ejemplo Completo

```dart
import 'package:flimer_plus/flimer_plus.dart';
import 'package:flutter/material.dart';

class EjemploSelectorImagenPlus extends StatefulWidget {
  @override
  _EjemploSelectorImagenPlusState createState() => _EjemploSelectorImagenPlusState();
}

class _EjemploSelectorImagenPlusState extends State<EjemploSelectorImagenPlus> {
  XFile? _imagenSeleccionada;
  List<XFile>? _imagenesSeleccionadas;

  Future<void> _seleccionarImagenUnica() async {
    final file = await flimerPlus.pickImage(source: ImageSource.gallery);
    if (file != null) {
      setState(() {
        _imagenSeleccionada = file;
      });
    }
  }

  Future<void> _seleccionarMultiplesImagenes() async {
    final files = await flimerPlus.pickImages();
    if (files != null && files.isNotEmpty) {
      setState(() {
        _imagenesSeleccionadas = files;
      });
    }
  }

  Future<void> _tomarFoto() async {
    final file = await flimerPlus.pickImage(source: ImageSource.camera);
    if (file != null) {
      setState(() {
        _imagenSeleccionada = file;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Ejemplo Flimer Plus')),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            ElevatedButton(
              onPressed: _seleccionarImagenUnica,
              child: Text('Seleccionar Una Imagen'),
            ),
            ElevatedButton(
              onPressed: _seleccionarMultiplesImagenes,
              child: Text('Seleccionar Múltiples Imágenes'),
            ),
            ElevatedButton(
              onPressed: _tomarFoto,
              child: Text('Tomar Foto'),
            ),
            if (_imagenSeleccionada != null)
              Container(
                margin: EdgeInsets.all(16),
                child: Text('Seleccionada: ${_imagenSeleccionada!.name}'),
              ),
            if (_imagenesSeleccionadas != null)
              Container(
                margin: EdgeInsets.all(16),
                child: Text('Seleccionadas ${_imagenesSeleccionadas!.length} imágenes'),
              ),
          ],
        ),
      ),
    );
  }
}
```

## 🔧 Referencia de API

### `flimerPlus.pickImage()`

Selecciona una imagen de la galería o cámara.

**Parámetros:**
- `source` (ImageSource): Fuente de imagen (galería o cámara). Por defecto: `ImageSource.gallery`
- `maxWidth` (double?): Ancho máximo de la imagen seleccionada
- `maxHeight` (double?): Alto máximo de la imagen seleccionada
- `imageQuality` (int?): Calidad de imagen de 0-100. Por defecto: calidad original
- `preferredCameraDevice` (CameraDevice): Cámara a usar. Por defecto: `CameraDevice.rear`

**Retorna:** `Future<XFile?>` - Archivo de imagen seleccionado o null si se canceló

### `flimerPlus.pickImages()`

Selecciona múltiples imágenes de la galería.

**Parámetros:**
- `maxWidth` (double?): Ancho máximo de las imágenes seleccionadas
- `maxHeight` (double?): Alto máximo de las imágenes seleccionadas
- `imageQuality` (int?): Calidad de imagen de 0-100. Por defecto: calidad original

**Retorna:** `Future<List<XFile>?>` - Lista de archivos de imagen seleccionados o null si se canceló

## 📄 Formatos de Imagen Soportados

- **JPG/JPEG** - Joint Photographic Experts Group
- **PNG** - Portable Network Graphics
- **WebP** - Formato Web Picture
- **BMP** - Bitmap (plataformas de escritorio)

## 🤝 Contribuciones

¡Las contribuciones son bienvenidas! No dudes en enviar un Pull Request.

1. Haz fork del repositorio
2. Crea tu rama de característica (`git checkout -b feature/caracteristica-increible`)
3. Confirma tus cambios (`git commit -m 'Agregar característica increíble'`)
4. Sube la rama (`git push origin feature/caracteristica-increible`)
5. Abre un Pull Request

## 📝 Licencia

Este proyecto está licenciado bajo la Licencia MIT - consulta el archivo [LICENSE](LICENSE) para más detalles.

## 🙋‍♂️ Soporte

¡Si te gusta este paquete, por favor dale una ⭐ en [GitHub](https://github.com/GianSandoval5/flimer_plus) y un 👍 en [pub.dev](https://pub.dev/packages/flimer_plus)!

Para preguntas o problemas, por favor reporta un issue en [GitHub](https://github.com/GianSandoval5/flimer_plus/issues).

