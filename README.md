![alt text](https://github.com/OhmGeek/Durhack/blob/master/img/banner.png "Banner")

# Durhide
Durhide is an app combining government traffic camera data with a visual map, to show the locations of these cameras, live feeds, and notifications when you are passing them. Think of this as an app both for governments and those who fear 'Big Brother' (not the TV show, although that's equally as scary).

This was created over a period of 24 hours, as part of Durhack 2016.

## Backend
The Backend API is deployed on Heroku, running Django. The Django site requires PostgreSQL. Unless you want to host your own service, this shouldn't concern you.

The API provides camera parameters, such as location and field of view.

## Android App
The Android app supports Android 19, although we recommend Android 25 (Nougat), as that's what we have been testing with.
The APK must be signed to allow for Google Play Services to work correctly.

The app is available for Android [here](https://play.google.com/store/apps/details?id=co.brookesoftware.mike.smilingpooemoji).

# Data
The data used for this project can be found at [Data Mill North](https://datamillnorth.org/dataset/traffic-web-cameras).

Since we created this project, the image quality of the cameras has improved. This could yield further improvements, so if you're interested, do submit a pull request to make the app more functional/fun!
