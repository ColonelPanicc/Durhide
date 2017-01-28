function getCamera() {
	var url = "http://localhost:8000/api/cameras/camera/";
	$.getJSON(url,function(data) {
		data.forEach(function(element) {
			var marker = new google.maps.Marker({
				position: {lat: element.Lat, lng: element.Long},
				map: document.getElementById('googlemap'),
				title: element.Location,
			});
		});
	});

}
