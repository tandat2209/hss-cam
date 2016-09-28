import os
import time
import argparse
from flask import Flask, render_template, Response, jsonify, url_for
from camera import Camera


# Camera URL
CAMERA_URL = 'http://192.168.1.239:81'
# Gallery folder name
STATIC_FOLDER = 'gallery'


# Init Flask app & Camera object
app = Flask(__name__, static_folder=STATIC_FOLDER)
camera_instance = Camera(CAMERA_URL)



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
    return Response(gen(camera_instance),
                    mimetype='multipart/x-mixed-replace; boundary=frame')


@app.route('/control_camera/<direction>', methods=['POST'])
def control_camera(direction):
    """Control camera move left, right."""
    pass


@app.route('/capture_image')
def capture_image():
    """
    Capture image and save to loca storage when user
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
