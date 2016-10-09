import os
import sys
sys.path.insert(0, os.path.abspath('.'))
# from camera_api.camera import Camera
# import camera_api.config as config
from mockCamera import VideoCamera;

import time
import argparse
from flask import Flask, render_template, Response, jsonify, url_for, send_file



# Gallery folder name
STATIC_FOLDER = 'gallery'


# Init Flask app & Camera object
app = Flask(__name__, static_folder=STATIC_FOLDER)
# ip_camera = Camera(config.DEFAULT_CAMERA_IP, config.USER_NAME, config.PASSWORD)

def gen(camera):
    """Video streaming generator function."""
    while True:
        frame = camera.get_frame()
        yield(b'--frame\r\n'
              b'Content-Type: image/jpeg\r\n\r\n' + frame + b'\r\n')


@app.route('/')
def index():
    """Video streaming homepage."""
    return render_template('index.html')


@app.route('/video_feed')
def video_feed():
    """Video streaming route."""
    return Response(gen(VideoCamera()),
                    mimetype='multipart/x-mixed-replace; boundary=frame')

@app.route('/get_images')
def get_images():
    frame = videoCam.get_frame()
    return send_file(frame, mimetype='image/jpeg', as_attachment=True, attachment_filename='myfile.jpg')

@app.route('/control_camera/<direction>', methods=['POST'])
def control_camera(direction):
    """Control camera move left, right, up, down."""
    return {
        'left': ip_camera.turn_left(),
        'right': ip_camera.turn_right(),
        'up': ip_camera.turn_up(),
        'down': ip_camera.turn_down()
    }[direction]


@app.route('/capture_image')
def capture_image():
    """
    Capture image and save to local storage when user
    press 'Capture' button on Android client.
    """
    pass


@app.route('/gallery', methods=['GET'])
def gallery():
    """
    Get list of all captured images included url, name, date captured.
    @return_type: JSON
    """
    images = []
    img_name = os.listdir(app.static_folder)
    for name in img_name:
        images.append({
            'url': url_for('static', filename=name, _external=True),
            'name': name,
            'date_captured': time.ctime(os.path.getctime(os.path.join(app.static_folder, name))) 
        })
    return jsonify(images)


@app.route('/gallery/<path:filename>', methods=['GET'])
def gallery_image(filename):
    """Serve specified captured image."""
    return app.send_static_file(filename)



if __name__ == '__main__':
    # Construct the argument parser and parse the arguments
    ap = argparse.ArgumentParser()
    ap.add_argument("-c", "--camera", help="URL of IP Camera")
    args = vars(ap.parse_args())

    # If the camera argument is not None,
    # then we set it to CAMERA_URL variable
    # otherwise we use default value
    if args.get('camera', None) is not None:
        CAMERA_URL = args['camera']
    
    # Run the app
    app.run(host='0.0.0.0', debug=True)
