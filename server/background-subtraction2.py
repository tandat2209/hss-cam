import cv2
import numpy as np
from datetime import datetime
import time

DETECT_SECOND = 2 # 2s
ALLOWED_INTERRUPT_SECOND = 3 # 3s
TAKE_PICTURE_SECOND = 10 # 10s

cap = cv2.VideoCapture(0)

def find_if_close(cnt1,cnt2):
    row1,row2 = cnt1.shape[0],cnt2.shape[0]
    for i in xrange(row1):
        for j in xrange(row2):
            dist = np.linalg.norm(cnt1[i]-cnt2[j])
            if abs(dist) < 50 :
                return True
    return False

def main():
    prevFrame = None
    prevDetectTime = datetime.now()
    interruptTime = datetime.now()
    something = False
    interrupt = False

    while True:
        ret, frame = cap.read()
        if not ret:
            break

        # process frame
        curFrame = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        curFrame = cv2.GaussianBlur(curFrame, (11, 11), 0)

        if prevFrame is None:
            prevFrame = curFrame
            continue

        frameDelta = cv2.absdiff(curFrame, prevFrame)
        _, thresh = cv2.threshold(frameDelta, 10, 255, cv2.THRESH_BINARY)
        kernel = np.ones((51,51),np.uint8)
        # thresh = cv2.dilate(thresh, kernel, iterations=3)
        thresh = cv2.morphologyEx(thresh, cv2.MORPH_CLOSE, kernel)
       
        contours, _ = cv2.findContours(thresh.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
        
        if(len(contours) == 0):
            if(not something):
                prevDetectTime = datetime.now()            
            else:
                if(interruptTime is None):
                    interruptTime = datetime.now()
                elif((datetime.now() - interruptTime).total_seconds() > ALLOWED_INTERRUPT_SECOND):
                    something = False;

        for c in contours:
            if cv2.contourArea(c) < 300:
                continue
            if(datetime.now() - prevDetectTime).total_seconds() > DETECT_SECOND:
                if (not something): 
                    print something, datetime.now(), "Something is moving => take picture"
                something = True
                interruptTime = None

            if (datetime.now() - prevDetectTime).total_seconds() > TAKE_PICTURE_SECOND:
                prevDetectTime = datetime.now()
                print something, datetime.now(), "Something is moving => take picture"
            
            x, y, w, h = cv2.boundingRect(c)
            cv2.rectangle(frame, (x,y), (x+w,y+h), (0,255,0), 2)
            # cv2.drawContours(frame, [c], 0, (0,255,0), 2)
        
        cv2.imshow("Frame", frame)
        cv2.imshow("Frame Delta", frameDelta)
        cv2.imshow("Thresh", thresh)

        prevFrame = curFrame
        k = cv2.waitKey(30) & 0xFF
        if k == 27:
            break

    cv2.destroyAllWindows()

if __name__=="__main__":
    main()
