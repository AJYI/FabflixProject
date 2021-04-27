/**
 * This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs three steps:
 *      1. Get parameter from request URL so it know which id to look for
 *      2. Use jQuery to talk to backend API to get the json data.
 *      3. Populate the data to correct html elements.
 */

/**
 * Retrieve parameter from request URL, matching by parameter name
 * @param target String
 * @returns {*}
 */
function getParameterByName(target) {
  // Get request URL
  let url = window.location.href;
  // Encode target parameter name to url encoding
  target = target.replace(/[\[\]]/g, "\\$&");

  // Ues regular expression to find matched parameter value
  let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
    results = regex.exec(url);
  if (!results) return null;
  if (!results[2]) return "";

  // Return the decoded parameter value
  return decodeURIComponent(results[2].replace(/\+/g, " "));
}

/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */

function handleResult(resultData) {
  console.log("handleResult: populating star info from resultData");
  // Setting the page title:
  let moviePageTitle = jQuery("#movie_title_page");
  moviePageTitle.append(resultData[0]["movie_title"]);

  // Setting the title:
  let movieTitle = jQuery("#movie_title");
  movieTitle.append(resultData[0]["movie_title"]);

  // Creating the row
  let movieBody = jQuery("#movie_table_body");
  let rowHTML = "";
  rowHTML += "<tr>";
  rowHTML +=
    "<th>" +
    resultData[0]["movie_year"] +
    "</th>" +
    "<th>" +
    resultData[0]["movie_director"] +
    "</th>"; //+
    // "<th>" +
    // resultData[0]["movie_genres"] +
    // "</th>";

  // Need a way to split this
  rowHTML += "<th>";
  let movGen = resultData[0]["movie_genres"];
  let movArr = movGen.split(",");

  for (let j in movArr){
    rowHTML +=
        "<p>" +
        '<a href="genreSearch.html?genre=' + movArr[j] + '">' + movArr[j] + "</a>" + "</p>";
  }
  rowHTML += "</th>";

  //Since query has been altered, changes have been made here.

  //Stars array
  let stars = resultData[0]["movie_actors"];
  let starsArr = stars.split(",");

  //StarsID array
  let starsID = resultData[0]["movie_star_ids"];
  let starIdArr = starsID.split(",");
  rowHTML += "<th>";
  for (let i in starsArr) {
    rowHTML +=
      "<p>" +
      '<a href="star.html?id=' +
      starIdArr[i] +
      '">' +
      starsArr[i] +
      "</a>" +
      "</p>";
  }
  rowHTML += "</th>";

  if (resultData[0]["movie_rating"] == null) {
    resultData[0]["movie_rating"] = "No Rating";
  }
  rowHTML += "<th>" + resultData[0]["movie_rating"] + "</th>" + "</tr>";

  rowHTML += "<th>" +
      "<p>" +
      '<a href="placeOrder.html?id='
      + resultData[i]["movie_id"]
      + '">'
      + "Add To Cart"
      + "</a>"
      + "</p>"
      + "</th>";

  movieBody.append(rowHTML);
}

/**
 * Once this .js is loaded, following scripts will be executed by the browser\
 */

// Get id from URL
let movieId = getParameterByName("id");

// Makes the HTTP GET request and registers on success callback function handleResult
jQuery.ajax({
  dataType: "json", // Setting return data type
  method: "GET", // Setting request method
  url: "api/movie?id=" + movieId, // Setting request url, which is mapped by StarsServlet in Stars.java
  success: (resultData) => handleResult(resultData), // Setting callback function to handle data returned successfully by the SingleStarServlet
});
