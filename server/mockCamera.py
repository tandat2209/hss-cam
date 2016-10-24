import cv2
from motionDetector import MotionDetector

cameraIPURL = "http://admin@192.168.1.239:81/media/?&user=admin&pwd=&action=stream&.mjpeg"
fps = 15
capSize = (640,480)
fourcc = cv2.cv.CV_FOURCC('m', 'p', '4', 'v') 
  
class VideoCamera(object):
    def __init__(self):
        # Using OpenCV to capture from device 0. If you have trouble capturing
        # from a webcam, comment the line below out and use a video file
        # instead.
        self.video = cv2.VideoCamera(0)
        # self.video = cv2.VideoCapture(cameraIPURL)
        # If you decide to use video.mp4, you must have this file in the folder
        # as the main.py.
        self.prevFrame = None
        self.motionDetector = MotionDetector()
        self.videoWriter = cv2.VideoWriter('1.mov', fourcc, fps, capSize, True)
    
    def __del__(self):
        self.video.release()
    

    def get_frame(self):
        
        ret, frame = self.video.read()
        detectFrame = self.motionDetector.detect(frame)
        ret, jpeg = cv2.imencode('.jpg', detectFrame)
        return jpeg.tobytes()

    def get_video(self):
        ret, frame = self.video.read()
        detectFrame = self.motionDetector.detect(frame)
        self.videoWriter.write(detectFrame)