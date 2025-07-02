import 'package:flimer_plus/flimer_plus.dart';

getFlimerPlus() => FlimerPlusStub();

class FlimerPlusStub implements FlimerPlus {
  @override
  Future<XFile?> pickImage({
    ImageSource source = ImageSource.gallery,
    double? maxWidth,
    double? maxHeight,
    int? imageQuality,
    CameraDevice preferredCameraDevice = CameraDevice.rear,
  }) {
    throw UnimplementedError('Unknown platform');
  }

  @override
  Future<List<XFile>?> pickImages({
    double? maxWidth,
    double? maxHeight,
    int? imageQuality,
  }) {
    throw UnimplementedError('Unknown platform');
  }
}
