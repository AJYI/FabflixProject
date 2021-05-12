let submit_form = $("#AddStarSubmitForm");

function handleEmployeeResult(resultDataString) {
  console.log("Reached here");
  let resultDataJson = resultDataString;

  console.log("handle login response");
  console.log(resultDataJson);
  console.log(resultDataJson["status"]);

  // If login succeeds, it will redirect the user to index.html
  if (resultDataJson["status"] === "success") {
    alert("Successfully added to the database\n" +
    "New starID: " + resultDataJson["id"]);
  } else {
    // If login fails, the web page will display
    // error messages on <div> with id "login_error_message"
    console.log("show error message");
    console.log(resultDataJson["message"]);
    alert("Unsuccessful add to the database. Incorrect format for year");
  }
  window.location.replace("./AddStar.html");
}

/**
* Submit the form content with POST method
* @param formSubmitEvent
*/
function submitForm(formSubmitEvent) {
  console.log("submit form!");
  /**
   * When users click the submit button, the browser will not direct
   * users to the url defined in HTML form. Instead, it will call this
   * event handler when the event is triggered.
   */

  let ActorName = document.forms["AddStarSubmitForm"]["ActorName"].value;
  if (ActorName == "") {
    alert("ACTOR NAME MUST NOT BE EMPTY");
    window.location.replace("./AddStar.html");
  }

  formSubmitEvent.preventDefault();
  $.ajax(
      "./addStar", {
        method: "GET",
        // Serialize the login form to the data sent by POST request
        data: submit_form.serialize(),
        success: handleEmployeeResult
      }
  );
}

// Bind the submit action of the form to a handler function
submit_form.submit(submitForm);