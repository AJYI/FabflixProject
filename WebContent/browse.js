





let movieLetter = getParameterByName("id");

jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/movieList?id =" + movieLetter, // Setting request url, which is mapped by MovieListServlet in MovieListServlet.java
    success: (resultData) => handleMovieResult(resultData), // Setting callback function to handle data returned successfully by the MovieListServlet
});