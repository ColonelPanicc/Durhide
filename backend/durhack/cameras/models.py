""" Camera Models (i.e. just the camera database) """

from django.db import models

# Create your models here.

class Camera(models.Model):
    """ Camera Model for our Database """
    Location = models.CharField(max_length=100)
    Long = models.FloatField()
    Lat = models.FloatField()
    ImgLink = models.URLField()
    LatRange1 = models.FloatField()
    LongRange1 = models.FloatField()
    LatRange2 = models.FloatField()
    LongRange2 = models.FloatField()

    def __str__(self):
        return self.Location
