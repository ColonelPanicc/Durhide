

function createMap() {
	var properties = {
		center:new google.maps.LatLng(35.9940,78.8986),
		zoom:5,
	};

	var map = new google.maps.Map(document.getElementById('googlemap'), properties);	
}

createMap();
