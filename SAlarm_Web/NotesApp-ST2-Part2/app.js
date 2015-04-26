Ext.application({
    name: "NotesApp",

    models: ["Note"],
    stores: ["Notes"],
    controllers: ["Notes"],
    views: ["NotesList", "NotesListContainer"],

    launch: function () {

        var notesListContainer = {
            xtype: "noteslistcontainer"
        };       

        Ext.Viewport.add(notesListContainer);
    }
});