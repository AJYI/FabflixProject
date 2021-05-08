/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */
function handleMovieResult(resultData) {
  console.log("handleStarResult: populating star table from resultData");

  // Populate the star table
  // Find the empty table body by id "star_table_body"
  let starTableBodyElement = jQuery("#tables");
  let rowHTML = "";
  for (let i = 0; i < resultData.length; i++){
    rowHTML += '<table border class = "tableClass">';
    //console.log(Object.keys(resultData[i]).length);
    for (let j = 0; j < Object.keys(resultData[i]).length; j++){
          rowHTML += "<tr>";
          rowHTML += "<th>";
          rowHTML += resultData[i][""+j];
          rowHTML += "</th>";
          rowHTML += "</tr>";
    }
    rowHTML += "</table>";
  }
  starTableBodyElement.append(rowHTML);
}

/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */
jQuery.ajax({
  dataType: "json", // Setting return data type
  method: "GET", // Setting request method
  url: "./metadata", // Setting request url, which is mapped by Metadata servlet
  success: (resultData) => handleMovieResult(resultData), // Setting callback function to handle data returned successfully by the Metadata servlet
});
