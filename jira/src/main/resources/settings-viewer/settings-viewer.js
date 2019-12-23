define('echeveria/settings/viewer', [
    'echeveria/dialog',
    'jira/util/events/reasons',
    'jira/util/events/types',
    'jira/util/events',
    'jquery'
], function (EcheveriaDialog, Reasons, Types, Events, $) {
    'use strict';

    var SettingsViewerDialog = EcheveriaDialog.extend({

        init: function init(options) {
            var defaults = {
                _config: {
                    dialog: '#settings-viwer-dialog'
                }
            };

            options = $.extend(true, defaults, options);

            this._super(options);
        },

        _getHeaderText: function _getHeaderText () {
            return 'Settings Viewer';
        },

    });

    Events.bind(Types.NEW_CONTENT_ADDED, function (e, context, reason) {
        if (reason !== Reasons.panelRefreshed) {

            // Initialize the dialog
            var settingsViewerDialog = new SettingsViewerDialog();

            // Set the button to open the dialog
            $('#open-viewer-button').click(function (e) {
                settingsViewerDialog.show();
            });

        }
    });

});