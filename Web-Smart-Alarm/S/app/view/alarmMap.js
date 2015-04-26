  Ext.define('SmartAlarm.view.alarmMap' , {
	extend: 'Ext.Panel',
	xtype: 'alarmmap',
	alias: "widget.alarmMap",

	requires: ['Ext.Map', 'Ext.util.Geolocation' ],

	config: {

		layout: 'card',
    id: 'alarmMap',

		items: 	
            [   {
                    xtype: 'map',
                    id : 'alarmMapId',
                    useCurrentLocation: true,
                   	mapOptions:{
                   		zoom: 20,
                   		mapTypeId: google.maps.MapTypeId.ROADMAP
               		}
                 }
                 

                     

           	]
        }


});