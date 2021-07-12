define('echeveria/dialog', [
    'jira/ajs/control',
    'jquery',
    'underscore'
], function (Control, $, _) {
    'use strict';

    return Control.extend({

        init: function init (options) {
            this._setOptions(options);

            this.initElements();
        },

        initElements: function initElements () {
            var _config = this.options._config;

            this.ajsDialog = AJS.dialog2(_config.dialog);
            this.$dialog = $(_config.dialog);
            this.$header = $(_config.header());
            this.$submit = $(_config.submit());
            this.$cancel = $(_config.cancel());

            this.$header.html(this._getHeaderText());

            var instance = this;
            this.$cancel.click(function (e) {
                instance.hide();
            });
        },

        _getHeaderText: function _getHeaderText () {
            return 'Override _getHeaderText() to set header';
        },

        show: function show () {
            this.ajsDialog.show();
        },

        hide: function hide() {
            this.ajsDialog.hide();
        },

        _getDefaultOptions: function _getDefaultOptions () {
            return {
                _config: {
                    dialog: '',
                    header: function () {
                        return this.dialog + ' .aui-dialog2-header-main';
                    },
                    submit: function () {
                        return this.dialog + ' #dialog-submit-button';
                    },
                    cancel: function () {
                        return this.dialog + ' #dialog-cancel-button';
                    }
                }
            };
        },

    });

});