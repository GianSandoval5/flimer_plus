import 'dart:async';

import 'package:cross_file/cross_file.dart';
import 'package:image_picker/image_picker.dart' show ImageSource, CameraDevice;

import 'flimer_stub.dart'
    if (dart.library.io) 'flimer_io.dart'
    if (dart.library.html) 'flimer_html.dart';

export 'package:cross_file/cross_file.dart' show XFile;
export 'package:image_picker/image_picker.dart' show ImageSource, CameraDevice;
export 'flimer_stub.dart'
    if (dart.library.io) 'flimer_io.dart'
    if (dart.library.html) 'flimer_html.dart';

/// Global singleton [FlimerPlus] instance
final FlimerPlus flimerPlus = FlimerPlus();

/// FlimerPlus stands for Flutter Image Picker Plus.
abstract class FlimerPlus {
  static void registerWith() {}

  /// Construct flimerPlus using global platform getter.
  factory FlimerPlus() => getFlimerPlus();

  /// Pick a single image.
  ///
  /// Define the [ImageSource] On Android and iOS platforms. While on Desktop
  /// and Web this will open a dialog image picker
  Future<XFile?> pickImage({
    ImageSource source = ImageSource.gallery,
    double? maxWidth,
    double? maxHeight,
    int? imageQuality,
    CameraDevice preferredCameraDevice = CameraDevice.rear,
  });

  /// Pick multiple images at once.
  ///
  /// Open the image gallery on Android and iOS platforms. While on Desktop
  /// and Web this will open a dialog images picker
  Future<List<XFile>?> pickImages({
    double? maxWidth,
    double? maxHeight,
    int? imageQuality,
  });
}
