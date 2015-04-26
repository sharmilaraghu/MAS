var temp1, temp2;
var latD;
var lngD;
var latS;
var lngS;
var tempTime;

Ext.define('SmartAlarm.controller.Main' , {
	extend: 'Ext.app.Controller',

	config: {
		refs: {

			alarm: 'alarmlist',
			alarmDetail: 'alarmdetails',
			alarmMap: 'alarmmap',
			addButton: '#new-Button',
			saveButton: '#save-Button',
			deleteButton: '#delete-Button',
			navView: '#navView',
			dateTime: '#dateTime',
			smartEnable: 'togglefield',
			destination: '#destination',
			devices: '#devices',
			isEnabled: 'checkboxfield',
			turnLightOn: '#turnLightOn',
			turnLightOff: '#turnLightOff',
			locate: '#locateButton',
			submitDest: '#submitDest'
		},
		control: {
			'list': {
				disclose: 'showDetails'

			},

			addButton: {
				tap: 'onNewAlarm'
			},

			navView:{
				pop: 'popFunction'
			},

			saveButton: {
				tap: 'onSaveButton'

			},

			deleteButton: {
				tap: 'onDeleteButton'

			},

			smartEnable: {
				change: 'onSmartToggle'

			},

			isEnabled: {
				check: 'onEnabled'

			},

			turnLightOn: {
				tap: 'onLightOn'

			},

			turnLightOff: {
				tap: 'onLightOff'

			},

			alarmMap: {
				painted: 'init'
			},

			alarm: {

				back : 'pressedBack'
			},

			locate: {
				tap: 'onLocate'

			},

			submitDest: {
				tap: 'onSubmitDest'
			}

	}


	},




	launch: function()
	{
		this.callParent(arguments);
		Ext.getStore('Alarms').load();
		console.log('launch');

	},

	pressedBack: function(t, eOpts) {
		temp2 = t;
		console.log(temp2.getActiveItem()._itemId);

		if(temp2.getActiveItem()._itemId == 'ext-list-1')
		{
			//console.log('Where I wanna be!');
		this.getAlarm().down('#new-Button').show();
		this.getAlarm().down('#save-Button').hide();
		this.getAlarm().down('#delete-Button').hide();
		this.getAlarm().down('#locateButton').hide();
		Ext.getCmp('floating').hide();
		}
		else if(temp2.getActiveItem()._itemId == 'alarmdetails')
		{
		this.getAlarm().down('#new-Button').hide();
		this.getAlarm().down('#save-Button').show();
		this.getAlarm().down('#delete-Button').show();
		this.getAlarm().down('#locateButton').hide();
		Ext.getCmp('floating').hide();
		}
		else
		{
		this.getAlarm().down('#new-Button').hide();
		this.getAlarm().down('#save-Button').hide();
		this.getAlarm().down('#delete-Button').hide();
		this.getAlarm().down('#locateButton').show();

		}



	},

	onLocate: function() {

		console.log("In this button")

		var locate = Ext.getCmp('locateButton');
		var floating = Ext.create('Ext.Panel', {
            items: [{
            	xtype: 'textfield',
			label: 'Dest',
			id: 'destinationText'

            },
            {
            	xtype: 'button',
            	text: 'Submit',
            	id: 'submitDest'

            }],
            id: 'floating',
           left: 0,
            padding: 10,
            width: '50%'
        }).showBy(locate);
        //var box = Ext.getBody().getBox(),
          //  size = floating.getSize();
        
       // floating.setPosition(box.right - size.width, box.bottom - size.height);


	},

/*	initialize: function() {
    geocoder = new google.maps.Geocoder();
    
  },

  codeAddress: function() {
    var address = Ext.getCmp('destinationText').getValue();
    geocoder.geocode( { 'address': address}, function(results, status) {
      if (status == google.maps.GeocoderStatus.OK) {
        map.setCenter(results[0].geometry.location);

        var marker = new google.maps.Marker({
            map: map,
            position: results[0].geometry.location
        });
        var latD = results[0].geometry.location.lat();
        var lngD = results[0].geometry.location.lng();

        console.log(latD);
        console.log(lngD);
      } else {
        alert("Geocode was not successful for the following reason: " + status);
      }
    });
  },*/

	onSubmitDest: function(){

		var geocoder;
		var map;
		
		geocoder = new google.maps.Geocoder();
		//var latlng = new google.maps.LatLng(-34.397, 150.644);
		map = Ext.getCmp('alarmMapId');
		map = map.getMap();

		var center = map.getCenter();
		//alert(center.lat() + ',' + center.lng());
		latS = center.lat();
		lngS = center.lng();

		//alert(center.formatted_address;

		var address = Ext.getCmp('destinationText').getValue();
	  	geocoder.geocode( { 'address': address}, function(results, status) {

    		//alert('Here ---')
      if (status == google.maps.GeocoderStatus.OK) {

      	
       
        latD = results[0].geometry.location.lat();
        lngD = results[0].geometry.location.lng();
        //alert(latD);
        //alert(lngD);

        var latlng = new google.maps.LatLng(latD, lngD);

        //alert('sent1')

        var mapOptions = {
			zoom: 11,
			center: latlng,
			mapTypeId: google.maps.MapTypeId.ROADMAP
		}

		

         map.setCenter(latlng);

        // alert('Here 2')

          var marker = new google.maps.Marker({
            map: map,
            position: latlng
        });

        
       
      } else {
        alert("Geocode was not successful for the following reason: " + status);
      }
    });


	  	Ext.Ajax.request({
   	 	url: 'https://maps.googleapis.com/maps/api/directions/json',
   	 	withCredentials: true,
   	 	useDefaultXhrHeader: false,
    	method: 'POST',                                              
    	scope:this,
    	params: {
        "origin": latS+','+lngS,
        "destination": latD+','+lngD
        
    	},
    	success: function(response){


    		console.log(response.responseText);
    		temp1 = response.responseText;


    	},                                    
    	failure: function(){console.log('failure');}
		});

	/*	var gcm = require('node-gcm');
	
		var message = new gcm.Message();

		var message = new gcm.Message({
		    delayWhileIdle: true,
		    timeToLive: 3,
		    data: { 
		        dest_lon: latD,
		        dest_lat: lngD,
		        src_lon: latS,
		        src_lat: lngS,
		        alarm_time: Ext.getCmp('dateTime').getValue(),
		        salarm_id: 1

		    }
		});

		var sender = new gcm.Sender('AIzaSyD0MXTmvoo0rzQg3fju6-I7SjxFFv0IBYs');
		var registrationIds = [];

		registrationIds.push('732911902235@gcm.googleapis.com');
		sender.send(message, registrationIds, 10, function (err, result) {
		  if(err) console.error(err);
		  else    console.log(result);
		});

	*/

	},

	init: function(){
    var me = this;
    var locationInput = document.getElementById('locationField');
 
    //Create new autocomplete object
    locationComplete = new google.maps.places.Autocomplete(locationInput);
 
    //Bias to users current location
    this.geolocate();
    var place;
 
    google.maps.event.addListener(locationComplete, 'place_changed', function() {
        place = locationComplete.getPlace();
        console.log(place.formatted_address); //address
        console.log(place.geometry.location.lat()); //latitude
        console.log(place.geometry.location.lng()); //longitude
    });
},


	geolocate: function() {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(function(position) {
      var geolocation = new google.maps.LatLng(
          position.coords.latitude, position.coords.longitude);
 
      locationComplete.setBounds(new google.maps.LatLngBounds(geolocation,
          geolocation));
    });
  }
},

	onMapRender: function(e) {
    var latLngCoordinates = new google.maps.LatLng(-33.8688, 151.2195);
    console.log("Here in map")
        marker = new google.maps.Marker({
            position: latLngCoordinates,
            animation: google.maps.Animation.DROP,
            map: this.getMymap().getMap()
        });

    this.getMymap().setMapCenter(latLngCoordinates);
},

	onLightOn: function()
	{

		console.log("Entered On button");
		Ext.Ajax.request({
   	 	url: 'http://143.215.128.120/apron/group/1/1?set=ON',
   	 	withCredentials: true,
   	 	useDefaultXhrHeader: false,
    	method: 'POST',                                              
    	scope:this,
    	success: function(){console.log('success');},                                    
    	failure: function(){console.log('failure');}
		});

	},

	onLightOff: function()
	{

		console.log("Entered Off button");
		Ext.Ajax.request({
   	 	url: 'http://143.215.128.120/apron/group/1/1?set=OFF',
   	 	withCredentials: true,
   	 	useDefaultXhrHeader: false,
    	method: 'POST',                                              
    	scope:this,
    	success: function(){console.log('success');},                                    
    	failure: function(){console.log('failure');}
		});


	},



	showDetails: function(list, record)
	{
		this.getAlarm().down('#new-Button').hide();
		this.getAlarm().down('#save-Button').show();
		this.getAlarm().down('#delete-Button').show();
		//console.log('ALARM NAME IS');
		//	console.log(record.data.alarmName);
		this.getAlarm().push({

			xtype: 'alarmdetails',
			record: record

});
	},


	onNewAlarm: function(){
		this.getAlarm().down('#new-Button').hide();
		this.getAlarm().down('#save-Button').show();
		
		var newAlarm = Ext.create("SmartAlarm.model.AlarmListItem", {

        alarmName: "",
        datetime: new Date(),
        isEnabled: "",
        isSmartEnabled: "",
        repeatWeekly: "",
        destination: "",
        devices: ""
    });

		this.getAlarm().push({xtype: 'alarmdetails', record: newAlarm});


	},

	popFunction: function(){
		
		if(this.getAlarm().getActiveItem()._itemId == 'ext-list-1')
		{
			console.log('Reached here 1');
		this.getAlarm().down('#new-Button').show();
		this.getAlarm().down('#save-Button').hide();
		this.getAlarm().down('#delete-Button').hide();
		Ext.getCmp('floating').hide();
		}
		else 
		{
			console.log('Reached here 2');
		this.getAlarm().down('#new-Button').hide();
		this.getAlarm().down('#save-Button').show();
		this.getAlarm().down('#delete-Button').show();
		Ext.getCmp('floating').hide();
		}

	},

	onSaveButton: function(){
		var alarmDetail = this.getAlarmDetail();
		var alarmStore = Ext.getStore('Alarms');
		var datetimepicker = this.getAlarmDetail().down('#dateTime').getValue();

		var newValues = alarmDetail.getValues();

		newValues.datetime = datetimepicker;

		console.log('HERE');

		console.log(newValues.datetime);



		var currentAlarm = alarmDetail.getRecord();

		console.log(newValues.alarmName);

		currentAlarm.set('alarmName', newValues.alarmName);

		console.log(currentAlarm.data.alarmName);

		currentAlarm.set('datetime', newValues.datetime);

		

		

		currentAlarm.set('isEnabled', newValues.isEnabled);

		currentAlarm.set('isSmartEnabled', newValues.isSmartEnabled);

		currentAlarm.set('repeatWeekly', newValues.repeatWeekly);

		currentAlarm.set('destination', newValues.destination);

		currentAlarm.set('devices', newValues.devices);

		//console.log('BEFORE CORDOVA SAVE');
		//	console.log(currentAlarm.getId());

		tempTime = currentAlarm.data.datetime;
		var n = currentAlarm.data.id.lastIndexOf('-');
		var newId = currentAlarm.data.id.substring(n+1);
		var oneHourId = newId + 1;
		var updatedId = newId + 2;
		//console.log('PLEASE');

		console.log(newId);

		if(currentAlarm.data.repeatWeekly == 'Y'){

			var k = "week";

		}

	if(currentAlarm.data.isSmartEnabled == 1)
	{

		console.log("Entered AJAX");
		Ext.Ajax.request({
   	 	url: 'http://dev.m.gatech.edu/developer/gsingh62/api/smartclock/smartclock',
   	 	withCredentials: true,
   	 	useDefaultXhrHeader: false,
    	method: 'POST',                                              
    	params: {
        "id" : 8,
        "name": currentAlarm.data.alarmName,
        "list_id": 1,
        "time_setting": tempTime,
        "repeats": currentAlarm.data.repeatWeekly,
        "smart_feature_on": currentAlarm.data.isSmartEnabled
    	},
    	scope:this,
    	success: function(){console.log('success');},                                    
    	failure: function(){console.log('failure');}
});


	}

		if(null == alarmStore.findRecord('id', currentAlarm.data.id)){

				alarmStore.add(currentAlarm);


		//at alarm time
		var subbed = new Date(currentAlarm.data.datetime - 5*60*1000);
			  	var subbed_new = new Date(tempTime - 3*60*1000);

		cordova.plugins.notification.local.schedule([{
				id: newId,
				text: 'Alarm Scheduled',
				at: tempTime,
				every: k,
				led: "FF0000",
				sound: 'file://sound.mp3'

		},
		{
				id: oneHourId,
				text: 'Alarm Scheduled',
				at: subbed,


		},
		{
				id: updatedId,
				text: 'Updated Alarm Scheduled',
				at: subbed_new,
				every: k,
				led: "FF0000",
				sound: 'file://sound.mp3'

		}

		]);



		cordova.plugins.notification.local.on("schedule", function(notification){

				//alert("Scheduled "+currentAlarm.data.alarmName);


		});

		cordova.plugins.notification.local.on("trigger", function(notification){

				alert("ALARM! ");
				var cur = new Date()


			if(cur >= subbed && cur < subbed_new )
			{

		Ext.Ajax.request({
   	 	url: 'https://maps.googleapis.com/maps/api/directions/json',
   	 	withCredentials: true,
   	 	useDefaultXhrHeader: false,
    	method: 'POST',                                              
    	scope:this,
    	params: {
        "origin": latS+','+lngS,
        "destination": latD+','+lngD
        
    	},
    	success: function(response){

    		temp1 = response.responseText;
    		

    	},                                    
    	failure: function(){console.log('failure');}
		});


			}



			else if(cur >= subbed_new )
				{
				Ext.Ajax.request({
   	 	url: 'http://143.215.128.120/apron/group/1/1?set=ON',
   	 	withCredentials: true,
   	 	useDefaultXhrHeader: false,
    	method: 'POST',                                              
    	scope:this,
    	success: function(){console.log('success');},                                    
    	failure: function(){console.log('failure');}
		});

			}

			/*	if(cur == tempTime)
				{


				}
				else if (cur == subbed)
				{



		Ext.Ajax.request({
   	 	url: 'https://maps.googleapis.com/maps/api/directions/json',
   	 	withCredentials: true,
   	 	useDefaultXhrHeader: false,
    	method: 'POST',                                              
    	scope:this,
    	params: {
        "origin": latS+','+lngS,
        "destination": latD+','+lngD
        
    	},
    	success: function(response){


    		console.log(response.responseText);
    		temp1 = response.responseText;
    		alert(response.responseText);

    	},                                    
    	failure: function(){console.log('failure');}
		});


				}
				else if (cur == subbed_new)
				{

					alert('HERE!!')

			

			}*/




	

		/*cordova.plugins.notification.local.on("trigger", function(notification){

				//Make ajax call

	  	Ext.Ajax.request({
   	 	url: 'https://maps.googleapis.com/maps/api/directions/json',
   	 	withCredentials: true,
   	 	useDefaultXhrHeader: false,
    	method: 'POST',                                              
    	scope:this,
    	params: {
        "origin": latS+','+lngS,
        "destination": latD+','+lngD
        
    	},
    	success: function(response){


    		console.log(response.responseText);
    		temp1 = response.responseText;
    		alert(response.responseText);

    	},                                    
    	failure: function(){console.log('failure');}
		});
*/




	/*	cordova.plugins.notification.local.clear(newId, function(){
			//alert("Deleted alarm!");

		});
		cordova.plugins.notification.local.schedule({
				id: newId,
				text: 'Updated Alarm Scheduled',
				at: subbed_new,
				every: k,
				led: "FF0000",
				sound: 'file://sound.mp3'

		});

		cordova.plugins.notification.local.on("schedule", function(notification){

				//alert("Triggered traffic aware alarm Scheduled "+subbed_new);


		});

		cordova.plugins.notification.local.on("trigger", function(notification){

			//alert("Triggered traffic aware alarm at"+subbed_new);
			alert("ALARM! "+subbed_new);


				Ext.Ajax.request({
		   	 	// url: 'http://192.168.1.4/apron/group/1/1?set=ON',
		   	 	url: 'http://143.215.128.120/apron/group/1/1?set=ON',

		   	 	withCredentials: true,
		   	 	useDefaultXhrHeader: false,
		    	method: 'POST',                                              
		    	params: {
		        
		    	},
		    	scope:this,
		    	success: function(){console.log('success');},                                    
		    	failure: function(){console.log('failure');}

		});




		});*/


				
		});


	}

	else {

		/*cordova.plugins.notification.local.update({

			id: newId,
			text: "Updated notification",
			at: currentAlarm.data.datetime


		});

		cordova.plugins.notification.local.on("update", function(notification){

			alert('Updated to time'+currentAlarm.data.datetime);
		}); */
		var n = currentAlarm.data.id.lastIndexOf('-');
		var newId = currentAlarm.data.id.substring(n+1);

		cordova.plugins.notification.local.clear(newId, function(){
			//alert("Deleted alarm!");

		});
		cordova.plugins.notification.local.schedule({
				id: newId,
				text: 'Updated Alarm Scheduled',
				at: currentAlarm.data.datetime,
				every: k,
				led: "FF0000",
				sound: 'file://sound.mp3'

		});

		cordova.plugins.notification.local.on("schedule", function(notification){

				alert("Updated Alarm Scheduled "+currentAlarm.data.alarmName);


		});

		cordova.plugins.notification.local.on("trigger", function(notification){


			Ext.Ajax.request({
		   	 	// url: 'http://192.168.1.4/apron/group/1/1?set=ON',
		   	 	url: 'http://143.215.128.120/apron/group/1/1?set=ON',

		   	 	withCredentials: true,
		   	 	useDefaultXhrHeader: false,
		    	method: 'POST',                                              
		    	params: {
		        
		    	},
		    	scope:this,
		    	success: function(){console.log('success');},                                    
		    	failure: function(){console.log('failure');}
				
		});


		});

		



	}




		alarmStore.sync();
		this.getAlarm().down('locateButton').hide();
		this.getAlarm().pop();



	},

	onDeleteButton: function(){
	
		var alarmDetail = this.getAlarmDetail();	
		var alarmStore = Ext.getStore('Alarms');
		
		var newValues = alarmDetail.getValues();


		var currentAlarm = alarmDetail.getRecord();

		//console.log(newValues.alarmName);

		currentAlarm.set('alarmName', newValues.alarmName);

		//console.log(currentAlarm.data.alarmName);

		currentAlarm.set('datetime', newValues.datetime);


		currentAlarm.set('isEnabled', newValues.isEnabled);

		currentAlarm.set('isSmartEnabled', newValues.isSmartEnabled);

		currentAlarm.set('repeatWeekly', newValues.repeatWeekly);

		currentAlarm.set('destination', newValues.destination);

		currentAlarm.set('devices', newValues.devices);

		console.log(currentAlarm.data.id);


		if(null != alarmStore.findRecord('id', currentAlarm.data.id)){

			console.log('Found the id in the data store');
			alarmStore.remove(currentAlarm);

		var n = currentAlarm.data.id.lastIndexOf('-');
		var newId = currentAlarm.data.id.substring(n+1);

		cordova.plugins.notification.local.clear(newId, function(){
			alert("Deleted alarm!");

		});

		//cordova.plugins.notification.local.on("clear", function(notification){
			//alert("Deleted!");

			//});




		}	

			alarmStore.sync();


			this.getAlarm().pop();

	},

	onSmartToggle: function(slider, newValue, oldValue, thumb)
	{

		console.log('Getting called');
		this.getAlarm().down('#locateButton').show();
		this.getAlarm().down('#save-Button').hide();
		this.getAlarm().push({xtype: 'alarmMap'});
		 


	//	if(oldValue==0 && newValue==1)
	//	{
			
	//		this.getAlarmDetail().down('#destination').show();
	//		this.getAlarmDetail().down('#devices').show();


			/*Ext.util.JSONP.request({
 		 	url: 'https://maps.googleapis.com/maps/api/',
  			callbackKey: 'callback',
			  params: { foo:'value, foo1:'value },
			  callback: function( data ) { console.log(data); }
			});*/

		

	//	}

		/*if(oldValue==1 && newValue==0)
		{
			
			this.getAlarmDetail().down('#destination').hide();
			this.getAlarmDetail().down('#devices').hide();

		}*/



	},

	onEnabled: function(checkbox, newVal, oldVal)
	{
		var alarmDetail = this.getAlarmDetail();	
		var alarmStore = Ext.getStore('Alarms');
		
		var newValues = alarmDetail.getValues();


		var currentAlarm = alarmDetail.getRecord();

		//console.log(newValues.alarmName);

		currentAlarm.set('alarmName', newValues.alarmName);

		//console.log(currentAlarm.data.alarmName);

		currentAlarm.set('datetime', newValues.datetime);


		currentAlarm.set('isEnabled', newValues.isEnabled);

		currentAlarm.set('isSmartEnabled', newValues.isSmartEnabled);

		currentAlarm.set('repeatWeekly', newValues.repeatWeekly);

		currentAlarm.set('destination', newValues.destination);

		currentAlarm.set('devices', newValues.devices);

		if(null != alarmStore.findRecord('id', currentAlarm.data.id) && oldVal == 1 && newVal == 0)
		{


		var n = currentAlarm.data.id.lastIndexOf('-');
		var newId = currentAlarm.data.id.substring(n+1);

		cordova.plugins.notification.local.cancel(newId, function(){
			alert("Canceled!");

		});

		cordova.plugins.notification.local.on("cancel", function(notification){
			alert("Canceled!");

		});

		}

	}




	
});