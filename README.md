# Durhack
Our Durhack Project

## Goals:
+ Android App
+ Some backend to integrate (through REST api)

Using CCTV Camera data, and make a game.

### Mechanics:
	+ Avoid cameras
	+ Use GPS to get location
	+ Google Play Games (with achievements)
	+ Longest distances (how long can you survive?)

## Backend (Local Development)
Django based backend. Navigate to the backend folder, and activate the virtual environment using:

```bash
source venv/bin/activate
```
This works on Ubuntu.

Then navigate to the durhack folder, and you can start the server using:

```bash
python3 manage.py runserver
```

Username: admin
Password: longpassword

### Subdirectories:
#### Cameras
This creates the model for each camera and registers it in the backend
#### UserSettings
This adds the user settings to the database

