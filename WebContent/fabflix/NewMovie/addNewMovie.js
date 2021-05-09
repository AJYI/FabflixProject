let submit_form = $("#AddMovieSubmitForm");

function handleEmployeeResult(resultDataString) {
    let resultDataJson = resultDataString;

    console.log("handle login response");
    console.log(resultDataJson);
    console.log(resultDataJson["status"]);

    // If login succeeds, it will redirect the user to index.html
    if (resultDataJson["status"] === "success") {
        alert("Successfully added to the database\n" +
        "MovieID: " + resultDataJson["m_id"] + "\n" +
        "GenreID: " + resultDataJson["g_id"] + "\n" +
        "StarID: " + resultDataJson["s_id"] + "\n");
    }
    // Error Message
    else if (resultDataJson["status"] === "fail"){
        alert(resultDataJson["message"]);
    }
    else {
        // If login fails, the web page will display
        // error messages on <div> with id "login_error_message"
        console.log("show error message");
        console.log(resultDataJson["message"]);
        alert(resultDataJson["message"]);
    }
    window.location.replace("./addNewMovie.html");
}

/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function submitForm(formSubmitEvent) {
    console.log("submit login form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */

    let movieTitle = document.forms["AddMovieSubmitForm"]["movieTitle"].value;
    let directorName = document.forms["AddMovieSubmitForm"]["directorName"].value;
    let movieYear = document.forms["AddMovieSubmitForm"]["movieYear"].value;
    let genre = document.forms["AddMovieSubmitForm"]["genre"].value;
    let star = document.forms["AddMovieSubmitForm"]["star"].value;

    if (movieTitle == "") {
        alert("MOVIE TITLE FIELD MUST NOT BE EMPTY");
        window.location.replace("./addNewMovie.html");
    }
    else if (directorName == "") {
        alert("DIRECTOR NAME FIELD MUST NOT BE EMPTY");
        window.location.replace("./addNewMovie.html");
    }
    else if (movieYear == "") {
        alert("MOVIE YEAR FIELD MUST NOT BE EMPTY");
        window.location.replace("./addNewMovie.html");
    }
    else if (genre == "") {
        alert("GENRE FIELD MUST NOT BE EMPTY");
        window.location.replace("./addNewMovie.html");
    }
    else if (star == "") {
        alert("STAR FIELD MUST NOT BE EMPTY");
        window.location.replace("./addNewMovie.html");
    }

    formSubmitEvent.preventDefault();
    $.ajax(
        "./addMovie", {
            method: "GET",
            // Serialize the login form to the data sent by POST request
            data: submit_form.serialize(),
            success: handleEmployeeResult
        }
    );
}

// Bind the submit action of the form to a handler function
submit_form.submit(submitForm);