Ext.define('SmartAlarm.view.IOT' , {
	extend: 'Ext.Panel',

	xtype: 'iot',

	config: {
		title: 'Devices',
		iconCls: 'star',
		scrollable: true,
		styleHtmlContent: true,

		items: [{
		xtype: 'titlebar',
 		docked: 'top',
 		title: 'Devices'
 		},
		
		
		
		                {
		                    xtype: 'button',
		                    //centered: true,
		                    id: 'turnLightOn',
		                    height: 10,
		                    width: 50,
		                    iconMask: true,
		                    text: 'ON',
							ui: 'action'
		                }, {
		                    xtype: 'button',
		                    //centered: true,
		                    id: 'turnLightOff',
		                    height: 10,
		                    width: 50,
		                    iconMask: true,
		                    text: 'OFF',
							ui: 'action'
		                }]
		

	}
	


});