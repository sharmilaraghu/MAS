Ext.define('SmartAlarm.view.AlarmList' , {
	extend: 'Ext.navigation.View',


	xtype: 'alarmlist',
	id: 'alarmlist',

	requires: ['SmartAlarm.store.Alarms', 'Ext.dataview.List', 'SmartAlarm.view.AlarmDetails' ],

	config: {

		
		title: 'Alarms',
		iconCls: 'time',
		scrollable: true,
		styleHtmlContent: true,
		id: 'navView',

		navigationBar: {
		
		docked: 'top',
		items: [{
			xtype: 'button',
			iconCls: 'add',
			align: 'right',
			ui: 'action',
			id: 'new-Button'

			},
			{
				xtype: 'button',
				iconCls: 'bookmarks',
				align: 'right',
				ui: 'action',
				id: 'save-Button',
				hidden: true

			},

			{
				xtype: 'button',
				iconCls: 'trash',
				align: 'right',
				ui: 'action',
				id: 'delete-Button',
				hidden: true	
			},

			{
				xtype : 'button',
				iconCls: 'locate',
				hidden: true,
				ui: 'action',
				id : 'locateButton',
				scope : 'this'
			}


			]

		},
		  
        items: {
        	title: 'Alarms',
        	xtype:  'list',
        	styleHtmlContent: true,
			itemTpl: '{alarmName} at {datetime}',
			store: 'Alarms',
			onItemDisclosure: true
			
		}



	}

});