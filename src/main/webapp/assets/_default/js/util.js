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
    },
    URL: {
        /**
         * Return an object populated with GET URL parameters
         *  The default URL to parse is {@code window.location}
         *  An optional URL to parse can be passed as function parameter
         *  If a parameter has a value -> value
         *  If a parameter has no value -> true
         *  If a parameter does not exists -> undefined
         * @example
         *  getParams().color -> red
         *  getParams().noValue -> true
         *  getParams().noExists -> undefined
         * @param url An optional url to parse
         * @author Carlo Corradini
         */
        getParams: function (url) {
            let queryString = url ? url.split("?")[1] : window.location.search.slice(1);
            let params = {};

            if (queryString) {
                queryString = queryString.split("#")[0];
                const arr = queryString.split("&");

                for (let i = 0; i < arr.length; i++) {
                    let a = arr[i].split("=");
                    let paramName = a[0];
                    let paramValue = typeof (a[1]) === "undefined" ? true : a[1];

                    if (typeof paramValue === "string") paramValue = paramValue.toLowerCase();

                    if (paramName.match(/\[(\d+)?\]$/)) {
                        const key = paramName.replace(/\[(\d+)?\]/, "");
                        if (!params[key]) params[key] = [];

                        if (paramName.match(/\[\d+\]$/)) {
                            const index = /\[(\d+)\]/.exec(paramName)[1];
                            params[key][index] = paramValue;
                        } else {
                            params[key].push(paramValue);
                        }
                    } else {
                        if (!params[paramName]) {
                            params[paramName] = paramValue;
                        } else if (params[paramName] && typeof params[paramName] === "string") {
                            params[paramName] = [params[paramName]];
                            params[paramName].push(paramValue);
                        } else {
                            params[paramName].push(paramValue);
                        }
                    }
                }
            }

            return params;
        },
        /**
         * Return the Base URL.
         * The URL to extract from can be passed as argument, otherwise {@code window.location.href}
         * is used instead.
         * @param url The URL to to get path from, leave empty if you want the current base url
         * @returns {string} The Base URL
         */
        getBaseUrl: function (url) {
            let queryString = url ? url : window.location.href;
            return queryString.substring(0, queryString.lastIndexOf("/")) + "/";
        }
    },
    ARRAY: {
        /**
         * Remove the {@code item} from the {@code array}.
         * If the {@code item} is not present in the {@code array} nothing will be touched.
         *
         * @param array The array to remove item from
         * @param item The item to remove from the array
         */
        remove: function (array, item) {
            const index = array.indexOf(item);

            if (index > -1) {
                array.splice(index, 1);
            }
        }
    },
    JSF: {
        /**
         * Return a correctly formatted JSF URL given {@code library} and relative {@code url}.
         *  If {@code library} is null | undefined or {@code url} is null | undefined the returned URL is undefined.
         *
         * @param library The JSF resource library
         * @param url The relative URL of the resource
         * @returns {string|undefined} The correctly formatted JSF URL for resources
         */
        resourceURL: function (library, url) {
            if (library === null || library === undefined
                || url === null || url === undefined) {
                return undefined;
            }
            return UTIL.STRING.format("{1}/javax.faces.resource{2}.xhtml?ln={3}", window.CONTEXT_PATH, url, library);
        }
    }
};