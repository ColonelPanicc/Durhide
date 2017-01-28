from django.conf.urls import url
from rest_framework import routers
from cameras.views import CameraViewSet

router = routers.DefaultRouter()
router.register(r'camera', CameraViewSet)

urlpatterns = router.urls
