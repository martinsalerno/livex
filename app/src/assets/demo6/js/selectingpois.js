var World = {
	isRequestingData: false,

	initiallyLoadedData: false,

	markerDrawable_idle: null,
	markerDrawable_selected: null,
	markerDrawable_directionIndicator: null,

	markerList: [],

	currentMarker: null,

	loadPoisFromJsonData: function loadPoisFromJsonDataFn(poiData) {
	    World.markerList = [];
		World.markerDrawable_idle = new AR.ImageResource("assets/marker_idle.png");
		World.markerDrawable_selected = new AR.ImageResource("assets/marker_selected.png");
		World.markerDrawable_directionIndicator = new AR.ImageResource("assets/indi.png");


		for (var currentPlaceNr = 0; currentPlaceNr < poiData.length; currentPlaceNr++) {
			var singlePoi = {
				"id": poiData[currentPlaceNr].id,
				"latitude": parseFloat(poiData[currentPlaceNr].latitude),
				"longitude": parseFloat(poiData[currentPlaceNr].longitude),
				"altitude": parseFloat(poiData[currentPlaceNr].altitude),
				"title": poiData[currentPlaceNr].name,
				"description": poiData[currentPlaceNr].description
			};

			/*
				To be able to deselect a marker while the user taps on the empty screen,
				the World object holds an array that contains each marker.
			*/
			var marker = new Marker(singlePoi);
			World.markerList.push(marker);
			marker.setSelected(marker);
		}
		World.updateStatusMessage(poiData[0].name);
		PoiRadar.show();
	},

    loadPois: function loadPoisFn(poi) {
    	var poiData = [];
    		poiData.push({
    				"id": poi.id,
    				"longitude": poi.longitude,
    				"latitude": poi.latitude,
    				"description": poi.description,
    				"altitude": AR.CONST.UNKNOWN_ALTITUDE,
    				"name": poi.name
    			});
    		World.loadPoisFromJsonData(poiData);
    },

    setMessage: function setMessageFn(text) {
    		World.updateStatusMessage(text);
    },


	// updates status message shown in small "i"-button aligned bottom center
	updateStatusMessage: function updateStatusMessageFn(message, isWarning) {

		var themeToUse = isWarning ? "e" : "c";
		var iconToUse = isWarning ? "alert" : "info";

		$("#status-message").html(message);
		$("#popupInfoButton").buttonMarkup({
			theme: themeToUse
		});
		$("#popupInfoButton").buttonMarkup({
			icon: iconToUse
		});
	},

	// location updates, fired every time you call architectView.setLocation() in native environment
	locationChanged: function locationChangedFn(lat, lon, alt, acc) {

		/*
			The custom function World.onLocationChanged checks with the flag World.initiallyLoadedData if the function was already called. With the first call of World.onLocationChanged an object that contains geo information will be created which will be later used to create a marker using the World.loadPoisFromJsonData function.
		*/
		if (!World.initiallyLoadedData) {
			/*
				requestDataFromLocal with the geo information as parameters (latitude, longitude) creates different poi data to a random location in the user's vicinity.
			*/
			// World.requestDataFromLocal(lat, lon);
			World.initiallyLoadedData = true;
		}
	},

	// fired when user pressed maker in cam
	onMarkerSelected: function onMarkerSelectedFn(marker) {

		// deselect previous marker
		if (World.currentMarker) {
			if (World.currentMarker.poiData.id == marker.poiData.id) {
				return;
			}
			World.currentMarker.setDeselected(World.currentMarker);
		}

		// highlight current one
		marker.setSelected(marker);
		var distanceToUserValue = (marker.markerObject.locations[0].distanceToUser() > 999) ? ((marker.markerObject.locations[0].distanceToUser() / 1000).toFixed(2) + " km") : (Math.round(marker.markerObject.locations[0].distanceToUser()) + " m");
		World.updateStatusMessage("Distancia: " + distanceToUserValue);
		World.currentMarker = marker;
	},

	onScreenClick: function onScreenClickFn() {
		if (World.currentMarker) {
			World.currentMarker.setDeselected(World.currentMarker);
		}
	},

};

AR.context.onLocationChanged = World.locationChanged;
AR.context.scene.minScalingDistance = 10;
AR.context.scene.maxScalingDistance = 2000;
AR.context.scene.scalingFactor = 0.2;
AR.context.onScreenClick = World.onScreenClick;