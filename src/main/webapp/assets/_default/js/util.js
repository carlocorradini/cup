"use strict";

/**
 * UTIL functions and methods
 */
window.UTIL = {
    /**
     * STRING util
     */
    STRING: {
        /**
         * Given a String pattern with {%d}, replace it with values passed as arguments
         * @example
         *  format("My name is {1} {2}", "Carlo", "Corradini");
         * @returns {string} The formatted String
         * @author Carlo Corradini
         */
        format: function () {
            const args = [].slice.call(arguments);
            if (this.toString() !== "[object Object]") {
                args.unshift(this.toString());
            }
            const pattern = new RegExp("{([1-" + args.length + "])}", "g");
            return String(args[0]).replace(pattern, function (match, index) {
                return args[index];
            });
        }
    }
};