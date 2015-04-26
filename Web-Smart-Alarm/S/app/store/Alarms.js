Ext.define('SmartAlarm.store.Alarms', {
	extend: 'Ext.data.Store',
	requires: 'Ext.data.proxy.LocalStorage',

	config: {
		model: 'SmartAlarm.model.AlarmListItem',
		proxy: {
			type: 'localstorage',
			id: 'alarms-app-store'

		}

	}
});