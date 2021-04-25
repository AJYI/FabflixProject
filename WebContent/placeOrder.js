



let movieId = getParameterByName(movieId);

// Makes the HTTP GET request and registers on success callback function handleMovieResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "POST", // Setting request method
    url: "placeOrder.html?id=" + movieId, // Setting request url, which is mapped by MovieListServlet in MovieListServlet.java
    success: (resultData) => handleMovieResult(resultData), // Setting callback function to handle data returned successfully by the MovieListServlet
});