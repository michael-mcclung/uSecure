# imports
import cv2
import os
import pyrebase
import firebase_admin
import numpy as np 
import pickle
import time
import RPi.GPIO as GPIO
import speech_recognition as sr
import pickle
from PIL import Image 
from time import sleep
from firebase_admin import credentials
from firebase import firebase as fb
from picamera.array import PiRGBArray
from picamera import PiCamera
import sys

# set up LED / breadboard / Raspberry Pi PINS
GPIO.setwarnings(False)
GPIO.setmode(GPIO.BCM)
GPIO.setup(18, GPIO.OUT)

# global variables / firebase paths
firebase = fb.FirebaseApplication('https://u-a3e12.firebaseio.com/', None)
firebase_mainuser_path = firebase.get('Main User', None)
user_id = list(firebase_mainuser_path.keys())[0]
led_path = '/Main User/' + user_id + '/LED/LED State/'
door_path = '/Main User/' + user_id + '/Door List/Door/'
voice_path = '/Main User/' + user_id + '/Voice Recognition/'
notification_path = '/Main User/' + user_id + '/Notifications/'
images_path = '/Main User/' + user_id + '/Images/Status/'
images_path_off = '/Main User/' + user_id + '/Images/'

# RecTrainer function to train the facial recognition
# after adding new user / phtotos of them
def recTrainer():
    
    # starting the xml file to train the recognizer with the photos
    faceCascade = cv2.CascadeClassifier("haarcascade_frontalface_default.xml")
    recognizer = cv2.face.LBPHFaceRecognizer_create()

    # paths to the saved photos in the local storage
    baseDir = os.path.dirname(os.path.abspath(__file__))
    imageDir = os.path.join(baseDir, "/home/pi/Desktop/CapstoneFaceRec/images")

    # variables
    currentId = 1
    labelIds = {}
    yLabels = []
    xTrain = []

    # trains the recognizer to recognize the faces produced from the xml file
    for root, dirs, files in os.walk(imageDir):
            print(root, dirs, files)
            for file in files:
                    print(file)
                    if file.endswith("png") or file.endswith("jpg"):
                            path = os.path.join(root, file)
                            label = os.path.basename(root)
                            print(label)

                            if not label in labelIds:
                                    labelIds[label] = currentId
                                    print(labelIds)
                                    currentId += 1

                            id_ = labelIds[label]
                            pilImage = Image.open(path).convert("L")
                            imageArray = np.array(pilImage, "uint8")
                            faces = faceCascade.detectMultiScale(imageArray, scaleFactor=1.1, minNeighbors=5)

                            for (x, y, w, h) in faces:
                                    roi = imageArray[y:y+h, x:x+w]
                                    xTrain.append(roi)
                                    yLabels.append(id_)

    # creates labels for the photos 
    with open("labels", "wb") as f:
            pickle.dump(labelIds, f)
            f.close()

    # trains recognizer, saves yml file and prints labels 
    recognizer.train(xTrain, np.array(yLabels))
    recognizer.save("trainer.yml")
    print(labelIds)


# voice recognition function
def voiceRec():

    # initital state of voiceRec function
    state = 0

    # Gets firebase database path with user id down to
    # the door's name given by user in the android application
    firebase_mainuser_path = firebase.get('Main User', None)
    user_id = list(firebase_mainuser_path.keys())[0] 
    final_path_to_phrase = '/Main User/' + user_id + '/Voice Recognition/Speech-to-text'
    firebase_phrase = firebase.get(final_path_to_phrase, None)

    # Initialize microphone to start up
    #device index for mic can change for unknown reasons, test indexes 0,1,2 if error: evice index out of range, etc
    audio_recognizer = sr.Recognizer()
    mic = sr.Microphone(device_index=2)

    # Listen to user speak into microphone
    with mic as source:
        user_recording = audio_recognizer.listen(source)

    # Let google voice recognize and convert audio to a string text
    try:
        google_translation = audio_recognizer.recognize_google(user_recording)
    except Exception as e:
        return 0

    # if google_translation is same as phrase in firebase then
    # change the switchState value in firebase to 1
    # This will unlock the door if the facial recognition is accepted as well
    if (google_translation == firebase_phrase):
        put_path = '/Main User/' + user_id + '/Voice Recognition/'
        firebase.put(put_path, 'switchState', 1)
        state = 1

    return state

# LED path state function on 1
def ledStatePathOn():

    led = 0
    
    # Gets path with user id from firebase database to the location of LED state
    # and then switches LED state to 1
    led_state = firebase.put(led_path, 'switchState', 1)
    GPIO.output(18, GPIO.HIGH)
    
    led = 1
    return led
    
# door path state function on 1
def doorStatePathOn():
    
    door = 0
    
    # Gets path with user id from firebase database to the location of door state
    # Must be only one user id on firebase database, switches door state to 1
    firebase.put(door_path, 'Door State', 1)
    
    door = 1
    return door

# voice path state function off 0
def statePathOff():
    
    state = 1
    
    # turn all states in firebase back to 0 (off)
    led_state = firebase.put(led_path, 'switchState', 0)
    door_state = firebase.put(door_path, 'Door State', 0)
    voice_state = firebase.put(voice_path, 'switchState', 0)
    notify_stateOff = firebase.put(notification_path, 'Unrecognized', 0)
    notify_state_open_door_off = firebase.put(notification_path, 'Opened Door', 0)

    # turn off LED / lock door
    GPIO.output(18, GPIO.LOW)

    state = 0
    return state

# notifications path state function on 1
def notifyOfIntruder():
    
    notifyIntruder = 0
    
    # change database to 1 to notify app of intruder
    notify_stateOn = firebase.put(notification_path, 'Unrecognized', 1)
    
    notifyIntruder = 1
    return notifyIntruder

# notifications path state function on 1
def notifyOfDoorOpened():
    
    notifyOpenDoor = 0
    
    # change database to 1 to notify app of their door being opened
    notify_stateOn = firebase.put(notification_path, 'Opened Door', 1)
    
    notifyOpenDoor = 1
    return notifyOpenDoor

# main 
if __name__=="__main__":
    
    # Gets path and reads from firebase database
    firebase = fb.FirebaseApplication('https://u-a3e12.firebaseio.com/', None)

    # get labels of names from local database
    with open('/home/pi/Desktop/CapstoneFaceRec/labels', 'rb') as f:
        dict = pickle.load(f)
        f.close()

        # initialize and start camera
        camera = PiCamera()
        # camera.close()
        # sometimes used when camera is started but commented out after 
        camera.resolution = (640, 480)
        camera.framerate = 30
        rawCapture = PiRGBArray(camera, size=(640, 480))
        #camera.close() sometimes used when camera is started but commented out after 

        # initialize and start facial recognizer
        faceCascade = cv2.CascadeClassifier("/home/pi/Desktop/CapstoneFaceRec/haarcascade_frontalface_default.xml")
        recognizer = cv2.face.LBPHFaceRecognizer_create()
        recognizer.read("/home/pi/Desktop/CapstoneFaceRec/trainer.yml")

        # font of 
        font = cv2.FONT_HERSHEY_SIMPLEX
        
        # for continuous camera capture "video"
        for frame in camera.capture_continuous(rawCapture, format="bgr", use_video_port=True):
            
            # frame and camera initialization
            frame = frame.array
            gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
            faces = faceCascade.detectMultiScale(gray, scaleFactor = 1.5, minNeighbors = 5)
            
            # camera gray initialization
            for (x, y, w, h) in faces:
                roiGray = gray[y:y+h, x:x+w]
                id_, conf = recognizer.predict(roiGray)

                # always watched in the firebase database so that if user indicates from
                # android app they need photos taken this will catch it and make the
                # camera start taking photos
                images_status = firebase.get(images_path, None)
                print(images_status)
                
                # camera starts taking photos and retrains the raspberry pi to
                # recognize the new users and saves the photos to the local directory
                if images_status == 1:
                    
                    # xml file
                    faceCascade = cv2.CascadeClassifier("haarcascade_frontalface_default.xml")
                    
                    # ask user for their name to be save for the pictures
                    name = input("What's his/her Name? ")
                    print(name)
                    dirName = "./images/" + name
                    print(dirName)
                    
                    # creates new directory by given name else name already exists
                    if not os.path.exists(dirName):
                            os.makedirs(dirName)
                            print("Directory Created")
                    else:
                            print("Name already exists")
                            sys.exit()
                    
                    # count = 1 to start counting the photos taken
                    count = 1
                    
                    # only 30 photos will be taken of user
                    if count > 30:
                            break
                        
                    # save photo as .jpg by name of user given and helps with recognition credintials
                    for (x, y, w, h) in faces:
                            roiGray = gray[y:y+h, x:x+w]
                            fileName = dirName + "/" + name + str(count) + ".jpg"
                            cv2.imwrite(fileName, roiGray)
                            cv2.imshow("face", roiGray)
                            cv2.rectangle(frame, (x, y), (x+w, y+h), (0, 255, 0), 2)
                            count += 1

                    cv2.imshow('frame', frame)
                    key = cv2.waitKey(1)
                    rawCapture.truncate(0)

                    if key == 27:
                            break
                     
                    # retrain the camera / raspberry pi to recognize saved users
                    recTrainer()    
                    firebase.put(images_path_off, 'Status', 0)
                    
                # always run this to detect faces    
                for name, value in dict.items():
                    if value == id_:
                        if conf <= 100:
                            
                            # print name and face recognized
                            print(name)
                            print("face recognized")
                            
                            # call door state path function to check if face is recognized
                            door_state = doorStatePathOn()
                            print(door_state)    
                            
                            # run voice rec code to check if voice command is recognized
                            time.sleep(2)
                            print("Start Speaking")
                            voice_state = voiceRec()
                            print(voice_state)
                            
                            # if voice and face recognition is good, switch LED switch state to 1
                            # to unlock the door and then lock door after 10 seconds
                            if door_state == voice_state:
                            
                                # call LED function and notify user on android app someone entered their home
                                led_state = ledStatePathOn()
                                notifyUser = notifyOfDoorOpened()
                                print(led_state)
                                print('User notified')
                                print(notifyUser)
                                
                                # let users get inside of house for 10 seconds
                                time.sleep(10)
                                
                                # switch all flags back to 0 after 30 seconds
                                statePathOff()
                                
                            # id door is unsuccessful but voice goes through or opposite,
                            # then alert user through notifications that there is an intruder
                            if door_state == 0 and voice_state == 1 or door_state == 1 and voice_state == 0:
                                notifyOfIntruder()
                                time.sleep(10)
                                statePathOff()

                            # use rectangle box on persons face through camera to help with facial recognition
                            cv2.rectangle(frame, (x, y), (x+w, y+h), (0, 255, 0), 2)
                            cv2.putText(frame, name + str(conf), (x, y), font, 2, (0, 0 ,255), 2,cv2.LINE_AA)
                        else:
                            #face is not rcognized, alert user
                            print(conf)
                            print('not recognized')
                    
            # cv2.imshow('frame', frame)
            key = cv2.waitKey(1)
            rawCapture.truncate(0)
            if key == 27:
                break
    
    #close camera, any windows that opened and exit once done    
    camera.close() 
    cv2.destroyAllWindows()
    
# exit
exit()
    






        
