#define TURN_LEFT 'L'
#define TURN_RIGHT 'R'
#define DO_NOTHING 'N'
#define DISTANCE_AS_DIFFERENT 30

struct DistanceSensor
{
    byte trig;
    byte echo;
};

DistanceSensor distanceSensorLeft = {5, 6},
               distanceSensorMiddle = {7, 8},
               distanceSensorRight = {9, 10};

// PIR: motion detection sensor
int PIRLeft = 2,
    PIRMiddle = 3,
    PIRRight = 4;

// pivot value for distance
int pivotDistanceLeft,
    pivotDistanceMiddle,
    pivotDistanceRight;

char lastCommand = DO_NOTHING;

void setup()
{
    Register_Distance_Sensor(distanceSensorLeft);
    Register_Distance_Sensor(distanceSensorMiddle);
    Register_Distance_Sensor(distanceSensorRight);

    Register_PIR_Sensor(PIRLeft);
    Register_PIR_Sensor(PIRMiddle);
    Register_PIR_Sensor(PIRRight);

    pivotDistanceLeft = Get_Distance(distanceSensorLeft);
    pivotDistanceMiddle = Get_Distance(distanceSensorMiddle);
    pivotDistanceRight = Get_Distance(distanceSensorRight);

    // Serial for bluetooth
    Serial.begin(9600);
}

void loop()
{
    int IsLeftHasMotion = Is_Distance_Change(distanceSensorLeft, &pivotDistanceLeft) && Get_PIR_value(PIRLeft),
        IsMiddleHasMotion = Is_Distance_Change(distanceSensorMiddle, &pivotDistanceMiddle) && Get_PIR_value(PIRMiddle),
        IsRightHasMotion = Is_Distance_Change(distanceSensorRight, &pivotDistanceRight) && Get_PIR_value(PIRRight);
    char command;

    if (IsLeftHasMotion)
    {
        command = TURN_LEFT;
    }

    if (IsMiddleHasMotion)
    {
        command = (lastCommand == TURN_LEFT) ? TURN_RIGHT : TURN_LEFT;
    }
    
    if (IsRightHasMotion)
    {
        command = TURN_RIGHT;
    }

    Send_Command(command);
    delay(1000);
    lastCommand = command;
}

void Register_Distance_Sensor(DistanceSensor sensor)
{
    pinMode(sensor.trig, OUTPUT);
    pinMode(sensor.echo, INPUT);
}

void Register_PIR_Sensor(int pin)
{
    pinMode(pin, INPUT);
}

int Get_PIR_value(int inputPin)
{
    int val = digitalRead(inputPin);
    return val;
}

int Get_Distance(DistanceSensor sensor)
{
    unsigned long duration;
    int distance;
    digitalWrite(sensor.trig, 0);
    delayMicroseconds(2);
    digitalWrite(sensor.trig, 1);
    delayMicroseconds(5);
    digitalWrite(sensor.trig, 0);
  
    duration = pulseIn(sensor.echo, HIGH);
    distance = int(duration/2/29.412);
    return distance;
}

int Is_Distance_Change(DistanceSensor sensor, int *pivot)
{
    int currentDistance = Get_Distance(sensor);
    if ((currentDistance - *pivot > DISTANCE_AS_DIFFERENT) || (*pivot - currentDistance > DISTANCE_AS_DIFFERENT))
    {
        *pivot = currentDistance;
        return 1; // true
    }
    return 0;
}

// this function send a command through bluetooth
void Send_Command(char command)
{
    Serial.print(command);
}
