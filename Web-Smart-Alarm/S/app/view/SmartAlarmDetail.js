Ext.define('SmartAlarm.view.SmartAlarmDetail', {
	extend: 'Ext.Panel',
	xtype: 'homepanel',

	config: {
		title: 'Home',
		scrollable: true,
		styleHtmlContent: true,

		 html: [
                '<img src="http://staging.sencha.com/img/sencha.png"><br/>',
                '<h1>Welcome to Sencha Touch</h1>',
                "<p>We're creating the Getting Started app, which demonstrates how ",
                "to use tabs, lists, and forms to create a simple app.</p>",
                '<h2>Sencha Touch (2.0.0)</h2>'
            ].join("")

	}

});