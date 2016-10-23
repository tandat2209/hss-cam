import cv2
from motionDetector import MotionDetector

fps = 15
capSize = (640,480)
fourcc = cv2.cv.CV_FOURCC('m', 'p', '4', 'v') 
  
class VideoCamera(object):
    def __init__(self):
        # Using OpenCV to capture from device 0. If you have trouble capturing
        # from a webcam, comment the line below out and use a video file
        # instead.
        self.video = cv2.VideoCapture(0)
        # If you decide to use video.mp4, you must have this file in the folder
        # as the main.py.
        # self.video = cv2.VideoCapture('video.mp4')
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