import requests
import config
import time

COMMAND_VALUE = {
	'UP' : 1,
	'DOWN' : 2,
	'LEFT' : 3,
	'RIGHT' : 4
}

COMMAND_CODE = {
	'START' : 2,
	'STOP' : 3
}

class Camera():
	def __init__(self, camera_address, username, password):
		self.camera_address = camera_address
		self.session = requests.Session()
		self.username = username
		self.password = password
		self.construct_api()

	def construct_api(self):
		self.credential_uri = config.CREDENTIAL_URI.format(self.username, self.password)

	def turn_up(self):
		self.execute('UP')

	def turn_down(self):
		self.execute('DOWN')

	def turn_left(self):
		self.execute('LEFT')

	def turn_right(self):
		self.execute('RIGHT')

	def get_stream_url(self):
		return self.camera_address + config.STREAM_URI + self.credential_uri

	def execute(self, value):
		camera_address = self.camera_address

		start_uri = config.CONTROL_URI.format(COMMAND_CODE['START'], COMMAND_VALUE[value])
		stop_uri = config.CONTROL_URI.format(COMMAND_CODE['STOP'], COMMAND_VALUE[value])
		
		start_url = camera_address + start_uri + self.credential_uri
		stop_url = camera_address + stop_uri + self.credential_uri

		# start, wait 1s and stop
		self.session.get(start_url)
		time.sleep(1)
		self.session.get(stop_url)

if __name__ == '__main__':
	camera = Camera(config.DEFAULT_CAMERA_IP, config.USER_NAME, config.PASSWORD)
	camera.turn_up()
	camera.turn_down()
	camera.turn_left()
	camera.turn_right()

	print(camera.get_stream_url())

