/**
 * This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs two steps:
 *      1. Use jQuery to talk to backend API to get the json data.
 *      2. Populate the data to correct html elements.
 */

// Need star name, star's birth year, and list of movies performed
function getParameterByName(target) {
    // Get request URL
    let url = window.location.href;
    // Encode target parameter name to url encoding
    target = target.replace(/[\[\]]/g, "\\$&");

    // Ues regular expression to find matched parameter value
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';

    // Return the decoded parameter value
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */
function handleStarResult(resultData) {
    //Setting the title page
    let starTitleElement = jQuery("#star_title_page");
    starTitleElement.append(resultData[0]["star_name"]);

    //Setting the h1 element for the page
    let starNameElement = jQuery("#star_name");
    starNameElement.append(resultData[0]["star_name"]);

    console.log("handleStarResult: populating star table from resultData");

    let starTableBodyElement = jQuery("#star_table_body");

    // Begin pulling and printing out data
    rowHTML = "";

    if(resultData[0]["star_birth_year"] == null){
        resultData[0]["star_birth_year"] = "N/A";
    }

    rowHTML += "<th> " + resultData[0]["star_birth_year"] + "</th>"


    let starMovies = resultData[0]["movies"];
    let starMoviesArray = starMovies.split('|');

    let movieID = resultData[0]["movie_id"];
    let moviesIDArray = movieID.split('|');

    rowHTML += "<th>";
    for (let j in starMoviesArray) {
        rowHTML +=
            "<p>" +
            '<a href="movie.html?id=' + moviesIDArray[j] + '">' + starMoviesArray[j] + '</a>' +
            "</p>";
    }

    rowHTML += "</th>";
    rowHTML += "</tr>";
    // Append the row created to the table body, which will refresh the page
    starTableBodyElement.append(rowHTML);
}


/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */
let starID = getParameterByName('id');
// Makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/star?id=" + starID, // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleStarResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});