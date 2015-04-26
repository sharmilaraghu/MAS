Ext.define('SmartAlarm.model.AlarmListItem', {
	extend: 'Ext.data.Model',

	config: {


		

		fields: [{name: 'id', type:'int'},'alarmName', 'datetime', 'isEnabled', 'isSmartEnabled', 'repeatWeekly', 'destination', 'devices'],

		

	},

	



});