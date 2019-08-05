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
    },
    NUMBER: {
        /**
         * Given a {@code n} value che if it's a Number
         * @example
         *  isNumber(23.4);
         *  isNumber(7);
         * @param n The value to check
         * @returns {boolean} True if {@code n} is a Number, false otherwise
         * @author Carlo Corradini
         */
        isNumber: function (n) {
            return n !== null && n !== undefined && !isNaN(+n) && isFinite(n);
        }
    }
};