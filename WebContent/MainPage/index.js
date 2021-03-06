/**
 * This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs two steps:
 *      1. Use jQuery to talk to backend API to get the json data.
 *      2. Populate the data to correct html elements.
 */

/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */
function handleMovieResult(resultData) {
  console.log("handleStarResult: populating star table from resultData");

  // Populate the star table
  // Find the empty table body by id "star_table_body"
  let starTableBodyElement = jQuery("#movie_list_table_body");

  // Iterate through resultData, no more than 10 entries
  for (let i = 0; i < Math.min(resultData.length); i++) {
    // Concatenate the html tags with resultData jsonObject
    let rowHTML = "";
    rowHTML += "<tr>";
    rowHTML +=
      "<th>" +
      // Adding a link to the movie
      //'<a href="movie.html?id=' +
      //resultData[i]["movie_id"] +
      //'">' +
      resultData[i]["movie_title"] +
      //"</a>" +
      "</th>";
    rowHTML += "<th>" + resultData[i]["movie_year"] + "</th>";
    rowHTML += "<th>" + resultData[i]["movie_director"] + "</th>";

    //Splitting movie genres
    //rowHTML += "<th>" + resultData[i]["movie_genre"] + "</th>";
    rowHTML += "<th>";
    let genres = resultData[i]["movie_genre"];
    let genresArr = genres.split(",");
    for (let j in genresArr){
      rowHTML += "<p>" + genresArr[j] + "</p>";
    }
    rowHTML += "</th>";

    // Need a way to split this

    rowHTML += "<th>";
    let movieActors = resultData[i]["movie_actors"];
    let movieActorArr = movieActors.split(",");

    for (let j in movieActorArr) {
      rowHTML +=
        "<p>" +
        // Adding a link to the movie
        //'<a href="star.html?id=' +
        //actorIdArr[j] +
        //'">' +
        movieActorArr[j] +
        //"</a>" +
        "</p>";
    }
    rowHTML += "</th>";
    rowHTML += "<th>" + resultData[i]["movie_rating"] + "</th>";
    rowHTML +=
        "<th>" + '<a href=' + resultData[i]["movie_id"] + '"../AddedToCart.html?id="><button onclick="addToCart()">Add To Cart</button></a>' + "</th>";
    rowHTML += "</tr>";

    // Append the row created to the table body, which will refresh the page
    starTableBodyElement.append(rowHTML);
  }
}

/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */

// Makes the HTTP GET request and registers on success callback function handleMovieResult
jQuery.ajax({
  dataType: "json", // Setting return data type
  method: "GET", // Setting request method
  url: "api/mainPage", // Setting request url, which is mapped by MovieListServlet in MovieListServlet.java
  success: (resultData) => handleMovieResult(resultData), // Setting callback function to handle data returned successfully by the MovieListServlet
});
