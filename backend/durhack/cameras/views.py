from rest_framework import viewsets
from cameras.models import Camera
from cameras.serializers import CameraSerializer

# Create your views here.
class CameraViewSet(viewsets.ModelViewSet):
    queryset = Camera.objects.all()
    serializer_class = CameraSerializer
