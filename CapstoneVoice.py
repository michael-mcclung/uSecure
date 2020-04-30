import speech_recognition as sr
import pyrebase
import firebase_admin
from firebase_admin import credentials
from firebase import firebase
import pprint

# Gets path and reads from firebase database
firebase = firebase.FirebaseApplication('https://u-a3e12.firebaseio.com/', None)

# Gets path with user id from firebase database
# Must be only one user id on firebase database
firebase_mainuser_path = firebase.get('Main User', None)
user_id = list(firebase_mainuser_path.keys())[0]
final_path_to_phrase = '/Main User/' + user_id + '/Voice Recognition/Door Name'
firebase_phrase = firebase.get(final_path_to_phrase, None)

# Initialize microphone to start up
audio_recognizer = sr.Recognizer()
mic = sr.Microphone(device_index=2)

# Listen to user speak into microphone
with mic as source:
    user_recording = audio_recognizer.listen(source)

# Let google voice recognize and convert audio to a string text
google_translation = audio_recognizer.recognize_google(user_recording)
print(google_translation)

# if google_translation is same as phrase in firebase then
# change the switchState value in firebase to 1
# This will unlock the door if the facial recognition is
# accepted as well
if (google_translation == firebase_phrase):
    put_path = '/Main User/' + user_id + '/Voice Recognition/'
    firebase.put(put_path, 'switchState', 1)
    
    
    