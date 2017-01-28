from django.shortcuts import render

# Create your views here.

def index(request):
	context = {
		'pagename': "View Cameras",
	}
	return render(request,'webclient/index.html', context)
