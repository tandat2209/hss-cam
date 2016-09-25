import cv2
import numpy as np
from datetime import datetime
import time

cv2.namedWindow("background-subtraction", 1)
cv2.namedWindow("fgmask", 1)

class MotionDetector():
    def __init__(self,threshold=8):
        self.threshold = threshold
        self.trigger_time = 0 #Hold timestamp of the last detection
        self.res = None
        self.isRecording = False

    def run(self):
        started = time.time()
        cap = cv2.VideoCapture(0)
        ret, frame = cap.read()
        self.height, self.width = frame.shape[0:2]
        self.nb_pixels = self.width * self.height   
        prevFrame = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        while True:
            ret, frame = cap.read()
            curFrame = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
            instant = time.time() #Get timestamp o the frame
            self.res = self.imageDiffs(curFrame, prevFrame) #Process the image
            if not self.isRecording:
                if(self.somethingHasMoved()):
                    self.trigger_time = instant #Update the trigger_time
                    if instant >  started +5:#Wait 5 second after the webcam start for luminosity adjusting etc..
                        print datetime.now().strftime("%b %d, %H:%M:%S"), "Something is moving !"
                        self.isRecording = True
            else:
                if instant >= self.trigger_time +10: #Record during 10 seconds
                    print datetime.now().strftime("%b %d, %H:%M:%S"), "Stop recording"
                    self.isRecording = False
            prevFrame = curFrame
            cv2.imshow("main", frame)
            cv2.imshow("subtract", self.res)
            k = cv2.waitKey(30) & 0xFF
            if k == 27:
                break
        cv2.destroyAllWindows()

    def imageDiffs(self, frame1, frame2):
        #Absdiff to get the difference between to the frames
        res = cv2.absdiff(frame1, frame2)

        #Remove the noise and do the threshold
        res = cv2.blur(res, (5,5))
        kernel = np.ones((5,5),np.uint8)
        res = cv2.morphologyEx(res, cv2.MORPH_OPEN, kernel)
        res = cv2.morphologyEx(res, cv2.MORPH_CLOSE, kernel)
        
        ret, res = cv2.threshold(res, 10, 255, cv2.THRESH_BINARY_INV)
        return res

    def somethingHasMoved(self):
        nb=0 #Will hold the number of black pixels
        for x in range(self.height): #Iterate the hole image
            for y in range(self.width):
                if self.res[x,y] == 0.0: #If the pixel is black keep it
                    nb += 1
        avg = (nb*100.0)/self.nb_pixels #Calculate the average of black pixel in the image
        return avg > self.threshold

if __name__=="__main__":
    detect = MotionDetector()
    detect.run()