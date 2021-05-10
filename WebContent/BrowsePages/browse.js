function handleGenreListResult(resultData) {
    console.log("handleStarResult: populating star table from resultData");

    // Populate the star table
    // Find the empty table body by id "star_table_body"
    let genreListBodyElement = jQuery("#genreListBody");

    let rowHTML = "";
    rowHTML += "<tr>";
    for (let i = 0; i < resultData.length; i++){
        if (i % 6 == 0){
            rowHTML += "</tr>";
            rowHTML += "<tr>";
        }
        rowHTML += "<th>";
        rowHTML += '<a href="BrowsePages/genreSearch.html?genre=' + resultData[i]["movie_genre"] +'">' + resultData[i]["movie_genre"] +'</a>';
        rowHTML += "</th>";
    }
    rowHTML += "</tr>";
    genreListBodyElement.append(rowHTML);

}

/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */

// Makes the HTTP GET request and registers on success callback function handleMovieResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/browseGenreList", // Setting request url, which is mapped by MovieListServlet in MovieListServlet.java
    success: (resultData) => handleGenreListResult(resultData), // Setting callback function to handle data returned successfully by the MovieListServlet
});
