/**
 * This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs two steps:
 *      1. Use jQuery to talk to backend API to get the json data.
 *      2. Populate the data to correct html elements.
 */

function getParameterByName(target) {
  // Get request URL
  let url = window.location.href;
  // Encode target parameter name to url encoding
  target = target.replace(/[\[\]]/g, "\\$&");

  // Ues regular expression to find matched parameter value
  let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
    results = regex.exec(url);
  if (!results) return null; // Changed from null
  if (!results[2]) return "";

  // Return the decoded parameter value
  return decodeURIComponent(results[2].replace(/\+/g, " "));
}

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
      '<a href="movie.html?id=' +
      resultData[i]["movie_id"] +
      '">' +
      resultData[i]["movie_title"] +
      "</a>" +
      "</th>";
    rowHTML += "<th>" + resultData[i]["movie_year"] + "</th>";
    rowHTML += "<th>" + resultData[i]["movie_director"] + "</th>";
    rowHTML += "<th>" + resultData[i]["movie_genre"] + "</th>";

    // Need a way to split this

    rowHTML += "<th>";
    let starIds = resultData[i]["star_id"];
    let actorIdArr = starIds.split(",");
    let movieActors = resultData[i]["movie_actors"];
    let movieActorArr = movieActors.split(",");

    for (let j in actorIdArr) {
      rowHTML +=
        "<p>" +
        // Adding a link to the movie
        '<a href="star.html?id=' +
        actorIdArr[j] +
        '">' +
        movieActorArr[j] +
        "</a>" +
        "</p>";
    }
    rowHTML += "</th>";
    rowHTML += "<th>" + resultData[i]["movie_rating"] + "</th>";

    rowHTML +=
      "<th>" +
      "<p>" +
      '<a href="placeOrder.html?id=' +
      resultData[i]["movie_id"] +
      '">' +
      "Add To Cart" +
      "</a>" +
      "</p>" +
      "</th>";
    rowHTML += "</tr>";

    // Append the row created to the table body, which will refresh the page
    starTableBodyElement.append(rowHTML);
  }
}

/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */
// Get id from URL
let titleStart = getParameterByName("titleStart");

// Makes the HTTP GET request and registers on success callback function handleMovieResult
jQuery.ajax({
  dataType: "json", // Setting return data type
  method: "GET", // Setting request method
  url: "titleSearch?titleStart=" + titleStart, // Setting request url, which is mapped by MovieListServlet in MovieListServlet.java
  success: (resultData) => handleMovieResult(resultData), // Setting callback function to handle data returned successfully by the MovieListServlet
});
