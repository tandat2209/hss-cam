USER_NAME = 'admin'
PASSWORD = ''

DEFAULT_CAMERA_IP = 'http://192.168.1.239:81/'
# http://192.168.1.239:81/media/?action=cmd&code=2&value=9&user=admin&pwd=
# http://192.168.1.239:81/media/?action=stream : Content-Type:multipart/x-mixed-replace;
STREAM_URI = 'media/?action=stream'
CONTROL_URI = "media/?action=cmd&code={0}&value={1}"
CREDENTIAL_URI = "&user={0}&pwd={1}"