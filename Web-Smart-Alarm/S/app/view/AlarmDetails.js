Ext.define('SmartAlarm.view.AlarmDetails',{
	extend: 'Ext.form.Panel',
	requires: ['Ext.form.FieldSet', 'Ext.ux.field.DateTimePicker', 'Ext.field.Toggle'],
	xtype: 'alarmdetails',

	config: {
		styleHtmlContent: true,
		scrollable: 'vertical',
		title: 'Alarm Details',
		id: 'alarmDetails'
	},

	initialize: function () {

		this.callParent(arguments);		

		var name = {

			xtype: 'textfield',
			name: 'alarmName',
			label: 'Alarm Name',
			id: 'alarmName'
			
		};

		var datetime = Ext.create('Ext.ux.field.DateTimePicker', {
         label: 'Date and time',
         id: 'dateTime',
          value: new Date()
     });



		var enable = {

			xtype: 'checkboxfield',
			name: 'isEnabled',
			label: 'Enabled',
			id: 'isEnabled'
			
		};

		var smartenable = {

			xtype: 'togglefield',
			name: 'isSmartEnabled',
			label: 'Smart Enabled',
			id: 'smartEnable'
			
		};

		var repeatWeekly = {

			xtype: 'checkboxfield',
			name: 'repeatWeekly',
			label: 'Repeat Weekly',
			id: 'repeatWeekly'
			
		};

		var destination = {

			xtype: 'textfield',
			name: 'destination',
			label: 'Smart Destination',
			id: 'destination',
			hidden: true
			
		};

		var devices = {

			xtype: 'textfield',
			name: 'devices',
			label: 'Smart Devices',
			id: 'devices',
			hidden: true
			
		};

		this.add({xtype: "fieldset", items: [name, datetime, enable, repeatWeekly, smartenable,  destination, devices]});
		this.setRecord(this.getRecord());
 		

    }

	


});