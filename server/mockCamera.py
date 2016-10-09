import cv2
from motionDetector import MotionDetector

class VideoCamera(object):
    def __init__(self):
        # Using OpenCV to capture from device 0. If you have trouble capturing
        # from a webcam, comment the line below out and use a video file
        # instead.
        # self.video = cv2.VideoCapture(0)
        self.video = cv2.VideoCapture('http://192.168.1.239:81/media/?action=stream&user=admin&pwd=')
        # self.video = cv2.VideoCapture('http://iris.not.iac.es/axis-cgi/mjpg/video.cgi')
        # If you decide to use video.mp4, you must have this file in the folder
        # as the main.py.
        # self.video = cv2.VideoCapture('video.mp4')
        self.prevFrame = None
        self.motionDetector = MotionDetector()
    
    def __del__(self):
        self.video.release()
    

    def get_frame(self):
        
        ret, frame = self.video.read()
        detectFrame = self.motionDetector.detect(frame)
        ret, jpeg = cv2.imencode('.jpg', detectFrame)
        return jpeg.tobytes()