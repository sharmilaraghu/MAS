Ext.define("SmartAlarm.view.NotesListContainer", {
    extend: "Ext.Container",
    xtype: "noteslistcontainer",

    alias: "widget.noteslistcontainer",
    

    requires: ['SmartAlarm.store.Notes', 'Ext.dataview.List', 'SmartAlarm.view.NotesList', 'SmartAlarm.view.NoteEditor' ],

   

    initialize: function () {

        this.callParent(arguments);

        var newButton = {
            xtype: "button",
            text: 'New',
            ui: 'action',
            handler: this.onNewButtonTap,
            scope: this
        };

        var topToolbar = {
            xtype: "toolbar",
            title: 'Task List',
            cls: 'list-item-title',
            docked: "top",
            items: [
                { xtype: 'spacer' },
                newButton
            ]
        };

        var notesList = {
            xtype: "noteslist",
            store: Ext.getStore("Notes"),
            listeners: {
                disclose: { fn: this.onNotesListDisclose, scope: this }
            }
        };

        this.add([topToolbar, notesList]);
    },
    onNewButtonTap: function () {
        console.log("newNoteCommand");
        this.fireEvent("newNoteCommand", this);
    },
    onNotesListDisclose: function (list, record, target, index, evt, options) {
        console.log("editNoteCommand");
        this.fireEvent('editNoteCommand', this, record);
    },
    config: {
        layout: {
            type: 'fit'
        },
         title: 'Tasks',
        iconCls: 'compose',
        scrollable: true,
        styleHtmlContent: true,
        html: "TaskList"
    }
});