import 'package:flimer_plus/flimer_plus.dart';
import 'package:flutter_web_plugins/flutter_web_plugins.dart';
import 'package:image_picker/image_picker.dart' as ip;

getFlimerPlus() => FlimerPlusHtml();

class FlimerPlusHtml implements FlimerPlus {
  static void registerWith(Registrar registrar) {}

  final ip.ImagePicker _picker = ip.ImagePicker();

  @override
  Future<XFile?> pickImage({
    ImageSource source = ImageSource.gallery,
    double? maxWidth,
    double? maxHeight,
    int? imageQuality,
    CameraDevice preferredCameraDevice = CameraDevice.rear,
  }) async {
    return await _picker.pickImage(
      source: source,
      maxWidth: maxWidth,
      maxHeight: maxHeight,
      imageQuality: imageQuality,
      preferredCameraDevice: preferredCameraDevice,
    );
  }

  @override
  Future<List<XFile>?> pickImages({
    double? maxWidth,
    double? maxHeight,
    int? imageQuality,
  }) async {
    return await _picker.pickMultiImage(
      maxWidth: maxWidth,
      maxHeight: maxHeight,
      imageQuality: imageQuality,
    );
  }
}
