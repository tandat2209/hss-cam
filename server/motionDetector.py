import cv2
import numpy as np
from datetime import datetime

MIN_COUNTOUR_AREA = 10000
MAX_COUNTOUR_AREA = 90000
BLUR_FILTER_MASK_SIZE = 11
MORPH_CLOSE_KERNEL = np.ones((71,71),np.uint8)
ERODE_KERNEL = np.ones((3,3), np.uint8)
DILATE_KERNEL = np.ones((3,3), np.uint8)

DETECT_SECOND = 2
TAKE_PICTURE_SECOND = 50

class MotionDetector(object):
    def __init__(self):
        self.prevFrame = None
        self.hasSomeThing = False
        self.prevDetectTime = datetime.now()
        self.interruptTime = datetime.now()
        pass
    
    def detect(self, curFrame):
        # process frame
        frame = curFrame

        curFrame = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        curFrame = cv2.medianBlur(curFrame, BLUR_FILTER_MASK_SIZE)
        if self.prevFrame is None:
            self.prevFrame = curFrame
            return frame

        frameDelta = cv2.absdiff(curFrame, self.prevFrame)
        _, thresh = cv2.threshold(frameDelta, 10, 255, cv2.THRESH_BINARY)
        
        thresh = cv2.erode(thresh, ERODE_KERNEL, iterations=1)
        thresh = cv2.dilate(thresh, DILATE_KERNEL, iterations=1)
        
        thresh = cv2.morphologyEx(thresh, cv2.MORPH_CLOSE, MORPH_CLOSE_KERNEL)
       
        contours, _ = cv2.findContours(thresh.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
        for c in contours:
            contourArea = cv2.contourArea(c)
            if contourArea < MIN_COUNTOUR_AREA or contourArea > MAX_COUNTOUR_AREA:
                continue
            if(datetime.now() - self.prevDetectTime).total_seconds() > DETECT_SECOND:
                if (not self.hasSomeThing): 
                    print self.hasSomeThing, datetime.now(), "Something is moving => take picture"
                self.hasSomeThing = True
                self.interruptTime = None

            if (datetime.now() - self.prevDetectTime).total_seconds() > TAKE_PICTURE_SECOND:
                self.prevDetectTime = datetime.now()
                print self.hasSomeThing, datetime.now(), "Something is moving => take picture"
            
            x, y, w, h = cv2.boundingRect(c)
            cv2.rectangle(frame, (x,y), (x+w,y+h), (0,255,0), 2)
            # cv2.drawContours(frame, [c], 0, (0,255,0), 2)
        self.prevFrame = curFrame
        return frame