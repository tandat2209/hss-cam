import base64
import time
import urllib2
import cv2
import numpy as np


"""
IP Camera class
Required:
    - cv2 (OpenCV Python binding)
    - Numpy
"""

class Camera(object):

    def __init__(self, url, username=None, password=None):
        self.url = url
        auth_encoded = base64.encodestring('%s:%s' % (username, password))[:-1]

        self.request = urllib2.Request(self.url)
        self.request.add_header('Authorization', 'Basic %s' % auth_encoded)


    def get_frame(self):
        response = urllib2.urlopen(self.request)
        img_array = np.asarray(bytearray(response.read()), dtype=np.uint8)
        frame = cv2.imdecode(im_array, 1)
        return frame
