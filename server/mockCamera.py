import cv2
from motionDetector import MotionDetector

cameraIPURL = "http://admin@192.168.1.239:81/media/?&user=admin&pwd=&action=stream&.mjpeg"
  
class VideoCamera(object):
    def __init__(self):
        # Using OpenCV to capture from device 0. If you have trouble capturing
        # from a webcam, comment the line below out and use a video file
        # instead.
        # self.video = cv2.VideoCapture(0)
        self.video = cv2.VideoCapture(cameraIPURL)
        self.motionDetector = MotionDetector()
    
    def __del__(self):
        self.video.release()
    

    def get_frame(self):
        ret, frame = self.video.read()
        detectFrame = self.motionDetector.detect(frame)
        ret, jpeg = cv2.imencode('.jpg', detectFrame)
        return jpeg.tobytes()

    def save_frame_to_image(self, path):
        ret, frame = self.video.read()
        try:
            cv2.imwrite(path, frame)
        except IOError:
            pass
