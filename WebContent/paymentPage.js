let credit_card_form = $("#credit_card");
/**
 * Handle the data returned by LoginServlet
 * @param resultDataString jsonObject
 */
function handleCCResult(resultDataString) {
    let resultDataJson = JSON.parse(resultDataString);

    console.log("handle credit card response");
    console.log(resultDataJson);
    console.log(resultDataJson["status"]);

    // If login succeeds, it will redirect the user to confirmation page
    if (resultDataJson["status"] === "success") {
        window.location.replace("confirmationPage.html"); // Replace with confirmation page
    } else {
        // If login fails, the web page will display
        // error messages on <div> with id "login_error_message"
        console.log("show error message");
        console.log(resultDataJson["message"]);
        $("#error").text(resultDataJson["message"]);
    }
}

/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function submitCCForm(formSubmitEvent) {
    console.log("submit credit card info form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    formSubmitEvent.preventDefault();

    $.ajax(
        "api/payment", {
            method: "POST",
            // Serialize the login form to the data sent by POST request
            data: credit_card_form.serialize(),
            success: handleCCResult
        }
    );
}

// Bind the submit action of the form to a handler function
credit_card_form.submit(submitCCForm);