Ext.define('SmartAlarm.view.Main', {
    extend: 'Ext.tab.Panel',
    requires: [
    'SmartAlarm.view.AlarmList',
    'SmartAlarm.view.NotesListContainer',
    'SmartAlarm.view.IOT'
    ],

    xtype: 'main',
    config: {
        tabBarPosition: 'bottom',
        items: [
            {
                xtype: 'alarmlist'
            },
            {
                xtype: 'noteslistcontainer'
            },

            {
                xtype: 'iot'
            }

        ]
    }
});
