Ext.define('SmartAlarm.view.TaskList' , {
	extend: 'Ext.Panel',

	xtype: 'tasklist',

	config: {
		title: 'Tasks',
		iconCls: 'compose',
		scrollable: true,
		styleHtmlContent: true,
		items: {
		xtype: 'titlebar',
 		docked: 'top',
 		title: 'List of Tasks'
 		},
		html: "TaskList"

	}



});