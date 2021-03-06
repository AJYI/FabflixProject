/*
 * This function is called by the library when it needs to lookup a query.
 *
 * The parameter query is the query string.
 * The doneCallback is a callback function provided by the library, after you get the
 *   suggestion list from AJAX, you need to call this function to let the library know.
 */
function handleLookup(query, doneCallback) {
    if(query.toString().length < 3){
        return;
    }
    console.log("autocomplete initiated")

    let storageSession = JSON.parse(sessionStorage.getItem("storedQuery"));
    if(storageSession == null){
        // Do nothing
    }
    else{
        let i;
        let storedQuery = JSON.parse(sessionStorage.getItem("storedQuery"));
        const keys = Object.keys(storedQuery);
        for (i = 0; i < keys.length; i++){
            if(query.toString() == keys[i]){
                console.log("Retrieving data from cache")
                console.log(storedQuery[query.toString()])
                doneCallback( { suggestions: storedQuery[query.toString()] } );
                return;
            }
        }
    }

    console.log("sending AJAX request to backend Java Servlet")
    // TODO: if you want to check past query results first, you can do it here

    // sending the HTTP GET request to the Java Servlet endpoint hero-suggestion
    // with the query data
    jQuery.ajax({
        "method": "GET",
        // generate the request url from the query.
        // escape the query string to avoid errors caused by special characters
        "url": "autocomplete?query=" + escape(query),
        "success": function(data) {
            // pass the data, query, and doneCallback function into the success handler
            handleLookupAjaxSuccess(data, query, doneCallback)
        },
        "error": function(errorData) {
            console.log("lookup ajax error")
            console.log(errorData)
        }
    })
}


/*
 * This function is used to handle the ajax success callback function.
 * It is called by our own code upon the success of the AJAX request
 *
 * data is the JSON data string you get from your Java Servlet
 *
 */
function handleLookupAjaxSuccess(data, query, doneCallback) {
    console.log("lookup ajax successful")

    // parse the string into JSON
    var jsonData = JSON.parse(data);

    //SessionStorage
    let storageSession = JSON.parse(sessionStorage.getItem("storedQuery"));
    if (storageSession === null){
        console.log("works");
        let storageData = {[query.toString()]: JSON.parse(data)};
        sessionStorage.setItem("storedQuery", JSON.stringify(storageData));
    }
    // If the stored query already exists
    else{
        let storage = JSON.parse(sessionStorage.getItem("storedQuery"));
        storage[query.toString()] = JSON.parse(data);
        sessionStorage.setItem('storedQuery', JSON.stringify(storage));
        //sessionStorage.setItem("storedQuery", storage+data);
    }
    console.log(jsonData)

    doneCallback( { suggestions: jsonData } );
}


/*
 * This function is the select suggestion handler function.
 * When a suggestion is selected, this function is called by the library.
 *
 * You can redirect to the page you want using the suggestion data.
 */
function handleSelectSuggestion(suggestion) {
    // TODO: jump to the specific result page based on the selected suggestion
    // "../movieList.html"
    location.href = "./MovieInformation/movie.html?id=" + suggestion["data"]["movieID"];
    //console.log("you select " + suggestion["value"] + " with ID " + suggestion["data"]["movieID"])

}


/*
 * This statement binds the autocomplete library with the input box element and
 *   sets necessary parameters of the library.
 *
 * The library documentation can be find here:
 *   https://github.com/devbridge/jQuery-Autocomplete
 *   https://www.devbridge.com/sourcery/components/jquery-autocomplete/
 *
 */
// $('#autocomplete') is to find element by the ID "autocomplete"
$('#autocomplete').autocomplete({
    // documentation of the lookup function can be found under the "Custom lookup function" section
    lookup: function (query, doneCallback) {
        handleLookup(query, doneCallback)
    },
    onSelect: function(suggestion) {
        handleSelectSuggestion(suggestion)
    },
    // set delay time
    deferRequestBy: 300,
    // there are some other parameters that you might want to use to satisfy all the requirements
    // TODO: add other parameters, such as minimum characters
});


/*
 * do normal full text search if no suggestion is selected
 */
function handleNormalSearch(query) {
    console.log("doing normal search with query: " + query);
    // TODO: you should do normal search here
}

// bind pressing enter key to a handler function
$('#autocomplete').keypress(function(event) {
    // keyCode 13 is the enter key
    if (event.keyCode == 13) {
        // pass the value of the input box to the handler function
        handleNormalSearch($('#autocomplete').val())
    }
})

// TODO: if you have a "search" button, you may want to bind the onClick event as well of that button